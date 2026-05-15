package com.ruoyi.account.service;

import java.util.List;

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
     * 查询卖号
     *
     * @param id 卖号id
     * @return 卖号
     */
    public FbAccountForSell selectFbAccountForSellById(String id);

    /**
     * 查询卖号
     *
     * @param email 邮箱
     * @return 卖号
     */
    public FbAccountForSell selectFbAccountForSellByEmail(String email);

    /**
     * 查询卖号列表
     * 
     * @param fbAccountForSell 卖号
     * @return 卖号集合
     */
    public List<FbAccountForSell> selectFbAccountForSellList(FbAccountForSell fbAccountForSell);

    /**
     * 查询卖号列表
     *
     * @param fbAccountForSell 卖号
     * @return 卖号集合
     */
    public List<FbAccountForSell> selectFbAccountForSellListNoId(FbAccountForSell fbAccountForSell);

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
     * 获取名字和好友数量（简体）
     * @param webDriver
     * @param fbAccount
     */
    public void getAccountNameAndFriendNumber(WebDriver webDriver, FbAccountForSell fbAccount);

    /**
     * 获取商城信息（简体）
     * @param webDriver
     * @param fbAccountForSell
     */
    public void getIsMarketplace(WebDriver webDriver, FbAccountForSell fbAccountForSell);

    /**
     * 获取广告状态（简体）
     * @param webDriver
     * @param fbAccountForSell
     */
    public void getAccountAdStatus(WebDriver webDriver, FbAccountForSell fbAccountForSell);

    /**
     * 获取Bm数量（简体）
     * @param webDriver
     * @param fbAccountForSell
     */
    public void getAccountBm(WebDriver webDriver, FbAccountForSell fbAccountForSell);

    /**
     * 获取主页数量（简体）
     * @param webDriver
     * @param fbAccountForSell
     */
    public void getAccountPage(WebDriver webDriver, FbAccountForSell fbAccountForSell);

    /**
     * 获取帖子数量（简体）
     * @param webDriver
     * @param fbAccountForSell
     */
    public void getAccountPostCount(WebDriver webDriver, FbAccountForSell fbAccountForSell);

    /**
     * 获取登录记录（简体）
     * @param webDriver
     * @param fbAccountForSell
     */
    public void getAccountLoginLog(WebDriver webDriver, FbAccountForSell fbAccountForSell);


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
     * 查询账号
     * @param ids
     * @return
     */
    public List<FbAccountForSell> selectFbAccountForSellListByAccountIds(List<Long> ids);

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
    public String addFriend(FbAccountForSell fbAccountForSell, String id, WebDriver webDriver);

    /**
     * 添加好友
     * @param addId
     * @param number
     * @param region
     * @param canLogin
     * @param isSell
     * @return
     */
    public String addFriendNew(String addId, Integer number, String region, String canLogin, String isSell);

    /**
     * 打开浏览器
     * @param fbAccountForSell
     * @return
     */
    public WebDriver openBrowser(FbAccountForSell fbAccountForSell);

    /**
     * 关闭浏览器
     * @param fbAccountForSell
     */
    public void closeBrowser(FbAccountForSell fbAccountForSell);

    /**
     * 显示浏览器
     * @param fbAccountForSell
     */
    public void showBrowser(FbAccountForSell fbAccountForSell);

    /**
     * 创建主页
     * @param fbAccountForSell
     * @param webDriver
     * @param pageName
     * @return
     */
    public String createPage(WebDriver webDriver, FbAccountForSell fbAccountForSell,String pageName);

    /**
     * 打开比特浏览器
     * @param fbAccountForSell
     * @return
     */
    public String openBitBrowser(FbAccountForSell fbAccountForSell);

    /**
     * 确认添加好友
     * @param fbAccountForSell
     * @return
     */
    public String confirmAddFriend(FbAccountForSell fbAccountForSell);


    /**
     * 修改账号名
     * @param fbAccountForSell
     * @param newName
     * @return
     */
    public String changeAccountName(WebDriver webDriver, FbAccountForSell fbAccountForSell, String newName);

    /**
     * 修改随机名字
     * @param webDriver
     * @param newName
     * @return
     */
    public String changeRandomAccountName(WebDriver webDriver, FbAccountForSell fbAccountForSell, String newName);


    /**
     * 批量查询
     * @param account
     * @return
     */
    public List<FbAccountForSell> batchSearch(FbAccountForSell account);

    /**
     *
     * @param webDriver
     * @param fbAccountForSell
     * @return
     */
    public String superAccount(WebDriver webDriver, FbAccountForSell fbAccountForSell);

    /**
     * 改成台湾号
     * @param webDriver
     * @param fbAccountForSell
     * @return
     */
    public String changeTWAccount(WebDriver webDriver, FbAccountForSell fbAccountForSell);

}
