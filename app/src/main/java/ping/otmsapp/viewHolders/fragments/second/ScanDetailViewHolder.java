package ping.otmsapp.viewHolders.fragments.second;

import android.content.Context;
import android.widget.TextView;

import ping.otmsapp.R;
import ping.otmsapp.entitys.annotations.Rid;
import ping.otmsapp.entitys.interfaces.ViewHolderAbs;
import ping.otmsapp.viewHolders.includeLayouts.ViewHolderRecycle;

/**
 * Created by Leeping on 2018/4/6.
 * email: 793065165@qq.com
 * 扫码详情视图持有者
 */

public class ScanDetailViewHolder extends ViewHolderAbs {

    @Rid(R.id.fragment_scandetail_tv_storeName)
    public TextView storeAndScanNoTv;

    @Rid(R.id.fragment_scandetail_tv_addr)
    public TextView addressTv;



    public ViewHolderRecycle viewHolderRecycle;

    public ScanDetailViewHolder(Context context) {
        super(context, R.layout.fragment_scandetail);
        addressTv.setSelected(true);
    }
}
