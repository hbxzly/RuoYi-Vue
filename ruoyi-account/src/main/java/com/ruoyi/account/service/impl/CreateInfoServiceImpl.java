package com.ruoyi.account.service.impl;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ruoyi.account.domain.*;
import com.ruoyi.account.mapper.EmailMapper;
import com.ruoyi.account.service.*;
import com.ruoyi.account.util.*;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.impl.SysUserServiceImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.ruoyi.account.mapper.CreateInfoMapper;


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

    @Autowired
    private IOperationLogService operationLogService;

    @Autowired
    private IPostsService postsService;

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
                if (StringUtils.isNull(create)) {
                    createInfoMapper.insertCreateInfo(createInfo);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、信息 " + createInfo.getNickName() + " 导入成功");
                }
                else if (isUpdateSupport) {
                    createInfoMapper.updateCreateInfo(createInfo);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、信息 " + createInfo.getNickName()  + " 更新成功");
                }
                else {
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
     * 通过OE创建账号
     * @param webDriver
     * @param createInfo
     * @throws InterruptedException
     */
    @Override
    public void createAccountByOE(WebDriver webDriver, CreateInfo createInfo) throws InterruptedException {
//        String projectId = "907996----U165DS";
        String projectId = "936068----SBE1P2";
//        String projectId = "907996----B22T60";
//        String projectId = "907996";
//        String mobile = DefuUtil.getMobile(projectId,"","192","");
        String mobile = DefuUtil.getMobile(projectId,"","0","192");
        createInfo.setPhone(mobile);
        createInfoMapper.updateCreateInfo(createInfo);

        try {
            webDriver.get("https://www.facebook.com/chinabusinesses/onboarding/684085081667854/?token=272483553796705");
            Thread.sleep(4000);
            WebDriverWait driverWait = new WebDriverWait(webDriver, 10, 1);
            WebElement mobileInput = null;
            try {
                mobileInput = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@placeholder='这个手机号将绑定你的 Meta 广告账户。']")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mobileInput != null) {
                mobileInput.sendKeys(mobile);
                //输入号码后创建广告账户按钮
                WebElement createAdButton = webDriver.findElement(By.xpath("//div[text()='创建广告账户']"));

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
                createInfo.setIsCreate("1");
                updateCreateInfo(createInfo);
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
            driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()='选择行业']"))).click();
            seleniumService.threadSleep(1);
            driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()='电子商务']"))).click();
            seleniumService.threadSleep(1);
            if (!webDriver.getPageSource().contains("Replace")) {
                WebElement license = null;

                try {
                    //营业执照上传框
                    license = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()='选择设备中的文件']")));
                    license.click();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                List<WebElement> elements = webDriver.findElements(By.xpath("//input[@type='text']"));
                System.out.println("输入框数量：" + elements.size());

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
                WebElement creditCode = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[2]/div/div[5]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input")));
                actions.click(creditCode).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
                actions.sendKeys(Keys.DELETE).perform();
                creditCode.sendKeys("91210106MA10AP108C");

                //公司名称
                WebElement companyName = webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[2]/div/div[6]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input"));
                actions.click(companyName).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
                actions.sendKeys(Keys.DELETE).perform();
                companyName.sendKeys(createInfo.getCompanyName());

                //公司邮箱
                WebElement companyEmail = webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[2]/div/div[8]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input"));
                actions.click(companyEmail).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
                actions.sendKeys(Keys.DELETE).perform();
                companyEmail.sendKeys("goodgoodstudydaydayup@gmail.com");

                //公司地址
                WebElement companyAddress = webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[2]/div/div[9]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input"));
                actions.click(companyAddress).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
                actions.sendKeys(Keys.DELETE).perform();
                companyAddress.sendKeys("台湾台北");

                //邮编
                WebElement postCode = webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[2]/div/div[10]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input"));
                actions.click(postCode).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
                actions.sendKeys(Keys.DELETE).perform();
                postCode.sendKeys("123");

                //地址拼音
                WebElement companyAddressEn = webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[2]/div/div[11]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input"));
                actions.click(companyAddressEn).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
                actions.sendKeys(Keys.DELETE).perform();
                companyAddressEn.sendKeys("S");

                //推广网站
                WebElement companyWebsite = webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[2]/div/div[12]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input"));
                actions.click(companyWebsite).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
                actions.sendKeys(Keys.DELETE).perform();
                companyWebsite.sendKeys("qq.com");
            } else {

                //公司名称
                WebElement companyName = webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[2]/div/div[6]/div/div/div[2]/div/div/div/div[1]/div[2]/div/div/input"));
                actions.click(companyName).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
                actions.sendKeys(Keys.DELETE).perform();
                companyName.sendKeys(createInfo.getCompanyName());
            }

            //下一步
            WebElement nextStep = webDriver.findElement(By.xpath("//div[text()='继续']"));
            nextStep.click();
            //账号名称
            WebElement adAccountName = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[1]/div/div[2]/div[1]/div[2]/div/div[2]/div/div[1]/div/div/div[2]/div/div[1]/div/div[1]/div[2]/div[1]/div/input")));
            actions.click(adAccountName).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
            actions.sendKeys(Keys.DELETE).perform();
            adAccountName.sendKeys("123");

            //推广网站
            WebElement promotionWebsite = webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div[2]/div/div[2]/div/div/div[2]/div[1]/div[2]/div/div[2]/div/div[1]/div/div[2]/div/div/div/div[1]/div[2]/div/div/input"));
            actions.click(promotionWebsite).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
            actions.sendKeys(Keys.DELETE).perform();
            promotionWebsite.sendKeys("qq.com");

            //下一步
            nextStep = webDriver.findElement(By.xpath("//div[text()='继续']"));
            nextStep.click();

            driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='checkbox']")));
            List<WebElement> elements = webDriver.findElements(By.xpath("//input[@type='checkbox']"));
            for (WebElement element : elements) {
                element.click();
                seleniumService.threadSleep(1);
            }

            //提交
            WebElement submitButton = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()='提交']")));
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

        try{
            webDriver.get("https://www.facebook.com");
            Thread.sleep(3000);
            if (webDriver.getCurrentUrl().contains("login")) {
                createInfo.setNote("没成功");
                createInfoMapper.updateCreateInfo(createInfo);
                webDriver.quit();
                return;
            }
            if (webDriver.getCurrentUrl().contains("checkpoint")) {
                createInfo.setCreateStatus("被封");
                createInfoMapper.updateCreateInfo(createInfo);
                webDriver.quit();
                return;
            }

            String cUser = webDriver.manage().getCookieNamed("c_user").getValue();
            createInfo.setId(cUser);
            createInfo.setCreateStatus("成功");
            // 获取当天日期
            LocalDate today = LocalDate.now();
            // 定义日期格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
            // 格式化日期
            String formattedDate = today.format(formatter);
            createInfo.setCreateDate(formattedDate);
            createInfoMapper.updateCreateInfo(createInfo);
        }catch (Exception e){
            e.printStackTrace();
        }



        try {
            webDriver.get("https://business.facebook.com/settings");
            Thread.sleep(3000);

            webDriver.manage().window().maximize();
            if (webDriver.getCurrentUrl().contains("business.facebook.com/business/loginpage")) {
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
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 10, 1);
        seleniumService.threadSleep(2);
        try {
            WebElement email = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("email")));
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
                if (createInfo.getId() ==null || "".equals(createInfo.getId())) {
                    for (char c : createInfo.getEmail().toCharArray()) {
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
                }else {
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
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='登录']"))).click();
            boolean b = seleniumService.waitingForContent(10, webDriver, "• Facebook");
            seleniumService.threadSleep(1);
            String pageSource = webDriver.getPageSource();
            Document document = Jsoup.parse(pageSource);

            //新版双重验证码输入
            if (document.select("#approvals_code").first() != null) {
                webDriver.findElement(By.id("approvals_code")).sendKeys(FBAccountUtil.getGoogleVerificationCode(createInfo.getSecretKey()));
                webDriver.findElement(By.id("checkpointSubmitButton")).click();
                Thread.sleep(2000);
                webDriver.findElement(By.id("checkpointSubmitButton")).click();
                Thread.sleep(2000);
                webDriver.get("https://www.facebook.com");
            }
            Element element = document.select("input[type=text]").first();
            if (element != null) {
                WebElement approvalsCode = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='text']")));
                approvalsCode.sendKeys(FBAccountUtil.getGoogleVerificationCode(createInfo.getSecretKey()));
                approvalsCode.sendKeys(Keys.ENTER);
                if (seleniumService.waitingForContent(2, webDriver, "currentColor")) {
                    createInfo.setNote("无法登录-秘钥错误");
                    updateCreateInfo(createInfo);
                    return "";
                }
                for (int i = 0; i < 10; i++) {
                    if (!webDriver.getCurrentUrl().contains("two_step_verification")){
                        break;
                    }
                    seleniumService.threadSleep(1);
                }
                webDriver.get("https://www.facebook.com");
            }
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
                updateCreateInfo(createInfo);
                return false;
        }

        if (webDriver.getPageSource().contains("="+createInfo.getId())) {
            return true;
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
        /*if (webDriverMap.containsKey(createInfo.getId())){
            seleniumService.showBrowser(processMap.get(createInfo.getId()));
        }*/
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


        Integer i = processMap.get(createInfo.getId());
        seleniumService.showBrowser(i);

        webDriver.get("https://www.facebook.com/profile.php?id=" + createInfo.getId()+"&sk=photos");
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 25, 1);

        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[normalize-space()='添加照片/视频' or normalize-space()='新增相片／影片']"))).click();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("没点到");
        }
        seleniumService.threadSleep(1);
        // 等待一段时间以确保文件对话框已打开（可以根据实际情况调整等待时间）
        seleniumService.threadSleep(1);
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
        seleniumService.threadSleep(1);
        seleniumService.simulateKeyPress(KeyEvent.VK_ENTER);
        seleniumService.threadSleep(3);
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated( By.xpath("//div[@role='button' and @aria-label='发帖' or @aria-label='發佈']"))).click();
            seleniumService.threadSleep(10);
            webDriver.navigate().refresh();
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@aria-label='这张照片的更多编辑选项' or @aria-label='適用此相片的更多選項']"))).click();
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='设为头像' or text()='設定為大頭貼照']"))).click();
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='保存' or text()='儲存']"))).click();

        } catch (Exception e) {
            e.printStackTrace();
            String oldNote = createInfo.getNote();

            if (oldNote == null || oldNote.isEmpty()) {
                createInfo.setNote("头像设置失败");
            } else {
                createInfo.setNote(oldNote + " | 头像设置失败");
            }

            updateCreateInfo(createInfo);
        }
        seleniumService.threadSleep(5);
        return "";
    }

    /**
     * 添加邮箱
     * @param webDriver
     * @param createInfo
     * @return
     */
    @Override
    public String updateAccountAddEmail(WebDriver webDriver, CreateInfo createInfo) throws JsonProcessingException {

        if (!isLogin(createInfo, webDriver)){
            createInfo.setCreateStatus("被锁");
            updateCreateInfo(createInfo);
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
        if (email == null){
            return "";
        }
        emailService.getMessage(email);
        createInfo.setEmail(email.getEmail());
        createInfo.setEmailPassword(email.getPassword());
        updateCreateInfo(createInfo);
        webDriver.get("https://accountscenter.facebook.com/personal_info/contact_points/?contact_point_type=email&dialog_type=add_contact_point");

        boolean b = seleniumService.waitingForContent(5, webDriver, "管理你的手机号和邮箱，确保联系方式是正确且最新的");
        if (b){
            try {
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()='添加新的联系方式']"))).click();
                seleniumService.threadSleep(1);
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()='添加邮箱']"))).click();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
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
        seleniumService.threadSleep(3);
        try {
            //点击继续
            webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='继续']"))).click();
        } catch (Exception e) {
            e.printStackTrace();
        }
        seleniumService.threadSleep(5);
        String pageSource = webDriver.getPageSource();
        if (pageSource.contains("WhatsApp")){
            createInfo.setEmail("提示WhatsApp");
            updateCreateInfo(createInfo);
            webDriver.get("https://www.facebook.com/" + createInfo.getId());
            seleniumService.threadSleep(3);
        }
        if (!pageSource.contains("WhatsApp")){
            seleniumService.threadSleep(30);
            String message = emailService.getMessage(email);
            String code = RegexUtil.addEmailToFacebookCode(message.replace(email.getEmail(),""));
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@inputmode='numeric']"))).sendKeys(code);
            List<WebElement> elements = webDriver.findElements(By.xpath("//span[text()='继续']"));
            elements.get(elements.size()-1).click();
            seleniumService.threadSleep(2);
            try {
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='关闭']"))).click();
                seleniumService.threadSleep(2);
                createInfo.setEmail(email.getEmail());
                createInfo.setEmailPassword(email.getPassword());
                email.setAccountId(createInfo.getId());
                createInfo.setIsBoundEmail("1");
                email.setIsBoundAccount("1");
                updateCreateInfo(createInfo);
                emailMapper.updateEmail(email);
            } catch (Exception e) {
                createInfo.setNote("添加邮箱失败");
                email.setAccountId(createInfo.getId());
                createInfo.setIsBoundEmail("1");
                email.setIsBoundAccount("1");
                email.setNote("绑定账号失败");
                updateCreateInfo(createInfo);
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
            updateCreateInfo(createInfo);
            return "error";
        }

        Integer i = processMap.get(createInfo.getId());
        seleniumService.showBrowser(i);
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 25, 1);

        //双重验证码
        webDriver.get("https://accountscenter.facebook.com/password_and_security/two_factor");
        seleniumService.threadSleep(2);
        //选择账号
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), '"+createInfo.getNickName()+"')]"))).click();
        seleniumService.threadSleep(2);
        //下一页
        String pageSource = webDriver.getPageSource();
        if (pageSource.contains("请输入我们发送到你 WhatsApp 帐户的验证码")){
            createInfo.setSecretKey("提示WhatsApp");
            updateCreateInfo(createInfo);
            return "0";
        }
        if (pageSource.contains("请重新输入密码")){
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='password']"))).sendKeys(createInfo.getPassword());
            List<WebElement> elements = webDriver.findElements(By.xpath("//span[text()='继续']"));
            elements.get(elements.size()-1).click();
        }
        if (pageSource.contains("查看你的電子郵件")){
            seleniumService.threadSleep(10);
            String verificationCode = "";
            try {
                System.out.println("取码----");
                verificationCode = EmailUtil.getVerificationCode(emailService.getMessage(emailService.selectEmailByEmail(createInfo.getEmail())));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='text']"))).sendKeys(verificationCode);
            List<WebElement> elements = webDriver.findElements(By.xpath("//span[text()='继续' or text()='繼續']"));
            elements.get(elements.size()-1).click();
        }
        seleniumService.threadSleep(5);
        pageSource = webDriver.getPageSource();
        if (pageSource.contains("请重新输入密码") || pageSource.contains("請重新輸入密碼")){
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='password']"))).sendKeys(createInfo.getPassword());
            List<WebElement> elements = webDriver.findElements(By.xpath("//span[text()='继续' or text()='繼續']"));
            elements.get(elements.size()-1).click();
        }
        try {
            seleniumService.threadSleep(2);
            List<WebElement> elements = webDriver.findElements(By.xpath("//span[text()='继续' or text()='繼續']"));
            elements.get(elements.size()-1).click();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String fa = "";
        try {
            seleniumService.threadSleep(10);
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@role='button' and text()='复制密钥' or text()='複製金鑰' ]"))).click();
            seleniumService.threadSleep(2);
            // 获取系统剪贴板
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            // 获取剪贴板中的内容
            Transferable contents = clipboard.getContents(null);
            // 判断是否是文本类型
            if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    fa = (String) contents.getTransferData(DataFlavor.stringFlavor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("剪贴板中没有文本内容");
            }
        } catch (Exception e) {
            System.out.println("没有读取到复制秘钥");
        }
        String twoFactorPageSource = webDriver.getPageSource();
        if (fa.equals("")){
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
        }
        if (!fa.equals("")){
            seleniumService.threadSleep(2);
            try {
                List<WebElement> elements = webDriver.findElements(By.xpath("//span[text()='输入验证码' or text()='輸入驗證碼']"));
                elements.get(elements.size()-1).click();
            } catch (Exception e) {
                e.printStackTrace();
            }
            seleniumService.threadSleep(2);
            String verificationCode = FBAccountUtil.getGoogleVerificationCode(fa);
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@maxlength='6']"))).sendKeys(verificationCode);
            try {
                seleniumService.threadSleep(2);
                List<WebElement> elements = webDriver.findElements(By.xpath("//span[text()='继续' or text()='繼續']"));
                elements.get(elements.size() - 1).click();
            } catch (Exception e) {
                e.printStackTrace();
            }


            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='password']"))).sendKeys(createInfo.getPassword());
            List<WebElement> elements = webDriver.findElements(By.xpath("//span[text()='继续' or text()='繼續']"));
            elements.get(elements.size() - 1).click();


            try {
                webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='完成']"))).click();
            } catch (Exception e) {
                e.printStackTrace();
            }
            createInfo.setSecretKey(fa);
            updateCreateInfo(createInfo);
            return "";
        }else {
            String oldNote = createInfo.getNote();

            if (oldNote == null || oldNote.isEmpty()) {
                createInfo.setNote("添加双重验证码失败");
            } else {
                createInfo.setNote(oldNote + " | 添加双重验证码失败");
            }

            updateCreateInfo(createInfo);
            return "";
        }
    }

    /**
     * 登录邮箱
     *
     * @param webDriver
     * @param createInfo
     * @return
     */
    @Override
    public String loginEmail(WebDriver webDriver, CreateInfo createInfo) {

        // 使用 JS 打开新标签页
        ((JavascriptExecutor) webDriver).executeScript("window.open('https://login.live.com', '_blank');");

        // 获取所有窗口句柄
        ArrayList<String> tabs = new ArrayList<String>(webDriver.getWindowHandles());

        // 切换到新标签页
        webDriver.switchTo().window(tabs.get(1));
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30, 1);
        //输入邮箱
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='usernameEntry']"))).sendKeys(createInfo.getEmail());
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@data-testid='primaryButton']"))).click();
        //输入密码
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='passwordEntry']"))).sendKeys(createInfo.getEmailPassword());
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@data-testid='primaryButton']"))).click();

        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@id='id__0']"))).click();

        boolean b = seleniumService.waitingForContent(5, webDriver, "保持登录状态?");
        if (b){
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@data-testid='primaryButton']"))).click();
        }
        seleniumService.threadSleep(5);
        ((JavascriptExecutor) webDriver).executeScript("window.open('https://outlook.live.com/mail/0/?refd=account.microsoft.com&fref=home.banner.viewinbox', '_blank');");


        return "";
    }

    /**
     * 删除电话
     *
     * @param webDriver
     * @param createInfo
     * @return
     */
    @Override
    public String deleteAccountPhoneNumber(WebDriver webDriver, CreateInfo createInfo) {
        webDriver.get("https://accountscenter.facebook.com/personal_info/contact_points/?contact_point_type=phone_number&contact_point_value=%2B86"+createInfo.getPhone()+"&dialog_type=contact_detail");
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30, 1);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[normalize-space(text())='删除手机号']"))).click();
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[normalize-space(text())='删除']"))).click();
        boolean b = seleniumService.waitingForContent(10, webDriver, "你已删除之前的手机号");
        if (b){
            createInfo.setPhone("");
            updateCreateInfo(createInfo);
            return "1";
        }
        return "";
    }

    /**
     * 改成繁体
     *
     * @param webDriver
     * @param createInfo
     * @return
     */
    @Override
    public String changeToTraditional(WebDriver webDriver, CreateInfo createInfo) {
        webDriver.get("https://www.facebook.com/settings/?tab=language_and_region");
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30, 1);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[normalize-space(text())='账户语言']"))).click();
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@placeholder='搜索语言']"))).sendKeys("台");
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[normalize-space(text())='繁体中文（台湾）']"))).click();
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[normalize-space(text())='确定']"))).click();
        webDriver.get("https://www.facebook.com/");
        return "";
    }

    /**
     * 发帖
     *
     * @param webDriver
     * @param createInfo
     * @return
     */
    @Override
    public String posts(WebDriver webDriver, CreateInfo createInfo) {
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
            seleniumService.threadSleep(2);
            WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30, 1);
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(normalize-space(.), '在想些什麼')]"))).click();
            seleniumService.threadSleep(1);
            String pageSource = webDriver.getPageSource();
            //如果仅朋友可见
            String xpath = "";
            if (pageSource.contains("https://static.xx.fbcdn.net/rsrc.php/v4/yJ/r/zPcex_q0TM1.png")) {
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@src='https://static.xx.fbcdn.net/rsrc.php/v4/yJ/r/zPcex_q0TM1.png']"))).click();
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@src='https://static.xx.fbcdn.net/rsrc.php/v4/yC/r/uaBHGktnPxt.png']"))).click();
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[normalize-space(text())='完成']"))).click();

            }

            Posts posts = new Posts();
            posts.setType("fbAccount");
            List<Posts> postsList = postsService.selectPostsList(posts);
            random = new Random();
            randomIndex = random.nextInt(postsList.size());
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@role='textbox' and @contenteditable='true']"))).sendKeys(postsList.get(randomIndex).getContent());
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
                operationLog.setOperationAccount(createInfo.getId());
                operationLog.setOperationAccountKeyId(createInfo.getKeyId());
                operationLog.setOperationContent("新号发帖");
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
        return "";
    }


}
