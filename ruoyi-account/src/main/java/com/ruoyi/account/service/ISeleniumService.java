package com.ruoyi.account.service;

import com.ruoyi.account.domain.*;
import org.openqa.selenium.WebDriver;

public interface ISeleniumService {

    /**
     * 打开打单个浏览器
     * @param fbAccount
     * @return
     */
    WebDriver openBrowser(FbAccount fbAccount);

    /**
     * 打开打单个浏览器
     * @param fbAccountForSell
     * @return
     */
    WebDriver openBrowserForAccountSellForTest(FbAccountForSell fbAccountForSell);


    /**
     * 打开打单个浏览器
     * @param fbAccountForSell
     * @return
     */
    WebDriver openBrowserForAccountSell(FbAccountForSell fbAccountForSell);

    /**
     * 关闭单个浏览器
     * @param fbAccount
     * @return
     */
    String closeBrowser(FbAccount fbAccount);

    /**
     * 显示浏览器
     * @param fbAccount
     */
    void showBrowser(FbAccount fbAccount);


    /**
     * 为邮箱打开浏览器
     * @param email
     * @return
     */
    WebDriver openBrowserForEmail(Email email);

    /**
     * 模拟按键，单键
     * @param keyName
     */
    public void simulateKeyPress(int keyName);

    /**
     * 模拟按键，双键
     * @param keyName1
     * @param keyName2
     */
    public void simulateKeyPress(int keyName1, int keyName2);


    /**
     * 通过页面代码获取xpath
     * @param pageSource
     * @param sourceCode
     * @return
     */
    public String getXpathBySourceCode(String pageSource, String sourceCode);

    /**
     * 模拟鼠标点击指定坐标位置
     * @param driver
     * @param x
     * @param y
     */
    public void clickAtCoordinates(WebDriver driver, int x, int y);

    /**
     * 等待时间
     * @param seconds
     */
    public void threadSleep(int seconds);
}
