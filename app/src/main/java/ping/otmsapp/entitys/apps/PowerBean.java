package ping.otmsapp.entitys.apps;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

/**
 * Created by user on 2018/2/25.
 *
 PARTIAL_WAKE_LOCK:保持CPU 运转，屏幕和键盘灯有可能是关闭的。
 SCREEN_DIM_WAKE_LOCK：保持CPU 运转，允许保持屏幕显示但有可能是灰的，允许关闭键盘灯
 SCREEN_BRIGHT_WAKE_LOCK：保持CPU 运转，允许保持屏幕高亮显示，允许关闭键盘灯
 FULL_WAKE_LOCK：保持CPU 运转，保持屏幕高亮显示，键盘灯也保持亮度
 ACQUIRE_CAUSES_WAKEUP：强制使屏幕亮起，这种锁主要针对一些必须通知用户的操作.
 ON_AFTER_RELEASE：当锁被释放时，保持屏幕亮起一段时间

 Level	-保持CPU	-保持屏幕亮	-保持键盘亮	-使用场景
 PARTIAL_WAKE_LOCK	-是	-否	-否	-长时间运行的后台服务，例如Service等
 SCREEN_DIM_WAKE_LOCK	-是	-低亮度	-否	-除非必须保持CPU运行直至运算完成，否则请使用FLAG_KEEP_SCREEN_ON方式
 SCREEN_BRIGHT_WAKE_LOCK	-是	-高亮度	-否	除非必须保持CPU运行直至运算完成，否则请使用FLAG_KEEP_SCREEN_ON方式
 FULL_WAKE_LOCK	-是	-高亮度	-是	-除非必须保持CPU运行直至运算完成，否则请使用FLAG_KEEP_SCREEN_ON方式


 ACQUIRE_CAUSES_WAKEUP	默认情况下wake locks并不是马上开启CPU、Screen或者Keyboard的illumination（对于Screen是Dim或Bright，Keyboard是Bright. wake locks只是在被开启后（比如用户的活动），让设备延续（保存）你设定开启的状态. 但是如果加上ACQUIRE_CAUSES_WAKEUP就可以让Screen或Keyboar的illumination没开启的情况，马上开启它们。 典型的应用就是在收到一个重要的notifications时，需要马上点亮屏幕。

 ON_AFTER_RELEASE	当wake lock被释放的时候，当前调用wake lock的activity的计数器会被重置，所以屏幕会继续亮一段时间

 http://blog.csdn.net/wzy_1988/article/details/46875343

 */

public class PowerBean {
    //电源锁
    private String tag;
    private PowerManager.WakeLock wakeLock;
    private PowerManager powerManager;
    public PowerBean(Context context,String tag) {
        this.tag = tag;
        powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        addWriteList(context);
    }

    private void addWriteList(Context context) {
        //服务加入白名单 - 针对Android6.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = context.getPackageName();
            boolean isIgnoring = powerManager.isIgnoringBatteryOptimizations(packageName);
            if (!isIgnoring) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("package:" + packageName));
                context.startActivity(intent);
            }
        }
    }

    //获取唤醒锁-保持CUP运转
    public void startPowerWakeLockByCPU() {
        if (wakeLock==null){
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, tag);
            wakeLock.acquire(24*60*60*1000L /*24H*/);
        }
    }
    //解除唤醒锁
    public void stopPowerWakeLock() {
        if (wakeLock!=null){
            wakeLock.release();
            wakeLock = null;
        }
    }

    //获取唤醒锁 点亮屏幕
    public void startPowerWakeLockByScreen(){
        if (wakeLock==null){
            wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, tag);
            wakeLock.acquire();
        }
        stopPowerWakeLock();
    }




}
