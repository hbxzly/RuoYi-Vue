package com.ruoyi.account.service;

import java.util.List;

import com.ruoyi.account.domain.CreateInfo;
import com.ruoyi.account.domain.ProxyIp;

/**
 * 代理Service接口
 * 
 * @author ruoyi
 * @date 2025-02-28
 */
public interface IProxyIpService 
{
    /**
     * 查询代理
     * 
     * @param keyId 代理主键
     * @return 代理
     */
    public ProxyIp selectProxyIpByKeyId(Long keyId);

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
     * 批量删除代理
     * 
     * @param keyIds 需要删除的代理主键集合
     * @return 结果
     */
    public int deleteProxyIpByKeyIds(Long[] keyIds);

    /**
     * 删除代理信息
     * 
     * @param keyId 代理主键
     * @return 结果
     */
    public int deleteProxyIpByKeyId(Long keyId);

    /**
     * 导入数据
     *
     * @param proxyIpList  数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    public String importCreateInfo(List<ProxyIp> proxyIpList, Boolean isUpdateSupport, String operName);
}
