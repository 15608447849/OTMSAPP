package ping.otmsapp.entitys.interfaces;

import ping.otmsapp.entitys.maps.location.LocationFilterHandler;
import ping.otmsapp.entitys.maps.location.TraceRecodeBean;

/**
 * Created by user on 2018/2/25.
 * 定位回调
 */

public interface LocationTraceCallback {
    void onDrivingTrace(LocationFilterHandler locationInfoStore, TraceRecodeBean traceRecode);
}
