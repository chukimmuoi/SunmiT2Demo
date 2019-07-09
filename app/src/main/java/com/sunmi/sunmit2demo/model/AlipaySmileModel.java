package com.sunmi.sunmit2demo.model;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.sunmi.sunmit2demo.bean.BizContentBean;
import com.sunmi.sunmit2demo.bean.MenuBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import openapi.AlipayCallBack;
import openapi.AlipayClient;
import openapi.AlipayResponse;
import openapi.DefaultAlipayClient;
import openapi.ZimIdRequester;
import openapi.request.AlipayTradePayRequest;
import openapi.response.AlipayTradePayResponse;


/**
 * Created by zhicheng.liu on 2018/4/2
 * address :liuzhicheng@sunmi.com
 * description :
 */

public class AlipaySmileModel {
    public static String merchantId = "";
    public static String appKey = "";
    public static String Public_Key = "";
    public static String appId =  "";
    public static String payMethod = "zoloz.authentication.customer.smilepay.initialize";
    public static String version = "1.0";
    public static final String url = "https://openapi.alipay.com/gateway.do";
    public static int i = 1;
    private AlipayClient alipayClient;
    private MenuBean menuBean;

    public AlipaySmileModel() {

    }

    private AlipayClient getAlipayClient() {
        if (alipayClient == null)
            alipayClient = new DefaultAlipayClient(url, appId, appKey, "json", "UTF-8", Public_Key, "RSA2");
        return alipayClient;
    }

    public Map<String, String> buildMerchantInfo() {
        Map<String, String> merchantInfo = new HashMap();
        merchantInfo.put("partnerId", merchantId);
        merchantInfo.put("merchantId", merchantId);
        merchantInfo.put("appId", appId);
        merchantInfo.put("merchantKey", "");
        merchantInfo.put("storeCode", "TEST");
        merchantInfo.put("brandCode", "TEST");
        merchantInfo.put("areaCode", "");
        merchantInfo.put("geo", "0.000000,0.000000");
        merchantInfo.put("wifiMac", "");
        merchantInfo.put("wifiName", "");
        merchantInfo.put("deviceNum", merchantId);
        merchantInfo.put("deviceMac", "CC:B8:A8:0F:63:3C");
        return merchantInfo;
    }

    public void setGoods(MenuBean menuBean) {
        this.menuBean = menuBean;
    }


    /**
     * 发送mZimId 给服务器获得mZimInitClientData
     *
     * @param metainfo
     */

    public void getInitClientData(String metainfo, final ZimIdRequester.RequestCallback callback) {
        new ZimIdRequester().request(metainfo, new ZimIdRequester.RequestCallback() {
            @Override
            public void onRequestStart() {
                callback.onRequestStart();
            }

            @Override
            public void onRequestResult(final String zimId, final String zimInitClientData) {
                callback.onRequestResult(zimId, zimInitClientData);
            }
        });

    }

    /**
     * 开始扣款
     *
     * @param fToken
     * @param amount
     * @param out_trade_no
     */
    public void startChargeback(final String out_trade_no, final String fToken, final String amount, final String subject, final String store_id, final AlipayModelCallBack alipayModelCallBack) {
        String money = "0.0" + i;
        AlipayTradePayRequest request = new AlipayTradePayRequest(); //创建API对应的request类
        String json = JSON.toJSONString(buildAlipayRequest(out_trade_no, fToken, money, subject, store_id));
        Log.e("@@@@@", json);
        request.setBizContent(json);
        getAlipayClient().execute(request, new AlipayCallBack() {
            @Override
            public <T extends AlipayResponse> T onResponse(T response) {
                if (response.isSuccess()) {
                    alipayModelCallBack.onPayResult((AlipayTradePayResponse) response);
                } else {
                    alipayModelCallBack.onFail(response.getSubMsg());
                }
                return response;
            }
        });
    }

    private BizContentBean buildAlipayRequest(String out_trade_no, String fToken, String money, String subject, String store_id) {
        BizContentBean bizContentBean = new BizContentBean();
        bizContentBean.setOut_trade_no(out_trade_no);
        bizContentBean.setScene("security_code");
        bizContentBean.setAuth_code(fToken);
        bizContentBean.setSubject(subject);
        bizContentBean.setProduct_code("FACE_TO_FACE_PAYMENT");
        bizContentBean.setTotal_amount(Double.parseDouble(money));
        bizContentBean.setBody(menuBean.getTitle());
        List<BizContentBean.GoodsDetailBean> goods_detail = new ArrayList<>();
        for (MenuBean.ListBean listBean : menuBean.getList()) {
            BizContentBean.GoodsDetailBean goodsDetailBean = new BizContentBean.GoodsDetailBean();
            goodsDetailBean.setBody(listBean.getParam2());
            goodsDetailBean.setGoods_id(listBean.getParam1());
            goodsDetailBean.setGoods_name(listBean.getParam2());
            goodsDetailBean.setPrice(listBean.getParam3().substring(1));
            goodsDetailBean.setQuantity(1);
            goods_detail.add(goodsDetailBean);
        }
        bizContentBean.setGoods_detail(goods_detail);
        bizContentBean.setOperator_id("yx_001");
        bizContentBean.setStore_id(store_id);
        bizContentBean.setTerminal_id("NJ_T_001");
        bizContentBean.setTimeout_express("2m");
        return bizContentBean;
    }

    public interface AlipayModelCallBack {
        void onPayResult(AlipayTradePayResponse result);

        void onFail(String msg);
    }
}
