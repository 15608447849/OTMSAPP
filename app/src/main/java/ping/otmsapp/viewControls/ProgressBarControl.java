package ping.otmsapp.viewControls;

import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import ping.otmsapp.entitys.interfaces.ViewControlAbs;

/**
 * Created by user on 2018/3/6.
 * 进度条控制
 */

public class ProgressBarControl extends ViewControlAbs {

    private ProgressBar progressBar;
    public ProgressBarControl(Handler handler, ProgressBar progressBar) {
        super(handler);
        this.progressBar = progressBar;
    }
    //是个正在显示中
    public boolean isShowing(){
        return progressBar.getVisibility() == View.VISIBLE;
    }
    //显示进度条
    public void showProgressBar(){
        postUi(new Runnable() {
            @Override
            public void run() {
                if (progressBar.getVisibility() == View.GONE)  {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });

    }
    //隐藏进度条
    public void hideProgressBar(){
        postUi(new Runnable() {
            @Override
            public void run() {
                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                    if (!progressBar.isIndeterminate()){
                        progressBar.setProgress(0);
                    }
                }
            }
        });
    }

    public void setProgressBarIndex(final int progress) {
        postUi(new Runnable() {
            @Override
            public void run() {
                if (progressBar.getVisibility() == View.VISIBLE){
                    progressBar.setProgress(progress);
                }
            }
        });

    }

}
