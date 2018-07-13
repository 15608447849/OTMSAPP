package ping.otmsapp.entitys.maps.location;

import android.content.Context;

import com.amap.api.maps.model.LatLng;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceListener;
import com.amap.api.trace.TraceLocation;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * Created by Leeping on 2018/4/20.
 * email: 793065165@qq.com
 *
 */

public class GdLocationCorrect implements Closeable, TraceListener {



    public interface CorrectResult {
        void onCorrect(List<LatLng> list, int distance);
    }

    private final int id  ;
    private final CorrectResult result;
    //轨迹纠偏使用
    private LBSTraceClient lbsTraceClient;

    public GdLocationCorrect(Context context,int i,CorrectResult correctResult) {
        lbsTraceClient = LBSTraceClient.getInstance(context);
        id = i;
        result = correctResult;
    }

    public void convert(List<TraceLocation> list){
        lbsTraceClient.queryProcessedTrace(id,
                list,
                LBSTraceClient.TYPE_AMAP,
                this);
    }

    @Override
    public void onRequestFailed(int i, String s) {

    }

    @Override
    public void onTraceProcessing(int i, int i1, List<LatLng> list) {

    }

    @Override
    public void onFinished(int i, List<LatLng> list, int i1, int i2) {
            result.onCorrect(list,i1);
    }

    @Override
    public void close() throws IOException {
        lbsTraceClient.stopTrace();//停止纠偏
        lbsTraceClient.destroy(); //轨迹纠偏销毁
    }
}
