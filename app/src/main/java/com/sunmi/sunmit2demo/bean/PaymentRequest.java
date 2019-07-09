package com.sunmi.sunmit2demo.bean;

import android.text.TextUtils;

public class PaymentRequest {


    public Config config;
    /**
     * 应用类型
     * 00:银行卡应用
     * 01:聚合扫码支付应用
     * 02:银行卡+银联扫码支付应用
     * 03-50由协议保留, 51往后为自定义扩展预留
     */
    public String appType;
    /**
     * 交易类型
     * 00－消费
     * 01－撤消
     * 02－退货
     * 03－预授权
     * 04－预授权撤销
     * 05－预授权完成
     * 06－预授权完成撤销
     * 07－结算
     * 08－签到
     * A1－打印
     * A2－本地交易记录查询
     */
    public String transType;
    /**
     * 应用包名
     */
    public String appId;
    /**
     * 交易金额
     * 单位为分，1元表示为100L
     */
    public long amount;
    /**
     * 支付码
     */
    public String payCode;
    /**
     * MIS订单号
     * 任意字符串，用于标识当笔交易的流水号，交易处理结果中会带回。
     */
    public String misId;
    /**
     * 商户订单号
     */
    public String orderId;
    /**
     * 原MIS订单号
     */
    public String oriMisId;
    /**
     * 原商户订单号
     */
    public String oriOrderId;
    /**
     * 原交易日期
     * 格式“MMdd”, 部分业务退货使用
     */
    public String oriTransDate;
    /**
     * 原系统参考号
     * 部分业务退货使用
     */
    public String oriReferenceNum;
    /**
     * 原凭证号
     * 撤销、打印使用
     */
    public String oriVoucherNum;
    /**
     * 原授权码
     * 预授权撤销使用
     */
    public String oriAuthNum;
    /**
     * 是否打印小票
     * 为“1”时打印；为“0”时不打印。默认打印
     */
    public String printTicket;

    public boolean checkBaseArgument() {
        if (TextUtils.isEmpty(appId))
            return false;

        switch (TransType.getTransType(transType)) {
            case CONSUME:
                if (!AppType.checkType(appType)) {
                    return false;
                }
                if (amount <= 0)
                    return false;
                return true;
            case RETURN_MONEY:
                if (TextUtils.isEmpty(oriMisId) && TextUtils.isEmpty(oriOrderId))
                    return false;
                if (amount <= 0)
                    return false;
                return true;
            case QUERY:
                if (TextUtils.isEmpty(orderId) && TextUtils.isEmpty(misId)) {
                    return false;
                }
                return true;
        }
        return false;
    }
}
