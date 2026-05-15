package com.ruoyi.account.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 名字对象 account_name
 * 
 * @author ruoyi
 * @date 2025-09-17
 */
public class AccountName extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    private Long keyId;

    /** 名字 */
    @Excel(name = "名字")
    private String name;

    /** 性别 */
    @Excel(name = "性别")
    private String gender;

    /** 是否使用 */
    @Excel(name = "是否使用")
    private String isUse;

    public void setKeyId(Long keyId) 
    {
        this.keyId = keyId;
    }

    public Long getKeyId() 
    {
        return keyId;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setGender(String gender) 
    {
        this.gender = gender;
    }

    public String getGender() 
    {
        return gender;
    }
    public void setIsUse(String isUse) 
    {
        this.isUse = isUse;
    }

    public String getIsUse() 
    {
        return isUse;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("keyId", getKeyId())
            .append("name", getName())
            .append("gender", getGender())
            .append("isUse", getIsUse())
            .toString();
    }
}
