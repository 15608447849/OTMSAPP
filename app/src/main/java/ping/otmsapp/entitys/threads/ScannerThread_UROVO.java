package ping.otmsapp.entitys.threads;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.KeyMapManager;
import android.device.ScanManager;
import android.device.scanner.configuration.PropertyID;
import android.util.Log;
import android.widget.EditText;

import com.seuic.scanner.DecodeInfo;
import com.seuic.scanner.DecodeInfoCallBack;
import com.seuic.scanner.ScannerFactory;
import com.seuic.scanner.ScannerKey;

import java.util.concurrent.SynchronousQueue;

import ping.otmsapp.entitys.apps.MediaBean;
import ping.otmsapp.entitys.interfaces.ScannerCallback;
import ping.otmsapp.utils.Ms;

import static android.device.KeyMapManager.KEY_TYPE_STARTAC;

/**
 * Created by Leeping on 2018/3/23.
 * email: 793065165@qq.com
 * 扫描控制
 */

public class ScannerThread_UROVO extends Thread   {

    private Context context;
    private MediaBean mediaBean;
    private ScannerCallback callback;
    ScanManager mScanManager;
    private SynchronousQueue<String> query  = new SynchronousQueue<>();
    public ScannerThread_UROVO(Context context) {
        setName("scanner-"+ getId());
        setDaemon(true);
        this.context = context;
        mediaBean = new MediaBean(context);
        mScanManager = new ScanManager();
        mScanManager.openScanner();
        mScanManager.switchOutputMode( 0);
        IntentFilter filter = new IntentFilter();
        int[] idbuf = new int[]{PropertyID.WEDGE_INTENT_ACTION_NAME, PropertyID.WEDGE_INTENT_DATA_STRING_TAG};
        String[] value_buf = mScanManager.getParameterString(idbuf);
        if(value_buf != null && value_buf[0] != null && !value_buf[0].equals("")) {
            filter.addAction(value_buf[0]);
        } else {
            filter.addAction(ScanManager.ACTION_DECODE);
        }
        context.registerReceiver(mScanReceiver,filter);
        start();
    }


    public void close(){
        context.unregisterReceiver(mScanReceiver);
        mediaBean.destroy();
        context=null;

    }
    @Override
    public void run() {
        while (context!=null){
            try {
                String code = query.take();
                Log.w("扫码",code);
                if(this.callback!=null) this.callback.onScanner(code);
            } catch (Exception ignored) { }
        }

    }
    public boolean isEnable = false;
    public void isEnable(boolean flag){
        this.isEnable = flag;
    }

    public void setCallback(ScannerCallback callback) {
        this.callback = callback;
    }

    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            byte[] barcode = intent.getByteArrayExtra(ScanManager.DECODE_DATA_TAG);
            int barcodelen = intent.getIntExtra(ScanManager.BARCODE_LENGTH_TAG, 0);
            String barcodeStr = new String(barcode, 0, barcodelen);

            try{
                if (isEnable){
                    query.add(barcodeStr);
                    mScanManager.stopDecode();
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }

    };


}
