package ping.otmsapp.assemblys.fragments.imps.secondPage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ping.otmsapp.assemblys.fragments.base.BaseFragment;
import ping.otmsapp.entitys.dataBeans.dispatch.DistributionPathBean;
import ping.otmsapp.entitys.dataBeans.sys.MemoryStoreBean;
import ping.otmsapp.viewAdpters.second.ScanDetailRecycleViewAdapter;
import ping.otmsapp.viewHolders.fragments.second.ScanDetailViewHolder;

import static ping.otmsapp.utils.STATE.STORE_DEAL_COMPLETE;
import static ping.otmsapp.utils.STATE.STORE_DEAL_LOAD;
import static ping.otmsapp.utils.STATE.STORE_DEAL_UNLOAD;

/**
 * Created by Leeping on 2018/4/6.
 * email: 793065165@qq.com
 */

public class ScanDetail extends BaseFragment {
    private ScanDetailViewHolder viewHolder;
    private ScanDetailRecycleViewAdapter adapter;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (viewHolder==null){
            viewHolder = new ScanDetailViewHolder(mContext);
            adapter = new ScanDetailRecycleViewAdapter(mContext);
            viewHolder.viewHolderRecycle.setRecyclerDefaultSetting(adapter,1);
        }
        return viewHolder.getLayoutFileRid();
    }
    @Override
    public boolean onExit() {
        return true;
    }
    @Override
    public void onActivityCallback(MemoryStoreBean memoryStore) {
        super.onActivityCallback(memoryStore);
        toIO(new Runnable() {
            @Override
            public void run() {
                DistributionPathBean distributionPathBean = (DistributionPathBean) memoryStoreBean.remove("store");;
                if (distributionPathBean!=null){
                    final StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append(distributionPathBean.getStoreName());
                    if (distributionPathBean.getState() == STORE_DEAL_LOAD){
                        stringBuffer.append("\t待装箱 [").append(distributionPathBean.getLoadScanIndex()).append("/").append(distributionPathBean.getBoxSum());
                    }else if (distributionPathBean.getState() == STORE_DEAL_UNLOAD){
                        stringBuffer.append("\t待卸货 [").append(distributionPathBean.getUnloadScanIndex()).append("/").append(distributionPathBean.getBoxSum());
                    }else if (distributionPathBean.getState() == STORE_DEAL_COMPLETE){
                        stringBuffer.append("\t已签收 [").append(distributionPathBean.getBoxSum());
                    }
                    stringBuffer.append("]");
                    final String addrStr = distributionPathBean.getDetailedAddress();
                    adapter.addDataList(distributionPathBean.getBoxNoList());
                    toUi(new Runnable() {
                        @Override
                        public void run() {
                            viewHolder.storeAndScanNoTv.setText(stringBuffer.toString());
                            viewHolder.addressTv.setText(addrStr);
                            adapter.notifyDataSetChanged();
                        }
                    });

                }

            }
        });

    }
}
