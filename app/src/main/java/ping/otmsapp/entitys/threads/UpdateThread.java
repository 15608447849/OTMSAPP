package ping.otmsapp.entitys.threads;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;

import java.io.File;
import java.io.RandomAccessFile;

import cn.hy.otms.rpcproxy.sysmanage.UpdateResponsePackage;
import ping.otmsapp.R;
import ping.otmsapp.utils.AppUtil;
import ping.otmsapp.zeroCIce.IceIo;

/**
 * Created by Leeping on 2018/6/6.
 * email: 793065165@qq.com
 * 更新apk
 */

public class UpdateThread extends Thread{
    private Context context;
    private Handler handler;
    public UpdateThread(Context context) {
        this.context = context;
        this.setDaemon(true);
        this.setName("update");
        handler = new Handler(Looper.getMainLooper());
        start();
    }

    @Override
    public void run() {
        while (true){
            check();
            try { synchronized (this){this.wait(1000*60*60);} } catch (InterruptedException ignored) {}
        }
    }

    private void check() {
        try{
            if (!checkWifiState()) return;
            int remoteVersionCode = getRemoteVersionCode();
            int localVersionCode = getLocalVersionCode();
            if (remoteVersionCode>localVersionCode){
                final String apkPath = downloadApk();
                if (apkPath==null) return;

               /* alertUpdateMessage(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (which == DialogInterface.BUTTON_POSITIVE){
                            sendUpdateInfoToSys(apkPath);
                        }else if (which == DialogInterface.BUTTON_NEGATIVE){
                           handler.post(new Runnable() {
                               @Override
                               public void run() {
                                   Toast.makeText(context,"当前版本,可能存在潜在问题,建议更新应用",Toast.LENGTH_LONG).show();
                               }
                           });
                        }
                    }
                });*/
                sendUpdateInfoToSys(apkPath);
            }

        }catch (Exception ignored){
//            ignored.printStackTrace();
        }
    }


    public int getRemoteVersionCode() {
        UpdateResponsePackage updateResponsePackage =
                IceIo.get().getDataBytes("app.version",1);
        if (updateResponsePackage!=null && updateResponsePackage.status == 0){
            String versionStr = new String(updateResponsePackage.packageInfo);
            return Integer.parseInt(versionStr);
        }
        return -1;
    }

    public int getLocalVersionCode() {
        return AppUtil.getVersionCode(context);
    }

    private boolean checkWifiState() {
        return AppUtil.isOpenWifi(context) && AppUtil.isNetworkAvailable(context);
    }

    private String downloadApk() throws Exception{
        String filename = "app.apk";
        String filePath = Environment.getExternalStorageDirectory().getPath()+ File.separator+filename;
        File apkFile = new File(filePath);
        if (apkFile.exists()) apkFile.delete();
        RandomAccessFile accessFile = new RandomAccessFile(apkFile,"rw");
        int index = 1;
        while (true){
            UpdateResponsePackage updateResponsePackage =
                    IceIo.get().getDataBytes(filename,index);
            if (updateResponsePackage==null || updateResponsePackage.status!=0) return null;
            accessFile.seek((updateResponsePackage.index-1) * updateResponsePackage.size); //起点
            accessFile.write(updateResponsePackage.packageInfo);
            index++;
            if (index>updateResponsePackage.total) break;
        }
        accessFile.close();
        return filePath;
    }



    private void alertUpdateMessage(final DialogInterface.OnClickListener listener) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // 设置提示框的标题
                builder.setTitle("版本更新").
                        setIcon(R.drawable.ic_launcher).
                        setMessage("发现新版本,请及时更新!").
                        setPositiveButton("确定", listener).setNegativeButton("取消", listener);
                AlertDialog alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
                alertDialog.show();
            }
        });

    }

    private void sendUpdateInfoToSys(String apkPath) {
        AppUtil.updateApk(context,apkPath);
    }
}
