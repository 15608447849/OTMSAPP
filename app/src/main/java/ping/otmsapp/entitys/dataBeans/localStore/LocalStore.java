package ping.otmsapp.entitys.dataBeans.localStore;

import org.jetbrains.annotations.NotNull;

import ping.otmsapp.entitys.dataBeans.login.LoginUserBean;
import ping.otmsapp.utils.AppUtil;

/**
 * Created by Leeping on 2018/5/15.
 * email: 793065165@qq.com
 */

public abstract class LocalStore extends DbJsonStoreAbs {

    //本地用户对象
    private final LoginUserBean loginUserBean;

    public LocalStore() {
        loginUserBean = new LoginUserBean().fetch();
        if (loginUserBean == null) throw new IllegalArgumentException("本地存储无法实例化,没有登陆用户信息.");
    }

    @NotNull
    @Override
    public String selfKey() {
        return AppUtil.stringForart("@用户码(%s)",loginUserBean.getUserCode());
    }


}
