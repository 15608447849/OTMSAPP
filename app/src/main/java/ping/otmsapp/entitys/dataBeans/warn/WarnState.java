package ping.otmsapp.entitys.dataBeans.warn;

/**
 * Created by lzp on 2018/3/7.
 * 警告报警
 */
public class WarnState {

        private String boxNumber;//箱号
        private String boxTypeStr;//箱类型
        private String plateNumber;//车牌号码
        private String warnInfo;
        private String warnValue;
        private String warnRange;
        private long time;

    public String getBoxNumber() {
        return boxNumber;
    }

    public void setBoxNumber(String boxNumber) {
        this.boxNumber = boxNumber;
    }

    public String getBoxTypeStr() {
        return boxTypeStr;
    }

    public void setBoxTypeStr(String boxTypeStr) {
        this.boxTypeStr = boxTypeStr;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getWarnInfo() {
        return warnInfo;
    }

    public void setWarnInfo(String warnInfo) {
        this.warnInfo = warnInfo;
    }

    public String getWarnValue() {
        return warnValue;
    }

    public void setWarnValue(String warnValue) {
        this.warnValue = warnValue;
    }

    public String getWarnRange() {
        return warnRange;
    }

    public void setWarnRange(String warnRange) {
        this.warnRange = warnRange;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
