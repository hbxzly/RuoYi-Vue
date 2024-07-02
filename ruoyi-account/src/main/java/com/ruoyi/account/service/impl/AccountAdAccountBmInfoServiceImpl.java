package com.ruoyi.account.service.impl;

import java.util.List;

import com.ruoyi.account.domain.AccountAdAccountBmInfo;
import com.ruoyi.account.mapper.AccountAdAccountBmInfoMapper;
import com.ruoyi.account.service.IAccountAdAccountBmInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 账号账户信息Service业务层处理
 *
 * @author ruoyi
 * @date 2024-03-02
 */
@Service
public class AccountAdAccountBmInfoServiceImpl implements IAccountAdAccountBmInfoService
{
    @Autowired
    private AccountAdAccountBmInfoMapper accountAdAccountBmInfoMapper;

    /**
     * 查询账号账户信息
     *
     * @param keyId 账号账户信息主键
     * @return 账号账户信息
     */
    @Override
    public AccountAdAccountBmInfo selectAccountAdAccountBmInfoByKeyId(Long keyId)
    {
        return accountAdAccountBmInfoMapper.selectAccountAdAccountBmInfoByKeyId(keyId);
    }

    /**
     * 查询账号账户信息列表
     *
     * @param accountAdAccountBmInfo 账号账户信息
     * @return 账号账户信息
     */
    @Override
    public List<AccountAdAccountBmInfo> selectAccountAdAccountBmInfoList(AccountAdAccountBmInfo accountAdAccountBmInfo)
    {
        return accountAdAccountBmInfoMapper.selectAccountAdAccountBmInfoList(accountAdAccountBmInfo);
    }

    /**
     * 新增账号账户信息
     *
     * @param accountAdAccountBmInfo 账号账户信息
     * @return 结果
     */
    @Override
    public int insertAccountAdAccountBmInfo(AccountAdAccountBmInfo accountAdAccountBmInfo)
    {
        return accountAdAccountBmInfoMapper.insertAccountAdAccountBmInfo(accountAdAccountBmInfo);
    }

    /**
     * 修改账号账户信息
     *
     * @param accountAdAccountBmInfo 账号账户信息
     * @return 结果
     */
    @Override
    public int updateAccountAdAccountBmInfo(AccountAdAccountBmInfo accountAdAccountBmInfo)
    {
        return accountAdAccountBmInfoMapper.updateAccountAdAccountBmInfo(accountAdAccountBmInfo);
    }

    /**
     * 批量删除账号账户信息
     *
     * @param keyIds 需要删除的账号账户信息主键
     * @return 结果
     */
    @Override
    public int deleteAccountAdAccountBmInfoByKeyIds(Long[] keyIds)
    {
        return accountAdAccountBmInfoMapper.deleteAccountAdAccountBmInfoByKeyIds(keyIds);
    }

    /**
     * 删除账号账户信息信息
     *
     * @param keyId 账号账户信息主键
     * @return 结果
     */
    @Override
    public int deleteAccountAdAccountBmInfoByKeyId(Long keyId)
    {
        return accountAdAccountBmInfoMapper.deleteAccountAdAccountBmInfoByKeyId(keyId);
    }
}
