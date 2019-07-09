package openapi.response;

import com.alibaba.fastjson.annotation.JSONField;

import openapi.AlipayResponse;

/**
 * Created by yueweizyw on 17/9/26.
 */

public class AlipayCommerceLogisticsUserOrderQueryResponse extends AlipayResponse {

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    @JSONField(name = "order_info")
    String orderInfo = "";
}
