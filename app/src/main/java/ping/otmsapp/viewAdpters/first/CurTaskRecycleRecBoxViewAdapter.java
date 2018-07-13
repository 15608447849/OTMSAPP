package ping.otmsapp.viewAdpters.first;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import ping.otmsapp.R;
import ping.otmsapp.entitys.dataBeans.dispatch.DistributionPathBean;
import ping.otmsapp.entitys.interfaces.RecycleAdapterAbs;
import ping.otmsapp.viewHolders.recycycleItems.first.CurTaskRecycleRecBoxItem;

/**
 * Created by lzp on 2018/2/28.
 * 关联视图 - CurTaskRecycleItem
 *
 */

public class CurTaskRecycleRecBoxViewAdapter extends RecycleAdapterAbs<DistributionPathBean,CurTaskRecycleRecBoxItem> {

    public CurTaskRecycleRecBoxViewAdapter(Context context) {
        super(context);
    }

    //创建视图持有者
    @Override
    public CurTaskRecycleRecBoxItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CurTaskRecycleRecBoxItem(getLayoutIdToView(R.layout.recycle_item_curtask_recyclebox, parent));
    }

    @Override
    public void onBindViewHolder(CurTaskRecycleRecBoxItem holder, int position) {
        final int index = position;

            DistributionPathBean distributionPathBean = mDatas.get(position);
            holder.storeTv.setText(distributionPathBean.getStoreName());
            //holder.boxNo.setText(AppUtil.stringForart("回收箱: %d >> ",distributionPathBean.getRecycleBoxList().size()));

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
