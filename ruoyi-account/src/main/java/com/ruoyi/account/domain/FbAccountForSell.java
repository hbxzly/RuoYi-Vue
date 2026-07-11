package com.ruoyi.account.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
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
public class FbAccountForSell extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    private Long keyId;

    /** 邮箱 */
    @Excel(name = "邮箱（必须）")
    private String email;

    /** 密码 */
    @Excel(name = "密码（必须）")
    private String password;

    /** 邮箱密码 */
    @Excel(name = "邮箱密码（必须）")
    private String emailPassword;

    /** ID */
    @Excel(name = "ID（必须）")
    private String id;

    /** 账户生日 */
    @Excel(name = "账户生日")
    private String birthday;

    /** 秘钥 */
    @Excel(name = "秘钥（必须）")
    private String secretKey;

    /** 是否商城号 */
    @Excel(name = "是否商城号")
    private String isMarketplace;

    /** 名字 */
    @Excel(name = "名字")
    private String name;

    /** 性别 */
    @Excel(name = "性别")
    private String gender;

    /** 创建日期 */
    @Excel(name = "创建日期")
    private String createDate;

    /** 地区 */
    @Excel(name = "地区（必须）")
    private String region;

    /** 备注 */
    @Excel(name = "备注")
    private String note;

    /** 能否登录 */
    @Excel(name = "能否登录")
    private String canLogin;

    /** 好友数量 */
    @Excel(name = "好友数量")
    private String friendNumber;

    /** 好友数量范围 */
    private String friendNumberMin;
    private String friendNumberMax;

    /** 能否广告 */
    @Excel(name = "能否广告")
    private String canAds;

    /** 个人户状态 */
    @Excel(name = "个人户状态")
    private String adAccountStatus;

    /** 主页数量 */
    @Excel(name = "主页数量")
    private String pageNumber;

    /** 主页数量范围 */
    private String pageNumberMin;
    private String pageNumberMax;

    /** bm数量 */
    @Excel(name = "BM数量")
    private String bmNumber;

    /** BM数量范围 */
    private String bmNumberMin;
    private String bmNumberMax;

    /** 帖子数量 */
    @Excel(name = "帖子数量")
    private String postsNumber;

    /** 帖子数量范围 */
    private String postsNumberMin;
    private String postsNumberMax;

    /** UA */
    private String ua;

    /** 浏览器状态 */
    private String browserStatus;

    /** 浏览器文件 */
    @Excel(name = "浏览器文件")
    private String browserProfile;

    /** 文件路径 */
    @Excel(name = "文件路径（必须）")
    private String filePath;

    /** 最近发帖时间 */
    @Excel(name = "最近发帖时间")
    private String lastPostsTime;

    /** 是否上架 */
    @Excel(name = "是否上架")
    private String isShelf;

    /** 邮箱状态 */
    @Excel(name = "邮箱状态（必须）")
    private String emailStatus;

    /** 是否卖出 */
    @Excel(name = "是否卖出（必须）")
    private String isSell;

    /** 卖出日期 */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate sellDate;

    @Excel(name = "cookie")
    private String cookie;


    // ========== 非持久化字段：批量搜索用 ==========
    private String searchType;
    private List<String> values;

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }


    public void setKeyId(Long keyId)
    {
        this.keyId = keyId;
    }

    public Long getKeyId()
    {
        return keyId;
    }
    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPassword()
    {
        return password;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }
    public void setEmailPassword(String emailPassword)
    {
        this.emailPassword = emailPassword;
    }

    public String getEmailPassword()
    {
        return emailPassword;
    }
    public void setBirthday(String birthday)
    {
        this.birthday = birthday;
    }

    public String getBirthday()
    {
        return birthday;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setSecretKey(String secretKey)
    {
        this.secretKey = secretKey;
    }

    public String getSecretKey()
    {
        return secretKey;
    }
    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getGender()
    {
        return gender;
    }
    public void setCreateDate(String createDate)
    {
        this.createDate = createDate;
    }

    public String getCreateDate()
    {
        return createDate;
    }
    public void setNote(String note)
    {
        this.note = note;
    }

    public String getNote()
    {
        return note;
    }
    public void setRegion(String region)
    {
        this.region = region;
    }

    public String getRegion()
    {
        return region;
    }
    public void setCanLogin(String canLogin)
    {
        this.canLogin = canLogin;
    }

    public String getCanLogin()
    {
        return canLogin;
    }
    public void setIsMarketplace(String isMarketplace)
    {
        this.isMarketplace = isMarketplace;
    }

    public String getIsMarketplace()
    {
        return isMarketplace;
    }
    public void setFriendNumber(String friendNumber)
    {
        this.friendNumber = friendNumber;
    }

    public String getFriendNumber()
    {
        return friendNumber;
    }
    public void setFriendNumberMin(String friendNumberMin)
    {
        this.friendNumberMin = friendNumberMin;
    }

    public String getFriendNumberMin()
    {
        return friendNumberMin;
    }
    public void setFriendNumberMax(String friendNumberMax)
    {
        this.friendNumberMax = friendNumberMax;
    }

    public String getFriendNumberMax()
    {
        return friendNumberMax;
    }
    public void setCanAds(String canAds)
    {
        this.canAds = canAds;
    }

    public String getCanAds()
    {
        return canAds;
    }
    public void setPageNumber(String pageNumber)
    {
        this.pageNumber = pageNumber;
    }

    public String getPageNumber()
    {
        return pageNumber;
    }
    public void setPageNumberMin(String pageNumberMin)
    {
        this.pageNumberMin = pageNumberMin;
    }

    public String getPageNumberMin()
    {
        return pageNumberMin;
    }
    public void setPageNumberMax(String pageNumberMax)
    {
        this.pageNumberMax = pageNumberMax;
    }

    public String getPageNumberMax()
    {
        return pageNumberMax;
    }
    public void setBmNumber(String bmNumber)
    {
        this.bmNumber = bmNumber;
    }

    public String getBmNumber()
    {
        return bmNumber;
    }
    public void setBmNumberMin(String bmNumberMin)
    {
        this.bmNumberMin = bmNumberMin;
    }

    public String getBmNumberMin()
    {
        return bmNumberMin;
    }
    public void setBmNumberMax(String bmNumberMax)
    {
        this.bmNumberMax = bmNumberMax;
    }

    public String getBmNumberMax()
    {
        return bmNumberMax;
    }
    public void setPostsNumber(String postsNumber)
    {
        this.postsNumber = postsNumber;
    }

    public String getPostsNumber()
    {
        return postsNumber;
    }
    public void setPostsNumberMin(String postsNumberMin)
    {
        this.postsNumberMin = postsNumberMin;
    }

    public String getPostsNumberMin()
    {
        return postsNumberMin;
    }
    public void setPostsNumberMax(String postsNumberMax)
    {
        this.postsNumberMax = postsNumberMax;
    }

    public String getPostsNumberMax()
    {
        return postsNumberMax;
    }
    public void setUa(String ua)
    {
        this.ua = ua;
    }

    public String getUa()
    {
        return ua;
    }
    public void setBrowserStatus(String browserStatus)
    {
        this.browserStatus = browserStatus;
    }

    public String getBrowserStatus()
    {
        return browserStatus;
    }
    public void setBrowserProfile(String browserProfile)
    {
        this.browserProfile = browserProfile;
    }

    public String getBrowserProfile()
    {
        return browserProfile;
    }
    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    public String getFilePath()
    {
        return filePath;
    }

    public void setLastPostsTime(String lastPostsTime)
    {
        this.lastPostsTime = lastPostsTime;
    }

    public String getLastPostsTime()
    {
        return lastPostsTime;
    }

    public void setIsShelf(String isShelf) {
        this.isShelf = isShelf;
    }

    public String getIsShelf() {
        return isShelf;
    }

    public void setEmailStatus(String emailStatus) {
        this.emailStatus = emailStatus;
    }

    public String getEmailStatus() {
        return emailStatus;
    }

    public String getIsSell() {
        return isSell;
    }

    public void setIsSell(String isSell) {
        this.isSell = isSell;
    }

    public LocalDate getSellDate() {
        return sellDate;
    }

    public void setSellDate(LocalDate sellDate) {
        this.sellDate = sellDate;
    }

    public String getAdAccountStatus() {
        return adAccountStatus;
    }

    public void setAdAccountStatus(String adAccountStatus) {
        this.adAccountStatus = adAccountStatus;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("keyId", getKeyId())
                .append("id", getId())
                .append("password", getPassword())
                .append("email", getEmail())
                .append("emailPassword", getEmailPassword())
                .append("birthday", getBirthday())
                .append("name", getName())
                .append("secretKey", getSecretKey())
                .append("gender", getGender())
                .append("createDate", getCreateDate())
                .append("note", getNote())
                .append("region", getRegion())
                .append("canLogin", getCanLogin())
                .append("adAccountStatus", getAdAccountStatus())
                .append("isMarketplace", getIsMarketplace())
                .append("emailStatus", getEmailStatus())
                .append("friendNumber", getFriendNumber())
                .append("friendNumberMin", getFriendNumberMin())
                .append("friendNumberMax", getFriendNumberMax())
                .append("canAds", getCanAds())
                .append("pageNumber", getPageNumber())
                .append("pageNumberMin", getPageNumberMin())
                .append("pageNumberMax", getPageNumberMax())
                .append("bmNumber", getBmNumber())
                .append("bmNumberMin", getBmNumberMin())
                .append("bmNumberMax", getBmNumberMax())
                .append("postsNumber", getPostsNumber())
                .append("postsNumberMin", getPostsNumberMin())
                .append("postsNumberMax", getPostsNumberMax())
                .append("ua", getUa())
                .append("browserStatus", getBrowserStatus())
                .append("browserProfile", getBrowserProfile())
                .append("filePath", getFilePath())
                .append("lastPostsTime", getLastPostsTime())
                .append("isShelf", getIsShelf())
                .append("isSell", getIsSell())
                .append("sellDate", getSellDate())
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FbAccountForSell that = (FbAccountForSell) obj;
        return Objects.equals(id, that.id); // 假设使用 id 作为唯一标识
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
