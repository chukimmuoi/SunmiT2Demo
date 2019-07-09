package com.sunmi.sunmit2demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import sunmi.ds.DSReceiver;

/**
 * Created by highsixty on 2017/9/21.
 */

public class MyBroadCastReceiver extends BroadcastReceiver {

    private DSReceiver mDSReceiver = null;
    private static final String RECEIVE_DATA = "com.sunmi.hcservice";
    private static final String CONNECT_STATE = "com.sunmi.hcservice.status";

    @Override
    public void onReceive(Context context, Intent intent) {
        mDSReceiver = DSReceiver.getInstance(context);
        if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
            switch (intent.getAction()) {
                case RECEIVE_DATA:
                    mDSReceiver.onReceive(intent);
                    break;
                case CONNECT_STATE:
                    mDSReceiver.onConnectStateChange(intent);
                    break;
            }
        }
    }


}
