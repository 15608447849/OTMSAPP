package ping.otmsapp.entitys.dataBeans.dispatch;

/**
 * Created by Leeping on 2018/3/23.
 * email: 793065165@qq.com
 * 扫码箱
 */

public class BoxBean {

    //二维码
    private String barCode;
    /**
     * 30 待装箱扫码
     * 31 待卸货扫码
     * 32 已卸货待回收
     */
    private int state = -1;
    //是否异常
    private boolean isAbnormal = false;
    //变更为可卸货状态的时间
    private long changeToUnloadStateTime;
    //变更为可回收状态的时间==签收时间 卸货时间
    private long changeToRecycleStateTime;


    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getChangeToUnloadStateTime() {
        return changeToUnloadStateTime;
    }

    public void setChangeToUnloadStateTime(long changeToUnloadStateTime) {
        this.changeToUnloadStateTime = changeToUnloadStateTime;
    }

    public long getChangeToRecycleStateTime() {
        return changeToRecycleStateTime;
    }

    public void setChangeToRecycleStateTime(long changeToRecycleStateTime) {
        this.changeToRecycleStateTime = changeToRecycleStateTime;
    }

    public boolean isAbnormal() {
        return isAbnormal;
    }

    public void setAbnormal(boolean abnormal) {
        isAbnormal = abnormal;
    }
}
