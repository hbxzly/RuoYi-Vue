package com.ruoyi.browser.client;

import com.ruoyi.account.util.HttpClientUtil;
import com.ruoyi.account.util.JsonDataUtil;
import com.ruoyi.browser.config.HubstudioProperties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class HubstudioClient {

    @Autowired
    private HubstudioProperties properties;



    public String createEnv(Map<String, Object> params) {
        return post("/env/create", params);
    }

    public String listEnv(Map<String, Object> params) {
        return post("/env/list", params);
    }

    public WebDriver openEnv(Long containerCode) {

        Map<String, Object> params = new HashMap<>();
        params.put("containerCode", containerCode);
        params.put("isWebDriverReadOnlyMode", false);
        params.put("isHeadless", false);
        String startResult = post("/browser/start", params);
        System.out.println(startResult);
        String backgroundPluginId = JsonDataUtil.getValueByNodeName(startResult, "data", "backgroundPluginId");
        String webdriver = JsonDataUtil.getValueByNodeName(startResult, "data", "webdriver");
        String debuggingPort = JsonDataUtil.getValueByNodeName(startResult, "data", "debuggingPort");
        //参数配置
        System.setProperty("webdriver.chrome.driver", webdriver);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("debuggerAddress", "127.0.0.1:"+debuggingPort);
        WebDriver webDriver = new ChromeDriver(options);
        webDriver.get("chrome-extension://"+backgroundPluginId+"/index.html");
        return webDriver;
    }

    public void closeEnv(Map<String , Object> params) {
        post("/browser/stop", params);
    }

    public void deleteEnv(Map<String , Object> params) {
        System.out.println(params);
        String post = post("/env/del", params);
        System.out.println(post);
    }

    public String addAccount(Map<String, Object> params) {
        return post("/container/add-account", params);
    }

    private String post(String path, Map<String, Object> params) {
        String url = properties.getBaseUrl() + path;
        return HttpClientUtil.post(url, params);
    }

    public void foreground(Map<String, Object> params) {
        post("/browser/foreground", params);
    }

}

