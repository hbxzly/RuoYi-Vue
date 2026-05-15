package com.ruoyi.account.mapper;

import java.util.List;
import com.ruoyi.account.domain.AccountName;

/**
 * 名字Mapper接口
 * 
 * @author ruoyi
 * @date 2025-09-17
 */
public interface AccountNameMapper 
{
    /**
     * 查询名字
     * 
     * @param keyId 名字主键
     * @return 名字
     */
    public AccountName selectAccountNameByKeyId(Long keyId);

    /**
     * 查询名字列表
     * 
     * @param accountName 名字
     * @return 名字集合
     */
    public List<AccountName> selectAccountNameList(AccountName accountName);

    /**
     * 新增名字
     * 
     * @param accountName 名字
     * @return 结果
     */
    public int insertAccountName(AccountName accountName);

    /**
     * 修改名字
     * 
     * @param accountName 名字
     * @return 结果
     */
    public int updateAccountName(AccountName accountName);

    /**
     * 删除名字
     * 
     * @param keyId 名字主键
     * @return 结果
     */
    public int deleteAccountNameByKeyId(Long keyId);

    /**
     * 批量删除名字
     * 
     * @param keyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAccountNameByKeyIds(Long[] keyIds);
}
