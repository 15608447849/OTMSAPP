package ping.otmsapp.viewHolders.fragments.first;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import ping.otmsapp.R;
import ping.otmsapp.entitys.annotations.Rid;
import ping.otmsapp.entitys.interfaces.ViewHolderAbs;
import ping.otmsapp.viewHolders.includeLayouts.ViewHolderRecycle;

/**
 * Created by user on 2018/2/28.
 * fragment - viewholder
 * 当前任务 - 布局
 */

public class CurTaskViewHolder extends ViewHolderAbs {

    public ViewHolderRecycle list;

    @Rid(R.id.curtask_input_et)
    public EditText inputEt;

    @Rid(R.id.curtask_input_btn)
    public Button inputBtn;

    @Rid(R.id.curtask_rg_group)
    public RadioGroup radioGroup;

    //装载列表
    @Rid(R.id.curtask_rg_rbutton_load)
    public RadioButton load;
    //卸载列表
    @Rid(R.id.curtask_rg_rbutton_unload)
    public RadioButton unload;

    //启程按钮
    @Rid(R.id.curtask_button_takeOut)
    public Button takeOut;
    //回程按钮
    @Rid(R.id.curtask_button_takeBack)
    public Button back;
    //提交货差
    @Rid(R.id.curtask_button_abnormal)
    public Button abnormal;
    //回收箱列表
    @Rid(R.id.curtask_button_addRecycle)
    public Button addRecycle;

    public CurTaskViewHolder(Context context) {
        super(context, R.layout.fragment_curtask);
    }

    @Override
    public void setOnClickListener(View.OnClickListener listener) {
        inputBtn.setOnClickListener(listener);
        takeOut.setOnClickListener(listener);
        back.setOnClickListener(listener);
        abnormal.setOnClickListener(listener);
        addRecycle.setOnClickListener(listener);
    }






}
