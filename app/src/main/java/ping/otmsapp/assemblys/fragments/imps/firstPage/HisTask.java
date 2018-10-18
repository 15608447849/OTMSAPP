package ping.otmsapp.assemblys.fragments.imps.firstPage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;

import cn.hy.otms.rpcproxy.appInterface.AppSchedvech;
import ping.otmsapp.assemblys.fragments.base.BaseFragment;
import ping.otmsapp.entitys.dataBeans.history.QueryInfoBean;
import ping.otmsapp.entitys.dataBeans.login.LoginUserBean;
import ping.otmsapp.entitys.dataBeans.tuples.Tuple2;
import ping.otmsapp.entitys.interfaces.OnRecyclerViewAdapterItemClickListener;
import ping.otmsapp.utils.AppUtil;
import ping.otmsapp.utils.Convert;
import ping.otmsapp.utils.Ms;
import ping.otmsapp.viewAdpters.first.HisTaskRecycleViewAdapter;
import ping.otmsapp.viewHolders.fragments.first.HisTaskViewHolder;
import ping.otmsapp.zeroCIce.IceIo;

/**
 * Created by lzp on 2018/2/27.
 * 历史任务
 */

public class HisTask extends BaseFragment implements OnRecyclerViewAdapterItemClickListener, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private HisTaskViewHolder viewHolder;
    private HisTaskRecycleViewAdapter adapter;
    protected Tuple2<ArrayList<String>,ArrayList<String>> monthsData;
    protected int curPos = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (viewHolder==null) {
            monthsData = AppUtil.initMonths();
            curPos = monthsData.getValue1().size()-1;
            viewHolder = new HisTaskViewHolder(mContext);
            viewHolder.niceSpinner.attachDataSource(monthsData.getValue1());//绑定月份
            viewHolder.niceSpinner.addOnItemClickListener(this);
            adapter = new HisTaskRecycleViewAdapter(mContext);
            adapter.setOnItemClickListener(this);
            viewHolder.list.recycle.setRecyclerDefaultSetting(adapter,1);
            viewHolder.list.refresh.setOnRefreshListener(this);
            viewHolder.niceSpinner.setSelectedIndex(curPos);//选中最后一项
        }
        return viewHolder.getLayoutFileRid();
    }
    @Override
    public void onStart() {
        super.onStart();
        onRefresh();
    }
    @Override
    public void onDestroyView() {
        viewHolder.destroy();
        viewHolder=null;
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        callServerData();
    }
    //下拉框选中回调
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        curPos = position;
        callServerData();
    }
    /**
     * 获取服务器信息
     */
    public void callServerData() {
        viewHolder.list.refresh.setRefreshing(true);
        IceIo.get().pool.post(new Runnable() {
            @Override
            public void run() {
                LoginUserBean loginUserBean = new LoginUserBean().fetch();
                if (loginUserBean!=null){
                    AppSchedvech[] result = IceIo.get().getHistoryTask(
                            loginUserBean.getUserCode(),
                            monthsData.getValue0().get(curPos));
                    resultHandle(result,"暂无数据");
                }else{
                    resultHandle(null,"异常登录");
                }

            }
        });
    }
    //结果处理
    public void resultHandle(AppSchedvech[] result, String msg){
        adapter.clearAll();
        try {
            if (result!=null && result.length>0) {
                adapter.addDataList(Convert.historyRecodeTrans(result));
            }else{
                showSnackBar(viewHolder,msg);//显示消息
            }
        } catch (Exception e) {
            Ms.Holder.get().error(e);
            showSnackBar(viewHolder,"数据异常");
        }
        toUi(new Runnable() {
            @Override
            public void run() {
                viewHolder.list.refresh.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        });
    }
    /**
     * list 子项点击回调
     */
    @Override
    public void onItemClick(View view, int position) {
       QueryInfoBean pathInfoBean = adapter.getData(position);
        if (memoryStoreBean!=null) memoryStoreBean.put("history",pathInfoBean);
       startFragment(6,null);
    }

}
