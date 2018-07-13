package ping.otmsapp.viewHolders.recycycleItems.first;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ping.otmsapp.R;
import ping.otmsapp.entitys.annotations.Rid;
import ping.otmsapp.entitys.interfaces.AutoRecycleViewHolder;

/**
 * Created by user on 2018/2/28.
 * R.id.layout_sub_curtasklist
 */

public class CurTaskRecycleItem extends AutoRecycleViewHolder {

    @Rid(R.id.recycle_item_tv_order)
    public TextView orderTv;

    @Rid(R.id.recycle_item_tv_storeName)
    public TextView storeTv;

    @Rid(R.id.recycle_item_tv_scanState)
    public TextView scanNumberTv;

    @Rid(R.id.recycle_item_tv_detail_btn)
    public Button detailBtn;

    @Rid(R.id.recycle_item_image_checkbox)
    public ImageView checkBoxImg;

    public CurTaskRecycleItem(View itemView) {
        super(itemView);
    }

    public void setCheck(){
        checkBoxImg.setImageResource(android.R.drawable.checkbox_on_background);
    }
    public void setUncheck(){
        checkBoxImg.setImageResource(android.R.drawable.checkbox_off_background);
    }

    public void hindCheck() {
        checkBoxImg.setVisibility(View.GONE);
    }
    public void showCheck() {checkBoxImg.setVisibility(View.VISIBLE);}
    public boolean isCheckVisible(){ return checkBoxImg.getVisibility()==View.VISIBLE;}
}
