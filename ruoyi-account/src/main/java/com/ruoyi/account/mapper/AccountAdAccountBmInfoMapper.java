package com.ruoyi.account.mapper;

import com.ruoyi.account.domain.AccountAdAccountBmInfo;

import java.util.List;

/**
 * 账号账户信息Mapper接口
 *
 * @author ruoyi
 * @date 2024-03-02
 */
public interface AccountAdAccountBmInfoMapper
{
    /**
     * 查询账号账户信息
     *
     * @param keyId 账号账户信息主键
     * @return 账号账户信息
     */
    public AccountAdAccountBmInfo selectAccountAdAccountBmInfoByKeyId(Long keyId);

    /**
     * 查询账号账户信息列表
     *
     * @param accountAdAccountBmInfo 账号账户信息
     * @return 账号账户信息集合
     */
    public List<AccountAdAccountBmInfo> selectAccountAdAccountBmInfoList(AccountAdAccountBmInfo accountAdAccountBmInfo);

    /**
     * 新增账号账户信息
     *
     * @param accountAdAccountBmInfo 账号账户信息
     * @return 结果
     */
    public int insertAccountAdAccountBmInfo(AccountAdAccountBmInfo accountAdAccountBmInfo);

    /**
     * 修改账号账户信息
     *
     * @param accountAdAccountBmInfo 账号账户信息
     * @return 结果
     */
    public int updateAccountAdAccountBmInfo(AccountAdAccountBmInfo accountAdAccountBmInfo);

    /**
     * 删除账号账户信息
     *
     * @param keyId 账号账户信息主键
     * @return 结果
     */
    public int deleteAccountAdAccountBmInfoByKeyId(Long keyId);

    /**
     * 批量删除账号账户信息
     *
     * @param keyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAccountAdAccountBmInfoByKeyIds(Long[] keyIds);
}
