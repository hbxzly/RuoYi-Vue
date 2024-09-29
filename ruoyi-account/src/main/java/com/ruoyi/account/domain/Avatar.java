package com.ruoyi.account.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 头像对象 avatar
 * 
 * @author ruoyi
 * @date 2024-09-29
 */
public class Avatar extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    private Long keyId;

    /** 头像路径 */
    @Excel(name = "头像路径")
    private String filePath;

    /** 头像名称 */
    @Excel(name = "头像名称")
    private String avatarName;

    /** 头像类型 */
    @Excel(name = "头像类型")
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
    public void setAvatarName(String avatarName) 
    {
        this.avatarName = avatarName;
    }

    public String getAvatarName() 
    {
        return avatarName;
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
            .append("avatarName", getAvatarName())
            .append("type", getType())
            .append("note", getNote())
            .toString();
    }
}
