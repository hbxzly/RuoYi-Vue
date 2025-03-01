package com.ruoyi.account.util;

import com.warrenstrange.googleauth.GoogleAuthenticator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class FBAccountUtil {


    /**
     * 获取双重验证
     * @param secretKey
     * @return
     */
    public static String getGoogleVerificationCode(String secretKey) {
        // 创建Google Authenticator实例
        GoogleAuthenticator gAuth = new GoogleAuthenticator();

        // 生成当前时间对应的TOTP密码
        int totpPassword = gAuth.getTotpPassword(secretKey);

        // 将TOTP密码格式化为字符串，确保不以0开头
        String formattedCode = String.format("%06d", totpPassword);

        return formattedCode;
    }


    public static boolean checkAccountActive(String accountId){
        String url = "https://graph.facebook.com/"+accountId+"/picture?type=normal";
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
            System.out.println(response.toString());
            return response.toString().contains("Photoshop");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;

    }
}
