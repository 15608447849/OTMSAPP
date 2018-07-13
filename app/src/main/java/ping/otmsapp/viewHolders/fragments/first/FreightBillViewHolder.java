package ping.otmsapp.viewHolders.fragments.first;

import android.content.Context;
import android.widget.TextView;

import ping.otmsapp.R;
import ping.otmsapp.entitys.annotations.Rid;
import ping.otmsapp.entitys.interfaces.ViewHolderAbs;
import ping.otmsapp.viewHolders.includeLayouts.ViewHolderRefreshRecycle;
import ping.otmsapp.viewHolders.includeLayouts.ViewHolderSpnner;

/**
 * Created by user on 2018/3/5.
 * R.layout.recycle_item_freightbill
 */

public class FreightBillViewHolder extends ViewHolderAbs {
    public ViewHolderSpnner spnner;

    @Rid(R.id.freightbill_displaybox_item_tv_totalcost)
    public TextView totalCostText;
    @Rid(R.id.freightbill_displaybox_item_tv_transportationcost)
    public TextView transportationCostText;
    @Rid(R.id.freightbill_displaybox_item_tv_foreignexpense)
    public TextView abnormalCostText;

    public ViewHolderRefreshRecycle list;

    public FreightBillViewHolder(Context context) {
        super(context, R.layout.fragment_freightbill);
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
