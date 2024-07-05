package com.ruoyi.account.service.impl;

import com.ruoyi.account.domain.AccountAdAccountBmInfo;
import com.ruoyi.account.domain.Advertise;
import com.ruoyi.account.domain.FbAccount;
import com.ruoyi.account.mapper.FbAccountMapper;
import com.ruoyi.account.service.IAccountAdAccountBmInfoService;
import com.ruoyi.account.service.IAdvertiseService;
import com.ruoyi.account.service.ISeleniumService;
import com.ruoyi.account.util.*;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.unix.Reboot;
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
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class ISeleniumServiceImpl implements ISeleniumService {

    @Autowired
    private FbAccountMapper fbAccountMapper;

    @Autowired
    private IAdvertiseService advertiseService;

    @Autowired
    private IAccountAdAccountBmInfoService accountAdAccountBmInfoService;

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
                option.addArguments("--disable-notifications");
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
        if (!isLogin(webDriver)) {
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
                email.sendKeys(fbAccount.getEmail());
                password.click();
                // 模拟Ctrl+A组合键
                actions.keyDown(Keys.CONTROL)
                        .sendKeys("a")
                        .keyUp(Keys.CONTROL)
                        .perform();
                Thread.sleep(500);
                actions.sendKeys(Keys.DELETE).perform();
                password.sendKeys(fbAccount.getPassword());
            } catch (Exception e) {
                e.printStackTrace();
            }
            WebElement loginButton = webDriver.findElement(By.name("login"));
            loginButton.click();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (webDriver.getPageSource().contains("查看通知")) {
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/div/div[1]/div[1]/div/div[2]/div[2]/div/div/div/div/div/div[4]/div/div/div/div[1]/div/span/span")))
                        .click();
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div[3]/div/div/div[1]/div/div[2]/div/div/div/div/div/div/div[3]/div[2]/div[4]/div/div/div[2]/div/div/div/div/label[2]/div[1]/div/div[2]/div/input")))
                        .click();
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div[3]/div/div/div[1]/div/div[2]/div/div/div/div/div/div/div[4]/div[3]/div/div/div/div/div/div/div/div/div[1]/div/span/span")))
                        .click();
            }
            //新版双重验证码输入
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
                    if (webDriver.getCurrentUrl().contains("/checkpoint")) {
                        if (webDriver.getPageSource().contains("帳號受到限制") || webDriver.getPageSource().contains("帐户受限")
                                || webDriver.getPageSource().contains("受到限制的帳戶") || webDriver.getPageSource().contains("アカウントが制限されています")
                                || webDriver.getPageSource().contains("Account restricted")) {
                            fbAccount.setStatus("2");
                        }
                        if (webDriver.getPageSource().contains("帳號停權")) {
                            fbAccount.setStatus("1");
                        }
                    }
                    fbAccountMapper.updateFbAccount(fbAccount);
                    ex.printStackTrace();
                    return false;
                }
                e.printStackTrace();
            }
            //判断是否存在继续操作按钮
            try {
                WebElement continueButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("checkpointSubmitButton")));
                if (continueButton != null) {
                    continueButton.click();
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
    public boolean isLogin(WebDriver webDriver) {

        Set<Cookie> cookies = webDriver.manage().getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("c_user")) {
                return true; // 找到了c_user项
            }
        }
        return false; // 未找到c_user项
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
            WebDriverWait wait = new WebDriverWait(webDriverMap.get(fbAccount.getId()), 8, 1);
            if (webDriverMap.get(fbAccount.getId()).getPageSource().contains("永久停用")) {
                closeBrowser(fbAccount);
                return;
            }
            for (String accountId : accountIds) {
                try {
                    webDriverMap.get(fbAccount.getId()).get("https://www.facebook.com/" + accountId);
                    webDriverMap.get(fbAccount.getId()).manage().window().maximize();
                    Thread.sleep(2000);
                    JavascriptExecutor js = (JavascriptExecutor) webDriverMap.get(fbAccount.getId());
                    js.executeScript("window.scrollTo(0, 0);");
                    String script = "var evt = new MouseEvent('click', {" +
                            "bubbles: true, cancelable: true, view: window, clientX: " + 1340 + ", clientY: " + 575 + "});" +
                            "document.elementFromPoint(" + 1340 + ", " + 575 + ").dispatchEvent(evt);";
                    js.executeScript(script);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!webDriverMap.get(fbAccount.getId()).getPageSource().contains("取消")) {

                    try {
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div[3]/div/div/div[1]/div[1]/div/div/div[1]/div[2]/div/div/div/div[4]/div/div/div[1]/div/div/div/div[1]/div[2]/span/span")))
                                .click();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div[4]/div/div/div[1]/div/div[2]/div/div/div/div[4]/div[1]/div/div/div[1]/div[2]/span/span")))
                            .click();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[7]/div[1]/div/div[2]/div/div/div/div/div/div/div[3]/div/div/div/div/div/div/div/div[1]/div/span/span")))
                            .click();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        if (!isLogin(webDriver)) {
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

        Map<String, String> bmInfo = new HashMap<>();
        WebDriver webDriver = webDriverMap.get(id);
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 20, 10);
        try {
            showBrowser(fbAccountMapper.selectOneByFbAccountId(id));
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.xeuugli.x2lwn1j.x78zum5.x1exxf4d.x1y71gwh.x1nb4dca.xu1343h.x13fuv20.xu3j5b3.x1q0q8m5.x26u7qi.x178xt8z.xm81vs4.xso031l.xy80clv.x10l6tqk")))
                    .click();
            Thread.sleep(2000);
            String bmPageSource = webDriver.getPageSource();
            bmInfo = ReadJsonData.getBmInfo(bmPageSource);
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.x1a2a7pz.xh8yej3")))
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
            List<String> adAccountAndBmInfo = ReadJsonData.getAdAccountBmInfo(id, adPageSource, bmInfo);
            for (String str : adAccountAndBmInfo) {
                String[] split = str.split("-");
                AccountAdAccountBmInfo accountAdAccountBmInfo = new AccountAdAccountBmInfo();
                accountAdAccountBmInfo.setAccountId(split[0]);
                accountAdAccountBmInfo.setAdAccountName(split[1]);
                accountAdAccountBmInfo.setAdAccountId(split[2]);
                accountAdAccountBmInfo.setBmName(split[3]);
                accountAdAccountBmInfo.setBmId(split[4]);
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

            System.out.println(adAccountAndBmInfo);

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
    public FbAccount checkAccountInfo(FbAccount fbAccount) {

        openBrowser(fbAccount);
        if (login(fbAccount)) {
            WebDriver webDriver = webDriverMap.get(fbAccount.getId());
            WebDriverWait wait = new WebDriverWait(webDriver, 50, 1);
            webDriver.get("https://www.facebook.com/business-support-home/" + fbAccount.getId());
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div/div/span/div/div[1]/div[2]/div/div[2]/div[1]/div[1]/div/div/div[1]/div[2]/div[1]/div/div/div/div[1]/div/div")));
            } catch (Exception e) {
                if (webDriver.getCurrentUrl().contains("/checkpoint")) {
                    if (webDriver.getPageSource().contains("立即") || webDriver.getPageSource().contains("开始") ||
                            webDriver.getPageSource().contains("has been locked")) {
                        fbAccount.setStatus("4");
                    } else {
                        fbAccount.setStatus("5");
                    }
                }
                e.printStackTrace();
            }
            if (webDriver.getCurrentUrl().contains("/www.facebook.com/business-support-home")){
                if (webDriver.getPageSource().contains("帳號受到限制") || webDriver.getPageSource().contains("帐户受限")
                        || webDriver.getPageSource().contains("受到限制的帳戶") || webDriver.getPageSource().contains("アカウントが制限されています")
                        || webDriver.getPageSource().contains("Account restricted")) {
                    fbAccount.setStatus("2");
                } else {
                    fbAccount.setStatus("1");
                }
            }
            if (webDriver.getPageSource().contains("帳號停權")) {
                fbAccount.setStatus("5");
            }
            fbAccount.setName(getAccountName(webDriver.getPageSource()));
            closeBrowser(fbAccount);
            return fbAccount;
        } else {
            closeBrowser(fbAccount);
            return null;
        }
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
                .sendKeys(fbAccount.getPassword()+".");
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
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        fbAccount.setPassword(fbAccount.getPassword()+".");
        return fbAccount;
    }

    /**
     * 解锁账号
     * @param fbAccount
     */
    @Override
    public void unlockAccount(FbAccount fbAccount) {
        WebDriver webDriver = webDriverMap.get(fbAccount.getId());
        WebDriverWait wait = new WebDriverWait(webDriver, 20,1);
        webDriver.findElement(By.xpath("/html/body/div[1]/div/div/div/div/div/div/div[2]/div/div/div[1]/div/div/div[1]/div/div/div/div/div/div/div/div/div/div/div/div/div[5]/div/div/div[1]/div/span/span"))
                .click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div/div/div/div/div/div[2]/div/div/div[1]/div/div/div[1]/div/div/div/div/div/div/div/div/div/div/div/div/div[4]/div/div[2]/div/div")))
                .click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div/div/div/div/div/div[2]/div/div/div[1]/div/div/div[1]/div/div/div/div/div/div/div/div/div/div/div/div/div[2]/div/div[1]/div/div[1]/div[2]/div[1]/div/div/div/span")))
                .click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div/div/div/div/div/div[2]/div/div/div[1]/div/div/div[1]/div/div/div/div/div/div/div/div/div/div/div/div/div[4]/div/div[2]/div/div/div[1]/div/span/span")))
                .click();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String message = EmailUtil.getMessage(fbAccount);
        String verificationCode = EmailUtil.getVerificationCode(message);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div/div/div/div/div/div[2]/div/div/div[1]/div/div/div[1]/div/div/div/div/div/div/div/div/div/div/div/div/div[2]/div/div/div/div/label/div/div/input")))
                .sendKeys(verificationCode);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div/div/div/div/div/div[2]/div/div/div[1]/div/div/div[1]/div/div/div/div/div/div/div/div/div/div/div/div/div[4]/div/div[2]/div/div/div[1]/div/span/span")))
                .click();
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




}
