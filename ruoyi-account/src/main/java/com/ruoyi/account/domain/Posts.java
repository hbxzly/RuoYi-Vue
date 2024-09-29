package com.ruoyi.account.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 帖子对象 posts
 * 
 * @author ruoyi
 * @date 2024-09-29
 */
public class Posts extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    private Long keyId;

    /** 路径 */
    @Excel(name = "路径")
    private String filePath;

    /** 图片名称 */
    @Excel(name = "图片名称")
    private String pictureName;

    /** 内容 */
    @Excel(name = "内容")
    private String content;

    /** 分类 */
    @Excel(name = "分类")
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
    public void setPictureName(String pictureName) 
    {
        this.pictureName = pictureName;
    }

    public String getPictureName() 
    {
        return pictureName;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
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
            .append("pictureName", getPictureName())
            .append("content", getContent())
            .append("type", getType())
            .append("note", getNote())
            .toString();
    }
}
