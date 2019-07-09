package openapi;

/**
 * Created by yueweizyw on 17/9/26.
 */

public interface AlipayCallBack {
    <T extends AlipayResponse> T onResponse(T response);
}
