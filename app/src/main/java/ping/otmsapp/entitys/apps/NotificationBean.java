package ping.otmsapp.entitys.apps;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import ping.otmsapp.R;


/**
 * Created by lzp on 2018/2/12.
 * 通知栏
 * 将服务置为前台服务
 * https://www.cnblogs.com/zyw-205520/archive/2013/04/18/3028208.html
 * https://blog.csdn.net/vipzjyno1/article/details/25248021
 */

public class NotificationBean {
    private Notification notification;
    private NotificationManager notificationManager;

    private int id;

    private static final String NOTIFICATION_CHANNEL_ID = "location.foreground.service";
    private static final String NOTIFICATION_CHANNEL_NAME = "ANDROID CHANNEL";

    public NotificationBean(Context context,int id, Class<?> cls,String action,String scheme,String nTitle,String nContent,String nInfo,int nDef) {
        this.id = id;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            notificationChannel.enableLights(true);//是否在桌面icon右上角展示小圆点
            notificationChannel.setLightColor(Color.RED);//小圆点颜色
            notificationChannel.enableVibration(true);//允许震动
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        //点击通知栏-跳转到这里.
        Intent intent = null;
        if (cls!=null){
            intent = new Intent(context, cls);
        }else if (action!=null && scheme!=null){
            intent = new Intent(action, Uri.parse(scheme));
        }
        PendingIntent pIntent = null;
        if (intent!=null){
            pIntent = PendingIntent.getActivity(context,0,intent,0);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        notification = notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_launcher)
                //.setTicker("ticker")
                 .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(nTitle) //
                .setContentText(nContent) //
                .setContentInfo(nInfo) //
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pIntent)
                .build();

//        Notification.DEFAULT_ALL    使用所有默认值，比如声音，震动，闪屏等等
//        DEFAULT_LIGHTS 使用默认闪光提示
//        DEFAULT_SOUNDS 使用默认提示声音
//        DEFAULT_VIBRATE 使用默认手机震动
        notification.defaults = nDef;//Notification.DEFAULT_SOUND;//设置为默认声音
//        FLAG_AUTO_CANCEL  该通知能被状态栏的清除按钮给清除掉
//        FLAG_NO_CLEAR     该通知能被状态栏的清除按钮给清除掉
//        FLAG_ONGOING_EVENT 通知放置在正在运行
//        FLAG_INSISTENT 是否一直进行，比如音乐一直播放，知道用户响应
        notification.flags = Notification.FLAG_ONGOING_EVENT;
    }

    public Notification getNotification() {
        return notification;
    }
    public void showNotification() {
        notificationManager.notify(id, notification);
    }
    public void cancelNotification() {
        notificationManager.cancel(id);
    }
}
