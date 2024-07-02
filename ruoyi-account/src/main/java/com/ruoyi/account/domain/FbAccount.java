package com.ruoyi.account.domain;


import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class FbAccount extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Integer keyId;

    private String id;

    private String password;

    private String email;

    private String emailPassword;

    private String birthday;

    private String name;

    private String secretKey;

    private String status;

    private String createDate;

    private String note;

    private String ua;

    private String dataSource;

    private String region;

    private String browserStatus;

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
