package com.purplelight.mcommunity.application;

import android.app.Application;

import com.purplelight.mcommunity.provider.entity.LoginInfo;

/**
 * 全局变量
 * Created by wangyn on 16/4/26.
 */
public class MCommApplication extends Application {

    private static LoginInfo mLoginInfo;

    public static void setLoginInfo(LoginInfo loginInfo){
        mLoginInfo = loginInfo;
    }

    public static LoginInfo getLoginInfo(){
        return mLoginInfo;
    }
}
