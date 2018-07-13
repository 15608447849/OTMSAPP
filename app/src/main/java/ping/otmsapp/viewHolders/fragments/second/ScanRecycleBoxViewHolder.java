package ping.otmsapp.viewHolders.fragments.second;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import ping.otmsapp.R;
import ping.otmsapp.entitys.annotations.Rid;
import ping.otmsapp.entitys.interfaces.ViewHolderAbs;
import ping.otmsapp.viewHolders.includeLayouts.ViewHolderRecycle;

/**
 * Created by Leeping on 2018/4/10.
 * email: 793065165@qq.com
 * 回收箱扫码页面
 */

public class ScanRecycleBoxViewHolder extends ViewHolderAbs {
    public ViewHolderRecycle list;

    @Rid(R.id.frg_recyclebox_title)
    public TextView title;

    @Rid(R.id.curtask_input_et)
    public EditText input;

    @Rid(R.id.curtask_input_btn)
    public Button inputBtn;

    @Rid(R.id.frg_recyclebox_radiogroup)
    public RadioGroup radioGroup;

    @Rid(R.id.frg_recyclebox_type_recycle)
    public RadioButton recyclToBtn;
    @Rid(R.id.frg_recyclebox_type_back)
    public RadioButton backToBtn;
    @Rid(R.id.frg_recyclebox_type_adjust)
    public RadioButton tanslateToBtn;

    public ScanRecycleBoxViewHolder(Context context) {
        super(context, R.layout.fragment_scanrecyclebox);
    }

    @Override
    public void setOnClickListener(View.OnClickListener listener) {
        inputBtn.setOnClickListener(listener);
    }
}
