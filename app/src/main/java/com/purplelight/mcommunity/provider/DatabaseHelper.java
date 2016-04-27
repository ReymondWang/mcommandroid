package com.purplelight.mcommunity.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.purplelight.mcommunity.provider.MCommProviderMeta.LoginInfoMetaData;

/**
 * 掌上园区的移动端数据库的创建及升级帮助类
 * Created by wangyn on 16/4/26.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, MCommProviderMeta.DATABASE_NAME, null, MCommProviderMeta.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "database created");
        db.execSQL("CREATE TABLE " + LoginInfoMetaData.TABLE_NAME + " ("
                + LoginInfoMetaData._ID + " INTEGER PRIMARY KEY,"
                + LoginInfoMetaData.USERID + " TEXT,"
                + LoginInfoMetaData.USERCODE + " TEXT,"
                + LoginInfoMetaData.USERNAME + " TEXT,"
                + LoginInfoMetaData.PASSWORD + " TEXT,"
                + LoginInfoMetaData.SEX + " TEXT,"
                + LoginInfoMetaData.EMAIL + " TEXT,"
                + LoginInfoMetaData.PHONE + " TEXT,"
                + LoginInfoMetaData.ADDRESS + " TEXT,"
                + LoginInfoMetaData.HEADIMGPATH + " TEXT,"
                + LoginInfoMetaData.CREATED_DATE + " INTEGER,"
                + LoginInfoMetaData.MODIFIED_DATE + " INTEGER"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version "
                + oldVersion + " to "
                + newVersion + ", which will destory all old data.");
        db.execSQL("DROP TABLE IF EXISTS " + LoginInfoMetaData.TABLE_NAME);
        onCreate(db);
    }
}
