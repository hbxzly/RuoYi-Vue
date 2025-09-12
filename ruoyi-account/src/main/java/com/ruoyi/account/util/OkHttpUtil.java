package com.ruoyi.account.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class OkHttpUtil {

    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper(); // Jackson 用于 JSON 处理

    /**
     * 发送 POST 请求
     * @param url 请求地址
     * @param requestData 请求数据 (HashMap)
     * @return 响应 JSON (HashMap)
     * @throws IOException
     */
    public static Map<String, Object> sendPostRequest(String url, Map<String, Object> requestData) throws IOException {
        // 将 HashMap 转换为 JSON 字符串
        String jsonBody = objectMapper.writeValueAsString(requestData);

        // 创建请求体 (application/json)
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);


        // 构造请求
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        // 执行请求
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // 解析响应 JSON
            String responseJson = response.body().string();
            return objectMapper.readValue(responseJson, Map.class);
        }
    }

}
