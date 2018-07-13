package ping.otmsapp.assemblys.fragments.imps.firstPage;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.trace.TraceLocation;
import com.autonavi.amap.mapcore.Inner_3dMap_location;

import java.util.ArrayList;
import java.util.List;

import ping.otmsapp.R;
import ping.otmsapp.assemblys.dialogs.DialogBuilder;
import ping.otmsapp.assemblys.fragments.base.BaseFragment;
import ping.otmsapp.entitys.dataBeans.dispatch.VehicleInfoBean;
import ping.otmsapp.entitys.maps.location.GdLocationCorrect;
import ping.otmsapp.entitys.maps.location.LocationGather;
import ping.otmsapp.entitys.maps.location.MTraceLocation;
import ping.otmsapp.entitys.maps.location.TraceRecodeBean;
import ping.otmsapp.utils.AppUtil;
import ping.otmsapp.viewHolders.fragments.first.PathTrailViewHolder;

/**
 * Created by user on 2018/2/27.
 * 路径轨迹
 *
 */

public class PathTrail extends BaseFragment implements  CompoundButton.OnCheckedChangeListener,  AMap.OnMyLocationChangeListener,Runnable, GdLocationCorrect.CorrectResult {

    private PathTrailViewHolder viewHolder;
    private AMap aMap;
    private volatile boolean isLoop = false;
    private LocationGather locationGather;//轨迹采集
    private GdLocationCorrect gdLocationCorrect; //轨迹纠偏
    //原始轨迹
    private Polyline sLine;
    //记录轨迹
    private Polyline rLine;

    //线程 - 循环读取数据
    private Thread traceReadThread = new Thread(this);

    //处理轨迹-当获取到轨迹时
    private void handleTrace(TraceRecodeBean traceRecodeBean) {

        VehicleInfoBean vehicleInfoBean = traceRecodeBean.getVehicleInfoBean();
        setText(viewHolder.userName,vehicleInfoBean.getDriverName());
        setText(viewHolder.plateNo,vehicleInfoBean.getVehicleCode());
        ArrayList<MTraceLocation> list =  traceRecodeBean.getFiltrationList();
        if (list.size() > 2){
            ArrayList<LatLng> sList = new ArrayList<>();
            for (MTraceLocation mTraceLocation : list){
                sList.add(new LatLng(mTraceLocation.getLatitude(),mTraceLocation.getLongitude()));
            }
           sLine.setPoints(sList);
           setCorrect(list);
        }
    }
    private void setCorrect(ArrayList<MTraceLocation> list) {
        ArrayList<TraceLocation> locations = new ArrayList<>();
        for (MTraceLocation mTrace : list ){
            //径度//维度//速度//角度//时间
            locations.add(new TraceLocation(mTrace.getLatitude(),mTrace.getLongitude(),mTrace.getSpeed(),mTrace.getBearing(),mTrace.getTime()));
        }
        gdLocationCorrect.convert(locations);
    }
    //纠偏轨迹回调
    @Override
    public void onCorrect(List<LatLng> list, int distance) {
        rLine.setPoints(list);
    }

    //清理-当获取不到轨迹时
    private void clearTrace() {
        setText(viewHolder.userName,"");
        setText(viewHolder.plateNo,"");

       sLine.setVisible(false);
       sLine.setPoints(new ArrayList<LatLng>());//设置空坐标
       rLine.setVisible(false);
       rLine.setPoints(new ArrayList<LatLng>());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (viewHolder==null){

            viewHolder = new PathTrailViewHolder(mContext);
            viewHolder.curTrace.setOnCheckedChangeListener(this);
            viewHolder.recTrace.setOnCheckedChangeListener(this);
            locationGather = new LocationGather();
            gdLocationCorrect = new GdLocationCorrect(mContext,1,this);
            checkWifiSetting();
            initMap();
            startThread();
        }
        return viewHolder.getLayoutFileRid();
    }

    //开始线程
    private void startThread() {
        isLoop = true;
        traceReadThread.start();
    }
    private void stopThread(){
        isLoop = false;
    }

    //初始化地图
    private void initMap() {
        this.aMap = viewHolder.gdMapView.getMap();
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setScaleControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);//显示默认的定位按钮
        uiSettings.setLogoPosition(AMapOptions.LOGO_MARGIN_LEFT); //高德地图图标位置
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        aMap.showBuildings(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18.0f));
        aMap.showIndoorMap(false); //显示室内地图
        aMap.setMyLocationStyle(new MyLocationStyle().myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER));
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                aMap.moveCamera(CameraUpdateFactory.zoomTo(cameraPosition.zoom));
            }
        });
        aMap.setOnMyLocationChangeListener(this);
        aMap.setMyLocationEnabled(true);//可触发定位并显示当前位置
        sLine = aMap.addPolyline(new PolylineOptions().width(10).color(Color.BLACK));
        sLine.setVisible(false);
        rLine = aMap.addPolyline(new PolylineOptions().width(10).setCustomTexture(BitmapDescriptorFactory.fromResource(R.drawable.grasp_trace_line)));
        rLine.setVisible(false);
    }
    private void checkWifiSetting() {
        if (AppUtil.isOpenWifi(mContext)) {
            return;
        }
        DialogBuilder.INSTANCE.prompt(mContext,
                "提示",
                "开启WIFI模块会提升定位准确性",
                R.drawable.icon_wifi,
                "去开启",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                        mContext.startActivity(intent); // 打开系统设置界面
                    }
                },
                "不了",null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewHolder.gdMapView.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        //网络问题 会报 Unable to resolve host "apilocate.amap.com": No address associated with hostname
        viewHolder.gdMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        viewHolder.gdMapView.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        viewHolder.gdMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        stopThread();//停止线程

        viewHolder.gdMapView.onDestroy();
        viewHolder.destroy();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == viewHolder.curTrace.getId()){
            sLine.setVisible(isChecked);
        }
        if (buttonView.getId() == viewHolder.recTrace.getId()){
            rLine.setVisible(isChecked);
        }
    }

    //设置文本内容
    private void setText(final TextView tv, final String s) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                tv.setText(s);
            }
        });
    }

    //是否第一次打开
    private boolean isFirst = true;

    //地图上面的定位回调
    @Override
    public void onMyLocationChange(Location location) {
        //移动到地图中心
        if (isFirst){
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            isFirst = false;
        }
        setText(viewHolder.speedText, AppUtil.stringForart("时速 %.2f m/s",location.getSpeed()));

        if (location instanceof Inner_3dMap_location){

            Inner_3dMap_location iLoc = (Inner_3dMap_location)location;

            if (iLoc.getErrorCode()!=0){
                setText(viewHolder.addressText,iLoc.getLocationDetail().trim());

            }else{
                setText(viewHolder.addressText,iLoc.getAddress().trim());
            }

        }
    }

    @Override
    public void run() {
        while (isLoop){
            if (isVisible()){
                TraceRecodeBean traceRecodeBean = locationGather.getTrace();
                //Ms.Holder.get().info("获取后台轨迹记录: "+ AppUtil.javaBeanToJson(traceRecodeBean));
                if (traceRecodeBean!=null){
                    handleTrace(traceRecodeBean);
                }else{
                    clearTrace();
                    isLoop = false;
                }
            }

            try { Thread.sleep(10*1000);} catch (InterruptedException ignored) {}
        }
    }



}
