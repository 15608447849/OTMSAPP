package ping.otmsapp.entitys.interfaces;

import android.view.View;

/**
 * Created by lzp on 2018/3/1.
 * recycleView 自动赋值
 */

public abstract class AutoRecycleViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder{
    public AutoRecycleViewHolder(View itemView) {
        super(itemView);
        autoInitView();
    }
    private void autoInitView() {
        try {
            ReflectionTool.autoViewValue(this,itemView,null);
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }
}
