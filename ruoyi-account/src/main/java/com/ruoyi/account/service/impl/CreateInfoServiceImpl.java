package com.ruoyi.account.service.impl;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ruoyi.account.domain.Email;
import com.ruoyi.account.domain.FbAccount;
import com.ruoyi.account.mapper.EmailMapper;
import com.ruoyi.account.service.IEmailService;
import com.ruoyi.account.service.ISeleniumService;
import com.ruoyi.account.util.*;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.impl.SysUserServiceImpl;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.ruoyi.account.mapper.CreateInfoMapper;
import com.ruoyi.account.domain.CreateInfo;
import com.ruoyi.account.service.ICreateInfoService;


/**
 * 创建信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-01-08
 */
@Service
public class CreateInfoServiceImpl implements ICreateInfoService {

    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private CreateInfoMapper createInfoMapper;

    @Autowired
    private ISeleniumService seleniumService;

    @Autowired
    private EmailMapper emailMapper;

    @Autowired
    private IEmailService emailService;

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
     * 查询创建信息
     * @param keyId 创建信息主键
     * @return 创建信息
     */
    @Override
    public CreateInfo selectCreateInfoByKeyId(Long keyId) {
        return createInfoMapper.selectCreateInfoByKeyId(keyId);
    }

    /**
     * 查询创建信息列表
     * 
     * @param createInfo 创建信息
     * @return 创建信息
     */
    @Override
    public List<CreateInfo> selectCreateInfoList(CreateInfo createInfo) {
        return createInfoMapper.selectCreateInfoList(createInfo);
    }

    /**
     * 新增创建信息
     * 
     * @param createInfo 创建信息
     * @return 结果
     */
    @Override
    public int insertCreateInfo(CreateInfo createInfo) {
        return createInfoMapper.insertCreateInfo(createInfo);
    }

    /**
     * 修改创建信息
     * 
     * @param createInfo 创建信息
     * @return 结果
     */
    @Override
    public int updateCreateInfo(CreateInfo createInfo) {
        return createInfoMapper.updateCreateInfo(createInfo);
    }

    /**
     * 批量删除创建信息
     * 
     * @param keyIds 需要删除的创建信息主键
     * @return 结果
     */
    @Override
    public int deleteCreateInfoByKeyIds(Long[] keyIds) {
        return createInfoMapper.deleteCreateInfoByKeyIds(keyIds);
    }

    /**
     * 删除创建信息信息
     * 
     * @param keyId 创建信息主键
     * @return 结果
     */
    @Override
    public int deleteCreateInfoByKeyId(Long keyId) {
        return createInfoMapper.deleteCreateInfoByKeyId(keyId);
    }

