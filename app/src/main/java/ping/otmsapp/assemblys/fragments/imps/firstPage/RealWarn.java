package ping.otmsapp.assemblys.fragments.imps.firstPage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Iterator;
import java.util.List;

import ping.otmsapp.assemblys.dialogs.DialogBuilder;
import ping.otmsapp.assemblys.fragments.base.BaseFragment;
import ping.otmsapp.entitys.dataBeans.sys.MemoryStoreBean;
import ping.otmsapp.entitys.dataBeans.warn.WarnState;
import ping.otmsapp.entitys.dataBeans.warn.WarnTag;
import ping.otmsapp.entitys.interfaces.Action0;
import ping.otmsapp.entitys.interfaces.OnRecyclerViewAdapterItemClickListener;
import ping.otmsapp.utils.AppUtil;
import ping.otmsapp.viewAdpters.first.RealWarnRecycleViewAdapter;
import ping.otmsapp.viewHolders.fragments.first.RealWarnViewHolder;
import ping.otmsapp.zeroCIce.IceIo;

/**
 * Created by user on 2018/2/27.
 * 实时预警
 */

public class RealWarn extends BaseFragment implements OnRecyclerViewAdapterItemClickListener {
    private RealWarnRecycleViewAdapter adapter;
    private RealWarnViewHolder viewHolder;
    private volatile boolean isSendHandleMsg = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (viewHolder==null) {
            viewHolder = new RealWarnViewHolder(mContext);
            adapter = new RealWarnRecycleViewAdapter(mContext);
            adapter.setOnItemClickListener(this);
            viewHolder.list.setRecyclerDefaultSetting(adapter,101);
        }
        return viewHolder.getLayoutFileRid();
    }


    @Override
    public void onResume() {
        super.onResume();
        onRefreshList();
    }


    public void onActivityCallback(MemoryStoreBean memoryStore){
        super.onActivityCallback(memoryStore);
        onRefreshList();
    }

    private void onRefreshList() {
        toIO(new Runnable() {
            @Override
            public void run() {
                WarnTag warnTag = new WarnTag().fetch();
                if (warnTag!=null){
                    List<WarnState> list = warnTag.getWarnStateList();
                    adapter.clearAll();
                    if (list!=null && list.size()>0){
                        adapter.addDataList(list);
                        warnTag.reset().save();
                    }
                    toUi(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
//                                viewHolder.list.recycler.smoothScrollToPosition(adapter.getItemCount()-1);
                            sendMessageToActivity(4);
                        }
                    });
                }
            }
        });
    }
    @Override
    public void onItemClick(View view, int position) {
        if (isSendHandleMsg) {
            showLongSnackBar(viewHolder,"正在请求服务器中,请稍后再试.");
            return;
        }
        //点击弹出处理预警消息
        final WarnState warnState = adapter.getData(position);
        DialogBuilder.INSTANCE.dialogSimple(mContext,
                AppUtil.stringForart("箱号:(%s)是否已成功处理?",
                        warnState.getBoxNumber()), new Action0() {
            @Override
            public void onAction0() {
                sendMessageToActivity(0,0);//打开进度条
                isSendHandleMsg = true;
                toIO(new Runnable() {
                    @Override
                    public void run() {
                        boolean flag = IceIo.get().changeWarnState(warnState.getBoxNumber(),warnState.getTime());
                        if (flag){
                            WarnTag warnTag = new WarnTag().fetch();
                            if (warnTag!=null){
                                List<WarnState> list = warnTag.getWarnStateList();
                                if (list!=null && list.size()>0){
                                    Iterator<WarnState> it = warnTag.getWarnStateList().iterator();
                                    while (it.hasNext()){
                                        if(it.next().getBoxNumber().equals(warnState.getBoxNumber())) it.remove();
                                    }
                                }
                                warnTag.save();
                            }
                            onRefreshList();
                            showLongSnackBar(viewHolder,AppUtil.stringForart("箱号:(%s) 处理成功",warnState.getBoxNumber()));
                        }
                        toUi(new Runnable() {
                            @Override
                            public void run() {
                                sendMessageToActivity(2);//关闭进度条
                                isSendHandleMsg = false;
                            }
                        });
                    }
                });
            }
        });
    }
    @Override
    public void onDestroyView() {
        sendMessageToActivity(2);//关闭进度条
        viewHolder.destroy();
        viewHolder=null;
        super.onDestroyView();
    }


}
