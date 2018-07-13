package ping.otmsapp.viewAdpters.second;

import android.content.Context;
import android.view.ViewGroup;

import ping.otmsapp.R;
import ping.otmsapp.entitys.dataBeans.dispatch.BoxBean;
import ping.otmsapp.entitys.interfaces.RecycleAdapterAbs;
import ping.otmsapp.viewHolders.recycycleItems.second.ScanDetailRecycleItem;

import static ping.otmsapp.utils.STATE.BOX_DEAL_LOAD;
import static ping.otmsapp.utils.STATE.BOX_DEAL_RECYCLE;
import static ping.otmsapp.utils.STATE.BOX_DEAL_UNLOAD;

/**
 * Created by Leeping on 2018/4/6.
 * email: 793065165@qq.com
 * 扫码详情页面 适配器
 */

public class ScanDetailRecycleViewAdapter extends RecycleAdapterAbs<BoxBean,ScanDetailRecycleItem>  {
    public ScanDetailRecycleViewAdapter(Context context) {
        super(context);
    }
    @Override
    public ScanDetailRecycleItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScanDetailRecycleItem(getLayoutIdToView(R.layout.recycle_item_scandetail, parent));
    }
    @Override
    public void onBindViewHolder(ScanDetailRecycleItem holder, int position) {
        BoxBean boxBean = getData(position);
        holder.boxNo.setText(boxBean.getBarCode());
        if (boxBean.getState() == BOX_DEAL_LOAD){
            holder.boxtate.setText("[待装箱]");
        } else if (boxBean.getState() == BOX_DEAL_UNLOAD){
            holder.boxtate.setText("[待卸货]");
        }else if (boxBean.getState() == BOX_DEAL_RECYCLE){
            holder.boxtate.setText("[已签收]");
        }
    }
}
