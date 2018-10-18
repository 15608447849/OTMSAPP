package ping.otmsapp.viewHolders.recycycleItems.first;

import android.view.View;
import android.widget.Button;
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
    @Rid(R.id.recycle_item_tv_boxSumNo_value)
    public TextView boxNoTv; //箱子总数
    @Rid(R.id.recycle_item_tv_customNo_value)
    public TextView customTv; // 客户总数
    @Rid(R.id.recycle_item_tv_mileage_value)
    public TextView mileageTv; //总里程
    @Rid(R.id.recycle_item_tv_plate_value)
    public TextView plateTv; //运输车牌
    @Rid(R.id.recycle_item_tv_cost_value)
    public TextView costFeeTv; //应结时间
    @Rid(R.id.recycle_item_tv_actual_value)
    public TextView actualFeeTv; //实结时间
    @Rid(R.id.recycle_item_btn_reject)
    public Button rejectBtn;
    @Rid(R.id.recycle_item_btn_sure)
    public Button sureBtn;
    @Rid(R.id.recycle_item_btn_update)
    public Button updateBtn;



    public FreightBillRecycleItem(View itemView) {
        super(itemView);
    }
}
