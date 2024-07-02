package com.ruoyi.account.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 账号账户信息对象 account_ad_account_bm_info
 *
 * @author ruoyi
 * @date 2024-03-02
 */
public class AccountAdAccountBmInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long keyId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String accountId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String adAccountName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String adAccountId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String bmName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String bmId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String note;

    public void setKeyId(Long keyId)
    {
        this.keyId = keyId;
    }

    public Long getKeyId()
    {
        return keyId;
    }
    public void setAccountId(String accountId)
    {
        this.accountId = accountId;
    }

    public String getAccountId()
    {
        return accountId;
    }
    public void setAdAccountName(String adAccountName)
    {
        this.adAccountName = adAccountName;
    }

    public String getAdAccountName()
    {
        return adAccountName;
    }
    public void setAdAccountId(String adAccountId)
    {
        this.adAccountId = adAccountId;
    }

    public String getAdAccountId()
    {
        return adAccountId;
    }
    public void setBmName(String bmName)
    {
        this.bmName = bmName;
    }

    public String getBmName()
    {
        return bmName;
    }
    public void setBmId(String bmId)
    {
        this.bmId = bmId;
    }

    public String getBmId()
    {
        return bmId;
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
            .append("accountId", getAccountId())
            .append("adAccountName", getAdAccountName())
            .append("adAccountId", getAdAccountId())
            .append("bmName", getBmName())
            .append("bmId", getBmId())
            .append("note", getNote())
            .toString();
    }
}
