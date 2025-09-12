package com.ruoyi.account.domain.dto;

import com.ruoyi.common.core.domain.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.List;

public class FbAccountForSellQueryDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 邮箱模糊查 */
    private String email;

    /** ID模糊查 */
    private String id;

    /** 地区多选 */
    private String region;

    /** 是否卖出 */
    private String isSell;

    /** 卖出日期范围 */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate sellDateStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate sellDateEnd;

    /** 好友数量范围 */
    private Integer friendNumberMin;
    private Integer friendNumberMax;

    /** 主页数量范围 */
    private Integer pageNumberMin;
    private Integer pageNumberMax;

    /** BM数量范围 */
    private Integer bmNumberMin;
    private Integer bmNumberMax;

    /** 帖子数量范围 */
    private Integer postsNumberMin;
    private Integer postsNumberMax;

    /** 是否上架 */
    private String isShelf;

    /** 是否广告 */
    private String canAds;

    /** 是否商城号 */
    private String isMarketplace;

    /** 性别多选 */
    private String gender;

    /** 邮箱状态多选 */
    private List<String> emailStatusList;

    /** 是否可登录多选 */
    private String canLogin;

    /** 创建时间范围（字符串） */
    private String createTimeBegin;
    private String createTimeEnd;

    /** 最近发帖时间范围（字符串） */
    private String lastPostsTimeBegin;
    private String lastPostsTimeEnd;

    /** 名字模糊 */
    private String name;

    /** 浏览器状态多选 */
    private String browserStatus;

    /** 备注模糊 */
    private String note;

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

    public String getIsSell() {
        return isSell;
    }

    public void setIsSell(String isSell) {
        this.isSell = isSell;
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

    public Integer getPostsNumberMin() {
        return postsNumberMin;
    }

    public void setPostsNumberMin(Integer postsNumberMin) {
        this.postsNumberMin = postsNumberMin;
    }

    public Integer getPostsNumberMax() {
        return postsNumberMax;
    }

    public void setPostsNumberMax(Integer postsNumberMax) {
        this.postsNumberMax = postsNumberMax;
    }

    public String getIsShelf() {
        return isShelf;
    }

    public void setIsShelf(String isShelf) {
        this.isShelf = isShelf;
    }

    public String getCanAds() {
        return canAds;
    }

    public void setCanAds(String canAds) {
        this.canAds = canAds;
    }

    public String getIsMarketplace() {
        return isMarketplace;
    }

    public void setIsMarketplace(String isMarketplace) {
        this.isMarketplace = isMarketplace;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<String> getEmailStatusList() {
        return emailStatusList;
    }

    public void setEmailStatusList(List<String> emailStatusList) {
        this.emailStatusList = emailStatusList;
    }

    public String getCanLogin() {
        return canLogin;
    }

    public void setCanLogin(String canLogin) {
        this.canLogin = canLogin;
    }

    public String getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(String createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public String getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public String getLastPostsTimeBegin() {
        return lastPostsTimeBegin;
    }

    public void setLastPostsTimeBegin(String lastPostsTimeBegin) {
        this.lastPostsTimeBegin = lastPostsTimeBegin;
    }

    public String getLastPostsTimeEnd() {
        return lastPostsTimeEnd;
    }

    public void setLastPostsTimeEnd(String lastPostsTimeEnd) {
        this.lastPostsTimeEnd = lastPostsTimeEnd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrowserStatus() {
        return browserStatus;
    }

    public void setBrowserStatus(String browserStatus) {
        this.browserStatus = browserStatus;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
