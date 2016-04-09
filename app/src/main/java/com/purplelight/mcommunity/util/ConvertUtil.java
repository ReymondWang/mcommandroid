package com.purplelight.mcommunity.util;

import android.support.annotation.NonNull;
import android.util.Base64;

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

}
