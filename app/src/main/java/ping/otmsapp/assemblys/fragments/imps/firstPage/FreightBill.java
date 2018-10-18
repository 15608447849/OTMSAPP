package ping.otmsapp.assemblys.fragments.imps.firstPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;

import cn.hy.otms.rpcproxy.appInterface.SureFeeInfo;
import ping.otmsapp.R;
import ping.otmsapp.assemblys.activitys.PictureUp;
import ping.otmsapp.assemblys.fragments.base.BaseFragment;
import ping.otmsapp.entitys.dataBeans.history.QueryInfoBean;
import ping.otmsapp.entitys.dataBeans.login.LoginUserBean;
import ping.otmsapp.entitys.dataBeans.tuples.Tuple2;
import ping.otmsapp.entitys.interfaces.OnRecyclerViewAdapterItemClickListener;
import ping.otmsapp.utils.AppUtil;
import ping.otmsapp.utils.Convert;
import ping.otmsapp.utils.Ms;
import ping.otmsapp.viewAdpters.first.FreightBillRecycleViewAdapter;
import ping.otmsapp.viewHolders.fragments.first.FreightBillViewHolder;
import ping.otmsapp.zeroCIce.IceIo;

/**
 * Created by user on 2018/2/27.
 * 运费账单
 *
 */

public class FreightBill extends BaseFragment implements OnRecyclerViewAdapterItemClickListener, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener{
    private FreightBillViewHolder viewHolder;
    private FreightBillRecycleViewAdapter adapter;
    protected Tuple2<ArrayList<String>,ArrayList<String>> monthsData;
    protected int curPos = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (viewHolder==null){
            monthsData = AppUtil.initMonthsDate();
//            curPos = monthsData.getValue1().size()-1;
            viewHolder = new FreightBillViewHolder(mContext);
            viewHolder.niceSpinner.attachDataSource(monthsData.getValue1());
            viewHolder.niceSpinner.addOnItemClickListener(this);
            adapter = new FreightBillRecycleViewAdapter(mContext);
            adapter.setOnItemClickListener(this);
            viewHolder.list.recycle.setRecyclerDefaultSetting(adapter,1);
            viewHolder.list.refresh.setOnRefreshListener(this);
            viewHolder.niceSpinner.setSelectedIndex(curPos);//选中第一项
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
                    SureFeeInfo[] result = IceIo.get().getFreightBill(
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
    private void resultHandle(SureFeeInfo[] result,String msg){

        adapter.clearAll();
        try {
            if (result!=null && result.length>0) {
                adapter.addDataList(Convert.freightBillTrans(result));
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
        final QueryInfoBean pathInfoBean = adapter.getData(position);
//        Ms.Holder.get().debug("view: "+ view);
//        viewHolder.list.recycle.recycler.smoothScrollToPosition(0);
        if (view.getId() == R.id.recycle_item_btn_reject){
            toIO(new Runnable() {
                @Override
                public void run() {
                    LoginUserBean loginUserBean = new LoginUserBean().fetch();
                    IceIo.get().changeFeeState(loginUserBean.getUserCode(),pathInfoBean.getTrainNumber(),0);

                    toUi(new Runnable() {
                        @Override
                        public void run() {
                            onRefresh();
                        }
                    });
                }
            });

        }else
        if (view.getId() == R.id.recycle_item_btn_sure){
            toIO(new Runnable() {
                @Override
                public void run() {
                    LoginUserBean loginUserBean = new LoginUserBean().fetch();
                    IceIo.get().changeFeeState(loginUserBean.getUserCode(),pathInfoBean.getTrainNumber(),2);
                    toUi(new Runnable() {
                        @Override
                        public void run() {
                            onRefresh();
                        }
                    });
                }
            });
        }else if (view.getId() == R.id.recycle_item_btn_update){
            //打开弹窗
            startActivity(new Intent(mContext, PictureUp.class));
        }
    }
}
