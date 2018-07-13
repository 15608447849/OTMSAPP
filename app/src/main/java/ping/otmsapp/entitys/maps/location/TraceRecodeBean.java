package ping.otmsapp.entitys.maps.location;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import ping.otmsapp.entitys.dataBeans.dispatch.VehicleInfoBean;
import ping.otmsapp.entitys.dataBeans.localStore.LocalStore;

import static ping.otmsapp.utils.RecodeTraceState.RECODE_WAIT;

/**
 * Created by user on 2018/2/28.
 * 轨迹记录类
 * 通过车辆信息构建轨迹记录对象
 */

public class TraceRecodeBean extends LocalStore<TraceRecodeBean> {


    public VehicleInfoBean getVehicleInfoBean() {
        return vehicleInfoBean;
    }
    //本次轨迹持有人信息
    private VehicleInfoBean vehicleInfoBean;
    //当前记录状态 1 等待记录 2 记录中 3 记录结束
    private int recodeState;
    //连续轨迹- 规则过滤后
    private ArrayList<MTraceLocation> filtrationList;


    //当前原始里程数 m
    private float mileage = 0.0f;

    public void setVehicleInfoBean(VehicleInfoBean vehicleInfoBean) {
        this.vehicleInfoBean = vehicleInfoBean;
        this.recodeState = RECODE_WAIT; //创建的时候,等待开始记录
    }

    public ArrayList<MTraceLocation> getFiltrationList() {
        if (filtrationList == null) filtrationList = new ArrayList<>();
        return filtrationList;
    }

    public void setFiltrationList(ArrayList<MTraceLocation> filtrationList) {
        this.filtrationList = filtrationList;
    }

    public float getMileage() {
        return mileage;
    }

    public void setMileage(float mileage) {
        this.mileage = mileage;
    }


    public int getRecodeState() {
        return recodeState;
    }

    public void setRecodeState(int recodeState) {
        this.recodeState = recodeState;
    }


    public int getFlag() {
        return getFiltrationList().size();
    }


    @NotNull
    @Override
    public String getKey() {
        return "行驶轨迹"+super.getKey();
    }
}
