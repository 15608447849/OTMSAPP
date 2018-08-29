package ping.otmsapp.entitys.dataBeans.login;

import org.jetbrains.annotations.NotNull;

import ping.otmsapp.entitys.dataBeans.localStore.DbJsonStoreAbs;

/**
 * Created by lzp on 2018/3/13.
 * 登录用户信息
 */

public class LoginUserBean extends DbJsonStoreAbs {
    public static final int driverCode = 2;
    /**
     * 用户码
     * */
    private String userCode;
    /**
     * 是否允许登陆,默认不允许
     */
    private boolean isAccess;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public boolean isAccess() {
        return isAccess;
    }

    public void setAccess(boolean access) {
        isAccess = access;
    }

    @NotNull
    @Override
    public String selfKey() {
        return "登陆信息";
    }



}
