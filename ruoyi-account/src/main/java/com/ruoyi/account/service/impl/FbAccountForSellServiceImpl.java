package com.ruoyi.account.service.impl;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.account.mapper.FbAccountForSellMapper;
import com.ruoyi.account.domain.FbAccountForSell;
import com.ruoyi.account.service.IFbAccountForSellService;

/**
 * 卖号Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-09-20
 */
@Service
public class FbAccountForSellServiceImpl implements IFbAccountForSellService 
{
    @Autowired
    private FbAccountForSellMapper fbAccountForSellMapper;

    /**
     * 查询卖号
     * 
     * @param keyId 卖号主键
     * @return 卖号
     */
    @Override
    public FbAccountForSell selectFbAccountForSellByKeyId(Long keyId)
    {
        return fbAccountForSellMapper.selectFbAccountForSellByKeyId(keyId);
    }

    /**
     * 查询卖号列表
     * 
     * @param fbAccountForSell 卖号
     * @return 卖号
     */
    @Override
    public List<FbAccountForSell> selectFbAccountForSellList(FbAccountForSell fbAccountForSell)
    {
        return fbAccountForSellMapper.selectFbAccountForSellList(fbAccountForSell);
    }

    /**
     * 新增卖号
     * 
     * @param fbAccountForSell 卖号
     * @return 结果
     */
    @Override
    public int insertFbAccountForSell(FbAccountForSell fbAccountForSell)
    {
        return fbAccountForSellMapper.insertFbAccountForSell(fbAccountForSell);
    }

    /**
     * 修改卖号
     * 
     * @param fbAccountForSell 卖号
     * @return 结果
     */
    @Override
    public int updateFbAccountForSell(FbAccountForSell fbAccountForSell)
    {
        return fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
    }

    /**
     * 批量删除卖号
     * 
     * @param keyIds 需要删除的卖号主键
     * @return 结果
     */
    @Override
    public int deleteFbAccountForSellByKeyIds(Long[] keyIds)
    {
        return fbAccountForSellMapper.deleteFbAccountForSellByKeyIds(keyIds);
    }

    /**
     * 删除卖号信息
     * 
     * @param keyId 卖号主键
     * @return 结果
     */
    @Override
    public int deleteFbAccountForSellByKeyId(Long keyId)
    {
        return fbAccountForSellMapper.deleteFbAccountForSellByKeyId(keyId);
    }

    /**
     * 登录账号
     *
     * @param webDriver
     * @param fbAccountForSell
     */
    @Override
    public void login(WebDriver webDriver, FbAccountForSell fbAccountForSell) {

    }
}
