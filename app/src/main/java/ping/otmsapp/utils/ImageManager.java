package ping.otmsapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

import ping.otmsapp.entitys.apps.CacheStore;

/**
 * Created by lzp on 2018/3/6.
 * 图片管理器
 */

public class ImageManager implements CacheStore.CacheStoreListener<String,Bitmap>{
    private ImageManager() {
    }
    private static class Hodler{private static final ImageManager INSTANCE = new ImageManager(); }
    /** 获取CrashHandler实例 ,单例模式 */
    public static ImageManager getInstance() {
        return ImageManager.Hodler.INSTANCE;
    }

    //程序的Context对象
    private Context mContext;
    //设备分辨率信息
    private DisplayMetrics dm;
    //图片内存存储
    private CacheStore<String,Bitmap> cacheList;

    public void init(Context context){
        mContext = context;
        dm = mContext.getResources().getDisplayMetrics();
        cacheList = new CacheStore<>(20);
        cacheList.setListener(this);
        Ms.Holder.get().info(dm);
        Ms.Holder.get().info("图片管理器初始完成");
    }

    @Override
    public void onDelete(String key, Bitmap value) {
        value.recycle();
        Ms.Holder.get().info("图片 "+ key+" ,删除 "+ value+" - "+value.isRecycled());
    }


    public Bitmap getBitmap(int rid){
        if (mContext!=null){
            String k = mContext.getPackageName()+"#"+rid;
            Bitmap bitmap = cacheList.get(k);
            if (bitmap==null){
                try {
                    bitmap = BitmapFactory.decodeResource(mContext.getResources(),rid);
                    Ms.Holder.get().info(k+"获取bitmap:"+ bitmap.getWidth()+" * "+bitmap.getHeight());
                    cacheList.put(k,bitmap);
                } catch (Exception e) {
                    Ms.Holder.get().error(e);
                }
            }
        }
        return null;
    }

    public void onDestroy(){
        mContext=null;
        dm = null;
        if (cacheList.size()>0){
            cacheList.clearAll();
        }
    }




}
