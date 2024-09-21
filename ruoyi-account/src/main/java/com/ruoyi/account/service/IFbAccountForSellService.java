package com.ruoyi.account.service;

import java.util.List;
import com.ruoyi.account.domain.FbAccountForSell;
import org.openqa.selenium.WebDriver;

/**
 * 卖号Service接口
 * 
 * @author ruoyi
 * @date 2024-09-20
 */
public interface IFbAccountForSellService 
{
    /**
     * 查询卖号
     * 
     * @param keyId 卖号主键
     * @return 卖号
     */
    public FbAccountForSell selectFbAccountForSellByKeyId(Long keyId);

    /**
     * 查询卖号列表
     * 
     * @param fbAccountForSell 卖号
     * @return 卖号集合
     */
    public List<FbAccountForSell> selectFbAccountForSellList(FbAccountForSell fbAccountForSell);

    /**
     * 新增卖号
     * 
     * @param fbAccountForSell 卖号
     * @return 结果
     */
    public int insertFbAccountForSell(FbAccountForSell fbAccountForSell);

    /**
     * 修改卖号
     * 
     * @param fbAccountForSell 卖号
     * @return 结果
     */
    public int updateFbAccountForSell(FbAccountForSell fbAccountForSell);

    /**
     * 批量删除卖号
     * 
     * @param keyIds 需要删除的卖号主键集合
     * @return 结果
     */
    public int deleteFbAccountForSellByKeyIds(Long[] keyIds);

    /**
     * 删除卖号信息
     * 
     * @param keyId 卖号主键
     * @return 结果
     */
    public int deleteFbAccountForSellByKeyId(Long keyId);

    /**
     * 登录账号
     * @param webDriver
     * @param fbAccountForSell
     */
    public void login(WebDriver webDriver, FbAccountForSell fbAccountForSell);


}
