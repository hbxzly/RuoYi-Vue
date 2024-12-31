package com.ruoyi.account.service;

import java.util.List;

import com.ruoyi.account.domain.FbAccount;
import com.ruoyi.account.domain.FbAccountForSell;
import org.openqa.selenium.WebDriver;

/**
 * 卖号Service接口
 * 
 * @author ruoyi
 * @date 2024-11-01
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
     * 导入数据
     *
     * @param FbAccountForSellList 数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importFbAccountForSell(List<FbAccountForSell> FbAccountForSellList, Boolean isUpdateSupport, String operName);

    /**
     * 登录账号
     * @param webDriver
     * @param fbAccountForSell
     */
    public String loginFbAccountForSell(WebDriver webDriver, FbAccountForSell fbAccountForSell);


    /**
     * 获取信息（简体）
     * @param webDriver
     * @param fbAccountForSell
     */
    public void getAccountMarketplaceAndNameAndFriendInSimplified(WebDriver webDriver, FbAccountForSell fbAccountForSell);


    /**
     * 获取账号语言
     * @param webDriver
     * @param fbAccountForSell
     * @return
     */
    public String getAccountLanguage(WebDriver webDriver, FbAccountForSell fbAccountForSell);

    /**
     * 查询账号
     * @param ids
     * @return
     */
    public List<FbAccountForSell> selectFbAccountForSellListByAccountIds(Long[] ids);

    /**
     * 检查是否登录
     * @param webDriver
     * @return
     */
    public boolean isLogin(WebDriver webDriver, FbAccountForSell fbAccountForSell);

    /**
     * 发贴
     * @param fbAccountForSell
     * @param webDriver
     */
    public void post(FbAccountForSell fbAccountForSell, WebDriver webDriver);

    /**
     * 添加好友
     * @param fbAccountForSell
     * @param id
     * @param webDriver
     */
    public void addFriend(FbAccountForSell fbAccountForSell, String id, WebDriver webDriver);
}
