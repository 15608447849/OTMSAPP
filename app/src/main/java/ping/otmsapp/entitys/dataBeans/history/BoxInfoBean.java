package ping.otmsapp.entitys.dataBeans.history;

import ping.otmsapp.utils.AppUtil;

/**
 * Created by Leeping on 2018/4/3.
 * email: 793065165@qq.com
 * 回收箱
 */

public class BoxInfoBean {
    //箱号
    private String boxNo;



    public String getBoxNo() {
        return boxNo;
    }

    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }



    @Override
    public String toString() {
        return AppUtil.stringForart("箱号: %s 已签收",boxNo);
    }
}
