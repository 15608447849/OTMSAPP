package ping.otmsapp.viewAdpters.first;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

import ping.otmsapp.R;
import ping.otmsapp.entitys.dataBeans.history.QueryInfoBean;
import ping.otmsapp.entitys.interfaces.RecycleAdapterAbs;
import ping.otmsapp.viewHolders.recycycleItems.first.HisTaskRecycleItem;

import static ping.otmsapp.utils.AppUtil.stringForart;

/**
 * Created by user on 2018/3/1.
 * 历史任务列表
 * 关联视图 HisTaskRecycleItem
 */

public class HisTaskRecycleViewAdapter extends RecycleAdapterAbs<QueryInfoBean,HisTaskRecycleItem> {

    public HisTaskRecycleViewAdapter(Context context) {
        super(context);
    }

    @Override
    public HisTaskRecycleItem onCreateViewHolder(ViewGroup parent, int viewType) {

        return new HisTaskRecycleItem(getLayoutIdToView(R.layout.recycle_item_histask, parent));
    }

    @Override
    public void onBindViewHolder(HisTaskRecycleItem holder, int position) {

        final int index = position;
        QueryInfoBean task = mDatas.get(position);
        holder.trainNoTv.setText(stringForart("车次号: %s",task.getTrainNumber()));
        holder.trainState.setText(stringForart("[ %s ]",task.getStateStr()));
        holder.feeTv.setText(stringForart("总费用: %.2f",task.getTotalFreight()));
        holder.customTv.setText(stringForart("客户数: %d",task.getCustorNo()));
        holder.boxNoTv.setText(String.format(Locale.getDefault(),"总箱数:  %d",task.getBoxSumNo()));

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
