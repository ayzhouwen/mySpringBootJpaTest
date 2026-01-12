package com.zw.util;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;

public class HttpHelper {

    /**
     * 发送 POST JSON 请求
     */
    public static HttpResponse postJson(String url, Object body) {
        return HttpRequest.post(url)
                .body(JSONUtil.toJsonStr(body))
                .contentType(ContentType.JSON.getValue())
                .timeout(10000)
                .execute();
    }

    /**
     * 发送 GET 请求
     */
    public static HttpResponse get(String url) {
        return HttpRequest.get(url)
                .timeout(10000)
                .execute();
    }

    /**
     * 发送 DELETE 请求
     */
    public static HttpResponse delete(String url) {
        return HttpRequest.delete(url)
                .timeout(10000)
                .execute();
    }
}
