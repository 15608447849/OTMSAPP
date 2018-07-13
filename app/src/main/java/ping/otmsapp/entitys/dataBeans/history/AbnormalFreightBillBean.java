package ping.otmsapp.entitys.dataBeans.history;

/**
 * Created by Leeping on 2018/4/9.
 * email: 793065165@qq.com
 */

public class AbnormalFreightBillBean {
    //异动原因
    private int cause;

    //异动名称
    private  String name;

    //异动费用
    private double freight;

    public int getCause() {
        return cause;
    }

    public void setCause(int cause) {
        this.cause = cause;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getFreight() {
        return freight;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }
}
