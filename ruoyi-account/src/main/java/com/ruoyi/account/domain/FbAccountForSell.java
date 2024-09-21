package com.ruoyi.account.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 卖号对象 fb_account_for_sell
 * 
 * @author ruoyi
 * @date 2024-09-20
 */
public class FbAccountForSell extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    private Long keyId;

    /** ID */
    private String id;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 邮箱密码 */
    @Excel(name = "邮箱密码")
    private String emailPassword;

    /** 生日 */
    @Excel(name = "生日")
    private String birthday;

    /** 账号名 */
    @Excel(name = "账号名")
    private String name;

    /** 秘钥 */
    @Excel(name = "秘钥")
    private String secretKey;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 创建日期 */
    @Excel(name = "创建日期")
    private String createDate;

    /** 好友数量 */
    @Excel(name = "好友数量")
    private String friendNumber;

    /** 备注 */
    @Excel(name = "备注")
    private String note;

    /**  */
    @Excel(name = "")
    private String ua;

    /**  */
    @Excel(name = "")
    private String dataSource;

    /** 账号地区 */
    @Excel(name = "账号地区")
    private String region;

    /** 浏览器状态 */
    @Excel(name = "浏览器状态")
    private String browserStatus;

    /**  */
    @Excel(name = "")
    private String browserProfile;

    /** 出售时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出售时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sellTime;

    /** 客户 */
    @Excel(name = "客户")
    private String client;

    /** 客户来源 */
    @Excel(name = "客户来源")
    private String clientSource;

    /** 商城 */
    @Excel(name = "商城")
    private String isMarketplace;

    /** 上次登录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "上次登录时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastLoginTime;

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
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setCreateDate(String createDate) 
    {
        this.createDate = createDate;
    }

    public String getCreateDate() 
    {
        return createDate;
    }
    public void setFriendNumber(String friendNumber) 
    {
        this.friendNumber = friendNumber;
    }

    public String getFriendNumber() 
    {
        return friendNumber;
    }
    public void setNote(String note) 
    {
        this.note = note;
    }

    public String getNote() 
    {
        return note;
    }
    public void setUa(String ua) 
    {
        this.ua = ua;
    }

    public String getUa() 
    {
        return ua;
    }
    public void setDataSource(String dataSource) 
    {
        this.dataSource = dataSource;
    }

    public String getDataSource() 
    {
        return dataSource;
    }
    public void setRegion(String region) 
    {
        this.region = region;
    }

    public String getRegion() 
    {
        return region;
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
    public void setSellTime(Date sellTime) 
    {
        this.sellTime = sellTime;
    }

    public Date getSellTime() 
    {
        return sellTime;
    }
    public void setClient(String client) 
    {
        this.client = client;
    }

    public String getClient() 
    {
        return client;
    }
    public void setClientSource(String clientSource) 
    {
        this.clientSource = clientSource;
    }

    public String getClientSource() 
    {
        return clientSource;
    }
    public void setIsMarketplace(String isMarketplace) 
    {
        this.isMarketplace = isMarketplace;
    }

    public String getIsMarketplace() 
    {
        return isMarketplace;
    }
    public void setLastLoginTime(Date lastLoginTime) 
    {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getLastLoginTime() 
    {
        return lastLoginTime;
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
            .append("status", getStatus())
            .append("createDate", getCreateDate())
            .append("friendNumber", getFriendNumber())
            .append("note", getNote())
            .append("ua", getUa())
            .append("dataSource", getDataSource())
            .append("region", getRegion())
            .append("browserStatus", getBrowserStatus())
            .append("browserProfile", getBrowserProfile())
            .append("sellTime", getSellTime())
            .append("client", getClient())
            .append("clientSource", getClientSource())
            .append("isMarketplace", getIsMarketplace())
            .append("lastLoginTime", getLastLoginTime())
            .toString();
    }
}
