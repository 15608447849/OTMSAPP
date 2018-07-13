package ping.otmsapp.entitys.interfaces;


import android.os.Handler;

/**
 * Created by user on 2018/2/25.
 * 视图控制
 *
 */

public abstract class ViewControlAbs {
    protected Handler handler;
    public ViewControlAbs(Handler handler) {
        this.handler = handler;
    }

    public Handler getHandler() {
        return handler;
    }

    public void postUi(Runnable runnable){
        if (handler!=null) handler.post(runnable);
    }
    public void destroy(){
        if (handler!=null) handler = null;
    }
}
