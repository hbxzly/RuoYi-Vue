package com.ruoyi.account.domain;

import java.time.LocalDateTime;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 代理对象 proxy_ip
 *
 * @author ruoyi
 * @date 2025-02-28
 */
public class ProxyIp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    private Long keyId;

    /** 主机地址 */
    @Excel(name = "主机地址")
    private String hostname;

    /** 端口 */
    @Excel(name = "端口")
    private String port;

    /** 用户名 */
    @Excel(name = "用户名")
    private String username;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 备注 */
    @Excel(name = "备注")
    private String note;

    /** 导入时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "导入时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime importTime;

    public void setKeyId(Long keyId)
    {
        this.keyId = keyId;
    }

    public Long getKeyId()
    {
        return keyId;
    }
    public void setHostname(String hostname)
    {
        this.hostname = hostname;
    }

    public String getHostname()
    {
        return hostname;
    }
    public void setPort(String port)
    {
        this.port = port;
    }

    public String getPort()
    {
        return port;
    }
    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getUsername()
    {
        return username;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPassword()
    {
        return password;
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

    public LocalDateTime getImportTime() {
        return importTime;
    }

    public void setImportTime(LocalDateTime importTime) {
        this.importTime = importTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("keyId", getKeyId())
                .append("hostname", getHostname())
                .append("port", getPort())
                .append("username", getUsername())
                .append("password", getPassword())
                .append("status", getStatus())
                .append("note", getNote())
                .append("importTime", getImportTime())
                .toString();
    }
}
