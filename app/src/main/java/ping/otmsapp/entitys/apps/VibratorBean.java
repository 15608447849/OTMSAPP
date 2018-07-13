package ping.otmsapp.entitys.apps;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

/**
 * Created by user on 2018/3/19.
 *  手机震动
 */

public class VibratorBean {
    //震动需要的
    private Vibrator vibrator;
    private Context context;

    public VibratorBean(Context context) {
        this.context = context;
    }

    //开始震动
    public void startVibrator() {
        if (vibrator == null) {
            vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        }
        //long型数组内的数组依次表示：等待0.2秒、震动0.3秒、等待0.2秒、震动0.3秒，0表示无限循环long型数组内定义的震动规则；如果是-1则表示不循环震动
        vibrator.vibrate(new long[]{200, 300, 200, 300}, -1);
    }

    //结束震动
    public void stopVibrator() {
        if (vibrator != null) {
            if (vibrator.hasVibrator()) {
                vibrator.cancel();
            }
            vibrator = null;
        }
    }

}
