package com.ruoyi.account.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 advertise
 *
 * @author ruoyi
 * @date 2023-09-25
 */
public class Advertise extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long keyId;

    /** 账户ID */
    @Excel(name = "账户ID")
    private String adAccountId;

    /** 账户名称 */
    @Excel(name = "账户名称")
    private String adAccountName;

    /** 主页名称 */
    @Excel(name = "主页名称")
    private String pageName;

    /** 主页ID */
    @Excel(name = "主页ID")
    private String pageId;

    /** 运营 */
    @Excel(name = "运营")
    private String operation;

    /** 客户 */
    @Excel(name = "客户")
    private String client;

    /** 投放内容 */
    @Excel(name = "投放内容")
    private String advertisingContent;

    /** 投放地区 */
    @Excel(name = "投放地区")
    private String placementArea;

    /** 投放状态 */
    @Excel(name = "投放状态")
    private String status;

    /** 账户类型 */
    @Excel(name = "账户类型")
    private String adType;

    /** 广告手 */
    @Excel(name = "广告手")
    private String operator;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 备注 */
    @Excel(name = "备注")
    private String note;

    /** 授权账号 */
    @Excel(name = "授权账号")
    private String authorizedAccount;

    /** 授权BM */
    @Excel(name = "授权BM")
    private String authorizedBm;

    public void setKeyId(Long keyId)
    {
        this.keyId = keyId;
    }

    public Long getKeyId()
    {
        return keyId;
    }
    public void setAdAccountId(String adAccountId)
    {
        this.adAccountId = adAccountId;
    }

    public String getAdAccountId()
    {
        return adAccountId;
    }
    public void setAdAccountName(String adAccountName)
    {
        this.adAccountName = adAccountName;
    }

    public String getAdAccountName()
    {
        return adAccountName;
    }
    public void setPageName(String pageName)
    {
        this.pageName = pageName;
    }

    public String getPageName()
    {
        return pageName;
    }
    public void setPageId(String pageId)
    {
        this.pageId = pageId;
    }

    public String getPageId()
    {
        return pageId;
    }
    public void setOperation(String operation)
    {
        this.operation = operation;
    }

    public String getOperation()
    {
        return operation;
    }
    public void setClient(String client)
    {
        this.client = client;
    }

    public String getClient()
    {
        return client;
    }
    public void setAdvertisingContent(String advertisingContent)
    {
        this.advertisingContent = advertisingContent;
    }

    public String getAdvertisingContent()
    {
        return advertisingContent;
    }
    public void setPlacementArea(String placementArea)
    {
        this.placementArea = placementArea;
    }

    public String getPlacementArea()
    {
        return placementArea;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setAdType(String adType)
    {
        this.adType = adType;
    }

    public String getAdType()
    {
        return adType;
    }
    public void setOperator(String operator)
    {
        this.operator = operator;
    }

    public String getOperator()
    {
        return operator;
    }
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getStartTime()
    {
        return startTime;
    }
    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }
    public void setNote(String note)
    {
        this.note = note;
    }

    public String getNote()
    {
        return note;
    }
    public void setAuthorizedAccount(String authorizedAccount)
    {
        this.authorizedAccount = authorizedAccount;
    }

    public String getAuthorizedAccount()
    {
        return authorizedAccount;
    }
    public void setAuthorizedBm(String authorizedBm)
    {
        this.authorizedBm = authorizedBm;
    }

    public String getAuthorizedBm()
    {
        return authorizedBm;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("keyId", getKeyId())
            .append("adAccountId", getAdAccountId())
            .append("adAccountName", getAdAccountName())
            .append("pageName", getPageName())
            .append("pageId", getPageId())
            .append("operation", getOperation())
            .append("client", getClient())
            .append("advertisingContent", getAdvertisingContent())
            .append("placementArea", getPlacementArea())
            .append("status", getStatus())
            .append("adType", getAdType())
            .append("operator", getOperator())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("note", getNote())
            .append("authorizedAccount", getAuthorizedAccount())
            .append("authorizedBm", getAuthorizedBm())
            .toString();
    }
}
