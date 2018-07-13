package ping.otmsapp.zeroCIce;

/**
 * Created by user on 2018/3/8.
 */

public interface IceListener<T> {
     void onResult(T result);
     void onError(Exception e);
}
