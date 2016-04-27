package com.purplelight.mcommunity.provider.dao.impl;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.purplelight.mcommunity.provider.MCommProviderMeta.LoginInfoMetaData;
import com.purplelight.mcommunity.provider.dao.ILoginInfoDao;
import com.purplelight.mcommunity.provider.entity.LoginInfo;

/**
 * 数据库信息的登录实现类
 * Created by wangyn on 16/4/26.
 */
public class LoginInfoImpl extends BaseDaoImpl implements ILoginInfoDao {

    private final static String TAG = "LoginInfoImpl";

    public LoginInfoImpl(Context context) {
        super(context);
    }

    /**
     * 一个手机上记录的登录帐号只能有一个，因此在保存数据的时候
     * 采用的方式为先清除数据库的信息，然后保存新的数据进去。
     * @param loginInfo      要保存的登录信息
     */
    @Override
    public void save(LoginInfo loginInfo) {
        Log.d(TAG, "Save into LoginInfo");

        ContentResolver contentResolver = getContext().getContentResolver();
        Uri uri = LoginInfoMetaData.CONTENT_URI;

        int cnt = contentResolver.delete(uri, null, new String[0]);
        Log.d(TAG, "delete count:" + cnt);

        ContentValues cv = new ContentValues();
        cv.put(LoginInfoMetaData.USERID, loginInfo.getId());
        cv.put(LoginInfoMetaData.USERCODE, loginInfo.getUserCode());
        cv.put(LoginInfoMetaData.USERNAME, loginInfo.getUserName());
        cv.put(LoginInfoMetaData.PASSWORD, loginInfo.getPassword());
        cv.put(LoginInfoMetaData.SEX, loginInfo.getSex());
        cv.put(LoginInfoMetaData.EMAIL, loginInfo.getEmail());
        cv.put(LoginInfoMetaData.PHONE, loginInfo.getPhone());
        cv.put(LoginInfoMetaData.ADDRESS, loginInfo.getAddress());
        cv.put(LoginInfoMetaData.HEADIMGPATH, loginInfo.getHeadImgPath());

        Uri insertedUri = contentResolver.insert(uri, cv);
        Log.d(TAG, "inserted uri:" + insertedUri);
    }


    /**
     * 从本地数据库中将保存的登录信息加载出来，如果没有则返回NULL
     * @return  LoginInfo实体
     */
    @Override
    public LoginInfo load() {
        Uri uri = LoginInfoMetaData.CONTENT_URI;
        Cursor c = getContext().getContentResolver().query(uri, null, null, null, null);
        if (c != null){
            int iUserId = c.getColumnIndex(LoginInfoMetaData.USERID);
            int iUserCode = c.getColumnIndex(LoginInfoMetaData.USERCODE);
            int iUserName = c.getColumnIndex(LoginInfoMetaData.USERNAME);
            int iSex = c.getColumnIndex(LoginInfoMetaData.SEX);
            int iPassword = c.getColumnIndex(LoginInfoMetaData.PASSWORD);
            int iEmail = c.getColumnIndex(LoginInfoMetaData.EMAIL);
            int iPhone = c.getColumnIndex(LoginInfoMetaData.PHONE);
            int iAddress = c.getColumnIndex(LoginInfoMetaData.ADDRESS);
            int iHeadImgPath = c.getColumnIndex(LoginInfoMetaData.HEADIMGPATH);

            c.moveToFirst();
            if (!c.isAfterLast()){
                LoginInfo loginInfo = new LoginInfo();
                loginInfo.setId(c.getString(iUserId));
                loginInfo.setUserCode(c.getString(iUserCode));
                loginInfo.setUserName(c.getString(iUserName));
                loginInfo.setSex(c.getString(iSex));
                loginInfo.setPassword(c.getString(iPassword));
                loginInfo.setEmail(c.getString(iEmail));
                loginInfo.setPhone(c.getString(iPhone));
                loginInfo.setAddress(c.getString(iAddress));
                loginInfo.setHeadImgPath(c.getString(iHeadImgPath));

                Log.d(TAG, "get logininfo:" + loginInfo.getUserName());
                return loginInfo;
            }
        }

        return null;
    }

    /**
     * 清空本地数据库中的保存的登录数据
     */
    @Override
    public void clear() {
        ContentResolver contentResolver = getContext().getContentResolver();
        Uri uri = LoginInfoMetaData.CONTENT_URI;

        int cnt = contentResolver.delete(uri, null, new String[0]);
        Log.d(TAG, "delete count:" + cnt);
    }
}
