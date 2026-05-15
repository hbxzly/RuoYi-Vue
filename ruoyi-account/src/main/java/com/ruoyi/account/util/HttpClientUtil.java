package com.ruoyi.account.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpClientUtil {

    private static final int CONNECT_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 30000;
    private static final ObjectMapper mapper = new ObjectMapper();

    private HttpClientUtil() {}

    /* ======================= GET ======================= */

    public static String get(String url) {
        try {
            HttpURLConnection con = openConnection(url, "GET");
            return readResponse(con);
        } catch (Exception e) {
            throw new RuntimeException("HTTP GET failed: " + url, e);
        }
    }

    /* ======================= POST JSON ======================= */

    public static String post(String url, Object body) {
        try {
            HttpURLConnection con = openConnection(url, "POST");
            con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            con.setDoOutput(true);

            // 自动处理 Map -> JSON
            String json;
            if (body instanceof String) {
                json = (String) body;
            } else if (body instanceof Map) {
                json = mapper.writeValueAsString(body);
            } else {
                throw new IllegalArgumentException("Unsupported body type: " + body.getClass());
            }

            try (OutputStream os = con.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            return readResponse(con);
        } catch (Exception e) {
            throw new RuntimeException("HTTP POST JSON failed: " + url, e);
        }
    }

    /* ======================= 基础方法 ======================= */

    private static HttpURLConnection openConnection(String url, String method) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(method);
        con.setConnectTimeout(CONNECT_TIMEOUT);
        con.setReadTimeout(READ_TIMEOUT);
        con.setRequestProperty("Accept-Charset", "UTF-8");
        return con;
    }

    private static String readResponse(HttpURLConnection con) throws Exception {
        int code = con.getResponseCode();
        InputStream is = code >= 200 && code < 300
                ? con.getInputStream()
                : con.getErrorStream();

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }
}

