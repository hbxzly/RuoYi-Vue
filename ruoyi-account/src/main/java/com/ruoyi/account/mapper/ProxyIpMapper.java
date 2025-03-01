package com.ruoyi.account.mapper;

import java.util.List;
import com.ruoyi.account.domain.ProxyIp;

/**
 * 代理Mapper接口
 * 
 * @author ruoyi
 * @date 2025-02-28
 */
public interface ProxyIpMapper 
{
    /**
     * 查询代理
     * 
     * @param keyId 代理主键
     * @return 代理
     */
    public ProxyIp selectProxyIpByKeyId(Long keyId);

    /**
     * 查询代理
     *
     * @param username 代理主键
     * @return 代理
     */
    public ProxyIp selectProxyIpByUsername(String username);

    /**
     * 查询代理列表
     * 
     * @param proxyIp 代理
     * @return 代理集合
     */
    public List<ProxyIp> selectProxyIpList(ProxyIp proxyIp);

    /**
     * 新增代理
     * 
     * @param proxyIp 代理
     * @return 结果
     */
    public int insertProxyIp(ProxyIp proxyIp);

    /**
     * 修改代理
     * 
     * @param proxyIp 代理
     * @return 结果
     */
    public int updateProxyIp(ProxyIp proxyIp);

    /**
     * 删除代理
     * 
     * @param keyId 代理主键
     * @return 结果
     */
    public int deleteProxyIpByKeyId(Long keyId);

    /**
     * 批量删除代理
     * 
     * @param keyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProxyIpByKeyIds(Long[] keyIds);
}
