package com.ruoyi.account.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 背景图对象 background
 * 
 * @author ruoyi
 * @date 2024-09-29
 */
public class Background extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号ID */
    private Long keyId;

    /** 文件路径 */
    @Excel(name = "文件路径")
    private String filePath;

    /** 背景图名称 */
    @Excel(name = "背景图名称")
    private String backgroundName;

    /** 类型 */
    @Excel(name = "类型")
    private String type;

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
    public void setFilePath(String filePath) 
    {
        this.filePath = filePath;
    }

    public String getFilePath() 
    {
        return filePath;
    }
    public void setBackgroundName(String backgroundName) 
    {
        this.backgroundName = backgroundName;
    }

    public String getBackgroundName() 
    {
        return backgroundName;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
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
            .append("filePath", getFilePath())
            .append("backgroundName", getBackgroundName())
            .append("type", getType())
            .append("note", getNote())
            .toString();
    }
}
