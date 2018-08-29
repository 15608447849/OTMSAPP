package ping.otmsapp.entitys.dataBeans.dispatch;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import ping.otmsapp.entitys.dataBeans.localStore.LocalStore;

/**
 * Created by Leeping on 2018/4/4.
 * email: 793065165@qq.com
 * 异常信息列表
 */

public class AbnormalListBean extends LocalStore {

    private VehicleInfoBean vehicleInfoBean;
    private ArrayList<AbnormalBean> list;

    public ArrayList<AbnormalBean> getList() {
        if (list==null) list = new ArrayList<>();
        return list;
    }

    public void setList(ArrayList<AbnormalBean> list) {
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
    public String selfKey() {
        return "异常箱"+super.selfKey();
    }
}
