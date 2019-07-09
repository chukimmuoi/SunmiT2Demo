/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package openapi;

/**
 * @author runzhi
 */
public interface AlipayClient {

    public <T extends AlipayResponse> void execute(AlipayRequest<T> request, AlipayCallBack callBack);

}
