//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sunmi.sunmit2demo.utils;

import com.sunmi.sunmit2demo.R;

import java.util.Calendar;

public class Utils {

    public static String getPmOrAm() {
        long var0 = System.currentTimeMillis();
        Calendar var2 = Calendar.getInstance();
        var2.setTimeInMillis(var0);
        return var2.get(Calendar.AM_PM) == 0 ? ResourcesUtils.getString(R.string.user_hello_am) : ResourcesUtils.getString(R.string.user_hello_pm);
    }
}
