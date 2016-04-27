package com.purplelight.mcommunity.util;

import android.support.annotation.NonNull;
import android.util.Base64;

import com.purplelight.mcommunity.fastdfs.ProtoCommon;

/**
 * 工具类
 * Created by wangyn on 15/7/22.
 */
public class ConvertUtil {

    public static double ToDouble(String strVal){
        try{
            return Double.parseDouble(strVal);
        }catch (Exception ex){
            return 0;
        }
    }

    public static int ToInt(String strVal){
        try{
            return Integer.parseInt(strVal);
        }catch(Exception ex){
            return 0;
        }
    }

    public static long ToLong(String strVal){
        try{
            return Long.parseLong(strVal);
        }catch(Exception ex){
            return 0;
        }
    }

    @NonNull
    public static String ToBase64String(String str){
        return Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
    }

    @NonNull
    public static String DecodeBase64(String baseStr){
        return new String(Base64.decode(baseStr, Base64.DEFAULT));
    }

    public static byte split_file_id(String file_id, String[] results, String splitor) {
        int pos = file_id.indexOf(splitor);
        if ((pos <= 0) || (pos == file_id.length() - 1)) {
            return ProtoCommon.ERR_NO_EINVAL;
        }

        results[0] = file_id.substring(0, pos); // group name
        results[1] = file_id.substring(pos + 1); // file name
        return 0;
    }

}
