package com.ruoyi.account.service;

import com.ruoyi.account.domain.FbAccount;
import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.WebDriver;

import java.awt.*;
import java.util.List;
import java.util.Map;

public interface ISeleniumService {

    /**
     * 打开打单个浏览器
     * @param fbAccount
     * @return
     */
    WebDriver openBrowser(FbAccount fbAccount);

    /**
     * 打开多个浏览器
     * @param ids
     * @return
     */
    String multipleOpenBrowser(String[] ids);

    /**
     * 关闭单个浏览器
     * @param fbAccount
     * @return
     */
    String closeBrowser(FbAccount fbAccount);

    /**
     * 关闭所有浏览器
     * @return
     */
    String closeAllBrowser();

    /**
     * 登录账号
     * @param fbAccount
     */
    void login(FbAccount fbAccount);

    /**
     * 显示浏览器
     * @param fbAccount
     */
    void showBrowser(FbAccount fbAccount);

    /**
     * 创建BM
     * @param fbAccount
     */
    void createBM(FbAccount fbAccount);

    /**
     * 打开广告，广告截图
     * @param keyIds
     */
    void openScreenshotPage(Long[] keyIds);

    /**
     * 查看BM
     * @param ids
     */
    void checkBM(String[] ids);

    /**
     * 查看账号品质
     * @param ids
     */
    void checkAccount(String[] ids);


    /**
     * 批量添加好友
     * @param id
     * @param accountIds
     */
    void batchAddFriend(String id, String[] accountIds);

    /**
     * 打开广告管理工具页面
     * @param fbAccount
     */
    void openAdvertise(FbAccount fbAccount,String adAccountId);

    /**
     * 修改浏览器名称
     * @param id
     */
    void updateBrowserProfile(String id) throws AWTException;

    /**
     * 打开广告管理工具界面
     * 读账户、BM信息
     * @param id
     */
    void openAds(String id);

    /**
     * 获取广告账户信息
     * @param id
     */
    void loadAdAccountInfo(String id);

    /**
     * 获取浏览器标签页句柄
     * @param id
     */
    void getWindowHandles(String id);

    /**
     * 获取账号姓名
     * @param ids
     */
    void getAccountName(String[] ids);

    /**
     *解限第一步
     * @param ids
     */
    void unlimitedAccountOneStep(String[] ids);

    /**
     *解限第一步
     * @param ids
     */
    void unlimitedAccountTwoStep(String[] ids);

    /**
     * 获取账号状态信息
     * @param fbAccount
     * @return
     */
    FbAccount checkAccountInfo(FbAccount fbAccount);

}
