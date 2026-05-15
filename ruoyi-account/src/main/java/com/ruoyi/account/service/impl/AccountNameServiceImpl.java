package com.ruoyi.account.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.account.mapper.AccountNameMapper;
import com.ruoyi.account.domain.AccountName;
import com.ruoyi.account.service.IAccountNameService;

/**
 * 名字Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-17
 */
@Service
public class AccountNameServiceImpl implements IAccountNameService 
{
    @Autowired
    private AccountNameMapper accountNameMapper;

    /**
     * 查询名字
     * 
     * @param keyId 名字主键
     * @return 名字
     */
    @Override
    public AccountName selectAccountNameByKeyId(Long keyId)
    {
        return accountNameMapper.selectAccountNameByKeyId(keyId);
    }

    /**
     * 查询名字列表
     * 
     * @param accountName 名字
     * @return 名字
     */
    @Override
    public List<AccountName> selectAccountNameList(AccountName accountName)
    {
        return accountNameMapper.selectAccountNameList(accountName);
    }

    /**
     * 新增名字
     * 
     * @param accountName 名字
     * @return 结果
     */
    @Override
    public int insertAccountName(AccountName accountName)
    {
        return accountNameMapper.insertAccountName(accountName);
    }

    /**
     * 修改名字
     * 
     * @param accountName 名字
     * @return 结果
     */
    @Override
    public int updateAccountName(AccountName accountName)
    {
        return accountNameMapper.updateAccountName(accountName);
    }

    /**
     * 批量删除名字
     * 
     * @param keyIds 需要删除的名字主键
     * @return 结果
     */
    @Override
    public int deleteAccountNameByKeyIds(Long[] keyIds)
    {
        return accountNameMapper.deleteAccountNameByKeyIds(keyIds);
    }

    /**
     * 删除名字信息
     * 
     * @param keyId 名字主键
     * @return 结果
     */
    @Override
    public int deleteAccountNameByKeyId(Long keyId)
    {
        return accountNameMapper.deleteAccountNameByKeyId(keyId);
    }
}
