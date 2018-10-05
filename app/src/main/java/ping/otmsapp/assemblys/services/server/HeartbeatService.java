package ping.otmsapp.assemblys.services.server;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import java.util.Collections;
import java.util.Comparator;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import cn.hy.otms.rpcproxy.appInterface.DispatchInfo;
import cn.hy.otms.rpcproxy.appInterface.WarnsInfo;
import cn.hy.otms.rpcproxy.comm.cstruct.BoolMessage;
import ping.otmsapp.assemblys.activitys.Index;
import ping.otmsapp.assemblys.services.connect.BinderAbs;
import ping.otmsapp.entitys.apps.NotificationBean;
import ping.otmsapp.entitys.apps.PowerBean;
import ping.otmsapp.entitys.dataBeans.dispatch.AbnormalBean;
import ping.otmsapp.entitys.dataBeans.dispatch.AbnormalListBean;
import ping.otmsapp.entitys.dataBeans.dispatch.BoxBean;
import ping.otmsapp.entitys.dataBeans.dispatch.DispatchBean;
import ping.otmsapp.entitys.dataBeans.dispatch.DispatchBeanRemoteState;
import ping.otmsapp.entitys.dataBeans.dispatch.DistributionPathBean;
import ping.otmsapp.entitys.dataBeans.dispatch.PathRouter;
import ping.otmsapp.entitys.dataBeans.dispatch.RecycleBoxBean;
import ping.otmsapp.entitys.dataBeans.dispatch.RecycleBoxListBean;
import ping.otmsapp.entitys.dataBeans.login.LoginUserBean;
import ping.otmsapp.entitys.dataBeans.tuples.Tuple2;
import ping.otmsapp.entitys.dataBeans.warn.WarnState;
import ping.otmsapp.entitys.dataBeans.warn.WarnTag;
import ping.otmsapp.entitys.interfaces.OnHeartbeatMessageCallback;
import ping.otmsapp.entitys.maps.location.TraceRecodeBean;
import ping.otmsapp.entitys.maps.location.TraceRecodeBeanRemoteState;
import ping.otmsapp.utils.AppUtil;
import ping.otmsapp.utils.Convert;
import ping.otmsapp.utils.Ms;
import ping.otmsapp.zeroCIce.IceIo;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static ping.otmsapp.utils.KEY.BUNDLE_KEY_DATA;
import static ping.otmsapp.utils.KEY.BUNDLE_KEY_TYPE;
import static ping.otmsapp.utils.KEY.INTENT_KEY_BUNDLE;
import static ping.otmsapp.utils.KEY.TYPE_BY_CLEAR_DISPATCH;
import static ping.otmsapp.utils.KEY.TYPE_BY_DISPATCH;
import static ping.otmsapp.utils.KEY.TYPE_BY_OPEN_GPS;
import static ping.otmsapp.utils.KEY.TYPE_BY_WARN;
import static ping.otmsapp.utils.RecodeTraceState.RECODE_FINISH;
import static ping.otmsapp.utils.STATE.BOX_DEAL_LOAD;
import static ping.otmsapp.utils.STATE.BOX_DEAL_RECYCLE;
import static ping.otmsapp.utils.STATE.BOX_DEAL_UNLOAD;
import static ping.otmsapp.utils.STATE.DISPATCH_DEAL_BACK;
import static ping.otmsapp.utils.STATE.DISPATCH_DEAL_COMPLETE;
import static ping.otmsapp.utils.STATE.DISPATCH_DEAL_LOAD;
import static ping.otmsapp.utils.STATE.DISPATCH_DEAL_TAKEOUT;
import static ping.otmsapp.utils.STATE.DISPATCH_DEAL_UNLOAD;
import static ping.otmsapp.utils.STATE.STORE_DEAL_COMPLETE;
import static ping.otmsapp.utils.STATE.STORE_DEAL_LOAD;
import static ping.otmsapp.utils.STATE.STORE_DEAL_UNLOAD;

