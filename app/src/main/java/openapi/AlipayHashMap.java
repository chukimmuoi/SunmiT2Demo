package openapi;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yueweizyw on 17/9/25.
 */
public class AlipayHashMap extends HashMap {

    public AlipayHashMap() {

    }

    public AlipayHashMap(Map<String, String> textParams) {
        this.putAll(textParams);
    }
}
