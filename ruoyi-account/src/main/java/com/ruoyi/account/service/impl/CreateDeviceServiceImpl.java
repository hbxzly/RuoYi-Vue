package com.ruoyi.account.service.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.account.domain.CreateInfo;
import com.ruoyi.account.domain.FbAccountForSell;
import com.ruoyi.account.service.ICreateInfoService;
import com.ruoyi.account.service.ISeleniumService;
import com.ruoyi.account.util.FBAccountUtil;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.account.mapper.CreateDeviceMapper;
import com.ruoyi.account.domain.CreateDevice;
import com.ruoyi.account.service.ICreateDeviceService;

/**
 * 创建设备Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-07-15
 */
@Service
public class CreateDeviceServiceImpl implements ICreateDeviceService 
{
    @Autowired
    private CreateDeviceMapper createDeviceMapper;

    @Autowired
    private ISeleniumService seleniumService;

    @Autowired
    private ICreateInfoService createInfoService;


    /**
     * 查询创建设备
     * 
     * @param keyId 创建设备主键
     * @return 创建设备
     */
    @Override
    public CreateDevice selectCreateDeviceByKeyId(Long keyId)
    {
        return createDeviceMapper.selectCreateDeviceByKeyId(keyId);
    }

    /**
     * 查询创建设备
     *
     * @param keyIds 创建设备主键
     * @return 创建设备
     */
    @Override
    public List<CreateDevice> selectCreateDeviceByKeyIds(Long[] keyIds) {
        return createDeviceMapper.selectCreateDeviceByKeyIds(keyIds);
    }

    /**
     * 查询创建设备列表
     * 
     * @param createDevice 创建设备
     * @return 创建设备
     */
    @Override
    public List<CreateDevice> selectCreateDeviceList(CreateDevice createDevice)
    {
        return createDeviceMapper.selectCreateDeviceList(createDevice);
    }

    /**
     * 通过账号ID查找设备
     *
     * @param id
     * @return
     */
    @Override
    public CreateDevice selectCreateDeviceByCreateId(String id) {
        return createDeviceMapper.selectCreateDeviceByCreateId(id);
    }

    /**
     * 新增创建设备
     * 
     * @param createDevice 创建设备
     * @return 结果
     */
    @Override
    public int insertCreateDevice(CreateDevice createDevice)
    {
        return createDeviceMapper.insertCreateDevice(createDevice);
    }

    /**
     * 修改创建设备
     * 
     * @param createDevice 创建设备
     * @return 结果
     */
    @Override
    public int updateCreateDevice(CreateDevice createDevice)
    {
        return createDeviceMapper.updateCreateDevice(createDevice);
    }

    /**
     * 批量删除创建设备
     * 
     * @param keyIds 需要删除的创建设备主键
     * @return 结果
     */
    @Override
    public int deleteCreateDeviceByKeyIds(Long[] keyIds)
    {
        return createDeviceMapper.deleteCreateDeviceByKeyIds(keyIds);
    }

    /**
     * 删除创建设备信息
     * 
     * @param keyId 创建设备主键
     * @return 结果
     */
    @Override
    public int deleteCreateDeviceByKeyId(Long keyId)
    {
        return createDeviceMapper.deleteCreateDeviceByKeyId(keyId);
    }

