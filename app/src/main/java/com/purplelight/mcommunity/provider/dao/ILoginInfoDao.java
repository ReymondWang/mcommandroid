package com.purplelight.mcommunity.provider.dao;

import com.purplelight.mcommunity.provider.entity.LoginInfo;

/**
 * 登录信息的操作类
 * Created by wangyn on 16/4/26.
 */
public interface ILoginInfoDao {

    void save(LoginInfo loginInfo);

    LoginInfo load();

    void clear();

}
