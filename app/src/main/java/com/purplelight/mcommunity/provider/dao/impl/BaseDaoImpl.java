package com.purplelight.mcommunity.provider.dao.impl;

import android.content.Context;

/**
 * 数据库操作类的基类实现
 * Created by wangyn on 16/4/26.
 */
public class BaseDaoImpl {

    private Context mContext;

    public BaseDaoImpl(Context context){
        mContext = context;
    }

    public Context getContext(){
        return mContext;
    }

}
