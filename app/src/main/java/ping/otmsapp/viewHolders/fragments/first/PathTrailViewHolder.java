package ping.otmsapp.viewHolders.fragments.first;

import android.content.Context;
import android.widget.Switch;
import android.widget.TextView;

import ping.otmsapp.R;
import ping.otmsapp.entitys.annotations.Rid;
import ping.otmsapp.entitys.interfaces.ViewHolderAbs;

/**
 * Created by lzp on 2018/3/7.
 * 轨迹 R.layout.fragment_pathtrail
 */

public class PathTrailViewHolder extends ViewHolderAbs {

    @Rid(R.id.fragment_pathtrail_gdmapview)
    public com.amap.api.maps.MapView gdMapView;

    @Rid(R.id.fragment_pathtrail_sw_real)
    public Switch curTrace;

    @Rid(R.id.fragment_pathtrail_sw_correct)
    public Switch recTrace;

    @Rid(R.id.fragment_pathtrail_tv_speed)
    public TextView speedText;

    @Rid(R.id.fragment_pathtrail_tv_address)
    public TextView addressText;

    @Rid(R.id.fragment_pathtrail_tv_user)
    public TextView userName;

    @Rid(R.id.fragment_pathtrail_tv_plate)
    public TextView plateNo;


    public PathTrailViewHolder(Context context) {
        super(context, R.layout.fragment_pathtrail);
    }

    public void setUserText(String name,String plate){
        userName.setText(name);
        plateNo.setText(plate);
    }

    public void clearText() {
        speedText.setText("");
    }
}
