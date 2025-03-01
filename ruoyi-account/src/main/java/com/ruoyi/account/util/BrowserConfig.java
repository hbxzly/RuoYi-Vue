package com.ruoyi.account.util;

import com.ruoyi.account.domain.CreateInfo;
import com.ruoyi.account.domain.FbAccount;
import com.ruoyi.account.domain.FbAccountForSell;

import java.util.HashMap;
import java.util.Map;

public class BrowserConfig {

    public static Map<String, String> getFbAccountBrowserConfig(FbAccount fbAccount) {
        Map<String, String> map = new HashMap<>();
        map.put("userDataDir",fbAccount.getDataSource() + fbAccount.getBrowserProfile());
        map.put("userAgent",fbAccount.getUa());
        return map;
    }

    public static Map<String, String> getFbAccountForSellBrowserConfig(FbAccountForSell fbAccountForSell) {
        Map<String, String> map = new HashMap<>();
        map.put("userDataDir",fbAccountForSell.getFilePath() + fbAccountForSell.getBrowserProfile());
        map.put("userAgent",fbAccountForSell.getUa());
        return map;
    }

    public static Map<String, String> getCreateInfoBrowserConfig(CreateInfo createInfo) {
        Map<String, String> map = new HashMap<>();
        map.put("userDataDir",createInfo.getFilePath() + createInfo.getBrowserProfile());
        map.put("userAgent",createInfo.getUa());
        return map;
    }

    public static Map<String, String> getTempBrowserConfig() {
        return new HashMap<>();
    }


}
