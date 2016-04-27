package com.purplelight.mcommunity.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 掌上园区的数据库Meta信息
 * Created by wangyn on 16/4/26.
 */
public class MCommProviderMeta {
    public static final String DATABASE_NAME = "mcomm.db";
    public static final int DATABASE_VERSION = 1;

    public MCommProviderMeta(){}

    public static final class LoginInfoMetaData implements BaseColumns{
        private LoginInfoMetaData() {}
        public static final String AUTHORITY = "com.purplelight.mcommunity.provider.MCommProvider.logininfo";
        public static final String TABLE_NAME = "logininfo";

        public static final Uri CONTENT_URI =
                Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.purplelight.mcommunity.logininfo";

        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.purplelight.mcommunity.logininfo";

        public static final String DEFAULT_SORT_ORDER = "modified DESC";

        public static final String USERID = "userid";

        public static final String USERCODE = "usercode";

        public static final String USERNAME = "username";

        public static final String PASSWORD = "password";

        public static final String SEX = "sex";

        public static final String EMAIL = "email";

        public static final String PHONE = "phone";

        public static final String ADDRESS = "address";

        public static final String HEADIMGPATH = "headimagepath";

        public static final String CREATED_DATE = "created";

        public static final String MODIFIED_DATE = "modified";
    }
}
