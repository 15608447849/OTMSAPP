package ping.otmsapp.viewHolders.recycycleItems.first;

import android.view.View;
import android.widget.TextView;

import ping.otmsapp.R;
import ping.otmsapp.entitys.annotations.Rid;
import ping.otmsapp.entitys.interfaces.AutoRecycleViewHolder;

/**
 * Created by user on 2018/3/7.
 * 警告报警子项
 * R.layout.recycle_item_realwarn
 */
public class RealWarnRecycleItem extends AutoRecycleViewHolder {

    @Rid(R.id.realwarn_recycle_item_tv_title)
    public TextView titleTv; //箱号:xxxxx (冷藏箱)

    @Rid(R.id.realwarn_recycle_item_tv_platenumber)
    public TextView  plateTv;//车牌号



    @Rid(R.id.realwarn_recycle_item_tv_warn_time)
    public TextView warnTimeTv;//预警时间
    @Rid(R.id.realwarn_recycle_item_tv_warn_info_value)
    public TextView warnInfoValueTv;//预警说明和预警值
    @Rid(R.id.realwarn_recycle_item_tv_warn_range)
    public TextView warnRangeTv; //预警正常范围

    public RealWarnRecycleItem(View itemView) {
        super(itemView);
    }
}
