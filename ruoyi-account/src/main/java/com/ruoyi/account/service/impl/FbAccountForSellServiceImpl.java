package com.ruoyi.account.service.impl;


import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ruoyi.account.domain.*;
import com.ruoyi.account.service.*;
import com.ruoyi.account.util.*;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.ruoyi.account.mapper.FbAccountForSellMapper;


/**
 * 卖号Service业务层处理
 *
 * @author ruoyi
 * @date 2024-11-01
 */
@Component
@Service
public class FbAccountForSellServiceImpl implements IFbAccountForSellService {

    @Autowired
    private FbAccountForSellMapper fbAccountForSellMapper;

    @Autowired
    private ISeleniumService seleniumService;

    @Autowired
    private IPostsService postsService;

    @Autowired
    private IOperationLogService operationLogService;

    @Autowired
    private IProxyIpService proxyIpService;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private IAccountNameService accountNameService;





    //webDriver集合
    HashMap<String,WebDriver> webDriverMap = new HashMap<>();

    //进程集合
    Map<String, Integer> processMap = new HashMap<>();

    @Scheduled(cron = "*/1 * * * * ?")
    public void cleanUpWebDrivers() {

        webDriverMap.entrySet().removeIf(entry -> {
            String id = entry.getKey();
            WebDriver webDriver = entry.getValue();
            try {
                webDriver.getTitle(); // 尝试调用以检测浏览器是否仍然活动
                return false;
            } catch (Exception e) {
                try {
                    webDriver.quit(); // 确保关闭已挂的浏览器实例
                } catch (Exception ignored) {

                }
                return true; // 如果调用失败，移除该实例
            }
        });

        // 清理 processMap
        processMap.entrySet().removeIf(entry -> {
            String id = entry.getKey();
            Integer processId = entry.getValue();
            try {
                // 检测进程是否仍然有效，例如通过系统调用
                if (seleniumService.isProcessAlive(processId)) {
                    return false; // 进程仍然有效
                }
            } catch (Exception ignored) {
            }
            return true; // 移除无效进程
        });

    }

    /**
     * 查询卖号
     *
     * @param keyId 卖号主键
     * @return 卖号
     */
    @Override
    public FbAccountForSell selectFbAccountForSellByKeyId(Long keyId) {
        return fbAccountForSellMapper.selectFbAccountForSellByKeyId(keyId);
    }

    /**
     * 查询卖号
     *
     * @param id 卖号id
     * @return 卖号
     */
    @Override
    public FbAccountForSell selectFbAccountForSellById(String id) {
        return fbAccountForSellMapper.selectFbAccountForSellById(id);
    }

    /**
     * 查询卖号
     *
     * @param email 邮箱
     * @return 卖号
     */
    @Override
    public FbAccountForSell selectFbAccountForSellByEmail(String email) {
        return fbAccountForSellMapper.selectFbAccountForSellByEmail(email);
    }

    /**
     * 查询卖号列表
     *
     * @param fbAccountForSell 卖号
     * @return 卖号
     */
    @Override
    public List<FbAccountForSell> selectFbAccountForSellList(FbAccountForSell fbAccountForSell) {
        return fbAccountForSellMapper.selectFbAccountForSellList(fbAccountForSell);
    }

    /**
     * 查询卖号列表
     *
     * @param fbAccountForSell 卖号
     * @return 卖号集合
     */
    @Override
    public List<FbAccountForSell> selectFbAccountForSellListNoId(FbAccountForSell fbAccountForSell) {
        return fbAccountForSellMapper.selectFbAccountForSellListNoId(fbAccountForSell);
    }

    /**
     * 新增卖号
     *
     * @param fbAccountForSell 卖号
     * @return 结果
     */
    @Override
    public int insertFbAccountForSell(FbAccountForSell fbAccountForSell) {
        return fbAccountForSellMapper.insertFbAccountForSell(fbAccountForSell);
    }

    /**
     * 修改卖号
     *
     * @param fbAccountForSell 卖号
     * @return 结果
     */
    @Override
    public int updateFbAccountForSell(FbAccountForSell fbAccountForSell) {
        return fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
    }

    /**
     * 批量删除卖号
     *
     * @param keyIds 需要删除的卖号主键
     * @return 结果
     */
    @Override
    public int deleteFbAccountForSellByKeyIds(Long[] keyIds) {
        return fbAccountForSellMapper.deleteFbAccountForSellByKeyIds(keyIds);
    }

