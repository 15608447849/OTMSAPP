package ping.otmsapp.assemblys.fragments.base
import android.app.Fragment
import android.os.Bundle
import ping.otmsapp.entitys.apps.CacheStore
import ping.otmsapp.utils.Ms


/**
 * Created by user on 2018/2/26.
 * Fragment Manager
 *
 *
//isAdded,如果该Fragment对象被添加到了它的Activity中，那么它返回true，否则返回false。

//isDetached 如果该Fragment已经明确的从UI中分离，那么它返回true。也就是说,在该Fragment对象上使用FragmentTransaction.detach(Fragment)方法。

//isHidden 如果该Fragment对象已经被隐藏，那么它返回true。默认情况下，Fragment是被显示的。能够用onHiddenChanged(boolean)回调方法获取该Fragment对象状态的改变，要注意的是隐藏状态与其他状态是正交的---也就是说，要把该Fragment对象显示给用户，Fragment对象必须是被启动并不被隐藏。

//isRemoving 如果当前的Fragment对象正在从它的Activity中被删除，那么就返回true。这删除过程不是该Fragment对象的Activity的结束过程，而是把Fragment对象从它所在的Activity中删除的过程。

//isResumed 如果Fragment对象是在恢复状态中，该方法会返回true。在onResume()和onPause()回调期间，这个方法都返回true。

//isVisible 如果该Fragment对象对用户可见，那么就返回true。这就意味着它：1.已经被添加到Activity中；2.它的View对象已经被绑定到窗口中；3.没有被隐藏。
 *
 */
private class FM private constructor(val cache: CacheStore<String, Fragment>, val stack:BackStack<BaseFragment>) {

    private object Holder { val INSTANCE = FM(CacheStore(20),BackStack()) }

    companion object Instance {
        val instance: FM by lazy { Holder.INSTANCE }
    }
//允许使用 “？.”操作符调用变量，其含义是如果b不是null,这个表达式将会返回b.length,否则返回 null.如果使用了”?.”,其表达式的值也应为 可为null的
    //如果 ?: 左侧的表达式值不是null, 就会返回表达式的的值,否则, 返回右侧表达式的值.
    //val l = b?.length ?: -1
//    如果b不为null，将返回b的长度，如果为null，将返回-1



    /**
     * 隐藏当前碎片
     */
    fun hideCurrent(attr: FragmentAttr){
        val m  = attr.fm ?: return
        val ft = m.beginTransaction()//事务操作
        //隐藏当前图层
        val cf =  m.findFragmentByTag(attr.currentTag)
        cf?: return
        if (cf.isVisible){
            ft.hide(cf)
            ft.commit()
        }

    }

    /**
     * 显示当前页面
     */
    fun showCurrent(attr:FragmentAttr){
        val m  = attr.fm ?: return
        val ft = m.beginTransaction()//事务操作

        val cf =  m.findFragmentByTag(attr.currentTag)
        cf?: return
        if (cf.isHidden){
            ft.show(cf)
            ft.commit()
        }

    }

    /**
     * 显示指定碎片

    Ms.Hodler.get().info("fragment 状态: \n isAdded = "+ f.isAdded+", isVisible = "+ f.isVisible +", isRemoving = "+ f.isRemoving+", isDetached = "+f.isDetached+", isResumed = "+ f.isResumed+", isHidden = "+ f.isHidden)
    //如果是在移除中且明确的从Ui界面分离重新添加
    if (f.isRemoving && f.isDetached) {

    }
     */
    fun show(containerId:Int,index:Int,attr:FragmentAttr){
        val tag = attr.getFragmentTag(index) ?: return //获取指定下标的碎片名(TAG)
        if (attr.currentTag == tag) return //当前显示中的碎片是否与要显示的碎片相同
        clearBackStack(attr);//清空回退栈
        hideCurrent(attr) //隐藏当前碎片
        attr.setCurrent(-1)//当前下标置-1
        val m  = attr.fm ?: return
        var fragment = m.findFragmentByTag(tag) //管理器中获取
        if (fragment == null){
            fragment = cache[tag]  //缓存获取
            if (fragment == null){
                fragment = create(tag) //创建
            }
        }
        if(fragment == null) return
        if (fragment is BaseFragment){
            add(containerId,tag,index,fragment,attr)
        }

    }

