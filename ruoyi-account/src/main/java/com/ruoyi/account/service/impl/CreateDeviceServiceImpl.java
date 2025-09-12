package com.ruoyi.account.service.impl;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.ruoyi.account.domain.CreateInfo;
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
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554");
        capabilities.setCapability(MobileCapabilityType.NO_RESET, true); // 注意是布尔型
        capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.facebook.litg");
        capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, "com.facebook.lite.MainActivity");

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
    public String CreateAccounnt(AppiumDriver appiumDriver, CreateInfo createInfo) {

        WebDriverWait webDriverWait = new WebDriverWait(appiumDriver, 30, 1);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup/android.view.ViewGroup[4]/android.view.View"))).click();
        //点击新建账号
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.FrameLayout[@resource-id=\"com.facebook.litg:id/main_layout\"]/android.widget.FrameLayout/android.view.ViewGroup[3]/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.View"))).click();

        //点击账号
        new TouchAction(appiumDriver)
                .tap(PointOption.point(200, 440))
                .perform();
        // 将文件路径复制到剪贴板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection("filePath");
        clipboard.setContents(stringSelection, null);
        seleniumService.simulateKeyPress(KeyEvent.VK_CONTROL,KeyEvent.VK_V);
        //点击账号
        new TouchAction(appiumDriver)
                .tap(PointOption.point(600, 440))
                .perform();

        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        stringSelection = new StringSelection("filePath");
        clipboard.setContents(stringSelection, null);
        seleniumService.simulateKeyPress(KeyEvent.VK_CONTROL,KeyEvent.VK_V);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.FrameLayout[@resource-id=\"com.facebook.litg:id/main_layout\"]/android.widget.FrameLayout/android.view.ViewGroup[4]/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.View"))).click();
        //继续
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.FrameLayout[@resource-id=\"com.facebook.litg:id/main_layout\"]/android.widget.FrameLayout/android.view.ViewGroup[3]/android.view.ViewGroup[2]/android.view.View"))).click();

        //继续
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.FrameLayout[@resource-id=\"com.facebook.litg:id/main_layout\"]/android.widget.FrameLayout/android.view.ViewGroup[3]/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.View"))).click();

        return "";
    }


}