    /**
     * 打开APP
     */
    @Override
    public AppiumDriver openApp(CreateDevice createDevice) {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        if (createDevice.getDeviceVersion() != null && !"".equals(createDevice.getDeviceVersion())) {
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, createDevice.getDeviceVersion());
        }
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, createDevice.getDeviceName());
        capabilities.setCapability(MobileCapabilityType.UDID, createDevice.getDeviceName());
        capabilities.setCapability(MobileCapabilityType.NO_RESET, true); // 注意是布尔型
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 300);
        capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, createDevice.getPackageName());
        capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, "com.facebook.katana.LoginActivity");
        capabilities.setCapability("appWaitActivity", "*");

        // 设置Appium服务器的地址
        URL appiumServerURL = null;
        try {
            appiumServerURL = new URL("http://localhost:4723/wd/hub");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        // 初始化AppiumDriver
        AppiumDriver<MobileElement> appiumDriver = new AndroidDriver<>(appiumServerURL, capabilities);

        return appiumDriver;
    }

    /**
     * 创建账号
     *
     * @param appiumDriver
     * @param createInfo
     * @return
     */
    @Override
    public String CreateAccounnt(AppiumDriver appiumDriver, CreateDevice createDevice, CreateInfo createInfo) {

        WebDriverWait webDriverWait30 = new WebDriverWait(appiumDriver, 30, 1);
        WebDriverWait webDriverWait10 = new WebDriverWait(appiumDriver, 10, 1);
        createDevice.setCreateAccountId(createInfo.getEmail());
        updateCreateDevice(createDevice);
        createInfo.setCreateStatus("已创建");
        createInfoService.updateCreateInfo(createInfo);
        seleniumService.threadSleep(2);
        try {
            webDriverWait10.until(ExpectedConditions.presenceOfElementLocated(By.id("com.android.packageinstaller:id/permission_deny_button"))).click();
        } catch (Exception e) {
            System.out.println("没弹出权限通知1");
        }
        //点击新建账号
        webDriverWait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup/android.view.ViewGroup[4]/android.view.View"))).click();
        seleniumService.threadSleep(2);

        //继续
        webDriverWait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.FrameLayout[@resource-id='"+createDevice.getPackageName()+":id/main_layout']/android.widget.FrameLayout/android.view.ViewGroup[3]/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.View"))).click();
        //姓氏
        webDriverWait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.FrameLayout[@resource-id='"+createDevice.getPackageName()+":id/main_layout']/android.widget.FrameLayout/android.view.ViewGroup[3]/android.widget.MultiAutoCompleteTextView[1]"))).sendKeys(createInfo.getNickName().substring(0, 1));
        //名字
        webDriverWait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.FrameLayout[@resource-id='"+createDevice.getPackageName()+":id/main_layout']/android.widget.FrameLayout/android.view.ViewGroup[3]/android.widget.MultiAutoCompleteTextView[2]"))).sendKeys(createInfo.getNickName().substring(1));
        //继续
        webDriverWait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.FrameLayout[@resource-id='"+createDevice.getPackageName()+":id/main_layout']/android.widget.FrameLayout/android.view.ViewGroup[3]/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.View"))).click();
        //使用电子邮件注册
        webDriverWait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.FrameLayout[@resource-id='"+createDevice.getPackageName()+":id/main_layout']/android.widget.FrameLayout/android.view.ViewGroup[3]/android.view.ViewGroup[2]/android.view.View"))).click();
        //输入邮箱
        webDriverWait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.MultiAutoCompleteTextView"))).sendKeys(createInfo.getEmail());
        //继续
        webDriverWait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.FrameLayout[@resource-id='"+createDevice.getPackageName()+":id/main_layout']/android.widget.FrameLayout/android.view.ViewGroup[3]/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.View"))).click();
        seleniumService.threadSleep(2);
        //输入生日
        try {
            inputDateByKeypad(appiumDriver,createDevice,createInfo.getBirthday());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //继续
        webDriverWait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.FrameLayout[@resource-id='"+createDevice.getPackageName()+":id/main_layout']/android.widget.FrameLayout/android.view.ViewGroup[3]/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup/android.view.View"))).click();
        if (createInfo.getGender().equals("男")){
            webDriverWait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.FrameLayout[@resource-id='"+createDevice.getPackageName()+":id/main_layout']/android.widget.FrameLayout/android.view.ViewGroup[4]/android.view.ViewGroup[2]/android.view.View[2]"))).click();
        }
        if (createInfo.getGender().equals("女")){
            webDriverWait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.FrameLayout[@resource-id='"+createDevice.getPackageName()+":id/main_layout']/android.widget.FrameLayout/android.view.ViewGroup[4]/android.view.ViewGroup[1]/android.view.View[2]"))).click();
        }
        //输入密码
        webDriverWait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.MultiAutoCompleteTextView"))).sendKeys(createInfo.getPassword());
        //继续
        webDriverWait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.FrameLayout[@resource-id='"+createDevice.getPackageName()+":id/main_layout']/android.widget.FrameLayout/android.view.ViewGroup[4]/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.View"))).click();
        //注册
        webDriverWait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.FrameLayout[@resource-id='"+createDevice.getPackageName()+":id/main_layout']/android.widget.FrameLayout/android.view.ViewGroup[4]/android.view.ViewGroup[7]/android.view.ViewGroup/android.view.View"))).click();
        try {
            //确定
            webDriverWait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.FrameLayout[@resource-id='"+createDevice.getPackageName()+":id/main_layout']/android.widget.FrameLayout/android.view.ViewGroup[4]/android.view.ViewGroup[2]/android.view.View"))).click();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            webDriverWait10.until(ExpectedConditions.presenceOfElementLocated(By.id("com.android.packageinstaller:id/permission_deny_button"))).click();
        } catch (Exception e) {
            System.out.println("没弹出权限通知1");
        }
        try {
            //如果有报错，确定
            webDriverWait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.FrameLayout[@resource-id='"+createDevice.getPackageName()+":id/main_layout']/android.widget.FrameLayout/android.view.ViewGroup[5]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.View[3]"))).click();
        } catch (Exception e) {
            e.printStackTrace();
        }



        return "";
    }

    /**
     * 登录账号
     *
     * @param appiumDriver
     * @param fbAccountForSell
     */
    @Override
    public void loginAccount(AppiumDriver appiumDriver, FbAccountForSell fbAccountForSell) {
        if (appiumDriver == null) {
            throw new RuntimeException("AppiumDriver is empty, cannot login account");
        }
        if (fbAccountForSell == null) {
            throw new RuntimeException("Account is empty, cannot login account");
        }
        if (isBlank(getLoginAccount(fbAccountForSell))) {
            throw new RuntimeException("Account id and email are both empty, cannot login account");
        }
        if (isBlank(fbAccountForSell.getPassword())) {
            throw new RuntimeException("Account password is empty, cannot login account");
        }

        WebDriverWait wait30 = new WebDriverWait(appiumDriver, 30, 500);
        dismissSystemDialogs(appiumDriver);
        openManualLoginFormIfNeeded(appiumDriver, wait30);

        WebElement accountInput = waitForAnyElement(wait30, appiumDriver,
                By.xpath("//android.widget.EditText[contains(@content-desc, '手机') or contains(@content-desc, '手機') or contains(@content-desc, '电子邮件') or contains(@content-desc, '電子郵件') or contains(@content-desc, 'Email') or contains(@content-desc, 'email') or contains(@content-desc, 'phone')]"),
                By.xpath("//android.widget.EditText[@password='false']"));
        inputText(accountInput, getLoginAccount(fbAccountForSell));

        WebElement passwordInput = waitForAnyElement(wait30, appiumDriver,
                By.xpath("//android.widget.EditText[@password='true']"),
                By.xpath("//android.widget.EditText[contains(@content-desc, '密码') or contains(@content-desc, '密碼') or contains(@content-desc, 'Password')]") );
        inputText(passwordInput, fbAccountForSell.getPassword());

        if (!clickFirstPresent(appiumDriver,
                By.xpath("//*[@content-desc='登入' or @content-desc='登录' or @content-desc='Log in']"),
                By.xpath("//*[@text='登入' or @text='登录' or @text='Log in']"))) {
            tapByRatio(appiumDriver, 0.5, 0.62);
        }
        seleniumService.threadSleep(6);

        dismissSystemDialogs(appiumDriver);
        failIfKnownLoginError(appiumDriver);
        submitTwoFactorIfShown(appiumDriver, fbAccountForSell, wait30);
        handlePostLoginPrompts(appiumDriver);
    }

    /**
     * 如果当前页面还没有登录输入框，则尝试进入手动账号登录表单。
     *
     * @param appiumDriver Appium驱动
     * @param wait 显式等待对象
     */
    private void openManualLoginFormIfNeeded(AppiumDriver appiumDriver, WebDriverWait wait) {
        seleniumService.threadSleep(1.5);
        if (hasAnyElement(appiumDriver, By.xpath("//android.widget.EditText"))) {
            return;
        }

        for (int i = 0; i < 4; i++) {
            dismissSystemDialogs(appiumDriver);
            if (hasAnyElement(appiumDriver, By.xpath("//android.widget.EditText"))) {
                return;
            }

            if (clickTextAndWaitForInput(appiumDriver,
                    "使用其他個人檔案", "使用其他个人档案", "使用其他个人主页",
                    "使用其他帳號", "使用其他账号", "使用其他帐户", "换个账号登录",
                    "Use another profile", "Use another account", "Log into another account")) {
                return;
            }

            if (clickTextAndWaitForInput(appiumDriver,
                    "我已有帳號", "我已有账号", "我已有帳戶", "我已有帐户",
                    "我已有個人檔案", "我已有个人主页",
                    "I already have an account", "I already have a Facebook account")) {
                return;
            }

            if (clickTextAndWaitForInput(appiumDriver,
                    "登入", "登录", "登錄", "Log in", "Login",
                    "開始使用", "开始使用", "Get started", "Continue")) {
                return;
            }

            tapLoginEntryFallback(appiumDriver, i);
            seleniumService.threadSleep(1.5);
        }

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.EditText")));
        } catch (Exception e) {
            throw new RuntimeException("Cannot open Facebook manual login form. Current page source: " + abbreviate(getPageSource(appiumDriver), 1200), e);
        }
    }

    /**
     * 检测并提交双重验证码。
     *
     * @param appiumDriver Appium驱动
     * @param account 当前登录账号
     * @param wait 显式等待对象
     */
    private void submitTwoFactorIfShown(AppiumDriver appiumDriver, FbAccountForSell account, WebDriverWait wait) {
        if (!pageContainsAny(appiumDriver, "雙重驗證", "双重验证", "驗證碼", "验证码", "authentication code", "two-factor", "two factor")) {
            return;
        }
        if (isBlank(account.getSecretKey())) {
            throw new RuntimeException("Account needs two-factor verification, but secretKey is empty");
        }

        WebElement codeInput = waitForAnyElement(wait, appiumDriver,
                By.xpath("//android.widget.EditText[contains(@content-desc, '驗證碼') or contains(@content-desc, '验证码') or contains(@content-desc, 'Code') or contains(@content-desc, 'code')]"),
                By.xpath("//android.widget.EditText"));
        inputText(codeInput, FBAccountUtil.getGoogleVerificationCode(account.getSecretKey().replaceAll("\\s+", "")));

        if (!clickFirstPresent(appiumDriver,
                By.xpath("//*[@content-desc='繼續' or @content-desc='继续' or @content-desc='Continue' or @content-desc='提交']"),
                By.xpath("//*[@text='繼續' or @text='继续' or @text='Continue' or @text='提交']"))) {
            tapByRatio(appiumDriver, 0.5, 0.7);
        }
        seleniumService.threadSleep(6);
        failIfKnownLoginError(appiumDriver);
    }

    /**
     * 处理登录成功后出现的常见确认、稍后再说、关闭等弹窗。
     *
     * @param appiumDriver Appium驱动
     */
    private void handlePostLoginPrompts(AppiumDriver appiumDriver) {
        for (int i = 0; i < 8; i++) {
            if (acceptRequiredFacebookTerms(appiumDriver)) {
                seleniumService.threadSleep(2);
                continue;
            }
            if (clickIfPageContains(appiumDriver, "大功告成", "關閉", "关闭", "Done", "Close")) {
                seleniumService.threadSleep(3);
                continue;
            }
            if (clickIfPageContains(appiumDriver, "稍後", "以后再说", "Not now", "暫不", "取消")) {
                seleniumService.threadSleep(1.5);
                continue;
            }
            if (clickIfPageContains(appiumDriver, "繼續", "继续", "Continue", "確定", "确定", "OK")) {
                seleniumService.threadSleep(1.5);
                continue;
            }
            break;
        }
    }

    /**
     * 如果页面要求同意Facebook条款，则开启必要开关并点击同意。
     *
     * @param appiumDriver Appium驱动
     * @return 是否处理了条款同意页面
     */
    private boolean acceptRequiredFacebookTerms(AppiumDriver appiumDriver) {
        if (!pageContainsAny(appiumDriver, "我同意", "同意以下", "agree to the following")) {
            return false;
        }
        for (int i = 0; i < 6; i++) {
            boolean changed = clickFirstPresent(appiumDriver,
                    By.xpath("//android.widget.Switch[@checked='false']"),
                    By.xpath("//*[@text='關閉' or @text='关闭' or @text='Off']"));
            if (changed) {
                seleniumService.threadSleep(0.6);
                continue;
            }
            if (clickFirstPresent(appiumDriver, By.xpath("//*[@content-desc='我同意' and @enabled='true']"), By.xpath("//*[@text='我同意' and @enabled='true']"))) {
                return true;
            }
            swipeByRatio(appiumDriver, 0.5, 0.78, 0.5, 0.32);
            seleniumService.threadSleep(1);
        }
        return clickFirstPresent(appiumDriver, By.xpath("//*[@content-desc='我同意' and @enabled='true']"), By.xpath("//*[@text='我同意' and @enabled='true']"));
    }

    /**
     * 关闭Android系统权限弹窗，避免遮挡Facebook登录页面。
     *
     * @param appiumDriver Appium驱动
     */
    private void dismissSystemDialogs(AppiumDriver appiumDriver) {
        clickFirstPresent(appiumDriver,
                By.id("com.android.packageinstaller:id/permission_deny_button"),
                By.id("com.android.permissioncontroller:id/permission_deny_button"),
                By.id("android:id/button2"));
    }

    /**
     * 根据当前页面文案识别已知登录失败场景，并抛出明确异常。
     *
     * @param appiumDriver Appium驱动
     */
    private void failIfKnownLoginError(AppiumDriver appiumDriver) {
        String pageSource = getPageSource(appiumDriver);
        if (pageSource.contains("密码错误") || pageSource.contains("密碼錯誤") || pageSource.contains("incorrect password")
                || pageSource.contains("你输入的密码不正确") || pageSource.contains("你輸入的密碼不正確")) {
            throw new RuntimeException("Mobile login failed: incorrect password");
        }
        if (pageSource.contains("找不到帐号") || pageSource.contains("找不到帳號") || pageSource.contains("cannot find your account")) {
            throw new RuntimeException("Mobile login failed: account not found");
        }
        if (pageSource.contains("帐号已停用") || pageSource.contains("帳號已停用") || pageSource.contains("account has been disabled")) {
            throw new RuntimeException("Mobile login failed: account disabled");
        }
        if (pageSource.contains("输入你看到的验证码") || pageSource.contains("輸入你看到的驗證碼") || pageSource.contains("CAPTCHA")) {
            throw new RuntimeException("Mobile login failed: captcha needs manual handling");
        }
    }

    /**
     * 按顺序等待多个定位器中的任意一个可见且可用的元素。
     *
     * @param wait 显式等待对象
     * @param appiumDriver Appium驱动
     * @param locators 候选定位器
     * @return 第一个可见且可用的元素
     */
    private WebElement waitForAnyElement(WebDriverWait wait, AppiumDriver appiumDriver, By... locators) {
        return (WebElement) wait.until(driver -> {
            for (By locator : locators) {
                List<WebElement> elements = appiumDriver.findElements(locator);
                for (WebElement element : elements) {
                    if (element.isDisplayed() && element.isEnabled()) {
                        return element;
                    }
                }
            }
            return null;
        });
    }

    /**
     * 查找并点击多个定位器中第一个可见且可用的元素。
     *
     * @param appiumDriver Appium驱动
     * @param locators 候选定位器
     * @return 是否成功点击元素
     */
    private boolean clickFirstPresent(AppiumDriver appiumDriver, By... locators) {
        for (By locator : locators) {
            List<WebElement> elements = appiumDriver.findElements(locator);
            for (WebElement element : elements) {
                try {
                    if (element.isDisplayed() && element.isEnabled()) {
                        if (clickElement(appiumDriver, element)) {
                            return true;
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        }
        return false;
    }

    /**
     * 优先使用元素 click，失败或无效时用元素中心点点击兜底。
     *
     * @param appiumDriver Appium驱动
     * @param element 目标元素
     * @return 是否执行了点击动作
     */
    private boolean clickElement(AppiumDriver appiumDriver, WebElement element) {
        try {
            element.click();
            return true;
        } catch (Exception ignored) {
        }
        return tapElementCenter(appiumDriver, element);
    }

    /**
     * 点击元素中心点。
     *
     * @param appiumDriver Appium驱动
     * @param element 目标元素
     * @return 是否执行了点击动作
     */
    private boolean tapElementCenter(AppiumDriver appiumDriver, WebElement element) {
        try {
            Point location = element.getLocation();
            Dimension size = element.getSize();
            int x = location.getX() + size.getWidth() / 2;
            int y = location.getY() + size.getHeight() / 2;
            new TouchAction(appiumDriver).tap(PointOption.point(x, y)).perform();
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * 点击候选文案并等待登录输入框出现，未出现时会重试。
     *
     * @param appiumDriver Appium驱动
     * @param texts 候选页面文案
     * @return 是否进入了输入框页面
     */
    private boolean clickTextAndWaitForInput(AppiumDriver appiumDriver, String... texts) {
        for (int i = 0; i < 3; i++) {
            if (hasAnyElement(appiumDriver, By.xpath("//android.widget.EditText"))) {
                return true;
            }
            if (clickIfPageContains(appiumDriver, texts)) {
                seleniumService.threadSleep(1.5);
                if (hasAnyElement(appiumDriver, By.xpath("//android.widget.EditText"))) {
                    return true;
                }
            } else {
                seleniumService.threadSleep(0.8);
            }
        }
        return hasAnyElement(appiumDriver, By.xpath("//android.widget.EditText"));
    }

    /**
     * Facebook不同版本的登录入口可能没有稳定文案，按常见按钮区域做兜底点击。
     *
     * @param appiumDriver Appium驱动
     * @param attempt 当前尝试次数
     */
    private void tapLoginEntryFallback(AppiumDriver appiumDriver, int attempt) {
        switch (attempt) {
            case 0:
                tapByRatio(appiumDriver, 0.5, 0.82);
                break;
            case 1:
                tapByRatio(appiumDriver, 0.5, 0.72);
                break;
            case 2:
                tapByRatio(appiumDriver, 0.5, 0.62);
                break;
            default:
                tapByRatio(appiumDriver, 0.5, 0.90);
                break;
        }
    }

    /**
     * 点击包含指定文案的元素，先点可点击父级，再点文本元素自身。
     *
     * @param appiumDriver Appium驱动
     * @param text 候选页面文案
     * @return 是否执行了点击动作
     */
    private boolean clickTextElement(AppiumDriver appiumDriver, String text) {
        String literal = xpathLiteral(text);
        return tapFirstPresent(appiumDriver,
                By.xpath("//*[contains(@content-desc," + literal + ") or contains(@text," + literal + ")]/ancestor::*[@clickable='true'][1]"),
                By.xpath("//*[contains(@content-desc," + literal + ") and @enabled='true']"),
                By.xpath("//*[contains(@text," + literal + ") and @enabled='true']"),
                By.xpath("//*[contains(@content-desc," + literal + ")]"),
                By.xpath("//*[contains(@text," + literal + ")]"));
    }

    /**
     * 查找并点击多个定位器中第一个可见且可用的元素，优先用中心点触摸。
     *
     * @param appiumDriver Appium驱动
     * @param locators 候选定位器
     * @return 是否成功点击元素
     */
    private boolean tapFirstPresent(AppiumDriver appiumDriver, By... locators) {
        for (By locator : locators) {
            List<WebElement> elements = appiumDriver.findElements(locator);
            for (WebElement element : elements) {
                try {
                    if (element.isDisplayed() && element.isEnabled()) {
                        if (tapElementCenter(appiumDriver, element)) {
                            return true;
                        }
                        if (clickElement(appiumDriver, element)) {
                            return true;
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        }
        return false;
    }

    /**
     * 将字符串转成 XPath 字面量，避免文案里出现单双引号导致 XPath 解析失败。
     *
     * @param value 原始字符串
     * @return XPath 字面量
     */
    private String xpathLiteral(String value) {
        if (value.indexOf("'") < 0) {
            return "'" + value + "'";
        }
        if (value.indexOf("\"") < 0) {
            return "\"" + value + "\"";
        }
        String[] parts = value.split("'", -1);
        StringBuilder builder = new StringBuilder("concat(");
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                builder.append(", \"'\", ");
            }
            builder.append("'").append(parts[i]).append("'");
        }
        builder.append(")");
        return builder.toString();
    }

    /**
     * 当前页面包含指定文案时，点击同名的文本或无障碍描述元素。
     *
     * @param appiumDriver Appium驱动
     * @param texts 候选页面文案
     * @return 是否成功点击匹配元素
     */
    private boolean clickIfPageContains(AppiumDriver appiumDriver, String... texts) {
        if (!pageContainsAny(appiumDriver, texts)) {
            return false;
        }
        for (String text : texts) {
            if (clickTextElement(appiumDriver, text)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断页面中是否存在指定定位器匹配的元素。
     *
     * @param appiumDriver Appium驱动
     * @param locator 元素定位器
     * @return 是否存在匹配元素
     */
    private boolean hasAnyElement(AppiumDriver appiumDriver, By locator) {
        try {
            return !appiumDriver.findElements(locator).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 清空输入框并输入文本。
     *
     * @param element 输入框元素
     * @param text 输入文本
     */
    private void inputText(WebElement element, String text) {
        element.click();
        try {
            element.clear();
        } catch (Exception ignored) {
        }
        element.sendKeys(text);
    }

    /**
     * 按屏幕宽高比例点击指定位置。
     *
     * @param appiumDriver Appium驱动
     * @param xRatio 横向比例
     * @param yRatio 纵向比例
     */
    private void tapByRatio(AppiumDriver appiumDriver, double xRatio, double yRatio) {
        int width = appiumDriver.manage().window().getSize().getWidth();
        int height = appiumDriver.manage().window().getSize().getHeight();
        new TouchAction(appiumDriver).tap(PointOption.point((int) (width * xRatio), (int) (height * yRatio))).perform();
    }

    /**
     * 按屏幕宽高比例执行滑动操作。
     *
     * @param appiumDriver Appium驱动
     * @param startXRatio 起点横向比例
     * @param startYRatio 起点纵向比例
     * @param endXRatio 终点横向比例
     * @param endYRatio 终点纵向比例
     */
    private void swipeByRatio(AppiumDriver appiumDriver, double startXRatio, double startYRatio, double endXRatio, double endYRatio) {
        int width = appiumDriver.manage().window().getSize().getWidth();
        int height = appiumDriver.manage().window().getSize().getHeight();
        new TouchAction(appiumDriver)
                .press(PointOption.point((int) (width * startXRatio), (int) (height * startYRatio)))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(600)))
                .moveTo(PointOption.point((int) (width * endXRatio), (int) (height * endYRatio)))
                .release()
                .perform();
    }

    /**
     * 获取手机端登录使用的账号，优先使用Facebook ID，其次使用邮箱。
     *
     * @param account 当前登录账号
     * @return 可用于登录的账号标识
     */
    private String getLoginAccount(FbAccountForSell account) {
        if (!isBlank(account.getId())) {
            return account.getId().trim();
        }
        if (!isBlank(account.getEmail())) {
            return account.getEmail().trim();
        }
        return "";
    }

    /**
     * 判断当前页面源码是否包含任意指定文案。
     *
     * @param appiumDriver Appium驱动
     * @param texts 候选文案
     * @return 是否包含任意文案
     */
    private boolean pageContainsAny(AppiumDriver appiumDriver, String... texts) {
        String pageSource = getPageSource(appiumDriver);
        for (String text : texts) {
            if (pageSource.contains(text)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 安全获取当前页面源码，获取失败时返回空字符串。
     *
     * @param appiumDriver Appium驱动
     * @return 页面源码
     */
    private String getPageSource(AppiumDriver appiumDriver) {
        try {
            return appiumDriver.getPageSource();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 截断长字符串，避免异常日志过长。
     *
     * @param value 原始字符串
     * @param maxLength 最大长度
     * @return 截断后的字符串
     */
    private String abbreviate(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength) + "...";
    }

    /**
     * 判断字符串是否为空或只包含空白字符。
     *
     * @param value 待判断字符串
     * @return 是否为空
     */
    private boolean isBlank(String value) {
        return value == null || "".equals(value.trim());
    }

    /**
     * 搜索没绑定账号的设备
     *
     * @return
     */
    @Override
    public CreateDevice selectMinNoAccountDevice() {
        return createDeviceMapper.selectMinNoAccountDevice();
    }

    /**
     * 使用Facebook生日输入页面的数字键盘输入生日。
     *
     * @param appiumDriver Appium驱动
     * @param createDevice 当前创建设备
     * @param backendDate 后端生日日期，格式为yyyy-MM-dd
     * @throws InterruptedException 输入间隔等待被中断时抛出
     */
    public void inputDateByKeypad(AppiumDriver appiumDriver, CreateDevice createDevice,String backendDate) throws InterruptedException {
        WebDriverWait webDriverWait30 = new WebDriverWait(appiumDriver, 30, 1);

        String[] parts = backendDate.split("-");
        String clickDate = parts[1] + parts[2] + parts[0];

        Map<Character, Integer> map = new HashMap<>();
        map.put('1', 1);
        map.put('2', 2);
        map.put('3', 3);
        map.put('4', 4);
        map.put('5', 5);
        map.put('6', 6);
        map.put('7', 7);
        map.put('8', 8);
        map.put('9', 9);
        map.put('0', 11);

        String pkg = createDevice.getPackageName();

        for (char c : clickDate.toCharArray()) {
            String xpath =
                    "//android.widget.FrameLayout[@resource-id='" + pkg + ":id/main_layout']" +
                            "/android.widget.FrameLayout/android.view.ViewGroup[4]" +
                            "/android.view.ViewGroup[" + map.get(c) + "]/android.view.View";

            webDriverWait30.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))).click();
            Thread.sleep(150);
        }
    }


}
