package ping.otmsapp.entitys.dataBeans.dispatch;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import ping.otmsapp.entitys.dataBeans.localStore.LocalStore;

/**
 * Created by Leeping on 2018/5/15.
 * email: 793065165@qq.com
 */

public class DispatchBeanRemoteState extends LocalStore<DispatchBeanRemoteState> {
    private VehicleInfoBean vehicleInfoBean;
    /**
     * 调度信息远程状态码
     */
    private int dispatchRemoteState;
    /**
     * 门店远程状态码 门店机构码-门店远程状态
     */
    private HashMap<String,Integer> storeRemoteStateMap;
    /**
     * 箱子远程状态码 箱子二维码-箱子远程状态码
     */
    private HashMap<String,Integer> boxRemoteStateMap;
    /**
     * 调度信息
     */
    public DispatchBeanRemoteState create(int dispatchRemoteState,
                                HashMap<String, Integer> storeRemoteStateMap,
                                HashMap<String, Integer> boxRemoteStateMap,
                                   VehicleInfoBean vehicleInfoBean) {
        this.dispatchRemoteState = dispatchRemoteState;
        this.storeRemoteStateMap = storeRemoteStateMap;
        this.boxRemoteStateMap = boxRemoteStateMap;
        this.vehicleInfoBean = vehicleInfoBean;
        return this;
    }

    public VehicleInfoBean getVehicleInfoBean() {
        return vehicleInfoBean;
    }

    public void setVehicleInfoBean(VehicleInfoBean vehicleInfoBean) {
        this.vehicleInfoBean = vehicleInfoBean;
    }

    public int getDispatchRemoteState() {
        return dispatchRemoteState;
    }

    public void setDispatchRemoteState(int dispatchRemoteState) {
        this.dispatchRemoteState = dispatchRemoteState;
    }

    public HashMap<String, Integer> getStoreRemoteStateMap() {
        return storeRemoteStateMap;
    }

    public void setStoreRemoteStateMap(HashMap<String, Integer> storeRemoteStateMap) {
        this.storeRemoteStateMap = storeRemoteStateMap;
    }

    public HashMap<String, Integer> getBoxRemoteStateMap() {
        return boxRemoteStateMap;
    }

    public void setBoxRemoteStateMap(HashMap<String, Integer> boxRemoteStateMap) {
        this.boxRemoteStateMap = boxRemoteStateMap;
    }

    @NotNull
    @Override
    public String getKey() {
        return "调度单状态同步"+super.getKey();
    }
}
