package ping.otmsapp.entitys.interfaces;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;

import ping.otmsapp.utils.Ms;

/**
 * Created by user on 2018/3/14.
 *
 */
public class RecycleViewAdapterWrap<H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter{
    public static final int SHOW_HEADER=1;
    public static final int SHOW_FOOTER=2;

    private RecyclerView.Adapter mAdapter;
    private AutoRecycleViewHolder mHeaderViewHolder;
    private AutoRecycleViewHolder mFooterViewHolder;

    public RecycleViewAdapterWrap(RecyclerView.Adapter mAdapter) {
        this.mAdapter=mAdapter;
    }


    public int getHeadersCount() {
        return mHeaderViewHolder==null?0:1;
    }

    public int getFootersCount() {
        return mFooterViewHolder==null?0:1;
    }


    @Override
    public int getItemViewType(int position) {
        //header
        int numHeaders = getHeadersCount();

        if (position < numHeaders) {
            return SHOW_HEADER;
        }
        // Adapter
        final int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemViewType(adjPosition);
            }
        }
        // Footer (off-limits positions will throw an IndexOutOfBoundsException)
        return SHOW_FOOTER;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == SHOW_HEADER){
            return mHeaderViewHolder;
        }
        if(viewType == SHOW_FOOTER){
            return mFooterViewHolder;
        }
        return mAdapter.onCreateViewHolder(parent,viewType);
    }

    @Nullable
    private H getViewHolder(Class<H> clazz,View viewItem) {
        //反射构建类类型
        try {
            Constructor cons = clazz.getConstructor(View.class);//获取有参构造
            return (H) cons.newInstance(viewItem);
        } catch (Exception e) {
            Ms.Holder.get().error(e);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //也要划分三个区域
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {//是头部
            return;
        }
        //adapter body
        final int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                mAdapter.onBindViewHolder(holder, adjPosition);
                return;
            }
        }
        //footer

    }
    @Override
    public int getItemCount() {
        if (mAdapter != null) {
            return getFootersCount() + getHeadersCount() + mAdapter.getItemCount();
        } else {
            return getFootersCount() + getHeadersCount();
        }
    }


    public void setHead(AutoRecycleViewHolder head) {
        this.mHeaderViewHolder = head;
    }

    public void setFooter(AutoRecycleViewHolder footer) {
        this.mFooterViewHolder = footer;
    }
}
