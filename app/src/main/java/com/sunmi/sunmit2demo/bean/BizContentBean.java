package com.sunmi.sunmit2demo.bean;

import java.util.List;

/**
 * Created by zhicheng.liu on 2018/4/4
 * address :liuzhicheng@sunmi.com
 * description :
 */

public class BizContentBean {

    /**
     * out_trade_no : 12449573
     * scene : security_code
     * auth_code : fp0f664885df27f85fb0349f0fdbb56952c
     * product_code : FACE_TO_FACE_PAYMENT
     * subject : 商米收银
     * total_amount : 0.01
     * body : Iphone6 16G
     * goods_detail : [{"goods_id":"apple-01","goods_name":"ipad","quantity":1,"price":2000,"goods_category":"34543238","body":"特价手机","show_url":"http://www.alipay.com/xxx.jpg"}]
     * operator_id : yx_001
     * store_id : 商米奶茶店
     * terminal_id : NJ_T_001
     * extend_params : {"sys_service_provider_id":"2088511833207846","industry_reflux_info":"{\\\"scene_code\\\":\\\"metro_tradeorder\\\",\\\"channel\\\":\\\"xxxx\\\",\\\"scene_data\\\":{\\\"asset_name\\\":\\\"ALIPAY\\\"}}","card_type":"S0JP0000"}
     * timeout_express : 90m
     */

    private String out_trade_no;
    private String scene;
    private String auth_code;
    private String product_code;
    private String subject;
    private double total_amount;
    private String body;
    private String operator_id;
    private String store_id;
    private String terminal_id;
    private ExtendParamsBean extend_params;
    private String timeout_express;
    private List<GoodsDetailBean> goods_detail;

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getAuth_code() {
        return auth_code;
    }

    public void setAuth_code(String auth_code) {
        this.auth_code = auth_code;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(String operator_id) {
        this.operator_id = operator_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getTerminal_id() {
        return terminal_id;
    }

    public void setTerminal_id(String terminal_id) {
        this.terminal_id = terminal_id;
    }

    public ExtendParamsBean getExtend_params() {
        return extend_params;
    }

    public void setExtend_params(ExtendParamsBean extend_params) {
        this.extend_params = extend_params;
    }

    public String getTimeout_express() {
        return timeout_express;
    }

    public void setTimeout_express(String timeout_express) {
        this.timeout_express = timeout_express;
    }

    public List<GoodsDetailBean> getGoods_detail() {
        return goods_detail;
    }

    public void setGoods_detail(List<GoodsDetailBean> goods_detail) {
        this.goods_detail = goods_detail;
    }

    public static class ExtendParamsBean {
        /**
         * sys_service_provider_id : 2088511833207846
         * industry_reflux_info : {\"scene_code\":\"metro_tradeorder\",\"channel\":\"xxxx\",\"scene_data\":{\"asset_name\":\"ALIPAY\"}}
         * card_type : S0JP0000
         */

        private String sys_service_provider_id;
        private String industry_reflux_info;
        private String card_type;

        public String getSys_service_provider_id() {
            return sys_service_provider_id;
        }

        public void setSys_service_provider_id(String sys_service_provider_id) {
            this.sys_service_provider_id = sys_service_provider_id;
        }

        public String getIndustry_reflux_info() {
            return industry_reflux_info;
        }

        public void setIndustry_reflux_info(String industry_reflux_info) {
            this.industry_reflux_info = industry_reflux_info;
        }

        public String getCard_type() {
            return card_type;
        }

        public void setCard_type(String card_type) {
            this.card_type = card_type;
        }
    }

    public static class GoodsDetailBean {
        /**
         * goods_id : apple-01
         * goods_name : ipad
         * quantity : 1
         * price : 2000
         * goods_category : 34543238
         * body : 特价手机
         * show_url : http://www.alipay.com/xxx.jpg
         */

        private String goods_id;
        private String goods_name;
        private int quantity;
        private String price;
        private String goods_category;
        private String body;
        private String show_url;

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getGoods_category() {
            return goods_category;
        }

        public void setGoods_category(String goods_category) {
            this.goods_category = goods_category;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getShow_url() {
            return show_url;
        }

        public void setShow_url(String show_url) {
            this.show_url = show_url;
        }
    }
}
