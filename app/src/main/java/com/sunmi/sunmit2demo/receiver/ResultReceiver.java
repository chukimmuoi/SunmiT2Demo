package com.sunmi.sunmit2demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.sunmi.sunmit2demo.bean.PaymentResponse;

public class ResultReceiver extends BroadcastReceiver {
    private static final String TAG = "ResultReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("sunmi.payment.action.result".equals(intent.getAction())) {
            String responseStr = intent.getStringExtra("response");
            Log.e(TAG, "response = " + responseStr);
            final PaymentResponse response = JSON.parseObject(responseStr, PaymentResponse.class);
            if ("T00".equals(response.resultCode)) {
                aliResultCallback.onSuccess(response);
            } else {
                aliResultCallback.onFail(response);
            }
        }
    }

    private AliResultCallback aliResultCallback;

    public void setResultCallback(AliResultCallback callback) {
        this.aliResultCallback = callback;
    }

    public interface AliResultCallback {
        void onSuccess(PaymentResponse response);

        void onFail(PaymentResponse response);
    }
}
