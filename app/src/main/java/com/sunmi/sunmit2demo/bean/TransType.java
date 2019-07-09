package com.sunmi.sunmit2demo.bean;

import android.text.TextUtils;

public enum TransType {
    //对外
    CONSUME("00"),//－消费
    PRE_AUTH("03"),//－预授权
    PRE_AUTH_COMPLETE("05"),//－预授权完成
    RETURN_MONEY("09"),//－退款
    PRINT("A1"),//－打印
    QUERY("A2"),//－本地交易记录查询
    //内部使用
    REVOKE("01"),//－撤消
    RETURN_GOODS("02"),//－退货
    PRE_AUTH_REVOKE("04"),//－预授权撤销
    PRE_AUTH_COMPLETE_REVOKE("06"),//－预授权完成撤销
    SETTLEMENT("07"),//－结算(换班)
    SIGN_IN("08");//－签到


    TransType(String code) {
        this.code = code;
    }

    private String code;

    public String Code() {
        return code;
    }

    public static TransType getTransType(String code) {
        if (TextUtils.isEmpty(code)) {
            throw new IllegalArgumentException();
        }
        switch (code) {
            case "00":
                return CONSUME;
            case "03":
                return PRE_AUTH;
            case "05":
                return PRE_AUTH_COMPLETE;
            case "09":
                return RETURN_MONEY;
            case "A1":
                return PRINT;
            case "A2":
                return QUERY;
        }
        throw new IllegalArgumentException();
    }

}
