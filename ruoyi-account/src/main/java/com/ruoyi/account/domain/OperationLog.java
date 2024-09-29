package com.ruoyi.account.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 操作记录对象 operation_log
 * 
 * @author ruoyi
 * @date 2024-09-29
 */
public class OperationLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    private Long keyId;

    /** 操作账号 */
    @Excel(name = "操作账号")
    private String operationAccount;

    /** 操作内容 */
    @Excel(name = "操作内容")
    private String operationContent;

    /** 操作状态 */
    @Excel(name = "操作状态")
    private String operationStatus;

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date operationTime;

    /** 备注 */
    @Excel(name = "备注")
    private String note;

    public void setKeyId(Long keyId) 
    {
        this.keyId = keyId;
    }

    public Long getKeyId() 
    {
        return keyId;
    }
    public void setOperationAccount(String operationAccount) 
    {
        this.operationAccount = operationAccount;
    }

    public String getOperationAccount() 
    {
        return operationAccount;
    }
    public void setOperationContent(String operationContent) 
    {
        this.operationContent = operationContent;
    }

    public String getOperationContent() 
    {
        return operationContent;
    }
    public void setOperationStatus(String operationStatus) 
    {
        this.operationStatus = operationStatus;
    }

    public String getOperationStatus() 
    {
        return operationStatus;
    }
    public void setOperationTime(Date operationTime) 
    {
        this.operationTime = operationTime;
    }

    public Date getOperationTime() 
    {
        return operationTime;
    }
    public void setNote(String note) 
    {
        this.note = note;
    }

    public String getNote() 
    {
        return note;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("keyId", getKeyId())
            .append("operationAccount", getOperationAccount())
            .append("operationContent", getOperationContent())
            .append("operationStatus", getOperationStatus())
            .append("operationTime", getOperationTime())
            .append("note", getNote())
            .toString();
    }
}
