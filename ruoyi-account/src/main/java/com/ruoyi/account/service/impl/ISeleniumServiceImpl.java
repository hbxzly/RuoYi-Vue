package com.ruoyi.account.service.impl;

import com.ruoyi.account.domain.*;
import com.ruoyi.account.service.IOperationLogService;
import com.ruoyi.account.util.CreatePageConstants;
import com.ruoyi.account.mapper.FbAccountMapper;
import com.ruoyi.account.service.IAccountAdAccountBmInfoService;
import com.ruoyi.account.service.IAdvertiseService;
import com.ruoyi.account.service.ISeleniumService;
import com.ruoyi.account.util.*;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.ptr.IntByReference;
import com.warrenstrange.googleauth.GoogleAuthenticator;
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
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;


@Service
public class ISeleniumServiceImpl implements ISeleniumService {

    @Autowired
    private FbAccountMapper fbAccountMapper;

    @Autowired
    private IAdvertiseService advertiseService;

    @Autowired
    private IAccountAdAccountBmInfoService accountAdAccountBmInfoService;

    @Autowired
    private IOperationLogService operationLogService;

    //webDriver集合
    private Map<String, WebDriver> webDriverMap;
    //进程集合
    private Map<String, Integer> processMap;


    public ISeleniumServiceImpl() {
        webDriverMap = new HashMap<>();
        processMap = new HashMap<>();
    }

