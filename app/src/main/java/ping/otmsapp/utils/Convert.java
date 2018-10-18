package ping.otmsapp.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.hy.otms.rpcproxy.appInterface.AppFee;
import cn.hy.otms.rpcproxy.appInterface.AppSchedvech;
import cn.hy.otms.rpcproxy.appInterface.DispatchInfo;
import cn.hy.otms.rpcproxy.appInterface.DispatchOrder;
import cn.hy.otms.rpcproxy.appInterface.DispatchRpc;
import cn.hy.otms.rpcproxy.appInterface.DispatchSchedvech;
import cn.hy.otms.rpcproxy.appInterface.Line;
import cn.hy.otms.rpcproxy.appInterface.Lpn;
import cn.hy.otms.rpcproxy.appInterface.Rcuorg;
import cn.hy.otms.rpcproxy.appInterface.SureFeeInfo;
import cn.hy.otms.rpcproxy.appInterface.WarnsDetailInfo;
import ping.otmsapp.entitys.dataBeans.dispatch.BoxBean;
import ping.otmsapp.entitys.dataBeans.dispatch.DispatchBean;
import ping.otmsapp.entitys.dataBeans.dispatch.DispatchBeanRemoteState;
import ping.otmsapp.entitys.dataBeans.dispatch.DistributionPathBean;
import ping.otmsapp.entitys.dataBeans.dispatch.VehicleInfoBean;
import ping.otmsapp.entitys.dataBeans.history.AbnormalFreightBillBean;
import ping.otmsapp.entitys.dataBeans.history.BoxInfoBean;
import ping.otmsapp.entitys.dataBeans.history.QueryInfoBean;
import ping.otmsapp.entitys.dataBeans.history.StoreInfoBean;
import ping.otmsapp.entitys.dataBeans.tuples.Tuple2;
import ping.otmsapp.entitys.dataBeans.warn.WarnState;

import static ping.otmsapp.utils.STATE.BOX_DEAL_LOAD;
import static ping.otmsapp.utils.STATE.STORE_DEAL_LOAD;

/**
 * Created by Leeping on 2018/4/9.
 * email: 793065165@qq.com
 */

public class Convert {

    public static Tuple2<DispatchBean, DispatchBeanRemoteState> dispatchTrans(DispatchInfo result) {
        DispatchRpc[] pathList = result.dispatchRpc;
        DispatchBean dispatchBean = new DispatchBean();//调度

        DispatchSchedvech dispatchSchedvech = result.dispatchSchedvech;
        VehicleInfoBean vehicleInfoBean = new VehicleInfoBean();
        vehicleInfoBean.setCarNumber(dispatchSchedvech.schedtn);//车次号
        vehicleInfoBean.setDriverCode(dispatchSchedvech.driverc + "");//司机用户码
        vehicleInfoBean.setPhoneNo(dispatchSchedvech.drivercp + "");//手机号码
        vehicleInfoBean.setVehicleCode(dispatchSchedvech.vechid);//车牌号
        vehicleInfoBean.setCarrierName(dispatchSchedvech.carrname);//承运商机构名
        vehicleInfoBean.setDriverName(dispatchSchedvech.drivern);//司机姓名

        DistributionPathBean distributionPathBean;
        List<DistributionPathBean> distributionPathBeanList = new ArrayList<>();
        BoxBean boxBean;
        List<BoxBean> boxBeanList;
        int pos = 0;
        HashMap<String, Integer> storeRemoteStateMap = new HashMap<>();
        HashMap<String, Integer> boxRemoteStateMap = new HashMap<>();
        for (DispatchRpc dispatchRpc : pathList) {
            distributionPathBean = new DistributionPathBean();
            distributionPathBean.setState(STORE_DEAL_LOAD);//21 等待装货 22等待卸货 23卸货完成

            distributionPathBean.setStoreName(dispatchRpc.cusabbname);//门店名
            distributionPathBean.setDetailedAddress(dispatchRpc.addr);//详细地址
            distributionPathBean.setCustomerAgency(dispatchRpc.cusid);//门店机构码
            distributionPathBean.setSpecifiedOrder(dispatchRpc.disrp);//装卸货顺序

            boxBeanList = new ArrayList<>();
            //集装箱
            for (DispatchOrder dispatchOrder : dispatchRpc.dispatchOrder) {
                boxBean = new BoxBean();
                boxBean.setBarCode(dispatchOrder.lpn); //二维码识别
                boxBean.setState(BOX_DEAL_LOAD+dispatchOrder.ostatus);//30 带装箱扫码 31 待卸货扫码 32回收 <-> 服务器: 0 没有, 1装 2 卸
                boxRemoteStateMap.put(boxBean.getBarCode(), boxBean.getState());//箱子远程状态同步状态
                boxBeanList.add(boxBean);
                pos++;
            }
            if (boxBeanList.size()==0) continue; //没有集装箱,不去此门店
            distributionPathBean.setBoxNoList(boxBeanList);
            distributionPathBean.setBoxSum(boxBeanList.size());//此门店总箱数
            storeRemoteStateMap.put(distributionPathBean.getCustomerAgency(), distributionPathBean.getState());//门店远程状态同步状态
            distributionPathBeanList.add(distributionPathBean);
        }
        dispatchBean.setStoreBoxSum(pos);//设置所有门店所有箱子总数
        dispatchBean.setState(STATE.DISPATCH_DEAL_LOAD);//1 扫码装货 2 等待启程 3 配送装卸 4 等待返程
        dispatchBean.setDistributionPathBean(distributionPathBeanList);
        dispatchBean.setVehicleInfoBean(vehicleInfoBean);
        dispatchBean.setAddr(result.rcuorg.addr);
        //远程状态对象 - 用于同步
        DispatchBeanRemoteState remoteStateSynceBean = new DispatchBeanRemoteState().create(dispatchBean.getState(),
                storeRemoteStateMap,
                boxRemoteStateMap,
                dispatchBean.getVehicleInfoBean());
        return new Tuple2<>(dispatchBean, remoteStateSynceBean);
    }

