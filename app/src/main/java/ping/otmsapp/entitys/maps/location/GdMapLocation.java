package ping.otmsapp.entitys.maps.location;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by lzp on 2018/2/25.
 * 高德地图
 * GPS定位采集
 * 只在后台服务中创建
 */
public class GdMapLocation implements Closeable {

    //声明mlocationClient对象
    private AMapLocationClient mLocationClient = null;
    //创建 初始化定位客户端
    public GdMapLocation(Context context,AMapLocationListener listener) {

        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);//定位模式,gps
            mLocationOption.setInterval(1000);//间隔
            mLocationOption.setSensorEnable(false);//不使用手机传感器定位角度
            mLocationOption.setLocationCacheEnable(false);//不使用定位缓存
        mLocationOption.setNeedAddress(false);//不用返回地理信息
        mLocationOption.setWifiScan(false);//降低耗电不自动刷新wifi
        mLocationOption.setLocationPurpose(null);//不需要场景
        mLocationClient = new AMapLocationClient(context);//定位客户端
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(listener);
    }

    //关闭
    @Override
    public void close() throws IOException {
        stopLocation();//停止定位
        mLocationClient.onDestroy(); // 高德定位客户端销毁
    }

    //开始定位
    public void startLocation() {
        stopLocation();
        mLocationClient.startLocation();
    }
    //停止定位
    public void stopLocation() {
        if(mLocationClient.isStarted()) mLocationClient.stopLocation();
    }

}



