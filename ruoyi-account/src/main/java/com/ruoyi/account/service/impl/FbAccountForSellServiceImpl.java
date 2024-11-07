package com.ruoyi.account.service.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ruoyi.account.domain.Posts;
import com.ruoyi.account.util.RandomUitl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.account.mapper.FbAccountForSellMapper;
import com.ruoyi.account.domain.FbAccountForSell;
import com.ruoyi.account.service.IFbAccountForSellService;

/**
 * 卖号Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-11-01
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
     * 导入数据
     *
     * @param FbAccountForSellList 数据列表
     * @param isUpdateSupport      是否更新支持，如果已存在，则进行更新数据
     * @param operName             操作用户
     * @return 结果
     */
    @Override
    public String importFbAccountForSell(List<FbAccountForSell> FbAccountForSellList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(FbAccountForSellList) || FbAccountForSellList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (FbAccountForSell fbAccountForSell : FbAccountForSellList) {
            try
            {
                // 验证是否存在这个用户
                FbAccountForSell f = fbAccountForSellMapper.selectFbAccountForSellById(fbAccountForSell.getId());
                if (StringUtils.isNull(f)) {
                    fbAccountForSell.setBrowserProfile(RandomUitl.generateRandomStringKey());
                    fbAccountForSell.setUa(RandomUitl.generateRandomPCUserAgent());
                    fbAccountForSellMapper.insertFbAccountForSell(fbAccountForSell);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、数据 " + fbAccountForSell.getId() + " 导入成功");
                }
                else if (isUpdateSupport) {
                    fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、数据 " + fbAccountForSell.getId() + " 更新成功");
                }
                else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、数据 " + fbAccountForSell.getId() + " 已存在");
                }
            }
            catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、数据 " + fbAccountForSell.getId() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    /**
     * 登录账号
     *
     * @param webDriver
     * @param fbAccountForSell
     */
    @Override
    public void loginFbAccountForSell(WebDriver webDriver, FbAccountForSell fbAccountForSell) {
        webDriver.get("https://www.facebook.com");
        // 清除所有 cookies
        webDriver.manage().window().maximize();
//        webDriver.manage().deleteAllCookies();
//        webDriver.navigate().refresh();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            WebDriverWait wait = new WebDriverWait(webDriver, 10, 1);
            WebElement email = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("email")));
            WebElement password = webDriver.findElement(By.name("pass"));
            try {
                email.click();
                // 创建Actions对象
                Actions actions = new Actions(webDriver);
                // 模拟Ctrl+A组合键
                actions.keyDown(Keys.CONTROL)
                        .sendKeys("a")
                        .keyUp(Keys.CONTROL)
                        .perform();
                Thread.sleep(500);
                actions.sendKeys(Keys.DELETE).perform();
                email.sendKeys(fbAccountForSell.getEmail());
                password.click();
                // 模拟Ctrl+A组合键
                actions.keyDown(Keys.CONTROL)
                        .sendKeys("a")
                        .keyUp(Keys.CONTROL)
                        .perform();
                Thread.sleep(500);
                actions.sendKeys(Keys.DELETE).perform();
                password.sendKeys(fbAccountForSell.getPassword());
            } catch (Exception e) {
                e.printStackTrace();
            }
            WebElement loginButton = webDriver.findElement(By.name("login"));
            loginButton.click();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String pageSource = webDriver.getPageSource();
            if (!pageSource.contains("账号或密码无效")){
                //新版双重验证码输入
                try {
                    WebElement approvalsCode = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/div/div[1]/div[1]/div/div[2]/div[2]/div/div/div/div/div[3]/div/form/div/div/div/div/div[1]/input")));
                    approvalsCode.sendKeys(getVerificationCode(fbAccountForSell.getSecretKey()));
                    WebElement submitButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/div/div[1]/div[1]/div/div[2]/div[2]/div/div/div/div/div[3]/div/div[1]/div/div/div/div[1]/div/span/span")));
                    submitButton.click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div[3]/div/div/div[1]/div[1]/div[2]/div/div/div[3]/div[1]/div/div/div/div[1]/div/span/span")))
                            .click();
                } catch (Exception e) {
                    try {
                        WebElement submitButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("checkpointSubmitButton")));
                        WebElement approvalsCode = webDriver.findElement(By.id("approvals_code"));
                        approvalsCode.sendKeys(getVerificationCode(fbAccountForSell.getSecretKey()));
                        submitButton.click();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }else {
                fbAccountForSell.setNote("账号或密码无效");
                fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取信息（简体）
     * @param webDriver
     * @param fbAccountForSell
     */
    @Override
    public void getAccountMarketplaceAndNameAndFriendInSimplified(WebDriver webDriver, FbAccountForSell fbAccountForSell) {

        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30, 1);
        webDriver.get("https://www.facebook.com/marketplace/ineligible/?ref=bookmark");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String pageSource = webDriver.getPageSource();
        if(pageSource.contains("你无法使用 Marketplace")){
            fbAccountForSell.setIsMarketplace("0");
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }else {
            fbAccountForSell.setIsMarketplace("1");
        }
        webDriver.get("https://www.facebook.com/"+fbAccountForSell.getId());
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='发布快拍']")));
        }catch (Exception e){
            e.printStackTrace();
        }
        //好有数量
        pageSource = webDriver.getPageSource();
        // 正则表达式匹配">数字 位朋友<"
        Pattern pattern = Pattern.compile("\\d+ 位好友");
        Matcher matcher = pattern.matcher(pageSource);

        while (matcher.find()) {
            // 输出找到的匹配项
            fbAccountForSell.setFriendNumber(matcher.group());
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }
        Document document = Jsoup.parse(pageSource);
        Element element = document.select("a[aria-label$=的时间线]").first();
        String name = element.attr("aria-label").replace("的时间线", "");

        // 提取第一个捕获组，即时间线前面的内容
        fbAccountForSell.setName(name);
        fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);


        //帖子数量
        webDriver.get("https://www.facebook.com/"+fbAccountForSell.getId()+"/allactivity?activity_history=false&category_key=YOURACTIVITYPOSTSSCHEMA&manage_mode=false&should_load_landing_page=false");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        pageSource = webDriver.getPageSource();
        // 定义正则表达式，匹配包含特定类名组合的文本
        pattern = Pattern.compile("x78zum5 x1q0g3np x1n2onr6");
        matcher = pattern.matcher(pageSource);

        // 统计匹配到的次数
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        if(count>0){
            fbAccountForSell.setPostsNumber(String.valueOf(count));
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }else {
            fbAccountForSell.setPostsNumber("0");
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }

        //账号状态，BM数量，主页数量
        webDriver.get("https://www.facebook.com/business-support-home/?landing_page=overview&source=link");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String currentUrl = webDriver.getCurrentUrl();
        if (!currentUrl.contains(fbAccountForSell.getId())) {
            try {
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()='Facebook account']")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            pageSource = webDriver.getPageSource();
            // 使用 Jsoup 解析 HTML 源代码
            document = Jsoup.parse(pageSource);

            // 使用 Jsoup 的选择器找到文本为 'Facebook account' 的 div 元素
            Element divElement = document.select("div:containsOwn(Facebook account)").first().parent();

            String divText = divElement.text().replaceAll("\\s+", "");
            if (divText.contains("帐户受限")) {
                fbAccountForSell.setCanAds("0");
                fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
            }else {
                fbAccountForSell.setCanAds("1");
                fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
            }
            pattern = Pattern.compile("编号");
            matcher = pattern.matcher(pageSource);

            int countBM = 0;
            while (matcher.find()) {
                countBM++;
            }
            fbAccountForSell.setBmNumber(String.valueOf(countBM));
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }else {
            fbAccountForSell.setBmNumber("0");
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }
        pageSource = webDriver.getPageSource();
        // 使用 Jsoup 解析 HTML 源代码
        document = Jsoup.parse(pageSource);
        // 查找具有特定 class 属性的 div 元素
        String text = document.select("div.x1iyjqo2.xs83m0k.xdl72j9.x3igimt.xedcshv.x1t2pt76.xyamay9.x1l90r2v.x1swvt13.x1pi30zi").get(0).text();
        if (text.contains("帐户受限")){
            fbAccountForSell.setCanAds("0");
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }
        webDriver.get("https://www.facebook.com/pages/?category=your_pages&ref=bookmarks");
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //主页数量
        pageSource = webDriver.getPageSource();
        pattern = Pattern.compile("/latest/inbox/all");
        matcher = pattern.matcher(pageSource);
        int countPage = 0;
        while (matcher.find()) {
            countPage++;
        }
        if (countPage>0){
            fbAccountForSell.setPageNumber(String.valueOf(countPage));
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }else {
            fbAccountForSell.setPageNumber("0");
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }
    }


    /**
     * 获取信息（繁体）
     * @param webDriver
     * @param fbAccountForSell
     */
    @Override
    public void getAccountMarketplaceAndNameAndFriendInTwTraditional(WebDriver webDriver, FbAccountForSell fbAccountForSell) {

        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30, 1);
        webDriver.get("https://www.facebook.com/marketplace/ineligible/?ref=bookmark");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String pageSource = webDriver.getPageSource();
        if(pageSource.contains("你目前無法使用 Marketplace")){
            fbAccountForSell.setIsMarketplace("0");
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }else {
            fbAccountForSell.setIsMarketplace("1");
        }
        webDriver.get("https://www.facebook.com/"+fbAccountForSell.getId());
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='新增到限時動態']")));
        }catch (Exception e){
            e.printStackTrace();
        }
        //好有数量
        pageSource = webDriver.getPageSource();
        // 正则表达式匹配">数字 位朋友<"
        Pattern pattern = Pattern.compile("\\d+ 位朋友");
        Matcher matcher = pattern.matcher(pageSource);

        while (matcher.find()) {
            // 输出找到的匹配项
            fbAccountForSell.setFriendNumber(matcher.group());
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }

        Document document = Jsoup.parse(pageSource);
        Element element = document.select("a[aria-label$=的動態時報]").first();
        String name = element.attr("aria-label").replace("的動態時報", "");
        // 提取第一个捕获组，即时间线前面的内容
        fbAccountForSell.setName(name);
        fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);


        //帖子数量
        webDriver.get("https://www.facebook.com/"+fbAccountForSell.getId()+"/allactivity?activity_history=false&category_key=YOURACTIVITYPOSTSSCHEMA&manage_mode=false&should_load_landing_page=false");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        pageSource = webDriver.getPageSource();
        // 定义正则表达式，匹配包含特定类名组合的文本
        pattern = Pattern.compile("x78zum5 x1q0g3np x1n2onr6");
        matcher = pattern.matcher(pageSource);

        // 统计匹配到的次数
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        if(count>0){
            fbAccountForSell.setPostsNumber(String.valueOf(count));
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }else {
            fbAccountForSell.setPostsNumber("0");
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }

        //账号状态，BM数量，主页数量
        webDriver.get("https://www.facebook.com/business-support-home/?landing_page=overview&source=link");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String currentUrl = webDriver.getCurrentUrl();
        if (!currentUrl.contains(fbAccountForSell.getId())) {
            try {
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()='Facebook 帳號']")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            pageSource = webDriver.getPageSource();
            // 使用 Jsoup 解析 HTML 源代码
            document = Jsoup.parse(pageSource);


            // 使用 Jsoup 的选择器找到文本为 'Facebook account' 的 div 元素
            Element divElement = document.select("div:containsOwn(Facebook 帳號)").first().parent();

            String divText = divElement.text();
            System.out.println("受限与否："+divText);
            if (divText.contains("帳號受到限制")) {
                fbAccountForSell.setCanAds("0");
                fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
            }else {
                fbAccountForSell.setCanAds("1");
                fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
            }
            pattern = Pattern.compile("編號");
            matcher = pattern.matcher(pageSource);

            int countBM = 0;
            while (matcher.find()) {
                countBM++;
            }
            fbAccountForSell.setBmNumber(String.valueOf(countBM));
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }else {
            fbAccountForSell.setBmNumber("0");
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }
        pageSource = webDriver.getPageSource();
        // 使用 Jsoup 解析 HTML 源代码
        document = Jsoup.parse(pageSource);
        // 查找具有特定 class 属性的 div 元素
        String text = document.select("div.x1iyjqo2.xs83m0k.xdl72j9.x3igimt.xedcshv.x1t2pt76.xyamay9.x1l90r2v.x1swvt13.x1pi30zi").get(0).text();
        if (text.contains("帳號受到限制")){
            fbAccountForSell.setCanAds("0");
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }
        webDriver.get("https://www.facebook.com/pages/?category=your_pages&ref=bookmarks");
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //主页数量
        pageSource = webDriver.getPageSource();
        pattern = Pattern.compile("/latest/inbox/all");
        matcher = pattern.matcher(pageSource);
        int countPage = 0;
        while (matcher.find()) {
            countPage++;
        }
        if (countPage>0){
            fbAccountForSell.setPageNumber(String.valueOf(countPage));
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }else {
            fbAccountForSell.setPageNumber("0");
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }
    }


    /**
     * 获取信息（繁体）
     * @param webDriver
     * @param fbAccountForSell
     */
    @Override
    public void getAccountMarketplaceAndNameAndFriendInHkTraditional(WebDriver webDriver, FbAccountForSell fbAccountForSell) {

        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30, 1);
        webDriver.get("https://www.facebook.com/marketplace/ineligible/?ref=bookmark");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String pageSource = webDriver.getPageSource();
        if(pageSource.contains("你目前無法使用 Marketplace")){
            fbAccountForSell.setIsMarketplace("0");
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }else {
            fbAccountForSell.setIsMarketplace("1");
        }
        webDriver.get("https://www.facebook.com/"+fbAccountForSell.getId());
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='新增到限時動態']")));
        }catch (Exception e){
            e.printStackTrace();
        }
        //好有数量
        pageSource = webDriver.getPageSource();
        // 正则表达式匹配">数字 位朋友<"
        Pattern pattern = Pattern.compile("\\d+ 位朋友");
        Matcher matcher = pattern.matcher(pageSource);

        while (matcher.find()) {
            // 输出找到的匹配项
            fbAccountForSell.setFriendNumber(matcher.group());
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }

        Document document = Jsoup.parse(pageSource);
        Element element = document.select("a[aria-label$=的生活時報]").first();
        String name = element.attr("aria-label").replace("的生活時報", "");

        // 提取第一个捕获组，即时间线前面的内容
        fbAccountForSell.setName(name);
        fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);

        //帖子数量
        webDriver.get("https://www.facebook.com/"+fbAccountForSell.getId()+"/allactivity?activity_history=false&category_key=YOURACTIVITYPOSTSSCHEMA&manage_mode=false&should_load_landing_page=false");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        pageSource = webDriver.getPageSource();
        // 定义正则表达式，匹配包含特定类名组合的文本
        pattern = Pattern.compile("x78zum5 x1q0g3np x1n2onr6");
        matcher = pattern.matcher(pageSource);

        // 统计匹配到的次数
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        if(count>0){
            fbAccountForSell.setPostsNumber(String.valueOf(count));
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }else {
            fbAccountForSell.setPostsNumber("0");
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }

        //账号状态，BM数量，主页数量
        webDriver.get("https://www.facebook.com/business-support-home/?landing_page=overview&source=link");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String currentUrl = webDriver.getCurrentUrl();
        if (!currentUrl.contains(fbAccountForSell.getId())) {
            try {
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()='Facebook 帳戶']")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            pageSource = webDriver.getPageSource();
            // 使用 Jsoup 解析 HTML 源代码
            document = Jsoup.parse(pageSource);

            // 使用 Jsoup 的选择器找到文本为 'Facebook account' 的 div 元素
            Element divElement = document.select("div:containsOwn(Facebook 帳戶)").first().parent();

            String divText = divElement.text();
            System.out.println("受限与否："+divText);
            if (divText.contains("受到限制的帳戶")) {
                fbAccountForSell.setCanAds("0");
                fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
            }else {
                fbAccountForSell.setCanAds("1");
                fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
            }
            pattern = Pattern.compile("編號");
            matcher = pattern.matcher(pageSource);

            int countBM = 0;
            while (matcher.find()) {
                countBM++;
            }
            fbAccountForSell.setBmNumber(String.valueOf(countBM));
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }else {
            fbAccountForSell.setBmNumber("0");
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }
        pageSource = webDriver.getPageSource();
        // 使用 Jsoup 解析 HTML 源代码
        document = Jsoup.parse(pageSource);
        // 查找具有特定 class 属性的 div 元素
        String text = document.select("div.x1iyjqo2.xs83m0k.xdl72j9.x3igimt.xedcshv.x1t2pt76.xyamay9.x1l90r2v.x1swvt13.x1pi30zi").get(0).text();
        if (text.contains("受到限制的帳戶")){
            fbAccountForSell.setCanAds("0");
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }
        webDriver.get("https://www.facebook.com/pages/?category=your_pages&ref=bookmarks");
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //主页数量
        pageSource = webDriver.getPageSource();
        pattern = Pattern.compile("/latest/inbox/all");
        matcher = pattern.matcher(pageSource);
        int countPage = 0;
        while (matcher.find()) {
            countPage++;
        }
        if (countPage>0){
            fbAccountForSell.setPageNumber(String.valueOf(countPage));
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }else {
            fbAccountForSell.setPageNumber("0");
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }
    }


    /**
     * 获取信息（英语）
     * @param webDriver
     * @param fbAccountForSell
     */
    @Override
    public void getAccountMarketplaceAndNameAndFriendInEnglish(WebDriver webDriver, FbAccountForSell fbAccountForSell) {

        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30, 1);
        webDriver.get("https://www.facebook.com/marketplace/ineligible/?ref=bookmark");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String pageSource = webDriver.getPageSource();
        if(pageSource.contains("Marketplace isn't available to you")){
            fbAccountForSell.setIsMarketplace("0");
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }else {
            fbAccountForSell.setIsMarketplace("1");
        }
        webDriver.get("https://www.facebook.com/"+fbAccountForSell.getId());
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Add to story']")));
        }catch (Exception e){
            e.printStackTrace();
        }
        //好有数量
        pageSource = webDriver.getPageSource();
        // 正则表达式匹配">数字 位朋友<"
        Pattern pattern = Pattern.compile("\\d+ friends");
        Matcher matcher = pattern.matcher(pageSource);

        while (matcher.find()) {
            // 输出找到的匹配项
            fbAccountForSell.setFriendNumber(matcher.group());
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }

        Document document = Jsoup.parse(pageSource);
        Element element =  document.select("a[aria-label$='s Timeline']").first();
        String name = element.attr("aria-label").replace("'s timeline", "");

        // 提取第一个捕获组，即时间线前面的内容
        fbAccountForSell.setName(name);
        fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);

        //帖子数量
        webDriver.get("https://www.facebook.com/"+fbAccountForSell.getId()+"/allactivity?activity_history=false&category_key=YOURACTIVITYPOSTSSCHEMA&manage_mode=false&should_load_landing_page=false");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        pageSource = webDriver.getPageSource();
        // 定义正则表达式，匹配包含特定类名组合的文本
        pattern = Pattern.compile("x78zum5 x1q0g3np x1n2onr6");
        matcher = pattern.matcher(pageSource);

        // 统计匹配到的次数
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        if(count>0){
            fbAccountForSell.setPostsNumber(String.valueOf(count));
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }else {
            fbAccountForSell.setPostsNumber("0");
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }

        //账号状态，BM数量，主页数量
        webDriver.get("https://www.facebook.com/business-support-home/?landing_page=overview&source=link");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String currentUrl = webDriver.getCurrentUrl();
        if (!currentUrl.contains(fbAccountForSell.getId())) {
            try {
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()='Facebook account']")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            pageSource = webDriver.getPageSource();
            // 使用 Jsoup 解析 HTML 源代码
            document = Jsoup.parse(pageSource);

            // 使用 Jsoup 的选择器找到文本为 'Facebook account' 的 div 元素
            Element divElement = document.select("div:containsOwn(Facebook account)").first().parent();

            String divText = divElement.text();
            System.out.println("受限与否："+divText);
            if (divText.contains("Account restricted")) {
                fbAccountForSell.setCanAds("0");
                fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
            }else {
                fbAccountForSell.setCanAds("1");
                fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
            }
            Element bmDivElement = document.select("div:containsOwn(Business accounts)").first().parent();
            pattern = Pattern.compile("ID");
            matcher = pattern.matcher(bmDivElement.text());

            int countBM = 0;
            while (matcher.find()) {
                countBM++;
            }
            fbAccountForSell.setBmNumber(String.valueOf(countBM));
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);

        }else {
            fbAccountForSell.setBmNumber("0");
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }
        pageSource = webDriver.getPageSource();
        // 使用 Jsoup 解析 HTML 源代码
        document = Jsoup.parse(pageSource);
        // 查找具有特定 class 属性的 div 元素
        String text = document.select("div.x1iyjqo2.xs83m0k.xdl72j9.x3igimt.xedcshv.x1t2pt76.xyamay9.x1l90r2v.x1swvt13.x1pi30zi").get(0).text();
        if (text.contains("Account restricted")){
            fbAccountForSell.setCanAds("0");
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }
        webDriver.get("https://www.facebook.com/pages/?category=your_pages&ref=bookmarks");
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //主页数量
        pageSource = webDriver.getPageSource();
        pattern = Pattern.compile("/latest/inbox/all");
        matcher = pattern.matcher(pageSource);
        int countPage = 0;
        while (matcher.find()) {
            countPage++;
        }
        if (countPage>0){
            fbAccountForSell.setPageNumber(String.valueOf(countPage));
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }else {
            fbAccountForSell.setPageNumber("0");
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }
    }

    /**
     * 获取账号语言
     * @param webDriver
     * @param fbAccountForSell
     * @return
     */
    @Override
    public String getAccountLanguage(WebDriver webDriver, FbAccountForSell fbAccountForSell) {
        webDriver.get("https://www.facebook.com/settings/?tab=language_and_region");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String pageSource = webDriver.getPageSource();
        Document document = Jsoup.parse(pageSource);
        String text = document.select("div[role=main]").first().text();
        //English  中文(台灣)  中文(香港)   中文(简体)
        if (text.contains("English")){
            return "English";
        }else if (text.contains("中文(台灣)")){
            return "中文(台灣)";
        }else if (text.contains("中文(香港)")){
            return "中文(香港)";
        }else if (text.contains("中文(简体)")){
            return "中文(简体)";
        }else {
            return "未知语言";
        }
    }

    /**
     * 查询账号
     *
     * @param ids
     * @return
     */
    @Override
    public List<FbAccountForSell> selectFbAccountForSellListByAccountIds(Long[] ids) {
        return fbAccountForSellMapper.selectFbAccountForSellListByAccountIds(ids);
    }


    /**
     * 获取双重验证
     *
     * @param secretKey
     * @return
     */
    public String getVerificationCode(String secretKey) {
        // 创建Google Authenticator实例
        GoogleAuthenticator gAuth = new GoogleAuthenticator();

        // 生成当前时间对应的TOTP密码
        int totpPassword = gAuth.getTotpPassword(secretKey);

        // 将TOTP密码格式化为字符串，确保不以0开头
        String formattedCode = String.format("%06d", totpPassword);

        return formattedCode;
    }


    /**
     * 判断是否在线
     *
     * @param webDriver
     * @return
     */
    @Override
    public String isLogin(WebDriver webDriver) {
        boolean hasCUser = false;
        boolean hasPresence = false;
        Set<Cookie> cookies = webDriver.manage().getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("c_user")) {
                hasCUser = true; // 找到了c_user项
            }
            if (cookie.getName().equals("presence")) {
                hasPresence = true;
            }
        }

        if (hasCUser && hasPresence) {
            return "true";
        }
        if (hasCUser && !hasPresence) {
            return "disable";
        }
        if (!hasCUser && !hasPresence) {
            return "error";
        }
        if (!hasCUser) {
            return "false";
        }
        return "error";
    }


}
