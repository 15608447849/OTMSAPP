package ping.otmsapp.viewHolders.includeLayouts;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import ping.otmsapp.R;
import ping.otmsapp.entitys.annotations.Rid;
import ping.otmsapp.entitys.interfaces.ViewHolderAbs;

/**
 * Created by user on 2018/2/27.
 * R.layout.layout_title
 */

public class ViewHolderTitle extends ViewHolderAbs {
    @Rid(R.id.title_iv_close)
    public ImageView exit;
    @Rid(R.id.title_progressBar)
    public ProgressBar progressBar;
    @Rid(R.id.title_iv_back)
    public ImageView back;
    public ViewHolderTitle(Context context, View viewRoot) {
        super(context,viewRoot);
    }
    @Override
    public void setOnClickListener(View.OnClickListener listener) {
        exit.setVisibility(View.VISIBLE);
        exit.setOnClickListener(listener);
        back.setOnClickListener(listener);
    }
    public void setProgressMax(int v){
        progressBar.setMax(v);
        progressBar.setProgress(0);
    }
    public void setProgressIndeterminate(boolean f){
        progressBar.setIndeterminate(f);
    }
}
