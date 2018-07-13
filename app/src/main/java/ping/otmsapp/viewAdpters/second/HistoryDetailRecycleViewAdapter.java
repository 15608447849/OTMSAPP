package ping.otmsapp.viewAdpters.second;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import ping.otmsapp.R;
import ping.otmsapp.entitys.dataBeans.history.StoreInfoBean;
import ping.otmsapp.entitys.interfaces.RecycleAdapterAbs;
import ping.otmsapp.utils.AppUtil;
import ping.otmsapp.viewHolders.recycycleItems.second.HistoryDetailRecycleItem;

/**
 * Created by Leeping on 2018/4/9.
 * email: 793065165@qq.com
 */

public class HistoryDetailRecycleViewAdapter  extends RecycleAdapterAbs<StoreInfoBean,HistoryDetailRecycleItem> {
    public HistoryDetailRecycleViewAdapter(Context context) {
        super(context);
    }

    @Override
    public HistoryDetailRecycleItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistoryDetailRecycleItem(getLayoutIdToView(R.layout.recycle_item_historydetail, parent));
    }

    @Override
    public void onBindViewHolder(HistoryDetailRecycleItem holder, int position) {
            StoreInfoBean storeInfoBean = getData(position);
            holder.storeName.setText(storeInfoBean.getSimName());
            holder.address.setText(storeInfoBean.getAddress());
            holder.boxNo.setText(AppUtil.stringForart("[%d]",storeInfoBean.getSumBoxNo()));
            holder.boxList.setAdapter( new ArrayAdapter(context,android.R.layout.simple_list_item_1,storeInfoBean.getBoxList()));
    }
}
