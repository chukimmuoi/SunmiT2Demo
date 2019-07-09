package com.sunmi.sunmit2demo.bean;

public class PaymentResponse {
    /**
     * 协议版本号
     */
    public String version;
    /**
     * 交易结果返回码
     * 00：成功
     * 非00：失败, 见返回码定义
     */
    public String resultCode;
    /**
     * 错误描述
     */
    public String resultMsg;
    /**
     * 应用类型
     */
    public String appType;
    /**
     * 交易类型
     */
    public String transType;

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
     * 交易金额
     * 单位为分，1元表示为100L
     */
    public long amount;
    /**
     * 已退款金额
     * 单位为分，1元表示为100L
     */
    public long refunded;
    /**
     * 实付金额
     * 单位为分，1元表示为100L
     */
    public long amount1;
    /**
     * 优惠金额
     * 单位为分，1元表示为100L
     */
    public long amount2;
    /**
     * 商家优惠金额
     * 单位为分，1元表示为100L
     */
    public long amount3;
    /**
     * 交易日期
     * 格式“MMdd”
     */
    public String transDate;
    /**
     * 交易时间
     * 格式“HHmmss”
     */
    public String transTime;
    /**
     * 凭证号
     */
    public String voucherNum;
    /**
     * 批次号
     */
    public String batchNum;
    /**
     * 参考号
     */
    public String referenceNum;
    /**
     * 授权号
     */
    public String authNum;
    /**
     * 卡号
     */
    public String cardNum;
    /**
     * 发卡行
     */
    public String issuer;
    /**
     * 收单行
     */
    public String acquirer;
    /**
     * 操作员号
     */
    public String operatorId;
    /**
     * 卡类型
     */
    public String cardType;
    /**
     * 账户类型
     */
    public String accountType;
    /**
     * 机型
     */
    public String model;
    /**
     * 终端号
     */
    public String terminalId;
    /**
     * 商户号
     */
    public String merchantId;
    /**
     * 第三方平台订单号(微信、支付宝的订单号)
     **/
    public String platformId;
    /**
     * 原MIS订单号
     */
    public String oriMisId;
    /**
     * 原商户订单号
     */
    public String oriOrderId;
}
