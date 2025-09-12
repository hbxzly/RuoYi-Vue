package com.ruoyi.account.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 创建设备对象 create_device
 * 
 * @author ruoyi
 * @date 2025-07-15
 */
public class CreateDevice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long keyId;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 包名 */
    @Excel(name = "包名")
    private String packageName;

    /** 创建账号ID */
    @Excel(name = "创建账号ID")
    private String createAccountId;

    /** 创建日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createDate;

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
    public void setDeviceName(String deviceName) 
    {
        this.deviceName = deviceName;
    }

    public String getDeviceName() 
    {
        return deviceName;
    }
    public void setPackageName(String packageName) 
    {
        this.packageName = packageName;
    }

    public String getPackageName() 
    {
        return packageName;
    }
    public void setCreateAccountId(String createAccountId) 
    {
        this.createAccountId = createAccountId;
    }

    public String getCreateAccountId() 
    {
        return createAccountId;
    }
    public void setCreateDate(Date createDate) 
    {
        this.createDate = createDate;
    }

    public Date getCreateDate() 
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("keyId", getKeyId())
            .append("deviceName", getDeviceName())
            .append("packageName", getPackageName())
            .append("createAccountId", getCreateAccountId())
            .append("createDate", getCreateDate())
            .append("note", getNote())
            .toString();
    }
}
