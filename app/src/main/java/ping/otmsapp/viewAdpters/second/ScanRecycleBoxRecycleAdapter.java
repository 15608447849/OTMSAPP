package ping.otmsapp.viewAdpters.second;

import android.content.Context;
import android.view.ViewGroup;

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
       final int index = position;
        final RecycleBoxBean recycleBox = getData(index);

        holder.boxNoTv.setText(AppUtil.stringForart("箱号: %s",recycleBox.getBoxNo()));
        holder.scannerStateTv.setText(AppUtil.stringForart("[%s]",recycleBox.getRecycleTypeString()));
    }
}
