package ping.otmsapp.entitys.dataBeans.dispatch;

import ping.otmsapp.entitys.interfaces.ScanBoxHandleCallback;
import ping.otmsapp.utils.Ms;

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
 * Created by Leeping on 2018/4/8.
 * email: 793065165@qq.com
 * 扫码处理
 */

public class ScannerBoxHandleBean {


    private ScanBoxHandleCallback scanBoxHandleCallback;

    public void setScanBoxHandleCallback(ScanBoxHandleCallback scanBoxHandleCallback) {
        this.scanBoxHandleCallback = scanBoxHandleCallback;
    }
    //检测数据正确性
    public void checkData() {
        DispatchBean dispatchBean = getDispatchBean();

        if (dispatchBean!=null){
          if (dispatchBean.getState() == DISPATCH_DEAL_LOAD){
              //装货中
              checkLoadData(dispatchBean);
              dispatchBean.save();
          }
        }
    }

    private void checkLoadData(DispatchBean dispatchBean) {
        int tempPos = 0;
        for (DistributionPathBean distributionPathBean : dispatchBean.getDistributionPathBean()){
                int tempStoreBoxIndex = 0;
                for (BoxBean boxBean : distributionPathBean.getBoxNoList()){
                    //如果箱子为等待卸货
                    if (boxBean.getState() == BOX_DEAL_UNLOAD){
                        tempStoreBoxIndex++;
                        tempPos++;
                    }else if (boxBean.getState() == BOX_DEAL_RECYCLE){
                        //箱子为回收 (错误状态,重置)
                        boxBean.setState(BOX_DEAL_LOAD);
                    }
                }
                distributionPathBean.setLoadScanIndex(tempStoreBoxIndex);
                checkStoreIsToUnLoadState(distributionPathBean);
        }
        dispatchBean.setLoadScanBoxIndex(tempPos);
        checkDispatchIsToTakeoutState(dispatchBean);
    }

    //扫码处理
    public synchronized void  scannerHandler(String sacnBarNo) throws Exception{

        DispatchBean dispatchBean = getDispatchBean();
        if (dispatchBean!=null){

            if (dispatchBean.getState() == DISPATCH_DEAL_LOAD || dispatchBean.getState() ==  DISPATCH_DEAL_TAKEOUT){
                //装箱处理 - 遍历全部门店 - 根据勾选的门店装货
                loadDispatch(sacnBarNo,dispatchBean);
            }
            else if (dispatchBean.getState() == DISPATCH_DEAL_UNLOAD){
                //卸货处理
                unloadDispatch(sacnBarNo,dispatchBean);
            }else{
                if (scanBoxHandleCallback!=null){
                    scanBoxHandleCallback.changeDispatchState(dispatchBean);
                }
            }

        }

    }
    //是否停止装货循环
    private boolean isLoadBreak = false;

    public void loadBreak(boolean b) {
        isLoadBreak = b;
    }
    /**
     * 扫码装箱
     * 处理调度单
     */
    private  void loadDispatch(String scanBarNo, DispatchBean dispatchBean){
        isLoadBreak = false;
        boolean isFind = false;
        for (DistributionPathBean distributionPathBean : dispatchBean.getDistributionPathBean()){
                    isFind = loadStore(scanBarNo,dispatchBean,distributionPathBean);
                    if (isLoadBreak || isFind) break;
            }

        if (!isLoadBreak && !isFind  && scanBoxHandleCallback!=null){
            //找不到箱子
            scanBoxHandleCallback.boxScanNoFind(scanBarNo);
        }
    }

