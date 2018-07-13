package ping.otmsapp.entitys.dataBeans.localStore;

import org.jetbrains.annotations.NotNull;

import ping.otmsapp.entitys.dataBeans.login.LoginUserBean;
import ping.otmsapp.utils.AppUtil;
import ping.otmsapp.utils.LDBStoreByJson;

/**
 * Created by Leeping on 2018/5/15.
 * email: 793065165@qq.com
 */

public abstract class LocalStore<T> extends LDBStoreByJson<T> {
    private LoginUserBean loginUserBean;

    public LocalStore() {
        this.loginUserBean = new LoginUserBean().fetch();
        if (loginUserBean == null) throw new IllegalArgumentException("本地存储无法实例化,没有登陆用户信息.");
    }

    @NotNull
    @Override
    public String getKey() {
        return AppUtil.stringForart("@用户码(%s)",loginUserBean.getUserCode());
    }


}