/**
 * Created by lzp on 2018/3/20.
 *  心跳获取调度信息
 */

public class HeartbeatService extends Service {

    private PendingIntent pendingIntentOp; //闹钟使用
    private PowerBean powerBean; //电源管理
    private NotificationBean notificationBean;//通知栏
    private OnHeartbeatMessageCallback callback;

    @Override
    public void onCreate() {
        powerBean = new PowerBean(getApplicationContext(),getClass().getSimpleName());
        notificationBean = new NotificationBean(
                getApplicationContext(),
                2,
                null,
                "android.intent.action.INDEX",
                "otmsapp://localhost:9999",
                "心跳服务",
                "正在后台运行",
                "点击进入应用",
                Notification.DEFAULT_LIGHTS
        );
        notificationBean.showNotification();
    }
    //检测ice后台返回的诗句有效性
    private boolean checkBoolMessage(BoolMessage boolMessage) throws IllegalStateException{
        if (boolMessage==null) return false;
        if (!boolMessage.flag){
            Ms.Holder.get().printObject(boolMessage);
        }
        return boolMessage.flag;
    }
    //用于activity实现双向通讯
    public class MBinder extends BinderAbs {
        public void setHeartbeatMessageCallback(OnHeartbeatMessageCallback callback){
            HeartbeatService.this.callback = callback;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return new HeartbeatService.MBinder();
    }
    @Override
    public boolean onUnbind(Intent intent) {
        callback = null;
        return true;
    }
    /**
     * 发信息到主页面
     */
    private void sendIndexActivity(String type,String data) {
        Bundle b = new Bundle();
        b.putString(BUNDLE_KEY_TYPE,type);
        if (data!=null){
            b.putString(BUNDLE_KEY_DATA, data);
        }
        if (callback!=null){
            callback.onBundleCallback(b);
        }else{
            Intent mIntent = new Intent(getApplicationContext(), Index.class);
            mIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            mIntent.putExtra(INTENT_KEY_BUNDLE,b);
            startActivity(mIntent);
        }
    }

    //获取下一次间隔的时间
    private long getNextTime() {
        long now = System.currentTimeMillis();
        return now + 5 * 1000;
    }