    public static List<QueryInfoBean> historyRecodeTrans(AppSchedvech[] result) {
        //Ms.Holder.get().printObject(result);

        List<QueryInfoBean> historyTaskList = new ArrayList<>();
        QueryInfoBean pathInfoBean;
        List<StoreInfoBean> storeInfoList;
        StoreInfoBean storeInfoBean;
        BoxInfoBean boxInfoBean;
        List<BoxInfoBean> boxInfoList;
        int boxSum;

        Line line;
        AppFee appFee;
        List<AbnormalFreightBillBean> abnormalFreightBillBeanList;
        AbnormalFreightBillBean abnormalFreightBillBean;
        for (AppSchedvech appSchedvech : result) {

            pathInfoBean = new QueryInfoBean();
            line = appSchedvech.line;
            boxSum = 0;
            storeInfoList = new ArrayList<>();
            for (Rcuorg rcuorg : line.rcuorgList) {
                storeInfoBean = new StoreInfoBean();
                storeInfoBean.setAddress(rcuorg.addr);
                storeInfoBean.setSimName(rcuorg.cusabbname);

                boxInfoList = new ArrayList<>();

                for (Lpn lpn : rcuorg.lpn) {
                    boxInfoBean = new BoxInfoBean();
                    boxInfoBean.setBoxNo(lpn.lpn);
                    boxInfoList.add(boxInfoBean);
                }

                storeInfoBean.setBoxList(boxInfoList);
                storeInfoBean.setSumBoxNo(boxInfoList.size());
                storeInfoList.add(storeInfoBean);
                boxSum += storeInfoBean.getSumBoxNo();
            }

            pathInfoBean.setBoxSumNo(boxSum);
            pathInfoBean.setCustorNo(storeInfoList.size());
            pathInfoBean.setStoreInfoBeanList(storeInfoList);
            pathInfoBean.setCustorNo(storeInfoList.size());

            pathInfoBean.setTrainNumber(appSchedvech.schedtn + "");
            pathInfoBean.setState(appSchedvech.ostatus);

            if (appSchedvech.appFee.length>0){
                appFee = appSchedvech.appFee[0];
                pathInfoBean.setInitFreight(appFee.iniamount);
                pathInfoBean.setAbnormalFreight(appFee.chgFee);
                pathInfoBean.setTotalFreight(appFee.totalFee);
            }

            pathInfoBean.setTimeStr(appSchedvech.time);
            historyTaskList.add(pathInfoBean);
        }
        return historyTaskList;
    }

    public static List<QueryInfoBean> freightBillTrans(SureFeeInfo[] result) {

        Ms.Holder.get().printObject(result);

        List<QueryInfoBean> historyTaskList = new ArrayList<>();
        QueryInfoBean pathInfoBean;

        for (SureFeeInfo sureFeeInfo : result) {
            pathInfoBean = new QueryInfoBean();

            pathInfoBean.setTrainNumber(sureFeeInfo.schedtn+"");
            pathInfoBean.setPlateNo(sureFeeInfo.vechid);
            pathInfoBean.setMileage((sureFeeInfo.gpsm+sureFeeInfo.zcgpsm / 1000f));
            pathInfoBean.setBoxSumNo(sureFeeInfo.sumCnt);
            pathInfoBean.setCustorNo(sureFeeInfo.custom);
            pathInfoBean.setCostFreight(sureFeeInfo.initFee/100f);
            pathInfoBean.setActualFreight(sureFeeInfo.lastFee/100f);

            historyTaskList.add(pathInfoBean);
        }
        return historyTaskList;
    }

    public static List<WarnState> handleWarn(WarnsDetailInfo[] detailList) {
        List<WarnState> warnStateList = new ArrayList<>();
        WarnState state ;
        for (WarnsDetailInfo warnsDetailInfo : detailList){
            if (warnsDetailInfo.ostatus==1) continue; //已处理移除
            state = new WarnState();
            state.setBoxNumber(warnsDetailInfo.lpn);
            state.setBoxTypeStr(warnsDetailInfo.mtype);
            state.setPlateNumber(warnsDetailInfo.vechid);
            state.setWarnInfo( (Double.parseDouble(warnsDetailInfo.wval) < Double.parseDouble(warnsDetailInfo.minval)) ? "温度过低" : "温度超高");
            state.setWarnValue(warnsDetailInfo.wval);
            state.setWarnRange(AppUtil.stringForart("正常:%s - %s",warnsDetailInfo.minval,warnsDetailInfo.maxval));
            state.setTime(warnsDetailInfo.timestamp);
            warnStateList.add(state);
        }
        return warnStateList;
    }
}