    /**
     * 装货
     * 处理单个门店
     */
    private boolean loadStore(String scanBarNo, DispatchBean dispatchBean, DistributionPathBean distributionPathBean) {
        if (dispatchBean.getState() != DISPATCH_DEAL_TAKEOUT && scanBoxHandleCallback!=null){
            boolean isContinue = scanBoxHandleCallback.starScanStore(dispatchBean,distributionPathBean); //开始扫描门店- 装货
            if (!isContinue) return false;
        }
        boolean isFind = false;
        int curPos = dispatchBean.getLoadScanBoxIndex(); //当前已扫描总箱数
        int curStoreBoxIndex = distributionPathBean.getLoadScanIndex();//当前门店已扫描总箱数

        for (BoxBean box : distributionPathBean.getBoxNoList()) {
            //发现一个可扫码得箱子
            if (box.getBarCode().equals(scanBarNo) ) {
                isFind = true;
                if (scanBoxHandleCallback!=null){
                    scanBoxHandleCallback.boxScannerSuccess(scanBarNo);//扫码成功
                }
                int state = box.getState();
                if (state == BOX_DEAL_LOAD){ //等待装货
                    //箱子改为卸货状态的时间
                    box.setChangeToUnloadStateTime(System.currentTimeMillis());
                    //箱子 转为卸货状态
                    box.setState(BOX_DEAL_UNLOAD);
                    //改变当前门店已扫码数+1
                    curStoreBoxIndex++;
                    distributionPathBean.setLoadScanIndex(curStoreBoxIndex);
                    //改变调度单总扫码数+1
                    curPos++;
                    dispatchBean.setLoadScanBoxIndex(curPos);
                    //检测门店是否进入卸货状态
                    checkStoreIsToUnLoadState(distributionPathBean);
                    //检测调度单是否进入等待启程状态
                   checkDispatchIsToTakeoutState(dispatchBean);
                    //保存调度单
                    dispatchBean.save();
                    if (scanBoxHandleCallback!=null){
                        //通知调度单状态改变
                        scanBoxHandleCallback.occurChange();
                    }

                }else if (state == BOX_DEAL_UNLOAD){ //等待卸货
                    //箱子改为卸货状态的时间
                    box.setChangeToUnloadStateTime(0);
                    //箱子 转为装货状态
                    box.setState(BOX_DEAL_LOAD);
                    //改变当前门店已扫码数-1
                    curStoreBoxIndex--;
                    distributionPathBean.setLoadScanIndex(curStoreBoxIndex);
                    //改变调度单总扫码数-1
                    curPos--;
                    dispatchBean.setLoadScanBoxIndex(curPos);
                    //检测门店是否返回装货状态
                    checkStoreIsToLoadState(distributionPathBean);
                    //检测调度单是否返回装货状态
                    checkDispatchIsToLoadState(dispatchBean);
                    //保存调度单
                    dispatchBean.save();
                    if (scanBoxHandleCallback!=null){
                        //通知改变
                        scanBoxHandleCallback.occurChange();
                    }
                }
                break;
            }
        }
        return isFind;
    }




    //检查门店是否进入卸货转台
    private void checkStoreIsToUnLoadState(DistributionPathBean distributionPathBean) {
        //如果当前门店已扫码数量 = 此门店总数量 ->> 改变状态为 等待卸货
        if (distributionPathBean.getLoadScanIndex() == distributionPathBean.getBoxSum()){
            //此门店进入卸货状态
            distributionPathBean.setState(STORE_DEAL_UNLOAD);
            if (scanBoxHandleCallback!=null){
                //通知改变门店状态
                scanBoxHandleCallback.changeStoreState(distributionPathBean);
            }
        }
    }

    private void checkStoreIsToLoadState(DistributionPathBean distributionPathBean) {
        //如果当前门店已扫码数量 < 此门店总数量 ->> 改变状态为 等待装货
        if (distributionPathBean.getLoadScanIndex() < distributionPathBean.getBoxSum()){
            //此门店进入卸货状态
            distributionPathBean.setState(STORE_DEAL_LOAD);
            if (scanBoxHandleCallback!=null){
                //通知改变门店状态
                //scanBoxHandleCallback.changeStoreState(distributionPathBean);
            }
        }
    }
    //检查调度单是否进入等待启程状态
    private void checkDispatchIsToTakeoutState(DispatchBean dispatchBean) {
        if (dispatchBean.getLoadScanBoxIndex() == dispatchBean.getStoreBoxSum()){
            //调度单 转为 等待启程
            dispatchBean.setState(DISPATCH_DEAL_TAKEOUT);
            if (scanBoxHandleCallback!=null){
                //通知改变调度单状态(调度单)
                scanBoxHandleCallback.changeDispatchState(dispatchBean);
            }

        }
    }
    private void checkDispatchIsToLoadState(DispatchBean dispatchBean) {
        if (dispatchBean.getLoadScanBoxIndex() < dispatchBean.getStoreBoxSum()){
            //调度单 转为 等待启程
            dispatchBean.setState(DISPATCH_DEAL_LOAD);
            if (scanBoxHandleCallback!=null){
                //通知改变调度单状态(调度单)
//                scanBoxHandleCallback.changeDispatchState(dispatchBean);
            }
        }
    }

