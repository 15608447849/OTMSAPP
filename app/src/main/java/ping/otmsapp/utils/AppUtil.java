package ping.otmsapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Looper;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationQualityReport;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.Contract;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import ping.otmsapp.entitys.dataBeans.tuples.Tuple2;

/**
 * Created by user on 2018/2/25.
 */

public class AppUtil {
    private static SimpleDateFormat sdf = null;

    public static String formatUTC(long l, String strPattern) {
        if (TextUtils.isEmpty(strPattern)) {
            strPattern = "yyyy-MM-dd HH:mm:ss";
        }
        if (sdf == null) {
            try {
                sdf = new SimpleDateFormat(strPattern, Locale.CHINA);
            } catch (Throwable e) {
            }
        } else {
            sdf.applyPattern(strPattern);
        }
        return sdf == null ? "NULL" : sdf.format(l);
    }

    /**
     * @param context 上下文
     * @return 仅仅是用来判断网络连接
     */
    @Contract("null -> false")
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            try {
                return cm.getActiveNetworkInfo().isAvailable();
            } catch (Exception e) {
            }
        }
        return false;
    }
    //是否打开wifi
    public static boolean isOpenWifi(Context context) {
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return mWifiManager.isWifiEnabled();
    }

    /**
     * 获取GPS状态的字符串
     *
     * @param statusCode GPS状态码
     * @return
     */
    public static String getAMapGPSStatusString(int statusCode) {
        String str = "";
        switch (statusCode) {
            case AMapLocationQualityReport.GPS_STATUS_OK:
                str = "GPS状态正常";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
                str = "手机中没有GPS Provider，无法进行GPS定位";
                break;
            case AMapLocationQualityReport.GPS_STATUS_OFF:
                str = "GPS关闭，建议开启GPS，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_MODE_SAVING:
                str = "选择的定位模式中不包含GPS定位，建议选择包含GPS定位的模式，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
                str = "没有GPS定位权限，建议开启gps定位权限";
                break;
        }
        return str;
    }

    /**
     * 获取错误码原因
     *
     * @param errorCode GPS状态码
     * @return
     */
    public static String getAMapErrorString(int errorCode) {
        String str = "";
        switch (errorCode) {
            case AMapLocation.ERROR_CODE_AIRPLANEMODE_WIFIOFF:
                str = "定位失败，飞行模式下关闭了WIFI开关，请关闭飞行模式或者打开WIFI开关";
                break;
            case AMapLocation.ERROR_CODE_FAILURE_CONNECTION:
                str = "网络连接异常";
                break;
            case AMapLocation.ERROR_CODE_FAILURE_NOENOUGHSATELLITES:
                str = "GPS定位失败，可用卫星数不足";
                break;
        }
        return str;
    }
    //获取版本号
    public static int getVersionCode(Context ctx) {
        // 获取packagemanager的实例
        int version = 0;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
            version = packInfo.versionCode;
        } catch (Exception e) {
        }
        return version;
    }
    /**
     * json to javabean
     *
     * @param json
     */
    public static <T> T jsonToJavaBean(String json, Class<T> cls) {
        try {
            if (json == null || json.length() == 0) return null;
            return new Gson().fromJson(json, cls);//对于javabean直接给出class实例
        } catch (JsonSyntaxException e) {
        }
        return null;
    }

    /**
     * json to javabean
     *
     * @param json
     */
    public static <T> T jsonToJavaBean(String json, Type type) {
        try {
            if (json == null || json.length() == 0) return null;
            return new Gson().fromJson(json, type);//对于javabean直接给出class实例
        } catch (JsonSyntaxException e) {
        }
        return null;
    }

    /**
     * javabean to json
     *
     * @param object
     * @return
     */
    public static String javaBeanToJson(Object object) {
        return new Gson().toJson(object);
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    public static void hideSoftInputFromWindow(Activity activity) {
        try {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
        }
    }

    //错误输出
    public static String printExceptInfo(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        return writer.toString();
    }

    public static String stringForart(String format, Object... args) {
        return String.format(Locale.getDefault(), format, args);
    }

    /**
     * 初始化月份信息
     *
     * @return
     */
    public static Tuple2<ArrayList<String>, ArrayList<String>> initMonths() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();
        for (int i = 1; i <= month; i++) {
            if (i < 10) {
                list.add(year + "0" + i);
            } else {
                list.add(year + "" + i);
            }
            list2.add(AppUtil.stringForart("%d年\t%d月", year, i));
        }

        return new Tuple2<>(list, list2);
    }

    public static boolean checkUIThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * 判断区间
     */
    public static boolean justInterval(float v, int i, int i1) {
        return ((v >= i) && v < i1);
    }

    /**
     * 判断GPS
     */
    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOenGPS(final Context context) {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    public static void updateApk(Context context, String apkPath) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://"+apkPath),"application/vnd.android.package-archive");
            context.startActivity(intent);
    }
}