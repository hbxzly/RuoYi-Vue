package com.ruoyi.browser.builder;

import com.ruoyi.account.domain.FbAccountForSell;
import com.ruoyi.account.domain.ProxyIp;

import java.util.*;

public class HubEnvParamBuilder {

    public static Map<String, Object> buildCreateFbEnvParam(ProxyIp proxyIp) {
        Map<String, Object> envParam = new HashMap<>();
        Map<String, Object> advancedBo = new HashMap<>();
        advancedBo.put("languageType", 1);
        advancedBo.put("languages", Arrays.asList("zh-TW"));
        if (proxyIp == null) {
            envParam.put("containerName", "FB账号");
            envParam.put("asDynamicType", "0");
            envParam.put("proxyTypeName", "不使用代理");
            envParam.put("advancedBo", advancedBo);
        } else {
            envParam.put("containerName", "FB账号");
            envParam.put("asDynamicType", "0");
            envParam.put("proxyTypeName", "不使用代理");
//            envParam.put("proxyHost", proxyIp.getHostname());
//            envParam.put("proxyPort", proxyIp.getPort());
//            envParam.put("proxyUsername", proxyIp.getUsername());
//            envParam.put("proxyPassword", proxyIp.getPassword());
            envParam.put("advancedBo", advancedBo);
        }
        return envParam;
    }

    public static Map<String, Object> buildCreateUnlockEmailEnvParam(ProxyIp proxyIp) {
        Map<String, Object> envParam = new HashMap<>();
        Map<String, Object> advancedBo = new HashMap<>();
        advancedBo.put("languageType", 1);
        advancedBo.put("uiLanguage", "en");
        advancedBo.put("languages", Arrays.asList("en-US"));
        if (proxyIp == null) {
            envParam.put("containerName", "解锁邮箱");
            envParam.put("asDynamicType", "0");
            envParam.put("proxyTypeName", "不使用代理");
            envParam.put("advancedBo", advancedBo);
        } else {
            envParam.put("containerName", "解锁邮箱");
            envParam.put("asDynamicType", "0");
            envParam.put("proxyTypeName", "Socks5");
            envParam.put("proxyServer", proxyIp.getHostname());
            envParam.put("proxyPort", proxyIp.getPort());
            envParam.put("proxyAccount", proxyIp.getUsername());
            envParam.put("proxyPassword", proxyIp.getPassword());
            envParam.put("advancedBo", advancedBo);
        }
        return envParam;
    }

    public static Map<String, Object> builderAddAccountParam(FbAccountForSell account,Long containerCode) {
        Map<String, Object> addAccountParam = new HashMap<>();
        addAccountParam.put("accountName", account.getId());
        addAccountParam.put("accountPassword", account.getPassword());
        addAccountParam.put("containerCode", containerCode);
        addAccountParam.put("name", account.getId());
        addAccountParam.put("otpSecret", account.getSecretKey().replaceAll(" ",""));
        addAccountParam.put("siteName", "Facebook");
        return addAccountParam;
    }

    public static Map<String, Object> builderEnvListParam(List containerCodeList) {
        Map<String, Object> envListParam = new HashMap<>();
        envListParam.put("containerCode", containerCodeList);
        return envListParam;
    }

    public static Map<String, Object> builderCloseEnvParam(Long containerCode) {
        Map<String, Object> CloseEnvParam = new HashMap<>();
        CloseEnvParam.put("containerCode", containerCode);
        return CloseEnvParam;
    }

    public static Map<String, Object> builderDeleteEnvParam(List<Long> containerCodeList) {
        Map<String, Object> DeleteEnvParam = new HashMap<>();
        DeleteEnvParam.put("containerCodes", containerCodeList);
        return DeleteEnvParam;
    }

    public static Map<String, Object> builderOpenEnvParam(Long containerCode) {
        Map<String, Object> openEnvParam = new HashMap<>();
        openEnvParam.put("containerCode", containerCode);
        openEnvParam.put("isWebDriverReadOnlyMode", false);
        openEnvParam.put("isHeadless", false);
        return openEnvParam;
    }

    public static Map<String, Object> builderForegroundParam(Long containerCode) {
        Map<String, Object> ForegroundParam = new HashMap<>();
        ForegroundParam.put("containerCode", containerCode);
        return ForegroundParam;
    }
}
