package ping.otmsapp.entitys.dataBeans.dispatch;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import ping.otmsapp.entitys.dataBeans.localStore.LocalStore;

/**
 * Created by Leeping on 2018/5/28.
 * email: 793065165@qq.com
 */

public class RecycleBoxListBean extends LocalStore<RecycleBoxListBean> {
    private VehicleInfoBean vehicleInfoBean;
    private ArrayList<RecycleBoxBean> list;
    public ArrayList<RecycleBoxBean> getList() {
        if (list==null) list = new ArrayList<>();
        return list;
    }

    public void setList(ArrayList<RecycleBoxBean> list) {
        this.list = list;
    }

    public VehicleInfoBean getVehicleInfoBean() {
        return vehicleInfoBean;
    }

    public void setVehicleInfoBean(VehicleInfoBean vehicleInfoBean) {
        this.vehicleInfoBean = vehicleInfoBean;
    }

    @NotNull
    @Override
    public String getKey() {
        return "回收箱"+super.getKey();
    }
}
