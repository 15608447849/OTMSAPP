package ping.otmsapp.assemblys.fragments.attrs;

import android.app.FragmentManager;

import ping.otmsapp.assemblys.fragments.base.FragmentAttr;

/**
 * Created by user on 2018/2/27.
 * Index.activity
 */

public class IndexFragmentAttr extends FragmentAttr {
    public IndexFragmentAttr(FragmentManager fm, int containerId) {
        super(fm, containerId);
    }

    @Override
    protected void initMap() {
        //一级页面
        setMap(1,"firstPage.CurTask");
        setMap(2,"firstPage.HisTask");
        setMap(3,"firstPage.RealWarn");
        setMap(4,"firstPage.FreightBill");
        setMap(5,"firstPage.PathTrail");
        //二级页面
        setMap(6,"secondPage.HistoryDetail");
        setMap(7,"secondPage.ScanDetail");
        setMap(8,"secondPage.ScanRecycleBox");

    }


}
