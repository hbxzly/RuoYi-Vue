package com.ruoyi.account.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class CompanyInfo {


    public static String getInfo(){

        String s ="";

        try {
            // 创建HttpClient实例
            HttpClient httpClient = HttpClients.createDefault();

            // 创建POST请求
            HttpPost httpPost = new HttpPost("https://www.meiguodizhi.com/api/v1/dz");

            // 设置请求体
            String payload = "{ \"path\": \"/\", \"method\": \"address\" }";
            StringEntity entity = new StringEntity(payload);
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-Type", "application/json");

            // 发送请求并获取响应
            HttpResponse response = httpClient.execute(httpPost);

            // 获取响应实体
            HttpEntity responseEntity = response.getEntity();

            // 读取响应内容
            String responseBody = EntityUtils.toString(responseEntity);

            // 获取响应代码
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200){
                s = responseBody;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return s;
    }

}
