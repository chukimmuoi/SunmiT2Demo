package com.sunmi.sunmit2demo.bean;

import java.util.HashMap;

public class AppType {
    private static HashMap<String, String> typeMap;

    static {
        /**
         *  应用类型
         *  00:银行卡应用
         *  01:聚合扫码支付应用
         *  02:银行卡+银联扫码支付应用
         *  03-50由协议保留, 51往后为自定义扩展预留
         */
        typeMap = new HashMap<>();
        typeMap.put("00", "银行卡应用");
        typeMap.put("01", "聚合扫码支付应用");
        typeMap.put("02", "银行卡+银联扫码支付应用");
        typeMap.put("51", "人脸");
    }

    public static boolean checkType(String type) {
        return typeMap.containsKey(type);
    }
}
