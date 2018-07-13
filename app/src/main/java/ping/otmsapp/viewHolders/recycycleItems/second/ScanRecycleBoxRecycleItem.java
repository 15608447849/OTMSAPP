package ping.otmsapp.viewHolders.recycycleItems.second;

import android.view.View;
import android.widget.TextView;

import ping.otmsapp.R;
import ping.otmsapp.entitys.annotations.Rid;
import ping.otmsapp.entitys.interfaces.AutoRecycleViewHolder;

/**
 * Created by Leeping on 2018/4/10.
 * email: 793065165@qq.com
 */

public class ScanRecycleBoxRecycleItem extends AutoRecycleViewHolder {
    //箱号
    @Rid(R.id.recycle_item_tv_boxNo)
    public TextView boxNoTv;

    //类型
    @Rid(R.id.recycle_item_tv_boxState)
    public TextView scannerStateTv;



       public ScanRecycleBoxRecycleItem(View itemView) {
        super(itemView);
    }
}
