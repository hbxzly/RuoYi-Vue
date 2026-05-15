package com.ruoyi.browser.service.impl;

import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ruoyi.account.domain.FbAccountForSell;
import com.ruoyi.account.domain.ProxyIp;
import com.ruoyi.account.service.IFbAccountForSellService;
import com.ruoyi.account.util.JsonDataUtil;
import com.ruoyi.browser.builder.HubEnvParamBuilder;
import com.ruoyi.browser.client.HubstudioClient;
import com.ruoyi.browser.convert.HubstudioConvert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.browser.mapper.HubEnvMapper;
import com.ruoyi.browser.domain.HubEnv;
import com.ruoyi.browser.service.IHubEnvService;

/**
 * Hubstudio 环境Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-02-02
 */
@Service
public class HubEnvServiceImpl implements IHubEnvService 
{
    @Autowired
    private HubEnvMapper hubEnvMapper;

    @Autowired
    HubstudioClient hubstudioClient;

    @Autowired
    HubstudioConvert hubstudioConvert;

    @Autowired
    IFbAccountForSellService fbAccountForSellService;

    /**
     * 查询Hubstudio 环境
     * 
     * @param id Hubstudio 环境主键
     * @return Hubstudio 环境
     */
    @Override
    public HubEnv selectHubEnvById(Long id)
    {
        return hubEnvMapper.selectHubEnvById(id);
    }

    /**
     * 查询Hubstudio 环境
     *
     * @param accountName
     * @return Hubstudio 环境
     */
    @Override
    public HubEnv selectHubEnvByAccountName(String accountName) {
        return hubEnvMapper.selectHubEnvByAccountName(accountName);
    }

    /**
     * 查询Hubstudio 环境列表
     * 
     * @param hubEnv Hubstudio 环境
     * @return Hubstudio 环境
     */
    @Override
    public List<HubEnv> selectHubEnvList(HubEnv hubEnv)
    {
        return hubEnvMapper.selectHubEnvList(hubEnv);
    }

    /**
     * 新增Hubstudio 环境
     * 
     * @param hubEnv Hubstudio 环境
     * @return 结果
     */
    @Override
    public int insertHubEnv(HubEnv hubEnv)
    {
        hubEnv.setCreateTime(DateUtils.getNowDate());
        return hubEnvMapper.insertHubEnv(hubEnv);
    }

    /**
     * 修改Hubstudio 环境
     * 
     * @param hubEnv Hubstudio 环境
     * @return 结果
     */
    @Override
    public int updateHubEnv(HubEnv hubEnv)
    {
        return hubEnvMapper.updateHubEnv(hubEnv);
    }

    /**
     * 批量删除Hubstudio 环境
     * 
     * @param ids 需要删除的Hubstudio 环境主键
     * @return 结果
     */
    @Override
    public int deleteHubEnvByIds(Long[] ids)
    {
        return hubEnvMapper.deleteHubEnvByIds(ids);
    }

    /**
     * 删除Hubstudio 环境信息
     * 
     * @param id Hubstudio 环境主键
     * @return 结果
     */
    @Override
    public int deleteHubEnvById(Long id)
    {
        return hubEnvMapper.deleteHubEnvById(id);
    }

    /**
     * 获取HubEnv
     *
     * @return
     */
    @Override
    public List<HubEnv> getHubEnvList(Map<String, Object> params) {
        String s = hubstudioClient.listEnv(params);
        try {
            return hubstudioConvert.parseList(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 创建打开环境
     *
     * @param keyId
     */
    @Override
    public WebDriver openOrCreateAndOpenEnvForFb(Long keyId) {
        FbAccountForSell account =
                fbAccountForSellService.selectFbAccountForSellByKeyId(keyId);

        //accountName 是账号ID
        HubEnv hubEnv =
                selectHubEnvByAccountName(account.getId());


        if (hubEnv == null) {
            hubEnv = createAndBindEnv(account);
        }

        return hubstudioClient.openEnv(hubEnv.getContainerCode());
    }

    /**
     * 创建打开email环境
     *
     * @return
     */
    @Override
    public Map<String, Object> openOrCreateAndOpenEnvForEmail(ProxyIp proxyIp) {
        Map<String, Object> emailEnvParam = HubEnvParamBuilder.buildCreateUnlockEmailEnvParam(proxyIp);
        System.out.println(emailEnvParam);
        String env = hubstudioClient.createEnv(emailEnvParam);
        System.out.println(env);
        String containerCode = JsonDataUtil.getValueByNodeName(env, "data", "containerCode");
        WebDriver webDriver = hubstudioClient.openEnv(Long.valueOf(containerCode));
        HashMap<String, Object> objectHashMap = new HashMap<>();
        objectHashMap.put("containerCode", containerCode);
        objectHashMap.put("webDriver", webDriver);
        return objectHashMap;
    }

    /**
     * 关闭环境
     *
     * @param hubEnv
     */
    @Override
    public void closeEnv(HubEnv hubEnv) {
        Map<String, Object> map = HubEnvParamBuilder.builderCloseEnvParam(hubEnv.getContainerCode());
        hubstudioClient.closeEnv(map);
    }

    /**
     * 删除环境
     *
     * @param hubEnv
     */
    @Override
    public void deleteEnv(HubEnv hubEnv) {
        Map<String, Object> deleteEnvParam = HubEnvParamBuilder.builderDeleteEnvParam(Arrays.asList(hubEnv.getContainerCode()));
        hubstudioClient.deleteEnv(deleteEnvParam);
    }


    /**
     * 创建+绑定账号
     * @param account
     * @return
     */
    @Override
    public HubEnv createAndBindEnv(FbAccountForSell account) {

        String envJson = hubstudioClient.createEnv(
                HubEnvParamBuilder.buildCreateFbEnvParam(new ProxyIp())
        );
        System.out.println(envJson.toString());
        Long containerCode = Long.valueOf(
                JsonDataUtil.getValueByNodeName(envJson, "data", "containerCode")
        );

        String addAccountJson = hubstudioClient.addAccount(
                HubEnvParamBuilder.builderAddAccountParam(account, containerCode)
        );

        String msg = JsonDataUtil.getValueByNodeName(addAccountJson, "msg");
        if (!"Success".equals(msg)) {
            throw new ServiceException("账号绑定环境失败");
        }

        String listJson = hubstudioClient.listEnv(
                HubEnvParamBuilder.builderEnvListParam(Arrays.asList(containerCode))
        );

        try {
            List<HubEnv> envs = hubstudioConvert.parseList(listJson);
            HubEnv hubEnv = envs.get(0);
            insertHubEnv(hubEnv);
            return hubEnv;
        } catch (JsonProcessingException e) {
            throw new ServiceException("环境解析失败");
        }
    }


}
