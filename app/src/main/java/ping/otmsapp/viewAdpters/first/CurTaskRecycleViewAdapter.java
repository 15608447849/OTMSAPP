package ping.otmsapp.viewAdpters.first;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import ping.otmsapp.R;
import ping.otmsapp.entitys.dataBeans.dispatch.DistributionPathBean;
import ping.otmsapp.entitys.interfaces.RecycleAdapterAbs;
import ping.otmsapp.utils.AppUtil;
import ping.otmsapp.viewHolders.recycycleItems.first.CurTaskRecycleItem;

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
            holder.hindCheck(); //隐藏勾选栏
            holder.scanNumberTv.setText("暂无任务");

            if (showType==1){
                if (distributionPathBean.getState() == STORE_DEAL_LOAD){
//                    holder.showCheck();
//                    holder.setUncheck();//未勾选
                    if (distributionPathBean.getLoadScanIndex()<distributionPathBean.getBoxSum()){
                        holder.scanNumberTv.setText(AppUtil.stringForart("待装载扫码数: [%d/%d]",distributionPathBean.getLoadScanIndex(),distributionPathBean.getBoxSum()));
                    }else{

                        holder.scanNumberTv.setText(AppUtil.stringForart("已完成: [%d/%d]",distributionPathBean.getLoadScanIndex(),distributionPathBean.getBoxSum()));
                    }
                }
            }else if (showType ==2){
                if (distributionPathBean.getState() == STORE_DEAL_UNLOAD){
                    holder.showCheck();
                    holder.setUncheck();//未勾选
                    if (distributionPathBean.getUnloadScanIndex()<distributionPathBean.getBoxSum()){

                        holder.scanNumberTv.setText(AppUtil.stringForart("可卸载扫码数: [%d/%d]",distributionPathBean.getUnloadScanIndex(),distributionPathBean.getLoadScanIndex()));
                    }else{
                        holder.scanNumberTv.setText(AppUtil.stringForart("已完成: [%d/%d]",distributionPathBean.getUnloadScanIndex(),distributionPathBean.getBoxSum()));

                    }
                }
            }


        if (selectPosition == position){
                if (holder.isCheckVisible()){
                    holder.setCheck();
                }else{
                    selectPosition = -1;
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
    private int selectPosition = -1;
    public void setSelectPosition(int selectPosition) {
        if (this.selectPosition == selectPosition) this.selectPosition =-1;
        else this.selectPosition = selectPosition;

    }

    public int getSelectPosition() {
        return selectPosition;
    }

    private int showType;
    public void setShowType(int showType) {
        this.showType = showType;
    }

    public interface ButtonCheck{
        void onClicked(View view,int postion);
    }
    private CurTaskRecycleViewAdapter.ButtonCheck clickCallback;

    public void setButtonClicked(CurTaskRecycleViewAdapter.ButtonCheck clickCallback) {
        this.clickCallback = clickCallback;
    }
}
