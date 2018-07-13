package ping.otmsapp.entitys.dataBeans.dispatch;

/**
 * Created by Leeping on 2018/4/10.
 * email: 793065165@qq.com
 * 待回收的箱子
 */

public class RecycleBoxBean {
    //回收箱 二维码
    private String boxNo;
    //回收箱 类型
    private int type;
    //用户码
    private String userCode;
    //回收箱 回收车次
    private long carNumber;
    //回收箱 回收时间
    private long time;
    //回收箱 是否同步
    private boolean isSynch = true;

    public RecycleBoxBean(String boxNo, int type, String userCode, long carNumber, long time) {
        this.boxNo = boxNo;
        this.type = type;
        this.userCode = userCode;
        this.carNumber = carNumber;
        this.time = time;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }


    public String getBoxNo() {
        return boxNo;
    }

    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        isSynch = true;
    }

    public long getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(long carNumber) {
        this.carNumber = carNumber;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSynch() {
        return isSynch;
    }

    public void setSynch(boolean synch) {
        isSynch = synch;
    }

    public Object getRecycleTypeString() {
        if (getType() == 1) return "回收箱";
        if (getType() == 2) return "退货箱";
        if (getType() == 3)return "调剂箱";
        return "未知";
    }
}