    /**
     * 删除卖号信息
     *
     * @param keyId 卖号主键
     * @return 结果
     */
    @Override
    public int deleteFbAccountForSellByKeyId(Long keyId) {
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
            try {
                // 验证是否存在这个用户
                FbAccountForSell f = fbAccountForSellMapper.selectFbAccountForSellById(fbAccountForSell.getId());
                Email email = new Email();
                if (StringUtils.isNull(f)) {
                    fbAccountForSell.setBrowserProfile(RandomUitl.generateRandomFilename());
                    fbAccountForSell.setUa(RandomUitl.generateRandomPCUserAgent());
                    fbAccountForSell.setFilePath("E:\\\\browsertemp\\\\");
                    int i = insertFbAccountForSell(fbAccountForSell);
                    if (i > 0) {
                        email.setEmail(fbAccountForSell.getEmail());
                        email.setAccountId(fbAccountForSell.getId());
                        email.setPassword(fbAccountForSell.getEmailPassword());
                        emailService.insertEmail(email);
                    }
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、数据 " + fbAccountForSell.getId() + " 导入成功");
                } else if (isUpdateSupport) {
                    updateFbAccountForSell(fbAccountForSell);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、数据 " + fbAccountForSell.getId() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、数据 " + fbAccountForSell.getId() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、数据 " + fbAccountForSell.getId() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
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
    public String loginFbAccountForSell(WebDriver webDriver, FbAccountForSell fbAccountForSell) {
        // 如果已经登录，直接返回
        /*if (isLogin(webDriver, fbAccountForSell)){
            System.out.println("打开浏览器时账号登录状态：OK");
            return "";
        }*/
        webDriver.manage().window().maximize();
        WebDriverWait wait = new WebDriverWait(webDriver, 20, 1);

        // 打开 Facebook 首页
        webDriver.get("https://www.facebook.com/profile.php?id="+fbAccountForSell.getId());
        seleniumService.threadSleep(5);

        /*// 尝试使用 cookie 登录，如果成功直接返回
        if (useCookieLogin(webDriver, fbAccountForSell)){
            System.out.println("使用cookie登录：成功");
            return "";
        }*/

        if (!webDriver.getPageSource().contains("忘記帳號")){
            if(isLogin(webDriver, fbAccountForSell)) return "";
        }


        if (webDriver.getPageSource().contains("忘記帳號") || webDriver.getPageSource().contains("忘记账户了")
                || webDriver.getPageSource().contains("Forgot Account?")){
            webDriver.findElement(By.name("email")).sendKeys(Keys.ENTER);
        }


        // 等待页面加载完成
        seleniumService.threadSleep(2);
        for (int i = 0; i < 180; i++) {
            String pageSource = webDriver.getPageSource();
            if (pageSource.contains("通过你设置的双重验证应用（例如 Duo Mobile 或 Google 身份验证器）为这个账户输入 6 位数验证码")
                || pageSource.contains("為此帳號輸入你從所設定的雙重驗證應用程式（例如 Duo Mobile 或 Google Authenticator）收到的 6 位數驗證碼。")
                || pageSource.contains("such as Duo Mobile or Google Authenticator")
                || pageSource.contains("為此帳戶輸入你從所設定的雙重驗證應用程式（例如 Duo Mobile 或 Google Authenticator）收到的 6 位數驗證碼。")) {
                // 获取当前页面 HTML
                Document document = Jsoup.parse(webDriver.getPageSource());

                // 检查登录过程中是否出现异常（密码错误、验证码等）
                if (handleLoginErrors(webDriver, fbAccountForSell, document)) return "";

                // 处理双重验证（两步验证码）
                handleTwoFactorVerification(webDriver, fbAccountForSell, wait, document);
                break;
            }
            seleniumService.threadSleep(1);
        }

        if (webDriver.getPageSource().contains("這有助於我們打擊有害行為、偵測並防止垃圾訊息，以及維護我們產品的信譽。")
                || webDriver.getPageSource().contains("This helps us to combat harmful conduct, detect and prevent spam and maintain the integrity of our Products.")
                || webDriver.getPageSource().contains("这有助于打击有害行为，检测和防止垃圾信息，并维护我们产品的诚信。"))
            return "";

        // 获取当前页面 HTML
        Document document = Jsoup.parse(webDriver.getPageSource());

        // 检查登录过程中是否出现异常（密码错误、验证码等）
        if (handleLoginErrors(webDriver, fbAccountForSell, document)) return "";

        // 处理双重验证（两步验证码）
        handleTwoFactorVerification(webDriver, fbAccountForSell, wait, document);

        return "";
    }

    /**
     * 使用已保存的 cookie 尝试登录
     */
    private boolean useCookieLogin(WebDriver driver, FbAccountForSell account) {
        if (account.getCookie() == null || account.getCookie().isEmpty()) return false;
        // 将 cookie 添加到浏览器
        JsonDataUtil.addCookiesToDriver(driver, account.getCookie());
        driver.navigate().refresh();
        // 判断是否登录成功
        return isLogin(driver, account);
    }

    /**
     * 输入账号和密码，模拟人工逐字符输入
     */
    private boolean enterCredentials(WebDriver driver, WebDriverWait wait, FbAccountForSell account) {
        try {
            // 等待邮箱输入框加载完成
            wait.until(ExpectedConditions.presenceOfElementLocated(By.name("email")));
            WebElement email = driver.findElement(By.name("email"));
            WebElement password = driver.findElement(By.name("pass"));

            Actions actions = new Actions(driver);

            // 模拟输入账号和密码
            typeWithDelay(actions, email, account.getId(), 0.1);
            typeWithDelay(actions, password, account.getPassword(), 0.1);

            seleniumService.threadSleep(2); // 等待输入完成
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 模拟逐字符输入，同时先清空输入框
     */
    private void typeWithDelay(Actions actions, WebElement element, String text, double delayMs) throws InterruptedException {
        element.click();
        // Ctrl+A 全选并删除
        actions.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
        seleniumService.threadSleep(0.5);
        actions.sendKeys(Keys.DELETE).perform();
        // 逐字符输入
        for (char c : text.toCharArray()) {
            element.sendKeys(String.valueOf(c));
            seleniumService.threadSleep(delayMs);
        }
    }

    /**
     * 点击登录按钮，支持不同文本的登录按钮
     */
    private void clickLoginButton(WebDriver driver, WebDriverWait wait) {
        try {
            driver.findElement(By.name("login")).click();
        } catch (Exception e) {
            // 兼容不同语言或 UI 文本的登录按钮
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//span[text()='登录' or text()='Log in' or text()='登入']"))).click();
        }
    }

    /**
     * 检查登录错误，如密码错误、需要验证码、改过密码等
     */
    private boolean handleLoginErrors(WebDriver driver, FbAccountForSell account, Document doc) {
        String pageSource = driver.getPageSource();

        if (driver.getCurrentUrl().contains("privacy_mutation_token")) {
            setLoginFailed(account, "无法登录-改过密码");
            return true;
        }
        if (pageSource.contains("输入你看到的验证码")) {
            account.setNote("无法登录-需要输入验证码");
            updateFbAccountForSell(account);
            return true;
        }
        if (pageSource.contains("账号或密码无效") || pageSource.contains("你输入的是旧密码")) {
            setLoginFailed(account, "无法登录-账号或密码无效/改过密码");
            return true;
        }
        return false;
    }

    /**
     * 设置账号登录失败状态
     */
    private void setLoginFailed(FbAccountForSell account, String note) {
        account.setNote(note);
        account.setCanLogin("0");
        updateFbAccountForSell(account);
    }

    /**
     * 处理两步验证码逻辑
     */
    private void handleTwoFactorVerification(WebDriver driver, FbAccountForSell account, WebDriverWait wait, Document doc) {
        try {
            // 新版双重验证码输入
            if (doc.select("#approvals_code").first() != null) {
                submitTwoFactorCode(driver, account, wait, "#approvals_code");
            } else if (doc.select("input[type=text]").first() != null) {
                submitTwoFactorCode(driver, account, wait, "input[type=text]");
            }
            // 处理单按钮多步骤验证流程
            if (doc.select("[role=button]").size() == 1) {
                handleSingleButtonFlow(driver, account, wait);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交两步验证码
     */
    private void submitTwoFactorCode(WebDriver driver, FbAccountForSell account, WebDriverWait wait, String selector) throws InterruptedException {
        WebElement codeInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(selector)));
        codeInput.sendKeys(FBAccountUtil.getGoogleVerificationCode(account.getSecretKey()));
        seleniumService.threadSleep(1.2);
        codeInput.sendKeys(Keys.ENTER);

        // 等待页面跳转完成
        for (int i = 0; i < 10 && driver.getCurrentUrl().contains("two_step_verification"); i++) {
            seleniumService.threadSleep(1);
        }

        driver.get("https://www.facebook.com");

        // 登录成功后更新 cookie
        if (isLogin(driver, account)) {
            account.setCookie(JsonDataUtil.getCookiesAsString(driver));
            updateFbAccountForSell(account);
        }
    }

    /**
     * 处理单按钮的多步骤验证流程
     */
    private void handleSingleButtonFlow(WebDriver driver, FbAccountForSell account, WebDriverWait wait) throws InterruptedException {
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@role='button']")));
        button.click();
        seleniumService.threadSleep(1.2);

        // 选择第一个单选项
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[type='radio'][value='1']"))).click();

        // 点击页面最后一个元素父元素
        List<WebElement> elements = driver.findElements(By.xpath("//div[@data-visualcompletion='ignore']"));
        elements.get(elements.size() - 1).findElement(By.xpath("..")).click();

        // 提交两步验证码
        submitTwoFactorCode(driver, account, wait, "input[type='text']");
    }


    /**
     * 获取信息（简体）
     *
     * @param webDriver
     * @param fbAccount
     */
    @Override
    public void getAccountNameAndFriendNumber(WebDriver webDriver,
                                              FbAccountForSell fbAccount) {

        WebDriverWait wait = new WebDriverWait(webDriver, 30,1);

        // 1️⃣ 打开个人主页
        webDriver.get("https://www.facebook.com/" + fbAccount.getId());

        // 等待关键资源加载
        seleniumService.waitingForContent(
                30,
                webDriver,
                "static.xx.fbcdn.net"
        );

        String pageSource = webDriver.getPageSource();

        // 2️⃣ 解析姓名
        parseAccountName(pageSource, fbAccount);

        // 3️⃣ 获取好友数量
        parseFriendNumber(wait, fbAccount);

        // 4️⃣ 获取性别（仅当为空时）
        if (isBlank(fbAccount.getGender())) {
            parseGender(webDriver, fbAccount);
        }

        // 5️⃣ 统一更新数据库（只更新一次）
        updateFbAccountForSell(fbAccount);
    }






    /**
     * 获取信息（简体）
     *
     * @param webDriver
     * @param fbAccountForSell
     */
    @Override
    public void getIsMarketplace(WebDriver webDriver, FbAccountForSell fbAccountForSell) {
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 5, 1);
        webDriver.get("https://facebook.com/marketplace/?ref=bookmark");
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/notifications/']")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (webDriver.getCurrentUrl().contains("ineligible")) {
            fbAccountForSell.setIsMarketplace("0");
            updateFbAccountForSell(fbAccountForSell);
        } else {
            fbAccountForSell.setIsMarketplace("1");
            updateFbAccountForSell(fbAccountForSell);
        }
    }

    /**
     * 获取个人广告账户状态（简体）
     *
     * @param webDriver
     * @param fbAccountForSell
     */
    @Override
    public void getAccountAdStatus(WebDriver webDriver, FbAccountForSell fbAccountForSell) {

        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30);
        webDriver.get("https://www.facebook.com/business-support-home/"+fbAccountForSell.getId()+"/?source=link");
        for (int i = 0; i < 5; i++) {
            boolean b = seleniumService.waitingForContent(5, webDriver, fbAccountForSell.getName());
            if (b){
                break;
            }
            webDriver.navigate().refresh();
            seleniumService.threadSleep(1);
        }
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()='" + fbAccountForSell.getName() + "']")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        seleniumService.threadSleep(3);
        String pageSource = webDriver.getPageSource();

        if (pageSource.contains("帳號受到限制") ||
                pageSource.contains("Account restricted") ||
                pageSource.contains("受到限制的帳戶") ||
                pageSource.contains("账户受限")) {
            fbAccountForSell.setCanAds("0");
        }else {
            fbAccountForSell.setCanAds("1");
        }
        updateFbAccountForSell(fbAccountForSell);

        if(pageSource.contains("沒有禁止刊登的廣告")||
                pageSource.contains("No ads rejected")||
                pageSource.contains("没有广告被拒")){
            fbAccountForSell.setAdAccountStatus("1");
        }else {
            fbAccountForSell.setAdAccountStatus("0");
        }
        updateFbAccountForSell(fbAccountForSell);
    }

    /**
     * 获取信息（简体）
     *
     * @param webDriver
     * @param fbAccountForSell
     */
    @Override
    public void getAccountBm(WebDriver webDriver, FbAccountForSell fbAccountForSell) {

        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30);
        //BM数量
        try {
            webDriver.get("https://www.facebook.com/business-support-home/?landing_page=overview&source=link");
            try {
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()='" + fbAccountForSell.getName() + "']")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String currentUrl = webDriver.getCurrentUrl();
            if (!currentUrl.contains(fbAccountForSell.getId())) {
                List<WebElement> elements = webDriver.findElements(By.xpath("//a[contains(@href, '/business-support-home/') and contains(@href, '?source=link')]"));
                int countBM = 0;

                for (WebElement el : elements) {
                    String href = el.getAttribute("href");

                    // 提取 ID（15位或更多数字）
                    Pattern pattern = Pattern.compile("/business-support-home/(\\d{15,})");
                    Matcher matcher = pattern.matcher(href);

                    if (matcher.find()) {
                        String id = matcher.group(1); // 提取的 ID
                        String text = el.getText();

                        if (text.contains(id)) {
                            countBM++;
                            System.out.println("匹配成功，ID: " + id);
                        } else {
                            System.out.println("跳过，无匹配编号: " + id);
                        }
                    } else {
                        System.out.println("未在 href 中匹配到 ID：" + href);
                    }
                    fbAccountForSell.setBmNumber(String.valueOf(countBM));
                    updateFbAccountForSell(fbAccountForSell);
                }
            } else {
                fbAccountForSell.setBmNumber("0");
                updateFbAccountForSell(fbAccountForSell);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 主页
     *
     * @param webDriver
     * @param fbAccountForSell
     */
    @Override
    public void getAccountPage(WebDriver webDriver, FbAccountForSell fbAccountForSell){
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30);
        //主页数量
        webDriver.get("https://www.facebook.com/pages/?category=your_pages&ref=bookmarks");
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/pages/?category=invites&ref=bookmarks']")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String pageSource = webDriver.getPageSource();
        Pattern pattern = Pattern.compile("/latest/inbox/all");
        Matcher matcher = pattern.matcher(pageSource);
        int countPage = 0;
        while (matcher.find()) {
            countPage++;
        }
        if (countPage > 0) {
            fbAccountForSell.setPageNumber(String.valueOf(countPage));
            updateFbAccountForSell(fbAccountForSell);
        } else {
            fbAccountForSell.setPageNumber("0");
            updateFbAccountForSell(fbAccountForSell);
        }
    }

    /**
     * 获取帖子数量（简体）
     *
     * @param webDriver
     * @param fbAccountForSell
     */
    @Override
    public void getAccountPostCount(WebDriver webDriver, FbAccountForSell fbAccountForSell) {
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30, 1);
        //帖子数量
        try {
            webDriver.get("https://www.facebook.com/" + fbAccountForSell.getId() + "/allactivity?activity_history=false&category_key=YOURACTIVITYPOSTSSCHEMA&manage_mode=false&should_load_landing_page=false");
            try {
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(text(),'" + fbAccountForSell.getName() + "')]")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<WebElement> elements = null;
            try {
                elements = webDriver.findElements(By.xpath("//a[@href='https://www.facebook.com/profile.php?id="+fbAccountForSell.getId()+"' and normalize-space(text())='"+fbAccountForSell.getName()+"']"));
                fbAccountForSell.setPostsNumber(String.valueOf(elements.size()));
            } catch (Exception e) {
                fbAccountForSell.setPostsNumber(String.valueOf(0));
                e.printStackTrace();
            }
            updateFbAccountForSell(fbAccountForSell);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取信息（简体）
     *
     * @param webDriver
     * @param fbAccountForSell
     */
    @Override
    public void getAccountLoginLog(WebDriver webDriver, FbAccountForSell fbAccountForSell) {

        //登录记录x
        try {
            webDriver.get("https://www.facebook.com/"+fbAccountForSell.getId()+"/allactivity/?category_key=LOGINSLOGOUTS&entry_point=ayi_hub");
            seleniumService.waitingForContent(10,webDriver, "https://static.xx.fbcdn.net/rsrc.php/v4/yu/r/5cE1svsoOSp.png");
            // 定义正则表达式
            String loginLog = webDriver.getPageSource();
            // 提取中文格式：2024年6月1日
            // 1. 中文格式：2025年7月2日
            Pattern zhDatePattern = Pattern.compile("\\d{4}年\\d{1,2}月\\d{1,2}日");
            Matcher zhMatcher = zhDatePattern.matcher(loginLog);

            List<String> allDates = new ArrayList<>();

            while (zhMatcher.find()) {
                allDates.add(zhMatcher.group());
            }

            // 2. 美式格式：July 2, 2025
            Pattern usDatePattern = Pattern.compile("([A-Z][a-z]+)\\s(\\d{1,2}),\\s(\\d{4})");
            Matcher usMatcher = usDatePattern.matcher(loginLog);

            while (usMatcher.find()) {
                String monthName = usMatcher.group(1);
                String day = usMatcher.group(2);
                String year = usMatcher.group(3);
                int month = parseEnglishMonth(monthName);
                String formatted = String.format("%s年%d月%d日", year, month, Integer.parseInt(day));
                allDates.add(formatted);
            }

            // 3. 英式格式：2 July 2025
            Pattern ukDatePattern = Pattern.compile("(\\d{1,2})\\s([A-Z][a-z]+)\\s(\\d{4})");
            Matcher ukMatcher = ukDatePattern.matcher(loginLog);

            while (ukMatcher.find()) {
                String day = ukMatcher.group(1);
                String monthName = ukMatcher.group(2);
                String year = ukMatcher.group(3);
                int month = parseEnglishMonth(monthName);
                String formatted = String.format("%s年%d月%d日", year, month, Integer.parseInt(day));
                allDates.add(formatted);
            }

            if (!allDates.isEmpty()) {
                fbAccountForSell.setLastPostsTime(String.join(";", allDates));
                updateFbAccountForSell(fbAccountForSell);
            } else {
                System.err.println("未识别到任何登录日期，账号：" + fbAccountForSell.getId());
            }

        } catch (Exception e) {
            System.err.println("获取账号 [" + fbAccountForSell.getId() + "] 登录记录出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取信息（简体）
     *
     * @param webDriver
     * @param fbAccountForSell
     */
    public void getAccountMarketplaceAndNameAndFriendInSimplified(WebDriver webDriver, FbAccountForSell fbAccountForSell) {

        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30, 1);
        webDriver.get("https://facebook.com/marketplace/?ref=bookmark");
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/notifications/']")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (webDriver.getCurrentUrl().contains("ineligible")) {
            fbAccountForSell.setCanLogin("1");
            fbAccountForSell.setIsMarketplace("0");
            updateFbAccountForSell(fbAccountForSell);
        } else {
            fbAccountForSell.setCanLogin("1");
            fbAccountForSell.setIsMarketplace("1");
            updateFbAccountForSell(fbAccountForSell);
        }
        webDriver.get("https://www.facebook.com/" + fbAccountForSell.getId());
        String target = "https://static.xx.fbcdn.net/rsrc.php/v4/yz/r/AqoGWewwdNN.png";
        seleniumService.waitingForContent(30, webDriver, target);

        String pageSource = webDriver.getPageSource();

        //账号名字
        String regex = "\"USER_ID\":\"" + fbAccountForSell.getId() + "\".*?\"NAME\":\"(.*?)\"";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pageSource);
        // 尝试查找匹配的 NAME 值
        if (matcher.find()) {
            String name = matcher.group(1);

            // 手动进行 Unicode 解码，将 uXXXX 形式的编码转换为对应的字符
            StringBuilder decodedNameBuilder = new StringBuilder();
            Matcher unicodeMatcher = Pattern.compile("\\\\u([0-9a-fA-F]{4})").matcher(name);
            int lastEnd = 0;
            while (unicodeMatcher.find()) {
                // 将前面的部分追加到结果中
                decodedNameBuilder.append(name, lastEnd, unicodeMatcher.start());
                // 解码当前 uXXXX 部分
                int charCode = Integer.parseInt(unicodeMatcher.group(1), 16);
                decodedNameBuilder.append((char) charCode);
                lastEnd = unicodeMatcher.end();
            }
            decodedNameBuilder.append(name.substring(lastEnd));
            String decodedName = decodedNameBuilder.toString();
            if (containsChinese(decodedName) && !fbAccountForSell.getRegion().contains("中文")){
                fbAccountForSell.setRegion(fbAccountForSell.getRegion()+"中文");
            }else if (!fbAccountForSell.getRegion().contains("英文")){
                fbAccountForSell.setRegion(fbAccountForSell.getRegion()+"英文");

            }
            // 获取 NAME 的值
            fbAccountForSell.setName(decodedName);
            updateFbAccountForSell(fbAccountForSell);
        }

        //好有数量
        Document document = Jsoup.parse(pageSource);
        Element element = document.select("h1:containsOwn(" + fbAccountForSell.getName() + ")").first();
        element = getNthParent(element, 5);
        element.select("a[href*='sk=friends']").first();
        if (element != null) {
            // 提取文本并解析数字
            String friendCountText = element.text();
            regex = "\\D+(\\d+)\\D+"; // 匹配非数字开头，跟一个数字，再跟非数字结尾

            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(friendCountText);
            String number = "0";
            if (matcher.find()) {
                number = matcher.group(1); // 提取数字部分
            }  // 获取 好友数量 部分
            fbAccountForSell.setFriendNumber(number);
            updateFbAccountForSell(fbAccountForSell);
        }

        //帖子数量
        try {
            webDriver.get("https://www.facebook.com/" + fbAccountForSell.getId() + "/allactivity?activity_history=false&category_key=YOURACTIVITYPOSTSSCHEMA&manage_mode=false&should_load_landing_page=false");
            try {
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(text(),'" + fbAccountForSell.getName() + "')]")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            pageSource = webDriver.getPageSource();
            document = Jsoup.parse(pageSource);
            int countPosts = document.select("a:containsOwn(" + fbAccountForSell.getName() + ")").size();

            if (countPosts > 0) {
                fbAccountForSell.setPostsNumber(String.valueOf(countPosts));
                updateFbAccountForSell(fbAccountForSell);
            } else {
                fbAccountForSell.setPostsNumber("0");
                updateFbAccountForSell(fbAccountForSell);
            }
        } catch (Exception e) {
            fbAccountForSell.setPostsNumber("0");
            updateFbAccountForSell(fbAccountForSell);
            e.printStackTrace();
        }

        //登录记录x
        try {
            webDriver.get("https://www.facebook.com/"+fbAccountForSell.getId()+"/allactivity/?category_key=LOGINSLOGOUTS&entry_point=ayi_hub");
            seleniumService.waitingForContent(10,webDriver, "https://static.xx.fbcdn.net/rsrc.php/v4/yu/r/5cE1svsoOSp.png");
            // 定义正则表达式
            String loginLog = webDriver.getPageSource();
            String dateRegex = "\\d{4}年\\d{1,2}月\\d{1,2}日";
            Pattern datePattern = Pattern.compile(dateRegex);
            Matcher dateMatcher = datePattern.matcher(loginLog);
            StringBuilder loginLogStr = new StringBuilder();
            while (dateMatcher.find()) {
                loginLogStr.append(dateMatcher.group()+";");
            }
            fbAccountForSell.setLastPostsTime(loginLogStr.toString());
            updateFbAccountForSell(fbAccountForSell);
        }catch (Exception e){}


        //BM数量
        try {
            webDriver.get("https://www.facebook.com/business-support-home/?landing_page=overview&source=link");
            try {
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()='" + fbAccountForSell.getName() + "']")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String currentUrl = webDriver.getCurrentUrl();
            if (!currentUrl.contains(fbAccountForSell.getId())) {
                pageSource = webDriver.getPageSource();
                // 使用 Jsoup 解析 HTML 源代码
                document = Jsoup.parse(pageSource);
                element = document.select("div[role=heading]:containsOwn(" + fbAccountForSell.getName() + ")").first();
                element = getNthParent(element, 12);
                Elements elements = element.siblingElements();
                String html = elements.get(0).html();
                pattern = Pattern.compile("business-support-home");
                matcher = pattern.matcher(html);
                int countBM = 0;
                while (matcher.find()) {
                    countBM++;
                }
                fbAccountForSell.setBmNumber(String.valueOf(countBM));
                updateFbAccountForSell(fbAccountForSell);
            } else {
                fbAccountForSell.setBmNumber("0");
                updateFbAccountForSell(fbAccountForSell);
            }

            //能否广告
            webDriver.get("https://www.facebook.com/business-support-home/" + fbAccountForSell.getId());
            for (int i = 0; i < 5; i++) {
                boolean b = seleniumService.waitingForContent(5, webDriver, fbAccountForSell.getName());
                if (b){
                    break;
                }
                webDriver.navigate().refresh();
                seleniumService.threadSleep(1);
            }
            try {
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()='" + fbAccountForSell.getName() + "']")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            pageSource = webDriver.getPageSource();
            // 使用 Jsoup 解析 HTML 源代码
            document = Jsoup.parse(pageSource);
            // 查找具有特定 class 属性的 div 元素
            element = document.select("div:containsOwn(" + fbAccountForSell.getName() + ")").first();
            element = getNthParent(element, 4);
            List<String> list = new ArrayList<>();
            list = getAllText(element, list);
            if (list.size() == 3) {
                fbAccountForSell.setCanAds("0");
                updateFbAccountForSell(fbAccountForSell);
            }
            if (list.size() == 1) {
                fbAccountForSell.setCanAds("1");
                updateFbAccountForSell(fbAccountForSell);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //主页数量
        webDriver.get("https://www.facebook.com/pages/?category=your_pages&ref=bookmarks");
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/pages/?category=invites&ref=bookmarks']")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        pageSource = webDriver.getPageSource();
        pattern = Pattern.compile("/latest/inbox/all");
        matcher = pattern.matcher(pageSource);
        int countPage = 0;
        while (matcher.find()) {
            countPage++;
        }
        if (countPage > 0) {
            fbAccountForSell.setPageNumber(String.valueOf(countPage));
            updateFbAccountForSell(fbAccountForSell);
        } else {
            fbAccountForSell.setPageNumber("0");
            updateFbAccountForSell(fbAccountForSell);
        }
    }


    /**
     * 获取账号语言
     *
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
        if (text.contains("English")) {
            return "English";
        } else if (text.contains("中文(台灣)")) {
            return "中文(台灣)";
        } else if (text.contains("中文(香港)")) {
            return "中文(香港)";
        } else if (text.contains("中文(简体)")) {
            return "中文(简体)";
        } else {
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
        return fbAccountForSellMapper.selectFbAccountForSellListByAccountIdsStr(ids);
    }

    /**
     * 查询账号
     *
     * @param ids
     * @return
     */
    @Override
    public List<FbAccountForSell> selectFbAccountForSellListByAccountIds(List<Long> ids) {
        return fbAccountForSellMapper.selectFbAccountForSellListByAccountIdsList(ids);
    }

    /**
     * 判断是否在线
     *
     * @param webDriver
     * @return
     */
    @Override
    public boolean isLogin(WebDriver webDriver, FbAccountForSell fbAccountForSell) {

        // 提取重复字符串为常量

        final String STATUS_ACTIVE = "1";
        final String STATUS_DISABLED = "0";

        String pageSource = webDriver.getPageSource();

        // 检查是否处于检查点页面
        if (webDriver.getCurrentUrl().contains("/checkpoint/")) {
            seleniumService.waitingForContent(5, webDriver, "xlink:href=\"https://scontent-tpe1-1.xx.fbcdn.net");

            // 处理检查点对应的情况
            if (pageSource.contains("/images/checkpoint/epsilon/comet/intro.png")) {
                updateFbAccountStatus(fbAccountForSell, "被封/被锁", STATUS_DISABLED);
                return false;
            }
            if (pageSource.contains("https://static.xx.fbcdn.net/rsrc.php/v4/yX/r/ACJE6Qz3VpL.png")) {
                updateFbAccountStatus(fbAccountForSell, "被封/被锁", STATUS_DISABLED);
                return false;
            }
        }

        if (pageSource.contains("="+fbAccountForSell.getId())) {
            return true;
        }

        return false; // 统一处理其他情况
    }

    /**
     * 发贴
     *
     * @param fbAccountForSell
     * @param webDriver
     */
    @Override
    public void post(FbAccountForSell fbAccountForSell, WebDriver webDriver) {

        OperationLog operationLog = new OperationLog();

        try {
            webDriver.manage().window().maximize();
            seleniumService.simulateKeyPress(KeyEvent.VK_ESCAPE);
            int[] numbers = {3, 4, 5};
            Random random = new Random();
            int randomIndex = random.nextInt(numbers.length);
            for (int i = 0; i < numbers[randomIndex]; i++) {
                seleniumService.simulateKeyPress(KeyEvent.VK_PAGE_DOWN);
                seleniumService.threadSleep(1);
            }
            seleniumService.simulateKeyPress(KeyEvent.VK_HOME);
            seleniumService.threadSleep(1);
            WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30, 1);
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@src='https://static.xx.fbcdn.net/rsrc.php/v4/y7/r/Ivw7nhRtXyo.png']"))).click();
            seleniumService.threadSleep(1);
            String pageSource = webDriver.getPageSource();
            //如果仅朋友可见
            String xpath = "";
            if (pageSource.contains("https://static.xx.fbcdn.net/rsrc.php/v4/yJ/r/zPcex_q0TM1.png")) {
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@src='https://static.xx.fbcdn.net/rsrc.php/v4/yJ/r/zPcex_q0TM1.png']"))).click();
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@src='https://static.xx.fbcdn.net/rsrc.php/v4/yD/r/KV7QFf-Yspp.png']"))).click();
                for (int i = 0; i < 3; i++) {
                    seleniumService.simulateKeyPress(KeyEvent.VK_TAB);
                    seleniumService.threadSleep(0.3);
                }
                seleniumService.simulateKeyPress(KeyEvent.VK_ENTER);
            }
            String p = "<p class=\"xdj266r x11i5rnm xat24cr x1mh8g0r x16tdsg8\"><br></p>";
            xpath = seleniumService.getXpathBySourceCode(pageSource, p);
            Posts posts = new Posts();
            posts.setType("fbAccount");
            List<Posts> postsList = postsService.selectPostsList(posts);
            random = new Random();
            randomIndex = random.nextInt(postsList.size());
            webDriver.findElement(By.xpath(xpath)).sendKeys(postsList.get(randomIndex).getContent());
            Point location = webDriver.findElement(By.xpath("//img[@src='https://static.xx.fbcdn.net/rsrc.php/v4/yq/r/b37mHA1PjfK.png']")).getLocation();
            seleniumService.clickAtCoordinates(webDriver, location.x, location.y + 70);
            seleniumService.threadSleep(2);
            pageSource = webDriver.getPageSource();
            if (pageSource.contains("role=\"dialog\"")) {
                if (pageSource.contains("<span class=\"x1lliihq x6ikm8r x10wlt62 x1n2onr6 xlyipyv xuxw1ft\">Post</span>")) {
                    webDriver.findElement(By.xpath("//span[text()='Post']")).click();
                }
                if (pageSource.contains("<span class=\"x1lliihq x6ikm8r x10wlt62 x1n2onr6 xlyipyv xuxw1ft\">發佈</span>")) {
                    webDriver.findElement(By.xpath("//span[text()='發佈']")).click();
                }
                if (pageSource.contains("<span class=\"x1lliihq x6ikm8r x10wlt62 x1n2onr6 xlyipyv xuxw1ft\">发帖</span>")) {
                    webDriver.findElement(By.xpath("//span[text()='发帖']")).click();
                }
            }
            seleniumService.threadSleep(2);
            pageSource = webDriver.getPageSource();
            if (pageSource.contains(postsList.get(randomIndex).getContent())){
                operationLog.setOperationAccount(fbAccountForSell.getId());
                operationLog.setOperationAccountKeyId(fbAccountForSell.getKeyId());
                operationLog.setOperationContent("卖号发帖");
                operationLog.setOperationStatus("成功");
                operationLog.setOperationTime(new Date());
                operationLogService.insertOperationLog(operationLog);
            }
            seleniumService.simulateKeyPress(KeyEvent.VK_F5);
            random = new Random();
            randomIndex = random.nextInt(numbers.length);
            for (int i = 0; i < numbers[randomIndex]; i++) {
                seleniumService.simulateKeyPress(KeyEvent.VK_PAGE_DOWN);
                seleniumService.threadSleep(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加好友
     *
     * @param fbAccountForSell
     * @param id
     * @param webDriver
     */
    @Override
    public String addFriend(FbAccountForSell fbAccountForSell, String id, WebDriver webDriver) {

        OperationLog operationLog = new OperationLog();
        WebDriverWait wait = new WebDriverWait(webDriver, 30, 1);
        webDriver.get("https://www.facebook.com/" + id);
        try {
            webDriver.manage().window().maximize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        seleniumService.waitingForContent(10,webDriver,"https://static.xx.fbcdn.net/rsrc.php/yG/r/kpb0jgKR4ve.webp");
        List<WebElement> beforeDialogElementList = null;
        try {
            beforeDialogElementList = webDriver.findElements(By.xpath("//div[@role='dialog']"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@src='https://static.xx.fbcdn.net/rsrc.php/yG/r/kpb0jgKR4ve.webp']"))).click();
        } catch (Exception e) {
            e.printStackTrace();
        }
        seleniumService.threadSleep(5);
        List<WebElement> afterDialogElementList = null;
        try {
            afterDialogElementList = webDriver.findElements(By.xpath("//div[@role='dialog']"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (beforeDialogElementList.size() != afterDialogElementList.size()) {
            try {
                List<WebElement> dialogElementList = webDriver.findElements(By.xpath("//div[@role='dialog']"));
                WebElement dialog = dialogElementList.get(dialogElementList.size()-1);
                // 正则表达式匹配 http 开头，png 结尾的 URL
                String regex = "http[^\\s]*?\\.png";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(dialog.getAttribute("outerHTML"));
                int count = 0;
                String secondMatch = null;
                while (matcher.find()) {
                    count++;
                    if (count == 2) { // 获取第二个匹配项
                        secondMatch = matcher.group();
                        break; // 找到第二个就退出循环
                    }
                }
                List<WebElement> elements = webDriver.findElements(By.xpath("//i[@style[contains(., '"+secondMatch+"')]]"));
                elements.get(elements.size()-1).click();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@src='https://static.xx.fbcdn.net/rsrc.php/y2/r/J32fl0R_iDX.webp']")));
            operationLog.setOperationAccount(fbAccountForSell.getId());
            operationLog.setOperationAccountKeyId(fbAccountForSell.getKeyId());
            operationLog.setOperationContent("卖号添加"+id+"好友");
            operationLog.setOperationStatus("成功");
            operationLog.setOperationTime(new Date());
            operationLogService.insertOperationLog(operationLog);
            return "success";
        }catch (Exception e){
            operationLog.setOperationAccount(fbAccountForSell.getId());
            operationLog.setOperationAccountKeyId(fbAccountForSell.getKeyId());
            operationLog.setOperationContent("卖号添加"+id+"好友");
            operationLog.setOperationStatus("失败");
            operationLog.setOperationTime(new Date());
            operationLogService.insertOperationLog(operationLog);
            e.printStackTrace();
            return "fail";
        }
    }


    /**
     * 添加好友
     * @param addId
     * @param number
     * @param region
     * @param canLogin
     * @param isSell
     * @return
     */
    @Override
    public String addFriendNew(String addId, Integer number, String region, String canLogin, String isSell) {
        // 构造查询条件
        FbAccountForSellQuery query = new FbAccountForSellQuery();
        query.setRegion(region);
        query.setCanLogin(canLogin);
        query.setIsSell(isSell);

        // 获取所有账号
        List<FbAccountForSell> allAccounts = fbAccountForSellMapper.selectFbAccountForSellListByQuery(query);

        // 计算5天前的截止日期
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -5);
        Date cutoffDate = cal.getTime();

        // 过滤掉近5天内有操作记录的账号
        List<FbAccountForSell> filteredAccounts = allAccounts.stream()
                .filter(account -> operationLogService.countOperationsAfter(account.getId(), cutoffDate) == 0)
                .collect(Collectors.toList());

        // 构建全局账号池，账号一旦被 poll 后就不再用于后续操作
        ConcurrentLinkedQueue<FbAccountForSell> accountPool = new ConcurrentLinkedQueue<>(filteredAccounts);

        // 解析目标ID数组
        String[] targetIds = addId.split("-");

        // 为每个 targetId 分配一个任务，并行处理不同 targetId
        ExecutorService executor = Executors.newFixedThreadPool(targetIds.length);
        List<Future<?>> futures = new ArrayList<>();

        for (String targetId : targetIds) {
            Future<?> future = executor.submit(() -> {
                int successCount = 0;
                while (successCount < number) {
                    // 从全局账号池中取账号
                    FbAccountForSell account = accountPool.poll();
                    if (account == null) {
                        System.out.println("账号池已空，targetId：" + targetId + " 添加好友数：" + successCount);
                        break;
                    }
                    if (!FBAccountUtil.checkAccountActive(account.getId()) && !targetId.contains(account.getId())) {
                        // 账号不活跃，跳过
                        continue;
                    }
                    // 阻塞式执行任务：调用 processAccount，等待返回结果
                    String result = processAccount(account, targetId);
                    if ("success".equals(result)) {
                        successCount++;
                        System.out.println("targetId：" + targetId + " 成功添加好友数：" + successCount);
                    }
                    // 可选：适当延迟，避免过快操作
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            futures.add(future);
        }

        // 等待所有 targetId 的任务完成
        for (Future<?> f : futures) {
            try {
                f.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        return "Completed";
    }

    /**
     * 阻塞式处理单个账号添加好友的逻辑，返回 "success" 或 "fail"
     */
    private String processAccount(FbAccountForSell account, String targetId) {
        WebDriver driver = openBrowser(account);
        driver.get("https://www.facebook.com/");
        // 检查登录状态，不登录则尝试登录
        if (!isLogin(driver, account)) {
            loginFbAccountForSell(driver, account);
            if (!isLogin(driver, account)) {
                // 登录失败，记录日志后返回失败
                OperationLog opLog = new OperationLog();
                opLog.setOperationAccount(account.getId());
                opLog.setOperationAccountKeyId(account.getKeyId());
                opLog.setOperationContent("卖号添加" + targetId + "好友");
                opLog.setOperationStatus("失败");
                opLog.setNote("登录失败");
                opLog.setOperationTime(new Date());
                operationLogService.insertOperationLog(opLog);
                try {
                    driver.close();
                    driver.quit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "fail";
            }
        }

        String result = addFriend(account, targetId, driver);
        try {
            driver.close();
            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 打开浏览器
     *
     * @param fbAccountForSell
     * @return
     */
    @Override
    public WebDriver openBrowser(FbAccountForSell fbAccountForSell) {
        if (!processMap.containsKey(fbAccountForSell.getId())){
            List<Integer> beforeListWindows = seleniumService.getListWindows();
            WebDriver webDriver = seleniumService.openBrowser(BrowserConfig.getFbAccountForSellBrowserConfig(fbAccountForSell));
            fbAccountForSellMapper.updateFbAccountForSellBrowserStatus(fbAccountForSell,"1");
            List<Integer> afterListWindows = seleniumService.getListWindows();
            Integer extraProcessId = seleniumService.findExtraProcessId(beforeListWindows, afterListWindows);
            webDriverMap.put(fbAccountForSell.getId(),webDriver);
            processMap.put(fbAccountForSell.getId(),extraProcessId);
            return webDriver;
        }
        if (processMap.containsKey(fbAccountForSell.getId())){
            showBrowser(fbAccountForSell);
            return null;
        }
        return null;
    }

    /**
     * 关闭浏览器
     *
     * @param fbAccountForSell
     */
    @Override
    public void closeBrowser(FbAccountForSell fbAccountForSell) {
        if (processMap.containsKey(fbAccountForSell.getId())){
            webDriverMap.get(fbAccountForSell.getId()).quit();
            processMap.remove(fbAccountForSell.getId());
            webDriverMap.remove(fbAccountForSell.getId());
        }
    }

    /**
     * 显示浏览器
     *
     * @param fbAccountForSell
     */
    @Override
    public void showBrowser(FbAccountForSell fbAccountForSell) {
        seleniumService.showBrowser(processMap.get(fbAccountForSell.getId()));
    }

    /**
     * 创建主页
     *
     * @param webDriver
     * @param fbAccountForSell
     * @param pageName
     * @return
     */
    @Override
    public String createPage(WebDriver webDriver, FbAccountForSell fbAccountForSell, String pageName) {
        loginFbAccountForSell(webDriver,fbAccountForSell);
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30);
        // 要输入的文本
        String text = "clothing";
        try {
            webDriver.get("https://www.facebook.com/pages/creation/?ref_type=launch_point");
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='text']"))).sendKeys(pageName);
            WebElement pageType = webDriver.findElement(
                    By.xpath("//input[@aria-label='类别（必填）' or @aria-label='類別（必填）' or @aria-label='Category (required)']")
            );
            for (char c : text.toCharArray()) {
                pageType.sendKeys(String.valueOf(c)); // 逐个输入字符
                seleniumService.threadSleep(0.2); // 模拟用户输入间隔，200ms
            }
            seleniumService.threadSleep(2);
            pageType.sendKeys(Keys.DOWN);
            pageType.sendKeys(Keys.ENTER);
            webDriver.findElement(By.xpath("//div[@aria-label='创建公共主页' or @aria-label='建立粉絲專頁' or @aria-label='Create Page']")).click();
        } catch (Exception e) {

        }



        boolean result = seleniumService.waitingForContent(15, webDriver, "US+1");
        OperationLog operationLog = new OperationLog();
        if (result){
            operationLog.setOperationAccount(fbAccountForSell.getId());
            operationLog.setOperationAccountKeyId(fbAccountForSell.getKeyId());
            operationLog.setOperationContent("卖号创建主页："+pageName);
            operationLog.setOperationStatus("成功");
            operationLog.setOperationTime(new Date());
            operationLogService.insertOperationLog(operationLog);
            return "true";
        }else {
            operationLog.setOperationAccount(fbAccountForSell.getId());
            operationLog.setOperationAccountKeyId(fbAccountForSell.getKeyId());
            operationLog.setOperationContent("卖号创建主页："+pageName);
            operationLog.setOperationStatus("失败");
            operationLog.setOperationTime(new Date());
            operationLogService.insertOperationLog(operationLog);
            webDriver.quit();
            return "false";
        }
    }


    /**
     * 打开比特浏览器
     * @param fbAccountForSell
     * @return
     */
    @Override
    public String openBitBrowser(FbAccountForSell fbAccountForSell) {
        ProxyIp ip = new ProxyIp();
        ip.setStatus("1");
        List<ProxyIp> proxyIpList = proxyIpService.selectProxyIpList(ip);
        ProxyIp proxyIp = proxyIpList.get(0);
        Map<String, Object> createAndUpdateConfig = BiteBrowser.createAndUpdateBrowse();

        createAndUpdateConfig.put("host", "127.0.0.1");
        createAndUpdateConfig.put("port", "8800");
        createAndUpdateConfig.put("proxyUserName", "");
        createAndUpdateConfig.put("proxyPassword", "");
        if (fbAccountForSell.getCookie() != ""  && fbAccountForSell.getCookie() != null) {
            createAndUpdateConfig.put("cookie", fbAccountForSell.getCookie());
        }
        try {
            Map<String, Object> resultMap = OkHttpUtil.post("http://127.0.0.1:54345/browser/update", createAndUpdateConfig);
            // 解析返回的 Map
            if (resultMap != null && (boolean) resultMap.get("success")) {
                // 获取 "data" 部分
                Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");

                // 获取 "id" 值
                if (dataMap != null) {
                    String id = (String) dataMap.get("id");
                    Map<String, Object> openBrowseConfig = BiteBrowser.openBrowse();
                    openBrowseConfig.put("id", id);
                    Map<String, Object> openResultMap = OkHttpUtil.post("http://127.0.0.1:54345/browser/open", openBrowseConfig);
                    if (openResultMap != null && (boolean) openResultMap.get("success")) {
                        Map<String, Object> openData = (Map<String, Object>) openResultMap.get("data");
                        openData.forEach((k, v) -> System.out.println(k + "=" + v));
                        if (openData != null) {
                            WebDriver webDriver = null;
                            try {
                                //参数配置
                                System.setProperty("webdriver.chrome.driver", openData.get("driver").toString());
                                ChromeOptions options = new ChromeOptions();
                                options.setExperimentalOption("debuggerAddress", openData.get("http").toString());
                                options.setExperimentalOption("debuggerAddress", openData.get("http").toString());
                                webDriver = new ChromeDriver(options);
                                try {
                                    webDriver.get("https://www.facebook.com");
                                    try {
                                        Thread.sleep(5000);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                    if (isLogin(webDriver,fbAccountForSell)){
                                        return "";
                                    }
                                    // 清除所有 cookies
                                    webDriver.manage().window().maximize();
                                    WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30, 1);
                                    try {
                                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("login")));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        WebElement email = webDriver.findElement(By.name("email"));
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
                                            //                email.sendKeys(fbAccountForSell.getEmail());
                                            for (char c : fbAccountForSell.getEmail().toCharArray()) {
                                                email.sendKeys(String.valueOf(c));
                                                // 可选：添加延迟，模拟更真实的逐字符输入
                                                Thread.sleep(100); // 延迟100毫秒
                                            }
                                            Thread.sleep(1000);
                                            password.click();
                                            // 模拟Ctrl+A组合键
                                            actions.keyDown(Keys.CONTROL)
                                                    .sendKeys("a")
                                                    .keyUp(Keys.CONTROL)
                                                    .perform();
                                            Thread.sleep(500);
                                            actions.sendKeys(Keys.DELETE).perform();
                                            //                password.sendKeys(fbAccountForSell.getPassword());
                                            for (char c : fbAccountForSell.getPassword().toCharArray()) {
                                                password.sendKeys(String.valueOf(c));
                                                // 可选：添加延迟，模拟更真实的逐字符输入
                                                Thread.sleep(100); // 延迟100毫秒
                                            }
                                            Thread.sleep(2000);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        WebElement loginButton = webDriver.findElement(By.name("login"));
                                        loginButton.click();
                                        seleniumService.threadSleep(1);
                                        seleniumService.waitingForContent(10, webDriver, "• Facebook");
                                        String pageSource = webDriver.getPageSource();
                                        Document document = Jsoup.parse(pageSource);
                                        if (webDriver.getCurrentUrl().contains("privacy_mutation_token=eyJ0eXBlIjowLCJjcmVhdGlvbl90aW1lIjoxNzQyMTQ")){
                                            String oldNote = fbAccountForSell.getNote();
                                            fbAccountForSell.setNote("无法登录-改过密码|"+oldNote);
                                            fbAccountForSell.setCanLogin("0");
                                            updateFbAccountForSell(fbAccountForSell);
                                            return "";
                                        }
                                        if (pageSource.contains("输入你看到的验证码")) {
                                            String oldNote = fbAccountForSell.getNote();
                                            fbAccountForSell.setNote("无法登录-需要输入验证码|"+oldNote);
                                            updateFbAccountForSell(fbAccountForSell);
                                            return "";
                                        }
                                        if (pageSource.contains("WhatsApp")) {
                                            String oldNote = fbAccountForSell.getNote();
                                            fbAccountForSell.setNote("无法登录-需要WhatsApp验证码|"+oldNote);
                                            updateFbAccountForSell(fbAccountForSell);
                                            return "";
                                        }
                                        if (pageSource.contains("账号或密码无效")) {
                                            String oldNote = fbAccountForSell.getNote();
                                            fbAccountForSell.setNote("无法登录-账号或密码无效|"+oldNote);
                                            fbAccountForSell.setCanLogin("0");
                                            updateFbAccountForSell(fbAccountForSell);
                                            return "";
                                        }
                                        if (pageSource.contains("你输入的是旧密码")) {
                                            String oldNote = fbAccountForSell.getNote();
                                            fbAccountForSell.setNote("无法登录-改过密码|"+oldNote);
                                            fbAccountForSell.setCanLogin("0");
                                            updateFbAccountForSell(fbAccountForSell);
                                            return "";
                                        }

                                        //新版双重验证码输入
                                        if (document.select("#approvals_code").first() != null) {
                                            webDriver.findElement(By.id("approvals_code")).sendKeys(FBAccountUtil.getGoogleVerificationCode(fbAccountForSell.getSecretKey()));
                                            webDriver.findElement(By.id("checkpointSubmitButton")).click();
                                            Thread.sleep(2000);
                                            webDriver.findElement(By.id("checkpointSubmitButton")).click();
                                            Thread.sleep(2000);
                                            webDriver.get("https://www.facebook.com");
                                        }
                                        Element element = document.select("input[type=text]").first();
                                        if (element != null) {
                                            WebElement approvalsCode = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='text']")));
                                            approvalsCode.sendKeys(FBAccountUtil.getGoogleVerificationCode(fbAccountForSell.getSecretKey()));
                                            approvalsCode.sendKeys(Keys.ENTER);
                                            Thread.sleep(3000);
                                            webDriver.get("https://www.facebook.com");
                                        }
                                        int size = document.select("[role=button]").size();
                                        if (size == 1) {
                                            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@role='button']"))).click();
                                            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[type='radio'][value='1']"))).click();
                                            List<WebElement> elements = webDriver.findElements(By.xpath("//div[@data-visualcompletion='ignore']"));
                                            elements.get(elements.size() - 1).findElement(By.xpath("..")).click();
                                            WebElement approvalsCode = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='text']")));
                                            approvalsCode.sendKeys(FBAccountUtil.getGoogleVerificationCode(fbAccountForSell.getSecretKey()));
                                            approvalsCode.sendKeys(Keys.ENTER);
                                            if (seleniumService.waitingForContent(2, webDriver, "currentColor")) {
                                                fbAccountForSell.setNote("无法登录-秘钥错误");
                                                updateFbAccountForSell(fbAccountForSell);
                                                return "";
                                            }
                                            for (int i = 0; i < 10; i++) {
                                                if (!isLogin(webDriver, fbAccountForSell)){
                                                    Thread.sleep(1000);
                                                }
                                            }
                                            webDriver.get("https://www.facebook.com");
                                            seleniumService.threadSleep(5);
                                            Set<Cookie> cookies = webDriver.manage().getCookies();
                                            JsonArray jsonArray = new JsonArray();

                                            for (Cookie cookie : cookies) {
                                                JsonObject jsonCookie = new JsonObject();
                                                jsonCookie.addProperty("domain", cookie.getDomain());
                                                if (cookie.getExpiry() != null) {
                                                    jsonCookie.addProperty("expirationDate", cookie.getExpiry().getTime() / 1000.0);
                                                }
                                                jsonCookie.addProperty("httpOnly", cookie.isHttpOnly());
                                                jsonCookie.addProperty("name", cookie.getName());
                                                jsonCookie.addProperty("path", cookie.getPath());
                                                jsonCookie.addProperty("secure", cookie.isSecure());
                                                jsonCookie.addProperty("session", cookie.getExpiry() == null);
                                                jsonCookie.addProperty("value", cookie.getValue());

                                                jsonArray.add(jsonCookie);
                                            }

                                            String json = new Gson().toJson(jsonArray);
                                            fbAccountForSell.setCookie(json);
                                            updateFbAccountForSell(fbAccountForSell);

                                        }

                                        return "";


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } catch (RuntimeException e) {
                                    e.printStackTrace(

                                    );
                                }
                                if ( !isLogin(webDriver, fbAccountForSell)){
                                    webDriver.quit();
                                    if (!fbAccountForSell.getNote().equals("账号或密码无效") && !fbAccountForSell.getNote().equals("无法登录-需要输入验证码")
                                            && !fbAccountForSell.getNote().equals("无法登录-秘钥错误") && !fbAccountForSell.getNote().equals("无法登录-需要WhatsApp验证码")
                                            && !fbAccountForSell.getNote().equals("无法登录-账号被锁") && !fbAccountForSell.getNote().equals("无法登录-改过密码") ){
                                        fbAccountForSell.setNote("无法登录-未知情况");
                                        fbAccountForSell.setCanLogin("0");
                                        updateFbAccountForSell(fbAccountForSell);
                                    }
                                }
                                if (isLogin(webDriver, fbAccountForSell)){
                                    fbAccountForSell.setNote("");
                                    updateFbAccountForSell(fbAccountForSell);
                                }
                                Thread.sleep(1000);
                                getAccountMarketplaceAndNameAndFriendInSimplified(webDriver, fbAccountForSell);
                                webDriver.close();
                                webDriver.quit();
                            } catch (Exception e) {
                                webDriver.close();
                                webDriver.quit();
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 确认添加好友
     *
     * @param fbAccountForSell
     * @return
     */
    @Override
    public String confirmAddFriend(FbAccountForSell fbAccountForSell) {
        WebDriver webDriver = openBrowser(fbAccountForSell);
        loginFbAccountForSell(webDriver, fbAccountForSell);
        webDriver.get("https://www.facebook.com/friends");
        WebDriverWait webDriverWait =new WebDriverWait(webDriver, 30);
        try {
            while (true) {
                // 获取所有匹配的元素
                List<WebElement> elements = webDriver.findElements(By.xpath("//span[text()='確認' or text()='Confirm' or text()='确认']"));

                if (elements.isEmpty()) {
                    System.out.println("所有元素已点击完毕，页面不再包含匹配的元素");
                    break; // 退出循环
                }

                System.out.println("发现 " + elements.size() + " 个匹配元素，开始点击...");

                // 点击所有匹配的元素
                for (WebElement element : elements) {
                    try {
                        element.click();
                        System.out.println("点击了一个元素");
                        Thread.sleep(500); // 等待 1 秒，避免操作过快
                    } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
                        System.out.println("点击失败，元素可能已消失或不可见，跳过...");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // 刷新页面
                System.out.println("点击完成，刷新页面...");
                webDriver.navigate().refresh();

                // 等待页面加载完成
                try {
                    Thread.sleep(3000); // 等待 3 秒，确保页面加载完成
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 修改账号名
     *
     * @param fbAccountForSell
     * @param newName
     * @return
     */
    @Override
    public String changeAccountName(WebDriver webDriver, FbAccountForSell fbAccountForSell, String newName) {


        loginFbAccountForSell(webDriver, fbAccountForSell);
        seleniumService.threadSleep(1);

        webDriver.get("https://www.facebook.com/profile.php?id="+fbAccountForSell.getId());
        WebDriverWait webDriverWait =new WebDriverWait(webDriver, 30);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@role='button' and contains(@aria-label,'個人檔案設定」的「查看更多」')]"))).click();
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[normalize-space()='開啟專業模式']"))).click();
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[normalize-space()='開啟']"))).click();
        seleniumService.waitingForContent(10,webDriver,"歡迎使用專業模式");
        webDriver.get("https://accountscenter.facebook.com/profiles/"+fbAccountForSell.getId()+"/name");
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='text']")));
        List<WebElement> elements = webDriver.findElements(By.xpath("//input[@type='text']"));
        Actions actions = new Actions(webDriver);
        actions.moveToElement(elements.get(0))
                .click()
                .click()
                .click()
                .perform();
        seleniumService.threadSleep(1);
        elements.get(0).sendKeys(newName.substring(1));
        actions.moveToElement(elements.get(2))
                .click()
                .click()
                .click()
                .perform();
        seleniumService.threadSleep(1);
        elements.get(2).sendKeys(newName.substring(0,1));
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='预览修改' or text()='檢視變更' or text()='Review change']"))).click();
        seleniumService.threadSleep(2);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='完成' or text()='Done']"))).click();
        seleniumService.threadSleep(4);
        if (webDriver.getPageSource().contains("查看 WhatsApp 訊息")){
            String oldNote = fbAccountForSell.getNote();
            if (!oldNote.contains("改名WhatsApp验证")){
                fbAccountForSell.setNote("改名WhatsApp验证|"+oldNote);
                updateFbAccountForSell(fbAccountForSell);
            }
            return "";
        }
        getAccountNameAndFriendNumber(webDriver, fbAccountForSell);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@role='button' and contains(@aria-label,'個人檔案設定」的「查看更多」')]"))).click();
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[normalize-space()='關閉專業模式']"))).click();
        seleniumService.threadSleep(2);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//span[normalize-space()='繼續'])[last()]"))).click();
        seleniumService.threadSleep(2);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//span[normalize-space()='關閉'])[last()]"))).click();
        return "";
    }

    /**
     * 随机修改账号名
     *
     * @param fbAccountForSell
     * @param newName
     * @return
     */
    @Override
    public String changeRandomAccountName(WebDriver webDriver, FbAccountForSell fbAccountForSell, String newName) {
        loginFbAccountForSell(webDriver, fbAccountForSell);
        seleniumService.threadSleep(1);
        webDriver.get("https://www.facebook.com/profile.php?id="+fbAccountForSell.getId());
        WebDriverWait webDriverWait =new WebDriverWait(webDriver, 30);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@role='button' and contains(@aria-label,'個人檔案設定」的「查看更多」')]"))).click();
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[normalize-space()='開啟專業模式']"))).click();
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[normalize-space()='開啟']"))).click();
        seleniumService.waitingForContent(10,webDriver,"歡迎使用專業模式");
        if (fbAccountForSell.getGender()==null || fbAccountForSell.getGender().equals("")){
            webDriver.get("https://www.facebook.com/profile.php?id="+fbAccountForSell.getId()+"&sk=about_contact_and_basic_info");
            String target = "https://static.xx.fbcdn.net/rsrc.php/v4/yz/r/AqoGWewwdNN.png";
            seleniumService.waitingForContent(30, webDriver, target);
            String female = "https://static.xx.fbcdn.net/rsrc.php/v4/yo/r/wfYa2HPiNGU.png";
            String male = "https://static.xx.fbcdn.net/rsrc.php/v4/yi/r/rodGQv9jZg5.png";
            if (webDriver.getPageSource().contains(female)) {
                fbAccountForSell.setGender("女");
            }else if (webDriver.getPageSource().contains(male)) {
                fbAccountForSell.setGender("男");
            }else {
                fbAccountForSell.setGender("女");
            }
            updateFbAccountForSell(fbAccountForSell);
        }
        AccountName accountName = new AccountName();
        accountName.setGender(fbAccountForSell.getGender());
        accountName.setIsUse("0");
        List<AccountName> accountNameList = accountNameService.selectAccountNameList(accountName);
        int index = (int) (Math.random() * accountNameList.size());
        AccountName randomAccountName = accountNameList.get(index);
        // 修改 isUse
        randomAccountName.setIsUse("1");
        // 更新保存
        accountNameService.updateAccountName(randomAccountName);
        webDriver.get("https://accountscenter.facebook.com/profiles/"+fbAccountForSell.getId()+"/name");
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='text']")));
        List<WebElement> elements = webDriver.findElements(By.xpath("//input[@type='text']"));
        Actions actions = new Actions(webDriver);
        actions.moveToElement(elements.get(0))
                .click()
                .click()
                .click()
                .perform();
        seleniumService.threadSleep(1);
        elements.get(0).sendKeys(randomAccountName.getName().substring(1));
        actions.moveToElement(elements.get(2))
                .click()
                .click()
                .click()
                .perform();
        seleniumService.threadSleep(1);
        elements.get(2).sendKeys(randomAccountName.getName().substring(0,1));
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='预览修改' or text()='檢視變更' or text()='Review change']"))).click();
        seleniumService.threadSleep(2);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='完成' or text()='Done']"))).click();
        seleniumService.threadSleep(4);
        if (webDriver.getPageSource().contains("查看 WhatsApp 訊息")){
            String oldNote = fbAccountForSell.getNote();
            if (!oldNote.contains("改名WhatsApp验证")){
                fbAccountForSell.setNote("改名WhatsApp验证|"+oldNote);
                updateFbAccountForSell(fbAccountForSell);
            }
            return "";
        }
        getAccountNameAndFriendNumber(webDriver, fbAccountForSell);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@role='button' and contains(@aria-label,'個人檔案設定」的「查看更多」')]"))).click();
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[normalize-space()='關閉專業模式']"))).click();
        seleniumService.threadSleep(2);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//span[normalize-space()='繼續'])[last()]"))).click();
        seleniumService.threadSleep(2);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//span[normalize-space()='關閉'])[last()]"))).click();
        return "";
    }

    /**
     * 批量查询
     *
     * @param account
     * @return
     */
    @Override
    public List<FbAccountForSell> batchSearch(FbAccountForSell account) {
        return fbAccountForSellMapper.batchSearch(account);
    }

    /**
     * @param webDriver
     * @param fbAccountForSell
     * @return
     */
    @Override
    public String superAccount(WebDriver webDriver, FbAccountForSell fbAccountForSell) {

        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 10);

        webDriver.get("https://auth.meta.com/");
        /*webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[normalize-space(text())='Sign up or log into Meta account' or normalize-space(text()='Sign up or log in to Meta account')]"))).click();
        seleniumService.threadSleep(1);*/
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[normalize-space(text())='以" + fbAccountForSell.getName() + "的身分繼續']"))).click();
        seleniumService.threadSleep(1);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[normalize-space(text())='允許並繼續']"))).click();
        seleniumService.threadSleep(1);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[normalize-space(text())='建立帳號']"))).click();
        seleniumService.threadSleep(5);
        webDriver.get("https://accountscenter.facebook.com/password_and_security/password/change");
        seleniumService.threadSleep(5);
//        if (webDriver.findElements(By.xpath("//div[normalize-space()='"+fbAccountForSell.getName()+"']")).size()>1) {
//            fbAccountForSell.setNote("superAcc");
//            updateFbAccountForSell(fbAccountForSell);
//        }
        return "";
    }

    /**
     * 改成台湾号
     *
     * @param webDriver
     * @param fbAccountForSell
     * @return
     */
    @Override
    public String changeTWAccount(WebDriver webDriver, FbAccountForSell fbAccountForSell) {
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30);
        webDriver.get("https://www.facebook.com/profile.php?id="+fbAccountForSell.getId()+"&sk=about");
        try {
            seleniumService.threadSleep(5);
            By locator = By.xpath(
                    "//span[normalize-space()='個人簡介']/ancestor::div[3]/div[2]/div/div/div[2]"
            );

            WebElement element = webDriver.findElement(locator);
           if (element.isEnabled()) {
               element.click();
               webDriver.findElement(By.xpath("//textarea[@maxlength='101']"));
           }

            // 打印 outerHTML（包含自身标签）
            System.out.println(element.getAttribute("outerHTML"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "";
    }


    // 获取第 n 级父元素的方法
    public Element getNthParent(Element element, int levels) {
        Element currentElement = element;

        for (int i = 0; i < levels; i++) {
            if (currentElement == null) {
                return null; // 如果没有父元素，返回 null
            }
            currentElement = currentElement.parent();
        }

        return currentElement; // 返回第 n 级父元素，或找不到时返回 null
    }

    // 定义一个递归函数来遍历所有子元素
    public List<String> getAllText(Element parent, List<String> list) {
        for (Element child : parent.children()) {
            if (child.children().isEmpty()) {
                if (!child.text().isEmpty()) {
                    list.add(child.text());
                }
            } else {
                getAllText(child, list);
            }
        }
        return list;
    }

    /**
     * 更新 FbAccount 状态的工具方法
     */
    private void updateFbAccountStatus(FbAccountForSell fbAccountForSell, String note, String status) {
        if (note != null) {

        }
        if (status != null) {
            fbAccountForSell.setCanLogin(status);
        }
        updateFbAccountForSell(fbAccountForSell);
    }

    /**
     * 判断是否是中文
     * @param input
     * @return
     */
    public static boolean containsChinese(String input) {
        // 正则表达式匹配中文字符（包括常用汉字）
        if (input != null && input.matches(".*[\u4e00-\u9fa5].*")) {
            return true;  // 包含中文字符
        }
        return false;  // 不包含中文字符
    }

    //英文月份转数字
    private int parseEnglishMonth(String monthName) {
        switch (monthName) {
            case "January": return 1;
            case "February": return 2;
            case "March": return 3;
            case "April": return 4;
            case "May": return 5;
            case "June": return 6;
            case "July": return 7;
            case "August": return 8;
            case "September": return 9;
            case "October": return 10;
            case "November": return 11;
            case "December": return 12;
            default: return 0;
        }
    }

    private void parseAccountName(String pageSource,
                                  FbAccountForSell fbAccount) {

        String regex = "\"USER_ID\":\"" + fbAccount.getId()
                + "\".*?\"NAME\":\"(.*?)\"";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pageSource);

        if (matcher.find()) {

            String rawName = matcher.group(1);
            String decodedName = decodeUnicode(rawName);

            fbAccount.setName(decodedName);

            // 处理语言标识
            String region = fbAccount.getRegion()
                    .replace("中文", "")
                    .replace("英文", "");

            if (containsChinese(decodedName)) {
                fbAccount.setRegion(region + "中文");
            } else {
                fbAccount.setRegion(region + "英文");
            }
        }
    }

    private void parseFriendNumber(WebDriverWait wait,
                                   FbAccountForSell fbAccount) {

        try {
            WebElement strong = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//a[contains(@href,'sk=friends_all')]//strong")
                    )
            );

            fbAccount.setFriendNumber(strong.getText().trim().replace(",", ""));

        } catch (Exception e) {
            fbAccount.setFriendNumber("0");
        }
    }

    private void parseGender(WebDriver webDriver,
                             FbAccountForSell fbAccount) {

        webDriver.get("https://www.facebook.com/"
                + fbAccount.getId()
                + "?sk=directory_personal_details");

        String pageSource = webDriver.getPageSource();

        if (pageSource.contains("Female") || pageSource.contains("女")) {
            fbAccount.setGender("女");
        } else if (pageSource.contains("Male") || pageSource.contains("男")) {
            fbAccount.setGender("男");
        }
    }

    private String decodeUnicode(String input) {

        Matcher matcher = Pattern
                .compile("\\\\u([0-9a-fA-F]{4})")
                .matcher(input);

        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            char ch = (char) Integer.parseInt(matcher.group(1), 16);
            matcher.appendReplacement(sb, String.valueOf(ch));
        }

        matcher.appendTail(sb);

        return sb.toString();
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}



