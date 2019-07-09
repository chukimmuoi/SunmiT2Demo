package com.sunmi.sunmit2demo.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.sunmi.payment.PaymentService;
import com.sunmi.sunmit2demo.bean.Config;
import com.sunmi.sunmit2demo.bean.PaymentRequest;
import com.sunmi.sunmit2demo.bean.PaymentResponse;
import com.sunmi.sunmit2demo.bean.TransType;
import com.sunmi.sunmit2demo.receiver.ResultReceiver;
import com.sunmi.sunmit2demo.utils.InstallApkUtils;


/**
 * Created by zhicheng.liu on 2018/3/30
 * address :liuzhicheng@sunmi.com
 * description :
 */

public class PayMentPayPresenter {

    private static final String TAG = "PayMentPayPresenter";
    Context context;
    ResultReceiver.AliResultCallback aliResultCallback;

    /**
     * 初始化刷脸服务
     *
     * @param context getApplicationContext()
     */
    public PayMentPayPresenter(Context context) {
        this.context = context;

    }


    public void init(ResultReceiver.AliResultCallback aliResultCallback) {
        this.aliResultCallback = aliResultCallback;
        initReceiver();
    }

    public boolean startFaceService(String orderId, String phoneNumber) {
        startFaceService(orderId, phoneNumber, 1);
        return true;
    }

    /**
     * 开始刷脸
     *
     * @return
     */
    public boolean startFaceService(String orderId, String phoneNumber, long money) {
        if (!InstallApkUtils.checkApkExist(context, InstallApkUtils.SunmiPayPkgName)) {
            aliResultCallback.onFail(new PaymentResponse());
            Toast.makeText(context, "no install SunmiPay", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            phoneNumber = "";
        }
        execute(orderId, phoneNumber, money);
        return true;
    }

    private void execute(String orderId, String phoneNumber, long money) {
        Intent intent = new Intent();
        intent.setAction("sunmi.payment.action.entry");
        intent.setPackage("com.sunmi.payment");
        PaymentRequest request = new PaymentRequest();
        request.appType = "51";
        request.appId = context.getPackageName();
        request.transType = TransType.CONSUME.Code();
        request.amount = 1/*money*/;
        request.orderId = orderId;
        request.printTicket = "0";
        request.payCode = phoneNumber;
        Config config = new Config();
        config.setResultDisplay(false);
        request.config = config;
        String jsonString = jsonString(request);
        PaymentService.getInstance().callPayment(jsonString);
    }


    public String jsonString(PaymentRequest request) {
        String string = JSON.toJSONString(request);
        return string;
    }

    private void initReceiver() {
        if (!InstallApkUtils.checkApkExist(context, InstallApkUtils.SunmiPayPkgName)) {
            return;
        }
        Log.e(TAG, "initReceiver = ");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("sunmi.payment.action.result");
        resultReceiver = new ResultReceiver();
        context.registerReceiver(resultReceiver, intentFilter);
        resultReceiver.setResultCallback(aliResultCallback);
    }

    ResultReceiver resultReceiver;

    public void destoryReceiver() {
        if (resultReceiver != null) {
            context.unregisterReceiver(resultReceiver);
        }
    }

}
