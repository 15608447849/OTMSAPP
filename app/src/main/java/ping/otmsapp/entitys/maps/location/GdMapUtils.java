package ping.otmsapp.entitys.maps.location;

import android.content.Context;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.DistanceResult;
import com.amap.api.services.route.DistanceSearch;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.TruckPath;
import com.amap.api.services.route.TruckRouteRestult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leeping on 2018/4/23.
 * email: 793065165@qq.com
 */

public class GdMapUtils {
    private GdMapUtils(){

    }


    private static final class Holder{
        private  final static GdMapUtils INSTANCE = new GdMapUtils();
    }

    public static GdMapUtils get(){
        return Holder.INSTANCE;
    }

    public static abstract class OnGeocodeSearchListenerAdapter implements GeocodeSearch.OnGeocodeSearchListener{
        @Override
        public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        }

        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        }
    }
    private Context context;
    public void init(Context context){
        this.context = context;
    }
    //地址转换, 全国范围查询
    public synchronized void addressConvertLatLonAsync(String address, GeocodeSearch.OnGeocodeSearchListener listener){
        //条件
        GeocodeQuery query = new GeocodeQuery(address, null);
        //map搜索功能
        GeocodeSearch geocodeSearch = new GeocodeSearch(context);
            geocodeSearch.setOnGeocodeSearchListener(listener);
            geocodeSearch.getFromLocationNameAsyn(query);

    }
    //地址转换同步
    public synchronized LatLonPoint addressConvertLatLon(String address){
        //条件
        GeocodeQuery query = new GeocodeQuery(address, null);
        //map搜索功能
        GeocodeSearch geocodeSearch = new GeocodeSearch(context);
        try {
            List<GeocodeAddress> list = geocodeSearch.getFromLocationName(query);
            if (list.size()>=1){
                GeocodeAddress geocodeAddress = list.get(0);
                return geocodeAddress.getLatLonPoint();
            }
        } catch (AMapException e) {
            e.printStackTrace();
        }
        return null;
    }
    //计算两点之间的时间和距离
    public long[] pointConvertTimeAndDistance1(LatLonPoint startPoint, LatLonPoint endPoint) {
        final long[] arr = new long[]{0L,0L};
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
        /**
         *
         *  http://a.amap.com/lbs/static/unzip/Android_Map_Doc/index.html
         *
         微型货车（GA≤1．8吨） RouteSearch.TRUCK_SIZE_MINI
         轻型货车（1．8吨＜GA≤6吨） RouteSearch.TRUCK_SIZE_LIGHT
         中型货车（6．0吨＜GA≤14吨） RouteSearch.TRUCK_SIZE_MEDIUM
         重型货车（GA＞14吨） RouteSearch.TRUCK_SIZE_HEAVY
         */
        RouteSearch.TruckRouteQuery query = new RouteSearch.TruckRouteQuery(fromAndTo,
                RouteSearch.DRIVING_SINGLE_AVOID_CONGESTION,
                null,
                RouteSearch.TRUCK_SIZE_MEDIUM);
        RouteSearch routeSearch = new RouteSearch(context);
        try {
            TruckRouteRestult truckRouteRestult = routeSearch.calculateTruckRoute(query);
            long timeSum = 0;
            long distance = 0;
            for (TruckPath truckPath : truckRouteRestult.getPaths()){
//                Log.w("时间",truckPath.getDuration()+"");
                timeSum+=truckPath.getDuration();
//                Log.w("距离",truckPath.getDistance()+"");
                distance+=truckPath.getDistance();
            }
            arr[0] = timeSum/truckRouteRestult.getPaths().size();
            arr[1] = distance/truckRouteRestult.getPaths().size();

        } catch (AMapException e) {
            e.printStackTrace();
//            routeSearch.setOnTruckRouteSearchListener(new RouteSearch.OnTruckRouteSearchListener() {
//                @Override
//                public void onTruckRouteSearched(TruckRouteRestult truckRouteRestult, int i) {
//                    Log.w("高德地图查询","错误码:"+i);
//                }
//            });
//            routeSearch.calculateTruckRouteAsyn(query);
        }
        return arr;
    }
    public long[] pointConvertTimeAndDistance2(LatLonPoint startPoint, LatLonPoint endPoint) {
        final long[] arr = new long[]{0L,0L};
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
    // 第三个参数表示途经点（最多支持16个），第四个参数表示避让区域（最多支持32个），第五个参数表示避让道路
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo,
                RouteSearch.DRIVING_SINGLE_AVOID_CONGESTION,
                null,
                null,
                "");

        RouteSearch routeSearch = new RouteSearch(context);
        try {
            DriveRouteResult driveRouteResult = routeSearch.calculateDriveRoute(query);
            long timeSum = 0;
            long distance = 0;
            for (DrivePath drivePath : driveRouteResult.getPaths()){
//                Log.w("时间",truckPath.getDuration()+"");
                timeSum+=drivePath.getDuration();
//                Log.w("距离",truckPath.getDistance()+"");
                distance+=drivePath.getDistance();
            }
            arr[0] = timeSum/driveRouteResult.getPaths().size();
            arr[1] = distance/driveRouteResult.getPaths().size();

        } catch (AMapException e) {
            e.printStackTrace();
        }
        return arr;
    }
    public long[] pointConvertTimeAndDistance3(LatLonPoint startPoint, LatLonPoint endPoint) {
        final long[] arr = new long[]{0L,0L};
        DistanceSearch distanceSearch = new DistanceSearch(context);
        DistanceSearch.DistanceQuery distanceQuery = new DistanceSearch.DistanceQuery();
        List<LatLonPoint> sList = new ArrayList<>();
        sList.add(startPoint);
        distanceQuery.setOrigins(sList);
        distanceQuery.setDestination(endPoint);
        distanceQuery.setType(DistanceSearch.TYPE_DRIVING_DISTANCE);
        try {
            DistanceResult distanceResult = distanceSearch.calculateRouteDistance(distanceQuery);
            if (distanceResult.getDistanceResults().size() == 1){
                arr[0] = (long) distanceResult.getDistanceResults().get(0).getDuration();
                arr[1] = (long) distanceResult.getDistanceResults().get(0).getDistance();
            }
        } catch (AMapException e) {
            e.printStackTrace();
        }
        return arr;
    }
}
