package ping.otmsapp.assemblys.services.connect;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by user on 2018/3/8.
 * 用于连接后台服务
 */

public class MyServiceConnection<T extends BinderAbs> implements ServiceConnection {
    protected Class<?> serviceClassType;
    private boolean isConnected;
    public <S extends Service> MyServiceConnection(Context context,Class<S> clazz) {
       this.serviceClassType = clazz;
        context.startService(new Intent(context,serviceClassType)); //打开服务
    }

    private T binder;
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        isConnected =true;
        binder = (T)service;
        if (listener!=null){
            listener.onServiceConnected(binder);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        isConnected = false;
        binder = null;
    }

    @Override
    public void onBindingDied(ComponentName name) {
        binder = null;
    }

    public void bind(Context context,ConnectedListener listener){
        if (!isConnected){
            setListener(listener);
            context.bindService( new Intent(context,serviceClassType),this, Context.BIND_ABOVE_CLIENT);
        }
    }
    public void unbind(Context context){
        if (isConnected){
            context.unbindService(this);
            listener = null;
            isConnected = false;
        }
    }

    public T getBinder() {
        return binder;
    }

    private ConnectedListener listener;

    public void setListener(ConnectedListener listener) {
        this.listener = listener;
    }

    public void stopServer(Context context) {
        unbind(context);
        context.stopService(new Intent(context,serviceClassType)); //关闭服务
    }


    /**
     * 连接成功监听
     *
     */
    public interface ConnectedListener{
        void onServiceConnected(BinderAbs binder);
    }
}
