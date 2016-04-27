package com.purplelight.mcommunity.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

import com.purplelight.mcommunity.provider.MCommProviderMeta.LoginInfoMetaData;

/**
 * 用户在移动端登录信息的数据库查询类
 * Created by wangyn on 16/4/26.
 */
public class LoginInfoProvider extends ContentProvider {
    private static final String TAG = "LoginInfo";

    private static HashMap<String, String> sLoginInfoMap;
    static {
        sLoginInfoMap = new HashMap<>();
        sLoginInfoMap.put(LoginInfoMetaData._ID, LoginInfoMetaData._ID);
        sLoginInfoMap.put(LoginInfoMetaData.USERID, LoginInfoMetaData.USERID);
        sLoginInfoMap.put(LoginInfoMetaData.USERCODE, LoginInfoMetaData.USERCODE);
        sLoginInfoMap.put(LoginInfoMetaData.USERNAME, LoginInfoMetaData.USERNAME);
        sLoginInfoMap.put(LoginInfoMetaData.PASSWORD, LoginInfoMetaData.PASSWORD);
        sLoginInfoMap.put(LoginInfoMetaData.SEX, LoginInfoMetaData.SEX);
        sLoginInfoMap.put(LoginInfoMetaData.EMAIL, LoginInfoMetaData.EMAIL);
        sLoginInfoMap.put(LoginInfoMetaData.PHONE, LoginInfoMetaData.PHONE);
        sLoginInfoMap.put(LoginInfoMetaData.ADDRESS, LoginInfoMetaData.ADDRESS);
        sLoginInfoMap.put(LoginInfoMetaData.HEADIMGPATH, LoginInfoMetaData.HEADIMGPATH);
        sLoginInfoMap.put(LoginInfoMetaData.CREATED_DATE, LoginInfoMetaData.CREATED_DATE);
        sLoginInfoMap.put(LoginInfoMetaData.MODIFIED_DATE, LoginInfoMetaData.MODIFIED_DATE);
    }

    private static final UriMatcher sUriMatcher;
    private static final int INCOMING_LOGININFO_COLLECTION_URI_INDICATOR = 1;
    private static final int INCOMING_SINGLE_LOGININFO_URI_INDICATOR = 2;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(LoginInfoMetaData.AUTHORITY
                , "logininfo"
                , INCOMING_LOGININFO_COLLECTION_URI_INDICATOR);
        sUriMatcher.addURI(LoginInfoMetaData.AUTHORITY
                , "logininfo/#"
                , INCOMING_SINGLE_LOGININFO_URI_INDICATOR);
    }

    private DatabaseHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        Log.i(TAG, "main onCreate called.");
        mOpenHelper = new DatabaseHelper(this.getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)){
            case INCOMING_LOGININFO_COLLECTION_URI_INDICATOR:
                qb.setTables(LoginInfoMetaData.TABLE_NAME);
                qb.setProjectionMap(sLoginInfoMap);
                break;
            case INCOMING_SINGLE_LOGININFO_URI_INDICATOR:
                qb.setTables(LoginInfoMetaData.TABLE_NAME);
                qb.setProjectionMap(sLoginInfoMap);
                qb.appendWhere(LoginInfoMetaData._ID
                        + "="
                        + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        String orderBy;
        if (TextUtils.isEmpty(sortOrder)){
            orderBy = LoginInfoMetaData.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
        if (getContext() != null) {
            c.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return c;
    }

    @Override
    public String getType(@NonNull Uri uri ) {
        switch (sUriMatcher.match(uri)){
            case INCOMING_LOGININFO_COLLECTION_URI_INDICATOR:
                return LoginInfoMetaData.CONTENT_TYPE;
            case INCOMING_SINGLE_LOGININFO_URI_INDICATOR:
                return LoginInfoMetaData.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        if (sUriMatcher.match(uri) != INCOMING_LOGININFO_COLLECTION_URI_INDICATOR){
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        Long now = System.currentTimeMillis();
        if (values.containsKey(LoginInfoMetaData.CREATED_DATE)){
            values.put(LoginInfoMetaData.CREATED_DATE, now);
        }
        if (values.containsKey(LoginInfoMetaData.MODIFIED_DATE)){
            values.put(LoginInfoMetaData.MODIFIED_DATE, now);
        }

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowId = db.insert(LoginInfoMetaData.TABLE_NAME, null, values);
        if (rowId > 0){
            Uri insertedUri = ContentUris.withAppendedId(LoginInfoMetaData.CONTENT_URI, rowId);
            if (getContext() != null) {
                getContext().getContentResolver().notifyChange(insertedUri, null);
            }

            return insertedUri;
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)){
            case INCOMING_LOGININFO_COLLECTION_URI_INDICATOR:
                count = db.delete(LoginInfoMetaData.TABLE_NAME, selection, selectionArgs);
                break;
            case INCOMING_SINGLE_LOGININFO_URI_INDICATOR:
                String rowId = uri.getPathSegments().get(1);
                count = db.delete(LoginInfoMetaData.TABLE_NAME
                        , LoginInfoMetaData._ID + "=" + rowId + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "")
                        , selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        return count;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)){
            case INCOMING_LOGININFO_COLLECTION_URI_INDICATOR:
                count = db.update(LoginInfoMetaData.TABLE_NAME, values, selection, selectionArgs);
                break;
            case INCOMING_SINGLE_LOGININFO_URI_INDICATOR:
                String rowId = uri.getPathSegments().get(1);
                count = db.update(LoginInfoMetaData.TABLE_NAME
                        , values
                        , LoginInfoMetaData._ID + "=" + rowId + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "")
                        , selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }
}
