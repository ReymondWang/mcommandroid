package com.purplelight.mcommunity.provider;

import android.content.Context;

import com.purplelight.mcommunity.provider.dao.ILoginInfoDao;
import com.purplelight.mcommunity.provider.dao.impl.BaseDaoImpl;
import com.purplelight.mcommunity.provider.dao.impl.LoginInfoImpl;

/**
 * 生成底层数据库操作类的工厂
 * Created by wangyn on 16/4/26.
 */
public class DomainFactory {

    public static ILoginInfoDao createLoginInfo(Context context){
        return new LoginInfoImpl(context);
    }

}
