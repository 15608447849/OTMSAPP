package ping.otmsapp.viewHolders.fragments.second;

import android.content.Context;
import android.widget.TextView;

import ping.otmsapp.R;
import ping.otmsapp.entitys.annotations.Rid;
import ping.otmsapp.entitys.interfaces.ViewHolderAbs;
import ping.otmsapp.viewHolders.includeLayouts.ViewHolderRecycle;

/**
 * Created by Leeping on 2018/4/4.
 * email: 793065165@qq.com
 */

public class HistoryDetailViewHolder extends ViewHolderAbs{
    @Rid(R.id.fragment_historydetail_tv_trainNo_value)
    public TextView pathName;

    @Rid(R.id.freightbill_displaybox_item_tv_totalcost)
    public TextView totalCostText;
    @Rid(R.id.freightbill_displaybox_item_tv_transportationcost)
    public TextView transportationCostText;
    @Rid(R.id.freightbill_displaybox_item_tv_foreignexpense)
    public TextView abnormalCostText;

    public ViewHolderRecycle list;

    public HistoryDetailViewHolder(Context context) {
        super(context, R.layout.fragment_historydetail);
        totalCostText.setSelected(true);
        transportationCostText.setSelected(true);
        abnormalCostText.setSelected(true);
    }



    public void setText(String tol, String trans, String abor) {
        totalCostText.setText(tol);
        transportationCostText.setText(trans);
        abnormalCostText.setText(abor);
    }

}
