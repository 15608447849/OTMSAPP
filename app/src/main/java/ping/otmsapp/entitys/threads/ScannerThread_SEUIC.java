package ping.otmsapp.entitys.threads;

import android.content.Context;

import com.seuic.scanner.DecodeInfo;
import com.seuic.scanner.DecodeInfoCallBack;
import com.seuic.scanner.ScannerFactory;
import com.seuic.scanner.ScannerKey;

import ping.otmsapp.entitys.apps.MediaBean;
import ping.otmsapp.entitys.interfaces.ScannerCallback;

/**
 * Created by Leeping on 2018/3/23.
 * email: 793065165@qq.com
 * 扫描控制
 */

public class ScannerThread_SEUIC extends Thread implements DecodeInfoCallBack {
    private com.seuic.scanner.Scanner scanner;
    private boolean flag = false;

    private MediaBean mediaBean;
    private ScannerCallback callback;
    public ScannerThread_SEUIC(Context context) {
        setName("scanner-"+ getId());
        setDaemon(true);
        mediaBean = new MediaBean(context);
        scanner = ScannerFactory.getScanner(context);
        scanner.setDecodeInfoCallBack(this);
        flag = true;
        start();
    }


    public void close(){

        flag = false;
        mediaBean.destroy();
        scanner.setDecodeInfoCallBack(null);
        ScannerKey.close();
        //                scanner.disable(); //扫描头禁用
//                scanner.close(); //堵塞线程 为什么不知道

    }

    private boolean isContinuationMode = false; //连续模式

    public void setContinuationMode(boolean continuation) {
        isContinuationMode = continuation;
    }

    private boolean isDown =false;
    @Override
    public void run() {
        scanner.enable();
        scanner.open();
        try {
            int ret1 = ScannerKey.open();
            if (ret1 > -1) {
                while (flag) {
                    int ret = ScannerKey.getKeyEvent();
                    if (ret > -1) {
                        switch (ret) {
                            case ScannerKey.KEY_DOWN:
                                if (isContinuationMode){
                                    if (!isDown){
                                        scanner.startScan();
                                    }else{
                                        scanner.stopScan();
                                    }
                                }else{
                                    scanner.startScan();
                                }

                                break;
                            case ScannerKey.KEY_UP:
                                if (!isContinuationMode){
                                    scanner.stopScan();
                                }
                                break;
                        }
                    }
                }
            }
        } catch (Exception e) {

        }finally {
            ScannerKey.close();
        }
    }

    @Override
    public void onDecodeComplete(DecodeInfo decodeInfo) {
//        mediaBean.play(R.raw.warn);
        if (callback!=null){
            callback.onScanner(decodeInfo.barcode);
        }
    }

    public void setCallback(ScannerCallback callback) {
        this.callback = callback;
    }


}
