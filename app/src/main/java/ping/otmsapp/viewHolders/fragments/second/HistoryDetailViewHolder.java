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
    @Rid(R.id.fragment_historydetail_tv_pathname)
    public TextView pathName;

    public ViewHolderRecycle list;

    public HistoryDetailViewHolder(Context context) {
        super(context, R.layout.fragment_historydetail);
    }

}
