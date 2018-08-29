package ping.otmsapp.zeroCIce;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

import cn.hy.otms.rpcproxy.appInterface.AppInterfaceServicePrx;
import cn.hy.otms.rpcproxy.appInterface.AppSchedvech;
import cn.hy.otms.rpcproxy.appInterface.DispatchInfo;
import cn.hy.otms.rpcproxy.appInterface.WarnsInfo;
import cn.hy.otms.rpcproxy.comm.cstruct.BoolMessage;
import cn.hy.otms.rpcproxy.sysmanage.SysManageServicePrx;
import cn.hy.otms.rpcproxy.sysmanage.UpdateRequestPackage;
import cn.hy.otms.rpcproxy.sysmanage.UpdateResponsePackage;
import cn.hy.otms.rpcproxy.sysmanage.UserGlobalInfo;
import ping.otmsapp.entitys.background.IOThreadPool;
import ping.otmsapp.entitys.dataBeans.dispatch.AbnormalBean;
import ping.otmsapp.entitys.dataBeans.dispatch.VehicleInfoBean;
import ping.otmsapp.entitys.maps.location.TraceRecodeBean;
import ping.otmsapp.utils.AppUtil;
import ping.otmsapp.utils.Ms;

import static ping.otmsapp.utils.RecodeTraceState.RECODE_ING;

/**
 * Created by lzp on 2018/3/7.
 * ICE访问线程池
 */

public class IceIo {
    //是否打印信息
    private final boolean isInfoPrint = true;
    //是否打印错误
    private final boolean isErrorPrint = true;

    private IceIo(){}



    private static class Holder{
        private static IceIo INSTANCE = new IceIo();
    }

    public IOThreadPool pool = new IOThreadPool();;
    public static IceIo get(){
        return Holder.INSTANCE;
    }

    private IceClient iceClient = new IceClient();

    private Context context;

    private String serverInfo = "unknown";

    public void create(Context context){
        if (this.context==null){
            this.context = context;
            iceClient.setArgs(readServerInfo());
        }
    }

