package ping.otmsapp.entitys.maps.location;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;

import ping.otmsapp.entitys.dataBeans.tuples.Tuple2;
import ping.otmsapp.utils.Ms;

import static ping.otmsapp.utils.RecodeTraceState.RECODE_FINISH;
import static ping.otmsapp.utils.RecodeTraceState.RECODE_ING;

/**
 * Created by Leeping on 2018/4/20.
 * email: 793065165@qq.com
 */

public class LocationGather implements AMapLocationListener {
    public interface GatherResult {
        void onGather(boolean f);
    }

    private GatherResult result;

    public void setGatherResult(GatherResult result) {
        this.result = result;
    }

    //轨迹点过滤
    private LocationFilterHandler locHandle;

    public void createLocationFilterHandler(){
        if (locHandle == null) {
            locHandle =  new LocationFilterHandler();;
        }
    }

    public void saveTraceRecode(Tuple2<MTraceLocation,Float> tuple){
        //调度对象
        TraceRecodeBean traceRecode  = getTrace();
        if (traceRecode!=null && traceRecode.getRecodeState()==RECODE_ING){
            traceRecode.getFiltrationList().add(tuple.getValue0()); //添加轨迹到过滤列表
            traceRecode.setMileage(traceRecode.getMileage()+tuple.getValue1());//距离
            traceRecode.save();//添加后保存

            if (result!=null) result.onGather(true);
        }else{
            if (result!=null) result.onGather(false);
        }
    }

    /**
     * 定位点回调
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        try {
            if (aMapLocation==null || aMapLocation.getErrorCode() != 0) return;
            createLocationFilterHandler();
            Tuple2<MTraceLocation,Float> tuple = locHandle.convert(aMapLocation);
            if (tuple!=null){
                saveTraceRecode(tuple);
            }
        } catch (Exception e) {
            Ms.Holder.get().error(e);
        }
    }

    //是否正在定位中
    public boolean getGarhering() {
        //调度对象
        TraceRecodeBean traceRecode  = getTrace();
        return traceRecode!=null && traceRecode.getRecodeState()==RECODE_ING;
    }

    //开始采集
    public void setGatherStart() {
        TraceRecodeBean traceRecode  = getTrace();
        if (traceRecode!=null) {
            traceRecode.setRecodeState(RECODE_ING);
            traceRecode.save();
        }
    }
    //结束采集
    public void setGatherStop() {
        TraceRecodeBean traceRecode  = getTrace();
        if (traceRecode!=null) {
            traceRecode.setRecodeState(RECODE_FINISH);
            traceRecode.save();
        }
    }



    public TraceRecodeBean getTrace() {
        return new TraceRecodeBean().fetch();
    }

}
