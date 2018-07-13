package ping.otmsapp.entitys.dataBeans.history;

import java.util.List;

/**
 * Created by Leeping on 2018/4/3.
 * email: 793065165@qq.com
 * 历史任务-路线
 *
 */

public class PathInfoBean {
    private List<StoreInfoBean> storeInfoBeanList;
    //总客户数
    private int custorNo;
    //此线路总箱数
    private int boxSumNo;
    //开始点
    private String startAddr;
    //结束点
    private String endAddr;
    //车次号
    private String trainNumber;
    //状态
    private int state;
    //状态 str
    private String stateStr;
    //初始运费
    private double initFreight;
    //异动运费及原因
    List<AbnormalFreightBillBean> abnormalFreightBillBeanList;
    //异动运费
    private double abnormalFreight;
    //总费用
    private double totalFreight;
    //签收时间
    private String timeStr;

    public List<StoreInfoBean> getStoreInfoBeanList() {
        return storeInfoBeanList;
    }

    public void setStoreInfoBeanList(List<StoreInfoBean> storeInfoBeanList) {
        this.storeInfoBeanList = storeInfoBeanList;
    }

    public String getStartAddr() {
        return startAddr;
    }

    public void setStartAddr(String startAddr) {
        this.startAddr = startAddr;
    }

    public String getEndAddr() {
        return endAddr;
    }

    public void setEndAddr(String endAddr) {
        this.endAddr = endAddr;
    }

    public int getBoxSumNo() {
        return boxSumNo;
    }

    public void setBoxSumNo(int boxSumNo) {
        this.boxSumNo = boxSumNo;
    }

    public int getCustorNo() {
        return custorNo;
    }

    public void setCustorNo(int custorNo) {
        this.custorNo = custorNo;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        switch (this.state) {
            case 0:
                setStateStr("在库");
                break;
            case 1:
                setStateStr("预调度");
                break;
            case 2:
                setStateStr("调度");
                break;
            case 3:
                setStateStr("在途");
                break;
            case 4:
                setStateStr("签收");
                break;
        }
    }

    public String getStateStr() {
        return stateStr;
    }

    public void setStateStr(String stateStr) {
        this.stateStr = stateStr;
    }

    public double getInitFreight() {
        return initFreight;
    }

    public void setInitFreight(double initFreight) {
        this.initFreight = initFreight;
    }

    public List<AbnormalFreightBillBean> getAbnormalFreightBillBeanList() {
        return abnormalFreightBillBeanList;
    }

    public void setAbnormalFreightBillBeanList(List<AbnormalFreightBillBean> abnormalFreightBillBeanList) {
        this.abnormalFreightBillBeanList = abnormalFreightBillBeanList;
    }

    public double getAbnormalFreight() {
        return abnormalFreight;
    }

    public void setAbnormalFreight(double abnormalFreight) {
        this.abnormalFreight = abnormalFreight;
    }

    public double getTotalFreight() {
        return totalFreight;
    }

    public void setTotalFreight(double totalFreight) {
        this.totalFreight = totalFreight;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }
}

