package ping.otmsapp.viewAdpters.first;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import ping.otmsapp.R;
import ping.otmsapp.entitys.dataBeans.history.PathInfoBean;
import ping.otmsapp.entitys.interfaces.RecycleAdapterAbs;
import ping.otmsapp.utils.AppUtil;
import ping.otmsapp.viewHolders.recycycleItems.first.FreightBillRecycleItem;

/**
 * Created by user on 2018/3/5.
 * 关联视图 - FreightBillRecycleItem
 */

public class FreightBillRecycleViewAdapter extends RecycleAdapterAbs<PathInfoBean,FreightBillRecycleItem> {
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
        PathInfoBean task = mDatas.get(position);

        holder.trainNoTv.setText(AppUtil.stringForart("车次: "+task.getTrainNumber()));
        holder.trainState.setText(AppUtil.stringForart("[%s]",task.getStateStr()));
        holder.boxNoTv.setText(AppUtil.stringForart("箱数: %d",task.getBoxSumNo()));
        holder.customTv.setText(AppUtil.stringForart("客户数: %d",task.getCustorNo()));
        holder.pathAddrTv.setText(AppUtil.stringForart("%s → %s",task.getStartAddr(),task.getEndAddr()));
        holder.transTimeTv.setText(AppUtil.stringForart("运输日期: %s",task.getTimeStr()));



        // 如果设置了回调，则设置点击事件
        if (listener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v,index);
                }
            });
        }
    }

}
