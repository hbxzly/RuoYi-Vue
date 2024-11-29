package com.ruoyi.account.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * email对象 email
 *
 * @author ruoyi
 * @date 2024-08-16
 */
public class Email extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long keyId;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    /** 账号ID */
    @Excel(name = "账号ID")
    private String accountId;

    /** 状态；0被释放，1正常，2被锁，3未知异常 */
    @Excel(name = "状态；0被释放，1正常，2被锁，3未知异常")
    private String status;

    /** 备注 */
    @Excel(name = "备注")
    private String note;

    /** 最近一次消息 */
    @Excel(name = "最近一次消息")
    private String lastTimeMessage;

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
    public void setAccountId(String accountId)
    {
        this.accountId = accountId;
    }

    public String getAccountId()
    {
        return accountId;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setNote(String note)
    {
        this.note = note;
    }

    public String getNote()
    {
        return note;
    }
    public void setLastTimeMessage(String lastTimeMessage)
    {
        this.lastTimeMessage = lastTimeMessage;
    }

    public String getLastTimeMessage()
    {
        return lastTimeMessage;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("keyId", getKeyId())
                .append("email", getEmail())
                .append("password", getPassword())
                .append("accountId", getAccountId())
                .append("status", getStatus())
                .append("note", getNote())
                .append("lastTimeMessage", getLastTimeMessage())
                .toString();
    }
}
