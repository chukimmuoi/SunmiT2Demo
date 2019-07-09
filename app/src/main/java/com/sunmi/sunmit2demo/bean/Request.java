package com.sunmi.sunmit2demo.bean;


public class Request {
    /**
     * 应用类型
     */
    public String appType;
    /**
     * 交易类型
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
    public Long amount;
    /**
     * 支付码
     */
    public String payCode;
    /**
     * 收银台流水号
     */
    public String misId;
    /**
     * Saas软件订单号
     */
    public String orderId;
    /**
     * 商户订单号
     */
    public String platformId;
    /**
     * 原收银台流水号
     */
    public String oriMisId;
    /**
     * 原Saas软件订单号
     */
    public String oriOrderId;
    /**
     * 原商户订单号
     */
    public String oriPlatformId;
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
    @Deprecated
    public String printTicket;

    /**
     * 商品信息
     */
    public String orderInfo;

    /**
     * 订单类型：
     * 1：收款(非金融)
     * 2：退款(非金融)
     * 3：银行卡（金融）
     */
    public String orderType;
    /**
     * 日期，格式：2018-09-12;
     */
    public String date;
    /**
     * 分页页码，从1开始
     */
    public Integer page;
    /**
     * 每页返回交易记录的数量, 最大50
     */
    public Integer pageSize;

    /**
     * 配置参数(UI、打印等参数配置)
     */
    public Config config;
}