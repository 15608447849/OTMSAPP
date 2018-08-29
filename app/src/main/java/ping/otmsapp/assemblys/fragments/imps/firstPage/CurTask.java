package ping.otmsapp.assemblys.fragments.imps.firstPage;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import ping.otmsapp.R;
import ping.otmsapp.assemblys.dialogs.DialogBuilder;
import ping.otmsapp.assemblys.fragments.base.BaseFragment;
import ping.otmsapp.entitys.apps.MediaBean;
import ping.otmsapp.entitys.dataBeans.dispatch.BoxBean;
import ping.otmsapp.entitys.dataBeans.dispatch.DispatchBean;
import ping.otmsapp.entitys.dataBeans.dispatch.DistributionPathBean;
import ping.otmsapp.entitys.dataBeans.dispatch.ScannerBoxHandleBean;
import ping.otmsapp.entitys.dataBeans.sys.MemoryStoreBean;
import ping.otmsapp.entitys.interfaces.Action0;
import ping.otmsapp.entitys.interfaces.OnRecyclerViewAdapterItemClickListener;
import ping.otmsapp.entitys.interfaces.ScanBoxHandleCallback;
import ping.otmsapp.entitys.maps.location.LocationGather;
import ping.otmsapp.utils.AppUtil;
import ping.otmsapp.utils.Ms;
import ping.otmsapp.viewAdpters.first.CurTaskRecycleViewAdapter;
import ping.otmsapp.viewHolders.fragments.first.CurTaskViewHolder;

import static ping.otmsapp.utils.STATE.BOX_DEAL_LOAD;
import static ping.otmsapp.utils.STATE.BOX_DEAL_UNLOAD;
import static ping.otmsapp.utils.STATE.DISPATCH_DEAL_BACK;
import static ping.otmsapp.utils.STATE.DISPATCH_DEAL_COMPLETE;
import static ping.otmsapp.utils.STATE.DISPATCH_DEAL_LOAD;
import static ping.otmsapp.utils.STATE.DISPATCH_DEAL_TAKEOUT;
import static ping.otmsapp.utils.STATE.DISPATCH_DEAL_UNLOAD;
import static ping.otmsapp.utils.STATE.STORE_DEAL_COMPLETE;
import static ping.otmsapp.utils.STATE.STORE_DEAL_LOAD;
import static ping.otmsapp.utils.STATE.STORE_DEAL_UNLOAD;

/**
 * Created by user on 2018/2/27.
 * * 驾驶员 - 当前任务
 */
