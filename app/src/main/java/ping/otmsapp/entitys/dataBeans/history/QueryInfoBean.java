package ping.otmsapp.entitys.dataBeans.history;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Leeping on 2018/4/3.
 * email: 793065165@qq.com
 * 历史任务-路线
 *
 */

public class QueryInfoBean implements Serializable{
    private List<StoreInfoBean> storeInfoBeanList;
    //总客户数
    private int custorNo;
    //总箱数
    private int boxSumNo;
    //车次号
    private String trainNumber;
    //车牌号
    private String plateNo;
    //状态
    private int state;
    //状态 str
    private String stateStr;

    //初始运费
    private double initFreight;
    //异动运费
    private double abnormalFreight;
    //总费用
    private double totalFreight;
    //签收时间
    private String timeStr;
    //里程数
    private double mileage;
    //应结运费
    private double costFreight;
    //实结运费
    private double actualFreight;




    public List<StoreInfoBean> getStoreInfoBeanList() {
        return storeInfoBeanList;
    }

    public void setStoreInfoBeanList(List<StoreInfoBean> storeInfoBeanList) {
        this.storeInfoBeanList = storeInfoBeanList;
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

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public double getCostFreight() {
        return costFreight;
    }

    public void setCostFreight(double costFreight) {
        this.costFreight = costFreight;
    }

    public double getActualFreight() {
        return actualFreight;
    }

    public void setActualFreight(double actualFreight) {
        this.actualFreight = actualFreight;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }
}

