package ping.otmsapp.entitys.dataBeans.history;

import java.util.List;

/**
 * Created by Leeping on 2018/4/3.
 * email: 793065165@qq.com
 */

public class StoreInfoBean {

    //门店地址
    private String address;
    //门店简称
    private String simName;
    //总箱数
    private int sumBoxNo;
    //箱子号列表
    private List<BoxInfoBean> boxList;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSimName() {
        return simName;
    }

    public void setSimName(String simName) {
        this.simName = simName;
    }

    public int getSumBoxNo() {
        return sumBoxNo;
    }

    public void setSumBoxNo(int sumBoxNo) {
        this.sumBoxNo = sumBoxNo;
    }

    public List<BoxInfoBean> getBoxList() {
        return boxList;
    }

    public void setBoxList(List<BoxInfoBean> boxList) {
        this.boxList = boxList;
    }
}
