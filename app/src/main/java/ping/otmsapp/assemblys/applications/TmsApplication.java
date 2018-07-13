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
import ping.otmsapp.utils.Ms;
import ping.otmsapp.utils.StoreImp;
import ping.otmsapp.zeroCIce.IceIo;


/**
 * Created by lzp on 2018/2/21.
 * 轨迹记录Application
 *
 */
public class TmsApplication extends Application implements Application.ActivityLifecycleCallbacks{
    public static final boolean isPrint = false;
    private static TmsApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        if (getPackageName().equals(getCurrentProcessName())){
            Ms.Holder.get().info(isPrint,"application onCreate");
            initParams();
            registerActivityLifecycleCallbacks(this);//注册 activity 生命周期管理

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
        application = this;
        Ms.Holder.get().init(application,"locCons");
        CrashHandler.getInstance().init(application);//异常捕获
        StoreImp.Hodler.get().create(application);//local存储
        IceIo.get().create(application); // ice 后台通讯
        GdMapUtils.get().init(application);
        new UpdateThread(application);//版本更新
//        IceIo.get().destroy();//关闭全局Ice访问
//        ImageManager.getInstance().onDestroy();
//        SPStore.Hodler.get().onDestroy();
//        application = null;
    }






    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Ms.Holder.get().info(isPrint,"application onConfigurationChanged: "+ newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Ms.Holder.get().info(isPrint,"application onLowMemory");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Ms.Holder.get().info(isPrint,"application onTerminate");
    }

    /**
     *
     * @param level
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Ms.Holder.get().info(isPrint,"application onTrimMemory level :"+ level);
//            startHeartbeatService();
        if (level == TRIM_MEMORY_RUNNING_MODERATE){

        }
        if (level == TRIM_MEMORY_UI_HIDDEN){

        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Ms.Holder.get().info(isPrint,"创建activity : "+ activity);
        // 竖屏锁定
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//应用运行时，保持屏幕高亮，不锁屏
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    @Override
    public void onActivityStarted(Activity activity) {
        Ms.Holder.get().info(isPrint,"开始 : "+ activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Ms.Holder.get().info(isPrint,"显示 : "+ activity);

    }

    @Override
    public void onActivityPaused(Activity activity) {
        Ms.Holder.get().info(isPrint,"暂停 : "+ activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Ms.Holder.get().info(isPrint,"停止 : "+ activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Ms.Holder.get().info(isPrint,"保存实例状态 : "+ activity);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Ms.Holder.get().info(isPrint,"销毁 : "+ activity);
    }


}
