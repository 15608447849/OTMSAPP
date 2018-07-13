package ping.otmsapp.entitys.dataBeans.dispatch;

/**
 * Created by Leeping on 2018/4/4.
 * email: 793065165@qq.com
 * 异常
 */

public class AbnormalBean {
    private boolean isSynch = true; //是否同步
    //调度车次
    private long carNumber;
    //异常发生客户机构码
    private String abnormalCustomerAgency;
    //异常箱号
    private String abnormalBoxNumber;
    //异常发生采集时间
    private long abnormalTime;
    //异常类型
    private int abnormalType;
    //异常发生备注
    private String abnormalRemakes;
    //异常发生人用户码
    private String abnormalUserCode;
    //异常处理客户机构
    private String handleCustomerAgency;
    //异常处理人用户码
    private String handlerUserCode;
    //异常处理时间
    private long handlerTime;
    //异常处理备注
    private String handlerRemakes;

    public boolean isSynch() {
        return isSynch;
    }

    public void setSynch(boolean synch) {
        isSynch = synch;
    }

    public long getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(long carNumber) {
        this.carNumber = carNumber;
    }

    public String getAbnormalCustomerAgency() {
        return abnormalCustomerAgency;
    }

    public void setAbnormalCustomerAgency(String abnormalCustomerAgency) {
        this.abnormalCustomerAgency = abnormalCustomerAgency;
    }

    public String getAbnormalBoxNumber() {
        return abnormalBoxNumber;
    }

    public void setAbnormalBoxNumber(String abnormalBoxNumber) {
        this.abnormalBoxNumber = abnormalBoxNumber;
    }

    public long getAbnormalTime() {
        return abnormalTime;
    }

    public void setAbnormalTime(long abnormalTime) {
        this.abnormalTime = abnormalTime;
    }

    public int getAbnormalType() {
        return abnormalType;
    }

    public void setAbnormalType(int abnormalType) {
        this.abnormalType = abnormalType;
    }

    public String getAbnormalRemakes() {
        return abnormalRemakes;
    }

    public void setAbnormalRemakes(String abnormalRemakes) {
        this.abnormalRemakes = abnormalRemakes;
    }

    public String getAbnormalUserCode() {
        return abnormalUserCode;
    }

    public void setAbnormalUserCode(String abnormalUserCode) {
        this.abnormalUserCode = abnormalUserCode;
    }

    public String getHandlerUserCode() {
        return handlerUserCode;
    }

    public void setHandlerUserCode(String handlerUserCode) {
        this.handlerUserCode = handlerUserCode;
    }

    public long getHandlerTime() {
        return handlerTime;
    }

    public void setHandlerTime(long handlerTime) {
        this.handlerTime = handlerTime;
    }

    public String getHandlerRemakes() {
        return handlerRemakes;
    }

    public void setHandlerRemakes(String handlerRemakes) {
        this.handlerRemakes = handlerRemakes;
    }

    public String getHandleCustomerAgency() {
        return handleCustomerAgency;
    }

    public void setHandleCustomerAgency(String handleCustomerAgency) {
        this.handleCustomerAgency = handleCustomerAgency;
    }
}
