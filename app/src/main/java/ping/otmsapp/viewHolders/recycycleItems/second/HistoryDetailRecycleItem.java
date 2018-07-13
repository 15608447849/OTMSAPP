package ping.otmsapp.viewHolders.recycycleItems.second;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import ping.otmsapp.R;
import ping.otmsapp.entitys.annotations.Rid;
import ping.otmsapp.entitys.interfaces.AutoRecycleViewHolder;

/**
 * Created by Leeping on 2018/4/9.
 * email: 793065165@qq.com
 */

public class HistoryDetailRecycleItem extends AutoRecycleViewHolder {
    @Rid(R.id.recycle_item_tv_storeName)
    public TextView storeName;

    @Rid(R.id.recycle_item_tv_boxNo)
    public TextView boxNo;

    @Rid(R.id.recycle_item_tv_address)
    public TextView address;

    @Rid(R.id.recycle_item_listview_boxList)
    public ListView boxList;

    public HistoryDetailRecycleItem(View itemView) {
        super(itemView);
    }
}
