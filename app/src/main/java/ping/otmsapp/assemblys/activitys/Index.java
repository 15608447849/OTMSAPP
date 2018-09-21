package ping.otmsapp.assemblys.activitys;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import ping.otmsapp.R;
import ping.otmsapp.assemblys.dialogs.DialogBuilder;
import ping.otmsapp.assemblys.fragments.attrs.IndexFragmentAttr;
import ping.otmsapp.assemblys.fragments.base.BaseFragment;
import ping.otmsapp.assemblys.services.connect.BinderAbs;
import ping.otmsapp.assemblys.services.connect.MyServiceConnection;
import ping.otmsapp.assemblys.services.server.HeartbeatService;
import ping.otmsapp.assemblys.services.server.LocationService;
import ping.otmsapp.entitys.apps.MediaBean;
import ping.otmsapp.entitys.apps.PowerBean;
import ping.otmsapp.entitys.apps.VibratorBean;
import ping.otmsapp.entitys.dataBeans.login.LoginUserBean;
import ping.otmsapp.entitys.dataBeans.sys.MemoryStoreBean;
import ping.otmsapp.entitys.dataBeans.warn.WarnTag;
import ping.otmsapp.entitys.interfaces.OnFragmentToActivityMessage;
import ping.otmsapp.entitys.interfaces.OnHeartbeatMessageCallback;
import ping.otmsapp.entitys.interfaces.ScannerCallback;
import ping.otmsapp.entitys.threads.ScannerThread_SEUIC;
import ping.otmsapp.entitys.threads.ScannerThread_UROVO;
import ping.otmsapp.utils.Ms;
import ping.otmsapp.viewControls.ProgressBarControl;
import ping.otmsapp.viewHolders.activity.IndexViewHolder;

import static android.device.KeyMapManager.KEY_TYPE_STARTAC;
import static ping.otmsapp.utils.KEY.BUNDLE_KEY_TYPE;
import static ping.otmsapp.utils.KEY.INTENT_KEY_BUNDLE;
import static ping.otmsapp.utils.KEY.TYPE_BY_CLEAR_DISPATCH;
import static ping.otmsapp.utils.KEY.TYPE_BY_DISPATCH;
import static ping.otmsapp.utils.KEY.TYPE_BY_OPEN_GPS;
import static ping.otmsapp.utils.KEY.TYPE_BY_WARN;

/**
 * Created by user on 2018/2/26.
 * 单页面应用主页
 *   -关联FragmentManager管理器 切换页面
 *
 */

public class Index extends Activity  implements View.OnClickListener,OnFragmentToActivityMessage,ScannerCallback, MyServiceConnection.ConnectedListener, OnHeartbeatMessageCallback {

