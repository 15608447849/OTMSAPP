package ping.otmsapp.viewHolders.includeLayouts;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import ping.otmsapp.R;
import ping.otmsapp.entitys.annotations.Rid;
import ping.otmsapp.entitys.interfaces.ViewHolderAbs;

/**
 * Created by user on 2018/2/27.
 * R.layout.layout_bottom_tab
 */

public class ViewHolderBottomTab extends ViewHolderAbs{

    @Rid(R.id.index_tab_curtask)
    public View curtask;
    @Rid(R.id.index_tab_curtask_iv_normal)
    public ImageView curtaskIvNor;
    @Rid(R.id.index_tab_curtask_iv_select)
    public ImageView curtaskIvSct;
    @Rid(R.id.index_tab_curtask_tv_number)
    public TextView curtaskNum;
    @Rid(R.id.index_tab_curtask_tv_tag)
    public TextView curtaskTag;

    @Rid(R.id.index_tab_histask)
    public View histask;
    @Rid(R.id.index_tab_histask_iv_normal)
    public ImageView histaskIvNor;
    @Rid(R.id.index_tab_histask_iv_select)
    public ImageView histaskIvSct;
    @Rid(R.id.index_tab_histask_tv_number)
    public TextView histaskNum;
    @Rid(R.id.index_tab_histask_tv_tag)
    public TextView histaskTag;

    @Rid(R.id.index_tab_realwarn)
    public View realwarn;
    @Rid(R.id.index_tab_realwarn_iv_normal)
    public ImageView realwarnIvNor;
    @Rid(R.id.index_tab_realwarn_iv_select)
    public ImageView realwarnIvSct;
    @Rid(R.id.index_tab_realwarn_tv_number)
    public TextView realwarnNum;
    @Rid(R.id.index_tab_realwarn_tv_tag)
    public TextView realwarnTag;

    @Rid(R.id.index_tab_freightbill)
    public View freightbill;
    @Rid(R.id.index_tab_freightbill_iv_normal)
    public ImageView freightbillIvNor;
    @Rid(R.id.index_tab_freightbill_iv_select)
    public ImageView freightbillIvSct;
    @Rid(R.id.index_tab_freightbill_tv_number)
    public TextView freightbillNum;
    @Rid(R.id.index_tab_freightbill_tv_tag)
    public TextView freightbillTag;

    @Rid(R.id.index_tab_pathtrail)
    public View pathtrail;
    @Rid(R.id.index_tab_pathtrail_iv_normal)
    public ImageView pathtrailIvNor;
    @Rid(R.id.index_tab_pathtrail_iv_select)
    public ImageView pathtrailIvSct;
    @Rid(R.id.index_tab_pathtrail_tv_number)
    public TextView pathtrailNum;
    @Rid(R.id.index_tab_pathtrail_tv_tag)
    public TextView pathtrailTag;

    public ViewHolderBottomTab(Context context, View viewRoot) {
        super(context,viewRoot);
        relevance();
    }

    private void relevance() {
        curtaskIvSct.setTag(curtaskIvNor);
        histaskIvSct.setTag(histaskIvNor);
        realwarnIvSct.setTag(realwarnIvNor);
        freightbillIvSct.setTag(freightbillIvNor);
        pathtrailIvSct.setTag(pathtrailIvNor);
    }

    @Override
    public void setOnClickListener(OnClickListener listener) {
        curtask.setOnClickListener(listener);
        histask.setOnClickListener(listener);
        realwarn.setOnClickListener(listener);
        freightbill.setOnClickListener(listener);
        pathtrail.setOnClickListener(listener);
    }
    private ImageView cShowImage;
    private TextView cShowText;
    public void showView(ImageView imageView,TextView textView){
        if (cShowText!=null && cShowImage!=null) {
            if (cShowImage.getId() == imageView.getId() && cShowText.getId() == textView.getId()){
                return;
            }
            cShowText.setTextColor(context.getResources().getColor(R.color.text_main_clolor));
            cShowImage.setVisibility(View.GONE);
            ((ImageView)cShowImage.getTag()).setVisibility(View.VISIBLE);
        }
        imageView.setVisibility(View.VISIBLE);
        ((ImageView)imageView.getTag()).setVisibility(View.GONE);
        textView.setTextColor(context.getResources().getColor(R.color.recycle_item_state_text_color));
        cShowImage = imageView;
        cShowText = textView;
    }





}
