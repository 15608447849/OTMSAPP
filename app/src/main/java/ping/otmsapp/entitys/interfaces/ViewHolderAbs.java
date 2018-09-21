package ping.otmsapp.entitys.interfaces;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import ping.otmsapp.utils.Ms;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by user on 2018/2/24.
 *
 */

public abstract class ViewHolderAbs {

    protected Context context;
    protected LayoutInflater layoutInflater;
    protected View viewRoot;

    public ViewHolderAbs(Context context,int layoutId) {
        this(context);
        this.layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        try {
            this.viewRoot = layoutInflater.inflate(layoutId,null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.toString());
        }
        autoInitView();
    }
    public void setProgressMax(int v){

    }
    public void setProgressIndeterminate(boolean f){

    }


    public ViewHolderAbs(Context context) {
        this.context = context;
    }
    public ViewHolderAbs(Context context,View viewRoot) {
        this(context);
        this.viewRoot = viewRoot;
        autoInitView();
    }

    private void autoInitView() {
        try {
            ReflectionTool.autoViewValue(this,viewRoot,context);
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }
    public View getLayoutFileRid(){

        return viewRoot;
    }

    public void setOnClickListener(View.OnClickListener listener){
        //node
    }

    public void destroy(){
//        Log.d("viewHolder",this+" 销毁");
         context = null;
         layoutInflater = null;
         viewRoot = null;
         //检测是否存在viewhodle对象,调用 destroy().
        autoDestroy();
    }

    private void autoDestroy() {
        try {
            ReflectionTool.autoDestroyViewHolder(this,"destroy");
        } catch (Exception e) {
            Ms.Holder.get().error(e);
        }
    }



}