    //是否停止卸货循环
    private boolean isUnloadBreak = false;

    public void unloadBreak(boolean b) {
        isUnloadBreak = b;
    }
    /**
     * 扫码卸货
     * 处理调度单
     */
    private void unloadDispatch(String scanBarNo, DispatchBean dispatchBean){
        isUnloadBreak = false;
        boolean isResult;
        for (DistributionPathBean distributionPathBean : dispatchBean.getDistributionPathBean()){
                //门店处于等待卸货状态
                if (distributionPathBean.getState() == STORE_DEAL_UNLOAD){
                    isResult = unloadStore(scanBarNo,dispatchBean,distributionPathBean);
                    if (isResult || isUnloadBreak){
                        break;
                    }
                }
            }

    }

    /**
     * 卸货
     * 处理单个门店
     */
    private boolean unloadStore(String scanBarNo, DispatchBean dispatchBean, DistributionPathBean distributionPathBean) {
        if (scanBoxHandleCallback!=null){
            boolean isContinue = scanBoxHandleCallback.starScanStore(dispatchBean,distributionPathBean); //开始扫描门店- 卸货
            if (!isContinue) return false;
        }

        int curPos = dispatchBean.getUnloadScanBoxIndex();
        int curStoreBoxIndex = distributionPathBean.getUnloadScanIndex();
        boolean result = false;
        boolean isError = true;
        for (BoxBean box : distributionPathBean.getBoxNoList()){
            //发现一个可扫码得箱子
            if (box.getBarCode().equals(scanBarNo)){
                if (box.getState() == BOX_DEAL_UNLOAD ) {
                    if (scanBoxHandleCallback!=null){
                        scanBoxHandleCallback.boxScannerSuccess(scanBarNo);
                    }
                    //设置改变成回收状态的时间
                    box.setChangeToRecycleStateTime(System.currentTimeMillis());
                    //箱子转为回收状态
                    box.setState(BOX_DEAL_RECYCLE);
                    //改变当前门店卸货总数+1
                    curStoreBoxIndex++;
                    distributionPathBean.setUnloadScanIndex(curStoreBoxIndex);
                    //改变调度单卸货总数+1
                    curPos++;
                    dispatchBean.setUnloadScanBoxIndex(curPos);

                    //检测门店是否进入完成状态
                    checkStoreIsToCompleteState(distributionPathBean);
                    //检测调度单是否进入等待回程状态
                    checkDispatchIsToBackState(dispatchBean);
                    //检测是否存在可以处理的异常
                    AbnormalListBean abnormalListBean = new AbnormalListBean().fetch();
                    if (abnormalListBean!=null){
                        for (AbnormalBean abnormalBean : abnormalListBean.getList()){
                            if (abnormalBean.getAbnormalBoxNumber().equals(scanBarNo)){
                                abnormalBean.setHandleCustomerAgency(distributionPathBean.getCustomerAgency());
                                abnormalBean.setHandlerUserCode(dispatchBean.getVehicleInfoBean().getDriverCode());
                                abnormalBean.setHandlerTime(System.currentTimeMillis());
                                abnormalBean.setHandlerRemakes("扫码卸货异常纠正");
                                //需要同步
                                abnormalBean.setSynch(true);
                                abnormalListBean.save();
                            }
                        }
                    }
                    //保存
                    dispatchBean.save();
                    if (scanBoxHandleCallback!=null){
                        //通知存在改变
                        scanBoxHandleCallback.occurChange();
                    }
                }else  if (box.getState()==BOX_DEAL_RECYCLE){
                    if (scanBoxHandleCallback!=null){
                        //箱子重复扫码通知
                        scanBoxHandleCallback.boxScanRepeat(scanBarNo);
                    }
                }
                isError = false;
                result = true;
              break;
            }
        }
        if (isError){
            //记录异常信息
            AbnormalListBean abnormalListBean = new AbnormalListBean().fetch();
            if (abnormalListBean!=null){
                boolean isRecode=true;
                for (AbnormalBean abnormalBean : abnormalListBean.getList()){
                    if (abnormalBean.getAbnormalCustomerAgency().equals(distributionPathBean.getCustomerAgency())
                            && abnormalBean.getAbnormalBoxNumber().equals(scanBarNo)
                            && abnormalBean.getAbnormalType()==1){
                        isRecode = false;
                        break;
                    }
                }
                if (isRecode){
                    AbnormalBean abnormalBean = new AbnormalBean();
                    abnormalBean.setCarNumber(dispatchBean.getVehicleInfoBean().getCarNumber());
                    abnormalBean.setAbnormalCustomerAgency(distributionPathBean.getCustomerAgency());
                    abnormalBean.setAbnormalBoxNumber(scanBarNo);
                    abnormalBean.setAbnormalTime(System.currentTimeMillis());
                    abnormalBean.setAbnormalType(1);
                    abnormalBean.setAbnormalRemakes("扫码卸货错误");
                    abnormalBean.setAbnormalUserCode(dispatchBean.getVehicleInfoBean().getDriverCode());
                    abnormalListBean.getList().add(abnormalBean);
                    abnormalListBean.save();
                }
            }
            if (scanBoxHandleCallback!=null){
                scanBoxHandleCallback.boxScanAbnormal(scanBarNo);
            }
        }
        return result;
    }

