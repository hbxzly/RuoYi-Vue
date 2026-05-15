package com.ruoyi.browser.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * Hubstudio 环境对象 hub_env
 *
 * @author ruoyi
 * @date 2026-02-02
 */
public class HubEnv extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 环境ID（Hubstudio） */
    @Excel(name = "环境ID", readConverterExp = "H=ubstudio")
    private Long containerCode;

    /** 环境序号 */
    @Excel(name = "环境序号")
    private Long serialNumber;

    /** 环境名称 */
    @Excel(name = "环境名称")
    private String containerName;

    /** 账号名称 */
    @Excel(name = "账号名称")
    private String accountName;

    /** 平台名称 */
    @Excel(name = "平台名称")
    private String platformName;

    /** 环境分组名称 */
    @Excel(name = "环境分组名称")
    private String tagName;

    /** 环境分组ID */
    @Excel(name = "环境分组ID")
    private String tagCode;

    /** 代理使用方式：1-静态，2-动态 */
    @Excel(name = "代理使用方式：1-静态，2-动态")
    private Long asDynamicType;

    /** 代理类型（HTTP / Socks5 等） */
    @Excel(name = "代理类型", readConverterExp = "H=TTP,/=,S=ocks5,等=")
    private String proxyTypeName;

    /** 代理主机 */
    @Excel(name = "代理主机")
    private String proxyHost;

    /** 代理端口 */
    @Excel(name = "代理端口")
    private Long proxyPort;

    /** 代理账号 */
    @Excel(name = "代理账号")
    private String proxyAccount;

    /** 代理密码 */
    @Excel(name = "代理密码")
    private String proxyPassword;

    /** 上一次使用的IP */
    @Excel(name = "上一次使用的IP")
    private String lastUsedIp;

    /** 上一次IP国家 */
    @Excel(name = "上一次IP国家")
    private String lastCountry;

    /** 上一次IP省/州 */
    @Excel(name = "上一次IP省/州")
    private String lastRegion;

    /** 上一次IP城市 */
    @Excel(name = "上一次IP城市")
    private String lastCity;

    /** 浏览器UA */
    @Excel(name = "浏览器UA")
    private String ua;

    /** 刷新URL */
    @Excel(name = "刷新URL")
    private String refreshUrl;

    /** 最后打开时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最后打开时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date openTime;

    /** 环境最后打开时间（简化展示） */
    @Excel(name = "环境最后打开时间", readConverterExp = "简=化展示")
    private String allOpenTime;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setContainerCode(Long containerCode)
    {
        this.containerCode = containerCode;
    }

    public Long getContainerCode()
    {
        return containerCode;
    }
    public void setSerialNumber(Long serialNumber)
    {
        this.serialNumber = serialNumber;
    }

    public Long getSerialNumber()
    {
        return serialNumber;
    }
    public void setContainerName(String containerName)
    {
        this.containerName = containerName;
    }

    public String getContainerName()
    {
        return containerName;
    }
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }

    public String getAccountName()
    {
        return accountName;
    }
    public void setPlatformName(String platformName)
    {
        this.platformName = platformName;
    }

    public String getPlatformName()
    {
        return platformName;
    }
    public void setTagName(String tagName)
    {
        this.tagName = tagName;
    }

    public String getTagName()
    {
        return tagName;
    }
    public void setTagCode(String tagCode)
    {
        this.tagCode = tagCode;
    }

    public String getTagCode()
    {
        return tagCode;
    }
    public void setAsDynamicType(Long asDynamicType)
    {
        this.asDynamicType = asDynamicType;
    }

    public Long getAsDynamicType()
    {
        return asDynamicType;
    }
    public void setProxyTypeName(String proxyTypeName)
    {
        this.proxyTypeName = proxyTypeName;
    }

    public String getProxyTypeName()
    {
        return proxyTypeName;
    }
    public void setProxyHost(String proxyHost)
    {
        this.proxyHost = proxyHost;
    }

    public String getProxyHost()
    {
        return proxyHost;
    }
    public void setProxyPort(Long proxyPort)
    {
        this.proxyPort = proxyPort;
    }

    public Long getProxyPort()
    {
        return proxyPort;
    }
    public void setProxyAccount(String proxyAccount)
    {
        this.proxyAccount = proxyAccount;
    }

    public String getProxyAccount()
    {
        return proxyAccount;
    }
    public void setProxyPassword(String proxyPassword)
    {
        this.proxyPassword = proxyPassword;
    }

    public String getProxyPassword()
    {
        return proxyPassword;
    }
    public void setLastUsedIp(String lastUsedIp)
    {
        this.lastUsedIp = lastUsedIp;
    }

    public String getLastUsedIp()
    {
        return lastUsedIp;
    }
    public void setLastCountry(String lastCountry)
    {
        this.lastCountry = lastCountry;
    }

    public String getLastCountry()
    {
        return lastCountry;
    }
    public void setLastRegion(String lastRegion)
    {
        this.lastRegion = lastRegion;
    }

    public String getLastRegion()
    {
        return lastRegion;
    }
    public void setLastCity(String lastCity)
    {
        this.lastCity = lastCity;
    }

    public String getLastCity()
    {
        return lastCity;
    }
    public void setUa(String ua)
    {
        this.ua = ua;
    }

    public String getUa()
    {
        return ua;
    }
    public void setRefreshUrl(String refreshUrl)
    {
        this.refreshUrl = refreshUrl;
    }

    public String getRefreshUrl()
    {
        return refreshUrl;
    }
    public void setOpenTime(Date openTime)
    {
        this.openTime = openTime;
    }

    public Date getOpenTime()
    {
        return openTime;
    }
    public void setAllOpenTime(String allOpenTime)
    {
        this.allOpenTime = allOpenTime;
    }

    public String getAllOpenTime()
    {
        return allOpenTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("containerCode", getContainerCode())
                .append("serialNumber", getSerialNumber())
                .append("containerName", getContainerName())
                .append("accountName", getAccountName())
                .append("platformName", getPlatformName())
                .append("tagName", getTagName())
                .append("tagCode", getTagCode())
                .append("asDynamicType", getAsDynamicType())
                .append("proxyTypeName", getProxyTypeName())
                .append("proxyHost", getProxyHost())
                .append("proxyPort", getProxyPort())
                .append("proxyAccount", getProxyAccount())
                .append("proxyPassword", getProxyPassword())
                .append("lastUsedIp", getLastUsedIp())
                .append("lastCountry", getLastCountry())
                .append("lastRegion", getLastRegion())
                .append("lastCity", getLastCity())
                .append("ua", getUa())
                .append("refreshUrl", getRefreshUrl())
                .append("openTime", getOpenTime())
                .append("allOpenTime", getAllOpenTime())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("remark", getRemark())
                .toString();
    }
}
