package ping.otmsapp.entitys.dataBeans.warn;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ping.otmsapp.entitys.dataBeans.dispatch.VehicleInfoBean;
import ping.otmsapp.entitys.dataBeans.localStore.LocalStore;

/**
 * Created by Leeping on 2018/5/11.
 * email: 793065165@qq.com
 */

public class WarnTag extends LocalStore {
    private VehicleInfoBean vehicleInfoBean;

    //时间戳
    private long current;
    private long remote;
    //实际预警信息
    List<WarnState> warnStateList;

    public long getCurrent() {
        return current;
    }

    public long getRemote() {
        return remote;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public void setRemote(long remote) {
        this.remote = remote;
    }

    public int getNumber(){
        return warnStateList == null? 0 : warnStateList.size();
    }

    public List<WarnState> getWarnStateList() {
        return warnStateList;
    }

    public void setWarnStateList(List<WarnState> warnStateList) {
        if (this.warnStateList!=null){
            //bug: 不会更新已存在的数据
            for (WarnState newWarnState : warnStateList){
                for (WarnState oldWarnState:this.warnStateList){
                    if (!newWarnState.getBoxNumber().equals(oldWarnState.getBoxNumber())){
                        warnStateList.add(oldWarnState);
                    }
                }
            }
        }
            this.warnStateList = warnStateList;
    }

    public VehicleInfoBean getVehicleInfoBean() {
        return vehicleInfoBean;
    }

    public void setVehicleInfoBean(VehicleInfoBean vehicleInfoBean) {
        this.vehicleInfoBean = vehicleInfoBean;
        this.current = System.currentTimeMillis();
    }

    public WarnTag reset(){
        current = remote;
        return this;
    }
    @NotNull
    @Override
    public String selfKey() {
        return "预警信息"+super.selfKey();
    }
}