    private void checkDispatchIsToBackState(DispatchBean dispatchBean) {
        //如果当前卸货箱数=调度单全部箱数 >> 改变调度信息
        if (dispatchBean.getUnloadScanBoxIndex() == dispatchBean.getStoreBoxSum()){
            //设置调度单签收状态时间
            dispatchBean.setChangeUnloadStateTime(System.currentTimeMillis());
            //转为等待回程状态
            dispatchBean.setState(DISPATCH_DEAL_BACK);
            if (scanBoxHandleCallback!=null){
                //通知改变调度单状态(调度单)
                scanBoxHandleCallback.changeDispatchState(dispatchBean);
            }
        }
    }

    //检查门店是否进入完成状态
    private void checkStoreIsToCompleteState(DistributionPathBean distributionPathBean) {
        //如果当前门店 总卸货数量 = 此门店总数量 >> 改变状态为 完成状态
        if (distributionPathBean.getUnloadScanIndex() == distributionPathBean.getBoxSum()){
            //修改门店状态
            distributionPathBean.setState(STORE_DEAL_COMPLETE);
            if (scanBoxHandleCallback!=null){
                //通知改变门店状态
                scanBoxHandleCallback.changeStoreState(distributionPathBean);
            }
        }
    }

    public DispatchBean getDispatchBean() {
        return new DispatchBean().fetch();
    }

    public boolean isDispatchExist() {
        DispatchBean dispatchBean =getDispatchBean();
        return !(dispatchBean == null || dispatchBean.getState() == DISPATCH_DEAL_COMPLETE);
    }

    /**
     * 设置启程状态
     * @return
     */
    public boolean setTakeOutState() {
        DispatchBean dispatchBean =getDispatchBean();
        if (dispatchBean.getState() == DISPATCH_DEAL_TAKEOUT){
            //设置状态的改变时间
            dispatchBean.setChangeTakeOutTime(System.currentTimeMillis());
            //转为卸货状态
            dispatchBean.setState(DISPATCH_DEAL_UNLOAD);
            //记录
            dispatchBean.save();
            if (scanBoxHandleCallback!=null){

                scanBoxHandleCallback.changeDispatchState(dispatchBean);
            }
            return true;
        }
      return false;
    }

    /**
     * 设置返仓状态
     * @return
     */
    public boolean setBackState() {
        DispatchBean dispatchBean =getDispatchBean();
        if (dispatchBean.getState() == DISPATCH_DEAL_BACK){
            //转为完成状态
            dispatchBean.setState(DISPATCH_DEAL_COMPLETE);
            dispatchBean.save();
            if (scanBoxHandleCallback!=null){

                scanBoxHandleCallback.changeDispatchState(dispatchBean);
            }
            return true;
        }
        return false;
    }


