package com.purplelight.mcommunity.web.result;

import com.purplelight.mcommunity.provider.entity.LoginInfo;

/**
 * 登录返回的结果
 * Created by wangyn on 16/4/26.
 */
public class LoginResult extends Result {
    private LoginInfo user;

    public LoginInfo getUser() {
        return user;
    }

    public void setUser(LoginInfo user) {
        this.user = user;
    }
}
