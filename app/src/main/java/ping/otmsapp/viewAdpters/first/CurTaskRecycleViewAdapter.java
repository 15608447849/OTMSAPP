package ping.otmsapp.viewAdpters.first;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import ping.otmsapp.R;
import ping.otmsapp.entitys.dataBeans.dispatch.DistributionPathBean;
import ping.otmsapp.entitys.interfaces.RecycleAdapterAbs;
import ping.otmsapp.utils.AppUtil;
import ping.otmsapp.viewHolders.recycycleItems.first.CurTaskRecycleItem;

import static ping.otmsapp.utils.STATE.DISPATCH_DEAL_LOAD;
import static ping.otmsapp.utils.STATE.DISPATCH_DEAL_UNLOAD;
import static ping.otmsapp.utils.STATE.STORE_DEAL_COMPLETE;
import static ping.otmsapp.utils.STATE.STORE_DEAL_LOAD;
import static ping.otmsapp.utils.STATE.STORE_DEAL_UNLOAD;

/**
 * Created by lzp on 2018/2/28.
 * 关联视图 - CurTaskRecycleItem
 *
 */

public class CurTaskRecycleViewAdapter extends RecycleAdapterAbs<DistributionPathBean,CurTaskRecycleItem> {

    public CurTaskRecycleViewAdapter(Context context) {
        super(context);
    }

    //创建视图持有者
    @Override
    public CurTaskRecycleItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CurTaskRecycleItem(getLayoutIdToView(R.layout.recycle_item_curtask, parent));
    }

    @Override
    public void onBindViewHolder(CurTaskRecycleItem holder, int position) {
        final int index = position;

        DistributionPathBean distributionPathBean = mDatas.get(position);
            holder.storeTv.setText(distributionPathBean.getStoreName());
            holder.orderTv.setText(AppUtil.stringForart("配送顺序: %d",distributionPathBean.getSpecifiedOrder()));
            holder.hindCheck(); //隐藏勾选框
            holder.setUnCheckImg();//设置不勾选
            holder.scanNumberTv.setText("无");

            if (showType == DISPATCH_DEAL_LOAD){ //装货列表 item

                if (distributionPathBean.getState() == STORE_DEAL_LOAD){
                    holder.showCheck();
                    if (distributionPathBean.getLoadScanIndex()<distributionPathBean.getBoxSum()){
                        holder.scanNumberTv.setText(AppUtil.stringForart("待装载扫码数: [%d/%d]",distributionPathBean.getLoadScanIndex(),distributionPathBean.getBoxSum()));
                    }
                }
                if (distributionPathBean.getState() == STORE_DEAL_UNLOAD){
                    holder.scanNumberTv.setText(AppUtil.stringForart("已全部装载: [%d/%d]",distributionPathBean.getLoadScanIndex(),distributionPathBean.getBoxSum()));
                }
            }
            else
            if (showType == DISPATCH_DEAL_UNLOAD){ //卸货列表

                if (distributionPathBean.getState() == STORE_DEAL_UNLOAD){

                    if (distributionPathBean.getUnloadScanIndex()<distributionPathBean.getBoxSum()){

                        holder.scanNumberTv.setText(AppUtil.stringForart("可卸载扫码数: [%d/%d]",distributionPathBean.getUnloadScanIndex(),distributionPathBean.getLoadScanIndex()));
                    }
                }
                if (distributionPathBean.getState() == STORE_DEAL_COMPLETE){
                    holder.scanNumberTv.setText(AppUtil.stringForart("已全部卸货: [%d/%d]",distributionPathBean.getUnloadScanIndex(),distributionPathBean.getBoxSum()));
                }

            }

        if (showType == currentType) holder.showCheck();

        //如果当前 checkbox 选中的下标 与 当前下标相同
        if (checkPos == position){
               if (showType == currentType){
                   holder.setCheckImg();//设置选中
               }
        }

        if (clickCallback!=null){
            holder.detailBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickCallback.onClicked(v,index);
                }
            });
        }

        // 如果设置了回调，则设置点击事件
        if (listener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v,index);
                }
            });
        }
    }

    /**
     * 选中下标
     */
    private int checkPos = -1;
    /**
     * 当前调度状态
     */
    private int currentType = 0;


    public void setCurrentType(int currentType){
        this.currentType = currentType;
    }

    public void setSelectPosition(int selectPosition) {
        if (showType!=currentType) return;
        if (this.checkPos == selectPosition) this.checkPos =-1; //已勾选状态选中-取消勾选
        else this.checkPos = selectPosition;
    }

    public void reset(){
        checkPos = -1;
    }

    public int getCheckPos() {
        return checkPos;
    }

    private int showType = -1;

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public interface ButtonCheck{
        void onClicked(View view,int pos);
    }

    private CurTaskRecycleViewAdapter.ButtonCheck clickCallback;

    public void setButtonClicked(CurTaskRecycleViewAdapter.ButtonCheck clickCallback) {
        this.clickCallback = clickCallback;
    }
}