    /**
     * 导入数据
     *
     * @param createInfoList  数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importCreateInfo(List<CreateInfo> createInfoList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(createInfoList) || createInfoList.size() == 0) {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (CreateInfo createInfo : createInfoList) {
            createInfo.setBrowserProfile(RandomUitl.generateRandomFilename());
            createInfo.setUa(RandomUitl.generateRandomPCUserAgent());
            createInfo.setFilePath("E:\\browser\\");
            try {
                // 验证是否存在这个账号
                CreateInfo create = createInfoMapper.selectCreateInfoByPassword(createInfo.getPassword());
                if (StringUtils.isNull(create))
                {
                    createInfoMapper.insertCreateInfo(createInfo);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、信息 " + createInfo.getNickName() + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    createInfoMapper.updateCreateInfo(createInfo);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、信息 " + createInfo.getNickName()  + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、信息 " + createInfo.getNickName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、信息 " + createInfo.getNickName()  + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    /**
     * 通过OE创建账号
     * @param webDriver
     * @param createInfo
     * @throws InterruptedException
     */
    @Override
    public void createAccountByOE(WebDriver webDriver, CreateInfo createInfo) throws InterruptedException {
        String projectId = "879390";
        System.out.println(createInfo);
        String mobile = DefuUtil.getMobile(projectId,"192");
        createInfo.setPhone(mobile);
        createInfoMapper.updateCreateInfo(createInfo);

        try {
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
                WebDriverWait driverWait1 = new WebDriverWait(webDriver, 10, 1);
                //输入代码输入框
                WebElement codeInput = null;


                try {
                    //检查号码是否可用
                    codeInput = driverWait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/div[1]/div[1]/div/div/div/div/div[2]/div[1]/div[2]/div/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input")));
                } catch (Exception e) {
                    createAccountByOE(webDriver, createInfo);
                    return;
                }

                String code = "";

                for (int i = 0; i < 5; i++) {
                    code = DefuUtil.getCode(mobile, projectId);
                    if (i == 4) {
                        createAccountByOE(webDriver, createInfo);
                        return;
                    } else if (code.equals("wait")) {
                        Thread.sleep(10000);
                    } else {
                        break;
                    }
                }

                codeInput.sendKeys(code);
                //输入代码后继续按钮
                WebElement continueButton = webDriver.findElement(By.xpath("/html/body/div[3]/div[1]/div[1]/div/div/div/div/div[3]/div/div[2]/div/span/div/div/div"));

                continueButton.click();
            }

            try {

                driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div/div/div/div[1]/div[1]/div[2]/div[2]/div/div[1]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input")))
                        .sendKeys(createInfo.getNickName().substring(0, 1));
                driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div/div/div/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input")))
                        .sendKeys(createInfo.getNickName().substring(1));
                driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div/div/div/div[1]/div[1]/div[2]/div[3]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input")))
                        .sendKeys(createInfo.getPassword());
                if (createInfo.getGender().equals("男")) {
                    driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div/div/div/div[1]/div[1]/div[2]/div[4]/div/div/div/div[2]/div/div/div/label[2]/div/div/div[1]/div/div/div[1]/input")))
                            .click();
                }
                driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div/div/div/div[1]/div[1]/div[2]/div[5]/div/div/div/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input")))
                        .sendKeys(createInfo.getBirthday());
                driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div/div/div/div[2]/div/div/div/span/div/div/div")))
                        .click();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Actions actions = new Actions(webDriver);
            //选择行业
            WebElement selectIndustry = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[1]/div[1]/div/div[2]/div/div/div/div/div/div[1]/div[2]/div[1]/div/div/div")));
            selectIndustry.click();
            if (!webDriver.getPageSource().contains("Replace")) {
                //选择电子商务行业
                WebElement industry = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[3]/div[1]/div[1]/div/div/div[1]/div[2]/div[2]/div[2]/div[7]/div/div/div[2]/div/div")));

                industry.click();

                WebElement license = null;

                try {
                    //营业执照上传框
                    license = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div/div[4]/div/div[2]/div/div/div/div/div[3]/div")));
                    license.click();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    // 创建Robot实例
                    Robot robot = new Robot();

                    // 等待一段时间以确保文件对话框已打开（可以根据实际情况调整等待时间）
                    Thread.sleep(5000);

                    // 输入文件路径
                    String filePath = "C:\\Users\\carrotKK\\Pictures\\企业微信截图_17165187941531.png"; // 替换成你要上传的文件的路径
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

                //信用代码
                WebElement creditCode = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[1]/div[5]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input")));
                actions.click(creditCode).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
                actions.sendKeys(Keys.DELETE).perform();
                creditCode.sendKeys("91210106MA10AP108C");

                //公司名称
                WebElement companyName = webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[1]/div[6]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input"));
                actions.click(companyName).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
                actions.sendKeys(Keys.DELETE).perform();
                companyName.sendKeys(createInfo.getCompanyName());

                //公司邮箱
                WebElement companyEmail = webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[1]/div[8]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input"));
                actions.click(companyEmail).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
                actions.sendKeys(Keys.DELETE).perform();
                companyEmail.sendKeys("goodgoodstudydaydayup@gmail.com");

                //公司地址
                WebElement companyAddress = webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[1]/div[9]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input"));
                actions.click(companyAddress).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
                actions.sendKeys(Keys.DELETE).perform();
                companyAddress.sendKeys("台湾台北");

                //邮编
                WebElement postCode = webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[1]/div[10]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input"));
                actions.click(postCode).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
                actions.sendKeys(Keys.DELETE).perform();
                postCode.sendKeys("123");

                //地址拼音
                WebElement companyAddressEn = webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[1]/div[11]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input"));
                actions.click(companyAddressEn).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
                actions.sendKeys(Keys.DELETE).perform();
                companyAddressEn.sendKeys("S");

                //推广网站
                WebElement companyWebsite = webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[1]/div[12]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input"));
                actions.click(companyWebsite).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
                actions.sendKeys(Keys.DELETE).perform();
                companyWebsite.sendKeys("qq.com");
            } else {

                //公司名称
                WebElement companyName = webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[1]/div[6]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input"));
                actions.click(companyName).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
                actions.sendKeys(Keys.DELETE).perform();
                companyName.sendKeys(createInfo.getCompanyName());
            }

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
            promotionWebsite.sendKeys("qq.com");

            //下一步
            nextStep = webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[3]/div/div/div[3]/div/div[2]/div"));
            nextStep.click();
            //提交
            WebElement submitButton = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[1]/div/div[3]/div/div[2]/div")));
            submitButton.click();


            for (int i = 0; i < 30; i++) {
                String pageSource = webDriver.getPageSource();
                if (pageSource.contains("请先修复以下错误再继续操作")) {
                    break;
                } else {
                    Thread.sleep(5000);
                }
                if (i == 29) {
                    System.out.println("未知错误");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            webDriver.get("https://business.facebook.com/settings");
            Thread.sleep(3000);

            if (webDriver.getCurrentUrl().contains("checkpoint")) {
                createInfo.setNote("被封");
                createInfoMapper.updateCreateInfo(createInfo);
                webDriver.quit();
                return;
            }
            if (webDriver.getCurrentUrl().contains("login")) {
                createInfo.setNote("没成功");
                createInfoMapper.updateCreateInfo(createInfo);
                webDriver.quit();
                return;
            }
            String cUser = webDriver.manage().getCookieNamed("c_user").getValue();
            createInfo.setId(cUser);
            // 获取当天日期
            LocalDate today = LocalDate.now();
            // 定义日期格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
            // 格式化日期
            String formattedDate = today.format(formatter);
            createInfo.setCreateDate(formattedDate);
            createInfoMapper.updateCreateInfo(createInfo);
            webDriver.manage().window().maximize();
            if (webDriver.getCurrentUrl().contains("overview")) {
                createInfo.setNote("没BM");
                createInfoMapper.updateCreateInfo(createInfo);
                webDriver.quit();
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread.sleep(1000);
        String url = webDriver.getCurrentUrl();

        // 定义正则表达式
        String regexBMID = "business_id=(\\d+)";
        Pattern patternBMID = Pattern.compile(regexBMID);
        Matcher matcherBMID = patternBMID.matcher(url);

        // 查找匹配
        if (matcherBMID.find()) {
            createInfo.setNote("OK");
            createInfoMapper.updateCreateInfo(createInfo);
        } else {
            System.out.println("未找到 business_id 参数");
        }
        webDriver.quit();

    }

    /**
     * 登录
     * @param webDriver
     * @param createInfo
     * @return
     */
    @Override
    public String login(WebDriver webDriver, CreateInfo createInfo) {
        webDriver.get("https://www.facebook.com");
        if (isLogin(createInfo, webDriver)){
            return "ok";
        }
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
                for (char c : createInfo.getId().toCharArray()) {
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
                for (char c : createInfo.getPassword().toCharArray()) {
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
            boolean b = seleniumService.waitingForContent(10, webDriver, "• Facebook");
            if (b) {
                return "ok";
            }else {
                return "error";
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 检查是否登录
     * @param createInfo
     * @param webDriver
     * @return
     */
    @Override
    public boolean isLogin(CreateInfo createInfo, WebDriver webDriver) {
        // 获取页面 URL 和 Cookies
        Set<Cookie> cookies = webDriver.manage().getCookies();

        // 检查是否处于检查点页面
        if (webDriver.getCurrentUrl().contains("/checkpoint/")) {
                createInfo.setCreateStatus("被封");
                createInfoMapper.updateCreateInfo(createInfo);
                return false;
        }
        // 检查 Cookies 是否包含特定项
        boolean hasCUser = cookies.stream().anyMatch(cookie -> "c_user".equals(cookie.getName()));
        boolean hasPresence = cookies.stream().anyMatch(cookie -> "presence".equals(cookie.getName()));

        if (hasCUser) {
            return hasPresence;
        }
        return false;
    }

    /**
     * 打开浏览器
     * @param createInfo
     * @return
     */
    @Override
    public WebDriver openBrowser(CreateInfo createInfo) {
        if (webDriverMap.containsKey(createInfo.getId())){
            seleniumService.showBrowser(processMap.get(createInfo.getId()));
        }
        List<Integer> beforeListWindows = seleniumService.getListWindows();
        WebDriver webDriver = seleniumService.openBrowser(BrowserConfig.getCreateInfoBrowserConfig(createInfo));
        List<Integer> afterListWindows = seleniumService.getListWindows();
        Integer extraProcessId = seleniumService.findExtraProcessId(beforeListWindows, afterListWindows);
        webDriverMap.put(createInfo.getId(), webDriver);
        processMap.put(createInfo.getId(), extraProcessId);
        return webDriver;
    }

    /**
     * 关闭浏览器
     * @param createInfo
     */
    @Override
    public void closeBrowser(CreateInfo createInfo) {
        webDriverMap.get(createInfo.getId()).quit();
        processMap.remove(createInfo.getId());
        webDriverMap.remove(createInfo.getId());
    }

    /**
     * 换头像
     * @param webDriver
     * @param createInfo
     * @return
     */
    @Override
    public String updateAccountAvatar(WebDriver webDriver, CreateInfo createInfo) {
        if (!isLogin(createInfo, webDriver)){
            createInfo.setCreateStatus("被锁");
            createInfoMapper.updateCreateInfo(createInfo);
            return "error";
        }

        Integer i = processMap.get(createInfo.getId());
        seleniumService.showBrowser(i);

        webDriver.get("https://www.facebook.com/" + createInfo.getId());
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 25, 1);

        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@aria-label='更换头像']"))).click();
        seleniumService.threadSleep(1000);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='上传照片']"))).click();
        // 等待一段时间以确保文件对话框已打开（可以根据实际情况调整等待时间）
        seleniumService.threadSleep(1000);
        String filePath = "";
        if (createInfo.getGender().equals("男")) {
            filePath = "E:\\头像\\台湾男号头像\\" + RandomUitl.getRandomFile("E:\\头像\\台湾男号头像\\"); // 替换成你要上传的文件的路径
        } else {
            filePath = "E:\\头像\\台湾女号头像\\" + RandomUitl.getRandomFile("E:\\头像\\台湾女号头像\\");
        }
        // 将文件路径复制到剪贴板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(filePath);
        clipboard.setContents(stringSelection, null);
        seleniumService.simulateKeyPress(KeyEvent.VK_CONTROL,KeyEvent.VK_V);
        seleniumService.threadSleep(500);
        seleniumService.simulateKeyPress(KeyEvent.VK_ENTER);
        seleniumService.threadSleep(3000);
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='保存']"))).click();
        } catch (Exception e) {
            createInfo.setNote("头像设置失败");
            createInfoMapper.updateCreateInfo(createInfo);
        }
        seleniumService.threadSleep(5000);
        return "";
    }

    /**
     * 添加邮箱
     * @param webDriver
     * @param createInfo
     * @return
     */
    @Override
    public String updateAccountAddEmail(WebDriver webDriver, CreateInfo createInfo) {

        if (!isLogin(createInfo, webDriver)){
            createInfo.setCreateStatus("被锁");
            createInfoMapper.updateCreateInfo(createInfo);
            return "error";
        }

        Integer i = processMap.get(createInfo.getId());
        seleniumService.showBrowser(i);
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 25, 1);
        List<Email> emails = emailMapper.selectEmailList(new Email());
        Email email = new Email();
        for (Email e : emails) {
            if (e.getAccountId().isEmpty()){
                email = e;
            }
        }
        emailService.getMessage(email);

        webDriver.get("https://accountscenter.facebook.com/personal_info/contact_points/?contact_point_type=email&dialog_type=add_contact_point");
        try {
            //输入邮箱
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='text']"))).sendKeys(email.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //选择账号
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='checkbox']"))).click();
        } catch (Exception e) {
            e.printStackTrace();
        }
        seleniumService.threadSleep(3000);
        try {
            //点击继续
            webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='继续']"))).click();
        } catch (Exception e) {
            e.printStackTrace();
        }
        seleniumService.threadSleep(10000);
        String pageSource = webDriver.getPageSource();
        if (pageSource.contains("WhatsApp")){
            createInfo.setEmail("提示WhatsApp");
            createInfoMapper.updateCreateInfo(createInfo);
            webDriver.get("https://www.facebook.com/" + createInfo.getId());
            seleniumService.threadSleep(3000);
        }
        if (!pageSource.contains("WhatsApp")){
            seleniumService.threadSleep(30000);
            String message = emailService.getMessage(email);
            String code = RegexUtil.addEmailToFacebookCode(message.replace(email.getEmail(),""));
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@inputmode='numeric']"))).sendKeys(code);
            List<WebElement> elements = webDriver.findElements(By.xpath("//span[text()='继续']"));
            elements.get(elements.size()-1).click();
            seleniumService.threadSleep(2000);
            try {
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='关闭']"))).click();
                seleniumService.threadSleep(2000);
                createInfo.setEmail(email.getEmail());
                createInfo.setEmailPassword(email.getPassword());
                email.setAccountId(createInfo.getId());
                createInfoMapper.updateCreateInfo(createInfo);
                emailMapper.updateEmail(email);
            } catch (Exception e) {
                createInfo.setNote("添加邮箱失败");
                email.setAccountId("100000000000");
                createInfoMapper.updateCreateInfo(createInfo);
                emailMapper.updateEmail(email);
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 开启双重验证
     * @param webDriver
     * @param createInfo
     * @return
     */
    @Override
    public String updateAccountOpenTwoFactor(WebDriver webDriver, CreateInfo createInfo) {

        if (!isLogin(createInfo, webDriver)){
            createInfo.setCreateStatus("被锁");
            createInfoMapper.updateCreateInfo(createInfo);
            return "error";
        }

        Integer i = processMap.get(createInfo.getId());
        seleniumService.showBrowser(i);
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 25, 1);

        //双重验证码
        webDriver.get("https://accountscenter.facebook.com/password_and_security/two_factor");
        seleniumService.threadSleep(2000);
        //选择账号
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), '"+createInfo.getNickName()+"')]"))).click();
        seleniumService.threadSleep(2000);
        //下一页
        String pageSource = webDriver.getPageSource();
        if (pageSource.contains("请输入我们发送到你 WhatsApp 帐户的验证码")){
            createInfo.setSecretKey("提示WhatsApp");
            createInfoMapper.updateCreateInfo(createInfo);
            return "0";
        }
        if (pageSource.contains("请重新输入密码")){
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='password']"))).sendKeys(createInfo.getPassword());
            List<WebElement> elements = webDriver.findElements(By.xpath("//span[text()='继续']"));
            elements.get(elements.size()-1).click();
        }
        try {
            seleniumService.threadSleep(2000);
            List<WebElement> elements = webDriver.findElements(By.xpath("//span[text()='继续']"));
            elements.get(elements.size()-1).click();
        } catch (Exception e) {
            e.printStackTrace();
        }

        seleniumService.threadSleep(10000);
        String twoFactorPageSource = webDriver.getPageSource();

        String fa = "";
        try {
            // 定义正则表达式
            String regex = "\\b([A-Z0-9]{4} ){7}[A-Z0-9]{4}\\b";
            // 编译正则表达式
            Pattern pattern = Pattern.compile(regex);
            // 匹配内容
            Matcher matcher = pattern.matcher(twoFactorPageSource);

            if (matcher.find()) {
                // 输出匹配到的第一个结果
                fa = matcher.group();
            } else {
                System.out.println("未找到符合格式的内容！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        seleniumService.threadSleep(2000);
        try {
            List<WebElement> elements = webDriver.findElements(By.xpath("//span[text()='继续']"));
            elements.get(elements.size()-1).click();
        } catch (Exception e) {
            e.printStackTrace();
        }
        seleniumService.threadSleep(2000);
        String verificationCode = FBAccountUtil.getGoogleVerificationCode(fa);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@maxlength='6']"))).sendKeys(verificationCode);
        try {
            Thread.sleep(2000);
            List<WebElement> elements = webDriver.findElements(By.xpath("//span[text()='继续']"));
            elements.get(elements.size()-1).click();
        } catch (Exception e) {
            e.printStackTrace();
        }
        seleniumService.threadSleep(5000);;
        pageSource = webDriver.getPageSource();
        if (pageSource.contains("前往你的身份验证应用")){
            seleniumService.threadSleep(30000);
            String erificationCode = FBAccountUtil.getGoogleVerificationCode(fa);
            List<WebElement> inputElements = webDriver.findElements(By.xpath("//input[@type='text']"));
            inputElements.get(inputElements.size()-1).sendKeys(erificationCode);
            List<WebElement> spanElements = webDriver.findElements(By.xpath("//span[text()='继续']"));
            spanElements.get(spanElements.size()-1).click();
        }

        try {
            webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='完成']"))).click();
        } catch (Exception e) {
            e.printStackTrace();
        }
        createInfo.setSecretKey(fa);
        createInfoMapper.updateCreateInfo(createInfo);
        return "";
    }


}
