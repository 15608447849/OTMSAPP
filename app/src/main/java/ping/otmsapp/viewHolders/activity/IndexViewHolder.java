package ping.otmsapp.viewHolders.activity;

import android.content.Context;
import android.view.View;

import ping.otmsapp.R;
import ping.otmsapp.entitys.annotations.Rid;
import ping.otmsapp.entitys.interfaces.ViewHolderAbs;
import ping.otmsapp.viewHolders.includeLayouts.ViewHolderBottomTab;
import ping.otmsapp.viewHolders.includeLayouts.ViewHolderTitle;

/**
 * Created by user on 2018/2/26.
 * R.layout.activity_index
 */
public class IndexViewHolder extends ViewHolderAbs {

    public ViewHolderTitle title;

    @Rid(R.id.index_container)
    public View fragmentContainer;

    public ViewHolderBottomTab bottom;
    public IndexViewHolder(Context context) {
        super(context, R.layout.activity_index);
    }


    @Override
    public void setOnClickListener(View.OnClickListener listener) {
        title.setOnClickListener(listener);
        bottom.setOnClickListener(listener); //设置底部按钮点击事件
    }
}
