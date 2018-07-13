package ping.otmsapp.viewHolders.recycycleItems.first;

import android.view.View;
import android.widget.TextView;

import ping.otmsapp.R;
import ping.otmsapp.entitys.annotations.Rid;
import ping.otmsapp.entitys.interfaces.AutoRecycleViewHolder;

/**
 * Created by user on 2018/3/1.
 * R.id.recycle_item_feightbill
 */

public class FreightBillRecycleItem extends AutoRecycleViewHolder {

    @Rid(R.id.recycle_item_tv_trainNo_value)
    public TextView trainNoTv; //车次号
    @Rid(R.id.recycle_item_tv_trainNo_state)
    public TextView trainState;// 状态
    @Rid(R.id.recycle_item_tv_boxSumNo_value)
    public TextView boxNoTv; //箱子总数
    @Rid(R.id.recycle_item_tv_customNo_value)
    public TextView customTv; // 客户总数
    @Rid(R.id.recycle_item_tv_pathAddr_value)
    public TextView pathAddrTv; //起点-终点
    @Rid(R.id.recycle_item_tv_transTime)
    public TextView transTimeTv; //运输时间

    public FreightBillRecycleItem(View itemView) {
        super(itemView);
    }
}
