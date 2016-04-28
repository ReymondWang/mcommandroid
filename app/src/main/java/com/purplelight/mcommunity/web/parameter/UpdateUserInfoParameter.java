package com.purplelight.mcommunity.web.parameter;

import com.purplelight.mcommunity.provider.entity.LoginInfo;

/**
 * 更新用户信息的参数
 * Created by wangyn on 16/4/28.
 */
public class UpdateUserInfoParameter extends Parameter {
    private LoginInfo user = new LoginInfo();

    public LoginInfo getUser() {
        return user;
    }

    public void setUser(LoginInfo user) {
        this.user = user;
    }
}
