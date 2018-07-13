package ping.otmsapp.viewHolders.recycycleItems.first;

import android.view.View;
import android.widget.TextView;

import ping.otmsapp.R;
import ping.otmsapp.entitys.annotations.Rid;
import ping.otmsapp.entitys.interfaces.AutoRecycleViewHolder;

/**
 * Created by Leeping on 2018/4/10.
 * email: 793065165@qq.com
 * 回收任务适配器
 */

public class CurTaskRecycleRecBoxItem  extends AutoRecycleViewHolder {
    @Rid(R.id.recycle_item_tv_storeName)
    public TextView storeTv;

    @Rid(R.id.recycle_item_tv_boxNo)
    public TextView boxNo;

    public CurTaskRecycleRecBoxItem(View itemView) {
        super(itemView);
    }
}
