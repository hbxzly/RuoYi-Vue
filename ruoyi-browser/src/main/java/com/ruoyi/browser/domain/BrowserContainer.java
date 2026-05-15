package com.ruoyi.browser.domain;

import lombok.Data;

import java.util.List;

@Data
public class BrowserContainer {

    private Long containerCode;
    private String containerName;

    private String proxyTypeName;

    private String ua;

    private String lastUsedIp;
    private String lastCountry;
    private String lastRegion;
    private String lastCity;

    private Integer coreVersion;

    private List<BrowserAccount> accounts;

    public boolean hasAccount(String accountName) {
        if (accounts == null) return false;
        return accounts.stream()
                .anyMatch(a -> accountName.equals(a.getAccountName()));
    }
}
