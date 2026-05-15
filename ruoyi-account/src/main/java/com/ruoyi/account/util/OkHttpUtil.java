package com.ruoyi.account.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class OkHttpUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .callTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(new TimeCostInterceptor())
            .build();

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    /**
     * POST JSON 请求（无 Header）
     */
    public static Map<String, Object> post(String url, Map<String, Object> requestData) {
        return post(url, requestData, Collections.emptyMap());
    }

    /**
     * POST JSON 请求（支持 Header）
     */
    public static Map<String, Object> post(
            String url,
            Map<String, Object> requestData,
            Map<String, String> headers) {

        try {
            String jsonBody = objectMapper.writeValueAsString(requestData);

            RequestBody body = RequestBody.create(JSON, jsonBody);

            Request.Builder builder = new Request.Builder()
                    .url(url)
                    .post(body);

            // 追加 Header
            headers.forEach(builder::addHeader);

            Request request = builder.build();

            try (Response response = client.newCall(request).execute()) {

                if (!response.isSuccessful()) {
                    throw new RuntimeException(
                            "HTTP 请求失败，code=" + response.code() +
                                    ", msg=" + response.message()
                    );
                }

                ResponseBody responseBody = response.body();
                if (responseBody == null) {
                    throw new RuntimeException("响应体为空");
                }

                String responseJson = responseBody.string();
                System.out.println(responseJson);

                return objectMapper.readValue(
                        responseJson,
                        new TypeReference<Map<String, Object>>() {}
                );
            }

        } catch (IOException e) {
            throw new RuntimeException("OkHttp POST 请求异常：" + e.getMessage(), e);
        }
    }

    /**
     * 请求耗时统计拦截器
     */
    static class TimeCostInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            long start = System.currentTimeMillis();
            Request request = chain.request();
            try {
                return chain.proceed(request);
            } finally {
                long cost = System.currentTimeMillis() - start;
                System.out.println(
                        "[OkHttp] " + request.method() +
                                " " + request.url() +
                                " 耗时：" + cost + " ms"
                );
            }
        }
    }
}