    /**
     * 手工签收
     * @return
     */
    public boolean setHandWord() {
        DispatchBean dispatchBean =getDispatchBean();
        if (dispatchBean.getState() == DISPATCH_DEAL_UNLOAD){
            //遍历所有门店
            for (DistributionPathBean distributionPathBean : dispatchBean.getDistributionPathBean()){

                //等待卸货状态
                if (distributionPathBean.getState() == STORE_DEAL_UNLOAD
                        && distributionPathBean.getUnloadScanIndex()<distributionPathBean.getBoxSum()
                        && scanBoxHandleCallback.handworkStore(dispatchBean,distributionPathBean)){
                    unloadHandwork(dispatchBean,distributionPathBean,dispatchBean);
                }
            }


            //如果当前卸货箱数=调度单全部箱数 >> 改变调度信息
            if (dispatchBean.getUnloadScanBoxIndex() == dispatchBean.getStoreBoxSum()){
                //设置调度单签收状态时间
                dispatchBean.setChangeUnloadStateTime(System.currentTimeMillis());
                //改变订单状态 转为等待回程状态
                dispatchBean.setState(DISPATCH_DEAL_BACK);
                if (scanBoxHandleCallback!=null){
                    //通知改变调度单状态(调度单)
                    scanBoxHandleCallback.changeDispatchState(dispatchBean);
                }
                //保存
                dispatchBean.save();
                if (scanBoxHandleCallback!=null){
                    //通知存在改变
                    scanBoxHandleCallback.occurChange();
                }
            }else{
                //记录
                dispatchBean.save();
                if (scanBoxHandleCallback!=null){
                    scanBoxHandleCallback.occurChange();
                }
            }

            return true;
        }

        return false;
    }
    //手工签单
    private void unloadHandwork(DispatchBean dispatchBean, DistributionPathBean distributionPathBean, DispatchBean bean) {
        int curPos = dispatchBean.getUnloadScanBoxIndex();


        //遍历所有箱子
        for (BoxBean boxBean : distributionPathBean.getBoxNoList()){
            if (boxBean.getState() == BOX_DEAL_UNLOAD){
                //记录异常
                AbnormalBean abnormalBean = new AbnormalBean();
                abnormalBean.setCarNumber(dispatchBean.getVehicleInfoBean().getCarNumber()); //车次号
                abnormalBean.setAbnormalCustomerAgency(distributionPathBean.getCustomerAgency());//异常门店
                abnormalBean.setAbnormalBoxNumber(boxBean.getBarCode());//异常箱号
                abnormalBean.setAbnormalTime(System.currentTimeMillis());//异常发生时间
                abnormalBean.setAbnormalType(3);//异常类型 - 货差异常
                abnormalBean.setAbnormalRemakes("手动签收,未正确扫码,无法获取异常详情"); //异常标注
                abnormalBean.setAbnormalUserCode(dispatchBean.getVehicleInfoBean().getDriverCode());
                AbnormalListBean abnormalListBean = new AbnormalListBean().fetch();
                abnormalListBean.getList().add(abnormalBean);
                abnormalListBean.save();
                //设置箱子异常
                boxBean.setAbnormal(true);
                //设置箱子状态
                boxBean.setState(BOX_DEAL_RECYCLE);
                curPos++;
            }
        }
        //设置此门店已卸货数
        distributionPathBean.setUnloadScanIndex(distributionPathBean.getBoxSum());
        //设置 当前已卸货扫码的箱子总数
        dispatchBean.setUnloadScanBoxIndex(curPos);
        //改变门店状态
        distributionPathBean.setState(STORE_DEAL_COMPLETE);

    }

    /**
     * 根据指定机构码获取机构信息
     * @param customerAgency
     * @return
     */
    public DistributionPathBean getDistributionPathByCustomerAgency(String customerAgency){
        final DispatchBean dispatchBean = getDispatchBean();
        for (DistributionPathBean distributionPathBean : dispatchBean.getDistributionPathBean()){
            if (customerAgency.equals(distributionPathBean.getCustomerAgency())){

              return distributionPathBean;
            }
        }
        return null;
    }

    public int getDispatchBeanState() {
        final DispatchBean dispatchBean = getDispatchBean();
        if (dispatchBean!=null) {
            return dispatchBean.getState();
        }
        return -1;
    }

    public String getDispatchStateString() {
            String str = "没有调度单";
            int state =getDispatchBeanState();
            if (state == DISPATCH_DEAL_LOAD) str = "等待装载";
            if (state == DISPATCH_DEAL_TAKEOUT) str = "等待启程";
            if (state == DISPATCH_DEAL_UNLOAD) str = "等待卸载";
            if (state == DISPATCH_DEAL_BACK) str = "等待返程";
            if (state == DISPATCH_DEAL_COMPLETE) str = "等待上传数据";
            return str;
    }



}
