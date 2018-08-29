package ping.otmsapp.viewAdpters.second;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import ping.otmsapp.R;
import ping.otmsapp.entitys.dataBeans.dispatch.RecycleBoxBean;
import ping.otmsapp.entitys.interfaces.RecycleAdapterAbs;
import ping.otmsapp.utils.AppUtil;
import ping.otmsapp.viewHolders.recycycleItems.second.ScanRecycleBoxRecycleItem;

/**
 * Created by Leeping on 2018/4/10.
 * email: 793065165@qq.com
 */

public class ScanRecycleBoxRecycleAdapter extends RecycleAdapterAbs<RecycleBoxBean,ScanRecycleBoxRecycleItem> {
    public ScanRecycleBoxRecycleAdapter(Context context) {
        super(context);
    }
    @Override
    public ScanRecycleBoxRecycleItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScanRecycleBoxRecycleItem(getLayoutIdToView(R.layout.recycle_item_scanrecyclebox, parent));
    }
    @Override
    public void onBindViewHolder(final ScanRecycleBoxRecycleItem holder, int position) {
        RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)holder.itemView.getLayoutParams();
       final int index = position;
       final RecycleBoxBean recycleBox = getData(index);
        if (recycleBox.isBlank()){
            param.height = 0;
            param.width = 0;
            holder.itemView.setVisibility(View.GONE);
        }else{
            param.height = FrameLayout.LayoutParams.WRAP_CONTENT;
            param.width = FrameLayout.LayoutParams.MATCH_PARENT;
            holder.itemView.setVisibility(View.VISIBLE);
            holder.boxNoTv.setText(AppUtil.stringForart("%s",recycleBox.getBoxNo()));
            holder.scannerStateTv.setText(AppUtil.stringForart("[%s]",recycleBox.getRecycleTypeString()));
        }
        holder.itemView.setLayoutParams(param);
    }
}
