package com.sunmi.sunmit2demo.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.sunmi.sunmit2demo.MyApplication;

/**
 * Created by zhicheng.liu on 2018/3/26
 * address :liuzhicheng@sunmi.com
 * description :
 */

public class ResourcesUtils {
    @NonNull
    public static String getString(Context context,@StringRes int id){
        if(context==null){
            return MyApplication.getInstance().getResources().getString(id);
        }
        return context.getResources().getString(id);
    }
    @NonNull
    public static String getString(@StringRes int id){
        return getString(null,id);
    }
}
