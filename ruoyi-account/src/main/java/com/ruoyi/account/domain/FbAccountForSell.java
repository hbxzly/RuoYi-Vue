package com.ruoyi.account.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

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
    @Excel(name = "邮箱")
    private String email;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    /** 邮箱密码 */
    @Excel(name = "邮箱密码")
    private String emailPassword;

    /** ID */
    @Excel(name = "ID")
    private String id;

    /** 账户生日 */
    @Excel(name = "账户生日")
    private String birthday;

    /** 秘钥 */
    @Excel(name = "秘钥")
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
    @Excel(name = "地区")
    private String region;

    /** 备注 */
    @Excel(name = "备注")
    private String note;

    /** 能否登录 */
    private String canLogin;

    /** 好友数量 */
    private String friendNumber;

    /** 能否广告 */
    private String canAds;

    /** 主页数量 */
    private String pageNumber;

    /** bm数量 */
    private String bmNumber;

    /** 帖子数量 */
    private String postsNumber;

    /** UA */
    private String ua;

    /** 浏览器状态 */
    private String browserStatus;

    /** 浏览器文件 */
    private String browserProfile;

    /** 文件路径 */
    @Excel(name = "文件路径")
    private String filePath;

    private String lastPostsTime;

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
    public void setBmNumber(String bmNumber) 
    {
        this.bmNumber = bmNumber;
    }

    public String getBmNumber() 
    {
        return bmNumber;
    }
    public void setPostsNumber(String postsNumber) 
    {
        this.postsNumber = postsNumber;
    }

    public String getPostsNumber() 
    {
        return postsNumber;
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
                .append("isMarketplace", getIsMarketplace())
                .append("friendNumber", getFriendNumber())
                .append("canAds", getCanAds())
                .append("pageNumber", getPageNumber())
                .append("bmNumber", getBmNumber())
                .append("postsNumber", getPostsNumber())
                .append("ua", getUa())
                .append("browserStatus", getBrowserStatus())
                .append("browserProfile", getBrowserProfile())
                .append("filePath", getFilePath())
                .append("lastPostsTime", getLastPostsTime())
                .toString();
    }
}
