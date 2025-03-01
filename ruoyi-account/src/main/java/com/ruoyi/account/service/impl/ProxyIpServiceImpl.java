package com.ruoyi.account.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.ruoyi.account.domain.CreateInfo;
import com.ruoyi.account.util.RandomUitl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.impl.SysUserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.account.mapper.ProxyIpMapper;
import com.ruoyi.account.domain.ProxyIp;
import com.ruoyi.account.service.IProxyIpService;

/**
 * 代理Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-02-28
 */
@Service
public class ProxyIpServiceImpl implements IProxyIpService {

    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private ProxyIpMapper proxyIpMapper;

    /**
     * 查询代理
     * 
     * @param keyId 代理主键
     * @return 代理
     */
    @Override
    public ProxyIp selectProxyIpByKeyId(Long keyId)
    {
        return proxyIpMapper.selectProxyIpByKeyId(keyId);
    }

    /**
     * 查询代理列表
     * 
     * @param proxyIp 代理
     * @return 代理
     */
    @Override
    public List<ProxyIp> selectProxyIpList(ProxyIp proxyIp)
    {
        return proxyIpMapper.selectProxyIpList(proxyIp);
    }

    /**
     * 新增代理
     * 
     * @param proxyIp 代理
     * @return 结果
     */
    @Override
    public int insertProxyIp(ProxyIp proxyIp)
    {
        return proxyIpMapper.insertProxyIp(proxyIp);
    }

    /**
     * 修改代理
     * 
     * @param proxyIp 代理
     * @return 结果
     */
    @Override
    public int updateProxyIp(ProxyIp proxyIp)
    {
        return proxyIpMapper.updateProxyIp(proxyIp);
    }

    /**
     * 批量删除代理
     * 
     * @param keyIds 需要删除的代理主键
     * @return 结果
     */
    @Override
    public int deleteProxyIpByKeyIds(Long[] keyIds)
    {
        return proxyIpMapper.deleteProxyIpByKeyIds(keyIds);
    }

    /**
     * 删除代理信息
     * 
     * @param keyId 代理主键
     * @return 结果
     */
    @Override
    public int deleteProxyIpByKeyId(Long keyId)
    {
        return proxyIpMapper.deleteProxyIpByKeyId(keyId);
    }

    /**
     * 导入数据
     *
     * @param proxyIpList     数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importCreateInfo(List<ProxyIp> proxyIpList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(proxyIpList) || proxyIpList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (ProxyIp proxyIp : proxyIpList) {
            try {
                // 验证是否存在这个账号
                ProxyIp ip = proxyIpMapper.selectProxyIpByUsername(proxyIp.getUsername());
                if (StringUtils.isNull(ip))
                {
                    proxyIp.setImportTime(LocalDateTime.now());
                    proxyIpMapper.insertProxyIp(proxyIp);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、信息 " + proxyIp.getPassword() + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    proxyIp.setImportTime(LocalDateTime.now());
                    proxyIpMapper.updateProxyIp(proxyIp);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、信息 " + proxyIp.getPassword()   + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、信息 " + proxyIp.getPassword()  + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、信息 " + proxyIp.getPassword()   + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