    @Override
    public WebDriver openBrowser(FbAccount fbAccount) {
        //判断账号是否已经打开
        if (!webDriverMap.containsKey(fbAccount.getId())) {
            try {
                //参数配置
                System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
                ChromeOptions option = new ChromeOptions();
                option.addArguments("--user-data-dir=" + fbAccount.getDataSource() + fbAccount.getBrowserProfile());
                option.addArguments("--remote-allow-origins=*");
                option.addArguments("--disable-notifications");//限制通知
                option.addArguments("--user-agent=" + fbAccount.getUa());
                option.setExperimentalOption("useAutomationExtension", false);
                option.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
                List<Integer> beforeProcessIdList = listWindows();
                WebDriver webDriver = new ChromeDriver(option);
                List<Integer> afterProcessIdList = listWindows();
                changeBrowserStatus(fbAccount, "1");
                processMap.put(fbAccount.getId(), findExtraProcessId(beforeProcessIdList, afterProcessIdList));
                webDriverMap.put(fbAccount.getId(), webDriver);
                return webDriver;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            showBrowser(fbAccount);
            return null;
        }


    }

    /**
     * 打开打单个浏览器
     *
     * @param fbAccountForSell
     * @return
     */
    @Override
    public WebDriver openBrowserForAccountSell(FbAccountForSell fbAccountForSell) {
        try {
            //参数配置
            System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
            ChromeOptions option = new ChromeOptions();
//            option.addArguments("--user-data-dir=" + fbAccountForSell.getFilePath() + fbAccountForSell.getBrowserProfile());
            option.addArguments("--remote-allow-origins=*");
            option.addArguments("--disable-notifications");//限制通知
            option.addArguments("--user-agent=" + fbAccountForSell.getUa());
            option.setExperimentalOption("useAutomationExtension", false);
            option.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
            WebDriver webDriver = new ChromeDriver(option);
            return webDriver;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String multipleOpenBrowser(String[] ids) {
        ArrayList<String> idList = new ArrayList<>();
        for (String id : ids) {
            idList.add(id);
            if (!webDriverMap.containsKey(id)) {
                openBrowser(fbAccountMapper.selectOneByFbAccountId(id));
            }
        }
        // 定义线程数量
        int threadCount = idList.size();
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < idList.size(); i++) {
            FbAccount fbAccount = fbAccountMapper.selectOneByFbAccountId(idList.get(i));
            threads[i] = new Thread(() -> login(fbAccount));
            threads[i].start();
        }
        return "ok";
    }

    @Override
    public String closeBrowser(FbAccount fbAccount) {

        try {
            if (webDriverMap.containsKey(fbAccount.getId())) {
                webDriverMap.get(fbAccount.getId()).close();
                webDriverMap.get(fbAccount.getId()).quit();
                webDriverMap.remove(fbAccount.getId());
                processMap.remove(fbAccount.getId());
                changeBrowserStatus(fbAccount, "0");
                return "ok";
            } else {
                return "failed";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            webDriverMap.remove(fbAccount.getId());
            changeBrowserStatus(fbAccount, "0");
        }

        return "ok";


    }

    @Override
    public String closeAllBrowser() {

        List<String> ids = new ArrayList<>();
        webDriverMap.entrySet().forEach(entry -> {
            ids.add(entry.getKey());
        });
        ids.forEach(id -> {
            closeBrowser(fbAccountMapper.selectOneByFbAccountId(id));
        });

        return "ok";
    }

    @Override
    public boolean login(FbAccount fbAccount) {

        WebDriver webDriver = webDriverMap.get(fbAccount.getId());
        webDriver.get("https://www.facebook.com");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30, 1);
        if (isLogin(webDriver,fbAccount).equals("false") || isLogin(webDriver,fbAccount).equals("error")) {
            if (fbAccount.getStatus() == "4" || fbAccount.getStatus() == "5") {
                return false;
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
//                email.sendKeys(fbAccountForSell.getEmail());
                    for (char c : fbAccount.getEmail().toCharArray()) {
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
                    for (char c : fbAccount.getPassword().toCharArray()) {
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
                waitingForContent(10, webDriver, "• Facebook");
                String pageSource = webDriver.getPageSource();
                Document document = Jsoup.parse(pageSource);
                if (pageSource.contains("输入你看到的验证码")) {
                    fbAccount.setNote("无法登录-需要输入验证码");
                    fbAccountMapper.updateFbAccount(fbAccount);
                    return false;
                }
                if (pageSource.contains("WhatsApp")) {
                    fbAccount.setNote("无法登录-需要WhatsApp验证码");
                    fbAccountMapper.updateFbAccount(fbAccount);
                    return false;
                }
                if (!pageSource.contains("账号或密码无效")) {
                    //新版双重验证码输入
                    if (document.select("#approvals_code").first() != null) {
                        webDriver.findElement(By.id("approvals_code")).sendKeys(getVerificationCode(fbAccount.getSecretKey()));
                        webDriver.findElement(By.id("checkpointSubmitButton")).click();
                        Thread.sleep(2000);
                        webDriver.findElement(By.id("checkpointSubmitButton")).click();
                        Thread.sleep(2000);
                        webDriver.get("https://www.facebook.com");
                    }
                    Element element = document.select("input[type=text]").first();
                    if (element != null) {
                        WebElement approvalsCode = webDriver.findElement(By.xpath("//input[@type='text']"));
                        approvalsCode.sendKeys(getVerificationCode(fbAccount.getSecretKey()));
                        WebElement submitButton = webDriver.findElement(By.xpath("(//div[@role='button'])[2]"));
                        submitButton.click();
                        if (waitingForContent(2, webDriver, "currentColor")) {
                            fbAccount.setNote("无法登录-秘钥错误");
                            fbAccountMapper.updateFbAccount(fbAccount);
                            return false;
                        }
                        Thread.sleep(3000);
                        webDriver.get("https://www.facebook.com");
                        if (isLogin(webDriver,fbAccount) != "true"){
                            return false;
                        }
                    }
                    int size = document.select("[role=button]").size();
                    if (size == 1) {
                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@role='button']"))).click();
                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[type='radio'][value='1']"))).click();
                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div[3]/div/div/div[1]/div/div[2]/div/div/div/div/div/div/div[4]/div[3]/div/div/div/div/div/div/div/div/div[1]/div/span/span"))).click();
                        WebElement approvalsCode = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='text']")));
                        approvalsCode.sendKeys(getVerificationCode(fbAccount.getSecretKey()));
                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/div/div[1]/div[1]/div/div[2]/div[2]/div/div/div/div/div[3]/div/div[1]/div/div/div/div[1]/div/span/span"))).click();
                        for (int i = 0; i < 10; i++) {
                            String login = isLogin(webDriver,fbAccount);
                            if (!login.equals("true")) {
                                Thread.sleep(1000);
                            }
                        }
                        webDriver.get("https://www.facebook.com");
                        isLogin(webDriver,fbAccount);
                    }

                    return true;
                } else {
                    fbAccount.setNote("账号或密码无效");
                    fbAccountMapper.updateFbAccount(fbAccount);
                    return false;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public void createBM(FbAccount fbAccount) {
        try {
            if (webDriverMap.containsKey(fbAccount.getId())) {

                String mobile = DefuMobile.getMobile();

                WebDriver webDriver = webDriverMap.get(fbAccount.getId());
                webDriver.get("https://www.facebook.com/chinabusinesses/onboarding/684085081667854/?token=272483553796705");
                Thread.sleep(2000);
                WebDriverWait driverWait = new WebDriverWait(webDriver, 10, 1);
                WebElement mobileInput = null;
                try {
                    mobileInput = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div/div[2]/div/div/div[1]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (mobileInput != null) {
                    mobileInput.sendKeys(mobile);
                    //输入号码后创建广告账户按钮
                    WebElement createAdButton = webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div/div[2]/div/div/div[2]/div/span/div/div/div"));

                    createAdButton.click();

                    //检查手机号码是否可用
                    WebDriverWait driverWait1 = new WebDriverWait(webDriver, 5, 1);
                    //输入代码输入框
                    WebElement codeInput = null;
                    for (int i = 0; i < 10; i++) {
                        try {
                            //检查号码是否可用
                            codeInput = driverWait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[4]/div[1]/div[1]/div/div/div/div/div[2]/div[1]/div[2]/div/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input")));
                        } catch (Exception e) {

                        }
                        //codeInput为null，号码不可用
                        if (codeInput == null) {
                            //刷新
                            webDriver.navigate().refresh();
                            //重新获取新号码
                            mobile = DefuMobile.getMobile();
                            mobileInput.sendKeys(mobile);
                            createAdButton.click();
                        } else {
                            break;
                        }
                    }
                    String code = "";

                    for (int i = 0; i < 6; i++) {
                        code = DefuMobile.getCode(mobile);
                        if (code.equals("wait")) {
                            System.out.println("请求" + i + "次");
                            Thread.sleep(10000);
                        } else {
                            System.out.println(code);
                            break;
                        }
                    }

                    codeInput.sendKeys(code);
                    //输入代码后继续按钮
                    WebElement continueButton = webDriver.findElement(By.xpath("/html/body/div[4]/div[1]/div[1]/div/div/div/div/div[3]/div/div[2]/div/span/div/div/div"));

                    continueButton.click();
                }

                //选择行业
                WebElement selectIndustry = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[1]/div[1]/div/div[2]/div/div/div/div/div/div[1]/div[2]/div[1]/div/div/div")));

                selectIndustry.click();
                //选择电子商务行业
                WebElement industry = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[4]/div[1]/div[1]/div/div/div[1]/div[2]/div[2]/div[2]/div[7]/div/div/div[2]/div/div")));

                industry.click();

                WebElement license = null;

                try {
                    //营业执照上传框
                    license = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[1]/div[4]/div/div[2]/div/div/div/div/i")));
                    license.click();
                } catch (Exception e) {
                    //已经上传过一次图片后出现的更换按钮
                    license = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[1]/div[3]/div/span/div/div")));
                    license.click();
                }


                try {
                    // 创建Robot实例
                    Robot robot = new Robot();

                    // 等待一段时间以确保文件对话框已打开（可以根据实际情况调整等待时间）
                    Thread.sleep(1000);

                    // 输入文件路径
                    String filePath = "C:\\Users\\hbxzly\\Pictures\\Camera Roll\\微信截图_20230701211206.png"; // 替换成你要上传的文件的路径
                    // 将文件路径复制到剪贴板
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    StringSelection stringSelection = new StringSelection(filePath);
                    clipboard.setContents(stringSelection, null);

                    // 模拟粘贴操作
                    robot.keyPress(KeyEvent.VK_CONTROL);
                    robot.keyPress(KeyEvent.VK_V);
                    robot.keyRelease(KeyEvent.VK_V);
                    robot.keyRelease(KeyEvent.VK_CONTROL);

                    // 模拟按下Enter键，以确认文件选择
                    robot.keyPress(KeyEvent.VK_ENTER);
                    robot.keyRelease(KeyEvent.VK_ENTER);
                } catch (AWTException | InterruptedException e) {
                    e.printStackTrace();
                }


                String info = CompanyInfo.getInfo();
                Actions actions = new Actions(webDriver);
                //信用代码
                WebElement creditCode = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[1]/div[5]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input")));
                actions.click(creditCode).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
                actions.sendKeys(Keys.DELETE).perform();
                creditCode.sendKeys("91210106MA10AP108C");

                //公司名称
                WebElement companyName = webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[1]/div[6]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input"));
                actions.click(companyName).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
                actions.sendKeys(Keys.DELETE).perform();
                companyName.sendKeys(ReadJsonData.getInfoByNodeName(info, "Company_Name"));

                //公司邮箱
                WebElement companyEmail = webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[1]/div[8]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input"));
                actions.click(companyEmail).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
                actions.sendKeys(Keys.DELETE).perform();
                companyEmail.sendKeys(ReadJsonData.getInfoByNodeName(info, "Temporary_mail"));

                //公司地址
                WebElement companyAddress = webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[1]/div[9]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input"));
                actions.click(companyAddress).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
                actions.sendKeys(Keys.DELETE).perform();
                companyAddress.sendKeys(ReadJsonData.getInfoByNodeName(info, "Full_Name_Tran"));

                //邮编
                WebElement postCode = webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[1]/div[10]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input"));
                actions.click(postCode).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
                actions.sendKeys(Keys.DELETE).perform();
                postCode.sendKeys(ReadJsonData.getInfoByNodeName(info, "Zip_Code"));

                //地址拼音
                WebElement companyAddressEn = webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[1]/div[11]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input"));
                actions.click(companyAddressEn).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
                actions.sendKeys(Keys.DELETE).perform();
                companyAddressEn.sendKeys(ReadJsonData.getInfoByNodeName(info, "City"));

                //推广网站
                WebElement companyWebsite = webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[1]/div[12]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input"));
                actions.click(companyWebsite).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
                actions.sendKeys(Keys.DELETE).perform();
                companyWebsite.sendKeys("www.qq.com");

                //下一步
                WebElement nextStep = webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[3]/div/div/div/span/div/div/div"));
                nextStep.click();
                //账号名称
                WebElement adAccountName = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[1]/div/div[2]/div[1]/div[2]/div/div[3]/div[2]/div/div[1]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input")));
                actions.click(adAccountName).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
                actions.sendKeys(Keys.DELETE).perform();
                adAccountName.sendKeys("123");

                //推广网站
                WebElement promotionWebsite = webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div/div[2]/div[1]/div[2]/div/div[2]/div/div[1]/div/div[2]/div/div/div/div[1]/div[2]/div/div/input"));
                actions.click(promotionWebsite).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
                actions.sendKeys(Keys.DELETE).perform();
                promotionWebsite.sendKeys("www.qq.com");

                //下一步
                nextStep = webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[3]/div/div/div[3]/div/div[2]/div"));
                nextStep.click();
                //提交
                WebElement submitButton = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[1]/div/div[3]/div/div[2]/div")));
                submitButton.click();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 判断是否在线
     *
     * @param webDriver
     * @return
     */
    public String isLogin(WebDriver webDriver, FbAccount fbAccount) {
        boolean hasCUser = false;
        boolean hasPresence = false;
        Set<Cookie> cookies = webDriver.manage().getCookies();
        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        if (webDriver.getCurrentUrl().contains("/checkpoint/")){
            waitingForContent(5,webDriver,"xlink:href=\"https://scontent-tpe1-1.xx.fbcdn.net");
            if (webDriver.getPageSource().contains("/images/checkpoint/epsilon/comet/intro.png")){
                fbAccount.setNote("无法登录-账号被锁");
                fbAccount.setStatus("4");
                fbAccountMapper.updateFbAccount(fbAccount);
                return "false";
            }
            if (webDriver.getPageSource().contains("https://static.xx.fbcdn.net/rsrc.php/v3/yP/r/vpsJBY0EYuI.png")){
                fbAccount.setNote("无法登录-账号被封");
                fbAccount.setStatus("5");
                fbAccountMapper.updateFbAccount(fbAccount);
                return "false";
            }
            fbAccount.setStatus("4");
            fbAccountMapper.updateFbAccount(fbAccount);
            return "false";
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("c_user")) {
                if (fbAccount.getStatus() != "1"){
                    fbAccount.setStatus("1");
                    fbAccountMapper.updateFbAccount(fbAccount);
                }
                hasCUser = true; // 找到了c_user项
            }
            if (cookie.getName().equals("presence")) {
                if (fbAccount.getStatus() != "1"){
                    fbAccount.setStatus("1");
                    fbAccountMapper.updateFbAccount(fbAccount);
                }
                hasPresence = true;
            }
        }

        if (hasCUser && hasPresence) {
            if (fbAccount.getStatus() != "1"){
                fbAccount.setStatus("1");
                fbAccountMapper.updateFbAccount(fbAccount);
            }
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

    @Override
    public void showBrowser(FbAccount fbAccount) {

        Integer id = processMap.get(fbAccount.getId());
        User32 user32 = User32.INSTANCE;

        user32.EnumWindows((hWnd, arg1) -> {
            IntByReference processId = new IntByReference();
            user32.GetWindowThreadProcessId(hWnd, processId);
            int windowProcessId = processId.getValue();

            if (user32.IsWindowVisible(hWnd) && windowProcessId == id) {
                user32.ShowWindow(hWnd, User32.SW_MINIMIZE);
                user32.ShowWindow(hWnd, User32.SW_RESTORE);
                user32.SetForegroundWindow(hWnd);
            }
            return true;
        }, null);

    }

    /**
     * 获取任务栏窗口
     *
     * @return
     */
    public static List<Integer> listWindows() {
        List<Integer> processIdList = new ArrayList<>();
        User32.INSTANCE.EnumWindows(new WinUser.WNDENUMPROC() {
            @Override
            public boolean callback(WinDef.HWND hWnd, Pointer data) {
                // 获取窗口标题
                char[] windowText = new char[512];
                User32.INSTANCE.GetWindowText(hWnd, windowText, 512);
                String wText = Native.toString(windowText);


                // 获取窗口所属进程ID
                IntByReference processId = new IntByReference();
                User32.INSTANCE.GetWindowThreadProcessId(hWnd, processId);
                int windowProcessId = processId.getValue();
                // 检查窗口是否可见且有标题
                if (User32.INSTANCE.IsWindowVisible(hWnd) && wText.length() > 0) {
                    processIdList.add(windowProcessId);
                }

                return true;
            }
        }, null);
        return processIdList;
    }

    /**
     * 返回新开浏览器的进程ID
     *
     * @param beforeProcessIdList 打开之前的进程
     * @param afterProcessIdList  打开之后的进程
     * @return
     */
    private static Integer findExtraProcessId(List<Integer> beforeProcessIdList, List<Integer> afterProcessIdList) {

        Set<Integer> set = new HashSet<>(beforeProcessIdList);

        for (Integer process : afterProcessIdList) {
            if (!set.contains(process)) {
                return process;
            }
        }

        return null; // 没有多出来的对象
    }

    /**
     * 修改浏览器状态
     *
     * @param fbAccount
     * @param status
     */
    public void changeBrowserStatus(FbAccount fbAccount, String status) {
        fbAccountMapper.updateBrowserStatus(fbAccount, status);
    }

    @Override
    public void checkBM(String[] ids) {
        ArrayList<String> idList = new ArrayList<>();
        for (String id : ids) {
            idList.add(id);
            if (!webDriverMap.containsKey(id)) {
                openBrowser(fbAccountMapper.selectOneByFbAccountId(id));
            }
        }
        // 定义线程数量
        int threadCount = idList.size();
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < idList.size(); i++) {
            FbAccount fbAccount = fbAccountMapper.selectOneByFbAccountId(idList.get(i));
            threads[i] = new Thread(() -> {
                WebDriver webDriver = webDriverMap.get(fbAccount.getId());
                WebDriverWait webDriverWait = new WebDriverWait(webDriver, 20, 1);
                webDriver.get("https://business.facebook.com/settings");
                try {
                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div/div[2]/div/div/div/div/div/div[2]/div[1]/div[4]/span/span/div/div[2]/div/div/div/div[1]/div[2]/div/div/input")))
                            .sendKeys(getVerificationCode(fbAccount.getSecretKey()));
                    webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div/div[2]/div/div/div/div/div/div[3]/div[2]/div/div/span/div/div/div")).click();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            threads[i].start();
        }
    }

    @Override
    public void checkAccount(String[] ids) {
        ArrayList<String> idList = new ArrayList<>();
        for (String id : ids) {
            idList.add(id);
            if (!webDriverMap.containsKey(id)) {
                FbAccount fbAccount = fbAccountMapper.selectOneByFbAccountId(id);
                openBrowser(fbAccount);
                login(fbAccount);
            }
        }
        // 定义线程数量
        int threadCount = idList.size();
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < idList.size(); i++) {
            FbAccount fbAccount = fbAccountMapper.selectOneByFbAccountId(idList.get(i));
            threads[i] = new Thread(() -> {
                webDriverMap.get(fbAccount.getId()).get("https://www.facebook.com/business-support-home/" + fbAccount.getId());
            });
            threads[i].start();
        }
    }

    /**
     * 批量添加好友
     *
     * @param id
     * @param accountIds
     */
    @Override
    public void batchAddFriend(String id, String[] accountIds) {
        if (!webDriverMap.containsKey(id)) {
            FbAccount fbAccount = fbAccountMapper.selectOneByFbAccountId(id);
            openBrowser(fbAccount);
            login(fbAccount);
            WebDriver webDriver = webDriverMap.get(fbAccount.getId());
            WebDriverWait wait = new WebDriverWait(webDriver, 30, 1);
            if (webDriverMap.get(fbAccount.getId()).getPageSource().contains("永久停用")) {
                closeBrowser(fbAccount);
                return;
            }
            for (String accountId : accountIds) {
                try {
                    webDriverMap.get(fbAccount.getId()).get("https://www.facebook.com/" + accountId);
                    webDriverMap.get(fbAccount.getId()).manage().window().maximize();
                    Thread.sleep(20000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String pageSource = webDriver.getPageSource();
                waitingForContent(10,webDriver,"https://static.xx.fbcdn.net/rsrc.php/v3/yK/r/r2FA830xjtI.png");
                try {
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@src='https://static.xx.fbcdn.net/rsrc.php/v3/yK/r/r2FA830xjtI.png']"))).click();
                } catch (Exception e) {
                    System.out.println("没添加成功");
                    e.printStackTrace();
                }

               /* if (!webDriverMap.get(fbAccount.getId()).getPageSource().contains("取消")) {

                    try {
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@aria-label='添加好友']")))
                                .click();
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
                try {
//                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div[4]/div/div/div[1]/div/div[2]/div/div/div/div[4]/div[1]/div/div/div[1]/div[2]/span/span")))
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-label='添加好友'], div[aria-label='Add friend'], div[aria-label='加朋友']")))
                            .click();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String pageSource = webDriver.getPageSource();
                if (!(pageSource.contains("取消邀請") || pageSource.contains("取消请求") || pageSource.contains("Cancel request"))){
                    try {
                        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div[aria-label='加为好友'], div[aria-label='Add friend'], div[aria-label='加朋友']")))
                                .click();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/
            }
            closeBrowser(fbAccount);
        }
    }


    @Override
    public void openAdvertise(FbAccount fbAccount, String adAccountId) {
        // 获取今天的日期
        LocalDate today = LocalDate.now();
        // 获取明天的日期
        LocalDate tomorrow = today.plusDays(1);
        // 将日期格式化为字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayStr = today.format(formatter);
        String tomorrowStr = tomorrow.format(formatter);
        openBrowser(fbAccount);
        WebDriver webDriver = webDriverMap.get(fbAccount.getId());
        if (isLogin(webDriver,fbAccount).equals("false")) {
            login(fbAccount);
        }
        if (webDriver != null && !webDriver.getPageSource().contains(adAccountId)) {
            webDriver.get("https://adsmanager.facebook.com/adsmanager/manage/ads?act=" + adAccountId + "&date=" + todayStr + "_" + tomorrowStr);
        }

    }

    @Override
    public void openScreenshotPage(Long[] keyIds) {
        ArrayList<Advertise> advertises = new ArrayList<>();
        Set<String> accountIds = new HashSet<>();
        for (Long keyId : keyIds) {
            Advertise advertise = advertiseService.selectAdvertiseByKeyId(keyId);
            advertises.add(advertise);
            accountIds.add(advertise.getAuthorizedAccount());
        }
        for (String id : accountIds) {
            if (!webDriverMap.containsKey(id)) {
                openBrowser(fbAccountMapper.selectOneByFbAccountId(id));
            }
        }
        for (String id : accountIds) {
            if (!webDriverMap.containsKey(id)) {
                login(fbAccountMapper.selectOneByFbAccountId(id));
            }
        }

        String[] accountIdArray = accountIds.toArray(new String[0]);
        int threadCount = accountIdArray.length;
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < accountIds.size(); i++) {
            FbAccount fbAccount = fbAccountMapper.selectOneByFbAccountId(accountIdArray[i]);
            threads[i] = new Thread(() -> login(fbAccount));
            threads[i].start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (Advertise advertise : advertises) {
            WebDriver webDriver = webDriverMap.get(advertise.getAuthorizedAccount());
            Set<String> windowHandles = webDriver.getWindowHandles();
            Map<String, String> windowAndURLMap = new HashMap<>();
            // 遍历每个窗口句柄
            for (String handle : windowHandles) {
                // 切换到窗口
                webDriver.switchTo().window(handle);

                // 获取当前窗口的 URL
                String currentURL = webDriver.getCurrentUrl();
                windowAndURLMap.put(handle, currentURL);
            }

            boolean isOpen = false;
            for (String key : windowAndURLMap.keySet()) {
                if (windowAndURLMap.get(key).contains("ads?") && windowAndURLMap.get(key).contains("act=" + advertise.getAdAccountId())) {
                    webDriver.switchTo().window(key);
                    isOpen = true;
                }
            }

            if (!isOpen) {
                String scriptScreenshotUrl = "window.open('" + AdvertiseUrl.screenshotUrl(advertise) + "','_blank');";
                String scriptBalanceUrl = "window.open('" + AdvertiseUrl.balanceUrl(advertise) + "','_blank');";
                ((ChromeDriver) webDriver).executeScript(scriptScreenshotUrl);
                ((ChromeDriver) webDriver).executeScript(scriptBalanceUrl);
            }
        }


    }

    @Override
    public void updateBrowserProfile(String id) throws AWTException {
        if (!webDriverMap.containsKey(id)) {
            WebDriver webDriver = openBrowser(fbAccountMapper.selectOneByFbAccountId(id));
            webDriver.get("chrome://settings/manageProfile");

            // 创建 Robot 对象
            Robot robot = new Robot();
            // 循环按下和释放Tab键6次
            for (int i = 0; i < 6; i++) {
                robot.keyPress(KeyEvent.VK_TAB);
                robot.keyRelease(KeyEvent.VK_TAB);
            }
            // 将字符串拆分为字符数组
            char[] chars = id.toCharArray();

            // 循环模拟键盘输入每个数字
            for (char c : chars) {
                // 转换字符为对应的数字键的虚拟键码
                int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);

                // 检查虚拟键码是否有效，然后模拟按下和释放键盘按键
                if (keyCode != KeyEvent.VK_UNDEFINED) {
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                }
            }
            // 模拟按下 Tab 键
            robot.keyPress(KeyEvent.VK_TAB);
            // 模拟释放 Tab 键
            robot.keyRelease(KeyEvent.VK_TAB);

        } else {
            WebDriver webDriver = webDriverMap.get(id);
            webDriver.get("chrome://settings/manageProfile");

            // 创建 Robot 对象
            Robot robot = new Robot();
            // 循环按下和释放Tab键6次
            for (int i = 0; i < 6; i++) {
                robot.keyPress(KeyEvent.VK_TAB);
                robot.keyRelease(KeyEvent.VK_TAB);
            }
            // 将字符串拆分为字符数组
            char[] chars = id.toCharArray();

            // 循环模拟键盘输入每个数字
            for (char c : chars) {
                // 转换字符为对应的数字键的虚拟键码
                int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);

                // 检查虚拟键码是否有效，然后模拟按下和释放键盘按键
                if (keyCode != KeyEvent.VK_UNDEFINED) {
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                }
            }
            // 模拟按下 Tab 键
            robot.keyPress(KeyEvent.VK_TAB);
            // 模拟释放 Tab 键
            robot.keyRelease(KeyEvent.VK_TAB);
        }
    }

    @Override
    public void openAds(String id) {
        WebDriver webDriver = webDriverMap.get(id);
        webDriver.get("https://adsmanager.facebook.com/adsmanager/manage");
    }

    @Override
    public void loadAdAccountInfo(String id) {

        WebDriver webDriver = webDriverMap.get(id);
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 20, 10);
        try {
            showBrowser(fbAccountMapper.selectOneByFbAccountId(id));
            //判断新旧版本
            String isOldMark = "x78zum5 x2lwn1j xeuugli x1exxf4d x1y71gwh x1nb4dca xu1343h x13fuv20 xu3j5b3 x1q0q8m5 x26u7qi x178xt8z xm81vs4 xso031l xy80clv x10l6tqk";
            String pageSource = webDriver.getPageSource();
            //旧版本
            if (pageSource.contains(isOldMark)) {

                webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div/div/div[1]/div/span/div/span[1]/div/div/div/div[2]/div/div[1]/div/div/div/div/div[1]/div/div[1]/div[2]/div/div/div/div")))
                        .click();
                try {
                    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/div/div/div/div[2]/div[1]/div[1]/div/div/div/div/div/div/li[2]/div/div/div/span/div/div/div")))
                            .click();
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div/div/div[3]/div/div/div/div/div[2]/div[1]/div[1]/div/div/div/div/div/div/li[2]/div/div/div/span/div/div/div")))
                            .click();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Thread.sleep(5000);
                String adPageSource = webDriver.getPageSource();
                //以字符串形式返回：个人号ID-广告账户名称-广告账户ID
                List<String> adAccountAndBmInfo = ReadJsonData.getAdAccountBmInfo(id, adPageSource);
                //数据持久化
                for (String str : adAccountAndBmInfo) {
                    String[] split = str.split("-");
                    AccountAdAccountBmInfo accountAdAccountBmInfo = new AccountAdAccountBmInfo();
                    accountAdAccountBmInfo.setAccountId(split[0]);
                    accountAdAccountBmInfo.setAdAccountName(split[1]);
                    accountAdAccountBmInfo.setAdAccountId(split[2]);
                    if (!(accountAdAccountBmInfoService.selectAccountAdAccountBmInfoList(accountAdAccountBmInfo).size() > 0)) {
                        accountAdAccountBmInfoService.insertAccountAdAccountBmInfo(accountAdAccountBmInfo);
                    }
                }
                // 创建Robot对象
                Robot robot = new Robot();
                // 模拟按下ESC键
                robot.keyPress(KeyEvent.VK_ESCAPE);
                // 模拟释放ESC键
                robot.keyRelease(KeyEvent.VK_ESCAPE);
            }
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div/div/div/div/span/div/span[1]/div/div/div/div[2]/div/div[1]/div/div/div/div/div[1]/div/div[1]/div[2]/div/div/div/div/div/div/div[1]"))).click();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getWindowHandles(String id) {
        WebDriver webDriver = webDriverMap.get(id);
        // 获取所有窗口句柄
        Set<String> windowHandles = webDriver.getWindowHandles();

        // 遍历每个窗口句柄
        for (String handle : windowHandles) {
            // 切换到窗口
            webDriver.switchTo().window(handle);

            // 获取当前窗口的 URL
            String currentURL = webDriver.getCurrentUrl();

            // 打印句柄和网址
            System.out.println("Window Handle: " + handle);
            System.out.println("URL: " + currentURL);
        }
    }

    @Override
    public void getAccountName(String[] ids) {
        ArrayList<String> idList = new ArrayList<>();
        for (String id : ids) {
            idList.add(id);
            if (!webDriverMap.containsKey(id)) {
                openBrowser(fbAccountMapper.selectOneByFbAccountId(id));
            }
        }
        // 定义线程数量
        int threadCount = idList.size();
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < idList.size(); i++) {
            FbAccount fbAccount = fbAccountMapper.selectOneByFbAccountId(idList.get(i));
            threads[i] = new Thread(() -> {
                WebDriver webDriver = webDriverMap.get(fbAccount.getId());
                webDriver.get("https://www.facebook.com");
                WebDriverWait webDriverWait = new WebDriverWait(webDriver, 20, 1);
                String name = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div[3]/div/div/div[1]/div[1]/div/div[1]/div/div/div[1]/div/div/div[1]/div[1]/ul/li[1]/div/a/div[1]/div[2]/div/div/div/div/span/span"))).getText();
                fbAccount.setName(name);
                fbAccountMapper.updateFbAccount(fbAccount);
            });
            threads[i].start();
        }
    }

    @Override
    public void unlimitedAccountOneStep(String[] ids) {
        Thread[] threads = new Thread[ids.length];
        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            WebDriver webDriver = webDriverMap.get(id);
            WebDriverWait webDriverWait = new WebDriverWait(webDriver, 50, 1);
            threads[i] = new Thread(() -> {
                webDriver.get("https://www.facebook.com/business-support-home/" + id);
                try {
                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div/div/span/div/div/div[2]/div/div[2]/div[2]/div/div[1]/div[2]/div/div/span/div/div/div")))
                            .click();
                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/span/div/div[1]/div[1]/div/div/div/div/div/div[1]/div[2]/div[2]/div/div/div[3]/div/div[2]/div/span/div/div/div")))
                            .click();
                } catch (Exception e) {
                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div/div/span/div/div/div[2]/div/div[2]/div[2]/div/div/div[2]/div/div/span/div/div/div")))
                            .click();
                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div/div/div/div/div/div[2]/div/div/div[1]/div/div/div[1]/div/div/div/div/div/div/div/div[2]/div/div/div[2]/div/div/div/div/div[1]/div/span/span")))
                            .click();
                    e.printStackTrace();
                }
            });
            threads[i].start();
        }
    }

    @Override
    public void unlimitedAccountTwoStep(String[] ids) {
    }

    /**
     * 获取账号状态信息
     *
     * @param fbAccount
     * @return
     */
    @Override
    public void checkAccountInfo(FbAccount fbAccount) {

        openBrowser(fbAccount);
        login(fbAccount);
        WebDriver webDriver = webDriverMap.get(fbAccount.getId());
        String loginResult = isLogin(webDriver,fbAccount);
        System.out.println(loginResult);
        if (loginResult.equals("true")) {
            // 通过JavaScriptExecutor进行页面滚动
            for (int i = 0; i < 3; i++) {
                JavascriptExecutor js = (JavascriptExecutor) webDriver;
                js.executeScript("window.scrollBy(0,500)");// 向下滚动500像素
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (loginResult.equals("false")) {
            checkAccountInfo(fbAccount);
        }
        if (loginResult.equals("disable")) {
            if (webDriver.getCurrentUrl().contains("/checkpoint")) {
                if (webDriver.getPageSource().contains("立即") || webDriver.getPageSource().contains("开始") ||
                        webDriver.getPageSource().contains("has been locked")) {
                    fbAccount.setStatus("4");
                } else {
                    fbAccount.setStatus("5");
                }
            }
        }
        fbAccountMapper.updateFbAccount(fbAccount);
        closeBrowser(fbAccount);
    }

    /**
     * 修改账号密码
     *
     * @param fbAccount
     * @return
     */
    @Override
    public FbAccount changePassword(FbAccount fbAccount) {
        WebDriver webDriver = webDriverMap.get(fbAccount.getId());
        WebDriverWait wait = new WebDriverWait(webDriver, 20, 1);
        webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div/div[2]/div[2]/form/div[2]/div[4]/div/a"))
                .click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div/div[2]/div/div/form/div/div[2]/div/table/tbody/tr[2]/td[2]/input")))
                .sendKeys(fbAccount.getEmail());
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div/div[2]/div/div/form/div/div[3]/div/div[1]/button")))
                .click();
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div/div[3]/div[2]/form/div[2]/div[4]/a")))
                    .click();
        } catch (Exception e) {
            e.printStackTrace();
        }

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div/div[2]/form/div/div[3]/div/div[1]/button")))
                .click();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String message = EmailUtil.getMessage(fbAccount);
        String verificationCode = EmailUtil.getVerificationCode(message);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div/div[2]/form/div/div[2]/div[3]/div[1]/input")))
                .sendKeys(verificationCode);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div/div[2]/form/div/div[3]/div/div[1]/button")))
                .click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div/div[2]/form/div/div[2]/div[2]/div[1]/div/input")))
                .sendKeys(fbAccount.getPassword() + ".");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div/div[2]/form/div/div[3]/div/div[1]/button[2]")))
                .click();
        try {
            WebElement approvalsCode = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/div/div[1]/div[1]/div/div[2]/div[2]/div/div/div/div/div[3]/div/form/div/div/div/div/div[1]/input")));
            approvalsCode.sendKeys(getVerificationCode(fbAccount.getSecretKey()));
            WebElement submitButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/div/div[1]/div[1]/div/div[2]/div[2]/div/div/div/div/div[3]/div/div[1]/div/div/div/div[1]/div/span/span")));
            submitButton.click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div[3]/div/div/div[1]/div[1]/div[2]/div/div/div[3]/div[1]/div/div/div/div[1]/div/span/span")))
                    .click();
        } catch (Exception e) {
            try {
                WebElement submitButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("checkpointSubmitButton")));
                WebElement approvalsCode = webDriver.findElement(By.id("approvals_code"));
                approvalsCode.sendKeys(getVerificationCode(fbAccount.getSecretKey()));
                submitButton.click();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        fbAccount.setPassword(fbAccount.getPassword() + ".");
        return fbAccount;
    }

    /**
     * 解锁账号
     *
     * @param fbAccount
     */
    @Override
    public void unlockAccount(FbAccount fbAccount) {
        WebDriver webDriver = webDriverMap.get(fbAccount.getId());
        WebDriverWait wait = new WebDriverWait(webDriver, 20, 1);
        webDriver.findElement(By.xpath("/html/body/div[1]/div/div/div/div/div/div/div[2]/div/div/div[1]/div/div/div[1]/div/div/div/div/div/div/div/div/div/div/div/div/div[5]/div/div/div[1]/div/span/span"))
                .click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div/div/div/div/div/div[2]/div/div/div[1]/div/div/div[1]/div/div/div/div/div/div/div/div/div/div/div/div/div[4]/div/div[2]/div/div")))
                .click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div/div/div/div/div/div[2]/div/div/div[1]/div/div/div[1]/div/div/div/div/div/div/div/div/div/div/div/div/div[2]/div/div[1]/div/div[1]/div[2]/div[1]/div/div/div/span")))
                .click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div/div/div/div/div/div[2]/div/div/div[1]/div/div/div[1]/div/div/div/div/div/div/div/div/div/div/div/div/div[4]/div/div[2]/div/div/div[1]/div/span/span")))
                .click();
        String message = EmailUtil.getMessage(fbAccount);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        message = EmailUtil.getMessage(fbAccount);
        System.out.println("邮件内容：" + message);
        String verificationCode = EmailUtil.getVerificationCode(message);
        System.out.println("验证码：" + verificationCode);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div/div/div/div/div/div[2]/div/div/div[1]/div/div/div[1]/div/div/div/div/div/div/div/div/div/div/div/div/div[2]/div/div/div/div/div[1]/div/label/input")))
                .sendKeys(verificationCode);
        //等待两秒
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div/div/div/div/div/div[2]/div/div/div[1]/div/div/div[1]/div/div/div/div/div/div/div/div/div/div/div/div/div[4]/div/div[2]/div/div/div[1]/div/span/span")))
                .click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div/div/div/div[2]/div/div/div[1]/div/div/div[1]/div/div/div/div/div/div/div/div/div/div/div/div/div[4]/div/div/div/div/div[1]/div/span/span")))
                .click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div/div/div/div/div/div[2]/div/div/div[1]/div/div/div[1]/div/div/div/div/div/div/div/div/div/div/div/div/div[5]/div/div[2]/div/div/div[1]/div/span/span")))
                .click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div/div/div/div/div/div[2]/div/div/div[1]/div/div/div[1]/div/div/div/div/div/div/div/div/div/div/div/div/div[4]/div/div/div[1]/div/span/span")))
                .click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div/div/div/div/div/div[2]/div/div/div[1]/div/div/div[1]/div/div/div/div/div/div/div/div/div/div/div/div/div[6]/a/div/div[1]/div/span/span")))
                .click();

    }

    /**
     * 创主页
     *
     * @param fbAccount
     * @param pageName
     */
    @Override
    public void createPage(FbAccount fbAccount, String pageName, Avatar avatar, Background background, List<Posts> postsList) throws InterruptedException {
        openBrowser(fbAccount);
        login(fbAccount);
        showBrowser(fbAccount);
        WebDriver webDriver = webDriverMap.get(fbAccount.getId());
        webDriver.get(CreatePageConstants.CREATE_PAGE_WEBSITE);
        Thread.sleep(5000);
        String pageSource = webDriver.getPageSource();
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30, 1);
        OperationLog operationLog = new OperationLog();
        operationLog.setOperationAccount(fbAccount.getId());
        operationLog.setOperationContent("create page:" + pageName);
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ESCAPE);
            robot.keyRelease(KeyEvent.VK_ESCAPE);
            Thread.sleep(1000);
            // 按下方向键“下”键
            robot.keyPress(KeyEvent.VK_DOWN);
            robot.keyRelease(KeyEvent.VK_DOWN);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        //创主页
        try {
            //主页名
            String pageNameXpath = "//input[@dir='ltr' and @autocomplete='off' and @type='text']";
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(pageNameXpath)))
                    .sendKeys(pageName);
            Thread.sleep(1000);
            //类别
            String pageTypeXpath = "//input[@style='width: 80px;']";
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(pageTypeXpath)))
                    .sendKeys("服装");
            Thread.sleep(2000);

            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(CreatePageConstants.CREATE_PAGE_SELECT_CLOTHES_XPATH))).click();
//            try {
//                Robot robot = new Robot();
//                // 按下方向键“下”键
//                robot.keyPress(KeyEvent.VK_DOWN);
//                robot.keyRelease(KeyEvent.VK_DOWN);
//                robot.keyPress(KeyEvent.VK_ENTER);
//                robot.keyRelease(KeyEvent.VK_ENTER);
//            } catch (AWTException e) {
//                e.printStackTrace();
//            }
//            Thread.sleep(2000);
            //简介
            String pageIntroductionXpath = WebPageUtil.getXpathBySelector(pageSource, CreatePageConstants.CREATE_PAGE_PAGE_INTRODUCTION_XPATH);
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(pageIntroductionXpath)))
                    .sendKeys(avatar.getNote());
            Thread.sleep(1000);
            //建立按钮
            String createPageButton = WebPageUtil.getXpathBySourceCode(pageSource, CreatePageConstants.CREATE_PAGE_CREATE_BUTTON_SOURCE_CODE);
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(createPageButton))).click();
            Thread.sleep(10000);
            for (int i = 0; i < 11; i++) {
                pageSource = webDriver.getPageSource();
                if (pageSource.contains("完成設定粉絲專頁")){
                    operationLog.setOperationStatus("create success");
                    operationLog.setOperationTime(new Date());
                    operationLogService.insertOperationLog(operationLog);
                    break;
                }
                if (i==10){
                    operationLog.setOperationStatus("create failed");
                    operationLog.setOperationTime(new Date());
                    operationLogService.insertOperationLog(operationLog);
                }
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            operationLog.setOperationStatus("create failed");
            operationLog.setOperationTime(new Date());
            operationLogService.insertOperationLog(operationLog);
            e.printStackTrace();
            return;
        }
        //设置营业时间
        try {
//            pageSource = webDriver.getPageSource();
//            String contactInformation = WebPageUtil.getXpathBySourceCode(pageSource, CreatePageConstants.CREATE_PAGE_INFORMATION_SOURCE_CODE);
//            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(contactInformation))).click();

            clickAtCoordinates(webDriver,100,240);
            try {
                Robot robot = new Robot();
                robot.keyPress(KeyEvent.VK_PAGE_DOWN);
                robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread.sleep(2000);
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(CreatePageConstants.CREATE_PAGE_OPENING_TIME_XPATH))).click();
            pageSource = webDriver.getPageSource();

            //下一步
            String secondPageContinueButton = WebPageUtil.getXpathBySourceCode(pageSource, CreatePageConstants.CREATE_PAGE_SECOND_PAGE_CONTINUE_BUTTON_SOURCE_CODE);
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(secondPageContinueButton))).click();
            Thread.sleep(5000);
            operationLog.setOperationContent(pageName + "设置营业时间");
            operationLog.setOperationStatus("设置成功");
            operationLog.setOperationTime(new Date());
            operationLogService.insertOperationLog(operationLog);
        } catch (Exception e) {
            operationLog.setOperationStatus(pageName + "设置营业时间");
            operationLog.setOperationStatus("设置失败");
            operationLog.setOperationTime(new Date());
            //下一步
            String secondPageContinueButton = WebPageUtil.getXpathBySourceCode(pageSource, CreatePageConstants.CREATE_PAGE_SECOND_PAGE_CONTINUE_BUTTON_SOURCE_CODE);
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(secondPageContinueButton))).click();
            operationLogService.insertOperationLog(operationLog);
            e.printStackTrace();
        }
        //添加头像背景
        try {
            pageSource = webDriver.getPageSource();
            //头像
            String uploadAvatar = WebPageUtil.getXpathBySourceCode(pageSource, CreatePageConstants.CREATE_PAGE_UPLOAD_AVATAR_SOURCE_CODE);
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(uploadAvatar))).click();
            try {
                // 创建Robot实例
                Robot robot = new Robot();

                // 等待一段时间以确保文件对话框已打开（可以根据实际情况调整等待时间）
                Thread.sleep(1000);

                // 输入文件路径
                String filePath = avatar.getFilePath() + avatar.getAvatarName(); // 替换成你要上传的文件的路径
                // 将文件路径复制到剪贴板
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection stringSelection = new StringSelection(filePath);
                clipboard.setContents(stringSelection, null);

                // 模拟粘贴操作
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_CONTROL);

                // 模拟按下Enter键，以确认文件选择
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
            } catch (AWTException | InterruptedException e) {
                e.printStackTrace();
            }
            Thread.sleep(10000);
            //封面
            String uploadBackground = WebPageUtil.getXpathBySourceCode(pageSource, CreatePageConstants.CREATE_PAGE_UPLOAD_BACKGROUND_SOURCE_CODE);
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(uploadBackground))).click();

            try {
                // 创建Robot实例
                Robot robot = new Robot();

                // 等待一段时间以确保文件对话框已打开（可以根据实际情况调整等待时间）
                Thread.sleep(1000);

                // 输入文件路径
                String filePath = background.getFilePath() + background.getBackgroundName(); // 替换成你要上传的文件的路径
                // 将文件路径复制到剪贴板
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection stringSelection = new StringSelection(filePath);
                clipboard.setContents(stringSelection, null);

                // 模拟粘贴操作
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_CONTROL);

                // 模拟按下Enter键，以确认文件选择
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
            } catch (AWTException | InterruptedException e) {
                e.printStackTrace();
            }
            Thread.sleep(10000);
            //第三页下一步
            String thirdPageContinueButton = WebPageUtil.getXpathBySourceCode(pageSource, CreatePageConstants.CREATE_PAGE_THIRD_PAGE_CONTINUE_BUTTON_SOURCE_CODE);
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(thirdPageContinueButton))).click();
            operationLog.setOperationContent(pageName + "设置主页头像背景");
            operationLog.setOperationStatus("设置成功");
            operationLog.setOperationTime(new Date());
            operationLogService.insertOperationLog(operationLog);
        } catch (Exception e) {
            operationLog.setOperationContent(pageName + "设置主页头像背景");
            operationLog.setOperationStatus("设置失败");
            operationLog.setOperationTime(new Date());
            operationLogService.insertOperationLog(operationLog);
            //第三页下一步
            String thirdPageContinueButton = WebPageUtil.getXpathBySourceCode(pageSource, CreatePageConstants.CREATE_PAGE_THIRD_PAGE_CONTINUE_BUTTON_SOURCE_CODE);
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(thirdPageContinueButton))).click();
            e.printStackTrace();
        }
        //进入主页
        try {
            Thread.sleep(10000);
            //第四页跳过
            pageSource = webDriver.getPageSource();
            String fourthPageSkipButton = WebPageUtil.getXpathBySourceCode(pageSource, CreatePageConstants.CREATE_PAGE_FOURTH_PAGE_SKIP_BUTTON_SOURCE_CODE);
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(fourthPageSkipButton))).click();
            Thread.sleep(10000);
            pageSource = webDriver.getPageSource();
            //第五页继续
            if (pageSource.contains("拓展粉絲專頁粉絲群")) {
                String fifthPageContinueButton = WebPageUtil.getXpathBySourceCode(pageSource, CreatePageConstants.CREATE_PAGE_FIFTH_PAGE_CONTINUE_BUTTON_SOURCE_CODE);
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(fifthPageContinueButton)))
                        .click();
            }
            Thread.sleep(10000);
            pageSource = webDriver.getPageSource();
            String fifthPageFinishButton = WebPageUtil.getXpathBySourceCode(pageSource, CreatePageConstants.CREATE_PAGE_FIFTH_PAGE_FINISH_BUTTON_SOURCE_CODE);
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(fifthPageFinishButton))).click();
            Thread.sleep(10000);
            //关闭对话框
            pageSource = webDriver.getPageSource();
            String talkLaterButton = WebPageUtil.getXpathBySourceCode(pageSource, CreatePageConstants.CREATE_PAGE_TALK_LATER_BUTTON_SOURCE_CODE);
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(talkLaterButton))).click();
            operationLog.setOperationContent(pageName + "进入主页");
            operationLog.setOperationStatus("进入成功");
            operationLog.setOperationTime(new Date());
            operationLogService.insertOperationLog(operationLog);
        } catch (Exception e) {
            operationLog.setOperationContent(pageName + "进入主页");
            operationLog.setOperationStatus("进入失败");
            operationLog.setOperationTime(new Date());
            operationLogService.insertOperationLog(operationLog);
            e.printStackTrace();
        }
        //发帖
        int postsNumber = 0;
        try {
            for (Posts posts : postsList) {
                postsNumber = ++postsNumber;
                pageSource = webDriver.getPageSource();
                String pictureButton = WebPageUtil.getXpathBySourceCode(pageSource, CreatePageConstants.CREATE_PAGE_PICTURE_BUTTON_SOURCE_CODE);
                webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath(pictureButton))).click();
                Thread.sleep(3000);
                pageSource = webDriver.getPageSource();
                String postsContent = WebPageUtil.getXpathBySourceCode(pageSource, CreatePageConstants.CREATE_PAGE_POSTS_CONTENT_INPUT_SOURCE_CODE);
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(postsContent))).sendKeys(posts.getContent());
                String addPictureButton = WebPageUtil.getXpathBySourceCode(pageSource, CreatePageConstants.CREATE_PAGE_ADD_PICTURE_BUTTON_SOURCE_CODE);
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(addPictureButton))).click();
                // 创建Robot实例
                Robot robot = new Robot();
                // 等待一段时间以确保文件对话框已打开（可以根据实际情况调整等待时间）
                Thread.sleep(1000);
                // 输入文件路径
                String filePath = posts.getFilePath() + posts.getPictureName(); // 替换成你要上传的文件的路径
                // 将文件路径复制到剪贴板
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection stringSelection = new StringSelection(filePath);
                clipboard.setContents(stringSelection, null);
                // 模拟粘贴操作
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                // 模拟按下Enter键，以确认文件选择
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
                Thread.sleep(5000);
                String postsButton = WebPageUtil.getXpathBySourceCode(pageSource, CreatePageConstants.CREATE_PAGE_POSTS_BUTTON_SOURCE_CODE);
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(postsButton))).click();
                Thread.sleep(2000);
                pageSource = webDriver.getPageSource();
                if (pageSource.contains("發佈原始貼文")){
                    String postOriginalButton = WebPageUtil.getXpathBySourceCode(pageSource, CreatePageConstants.CREATE_PAGE_POSTS_ORIGINAL_POST_BUTTON_SOURCE_CODE);
                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(postOriginalButton))).click();
                }
                if (pageSource.contains("稍後再說")){
                    String postTalkLaterButton = WebPageUtil.getXpathBySourceCode(pageSource, CreatePageConstants.CREATE_PAGE_POSTS_TALK_LATER_POST_BUTTON_SOURCE_CODE);
                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(postTalkLaterButton))).click();
                }
                Thread.sleep(10000);
                operationLog.setOperationContent(pageName + "发第"+postsNumber+"贴");
                operationLog.setOperationStatus("发帖成功");
                operationLog.setOperationTime(new Date());
                operationLogService.insertOperationLog(operationLog);
            }
        } catch (Exception e) {
            operationLog.setOperationContent(pageName + "发第"+postsNumber+"贴");
            operationLog.setOperationStatus("发帖失败");
            operationLog.setOperationTime(new Date());
            operationLogService.insertOperationLog(operationLog);
            e.printStackTrace();
        }


    }

    /**
     * 为邮箱打开浏览器
     * @param email
     * @return
     */
    @Override
    public WebDriver openBrowserForEmail(Email email) {
        try {
            //参数配置
            System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
            ChromeOptions option = new ChromeOptions();
//            option.addArguments("--user-data-dir=" + fbAccountForSell.getFilePath() + fbAccountForSell.getBrowserProfile());
            option.addArguments("--remote-allow-origins=*");
            option.addArguments("--disable-notifications");//限制通知
            option.setExperimentalOption("useAutomationExtension", false);
            option.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
            WebDriver webDriver = new ChromeDriver(option);
            return webDriver;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //获取账号名称
    public static String getAccountName(String html) {
        String name = "";
        try {
            // 从URL加载整个页面
            Document doc = Jsoup.parse(html);

            // 使用CSS选择器选择特定的<div>标签
            Elements divs = doc.select("div.x1xqt7ti.xm46was.x1jrz1v4.xbsr9hj.xuxw1ft.x6ikm8r.x10wlt62.xlyipyv.x1h4wwuj.x117nqv4.xeuugli");

            // 遍历所有匹配的<div>标签
            for (Element div : divs) {
                name = div.text();
            }
        } catch (Exception e) {
            name = "获取失败";
            e.printStackTrace();
        }
        return name;
    }

    // 模拟鼠标点击指定坐标位置
    public static void clickAtCoordinates(WebDriver driver, int x, int y) {
        // 使用Actions类模拟鼠标点击
        Actions actions = new Actions(driver);
        // 移动鼠标到指定坐标，并点击
        actions.moveByOffset(x, y).click().perform();
    }

    //等待页面加载
    public boolean waitingForContent(int time, WebDriver webDriver, String content) {
        for (int i = 0; i < time; i++) {
            if (webDriver.getPageSource().contains(content)) {
                return true;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
