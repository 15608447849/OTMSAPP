package ping.otmsapp.entitys.interfaces;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2018/3/1.
 *
 */
public abstract class RecycleAdapterAbs<D,C extends AutoRecycleViewHolder> extends android.support.v7.widget.RecyclerView.Adapter<C> {



    // 子项点击事件接口
    protected OnRecyclerViewAdapterItemClickListener listener;

    /**
     *
     * @param listener 设置子项点击事件
     */
    public void setOnItemClickListener(OnRecyclerViewAdapterItemClickListener listener)
    {
        this.listener = listener;
    }

    protected View  getLayoutIdToView(int layoutId,ViewGroup parent){
        return LayoutInflater.from(context).inflate(layoutId, parent, false);
    }



    protected List<D> mDatas;

    protected Context context;

    public RecycleAdapterAbs(Context context) {
        this(context,new ArrayList<D>());
    }

    public RecycleAdapterAbs(Context context,List<D> datas) {
        this.context = context;
        mDatas = datas;
    }

    @Override
    public abstract C onCreateViewHolder(ViewGroup parent, int viewType);



    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 获取数据列表
     */
    public List<D> getDataList(){
        return mDatas;
    }
    /**
     * 获取指定下标的数据
     */
    public D getData(int pos){
        if (mDatas.size()>pos) return mDatas.get(pos);
        else return null;
    }
    /**
     * 添加数据
     */
    public void addData(D data){
        mDatas.add(data);
    }
    /**
     * 添加数据
     */
    public void addData(int pos,D data){
        mDatas.add(pos,data);
    }
    /**
     * 清理所有数据
     */
    public void clearAll(){
        mDatas.clear();
    }

    /**
     * 添加列表
     */
    public void addDataList(List<D> list){
        addDataList(list,false);
    }
    /**
     * 添加列表
     */
    public void addDataList(List<D> list,boolean isClear){
        if (isClear) clearAll();
        mDatas.addAll(list);
    }


}
