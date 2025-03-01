package com.ruoyi.account.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpClientUtil {

    public static Map<String, String> getIpAddress() {

        Map<String, String> map = new HashMap<String, String>();
        String url = "https://whoer.net/zh/main/api/ip";

        try {
            URL obj = new URL(url);


            // 设置代理（根据你的代理信息替换 host 和 port）
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890)); // 示例代理
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection(proxy);

            // 设置请求方法
            connection.setRequestMethod("GET");

            // 添加请求头
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36");

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // 读取响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            map.put("ip", JsonDataUtil.getValueByNodeName(response.toString(), "ip"));
            map.put("country", JsonDataUtil.getValueByNodeName(response.toString(), "country"));
            return map;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // GET 请求方法
    public static String sendGet(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            throw new RuntimeException("GET request failed. Response Code: " + responseCode);
        }
    }

    // POST 请求方法
    public static String sendPost(String url, Map<String, String> parameters) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // 将参数转换为 key=value 形式的字符串
        String formData = parameters.entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        // 发送 POST 请求
        con.setDoOutput(true);
        try (OutputStream os = con.getOutputStream()) {
            os.write(formData.getBytes());
            os.flush();
        }

        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            throw new RuntimeException("POST request failed. Response Code: " + responseCode);
        }
    }
}
