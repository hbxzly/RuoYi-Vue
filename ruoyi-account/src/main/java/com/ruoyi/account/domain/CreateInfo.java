package com.ruoyi.account.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 创建信息对象 create_info
 *
 * @author ruoyi
 * @date 2025-06-02
 */
public class CreateInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
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

    /** 手机 */
    @Excel(name = "手机")
    private String phone;

    /** 秘钥 */
    @Excel(name = "秘钥")
    private String secretKey;

    /** ID */
    @Excel(name = "ID")
    private String id;

    /** 账号名称 */
    @Excel(name = "账号名称")
    private String nickName;

    /** 生日 */
    @Excel(name = "生日")
    private String birthday;

    /** 是否商城号 */
    @Excel(name = "是否商城号")
    private String isMarketplace;

    /** 性别 */
    @Excel(name = "性别")
    private String gender;

    /** 生产日期 */
    @Excel(name = "生产日期")
    private String createDate;

    /** 地区 */
    @Excel(name = "地区")
    private String createIp;

    /** 创建状态 */
    @Excel(name = "创建状态")
    private String createStatus;

    /** UA */
    @Excel(name = "UA")
    private String ua;

    /** 路径 */
    @Excel(name = "路径")
    private String filePath;

    /** 浏览器文件夹名称 */
    @Excel(name = "浏览器文件夹名称")
    private String browserProfile;

    /** 公司名称 */
    @Excel(name = "公司名称")
    private String companyName;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 家乡地址 */
    @Excel(name = "家乡地址")
    private String hometownAddress;

    /** 账号其他信息 */
    @Excel(name = "账号其他信息")
    private String accountOtherInfo;

    /** 备注 */
    @Excel(name = "备注")
    private String note;

    /** cookie */
    @Excel(name = "cookie")
    private String cookie;

    /** 绑定邮箱 */
    @Excel(name = "绑定邮箱")
    private String isBoundEmail;

    /** 是否双重 */
    @Excel(name = "是否双重")
    private String is2fa;

    /** 绑定电话 */
    @Excel(name = "绑定电话")
    private String isBoundTelephone;

    /** 是否创建 */
    @Excel(name = "是否创建")
    private String isCreate;

    public void setKeyId(Long keyId)
    {
        this.keyId = keyId;
    }

    public Long getKeyId()
    {
        return keyId;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPassword()
    {
        return password;
    }
    public void setEmailPassword(String emailPassword)
    {
        this.emailPassword = emailPassword;
    }

    public String getEmailPassword()
    {
        return emailPassword;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPhone()
    {
        return phone;
    }
    public void setSecretKey(String secretKey)
    {
        this.secretKey = secretKey;
    }

    public String getSecretKey()
    {
        return secretKey;
    }
    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getNickName()
    {
        return nickName;
    }
    public void setBirthday(String birthday)
    {
        this.birthday = birthday;
    }

    public String getBirthday()
    {
        return birthday;
    }
    public void setIsMarketplace(String isMarketplace)
    {
        this.isMarketplace = isMarketplace;
    }

    public String getIsMarketplace()
    {
        return isMarketplace;
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
    public void setCreateIp(String createIp)
    {
        this.createIp = createIp;
    }

    public String getCreateIp()
    {
        return createIp;
    }
    public void setCreateStatus(String createStatus)
    {
        this.createStatus = createStatus;
    }

    public String getCreateStatus()
    {
        return createStatus;
    }
    public void setUa(String ua)
    {
        this.ua = ua;
    }

    public String getUa()
    {
        return ua;
    }
    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    public String getFilePath()
    {
        return filePath;
    }
    public void setBrowserProfile(String browserProfile)
    {
        this.browserProfile = browserProfile;
    }

    public String getBrowserProfile()
    {
        return browserProfile;
    }
    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public String getCompanyName()
    {
        return companyName;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }
    public void setHometownAddress(String hometownAddress)
    {
        this.hometownAddress = hometownAddress;
    }

    public String getHometownAddress()
    {
        return hometownAddress;
    }
    public void setAccountOtherInfo(String accountOtherInfo)
    {
        this.accountOtherInfo = accountOtherInfo;
    }

    public String getAccountOtherInfo()
    {
        return accountOtherInfo;
    }
    public void setNote(String note)
    {
        this.note = note;
    }

    public String getNote()
    {
        return note;
    }
    public void setCookie(String cookie)
    {
        this.cookie = cookie;
    }

    public String getCookie()
    {
        return cookie;
    }
    public void setIsBoundEmail(String isBoundEmail)
    {
        this.isBoundEmail = isBoundEmail;
    }

    public String getIsBoundEmail()
    {
        return isBoundEmail;
    }
    public void setIs2fa(String is2fa)
    {
        this.is2fa = is2fa;
    }

    public String getIs2fa()
    {
        return is2fa;
    }
    public void setIsBoundTelephone(String isBoundTelephone)
    {
        this.isBoundTelephone = isBoundTelephone;
    }

    public String getIsBoundTelephone()
    {
        return isBoundTelephone;
    }
    public void setIsCreate(String isCreate)
    {
        this.isCreate = isCreate;
    }

    public String getIsCreate()
    {
        return isCreate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("keyId", getKeyId())
                .append("email", getEmail())
                .append("password", getPassword())
                .append("emailPassword", getEmailPassword())
                .append("phone", getPhone())
                .append("secretKey", getSecretKey())
                .append("id", getId())
                .append("nickName", getNickName())
                .append("birthday", getBirthday())
                .append("isMarketplace", getIsMarketplace())
                .append("gender", getGender())
                .append("createDate", getCreateDate())
                .append("createIp", getCreateIp())
                .append("createStatus", getCreateStatus())
                .append("ua", getUa())
                .append("filePath", getFilePath())
                .append("browserProfile", getBrowserProfile())
                .append("companyName", getCompanyName())
                .append("address", getAddress())
                .append("hometownAddress", getHometownAddress())
                .append("accountOtherInfo", getAccountOtherInfo())
                .append("note", getNote())
                .append("cookie", getCookie())
                .append("isBoundEmail", getIsBoundEmail())
                .append("is2fa", getIs2fa())
                .append("isBoundTelephone", getIsBoundTelephone())
                .append("isCreate", getIsCreate())
                .toString();
    }
}
