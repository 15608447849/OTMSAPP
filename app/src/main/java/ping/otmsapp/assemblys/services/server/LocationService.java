package ping.otmsapp.assemblys.services.server;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;

import ping.otmsapp.assemblys.services.connect.BinderAbs;
import ping.otmsapp.entitys.apps.NotificationBean;
import ping.otmsapp.entitys.apps.PowerBean;
import ping.otmsapp.entitys.maps.location.GdMapLocation;
import ping.otmsapp.entitys.maps.location.LocationGather;

/**
 * Created by lzp on 2018/2/24.
 * 后台gps定位服务
 */
public class LocationService extends Service implements LocationGather.GatherResult {
    //绑定对象
    public class MBinder extends BinderAbs {

    }
    //地图定位点记录
    private LocationGather locationGarher;
    //高德地图定位
    private GdMapLocation gdMapLocation;
    //通知
    private NotificationBean notificationBean;
    //电源
    private PowerBean powerBean;

    /**
     * 绑定
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MBinder();
    }

    /**
     * 取消绑定
     */
    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 创建
     */
    @Override
    public void onCreate() {
        super.onCreate();

        locationGarher = new LocationGather();
        locationGarher.setGatherResult(this);
        gdMapLocation = new GdMapLocation(getApplicationContext(),locationGarher);
        notificationBean = new NotificationBean(
                getApplicationContext(),
                1,
                null,
                "android.intent.action.INDEX",
                "otmsapp://localhost:9999",
                "行驶轨迹采集",
                "正在后台运行",
                "点击进入应用",
                Notification.DEFAULT_LIGHTS
                );
        powerBean = new PowerBean(getApplicationContext(),getClass().getSimpleName()); //电源管理
//        initRecode();
        gdMapLocation.startLocation();//创建应用开始定位

    }


    /**
     * 销毁
     */
    @Override
    public void onDestroy() {
        try {
            gdMapLocation.close();//关闭定位
        } catch (IOException e) {
        }
        super.onDestroy();
    }


    @Override
    public void onGather(boolean f) {
        if (f){
            //保持cpu
            powerBean.startPowerWakeLockByCPU();
            //打开通知
            notificationBean.showNotification();

        }else{
            //释放cup
            powerBean.stopPowerWakeLock();
            //关闭通知
            notificationBean.cancelNotification();
        }
    }


}
