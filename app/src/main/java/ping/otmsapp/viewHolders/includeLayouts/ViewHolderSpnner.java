package ping.otmsapp.viewHolders.includeLayouts;

import android.content.Context;
import android.view.View;

import ping.otmsapp.R;
import ping.otmsapp.entitys.annotations.Rid;
import ping.otmsapp.entitys.interfaces.ViewHolderAbs;

/**
 * Created by Leeping on 2018/4/3.
 * email: 793065165@qq.com
 */

public class ViewHolderSpnner extends ViewHolderAbs {
    @Rid(R.id.spinner)
    public org.angmarch.views.NiceSpinner niceSpinner;

    public ViewHolderSpnner(Context context, View viewRoot) {
        super(context,viewRoot);
    }


}
