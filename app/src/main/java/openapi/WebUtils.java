package openapi;

import java.util.HashMap;

/**
 * Created by yueweizyw on 17/9/25.
 */
public class WebUtils {

    public static String buildQuery(HashMap<String, String> protocalOptParams, String charset) {
        StringBuilder queryString = new StringBuilder();

        for (String item : protocalOptParams.keySet()) {
            if (queryString.length() == 0) {
                queryString.append(item + "=" + protocalOptParams.get(item));
            } else {
                queryString.append("&" + item + "=" + protocalOptParams.get(item));
            }

        }
        return queryString.toString();
    }

}
