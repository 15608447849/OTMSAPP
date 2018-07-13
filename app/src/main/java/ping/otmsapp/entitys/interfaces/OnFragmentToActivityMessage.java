package ping.otmsapp.entitys.interfaces;

import android.os.Bundle;

import ping.otmsapp.assemblys.fragments.base.BaseFragment;

/**
 * Created by user on 2018/3/4.
 * fragment -> activity 通讯
 */

public interface OnFragmentToActivityMessage {


    /**
     * fragment 声明周期回调
     */
    void onFragmentLife(BaseFragment baseFragment, String lifeType);
    /**
     * fragment 传递信息到 activity
     * 或者 获取某信息
     */
    Object onMessage(int what,Object attr);

    /**
     * 子页面跳转或关闭
     */
    void pageJump(int pageIndex,Bundle bundle);



}
