package com.ruoyi.account.service.impl;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.ruoyi.account.domain.CreateInfo;
import com.ruoyi.account.domain.FbAccountForSell;
import com.ruoyi.account.service.ICreateInfoService;
import com.ruoyi.account.service.ISeleniumService;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
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
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "9.0.1");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, createDevice.getDeviceName());
        capabilities.setCapability(MobileCapabilityType.NO_RESET, true); // 注意是布尔型
        capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, createDevice.getPackageName());
        capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, "com.facebook.katana.LoginActivity");

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
        WebDriverWait webDriverWait30 = new WebDriverWait(appiumDriver, 30, 1);
        WebDriverWait webDriverWait10 = new WebDriverWait(appiumDriver, 10, 1);

        webDriverWait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.view.View[@content-desc=\"忘記密碼？\"]")));
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

    public void inputDateByKeypad(AppiumDriver appiumDriver, CreateDevice createDevice,String backendDate) throws InterruptedException {
        WebDriverWait webDriverWait30 = new WebDriverWait(appiumDriver, 30, 1);

        String[] parts = backendDate.split("-");
        String clickDate = parts[1] + parts[2] + parts[0];

        Map<Character, Integer> map = Map.of(
                '1',1,'2',2,'3',3,'4',4,'5',5,'6',6,'7',7,'8',8,'9',9,'0',11
        );

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
