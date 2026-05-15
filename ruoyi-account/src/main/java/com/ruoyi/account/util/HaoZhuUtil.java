package com.ruoyi.account.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HaoZhuUtil {




//        Response Code: 200
//        Response: {"msg":"success","code":0,"token":"12979f4ebab7cffff52fe395b8909819a2051992789669bee5737706bf724aeccd6418c51006e7443dd322f1d42670b8e5c6769c53ff3312f4e7016c25674dc8a8b41ac296aa956978d33d87706175ac"}

       public static String token = "12979f4ebab7cffff52fe395b8909819a2051992789669bee5737706bf724aeccd6418c51006e7443dd322f1d42670b8e5c6769c53ff3312f4e7016c25674dc8a8b41ac296aa956978d33d87706175ac";

//        String sid = "21641-OXSZWYJ37F";
        public static String sid = "21641";




        /*String user = "cd1bbc73d496df893b31668ff27f82c100ca91aec3d4912364118624c45ba597";
        String pass = "63c6a1d16c038c1a258435d293cfa7f53a9d27f80522175f5921060810d0571c";
        String url = "https://api.haozhuyun.com/sms/?api=login&user="+user+"&pass="+pass;*/

//        String mobile = getMobile(token, sid, "", "", "");
//        System.out.println(mobile);
//        String phone = getValueByNodeName(mobile, "phone");
//        System.out.println(phone);

        /*for (int i = 0; i < 10; i++) {
            String code = getCode(token, sid, "16700950942");
            System.out.println(code);
            Thread.sleep(10000);
        }*/


    public static String getValueByNodeName(String jsonData, String nodeName) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonData);
        return jsonNode.get(nodeName).asText();
    }

    public static String request(String url){

        try {
            URL obj = new URL(url);

            // 设置代理（根据你的代理信息替换 host 和 port）
            //Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890)); // 示例代理
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

            // 设置请求方法
            connection.setRequestMethod("GET");

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

            // 打印响应
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     *
     * @param token
     * @param sid
     * @param paragraph 号码类型，留空为不限制，ascription=1只取虚拟，ascription=2只取实卡
     * @param exclude 排除号段
     * @param ascription 只取号段
     * @return
     */
    public static String getMobile(String token, String sid, int paragraph, String exclude, String ascription){
        String url = "https://api.haozhuyun.com/sms/?api=getPhone&token="+token+"&sid="+sid+"&exclude="+exclude+"&paragraph="+paragraph;
        String request = request(url);
        String phone = "";
        try {
            if (getValueByNodeName(request, "code").equals("0")) {
                phone = getValueByNodeName(request, "phone");
                return phone;
            }else {
                return "获取号码失败";
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getCode(String token, String sid, String phone){
        String url = "https://api.haozhuyun.com/sms/?api=getMessage&token="+token+"&sid="+sid+"&phone="+phone;
        String request = request(url);
        System.out.println(request);
        String code = "wait";
        try {
            if (getValueByNodeName(request, "code").equals("0")) {
                code = getValueByNodeName(request, "yzm");
                return code;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return code;
    }

}
