package ping.otmsapp.viewHolders.includeLayouts;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ping.otmsapp.R;
import ping.otmsapp.entitys.annotations.Rid;
import ping.otmsapp.entitys.interfaces.ViewHolderAbs;

/**
 * Created by lzp on 2018/2/28.
 * recycle view
 */

public class ViewHolderRecycle extends ViewHolderAbs {

    @Rid(R.id.recycler)
    public RecyclerView recycler;
    public ViewHolderRecycle(Context context, View viewRoot) {
        super(context,viewRoot);
    }

    public void setRecyclerDefaultSetting(RecyclerView.Adapter adapter,int tag){
        if (tag == 1){
            //垂直 列表展示
            recycler.setLayoutManager(new LinearLayoutManager(context));
            recycler.setItemAnimator(new DefaultItemAnimator());

        }
        if (tag==2){


            recycler.setLayoutManager(new GridLayoutManager(context,1,LinearLayoutManager.HORIZONTAL,false));
            recycler.setItemAnimator(new DefaultItemAnimator());
        }
        if (tag==101){
            //垂直列表加分割线
            recycler.setLayoutManager(new LinearLayoutManager(context));
            recycler.setItemAnimator(new DefaultItemAnimator());
            recycler.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        }
        if (adapter!=null){
            recycler.setAdapter(adapter);
        }
    }
}
