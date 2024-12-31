package com.ruoyi.account.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 账号对象 fb_account
 *
 * @author ruoyi
 * @date 2024-12-12
 */
public class FbAccount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    private Long keyId;

    /** ID */
    @Excel(name = "ID")
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

    /** 名字 */
    @Excel(name = "名字")
    private String name;

    /** 秘钥 */
    @Excel(name = "秘钥")
    private String secretKey;

    /** 状态0：停用；1：正常；2：被锁；3：停用申诉 */
    @Excel(name = "状态0：停用；1：正常；2：被锁；3：停用申诉")
    private String status;

    /** 创建日期 */
    @Excel(name = "创建日期")
    private String createDate;

    /** 备注 */
    @Excel(name = "备注")
    private String note;

    /** UA */
    @Excel(name = "UA")
    private String ua;

    /** 文件路径 */
    @Excel(name = "文件路径")
    private String dataSource;

    /** 地区 */
    @Excel(name = "地区")
    private String region;

    /** 浏览器0,关闭；1,打开 */
    @Excel(name = "浏览器0,关闭；1,打开")
    private String browserStatus;

    /** 文件名 */
    @Excel(name = "文件名")
    private String browserProfile;

    /** 好友数量 */
    @Excel(name = "好友数量")
    private Long friendNumber;

    /** 能否广告 */
    @Excel(name = "能否广告")
    private String canAds;

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
    public void setFriendNumber(Long friendNumber)
    {
        this.friendNumber = friendNumber;
    }

    public Long getFriendNumber()
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
                .append("note", getNote())
                .append("ua", getUa())
                .append("dataSource", getDataSource())
                .append("region", getRegion())
                .append("browserStatus", getBrowserStatus())
                .append("browserProfile", getBrowserProfile())
                .append("friendNumber", getFriendNumber())
                .append("canAds", getCanAds())
                .toString();
    }
}
