package ping.otmsapp.assemblys.fragments.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ping.otmsapp.entitys.background.IOThreadPool;
import ping.otmsapp.entitys.dataBeans.sys.MemoryStoreBean;
import ping.otmsapp.entitys.interfaces.OnFragmentToActivityMessage;
import ping.otmsapp.entitys.interfaces.ViewHolderAbs;

/**
 * Created by user on 2018/2/27.
 *单页面应用 基础fragment
 * 定义 fragment-activity之间相互交互的接口
 */

public class BaseFragment extends Fragment {

    public static final boolean isPrint = false;

    protected Context mContext;

    protected Handler mHandler;

    protected OnFragmentToActivityMessage mActivityCallback;

    protected IOThreadPool pool;

    protected MemoryStoreBean memoryStoreBean;



    //activity - onCreate
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        init(activity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context activity) {
        this.mContext = activity;
        this.mHandler = new Handler(Looper.getMainLooper());

        if (activity instanceof OnFragmentToActivityMessage){
            this.mActivityCallback = (OnFragmentToActivityMessage)activity;
        }
        pool= new IOThreadPool();
    }

    //activity - onCreate
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    //activity - onCreate
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    //activity - onCreate
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    //activity - onStart
    @Override
    public void onStart() {
        super.onStart();
    }
    //activity - onResume
    @Override
    public void onResume() {
        super.onResume();
        if (mActivityCallback !=null){
            mActivityCallback.onFragmentLife(this,"onResume");
        }

    }
    //activity - onPause
    @Override
    public void onPause() {
        super.onPause();
    }

    //activity - onStop
    @Override
    public void onStop() {
        super.onStop();
    }
    //activity - onDestroy
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    //activity - onDestroy
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    //activity - onDestroy
    @Override
    public void onDetach() {
        if (pool!=null) pool.close();
        pool=null;
        this.mContext = null;
        this.mHandler = null;
        super.onDetach();
        FM.Instance.getInstance().removeCache(getTag());//从fragment管理器的缓存中把自己删除
    }

    /**
     * hide()跳转新的Fragment时，旧的Fragment回调onHiddenChanged()，不会回调onStop()等生命周期方法，而新的Fragment在创建时是不会回调onHiddenChanged()，所以一般会和onresume（）方法配合使用。具体场景自己来判断。
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            onResume();
        }
    }

    /**
     * 使用FragmentPagerAdapter+ViewPager时，切换回上一个Fragment页面时（已经初始化完毕），不会回调任何生命周期方法，也不会走onHiddenChanged()方法，所以在viewPage配合fragment搭建页面的情况下，切换页面时候，只能使用setUserVisibleHint方法。
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    // activity - > fragment 传递消息
    public void onActivityCallback(MemoryStoreBean memoryStore){
        memoryStoreBean = memoryStore;
    }


    public void onActivityCallback(String str){
    }

    protected void toUi(Runnable r){
        if (mHandler!=null){
            mHandler.post(r);
        }
    }
    protected void toIO(Runnable r){
        if (pool!=null){
            pool.post(r);
        }
    }

    /**
     * 提示消息
     * @param viewHolderAbs
     * @param s
     */
    protected void showSnackBar(final ViewHolderAbs viewHolderAbs,final String s) {
        toUi(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(viewHolderAbs.getLayoutFileRid(), s,Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    protected void showLongSnackBar(final ViewHolderAbs viewHolderAbs,final String s) {
        toUi(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(viewHolderAbs.getLayoutFileRid(), s,Snackbar.LENGTH_LONG).show();
            }
        });
    }

    protected void sendMessageToActivity(int what){
        sendMessageToActivity(what,null);
    }

    protected void sendMessageToActivity(int what,Object attr){
            if (mActivityCallback !=null) mActivityCallback.onMessage(what,attr);
    }
    protected void startFragment(int index){
        startFragment(index,null);
    }
    protected void startFragment(int index,Bundle bundle){
        if (mActivityCallback!=null) mActivityCallback.pageJump(index,bundle);
    }
    protected void finishSelf() {
        if (mActivityCallback!=null) mActivityCallback.pageJump(-1,null);
    }

    public boolean onExit(boolean execute) {
        boolean isExit = onExit();
        if (isExit && execute) finishSelf();

        return isExit;
    }
    protected boolean onExit(){
        return false;
    }
}
