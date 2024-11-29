package com.ruoyi.account.domain;


import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class FbAccount extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Integer keyId;

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

    /** 0,被封；1,正常；2,受限；3,永久受限；4,被锁；5,停用；6,受限申诉；7，停用申诉； */
    @Excel(name = "0,被封；1,正常；2,受限；3,永久受限；4,被锁；5,停用；6,受限申诉；7，停用申诉；")
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

    /** 数据源 */
    @Excel(name = "数据源")
    private String dataSource;

    /** 地区 */
    @Excel(name = "地区")
    private String region;

    /** 0,关闭；1,打开 */
    @Excel(name = "0,关闭；1,打开")
    private String browserStatus;

    /** 浏览器文件 */
    @Excel(name = "浏览器文件")
    private String browserProfile;

    public Integer getKeyId() {
        return keyId;
    }

    public void setKeyId(Integer keyId) {
        this.keyId = keyId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword == null ? null : emailPassword.trim();
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday == null ? null : birthday.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey == null ? null : secretKey.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getBrowserStatus() {
        return browserStatus;
    }

    public void setBrowserStatus(String browserStatus) {
        this.browserStatus = browserStatus;
    }

    public String getBrowserProfile() {
        return browserProfile;
    }

    public void setBrowserProfile(String browserProfile) {
        this.browserProfile = browserProfile;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
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
                .toString();
    }
}
