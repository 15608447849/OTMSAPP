package ping.otmsapp.entitys.maps.location;

import org.jetbrains.annotations.NotNull;

import ping.otmsapp.entitys.dataBeans.dispatch.VehicleInfoBean;
import ping.otmsapp.entitys.dataBeans.localStore.LocalStore;

/**
 * Created by Leeping on 2018/5/15.
 * email: 793065165@qq.com
 */

public class TraceRecodeBeanRemoteState extends LocalStore {
    private VehicleInfoBean vehicleInfoBean;
    /**
     * 轨迹点远程标识数
     */
    private int traceRecodeFlag;

    public TraceRecodeBeanRemoteState create(int traceRecodeFlag,VehicleInfoBean vehicleInfoBean) {
        this.traceRecodeFlag = traceRecodeFlag;
        this.vehicleInfoBean = vehicleInfoBean;
        return this;
    }


    public VehicleInfoBean getVehicleInfoBean() {
        return vehicleInfoBean;
    }

    public void setVehicleInfoBean(VehicleInfoBean vehicleInfoBean) {
        this.vehicleInfoBean = vehicleInfoBean;
    }

    public int getTraceRecodeFlag() {
        return traceRecodeFlag;
    }

    public void setTraceRecodeFlag(int traceRecodeFlag) {
        this.traceRecodeFlag = traceRecodeFlag;
    }

    @NotNull
    @Override
    public String selfKey() {
        return "行驶轨迹状态同步"+super.selfKey();
    }
}
