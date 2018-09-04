package ping.otmsapp.assemblys.fragments.imps.secondPage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ping.otmsapp.assemblys.fragments.base.BaseFragment;
import ping.otmsapp.entitys.dataBeans.history.PathInfoBean;
import ping.otmsapp.entitys.dataBeans.sys.MemoryStoreBean;
import ping.otmsapp.viewAdpters.second.HistoryDetailRecycleViewAdapter;
import ping.otmsapp.viewHolders.fragments.second.HistoryDetailViewHolder;

/**
 * Created by Leeping on 2018/4/4.
 * email: 793065165@qq.com
 *
 */

public class HistoryDetail extends BaseFragment {
    private HistoryDetailViewHolder viewHolder;
    private HistoryDetailRecycleViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (viewHolder==null){
            adapter = new HistoryDetailRecycleViewAdapter(mContext);
            viewHolder = new HistoryDetailViewHolder(mContext);

            viewHolder.list.setRecyclerDefaultSetting(adapter,2);
        }
        return viewHolder.getLayoutFileRid();
    }

    @Override
    protected boolean onExit() {
        return true;
    }

    @Override
    public void onActivityCallback(MemoryStoreBean memoryStore) {
        super.onActivityCallback(memoryStore);
        final PathInfoBean pathInfoBean = memoryStore.get("history",null);
        toIO(new Runnable() {
            @Override
            public void run() {

                final StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(pathInfoBean.getStateStr()).append("\t").append(pathInfoBean.getStartAddr()).append(" → ").append(pathInfoBean.getEndAddr());
                adapter.addDataList(pathInfoBean.getStoreInfoBeanList());
                toUi(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder.pathName.setText(stringBuffer.toString());
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

    }
}
