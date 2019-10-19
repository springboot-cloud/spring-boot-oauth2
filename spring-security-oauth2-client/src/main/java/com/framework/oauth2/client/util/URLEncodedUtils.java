package com.framework.oauth2.client.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author XiongFeiYang
 * @description URL工具类
 * @createTime 2019-10-19 15:08
 **/
public class URLEncodedUtils {

    private URLEncodedUtils() {
    }

    /**
     * 将map中的参数转为URL参数
     *
     * @param map map
     * @return String
     */
    public static String toUrlParams(Map<?, ?> map) {
        StringBuilder urlParams = new StringBuilder();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (urlParams.length() > 0) {
                urlParams.append("&");
            }
            urlParams.append(String.format("%s=%s",
                    entry.getKey().toString(),
                    entry.getValue().toString()
            ));
        }
        return urlParams.toString();
    }

    /**
     * URL编码
     *
     * @param s s
     * @return String
     */
    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

}
