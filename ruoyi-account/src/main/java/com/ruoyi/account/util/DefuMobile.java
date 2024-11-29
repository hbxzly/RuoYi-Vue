package com.ruoyi.account.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.parameters.P;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DefuMobile {

    public static String token = "IRfKtE+XCZxBz/RMqPjAZ1uIhna2ux7s97UQVO2kyK3flt712niRQUo97IreFux14YOKlYETE+1fxOFsXZumOMNMoH/rFo64WkWngbANr4KI5pbLC9Mgfch+YNliA2nWp1CYMnReIX/PicrtDJ8ywG2JkpTDr7t2ZnfelxKkB2s=";

    /**
     * 获取号码
     * @return
     */
    public static String getMobile(){

        try {
            // 指定要请求的URL
            String urlString = "http://api.sqhyw.net:90/api/get_mobile?token="+token+"&project_id=10345&scope=171";

            URL url = new URL(urlString);

            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 设置请求方法为GET
            connection.setRequestMethod("GET");
            // 获取响应代码
            int responseCode = connection.getResponseCode();
            if (responseCode == 200){
                String readResponse = readResponse(connection);
                System.out.println(readResponse);
                // 关闭连接
                connection.disconnect();
                return jsonExtractor(readResponse,"mobile");
            }else {
                connection.disconnect();
                return "请求失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "异常";
        }
    }

    /**
     * 获取结点值
     * @param jsonResponse
     * @param nodeName
     * @return
     */
    public static String jsonExtractor(String jsonResponse, String nodeName) {

        if (!nodeName.equals("code")){
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                //判断请求状态
                if (jsonNode.get("message").asText().equals("ok")){
                    //判断是否存在结点
                    if (jsonNode.has(nodeName)){
                        return jsonNode.get(nodeName).asText();
                    }
                    return jsonNode.path("data").get(0).path(nodeName).asText();
                }else {
                    return "请求失败";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "异常";
            }
        }else {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                if (jsonNode.has(nodeName)){
                    return jsonNode.get(nodeName).asText();
                }else {
                    return "wait";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "异常";
            }
        }

    }

    /**
     * 读取返回数据
     * @param connection
     * @return
     * @throws Exception
     */
    public static  String readResponse(HttpURLConnection connection) throws Exception {
        // 读取响应内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();

        return response.toString();
    }

    /**
     * 获取验证码
     * @param mobile
     * @return
     */
    public static String getCode(String mobile){

        try {
            // 指定要请求的URL
            String urlString = "http://api.sqhyw.net:90/api/get_message?token="+token+"&project_id=10345&phone_num="+mobile;

            URL url = new URL(urlString);

            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 设置请求方法为GET
            connection.setRequestMethod("GET");

            // 获取响应代码
            int responseCode = connection.getResponseCode();
            if (responseCode == 200){
                String readResponse = readResponse(connection);
                // 关闭连接
                connection.disconnect();
                return jsonExtractor(readResponse,"code");
            }else {
                connection.disconnect();
                return "请求失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "异常";
        }
    }
}
