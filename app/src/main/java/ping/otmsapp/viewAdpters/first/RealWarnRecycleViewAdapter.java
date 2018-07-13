package ping.otmsapp.viewAdpters.first;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import ping.otmsapp.R;
import ping.otmsapp.entitys.dataBeans.warn.WarnState;
import ping.otmsapp.entitys.interfaces.RecycleAdapterAbs;
import ping.otmsapp.utils.AppUtil;
import ping.otmsapp.viewHolders.recycycleItems.first.RealWarnRecycleItem;

/**
 * Created by user on 2018/3/7.
 */

public class RealWarnRecycleViewAdapter extends RecycleAdapterAbs<WarnState,RealWarnRecycleItem> {



    public RealWarnRecycleViewAdapter(Context context) {
        super(context);
    }

    @Override
    public RealWarnRecycleItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RealWarnRecycleItem(getLayoutIdToView(R.layout.recycle_item_realwarn, parent));
    }

    @Override
    public void onBindViewHolder(final RealWarnRecycleItem holder, final int position) {
        WarnState state = mDatas.get(position);
        holder.titleTv.setText(AppUtil.stringForart("箱号: %s (%s)",state.getBoxNumber(),state.getBoxTypeStr()));
        holder.plateTv.setText(state.getPlateNumber());
        holder.warnTimeTv.setText(AppUtil.formatUTC(state.getTime(),"yyyy/MM/dd HH:mm:ss"));
        holder.warnInfoValueTv.setText(AppUtil.stringForart("%s(%s)",state.getWarnInfo(),state.getWarnValue()));
        holder.warnRangeTv.setText(state.getWarnRange());
        if (listener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(holder.itemView,position);
                }
            });

        }
    }
}
