package ping.otmsapp.viewHolders.recycycleItems.first;

import android.view.View;
import android.widget.TextView;

import ping.otmsapp.R;
import ping.otmsapp.entitys.annotations.Rid;
import ping.otmsapp.entitys.interfaces.AutoRecycleViewHolder;

/**
 * Created by user on 2018/3/1.
 * R.id.recycle_item_histask
 */

public class HisTaskRecycleItem extends AutoRecycleViewHolder {


    @Rid(R.id.recycle_item_tv_trainNo_value)
    public TextView trainNoTv;
    @Rid(R.id.recycle_item_tv_trainNo_state)
    public TextView trainState;
    @Rid(R.id.recycle_item_tv_boxSumNo_value)
    public TextView boxNoTv;
    @Rid(R.id.recycle_item_tv_customNo_value)
    public TextView customTv;
    @Rid(R.id.recycle_item_tv_fee_value)
    public TextView feeTv;
    public HisTaskRecycleItem(View itemView) {
        super(itemView);
        feeTv.setSelected(true);
    }
}