    /**
     * 打开后台心跳服务闹钟
     am.setRepeating(AlarmManager.RTC_WAKEUP , System.currentTimeMillis(), INTERVAL_TIME, pendingIntentOp);
     am.setWindow(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), 20000, pendingIntentOp);
     */
    public void startAlarmManagerHeartbeat() {
        if (pendingIntentOp==null){
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), HeartbeatService.class);
            pendingIntentOp = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
        }
        AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT>=19) am.setExact(AlarmManager.RTC_WAKEUP, getNextTime(),  pendingIntentOp);
        else am.set(AlarmManager.RTC_WAKEUP, getNextTime(),  pendingIntentOp);
    }

    //停止闹钟
    public void stopAlarmManagerHeartbeat(){
        AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntentOp);
    }

    //销毁
    @Override
    public void onDestroy() {
        notificationBean.cancelNotification();
        powerBean.stopPowerWakeLock();
        stopAlarmManagerHeartbeat();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        execute();
        return super.onStartCommand(intent, flags, startId);
    }

    //执行
    private void execute() {
        powerBean.startPowerWakeLockByCPU();
        //如果不在执行中
        IceIo.get().pool.post(new Runnable() {
            @Override
            public void run() {
                if (!AppUtil.isOenGPS(getApplicationContext())){
                    //通知信息到activity
                    sendIndexActivity(TYPE_BY_OPEN_GPS, null);
                }
                if (AppUtil.isNetworkAvailable(getApplicationContext())){
                    try {


                        synchToServer();
                        handleDispatch();
                    } catch (Exception e) {
                        Ms.Holder.get().error(e);;
                    }
                }
                startAlarmManagerHeartbeat();//开始闹钟
                powerBean.stopPowerWakeLock();
            }
        });
    }

    // 获取调度信息或者同步调度信息 - 间隔触发
    private void handleDispatch() {
        //获取用户信息
        LoginUserBean userBean = new LoginUserBean().fetch();
        if (userBean == null || !userBean.isAccess())  return;
        String traceNo = "";
        //获取本地调度信息
        DispatchBean dispatchBean = new DispatchBean().fetch();
        if (dispatchBean!=null ){
            if (dispatchBean.getState() <= DISPATCH_DEAL_TAKEOUT){
                //在装货中或者等待启程中,获取本地调度任务车次号
                traceNo = String.valueOf(dispatchBean.getVehicleInfoBean().getCarNumber());
            }else{
                return;
            }
        }
        //获取远程调度信息 如果没有车次,获取新车次任务;
        // 如果有车次,通知后台收到任务
        dispatchInfoPause(IceIo.get().dispatchInfoSynch(userBean.getUserCode(),traceNo));

    }

    //调度信息解析
    public void dispatchInfoPause(DispatchInfo result) {
        if (result==null) return;

        if (result.flag == -1) return; //-1 没有修改
        if (result.flag==-10) {  //-10 强制删除当前任务
            forceDelete();
            return;
        }
        if (result.dispatchSchedvech.driverc == 0) return;
            Ms.Holder.get().printObject(result);
            Tuple2<DispatchBean,DispatchBeanRemoteState> tuple2  = Convert.dispatchTrans(result);//转换信息
            DispatchBean dispatchBean = tuple2.getValue0();
            DispatchBeanRemoteState dispatchBeanRemoteState = tuple2.getValue1();
            //本地存储调度信息
            dispatchBean.save();
            //本地存储调度信息-远程同步状态
            dispatchBeanRemoteState.save();
            //本地异常队列
            AbnormalListBean abnormalListBean = new AbnormalListBean();
            abnormalListBean.setVehicleInfoBean(dispatchBean.getVehicleInfoBean());
            abnormalListBean.save();
            //本地回收队列
            RecycleBoxListBean recycleBoxListBean = new RecycleBoxListBean();
            recycleBoxListBean.setVehicleInfoBean(dispatchBean.getVehicleInfoBean());
            recycleBoxListBean.save();
            //预警信息标识
            WarnTag warnTag = new WarnTag();
            warnTag.setVehicleInfoBean(dispatchBean.getVehicleInfoBean());
            warnTag.save();
            //本地存储轨迹记录对象
            TraceRecodeBean traceRecode = new TraceRecodeBean();//轨迹记录
            traceRecode.setVehicleInfoBean(dispatchBean.getVehicleInfoBean());
            traceRecode.save();
            TraceRecodeBeanRemoteState traceRecodeBeanRemoteState = new TraceRecodeBeanRemoteState().create(traceRecode.getFlag(),dispatchBean.getVehicleInfoBean());
            //本地存储轨迹记录同步对象
            traceRecodeBeanRemoteState.save();
            //计算路顺->已转移后台计算
//            planRoute(dispatchBean);
            //通知信息到activity
            sendIndexActivity(TYPE_BY_DISPATCH, null);

    }

    //用于同步数据
    private void synchToServer() {
            LoginUserBean userBean = new LoginUserBean().fetch();
            if (userBean == null || !userBean.isAccess())  return;
            //计算路径
//           planRoute(null);
            //获取预警
            //getWarn();
            //轨迹是否需要同步
            synchTraceRecode();
            //异常同步
            synchAbnormal();
            //回收箱同步
            synchRecycle();
            //调度信息同步
            synchDispatchRecode();
    }

    //计算路径
    private void planRoute(DispatchBean dispatchBean) {
        try {
            if (dispatchBean==null) dispatchBean = new DispatchBean().fetch();
            if (dispatchBean!=null && !dispatchBean.isCalculationPath()){
                String resultInfo = new PathRouter(dispatchBean).execute();
                Ms.Holder.get().debug(resultInfo);
                dispatchBean.setCalculationPath(true);
                //对门店列表按照 设置路顺 排序
                Collections.sort(dispatchBean.getDistributionPathBean(), new Comparator<DistributionPathBean>() {
                    @Override
                    public int compare(DistributionPathBean o1, DistributionPathBean o2) {
                        return o1.getSpecifiedOrder() - o2.getSpecifiedOrder(); //升序排序
                    }
                });
                dispatchBean.save();
            }
        }catch (Exception ignored){}
    }

    /**
     * 同步异常信息
     */
    private void synchAbnormal() {
        AbnormalListBean abnormalListBean = new AbnormalListBean().fetch();
        if (abnormalListBean!=null){
            for (AbnormalBean abnormalBean :abnormalListBean.getList()){
                if (abnormalBean.isSynch()){
                    BoolMessage boolMessage = IceIo.get().addAbnormal(abnormalBean);
                    if (checkBoolMessage(boolMessage)){
                        abnormalBean.setSynch(false);
                        //写入信息
                        abnormalListBean.save();
                    }

                }
            }
        }
    }

    /**
     * 同步回收箱
     */
    @SuppressLint("UseSparseArrays")
    private void synchRecycle() {
        RecycleBoxListBean recycleBoxListBean = new RecycleBoxListBean().fetch();
        BoolMessage boolMessage;
        boolean isSave = false;
        if (recycleBoxListBean!=null){
            for (RecycleBoxBean recycleBoxBean : recycleBoxListBean.getList()){
                if (recycleBoxBean.isSynch() ){
                    if (!recycleBoxBean.isBlank()){ //不是空箱号
                        //确定同步
                        boolMessage = IceIo.get().updateRecycleBoxSynch(
                                recycleBoxBean.getCarNumber(),
                                recycleBoxBean.getUserCode(),
                                recycleBoxBean.getBoxNo(),
                                recycleBoxBean.getType(),
                                recycleBoxBean.getTime(),
                                recycleBoxBean.getStoreId()
                        );
                    }else{
                        boolMessage = IceIo.get().updateRecycleBoxNumberSynch(
                                recycleBoxBean.getCarNumber(),
                                recycleBoxBean.getStoreId(),
                                recycleBoxBean.getType() == 2?1:0,
                                recycleBoxBean.getType() == 3?1:0,
                                recycleBoxBean.getTime()
                                );
                    }
                    if (checkBoolMessage(boolMessage)){
                        recycleBoxBean.setSynch(false);
                        isSave = true;
                    }
                }
            }

            //写入信息
            if (isSave) recycleBoxListBean.save();
        }
    }

    /**
     * 同步本地轨迹与远程轨迹
     */
    private void synchTraceRecode() {
        TraceRecodeBean traceRecodeBean = new TraceRecodeBean().fetch();
        TraceRecodeBeanRemoteState traceRecodeBeanRemoteState = new TraceRecodeBeanRemoteState().fetch();
        if (traceRecodeBean!=null && traceRecodeBeanRemoteState!=null){
            if (traceRecodeBeanRemoteState.getTraceRecodeFlag() < traceRecodeBean.getFlag()){
                int i = IceIo.get().addTrail(traceRecodeBean);
                if (i>0){
                    //更新本地轨迹远程状态对象
                    traceRecodeBeanRemoteState.setTraceRecodeFlag(i);
                    traceRecodeBeanRemoteState.save();
                }
            }
        }
    }
    /**
     * 同步本地轨迹与远程轨迹
     */
    private boolean checkTraceRecodeFinish() {
        TraceRecodeBean traceRecodeBean = new TraceRecodeBean().fetch();
        if (traceRecodeBean!=null && traceRecodeBean.getRecodeState() == RECODE_FINISH){
                int i = IceIo.get().addTrail(traceRecodeBean);
                if ( (traceRecodeBean.getFlag() > 2 && i == traceRecodeBean.getFlag())|| traceRecodeBean.getFlag()==0){
                    return true;
                }

        }
        return false;
    }

    //同步调度信息
    private void synchDispatchRecode() {
        DispatchBean dispatchBean = new DispatchBean().fetch();
        if (dispatchBean!=null){
            //当前远程状态
            DispatchBeanRemoteState dispatchBeanRemoteState = new DispatchBeanRemoteState().fetch();
            if (dispatchBeanRemoteState!=null){
                boolean isChangeState = synchDispatch(dispatchBean,dispatchBeanRemoteState);
                //再次执行
                if (isChangeState) synchDispatchRecode();
            }
        }
    }
    //同步调度信息
    private boolean synchDispatch(DispatchBean dispatchBean, DispatchBeanRemoteState dispatchBeanRemoteState) throws IllegalStateException {
        //检查门店同步是否存在改变
        boolean isChangeState = synchStore(dispatchBean,dispatchBeanRemoteState);

        int remoteState = dispatchBeanRemoteState.getDispatchRemoteState();
        int localState = dispatchBean.getState();

        BoolMessage boolMessage;
        if (remoteState<localState ){ //发现调度信息存在改变
            if (remoteState == DISPATCH_DEAL_LOAD){
                //改变调度状态 -> 等待启程
                dispatchBeanRemoteState.setDispatchRemoteState(DISPATCH_DEAL_TAKEOUT);
            }else if (remoteState == DISPATCH_DEAL_TAKEOUT){
                //改变调度状态 -> 在途
                boolMessage = IceIo.get().changeDispatchStateSynch(dispatchBean.getVehicleInfoBean().getCarNumber(),
                        dispatchBean.getVehicleInfoBean().getDriverCode(),
                        3,
                        dispatchBean.getChangeTakeOutTime());
                if (checkBoolMessage(boolMessage)){
                    //改变车辆状态 -> 在途
                    boolMessage =IceIo.get().changeVehicleStateSynch(
                            dispatchBean.getVehicleInfoBean().getCarNumber(),
                            dispatchBean.getVehicleInfoBean().getVehicleCode(),
                            3);
                    if (checkBoolMessage(boolMessage)){
                        dispatchBeanRemoteState.setDispatchRemoteState(DISPATCH_DEAL_UNLOAD);
                        isChangeState=true;
                    }
                }
            }else if (remoteState == DISPATCH_DEAL_UNLOAD){
                boolMessage =  IceIo.get().changeDispatchStateSynch(dispatchBean.getVehicleInfoBean().getCarNumber(),
                        dispatchBean.getVehicleInfoBean().getDriverCode(),
                        4,
                        dispatchBean.getChangeUnloadStateTime()
                        );
                if (checkBoolMessage(boolMessage)){
                    dispatchBeanRemoteState.setDispatchRemoteState(DISPATCH_DEAL_BACK);
                    isChangeState=true;
                }
            }else if (remoteState==DISPATCH_DEAL_BACK){
               boolMessage = IceIo.get().changeVehicleStateSynch(dispatchBean.getVehicleInfoBean().getCarNumber(),
                        dispatchBean.getVehicleInfoBean().getVehicleCode(),
                        0);
               if (checkBoolMessage(boolMessage)){
                   dispatchBeanRemoteState.setDispatchRemoteState(DISPATCH_DEAL_COMPLETE);
                   isChangeState=true;
               }
            }
        }

        if (remoteState>localState){
            dispatchBeanRemoteState.setDispatchRemoteState(localState);//本地状态回滚
            isChangeState = true;
        }

        if (isChangeState){
            //保存更新后的远程状态
            dispatchBeanRemoteState.save();
        }

        if (remoteState == DISPATCH_DEAL_COMPLETE){ //调度单已完成
            //检测轨迹是否传输完毕;异常状态同步情况;回收箱同步情况
            if (checkTraceRecodeFinish() && checkAbnormalState() && checkRecycleBoxState()){
                forceDelete();
                isChangeState = false;
            }
        }

        return isChangeState;
    }
    //强制删除任务
    private void forceDelete() {
        //清理异常队列
        new AbnormalListBean().remove();
        //清理回收队列
        new RecycleBoxListBean().remove();
        //清理轨迹记录远程
        new TraceRecodeBeanRemoteState().remove();
        //清理轨迹记录
        new TraceRecodeBean().remove();
        //清理调度信息远程
        new DispatchBeanRemoteState().remove();
        //清理调度信息
        new DispatchBean().remove();
        //通知
        sendIndexActivity(TYPE_BY_CLEAR_DISPATCH,null);
    }

    //同步门店
    private boolean synchStore(DispatchBean dispatchBean, DispatchBeanRemoteState dispatchBeanRemoteState) throws IllegalStateException{
        boolean isChangeState = false;//默认没有一个改变
        int remoteState;
        int localState;
        BoolMessage boolMessage;
        boolean boxFlag;
        for (DistributionPathBean distributionPathBean : dispatchBean.getDistributionPathBean()){
            boxFlag = synchBox(dispatchBean,distributionPathBean, dispatchBeanRemoteState);// 检测箱子是否存在改变
            if (boxFlag) isChangeState = true;
            localState = distributionPathBean.getState();
            remoteState = dispatchBeanRemoteState.getStoreRemoteStateMap().get(distributionPathBean.getCustomerAgency());
            if (remoteState<localState){
                if (remoteState == STORE_DEAL_LOAD){
                    dispatchBeanRemoteState.getStoreRemoteStateMap().put(distributionPathBean.getCustomerAgency(),STORE_DEAL_UNLOAD);
                    isChangeState = true;
                }else if (remoteState == STORE_DEAL_UNLOAD){
                    //添加路顺及里程数(废弃,不使用)
                    boolMessage = IceIo.get().setGpsMileageSynch(dispatchBean.getVehicleInfoBean().getCarNumber(),
                            distributionPathBean.getCustomerAgency(),
                            distributionPathBean.getSpecifiedOrder(),
                            distributionPathBean.getFactMileage());
                    if (checkBoolMessage(boolMessage)){
                        dispatchBeanRemoteState.getStoreRemoteStateMap().put(distributionPathBean.getCustomerAgency(),STORE_DEAL_COMPLETE);
                        isChangeState = true;
                    }
                }
            }
            if (remoteState>localState){//门店状态回退
                if (remoteState == STORE_DEAL_UNLOAD){
                    // 等待卸货->等待装货
                    dispatchBeanRemoteState.getStoreRemoteStateMap().put(distributionPathBean.getCustomerAgency(),STORE_DEAL_LOAD);
                    isChangeState = true;
                }else if (remoteState == STORE_DEAL_COMPLETE){
                    //不支持已卸载完成的门店状态回退
                }
            }

        }
        return  isChangeState;
    }

    //同步箱子
    private boolean synchBox(DispatchBean dispatchBean,DistributionPathBean distributionPathBean, DispatchBeanRemoteState dispatchBeanRemoteState) throws IllegalStateException{
        boolean isChangeState = false;//默认没有一个改变
        int remoteState;
        int localState;
        BoolMessage boolMessage;
        //循环门店箱子
        for (BoxBean box:distributionPathBean.getBoxNoList()){
            remoteState = dispatchBeanRemoteState.getBoxRemoteStateMap().get(box.getBarCode());
            localState = box.getState();
            //发现一个本地状态与远程状态不同步的箱子
            if (remoteState<localState){
                if (remoteState == BOX_DEAL_LOAD){
                    //通知后台改变箱子状态 - 装货完成
                    boolMessage = IceIo.get().changeBoxStateSynch(
                            dispatchBean.getVehicleInfoBean().getCarNumber(),
                            distributionPathBean.getCustomerAgency(),
                            box.getBarCode(),
                            1,
                            box.getChangeToUnloadStateTime()
                            );
                    if (checkBoolMessage(boolMessage)){
                        dispatchBeanRemoteState.getBoxRemoteStateMap().put(box.getBarCode(),BOX_DEAL_UNLOAD);
                        isChangeState =true;//改变成功
                    }
                }else if (remoteState == BOX_DEAL_UNLOAD){
                        if (box.isAbnormal()){
                            //如果箱子存在异常, 不改变状态
                            dispatchBeanRemoteState.getBoxRemoteStateMap().put(box.getBarCode(),BOX_DEAL_RECYCLE);
                            isChangeState =true;
                        }else{
                            //通知后台改变箱子状态-卸货完成
                            boolMessage = IceIo.get().changeBoxStateSynch(
                                    dispatchBean.getVehicleInfoBean().getCarNumber(),
                                    distributionPathBean.getCustomerAgency(),
                                    box.getBarCode(),
                                    2,
                                    box.getChangeToRecycleStateTime());
                            if (checkBoolMessage(boolMessage)){
                                dispatchBeanRemoteState.getBoxRemoteStateMap().put(box.getBarCode(),BOX_DEAL_RECYCLE);
                                isChangeState =true;
                            }
                        }
                }
            }
            if (remoteState>localState){//状态回退
                if (remoteState == BOX_DEAL_UNLOAD){
                    //等待卸货->等待装货
                    boolMessage = IceIo.get().changeBoxStateSynch(
                            dispatchBean.getVehicleInfoBean().getCarNumber(),
                            distributionPathBean.getCustomerAgency(),
                            box.getBarCode(),
                            0,
                            0
                    );
                    if (checkBoolMessage(boolMessage)){
                        dispatchBeanRemoteState.getBoxRemoteStateMap().put(box.getBarCode(),BOX_DEAL_LOAD);
                        isChangeState = true;//改变成功
                    }
                }else if (remoteState == BOX_DEAL_RECYCLE){
                    //不支持已卸载箱子状态回退
                }
            }
        }
        return isChangeState;
    }

    //检查异常列表是否存在未同步
    private boolean checkAbnormalState() {
        boolean flag = true;
        AbnormalListBean abnormalListBean = new AbnormalListBean().fetch();
        if (abnormalListBean!=null){
            for (AbnormalBean abnormalBean : abnormalListBean.getList()){
                if (abnormalBean.isSynch()){
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    //检查回收列表是否存在未同步
    private boolean checkRecycleBoxState(){
        boolean flag = true;
        RecycleBoxListBean recycleBoxListBean = new RecycleBoxListBean().fetch();
        if (recycleBoxListBean!=null){
            for (RecycleBoxBean recycleBoxBean : recycleBoxListBean.getList()){
                if (recycleBoxBean.isSynch()){
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }
   //获取预警信息
    private void getWarn(){
        try {
                WarnTag warnTag = new WarnTag().fetch();
                if (warnTag==null) return;
                WarnsInfo warnsInfo = IceIo.get().queryTimeLaterWarnInfoByDriver(
                        LoginUserBean.driverCode,
                        warnTag.getVehicleInfoBean().getCarNumber(),
                        warnTag.getCurrent());
                if (warnsInfo!=null && warnsInfo.pendingWarnsNum >0){
                    List<WarnState> warnsInfoList = Convert.handleWarn(warnsInfo.detailList);
                        warnTag.setWarnStateList(warnsInfoList);
                        warnTag.setRemote(warnsInfoList.get(0).getTime());
                        warnTag.save();
                }
                if (warnTag.getNumber()>0) sendIndexActivity(TYPE_BY_WARN, null);
        } catch (Exception e) {
            Ms.Holder.get().error(e);
        }
    }
}
