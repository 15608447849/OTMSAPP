package ping.otmsapp.viewAdpters.first;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import ping.otmsapp.R;
import ping.otmsapp.entitys.dataBeans.history.QueryInfoBean;
import ping.otmsapp.entitys.interfaces.RecycleAdapterAbs;
import ping.otmsapp.utils.AppUtil;
import ping.otmsapp.viewHolders.recycycleItems.first.FreightBillRecycleItem;

/**
 * Created by user on 2018/3/5.
 * 关联视图 - FreightBillRecycleItem
 */

public class FreightBillRecycleViewAdapter extends RecycleAdapterAbs<QueryInfoBean,FreightBillRecycleItem> {
    public FreightBillRecycleViewAdapter(Context context) {
        super(context);
    }

    @Override
    public FreightBillRecycleItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FreightBillRecycleItem(getLayoutIdToView(R.layout.recycle_item_freightbill, parent));
    }

    @Override
    public void onBindViewHolder(FreightBillRecycleItem holder, int position) {
        final int index = position;
        QueryInfoBean task = mDatas.get(position);

        holder.trainNoTv.setText(AppUtil.stringForart("车次号: "+task.getTrainNumber()));
        holder.boxNoTv.setText(AppUtil.stringForart("总箱数: %d",task.getBoxSumNo()));
        holder.customTv.setText(AppUtil.stringForart("客户数: %d",task.getCustorNo()));
        holder.plateTv.setText(AppUtil.stringForart("车牌号: %s",task.getPlateNo()));
        holder.costFeeTv.setText(AppUtil.stringForart("应结费用: %s",task.getCostFreight()));
        holder.actualFeeTv.setText(AppUtil.stringForart("实结费用: %s",task.getActualFreight()));
        holder.mileageTv.setText(AppUtil.stringForart("总里程: %s",task.getMileage()));

        // 如果设置了回调，则设置点击事件
        if (listener!=null){
           View.OnClickListener l =  new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v,index);
                }
            };
            holder.itemView.setOnClickListener(l);
            holder.rejectBtn.setOnClickListener(l);
            holder.sureBtn.setOnClickListener(l);
            holder.updateBtn.setOnClickListener(l);
        }
    }

}
