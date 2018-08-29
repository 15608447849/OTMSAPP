package ping.otmsapp.entitys.dataBeans.dispatch;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ping.otmsapp.entitys.dataBeans.localStore.LocalStore;

/**
 * Created by leeping on 2018/3/22.
 * 调度信息
 */

public class DispatchBean extends LocalStore {
    //司机,车辆信息
    private VehicleInfoBean vehicleInfoBean;
    //门店路线信息
    private List<DistributionPathBean> distributionPathBean;
    //调度中心的地址
    private String addr;
    //是否计算路径
    private boolean isCalculationPath;
    /**
     * 1 扫码装载任务(未启程)
     * 2 启程
     * 3 配送装卸任务(途中)
     * 4 返程
     * 5 完成
     */
    private int state = -1;
    //所有门店箱子总数
    private int storeBoxSum;
    //当前 装货扫描 箱子总数
    private int loadScanBoxIndex;
    //当前 卸货扫描 箱子总数
    private int unloadScanBoxIndex;
    //变更 启程 状态的时间
    private long changeTakeOutTime;
    //变更 签收 状态的时间
    private long changeUnloadStateTime;

    public boolean isCalculationPath() {
        return isCalculationPath;
    }

    public void setCalculationPath(boolean calculationPath) {
        isCalculationPath = calculationPath;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public VehicleInfoBean getVehicleInfoBean() {
        return vehicleInfoBean;
    }

    public void setVehicleInfoBean(VehicleInfoBean vehicleInfoBean) {
        this.vehicleInfoBean = vehicleInfoBean;
    }

    public List<DistributionPathBean> getDistributionPathBean() {
        return distributionPathBean;
    }

    public void setDistributionPathBean(List<DistributionPathBean> distributionPathBean) {
        this.distributionPathBean = distributionPathBean;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


    public int getStoreBoxSum() {
        return storeBoxSum;
    }

    public void setStoreBoxSum(int storeBoxSum) {
        this.storeBoxSum = storeBoxSum;
    }

    public int getLoadScanBoxIndex() {
        return loadScanBoxIndex;
    }

    public void setLoadScanBoxIndex(int loadScanBoxIndex) {
        this.loadScanBoxIndex = loadScanBoxIndex;
    }

    public int getUnloadScanBoxIndex() {
        return unloadScanBoxIndex;
    }

    public void setUnloadScanBoxIndex(int unloadScanBoxIndex) {
        this.unloadScanBoxIndex = unloadScanBoxIndex;
    }


    public long getChangeTakeOutTime() {
        return changeTakeOutTime;
    }

    public void setChangeTakeOutTime(long changeTakeOutTime) {
        this.changeTakeOutTime = changeTakeOutTime;
    }

    public long getChangeUnloadStateTime() {
        return changeUnloadStateTime;
    }

    public void setChangeUnloadStateTime(long changeUnloadStateTime) {
        this.changeUnloadStateTime = changeUnloadStateTime;
    }

    @NotNull
    @Override
    public String selfKey() {
        return "调度单"+super.selfKey();
    }
}
