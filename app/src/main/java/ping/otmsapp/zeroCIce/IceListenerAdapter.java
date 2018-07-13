package ping.otmsapp.zeroCIce;

import ping.otmsapp.utils.Ms;

/**
 * Created by user on 2018/3/8.
 */

public abstract class IceListenerAdapter<T> implements IceListener<T>{
    @Override
    public void onError(Exception e) {
        Ms.Holder.get().error(e);
    }
}
