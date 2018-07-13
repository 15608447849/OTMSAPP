package ping.otmsapp.entitys.dataBeans.dispatch;

/**
 * Created by Leeping on 2018/3/22.
 * email: 793065165@qq.com
 * 车辆信息
 *
 */

public class VehicleInfoBean {
    //司机用户编码
    private String driverCode;
    //车牌号
    private String vehicleCode;
    //车次号
    private long carNumber;
    //司机手机号
    private String phoneNo;
    //司机姓名
    private String driverName;
    //承运商名称
    private String carrierName;

    public String getDriverCode() {
        return driverCode;
    }

    public void setDriverCode(String driverCode) {
        this.driverCode = driverCode;
    }

    public String getVehicleCode() {
        return vehicleCode;
    }

    public void setVehicleCode(String vehicleCode) {
        this.vehicleCode = vehicleCode;
    }

    public void setCarNumber(long carNumber) {
        this.carNumber = carNumber;
    }

    public long getCarNumber() {
        return carNumber;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }
}
