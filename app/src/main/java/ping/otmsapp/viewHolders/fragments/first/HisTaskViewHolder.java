package ping.otmsapp.viewHolders.fragments.first;

import android.content.Context;

import ping.otmsapp.R;
import ping.otmsapp.entitys.annotations.Rid;
import ping.otmsapp.entitys.interfaces.ViewHolderAbs;
import ping.otmsapp.viewHolders.includeLayouts.ViewHolderRefreshRecycle;

/**
 * Created by user on 2018/3/1.
 * R.layout.fragment_histask
 */

public class HisTaskViewHolder extends ViewHolderAbs {
    @Rid(R.id.spinner)
    public org.angmarch.views.NiceSpinner niceSpinner;

    public ViewHolderRefreshRecycle list;
    public HisTaskViewHolder(Context context) {
        super(context, R.layout.fragment_histask);
    }

}
