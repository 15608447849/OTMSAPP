package ping.otmsapp.viewHolders.activity;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import ping.otmsapp.R;
import ping.otmsapp.entitys.annotations.Rid;
import ping.otmsapp.entitys.interfaces.ViewHolderAbs;
import ping.otmsapp.utils.AppUtil;

/**
 * Created by user on 2018/3/2.
 * R.layout.dialog_login
 */

public class LoginViewHolder extends ViewHolderAbs{

    @Rid(R.id.login_progressBar)
    public ProgressBar progressBar;
    @Rid(R.id.login_tv_title)
    public TextView title;
    @Rid(R.id.login_text_phone)
    public TextInputLayout phoneTextView;
    @Rid(R.id.login_text_password)
    public TextInputLayout passTextView;
    @Rid(R.id.login_btn)
    public Button loginBtn;

    public LoginViewHolder(Context context) {
       super(context, R.layout.activity_login);
       title.setText(AppUtil.stringForart("%s v%d.0",context.getString(R.string.app_name),AppUtil.getVersionCode(context)));
    }

    @Override
    public void setOnClickListener(View.OnClickListener listener) {
        loginBtn.setOnClickListener(listener);
    }
    public void setProgressIndeterminate(boolean f){
        progressBar.setIndeterminate(f);
    }
}