    fun add(containerId:Int,tag:String,index:Int,fragment:BaseFragment,attr:FragmentAttr){
        val m  = attr.fm ?: return
        //事务操作
        val ft = m.beginTransaction()
        //属性动画
        val anim = FragmentAnim.convertAnimations(FragmentAnim.CoreAnim.fade)
        ft.setCustomAnimations(anim[0], anim[1], anim[2], anim[3])
        //已经添加成功但被隐藏 -> 显示
        if (fragment.isAdded && fragment.isHidden){
            ft.show(fragment)
        }else{
            ft.add(containerId, fragment,tag)
        }
        ft.commitAllowingStateLoss()
        attr.setCurrent(index)
    }

    /**
     * 获取一个存在的fragment
     */
    fun getFragment(attr: FragmentAttr, index:Int):BaseFragment?{
        val tag = attr.getFragmentTag(index) ?: return null
        val m  = attr.fm ?: return null
        var f = m.findFragmentByTag(tag) //管理器中获取
        if (f == null) {
            f = cache[tag]  //缓存获取
        }
       if(f!=null) return f as? BaseFragment
       return null;
    }

    /**
     * 创建碎片
     */
    private fun create(name:String): Fragment? {
        try {
            val fragment = Class.forName(name).newInstance() as Fragment
            cache.put(name,fragment)
            return fragment
        } catch (e: Exception) {
            Ms.Holder.get().error(e)
        }
        return null
    }

    /**
     * 移除缓存
     */
    fun removeCache(tag:String){
        cache.remove(tag)
    }

    /**
     *  回退栈 - 添加fragment
     * 1 判断当前fragment是否存在子页面( 即: baseFragment - child fragment boolean-flag )
     * 2 如果没有子页面正在打开中, 隐藏
     * 3 添加碎片到回退栈中
     *
     * 1 隐藏当前fragment
     */
    fun addFragmentToBackStack(containerId:Int,tag:String,attr:FragmentAttr,bundle:Bundle?){
        val m  = attr.fm ?: return
        val fragment = create(tag) ?: return
        hideCurrent(attr)
        if(bundle!=null){
            fragment.arguments = bundle
        }
        // 开启事务
        val beginTransaction = m
                .beginTransaction()
        // 执行事务,添加Fragment
        beginTransaction.add(containerId, fragment, tag)
        // 添加到回退栈,并定义标记
        beginTransaction.addToBackStack(tag)
        // 提交事务
        beginTransaction.commit()

    }
    /**
     * 回退栈 - 移除fragment
     */
    fun removeFragmentToBackStack(attr:FragmentAttr){
        val m  = attr.fm ?: return
        // 获取当前回退栈中的Fragment个数
        var backStackEntryCount = m.getBackStackEntryCount()
        // 判断当前回退栈中的fragment个数,
        if (backStackEntryCount > 0) {
            // 立即回退一步
            m.popBackStackImmediate();
        }
        backStackEntryCount = m.getBackStackEntryCount()
        if (backStackEntryCount==0){
            showCurrent(attr)
        }
    }

    fun getFragmentToBackStack(attr:FragmentAttr):BaseFragment?{
        val m  = attr.fm ?: return null
        // 获取当前回退栈中的Fragment个数
        var backStackEntryCount = m.getBackStackEntryCount()
        if (backStackEntryCount>0){
            val backStack = m
                    .getBackStackEntryAt(backStackEntryCount - 1)
            // 获取当前栈顶的Fragment的标记值
            val tag = backStack.name
            val fragment = m.findFragmentByTag(tag)
            if (fragment!=null) return fragment as BaseFragment
        }
        return null
    }

    /**
     * 清空回退栈
     */
    fun clearBackStack(attr:FragmentAttr){
        val m  = attr.fm ?: return
        var backStackEntryCount = m.getBackStackEntryCount()
        while (backStackEntryCount > 0) {
            // 立即回退一步
            m.popBackStackImmediate();
            backStackEntryCount = m.getBackStackEntryCount()
        }
    }


}