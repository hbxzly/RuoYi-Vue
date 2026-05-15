package com.ruoyi.account.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * 卖号对象 fb_account_for_sell
 *
 * @author ruoyi
 * @date 2024-11-01
 */
public class FbAccountForSellQuery extends BaseEntity {

    /** 主键 */
    private Long keyId;

    /** 邮箱 */
    private String email;

    /** ID */
    private String id;

    /** 地区 */
    private String region;

    /** 是否商城号 */
    private String isMarketplace;

    /** 是否可登录 */
    private String canLogin;

    /** 是否可投广告 */
    private String canAds;

    /** 是否上架 */
    private String isShelf;

    /** 是否卖出 */
    private String isSell;

    /** ========== 核心：邮箱状态多选 ========== */
    private List<String> emailStatusList;

    /** 个人户状态 */
    private String adAccountStatus;

    /** 好友数范围 */
    private Integer friendNumberMin;
    private Integer friendNumberMax;

    /** 主页数量范围 */
    private Integer pageNumberMin;
    private Integer pageNumberMax;

    /** BM 数量范围 */
    private Integer bmNumberMin;
    private Integer bmNumberMax;

    /** 创建时间范围 */
    private String createDateStart;
    private String createDateEnd;

    /** 卖出日期范围 */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate sellDateStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate sellDateEnd;

    // ================== getter / setter ==================

    public Long getKeyId() {
        return keyId;
    }

    public void setKeyId(Long keyId) {
        this.keyId = keyId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getIsMarketplace() {
        return isMarketplace;
    }

    public void setIsMarketplace(String isMarketplace) {
        this.isMarketplace = isMarketplace;
    }

    public String getCanLogin() {
        return canLogin;
    }

    public void setCanLogin(String canLogin) {
        this.canLogin = canLogin;
    }

    public String getCanAds() {
        return canAds;
    }

    public void setCanAds(String canAds) {
        this.canAds = canAds;
    }

    public String getIsShelf() {
        return isShelf;
    }

    public void setIsShelf(String isShelf) {
        this.isShelf = isShelf;
    }

    public String getIsSell() {
        return isSell;
    }

    public void setIsSell(String isSell) {
        this.isSell = isSell;
    }

    public List<String> getEmailStatusList() {
        return emailStatusList;
    }

    public void setEmailStatusList(List<String> emailStatusList) {
        this.emailStatusList = emailStatusList;
    }

    public String getAdAccountStatus() {
        return adAccountStatus;
    }

    public void setAdAccountStatus(String adAccountStatus) {
        this.adAccountStatus = adAccountStatus;
    }

    public Integer getFriendNumberMin() {
        return friendNumberMin;
    }

    public void setFriendNumberMin(Integer friendNumberMin) {
        this.friendNumberMin = friendNumberMin;
    }

    public Integer getFriendNumberMax() {
        return friendNumberMax;
    }

    public void setFriendNumberMax(Integer friendNumberMax) {
        this.friendNumberMax = friendNumberMax;
    }

    public Integer getPageNumberMin() {
        return pageNumberMin;
    }

    public void setPageNumberMin(Integer pageNumberMin) {
        this.pageNumberMin = pageNumberMin;
    }

    public Integer getPageNumberMax() {
        return pageNumberMax;
    }

    public void setPageNumberMax(Integer pageNumberMax) {
        this.pageNumberMax = pageNumberMax;
    }

    public Integer getBmNumberMin() {
        return bmNumberMin;
    }

    public void setBmNumberMin(Integer bmNumberMin) {
        this.bmNumberMin = bmNumberMin;
    }

    public Integer getBmNumberMax() {
        return bmNumberMax;
    }

    public void setBmNumberMax(Integer bmNumberMax) {
        this.bmNumberMax = bmNumberMax;
    }

    public String getCreateDateStart() {
        return createDateStart;
    }

    public void setCreateDateStart(String createDateStart) {
        this.createDateStart = createDateStart;
    }

    public String getCreateDateEnd() {
        return createDateEnd;
    }

    public void setCreateDateEnd(String createDateEnd) {
        this.createDateEnd = createDateEnd;
    }

    public LocalDate getSellDateStart() {
        return sellDateStart;
    }

    public void setSellDateStart(LocalDate sellDateStart) {
        this.sellDateStart = sellDateStart;
    }

    public LocalDate getSellDateEnd() {
        return sellDateEnd;
    }

    public void setSellDateEnd(LocalDate sellDateEnd) {
        this.sellDateEnd = sellDateEnd;
    }
}