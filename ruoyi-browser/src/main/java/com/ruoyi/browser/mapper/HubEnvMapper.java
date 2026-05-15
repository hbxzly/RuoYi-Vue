package com.ruoyi.browser.mapper;

import java.util.List;
import com.ruoyi.browser.domain.HubEnv;

/**
 * Hubstudio 环境Mapper接口
 * 
 * @author ruoyi
 * @date 2026-02-02
 */
public interface HubEnvMapper 
{
    /**
     * 查询Hubstudio 环境
     * 
     * @param id Hubstudio 环境主键
     * @return Hubstudio 环境
     */
    public HubEnv selectHubEnvById(Long id);

    /**
     * 查询Hubstudio 环境
     *
     * @param accountName
     * @return Hubstudio 环境
     */
    public HubEnv selectHubEnvByAccountName(String accountName);

    /**
     * 查询Hubstudio 环境列表
     * 
     * @param hubEnv Hubstudio 环境
     * @return Hubstudio 环境集合
     */
    public List<HubEnv> selectHubEnvList(HubEnv hubEnv);

    /**
     * 新增Hubstudio 环境
     * 
     * @param hubEnv Hubstudio 环境
     * @return 结果
     */
    public int insertHubEnv(HubEnv hubEnv);

    /**
     * 修改Hubstudio 环境
     * 
     * @param hubEnv Hubstudio 环境
     * @return 结果
     */
    public int updateHubEnv(HubEnv hubEnv);

    /**
     * 删除Hubstudio 环境
     * 
     * @param id Hubstudio 环境主键
     * @return 结果
     */
    public int deleteHubEnvById(Long id);

    /**
     * 批量删除Hubstudio 环境
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteHubEnvByIds(Long[] ids);
}
