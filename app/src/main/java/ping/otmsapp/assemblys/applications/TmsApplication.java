package ping.otmsapp.assemblys.applications;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.WindowManager;

import ping.otmsapp.entitys.maps.location.GdMapUtils;
import ping.otmsapp.entitys.threads.UpdateThread;
import ping.otmsapp.utils.CrashHandler;
import ping.otmsapp.utils.DB;
import ping.otmsapp.utils.Ms;
import ping.otmsapp.zeroCIce.IceIo;


/**
 * Created by lzp on 2018/2/21.
 * 轨迹记录Application
 *
 */
public class TmsApplication extends Application implements Application.ActivityLifecycleCallbacks{

    @Override
    public void onCreate() {
        super.onCreate();
        if (getPackageName().equals(getCurrentProcessName())){
            registerActivityLifecycleCallbacks(this);//注册 activity 生命周期管理
            initParams();
        }

    }




    /**
     * 获取当前进程名
     */
        private String getCurrentProcessName() {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        if (manager!=null){
            for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
                if (process.pid == pid) {
                    processName = process.processName;
                }
            }
        }
        return processName;
    }

    private void initParams() {
        Ms.Holder.get().init(getApplicationContext());//日志输出
        CrashHandler.getInstance().init(getApplicationContext());//异常捕获
        DB.Holder.get().create(getApplicationContext());// 数据库存储
        IceIo.get().create(getApplicationContext()); // ice 后台通讯
        GdMapUtils.get().init(getApplicationContext());
        new UpdateThread(getApplicationContext());//版本更新
    }






    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     *
     * @param level
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_MODERATE){

        }
        if (level == TRIM_MEMORY_UI_HIDDEN){

        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        // 竖屏锁定
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //应用运行时，保持屏幕高亮，不锁屏
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //输入法自适应
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }


}