    private IndexViewHolder viewHolder;//关联布局文件
    private VibratorBean vibratorBean;//震动控制
    private PowerBean powerBean; //电源控制
    private ScannerThread_SEUIC scannerThread;//扫描控制
    private ScannerThread_UROVO scannerThread2;//扫描控制
    private MediaBean mediaBean;
    private ProgressBarControl progressBarControl;//进度条
    private MemoryStoreBean memoryStore;//内存存储-数据共享块
    private IndexFragmentAttr indexFragmentAttr;//fragment绑定
    private MyServiceConnection<LocationService.MBinder> locationConn;//轨迹定位收集服务
    private MyServiceConnection<HeartbeatService.MBinder> heartbeatConn;//连接后台心跳
    private static final int GPS_SETTING_OPEN_CODE = 100;//设置打开GPS回调
    private boolean isShowGpsDialog = false;//是否已经打开GPS开启的对话框

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        locationConn = new MyServiceConnection<>(this,LocationService.class);
        heartbeatConn = new MyServiceConnection<>(this,HeartbeatService.class);
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_index);
        memoryStore = new MemoryStoreBean(); //内存存储
        viewHolder = new IndexViewHolder(this); //视图持有
        setContentView(viewHolder.getLayoutFileRid()); //绑定视图
        viewHolder.setOnClickListener(this);//设置点击事件
        progressBarControl = new ProgressBarControl(new Handler(),viewHolder.title.progressBar); //进度条
        vibratorBean = new VibratorBean(this);//震动控制
        mediaBean = new MediaBean(this);//音乐播放
        powerBean = new PowerBean(this,getClass().getSimpleName());//电源控制

        if (Build.VERSION.SDK_INT==22){
            scannerThread = new ScannerThread_SEUIC(this);//扫描线程-扫码枪专用
            scannerThread.setCallback(this);
            scannerThread.setContinuationMode(true);
        }
        if(Build.VERSION.SDK_INT == 18){
            scannerThread2 = new ScannerThread_UROVO(this);//扫描线程-扫码枪专用
            scannerThread2.setCallback(this);
        }

        indexFragmentAttr = new IndexFragmentAttr(getFragmentManager(),viewHolder.fragmentContainer.getId());//碎片关联
        showFragment(1);
    }




    @Override
    protected void onResume() {
        super.onResume();
        //绑定服务
        bindServer();
        if (scannerThread2!=null) scannerThread2.isEnable(true);

    }
    @Override
    protected void onPause() {
        super.onPause();
        //解除绑定
        unbindServer();
        if (scannerThread2!=null) scannerThread2.isEnable(false);
    }

    @Override
    protected void onDestroy() {
        if (scannerThread!=null) scannerThread.close();
        if (scannerThread2!=null) scannerThread2.close();
        viewHolder.destroy();
        progressBarControl.destroy();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //按下的如果是BACK，同时没有重复
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //绑定服务
    private void bindServer() {
        if (locationConn!=null){
            locationConn.bind(this, this);
        }
        if (heartbeatConn!=null){
            heartbeatConn.bind(this, this);
        }
    }

    //解绑服务
    private void unbindServer() {
        if (locationConn!=null){
            locationConn.unbind(this);
        }
        if (heartbeatConn!=null && heartbeatConn.getBinder()!=null){
            heartbeatConn.getBinder().setHeartbeatMessageCallback(null);
            heartbeatConn.unbind(this);
        }
    }

    //显示碎片 - 设置底部
    private void showFragment(int i){
        switch (i){
            case 1:
                viewHolder.bottom.showView(viewHolder.bottom.curtaskIvSct,viewHolder.bottom.curtaskTag);
                indexFragmentAttr.show(1);
                break;
            case 2:
                viewHolder.bottom.showView(viewHolder.bottom.histaskIvSct,viewHolder.bottom.histaskTag);
                indexFragmentAttr.show(2);
                break;
            case 3:
                viewHolder.bottom.showView(viewHolder.bottom.realwarnIvSct,viewHolder.bottom.realwarnTag);
                indexFragmentAttr.show(3);
                break;
            case 4:
                viewHolder.bottom.showView(viewHolder.bottom.freightbillIvSct,viewHolder.bottom.freightbillTag);
                indexFragmentAttr.show(4);
                break;
            case 5:
                viewHolder.bottom.showView(viewHolder.bottom.pathtrailIvSct,viewHolder.bottom.pathtrailTag);
                indexFragmentAttr.show(5);
                break;
        }
    }

    /**
     * 指定通知fragment
     * @param index
     */
    private void notifyFragment(int index) {
        BaseFragment fragment =  indexFragmentAttr.getTarget(index);
        //如果当前正在显示中,传递数据
        if (fragment!=null && fragment.isVisible()){
            fragment.onActivityCallback(memoryStore);
        }else{
            //跳转页面>当前页面
            showFragment(index);
        }
    }
    //所有fragment的声明周期回调
    @Override
    public void onFragmentLife(BaseFragment baseFragment, String lifeType) {
        if (lifeType.equals("onResume")){
            baseFragment.onActivityCallback(memoryStore);
        }

        if (baseFragment.onExit(false)){
            viewHolder.title.back.setVisibility(View.VISIBLE);
        }else{
            viewHolder.title.back.setVisibility(View.GONE);
        }
    }
    /**
     * 点击事件回调
     * @param v
     */
    @Override
    public void onClick(View v) {
        int vid = v.getId();
        if (vid == viewHolder.bottom.curtask.getId()){
            showFragment(1);
        }else
        if (vid == viewHolder.bottom.histask.getId()){
            showFragment(2);
        }else
        if (vid == viewHolder.bottom.realwarn.getId()){
            showFragment(3);
        }else
        if (vid == viewHolder.bottom.freightbill.getId()){
            showFragment(4);
        }else
        if (vid == viewHolder.bottom.pathtrail.getId()){
            showFragment(5);
        }else
        if (vid == viewHolder.title.exit.getId()){
            logoutAlert();
        }else
        if (vid == viewHolder.title.back.getId()){
            fragmentExit();
        }
    }
    //登出弹框
    private void logoutAlert() {
        DialogBuilder.INSTANCE.prompt(this,
                "提示",
                "是否注销当前用户",
                R.drawable.icon_warning,
                "注销",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        logout();
                    }
                },
                "取消",null);
    }
    //登出操作
    private void logout() {
        if (locationConn!=null)  locationConn.stopServer(this);
        if (heartbeatConn!=null) heartbeatConn.stopServer(this);
        LoginUserBean loginUserBean = new LoginUserBean().fetch();
        loginUserBean.remove();
        //返回权限页面
        Intent intent = new Intent(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }

    //关联fragment的通讯接口
    @Override
    public Object onMessage(int what, Object attr) {
        try {
            if (what==0){
                int num = (int)attr;
                if (num > 0){
                    viewHolder.title.setProgressMax(num);//设置进度条
                }else{
                    viewHolder.title.setProgressIndeterminate(true);
                    progressBarControl.showProgressBar();
                }
            }else if (what == 1){//显示进度条
                progressBarControl.showProgressBar();
            }else if (what == 2){//隐藏进度条
                progressBarControl.hideProgressBar();
            }else if (what == 3) {//设置当前进度
                progressBarControl.setProgressBarIndex((int)attr);
            }else if (what == 4){ //预警有关
                warnNotify(0);
            }else if (what == 5){ //震动
                vibrator();
            }
        } catch (Exception e) {
            Ms.Holder.get().error(e);
        }
        return null;
    }

    /**
     * 页面跳转
     */
    @Override
    public void pageJump(int index, Bundle bundle) {

        if (index==-1)  indexFragmentAttr.goBack();
        else indexFragmentAttr.showToBackStack(index,bundle);
    }

    /**
     * 震动一下
     */
    private void vibrator(){
        vibratorBean.stopVibrator();
        vibratorBean.startVibrator();
    }
    //发送预警
    private void playWarn(){
        mediaBean.close();
        mediaBean.play(R.raw.warn);
    }

    /**
     * 预警发来的通知
     * */
    public void warnNotify(int index){


        if (index==0){
            powerBean.stopPowerWakeLock();
            if (viewHolder.bottom.realwarnNum.getVisibility()==View.VISIBLE){
                viewHolder.bottom.realwarnNum.setVisibility(View.GONE);
            }
        }else{
            powerBean.startPowerWakeLockByScreen();
            vibrator();
            playWarn();
            if (viewHolder.bottom.realwarnNum.getVisibility()==View.GONE){
                viewHolder.bottom.realwarnNum.setVisibility(View.VISIBLE);
            }
            if (index<99){
                viewHolder.bottom.realwarnNum.setText(index+"");
            }else{
                viewHolder.bottom.realwarnNum.setText("99+");
            }
        }
    }

    //activity是 单栈模式 ,被调用startActivity的回调.
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handlerBundle(intent.getBundleExtra(INTENT_KEY_BUNDLE));
    }
    //保存内存存储
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);//注释可 阻止activity保存fragment的状态
    }

    //处理
    private void handlerBundle(Bundle b) {
        if (b!=null){
            String type = b.getString(BUNDLE_KEY_TYPE,null);
            if (type.equals(TYPE_BY_DISPATCH) || type.equals(TYPE_BY_CLEAR_DISPATCH)){ //收到调度信息或清理
                dispatch();
            }
            else
            if (type.equals(TYPE_BY_WARN)){ //收到预警信息
                warn();
            }else
                if (type.equals(TYPE_BY_OPEN_GPS)){ //请求GPS打开窗口
                    openGpsDialog();
                }
        }
    }

    //打开GPS弹窗
    private void openGpsDialog() {
        if (isShowGpsDialog){
            return;
        }
        isShowGpsDialog = true;
        DialogBuilder.INSTANCE.prompt(this,
                "警告",
                "请打开GPS定位",
                R.drawable.icon_warning,
                "去开启",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        // 打开GPS设置界面
                        startActivityForResult(intent,GPS_SETTING_OPEN_CODE);
                    }
                },
                "结束应用", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GPS_SETTING_OPEN_CODE) {
            isShowGpsDialog = false;
        }
    }

    /**
     * 处理调度信息
     */
    private void dispatch() {
        notifyFragment(1);
    }
    /**
     * 处理预警信息
     */
    private void warn() {
        WarnTag warnTag = new WarnTag().fetch();
        if (warnTag==null) return;
        if (warnTag.getNumber()>0) warnNotify(warnTag.getNumber());
        BaseFragment fragment =  indexFragmentAttr.getTarget(3);
        //如果当前正在显示中,传递数据
        if (fragment!=null && fragment.isVisible()){
            fragment.onActivityCallback(memoryStore);
        }
    }

    /**
     * 扫描回调. -按钮监听
     * @param data
     */
    @Override
    public void onScanner(String data) {
        indexFragmentAttr.getCurTarget().onActivityCallback(data);
    }

    /**
     * 服务绑定成功回调
     * @param binder
     */
    @Override
    public void onServiceConnected(BinderAbs binder) {
        if (binder instanceof HeartbeatService.MBinder){
            ((HeartbeatService.MBinder)binder).setHeartbeatMessageCallback(this);
        }
    }

    /**
     * 心跳服务收到消息 回调
     * @param b
     */
    @Override
    public void onBundleCallback(final Bundle b) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                handlerBundle(b);
            }
        });

    }
    //fragment回退
    private void fragmentExit() {
        indexFragmentAttr.getCurTarget().onExit(true);
    }


}
