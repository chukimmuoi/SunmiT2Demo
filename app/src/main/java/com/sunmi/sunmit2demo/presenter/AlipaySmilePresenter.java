package com.sunmi.sunmit2demo.presenter;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.zoloz.smile2pay.service.Zoloz;
import com.alipay.zoloz.smile2pay.service.ZolozCallback;
import com.sunmi.sunmit2demo.R;
import com.sunmi.sunmit2demo.model.AlipaySmileModel;
import com.sunmi.sunmit2demo.utils.ResourcesUtils;

import openapi.ZimIdRequester;

import java.util.HashMap;
import java.util.Map;

import openapi.response.AlipayTradePayResponse;


/**
 * Created by zhicheng.liu on 2018/3/30
 * address :liuzhicheng@sunmi.com
 * description :
 */

public class AlipaySmilePresenter implements AlipaySmileModel.AlipayModelCallBack {

    private static final String TAG = AlipaySmilePresenter.class.getName();

    public static final String KEY_INIT_RESP_NAME = "zim.init.resp";
    public static final String KEY_IPHONE_NUMBER = "phone_number";
    public static final String RESPONSE_SUCCESS = "1000";
    public static final String RESPONSE_CANCEL_BY_USER = "1003";
    public static final String RESPONSE_TIMEOUT_BY_USER = "1004";
    public static final String RESPONSE_CHOOSE_OTHER_PAYMENT = "1005";

    Map<String, String> merchantInfo = new HashMap();

    String mZimId;
    String mZimInitClientData;
    String money;
    String subject;
    String store;
    private String phoneNumber;

    private Zoloz zoloz;
    AlipaySmileCallBack alipaySmileCallBack;
    AlipaySmileModel model;
    Context context;

    public interface AlipaySmileCallBack {
        void onStartFaceService();

        void onFaceSuccess(String code, String msg);

        void onSuccess(String code, String error);

        void onGetMetaInfo(String metaInfo);

        void onGetZimIdSuccess(String zimId);

        void onFail(String code, String msg);
    }

    /**
     * 初始化刷脸服务
     *
     * @param context getApplicationContext()
     * @param model   数据
     */
    public AlipaySmilePresenter(Context context, AlipaySmileModel model) {
        this.context=context;
        zoloz = com.alipay.zoloz.smile2pay.service.Zoloz.getInstance(context);
        this.model = model;

    }

    public void init(Map<String, String> merchantInfo, String money, String subject, String store) {
        this.money = money;
        this.subject = subject;
        this.store = store;
        this.merchantInfo = merchantInfo;
        zoloz.zolozInstall(merchantInfo);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * 获得 zimId
     */
    private void GetMetaInfo() {
        zoloz.zolozGetMetaInfo(merchantInfo, new ZolozCallback() {
            @Override
            public void response(Map smileToPayResponse) {
                if (smileToPayResponse == null) {
                    Log.e(TAG, "zolozGetMetaInfo response is null");
                    return;
                }

                String code = (String) smileToPayResponse.get("code");
                String metaInfo = (String) smileToPayResponse.get("metainfo");
                Log.e(TAG, "zolozGetMetaInfo response is "+code);
                //获取metainfo成功
                if (RESPONSE_SUCCESS.equalsIgnoreCase(code) && metaInfo != null && !TextUtils.isEmpty(metaInfo)) {
                    Log.i(TAG, "metainfo is:" + metaInfo);
                    alipaySmileCallBack.onGetMetaInfo(metaInfo);
                    model.getInitClientData(metaInfo, requestCallback);
                } else {
                    alipaySmileCallBack.onFail(code, metaInfo);
                }
            }
        });
    }


    /**
     * 初始化刷脸
     *
     * @return
     */
    public boolean startFaceService(AlipaySmileCallBack alipaySmileCallBacks) {
        if (merchantInfo == null || merchantInfo.isEmpty()) {
            return false;
        }
        this.alipaySmileCallBack = alipaySmileCallBacks;
        GetMetaInfo();
        return true;
    }
    /**
     * 开始刷脸
     */
    private void execute(String mZimId, String mZimInitClientData) {
        alipaySmileCallBack.onStartFaceService();
        final Map params = new HashMap();
        params.put(KEY_INIT_RESP_NAME, mZimInitClientData);
        if(!TextUtils.isEmpty(phoneNumber)) {
            params.put(KEY_IPHONE_NUMBER, phoneNumber);
        }
        zoloz.zolozVerify(mZimId, params, new ZolozCallback() {
            @Override
            public void response(Map smileToPayResponse) {
                if (smileToPayResponse == null) {
                    Log.e(TAG, "zolozVerify response is null");
                    return;
                }
                String code = (String) smileToPayResponse.get("code");
                final String fToken = (String) smileToPayResponse.get("ftoken");
                String subCode = (String) smileToPayResponse.get("subCode");
                String msg = (String) smileToPayResponse.get("msg");
                Log.e(TAG, "zolozVerify , code:" + code + "subcode:" + subCode + "msg" + msg + " ftoken" + fToken);
                //刷脸成功
                if (RESPONSE_SUCCESS.equalsIgnoreCase(code) && fToken != null) {
                    alipaySmileCallBack.onFaceSuccess(code, msg);
//                      开始扣款
                    model.startChargeback(SystemClock.uptimeMillis() + "", fToken, money, subject, store, AlipaySmilePresenter.this);
                } else {
                    alipaySmileCallBack.onFail(code, msg);
                }
            }
        });

    }

    ZimIdRequester.RequestCallback requestCallback = new ZimIdRequester.RequestCallback() {
        @Override
        public void onRequestStart() {

        }

        @Override
        public void onRequestResult(String zimId, String zimInitClientData) {
            Log.e(TAG,"onRequestResult====="+zimId+" zimInitClientData ==="+zimInitClientData);
            if (!TextUtils.isEmpty(zimId) && !TextUtils.isEmpty(zimInitClientData)) {
                mZimId = zimId;
                mZimInitClientData = zimInitClientData;
                alipaySmileCallBack.onGetZimIdSuccess(zimId);
                execute(mZimId, mZimInitClientData);
            } else {
                alipaySmileCallBack.onFail("-1001", ResourcesUtils.getString(context, R.string.tips_net_fail));
                return;
            }
        }
    };

    @Override
    public void onPayResult(AlipayTradePayResponse result) {
        String code = result.getCode();
        String error = result.getMsg();
        Log.i("@@@@@@@@", "支付成功  " + error);
        alipaySmileCallBack.onSuccess(code, error);
    }

    @Override
    public void onFail(String msg) {
        Log.i("@@@@@@@@", "支付异常" + msg);
        alipaySmileCallBack.onFail("-1", msg);
    }


    public void close() {
        zoloz.zolozUninstall();
    }
}
