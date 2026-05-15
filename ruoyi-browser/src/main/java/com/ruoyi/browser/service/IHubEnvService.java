package com.ruoyi.browser.service;

import java.util.List;
import java.util.Map;

import com.ruoyi.account.domain.FbAccountForSell;
import com.ruoyi.account.domain.ProxyIp;
import com.ruoyi.browser.domain.HubEnv;
import org.openqa.selenium.WebDriver;

/**
 * Hubstudio 环境Service接口
 * 
 * @author ruoyi
 * @date 2026-02-02
 */
public interface IHubEnvService 
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
     * 批量删除Hubstudio 环境
     * 
     * @param ids 需要删除的Hubstudio 环境主键集合
     * @return 结果
     */
    public int deleteHubEnvByIds(Long[] ids);

    /**
     * 删除Hubstudio 环境信息
     * 
     * @param id Hubstudio 环境主键
     * @return 结果
     */
    public int deleteHubEnvById(Long id);

    /**
     * 获取HubEnv
     * @return
     */
    public List<HubEnv> getHubEnvList(Map<String, Object> params);


    /**
     * 创建打开FB环境
     * @param keyId
     * @return
     */
    public WebDriver openOrCreateAndOpenEnvForFb(Long keyId);

    /**
     * 创建打开email环境
     * @return
     */
    public Map<String, Object> openOrCreateAndOpenEnvForEmail(ProxyIp proxyIp);

    /**
     * 关闭环境
     * @param hubEnv
     */
    public void closeEnv(HubEnv hubEnv);

    /**
     * 删除环境
     * @param hubEnv
     */
    public void deleteEnv(HubEnv hubEnv);

    /**
     * 创建+绑定账号
     * @param account
     * @return
     */
    public HubEnv createAndBindEnv(FbAccountForSell account);
}
