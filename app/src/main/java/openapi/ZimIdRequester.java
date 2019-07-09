package openapi;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sunmi.sunmit2demo.model.AlipaySmileModel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import openapi.http.SignUtils;


/**
 * @author xinwen
 */
public class ZimIdRequester {

    private static final String TAG = "ZimIdRequester";


    private static String privateKey = "";

    private static final String RSA2 = "RSA2";

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public interface RequestCallback {
        /**
         * 网络请求开始
         */
        void onRequestStart();

        /**
         * 网络请求结果返回
         *
         * @param zimId Zim Id
         */
        void onRequestResult(String zimId, String zimInitClientData);
    }

    public static String getDateFormat(long sd) {
        Date dat = new Date(sd);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);

        String sb = format.format(gc.getTime());
        return sb;
    }

    /**
     * 请求Zim Id
     *
     * 异步操作，不会阻塞调用线程。请求结果通过callback回调给调用方
     *
     * @param zimmetainfo  zimmetainfo
     * @param callback RequestCallback
     */
    public void request(final String zimmetainfo, final RequestCallback callback) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onRequestStart();

                String zimId = null;
                String zimInitClientData = null;
                try {
                    String gateway = "";
                    String method = "";
                    String appid = "";

                    method = AlipaySmileModel.payMethod;

                    // 开放平台，正式网关
                    gateway = AlipaySmileModel.url;

                    privateKey = AlipaySmileModel.appKey;
                    appid = AlipaySmileModel.appId;

                    String timestamp = getDateFormat(System.currentTimeMillis());

                    Map<String, String> params = new HashMap<String, String>();
                    //params.put("ws_service_url", request.ws_service_url);
                    params.put("timestamp", timestamp);
                    params.put("zimmetainfo",zimmetainfo);
                    //params.put("charset", request.charset);
                    params.put("app_id", appid);
                    params.put("method", method);
                    params.put("version", AlipaySmileModel.version);
                    params.put("sign_type", RSA2);
                    String signContent = SignUtils.getSignContent(params);
                    //BioLog.w("signContent=" + signContent);

                    //gateway += "?ws_service_url=";
                    //gateway += URLEncoder.encode(request.ws_service_url, "UTF-8");

                    gateway += "?sign=";
                    gateway += URLEncoder.encode(SignUtils.rsaSign(signContent, privateKey, "UTF-8", RSA2), "UTF-8");

                    gateway += "&timestamp=";
                    gateway += URLEncoder.encode(timestamp, "UTF-8");

                    gateway += "&zimmetainfo=";
                    gateway += URLEncoder.encode(zimmetainfo, "UTF-8");

                    gateway += "&sign_type=";
                    gateway += URLEncoder.encode(RSA2, "UTF-8");

                    //gateway += "&charset=";
                    //gateway += URLEncoder.encode(request.charset, "UTF-8");

                    gateway += "&app_id=";
                    gateway += URLEncoder.encode(appid, "UTF-8");

                    gateway += "&method=";
                    gateway += URLEncoder.encode(method, "UTF-8");

                    gateway += "&version=";
                    gateway += URLEncoder.encode(AlipaySmileModel.version, "UTF-8");

                    URL url = new URL(gateway);

                    Log.i(TAG, "url=" + url);

                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                    //通过setRequestMethod将conn设置成POST方法
                    conn.setRequestMethod("GET");

                    conn.setConnectTimeout((int)TimeUnit.SECONDS.toMillis(5));
                    conn.setReadTimeout((int)TimeUnit.SECONDS.toMillis(5));

                    //调用conn.setDoOutput()方法以显式开启请求体
                    conn.setDoOutput(true);

                    //用setRequestProperty方法设置一个自定义的请求头:paraData，由于后端判断
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

                    //String body = new String("paraData=" + paraData);
                    //
                    //// 获取conn的输出流
                    //OutputStream os = conn.getOutputStream();
                    //// 获取两个键值对name=孙群和age=27的字节数组，将该字节数组作为请求体
                    //byte[] requestBody = body.getBytes("utf-8");
                    //// 将请求体写入到conn的输出流中
                    //os.write(requestBody);
                    //// 记得调用输出流的flush方法
                    //os.flush();
                    //// 关闭输出流
                    //os.close();

                    //当调用getInputStream方法时才真正将请求体数据上传至服务器
                    InputStream is = conn.getInputStream();
                    //获得响应体的字节数组
                    byte[] responseBody = getBytesByInputStream(is);
                    Log.i(TAG, "responseBody=" + new String(responseBody, "utf-8"));

                    JSONObject jsonObject = JSON.parseObject(new String(responseBody, "utf-8"));
                    JSONObject respJson = new JSONObject();
                    String retObject = "";
                    retObject = "zoloz_authentication_customer_smilepay_initialize_response";
                    respJson = jsonObject.getJSONObject(retObject);

                    String result = respJson.getString("result");
                    Log.e("@@@@@@@","request"+result);
                    JSONObject resultJson = JSON.parseObject(result);
                    zimId = resultJson.getString("zimId");
                    zimInitClientData = resultJson.getString("zimInitClientData");

                    //获得响应头
                    String responseHeader = getResponseHeader(conn);
                    Log.i(TAG, "responseHeader=" + responseHeader);
                } catch (Throwable e) {
                    Log.w(TAG, e);
                } finally {
                    callback.onRequestResult(zimId, zimInitClientData);
                }
            }
        });
    }

    /**
     * 读取请求头
     *
     * @param conn HttpURLConnection
     * @return String
     */
    private String getReqeustHeader(HttpURLConnection conn) {
        Map<String, List<String>> requestHeaderMap = conn.getRequestProperties();
        Iterator<String> requestHeaderIterator = requestHeaderMap.keySet().iterator();
        StringBuilder sbRequestHeader = new StringBuilder();
        while (requestHeaderIterator.hasNext()) {
            String requestHeaderKey = requestHeaderIterator.next();
            String requestHeaderValue = conn.getRequestProperty(requestHeaderKey);
            sbRequestHeader.append(requestHeaderKey);
            sbRequestHeader.append(":");
            sbRequestHeader.append(requestHeaderValue);
            sbRequestHeader.append("\n");
        }
        return sbRequestHeader.toString();
    }

    /**
     * 读取响应头
     *
     * @param conn HttpURLConnection
     * @return
     */
    private String getResponseHeader(HttpURLConnection conn) {
        Map<String, List<String>> responseHeaderMap = conn.getHeaderFields();
        int size = responseHeaderMap.size();
        StringBuilder sbResponseHeader = new StringBuilder();
        for (int i = 0; i < size; i++) {
            String responseHeaderKey = conn.getHeaderFieldKey(i);
            String responseHeaderValue = conn.getHeaderField(i);
            sbResponseHeader.append(responseHeaderKey);
            sbResponseHeader.append(":");
            sbResponseHeader.append(responseHeaderValue);
            sbResponseHeader.append("\n");
        }
        return sbResponseHeader.toString();
    }

    /**
     * 根据字节数组构建UTF-8字符串
     *
     * @param bytes byte[]
     * @return String
     */
    private String getStringByBytes(byte[] bytes) {
        String str = "";
        try {
            str = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 从InputStream中读取数据，转换成byte数组，最后关闭InputStream
     *
     * @param is InputStream
     * @return byte[]
     */
    private byte[] getBytesByInputStream(InputStream is) {
        byte[] bytes = null;
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(baos);
        byte[] buffer = new byte[1024 * 8];
        int length = 0;
        try {
            while ((length = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }
            bos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bytes;
    }
}
