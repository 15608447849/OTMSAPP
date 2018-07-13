package ping.otmsapp.viewHolders.includeLayouts;

import android.content.Context;
import android.view.View;

import ping.otmsapp.R;
import ping.otmsapp.entitys.annotations.Rid;
import ping.otmsapp.entitys.interfaces.ViewHolderAbs;

/**
 * Created by Leeping on 2018/4/6.
 * email: 793065165@qq.com
 */

public class ViewHolderRefreshRecycle extends ViewHolderAbs {
    public ViewHolderRecycle recycle;

    @Rid(R.id.swipe_refresh)
    public android.support.v4.widget.SwipeRefreshLayout refresh;
    public ViewHolderRefreshRecycle(Context context, View viewRoot) {
        super(context,viewRoot);
    }
}
