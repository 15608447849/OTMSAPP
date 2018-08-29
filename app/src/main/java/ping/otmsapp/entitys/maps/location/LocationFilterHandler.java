package ping.otmsapp.entitys.maps.location;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;

import ping.otmsapp.entitys.dataBeans.tuples.Tuple2;
import ping.otmsapp.utils.AppUtil;
import ping.otmsapp.utils.Ms;

/**
 * Created by user on 2018/3/9.
 * 定位点处理
 */

public class LocationFilterHandler {

    //当前GPS定位点信息
    private AMapLocation curtLoc;
    //上一次被记录的GPS定位点信息
    private AMapLocation prevLoc;

    public LocationFilterHandler() {
        Ms.Holder.get().writeFile("\n\n"+AppUtil.formatUTC(System.currentTimeMillis(),null)+" 轨迹记录:\n");
    }

    /**
     * @return 上一个位置经纬度
     */
    public LatLng getPrevLatLng() {
        if (prevLoc!=null) return new LatLng(prevLoc.getLatitude(), prevLoc.getLatitude());
        return null;
    }

    /**
     * @return 上一个位置角度
     */
    public float getPrevBearing() {
        if (prevLoc!=null) return prevLoc.getBearing();
        return 0;
    }

    /**
     *
     * @return 获取当前位置的经纬度
     */
    public LatLng getCurLatLng(){
        if (curtLoc!=null) return  new LatLng(curtLoc.getLatitude(), curtLoc.getLatitude());
        return null;
    }

    /**
     * 获取当前位置角度
     * @return
     */
    public float getCurBearing(){
        if (curtLoc!=null) return  curtLoc.getBearing();
        return 0;
    }

    private StringBuffer getLocInfo(AMapLocation loc){
        StringBuffer stringBuffer = new StringBuffer("\n");
            stringBuffer.append(""+AppUtil.formatUTC(loc.getTime(),null)).append("\t");
        stringBuffer.append("经纬度:["+loc.getLatitude()+","+loc.getLongitude()).append("]\t");
            stringBuffer.append("卫星:"+loc.getSatellites()).append("\t");
            stringBuffer.append("精度:"+loc.getAccuracy()).append("\t");
            stringBuffer.append("速度:"+loc.getSpeed()).append("\t");
            stringBuffer.append("角度:"+loc.getBearing()).append("\t");

            return stringBuffer;
    }

    //转换过滤
    public Tuple2<MTraceLocation,Float> convert(AMapLocation aMapLocation) throws Exception {
        curtLoc = aMapLocation;
        StringBuffer stringBuffer = getLocInfo(aMapLocation);
        Ms.Holder.get().writeFile(stringBuffer.toString());
            //如果当前 类型不符,精度过大,卫星数过低 不记录
            if ( curtLoc.getLocationType() != AMapLocation.LOCATION_TYPE_GPS //类型不符
                    || curtLoc.getAccuracy() > 50 //精度过大
                    || (curtLoc.getBearing() == 0 && curtLoc.getSpeed() == 0) //不存在角度偏移 且静止
                    || curtLoc.getSatellites() < 4){ //卫星数过低
                return null;
            }

        if (prevLoc == null) {
            prevLoc = curtLoc;
            Ms.Holder.get().writeFile(stringBuffer.append("起点").toString());
            return null;
        }

        final float cDistance = AMapUtils.calculateLineDistance(getCurLatLng(),getPrevLatLng());//距离改变量,单位米
        final float cBearing = Math.abs(getCurBearing() - getPrevBearing());//角度改变量
        stringBuffer.append("距离变化:"+ cDistance +" 米\t");
        stringBuffer.append("角度变化:"+cBearing+" 度\t");

            //如果角度改变量过小 并且 速度改变量过小
            if ( (cDistance > curtLoc.getAccuracy()  && cBearing < 45 )
                    || (cDistance <curtLoc.getAccuracy() && (cBearing > 30 && cBearing < 60 ))  ){
                stringBuffer.append("记录.");
                Ms.Holder.get().writeFile(stringBuffer.toString());
            }else{
                return null;
            }

        MTraceLocation tloc = new MTraceLocation(
                curtLoc.getLatitude(),//径度
                curtLoc.getLongitude(),//维度
                curtLoc.getSpeed(),//速度
                curtLoc.getBearing(),//角度
                curtLoc.getTime());
        prevLoc = curtLoc;

        return new Tuple2<>(tloc, cDistance);
    }
}
