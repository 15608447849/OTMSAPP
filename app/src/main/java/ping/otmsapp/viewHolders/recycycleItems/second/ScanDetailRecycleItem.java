package ping.otmsapp.viewHolders.recycycleItems.second;

import android.view.View;
import android.widget.TextView;

import ping.otmsapp.R;
import ping.otmsapp.entitys.annotations.Rid;
import ping.otmsapp.entitys.interfaces.AutoRecycleViewHolder;

/**
 * Created by Leeping on 2018/4/6.
 * email: 793065165@qq.com
 */

public class ScanDetailRecycleItem extends AutoRecycleViewHolder {
    @Rid(R.id.recycle_item_tv_boxNo)
    public TextView boxNo;

    @Rid(R.id.recycle_item_tv_boxState)
    public TextView boxtate;

    public ScanDetailRecycleItem(View itemView) {
        super(itemView);
    }



}
