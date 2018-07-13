package ping.otmsapp.entitys.dataBeans.dispatch;

import java.util.List;

/**
 * Created by leeping on 2018/3/22.
 *  门店信息及配送路线信息
 *
 */
public class DistributionPathBean {
    //门店名
    private String storeName;
    //详细地址
    private String detailedAddress;
    //客户机构码
    private String customerAgency;
    //相关扫码箱编号列表
    private List<BoxBean> boxNoList;
    //指定配送顺序
    private int specifiedOrder;
    //上一个点到此门店 公里数
    private int factMileage = -1;
    //集装箱总数
    private int boxSum;
    //当前装箱已扫描总数
    private int loadScanIndex;
    //当前卸货已扫描总数
    private int unloadScanIndex;
    /**
     * 21 门店等待装货
     * 22 门店等待卸货
     * 23 门店卸货完成
     */
    private int state;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public String getCustomerAgency() {
        return customerAgency;
    }

    public void setCustomerAgency(String customerAgency) {
        this.customerAgency = customerAgency;
    }

    public List<BoxBean> getBoxNoList() {
        return boxNoList;
    }

    public void setBoxNoList(List<BoxBean> boxNoList) {
        this.boxNoList = boxNoList;
    }

    public int getSpecifiedOrder() {
        return specifiedOrder;
    }

    public void setSpecifiedOrder(int specifiedOrder) {
        this.specifiedOrder = specifiedOrder;
    }

    public int getFactMileage() {
        return factMileage;
    }

    public void setFactMileage(int factMileage) {
        this.factMileage = factMileage;
    }

    public int getBoxSum() {
        return boxSum;
    }

    public void setBoxSum(int boxSum) {
        this.boxSum = boxSum;
    }

    public int getLoadScanIndex() {
        return loadScanIndex;
    }

    public void setLoadScanIndex(int loadScanIndex) {
        this.loadScanIndex = loadScanIndex;
    }

    public int getUnloadScanIndex() {
        return unloadScanIndex;
    }

    public void setUnloadScanIndex(int unloadScanIndex) {
        this.unloadScanIndex = unloadScanIndex;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


}