public class CurTask extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, OnRecyclerViewAdapterItemClickListener, ScanBoxHandleCallback, CurTaskRecycleViewAdapter.ButtonCheck {
    private ReentrantLock lock = new ReentrantLock();//重入锁
    private CurTaskViewHolder viewHolder;
    private CurTaskRecycleViewAdapter adapter;//集装箱 适配器
    private int curRadioButtonId = -1; //当前选中的radiobutton
    private ScannerBoxHandleBean scanHandle;//扫码
    private MediaBean mediaBean;//音乐播放
    private LocationGather locationGather;//轨迹采集




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (viewHolder == null) {
            mediaBean = new MediaBean(mContext);//音乐
            locationGather = new LocationGather();
            adapter = new CurTaskRecycleViewAdapter(mContext);
            adapter.setOnItemClickListener(this);
            adapter.setButtonClicked(this);
            viewHolder = new CurTaskViewHolder(mContext);
            viewHolder.list.setRecyclerDefaultSetting(adapter, 1); //设置适配器
            viewHolder.setOnClickListener(this);
            viewHolder.radioGroup.setOnCheckedChangeListener(this);
            scanHandle = new ScannerBoxHandleBean(); //扫码处理
            scanHandle.setScanBoxHandleCallback(this);
            viewHolder.radioGroup.check(viewHolder.load.getId()); //默认选中第一项
        }
        return viewHolder.getLayoutFileRid();
    }

    @Override
    public void onDestroyView() {
        scanHandle.setScanBoxHandleCallback(null);
        scanHandle = null;
        mediaBean.destroy();
        viewHolder.destroy();
        viewHolder = null;
        super.onDestroyView();
    }

    /**
     * tab 选中改变回调
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (curRadioButtonId == checkedId) return;
        curRadioButtonId = checkedId;
        onRefreshList();
    }
    private void toUnloadState(){

        DispatchBean d = scanHandle.getDispatchBean();
        for (DistributionPathBean store : d.getDistributionPathBean()){
            for (BoxBean box : store.getBoxNoList()){
                box.setState(BOX_DEAL_UNLOAD);
            }
            store.setState(STORE_DEAL_UNLOAD);
            store.setUnloadScanIndex(0);

        }
        d.setUnloadScanBoxIndex(0);
        d.setState(DISPATCH_DEAL_UNLOAD);
        d.save();
    }

    private void toLoadState(){
        DispatchBean d = scanHandle.getDispatchBean();
        for (DistributionPathBean store : d.getDistributionPathBean()){
            for (BoxBean box : store.getBoxNoList()){
                box.setState(BOX_DEAL_LOAD);
            }
            store.setState(STORE_DEAL_LOAD);
            store.setLoadScanIndex(0);
        }
        d.setLoadScanBoxIndex(0);
        d.setState(DISPATCH_DEAL_LOAD);
        d.save();
    }
    /**
     * 按钮点击
     */
    @Override
    public void onClick(View v) {
        if (!scanHandle.isDispatchExist()) {
            showLongSnackBar(viewHolder, "当前没有调度单信息");
            return;
        }
        final int vid = v.getId();
        if (vid == viewHolder.inputBtn.getId()){
            AppUtil.hideSoftInputFromWindow((Activity) mContext);
            final String boxNo = viewHolder.inputEt.getText().toString();
            if (!TextUtils.isEmpty(boxNo)){
                if (boxNo.equals("1")){
                    toLoadState();
                    return;
                }
//                if (boxNo.equals("2")){
//                    toUnloadState();
//                    return;
//                }
                scanner(boxNo);
                viewHolder.inputEt.setText("");
            }
        }else if (vid == viewHolder.takeOut.getId()) {
            DialogBuilder.INSTANCE.dialogSimple(mContext, "启程将开启GPS定位,确定准备好了吗?", new Action0() {
                @Override
                public void onAction() {
                    pool.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!scanHandle.setTakeOutState()) {
                                showSnackBar(viewHolder, "启程失败,"+scanHandle.getDispatchStateString());
                            }
                        }
                    });
                }
            });
        }else if (vid == viewHolder.abnormal.getId()) {
            DialogBuilder.INSTANCE.dialogSimple(mContext, "货物遗失或损坏无法正常签收?", new Action0() {
                @Override
                public void onAction() {
                    pool.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!scanHandle.setHandWord()) {
                                showSnackBar(viewHolder, "异常签收失败,"+scanHandle.getDispatchStateString());
                            }
                        }
                    });
                }
            });

        } else if (vid == viewHolder.back.getId()) {
            DialogBuilder.INSTANCE.dialogSimple(mContext, "将结束本次行程,确定您已成功返回仓库?", new Action0() {
                @Override
                public void onAction() {
                    pool.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!scanHandle.setBackState()) {
                                showSnackBar(viewHolder, "返程失败,"+scanHandle.getDispatchStateString());
                            }
                        }
                    });
                }
            });
        }else if (vid == viewHolder.addRecycle.getId()){
            //跳转到回收fragment
            startFragment(8);
        }
    }

    /**
     * 刷新列表
     */
    private void onRefreshList() {
        pool.post(new Runnable() {
            @Override
            public void run() {
                try {
                    lock.lock();
                    //list show
                    listDisplayData();
                } finally {
                    lock.unlock();
                }
            }
        });
    }
    //集装箱 列表 显示
    private void listDisplayData() {
        adapter.clearAll(); //清空适配器
        if (scanHandle.isDispatchExist()) {

            final DispatchBean dispatchBean = scanHandle.getDispatchBean();
            int state = dispatchBean.getState();
            adapter.setCurrentType(state);
            List<DistributionPathBean> list = dispatchBean.getDistributionPathBean();//获取门店对象
            if (curRadioButtonId == viewHolder.load.getId()) {
//                filter(list,STORE_DEAL_LOAD);
                adapter.setShowType(DISPATCH_DEAL_LOAD);//显示待装载列表
                adapter.addDataList(list);
            } else if (curRadioButtonId == viewHolder.unload.getId()) {
                filter(list,STORE_DEAL_UNLOAD);
                adapter.setShowType(DISPATCH_DEAL_UNLOAD);//显示待卸货列表
                adapter.addDataList(list);
            }
        }
        toUi(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();//通知适配器更新列表
            }
        });
    }

    //过滤非指定状态门店
    private void filter(List<DistributionPathBean> list, int state) {
        Iterator<DistributionPathBean> iterator = list.iterator();
        while (iterator.hasNext()){
            if (iterator.next().getState()!=state) iterator.remove();
        }
    }

    /**
     * 扫描
     */
    public void scanner(final String boxNo) {
        if (adapter != null && scanHandle.isDispatchExist()) {
            sendMessageToActivity(5);//震动
            pool.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        scanHandle.scannerHandler(boxNo);
                    } catch (Exception e) {
                        e.printStackTrace();
                        showSnackBar(viewHolder,"扫码异常");
                    }
                }
            });

        }
    }
    //fragmrnt 在 resume时, activity回调此方法
    @Override
    public void onActivityCallback(MemoryStoreBean memoryStore) {
        super.onActivityCallback(memoryStore);
        //碎片 onresume 之后 -> 判断是否存在适配器:存在扫码数据处理,存在调度单-通知处理
        if (adapter != null && scanHandle != null && scanHandle.isDispatchExist()) {
            scanHandle.checkData();//检测数据
            checkTab();
            mediaBean.replay(R.raw.msg);
            sendMessageToActivity(5);//震动
            showLongSnackBar(viewHolder, "请及时处理调度单,"+scanHandle.getDispatchStateString());
        }else{
            onRefreshList();
        }
    }

    private boolean checkSelectTab(final int tabId){
        return curRadioButtonId == tabId;
    }

    private void selectTab(int tabId){
        if (checkSelectTab(tabId)) {
            //刷新列表
            onRefreshList();
        } else {
            //页面跳转到指定tab列表
            switchTargetTab(tabId);
        }
    }

    private void switchTargetTab(final int tabId) {
        toUi(new Runnable() {
            @Override
            public void run() {
                viewHolder.radioGroup.check(tabId);
            }
        });
    }

    private void checkTab() {
        int state = scanHandle.getDispatchBean().getState();
        if (state == DISPATCH_DEAL_LOAD){
            selectTab(viewHolder.load.getId());
        }else if (state == DISPATCH_DEAL_UNLOAD){
            selectTab(viewHolder.unload.getId());
        }
    }


    @Override
    public void onActivityCallback(String str) {
        if (isVisible()) { //是否正在显示中
            scanner(str);
        }
    }
    /**
     * list item 选中 - 勾选/取消勾选
     */
    @Override
    public void onItemClick(View view, final int position) {
        adapter.setSelectPosition(position);//设置适配器勾选图标选中
        adapter.notifyDataSetChanged();//更新
    }

    @Override
    public void onClicked(View view, int pos) {
        memoryStoreBean.put("store", adapter.getDataList().get(pos));
        startFragment(7);
    }
    //开始扫描门店
    @Override
    public boolean starScanStore(DispatchBean dispatchBean, DistributionPathBean distributionPathBean) {

        if (dispatchBean.getState() == DISPATCH_DEAL_LOAD) { //装载

            boolean flag =  checkSelectTab(viewHolder.load.getId());
            if (flag){
                int checkItemPos = adapter.getCheckPos();
                if (checkItemPos == -1) {
                    scanHandle.loadBreak(true);
                    showSnackBar(viewHolder, "请选中指定门店进行装货扫描");
                } else {
                    DistributionPathBean select = adapter.getData(checkItemPos);
                    Ms.Holder.get().debug("装货扫描 "+ distributionPathBean.getCustomerAgency()+" - "+ select.getCustomerAgency());
                    //选中的门店
                    if (distributionPathBean.getCustomerAgency().equals(select.getCustomerAgency())) {
                        Ms.Holder.get().debug("找到匹配的门店");
                        return true;
                    }
                }
            }else{
                showSnackBar(viewHolder, "请在相关页面进行操作");
            }
        }else if (dispatchBean.getState() == DISPATCH_DEAL_UNLOAD) { //卸载

            boolean flag = checkSelectTab(viewHolder.unload.getId());
            if (flag){
                int checkItemPos = adapter.getCheckPos();
                if (checkItemPos == -1) {
                    scanHandle.unloadBreak(true);
                    showSnackBar(viewHolder, "请选中指定门店进行卸货扫描");
                } else {
                    //选中的门店
                    if (distributionPathBean.getCustomerAgency().equals(adapter.getData(checkItemPos).getCustomerAgency())) {
                        return true;
                    }
                }
            }else{
                showSnackBar(viewHolder, "请在相关页面进行操作");
            }
        }
        return false;
    }
    @Override
    public void occurChange() {
        //扫码触发数据发生改变
        onRefreshList();
    }

    //改变门店状态
    @Override
    public void changeStoreState(DistributionPathBean distributionPathBean) {
        if (distributionPathBean.getState() == STORE_DEAL_UNLOAD) {
            showSnackBar(viewHolder, distributionPathBean.getStoreName() + " 全部装载");
        } else if (distributionPathBean.getState() == STORE_DEAL_COMPLETE) {
            showSnackBar(viewHolder, distributionPathBean.getStoreName() + " 全部卸载");
        }
        adapter.reset();//复位checkbox
    }

    //改变调度状态
    @Override
    public void changeDispatchState(DispatchBean dispatchBean) {
        if (dispatchBean.getState() == DISPATCH_DEAL_TAKEOUT) {
            showLongSnackBar(viewHolder, "点击启程,可请及时开启您的行程");
        } else if (dispatchBean.getState() == DISPATCH_DEAL_UNLOAD) {
            //从等待启程->卸货状态->卸载列表, 通知轨迹记录-开始记录
            locationGather.setGatherStart();
            selectTab(viewHolder.unload.getId());
            showLongSnackBar(viewHolder, "已开启行程,祝您一路平安");
        } else if (dispatchBean.getState() == DISPATCH_DEAL_BACK) {
            //自动或者手动签单 : 装卸状态->等待返程->卸货列表
            selectTab(viewHolder.unload.getId());
            showLongSnackBar(viewHolder, "如果已经完成卸货,可点击返程,结束您的任务");
        } else if (dispatchBean.getState() == DISPATCH_DEAL_COMPLETE) {
            //等待返程->任务完成,通知轨迹记录-结束记录
            locationGather.setGatherStop();
            selectTab(viewHolder.load.getId());
            showLongSnackBar(viewHolder, "已结束您的行程,您辛苦了");
        }
    }

    //扫码成功
    @Override
    public void boxScannerSuccess(String scanBarNo) {
        mediaBean.replay(R.raw.success);//发出警报
    }

    //没找到的扫码编号
    @Override
    public void boxScanNoFind(String scanBarNo) {
        mediaBean.replay(R.raw.nofount);//发出警报
        showLongSnackBar(viewHolder, AppUtil.stringForart("[ %s ]找不到相关信息",scanBarNo));
    }
    @Override
    public void boxScanRepeat(String scanBarNo) {
        mediaBean.replay(R.raw.warn);//发出警报
        showLongSnackBar(viewHolder, AppUtil.stringForart("[ %s ]重复扫码",scanBarNo));
    }

    @Override
    public void boxScanAbnormal(String scanBarNo) {
        mediaBean.replay(R.raw.warn);//发出警报
        showLongSnackBar(viewHolder, AppUtil.stringForart("[ %s ]扫码异常",scanBarNo));
    }
    /**
     * 手工签单
     */
    @Override
    public boolean handworkStore(DispatchBean dispatchBean, DistributionPathBean distributionPathBean) {
            int selectIndex = adapter.getCheckPos();
            if (selectIndex!=-1){
                DistributionPathBean distributionPathBean1 = adapter.getData(selectIndex);
                if (distributionPathBean1.getCustomerAgency().equals(distributionPathBean.getCustomerAgency())){
                    return true;
                }
            }else{
                showSnackBar(viewHolder,"请选择门店进行手工签收");
            }
        return false;
    }

}
