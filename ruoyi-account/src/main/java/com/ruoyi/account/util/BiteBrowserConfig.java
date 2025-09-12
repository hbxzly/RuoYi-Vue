package com.ruoyi.account.util;

import java.util.HashMap;
import java.util.Map;

public class BiteBrowserConfig {

    /**
     * 创建、更新修改窗口
     * @return
     */
    public static Map<String, Object> createAndUpdateBrowse() {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("id", null);
        jsonMap.put("proxyMethod", 2);//代理方式，2 自定义，3 提取 IP，默认 2。注意：设置提取 IP 时，需要同时设置下方 dynamicIpUrl 等几个字段值
        jsonMap.put("host", "");
        jsonMap.put("port", "");
        jsonMap.put("proxyUserName", "");
        jsonMap.put("proxyPassword", "");
        // 创建 browserFingerPrint 子 Map
        Map<String, Object> browserFingerPrint = new HashMap<>();
        browserFingerPrint.put("isIpCreateLanguage", false);
        browserFingerPrint.put("languages", "en-US");
        // 把子 Map 放入主 Map
        jsonMap.put("browserFingerPrint", browserFingerPrint);

        return jsonMap;
    }

    /**
     * 打开浏览器
     * @return
     */
    public static Map<String, Object> openBrowse() {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("id", null);
        return jsonMap;
    }

    /**
     * 关闭浏览器
     * @return
     */
    public static Map<String, Object> closeBrowse() {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("id", null);
        return jsonMap;
    }
}
