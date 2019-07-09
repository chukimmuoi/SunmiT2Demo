package com.sunmi.sunmit2demo.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;


import java.io.File;

public class InstallApkUtils {
    public static String smilePkgName = "com.alipay.zoloz.smile";
    public static String SunmiPayPkgName = "com.sunmi.payment";

    public static boolean checkApkExist(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * get App versionCode * @param context * @return
     */
    public static int getVersionCode(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        int versionCode = 0;
        try {
            packageInfo = packageManager.getPackageInfo(packageName, 0);
            versionCode = packageInfo.versionCode;
            Log.e("@@@","versionCode=="+versionCode+" ");

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }


}
