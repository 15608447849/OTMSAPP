package ping.otmsapp.assemblys.fragments.base;

import android.app.FragmentManager;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2018/2/27.
 */

public abstract class FragmentAttr {
    protected static final String PREFIX = "ping.otmsapp.assemblys.fragments.imps.";

    //存储所有的 碎片
    private Map<Integer,String> map;

    //当前所在位置
    private int current = -1;
    //碎片管理器
    private FragmentManager fm;
    //容器ID
    private int containerId;

    public FragmentAttr(FragmentManager fm,int containerId) {
        this.fm = fm;
        this.containerId = containerId;
        this.map = new HashMap<>();
        initMap();
    }

    public FragmentManager getFm() {
        return fm;
    }

    //获取当前所在位置碎片Tag
    public String getCurrentTag() {
        return map.get(current);
    }

    //设置当前所在位置的碎片
    public void setCurrent(int current) {
        this.current = current;
    }

    protected void setMap(int index,String clazzName) {
       map.put(index,PREFIX+clazzName);
    }

    protected abstract void initMap();

    public String getFragmentTag(int id){
        return map.get(id);
    }


    public void show(int index){
        if (map.get(index)!=null){
            //显示指定碎片
            FM.Instance.getInstance().show(containerId,index,this);
        }
    }

    /**
     * 获取指定碎片
     * @param i
     * @return
     */
    public BaseFragment getTarget(int i) {
        try {
           return FM.Instance.getInstance().getFragment(this,i);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 获取当前碎片
     * @return
     */
    public BaseFragment getCurTarget() {
        try {

            BaseFragment fragment  =   FM.Instance.getInstance().getFragment(this,current);
            if (fragment!=null && !fragment.isVisible()){
                //当前页面存在却又没有在显示中, 代表 回退栈有对象. 取出来
                fragment = FM.Instance.getInstance().getFragmentToBackStack(this);

            }
            return fragment;
        } catch (Exception e) {
        }
        return null;
    }

    public void showToBackStack(int index,Bundle bundle){
        if (map.get(index)!=null)
            //显示指定碎片并添加到回退栈
            FM.Instance.getInstance().addFragmentToBackStack(containerId,getFragmentTag(index),this,bundle);
    }
    public void goBack(){
        FM.Instance.getInstance().removeFragmentToBackStack(this);
    }


}
