package com.ruoyi.account.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DefuUtil {


    public static String token = "EUjJbMHBn9YDEhXxMJi959hIX43Wz+IVHCDfCrkBjki4GxTruoj37zWELSHW0MAKJ4E3YN5gLPJECxVTa94JBJikRTdfyt/e+iNRwrIMeAIh/EOHfdNIVH1Pz6pqDx853cUcszHH5fc88a8i5SzRbNdwk5q4Gc9eRxjYVerGjEI=";

    /**
     * 获取号码
     * @return
     */
    public static String getMobile(String projectId){

        try {
            // 指定要请求的URL
            String urlString = "http://api.sqhyw.net:90/api/get_mobile?token="+token+"&project_id="+projectId;

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
     * 获取号码
     * @return
     */
    public static String getMobile(String projectId, String scope_black){

        try {
            // 指定要请求的URL
            String urlString = "http://api.sqhyw.net:90/api/get_mobile?token="+token+"&project_id="+projectId+"&scope_black="+scope_black;

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
                System.out.println(readResponse);
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
     * 获取号码
     * @return
     */
    public static String getMobileByOperator(String projectId, String scope_black, String operator){

        try {
            // 指定要请求的URL
            String urlString = "http://api.sqhyw.net:90/api/get_mobile?token="+token+"&project_id="+projectId+"&scope_black="+scope_black+"&operator="+operator;

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
                System.out.println(readResponse);
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
     * 获取号码
     * @return
     */
    public static String getMobile(String projectId, String scope, String scope_black ){

        try {
            // 指定要请求的URL
            String urlString = "http://api.sqhyw.net:90/api/get_mobile?token="+token+"&project_id="+projectId+"&scope_black="+scope_black+"&scope="+scope;

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
                System.out.println(readResponse);
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
     * 获取号码
     * @return
     */
    public static String getMobileByPhone(String projectId, String phone_num){

        try {
            // 指定要请求的URL
            String urlString = "http://api.sqhyw.net:90/api/get_mobile?token="+token+"&project_id="+projectId+"&phone_num="+phone_num;

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
    public static String getCode(String mobile,String projectId){

        try {
            // 指定要请求的URL
            String urlString = "http://api.sqhyw.net:90/api/get_message?token="+token+"&project_id="+projectId+"&phone_num="+mobile;

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