    private String[] readServerInfo() {
        //读取配置文件
        String iceTag = "LBXTMS/Locator";
        String host = "192.168.1.120";
        String port = "4061";
        try {
            File externalFilesDir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
            assert externalFilesDir!=null;
            String configFileName = "server.config";
            String path = externalFilesDir.toString()+ File.separator +configFileName;
            File file = new File(path);
            if (!file.exists()){
               if (file.createNewFile()){
                   FileOutputStream fos=new FileOutputStream(file);
                   fos.write(host.getBytes());
                   fos.write("\n".getBytes());
                   fos.write(port.getBytes());
                   fos.write("\n".getBytes());
                   fos.close();
               }
            }else{
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                host = br.readLine();
                port = br.readLine();
                br.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        serverInfo = iceTag+":"+host+":"+port;
        return new String[]{
                String.format(Locale.CHINA,"--Ice.Default.Locator=%s:tcp -h %s -p %s",iceTag,host,port),
                "--Ice.MessageSizeMax=4096"
        };
    }

    public void destroy(){
        this.context = null;
        iceClient.close();
        pool.close();
    }

    private String[] convert(Object... objects){
        String[] strings = new String[objects.length];
        String temp;
        for (int i = 0;i<objects.length;i++){
            temp = String.valueOf(objects[i]) ;
            strings[i] = temp==null || temp.equals("null")?"":temp;
        }
        return strings;
    }

    private void checkNetwork() {
        if (context==null)throw new IllegalStateException("context is null");
        if (!AppUtil.isNetworkAvailable(context)) throw new IllegalStateException("network is fail");
    }

    private void println(String... str){
        StringBuffer stringBuffer = new StringBuffer(Thread.currentThread()+" ,"+serverInfo+"\t");
        for (String s: str){
            stringBuffer.append(" ,").append(s);
        }

        Ms.Holder.get().debug(isInfoPrint, stringBuffer);
    }


    /**
     *  用户登陆
     */
    public UserGlobalInfo login(final String phone, final String password){
        try {
            println("用户登陆", phone, password);
            checkNetwork();
            SysManageServicePrx smsPrx = iceClient.getServicePrx(SysManageServicePrx.class);
            assert smsPrx != null;
            return smsPrx.loginCS(phone, password);
        }catch(Exception e){
             Ms.Holder.get().error(isErrorPrint,e);
        }
        return null;
    }

    /**
     * 轨迹信息传送
     * 车牌,车次,轨迹记录对象
     */
    public int addTrail(TraceRecodeBean traceRecode){
        try{
            VehicleInfoBean vehicleInfoBean = traceRecode.getVehicleInfoBean();
            println("传送轨迹","车次号:"+vehicleInfoBean.getCarNumber(),"轨迹点数:" + traceRecode.getFlag(),"本地状态码:"+traceRecode.getRecodeState() );
            checkNetwork();
            String traceListStr = AppUtil.javaBeanToJson(traceRecode.getFiltrationList());
            int state = traceRecode.getRecodeState() == RECODE_ING ? 2 : 4; //2.实时 4.历史(完成)
            AppInterfaceServicePrx aisPrx = iceClient.getServicePrx(AppInterfaceServicePrx.class);
            //车次号,车牌号,轨迹点数,轨迹json,状态码
            assert aisPrx != null;
            return aisPrx.addTrail(convert(
                    vehicleInfoBean.getCarNumber(),
                    vehicleInfoBean.getVehicleCode(),
                    traceRecode.getFlag(),
                    traceListStr,
                    state
                    ));
        }catch (Exception e){
            Ms.Holder.get().error(isErrorPrint,e);
        }
        return  -1;
    }

    /**
     * 获取调度信息
     */
    public DispatchInfo dispatchInfoSynch(final String userid,final String schedth){
        try{
            println("获取调度单",userid,schedth);
            checkNetwork();
            AppInterfaceServicePrx aisPrx= iceClient.getServicePrx(AppInterfaceServicePrx.class);
            //用户码
            assert aisPrx != null;
            return aisPrx.heartbeatByDriverC(convert(userid,schedth));
        }catch (Exception e){
            Ms.Holder.get().error(isErrorPrint,e);
        }
        return null;
    }


    /**
     * 更改调度单中某订单对应的箱子信息
     * 状态（0未装卸，1已装，2已卸）
     */
    public BoolMessage changeBoxStateSynch(long schedth,final String cusid, String boxNo, int state,long time){
            try{
                println("改变箱子状态",boxNo,state+"");
                checkNetwork();
                AppInterfaceServicePrx aisPrx= iceClient.getServicePrx(AppInterfaceServicePrx.class);
                //状态,箱号,调度车次,机构码, 装载或者卸货对应的时间
                assert aisPrx != null;
                return aisPrx.updateBoxStatus(convert(
                        state,
                        boxNo,
                        schedth,
                        cusid,
                        time));
            }catch (Exception e){
                Ms.Holder.get().error(isErrorPrint,e);
            }
        return null;
    }

    /**
     * 修改调度单状态
     */
    public BoolMessage changeDispatchStateSynch(final long schedtn,final String userid, final int state,final long time){
            try{
                println("改变调度状态",schedtn+"单号",userid+"用户ID","状态"+state);
                checkNetwork();
                AppInterfaceServicePrx aisPrx= iceClient.getServicePrx(AppInterfaceServicePrx.class);
                //车次号,用户码,状态,修改时间
                assert aisPrx != null;
                return aisPrx.updateSchedvechStatus(convert(schedtn,userid,state,time));
            }catch (Exception e){
                Ms.Holder.get().error(isErrorPrint,e);
            }
        return null;
    }

    /**
     * 改变车辆状态
     */
    public BoolMessage changeVehicleStateSynch(final long schedtn,final String vechid,final int status){
            try{
                println("改变车辆状态",vechid,status+"");
                checkNetwork();
                AppInterfaceServicePrx aisPrx= iceClient.getServicePrx(AppInterfaceServicePrx.class);
                //车次号,车牌号,状态
                assert aisPrx != null;
                return aisPrx.updateVehStatus(convert(schedtn,vechid,status));
            }catch (Exception e){
                Ms.Holder.get().error(isErrorPrint,e);
            }
        return null;
    }

    /**
     * 设置
     */
    public BoolMessage setGpsMileageSynch(final long schedtn,final String cusid,final int rp,final int mileage){
        try{
            println("设置门店路顺和里程数",schedtn+"",cusid,"实际顺序:"+rp,mileage+"km");
            checkNetwork();
            AppInterfaceServicePrx aisPrx= iceClient.getServicePrx(AppInterfaceServicePrx.class);
            //车次号,客户机构码,实际路顺,实际里程数
            assert aisPrx != null;
            return aisPrx.addGpsRp(convert(schedtn,cusid,rp,mileage));
        }catch (Exception e){
            Ms.Holder.get().error(isErrorPrint,e);
        }
        return null;
    }

    /**
     * 修改回收箱
     */
    public BoolMessage updateRecycleBoxSynch(final long schedtn,final String usedid,final String lpn,final int recycleType,final long time,final String cusid){
            try{
                println("回收",schedtn+"",cusid,usedid+"","[ "+lpn+" ]",recycleType+"");
                checkNetwork();
                AppInterfaceServicePrx aisPrx= iceClient.getServicePrx(AppInterfaceServicePrx.class);
                //调度车次,用户码,箱号,回收类型,回收时间
                assert aisPrx != null;
                return aisPrx.updateRecycle(convert(
                        schedtn,
                        usedid,
                        lpn,
                        recycleType,
                        time,
                        cusid
                ));
            }catch (Exception e){
                Ms.Holder.get().error(isErrorPrint,e);
            }
        return null;
    }

    /**
     * 回收箱子数量
     * 调度车次,门店编号,调剂个数,退货个数,回收时间
     */
    public BoolMessage updateRecycleBoxNumberSynch(final long schedtn,final String cusid,final int backTypeNum,final int transferTypeNumber,final long time) {
        try{
            println("回收-纸箱数量",schedtn+"",cusid,backTypeNum+"",transferTypeNumber+"",time+"");
            checkNetwork();
            AppInterfaceServicePrx aisPrx= iceClient.getServicePrx(AppInterfaceServicePrx.class);
            //调度车次,用户码,箱号,回收类型,回收时间
            assert aisPrx != null;
            return aisPrx.grade(convert(
                    schedtn,
                    cusid,
                    backTypeNum,
                    transferTypeNumber,
                    time
            ));
        }catch (Exception e){
            Ms.Holder.get().error(isErrorPrint,e);
        }
        return null;
    }
    /**
     * 异常反馈
     */
    public BoolMessage addAbnormal(AbnormalBean abnormalBean){
            try{
                println("添加箱子异常信息"+abnormalBean.getAbnormalBoxNumber()+" "+abnormalBean.getAbnormalRemakes());
                checkNetwork();
                AppInterfaceServicePrx aisPrx= iceClient.getServicePrx(AppInterfaceServicePrx.class);
                //异常发生人用户码,异常车次,异常机构码,异常箱号,异常类型,发生异常时间,异常说明
                //异常处理人用户码,异常处理机构码,异常处理时间,异常处理说明
                assert aisPrx != null;
                return aisPrx.changeAbnormal(convert(
                        abnormalBean.getAbnormalUserCode(),abnormalBean.getCarNumber(),abnormalBean.getAbnormalCustomerAgency(),abnormalBean.getAbnormalBoxNumber(),abnormalBean.getAbnormalType(),abnormalBean.getAbnormalTime(),abnormalBean.getAbnormalRemakes(),
                        abnormalBean.getHandlerUserCode(), abnormalBean.getHandleCustomerAgency(),abnormalBean.getHandlerTime(),abnormalBean.getHandlerRemakes()
                ));
            }catch (Exception e){
                Ms.Holder.get().error(isErrorPrint,e);
            }
        return null;
    }

    /**
     * 获取历史任务信息
     */
    public AppSchedvech[] getHistoryTask(final String userid, final String year_mon){
        try {
            println("获取当前用户历史任务",userid+"",year_mon);
            checkNetwork();
            AppInterfaceServicePrx aisPrx= iceClient.getServicePrx(AppInterfaceServicePrx.class);
            //用户码,年月(如201803)
            assert aisPrx != null;
            return  aisPrx.queryVechInfoPage(convert(userid,year_mon));
        } catch (Exception e) {
            Ms.Holder.get().error(isErrorPrint,e);
        }
        return null;
    }


    /**
     * 司机在途 查询预警信息
     */
    public WarnsInfo queryTimeLaterWarnInfoByDriver(long roleCode, long trainNo, long time) {
        try {
        println("请求预警数据",roleCode+"",trainNo+"",time+"");
        AppInterfaceServicePrx aisPrx = iceClient.getServicePrx(AppInterfaceServicePrx.class);
            assert aisPrx != null;
            return aisPrx.queryWarnsInfo(convert(
                "TimeLater",
                roleCode,
                time,
                trainNo
        ));
        } catch (Exception e) {
            Ms.Holder.get().error(isErrorPrint,e);
        }
        return null;
    }

    /**
     * 获取文件
     */
    public UpdateResponsePackage getDataBytes(String remoteFileName, int index){

        try {
            println("获取数据流", remoteFileName, index+"");
            checkNetwork();
            UpdateRequestPackage updateRequestPackage = new UpdateRequestPackage();
            updateRequestPackage.index = index; //第一次请求写1
            updateRequestPackage.size = 1024*1024;//返回块大小 0为整包返回
            updateRequestPackage.filePath = remoteFileName;//返回文件
            updateRequestPackage.OS = 2;
            SysManageServicePrx smsPrx = iceClient.getServicePrx(SysManageServicePrx.class);
            assert smsPrx != null;
            return smsPrx.getVersionPackage(updateRequestPackage);
        }catch(Exception e){
            Ms.Holder.get().error(isErrorPrint,e);
        }
        return null;
    }

    /**
     * 修改预警是否处理
     */
    public boolean changeWarnState(String boxNo,long time){
        try {
            println("修改预警状态", boxNo, time+"");
            checkNetwork();
            AppInterfaceServicePrx aisPrx = iceClient.getServicePrx(AppInterfaceServicePrx.class);
            assert aisPrx != null;
            return aisPrx.updateWarnState(convert(boxNo,time));
        }catch(Exception e){
            Ms.Holder.get().error(isErrorPrint,e);
        }
        return false;
    }


}
