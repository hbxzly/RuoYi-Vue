package com.ruoyi.account.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ruoyi.account.domain.*;
import com.ruoyi.account.mapper.EmailMapper;
import com.ruoyi.account.service.*;
import com.ruoyi.account.util.*;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.impl.SysUserServiceImpl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * emailService业务层处理
 * 
 * @author ruoyi
 * @date 2024-06-21
 */
@Service
public class EmailServiceImpl implements IEmailService {

    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);
    
    @Autowired
    private EmailMapper emailMapper;

    @Autowired
    ISeleniumService seleniumService;

    @Autowired
    IFbAccountForSellService fbAccountForSellService;

    @Autowired
    IFbAccountService fbAccountService;

    @Autowired
    IOperationLogService operationLogService;




    /**
     * 查询email
     * 
     * @param keyId email主键
     * @return email
     */
    @Override
    public Email selectEmailByKeyId(Long keyId)
    {
        return emailMapper.selectEmailByKeyId(keyId);
    }

    /**
     * 查询email
     *
     * @param email
     * @return
     */
    @Override
    public Email selectEmailByEmail(String email) {
        return emailMapper.selectEmailByEmail(email);
    }

    /**
     * 查询email列表
     * 
     * @param email email
     * @return email
     */
    @Override
    public List<Email> selectEmailList(Email email) {
        return emailMapper.selectEmailList(email);
    }

    /**
     * 新增email
     * 
     * @param email email
     * @return 结果
     */
    @Override
    public int insertEmail(Email email) {
        if (selectEmailByEmail(email.getEmail()) == null) {
            return emailMapper.insertEmail(email);
        }
        return 0;
    }

    /**
     * 修改email
     * 
     * @param email email
     * @return 结果
     */
    @Override
    public int updateEmail(Email email) {
        if(email.getStatus() != null && email.getStatus().equals("1")){
            FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByEmail(email.getEmail());
            if (fbAccountForSell != null) {
                fbAccountForSell.setEmailStatus("1");
                fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
            }
        }
        if(email.getStatus() != null && email.getStatus().equals("0")){
            FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByEmail(email.getEmail());
            if (fbAccountForSell != null) {
                fbAccountForSell.setEmailStatus("0");
                fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
            }
        }
        return emailMapper.updateEmail(email);
    }

    /**
     * 批量删除email
     * 
     * @param keyIds 需要删除的email主键
     * @return 结果
     */
    @Override
    public int deleteEmailByKeyIds(Long[] keyIds) {
        return emailMapper.deleteEmailByKeyIds(keyIds);
    }

    /**
     * 删除email信息
     * 
     * @param keyId email主键
     * @return 结果
     */
    @Override
    public int deleteEmailByKeyId(Long keyId)
    {
        return emailMapper.deleteEmailByKeyId(keyId);
    }

    /**
     * 导入email数据
     *
     * @param emailList       FB数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importEmail(List<Email> emailList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(emailList) || emailList.size() == 0) {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Email email : emailList) {
            try
            {
                // 验证是否存在这个账号
                Email e = emailMapper.selectEmailByEmail(email.getEmail());
                if (StringUtils.isNull(e))
                {
                    emailMapper.insertEmail(email);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、邮箱 " + email.getEmail() + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    updateEmail(email);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、邮箱 " + email.getEmail()  + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、邮箱 " + email.getEmail()  + " 已存在");
                }
            }
            catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、邮箱 " + email.getEmail()  + " 导入失败：";
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
     * 获取邮件
     *
     * @param email
     * @return
     */
    @Override
    public String getMessage(Email email) throws JsonProcessingException {
        String url = "https://bsh.bhdata.com:30015/bhmailer?uid=492746380&sign=99277cf10db6483c92e3c6e142bb8db1&act=checkMail&email="+email.getEmail()+"&pass="+email.getPassword()+"&sent=-10000&t=-100000";
        String message = "";
        try {
            message = HttpClientUtil.get(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String msg = JsonDataUtil.getValueByNodeName(message, "msg");
        if (msg != null && msg.contains("需要绑定辅助邮箱，但没有启用自动绑定辅助邮箱")){
            email.setStatus("1");
            updateEmail(email);
            FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByEmail(email.getEmail());
            if (fbAccountForSell != null) {
                fbAccountForSell.setEmailStatus("1");
                fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
            }
        }
        if (msg != null && msg.contains("没有发现匹配的邮件")){
            email.setStatus("1");
            updateEmail(email);
            FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByEmail(email.getEmail());
            if (fbAccountForSell != null) {
                fbAccountForSell.setEmailStatus("1");
                fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
            }
        }
        if (msg != null && msg.contains("没有启用自动验证手机号码")){
            email.setStatus("0");
            updateEmail(email);
            FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByEmail(email.getEmail());
            if (fbAccountForSell != null) {
                fbAccountForSell.setEmailStatus("0");
                fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
            }else {
                FbAccount fbAccount = fbAccountService.selectFbAccountByEmail(email.getEmail());
                if (fbAccount != null) {
                    fbAccount.setNote(fbAccount.getNote() + "邮箱无法用");
                    fbAccountService.updateFbAccount(fbAccount);
                }
            }

        }
        if (msg != null && msg.contains("That Microsoft account doesn't exist")){
            email.setStatus("0");
            updateEmail(email);
            FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByEmail(email.getEmail());
            if (fbAccountForSell != null) {
                fbAccountForSell.setEmailStatus("0");
                fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
            }else {
                FbAccount fbAccount = fbAccountService.selectFbAccountByEmail(email.getEmail());
                if (fbAccount != null) {
                    fbAccount.setNote(fbAccount.getNote() + "邮箱无法用");
                    fbAccountService.updateFbAccount(fbAccount);
                }
            }

        }
        if (msg != null && msg.contains("We couldn't find an account with that username")){
            email.setStatus("0");
            updateEmail(email);
            FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByEmail(email.getEmail());
            if (fbAccountForSell != null) {
                fbAccountForSell.setEmailStatus("0");
                fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
            }else {
                FbAccount fbAccount = fbAccountService.selectFbAccountByEmail(email.getEmail());
                if (fbAccount != null) {
                    fbAccount.setNote(fbAccount.getNote() + "邮箱无法用");
                    fbAccountService.updateFbAccount(fbAccount);
                }
            }

        }
        if (msg != null && msg.contains("sign in to account.live.com from a browser")){
            email.setStatus("0");
            updateEmail(email);
            FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByEmail(email.getEmail());
            if (fbAccountForSell != null) {
                fbAccountForSell.setEmailStatus("0");
                fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
            }else {
                FbAccount fbAccount = fbAccountService.selectFbAccountByEmail(email.getEmail());
                if (fbAccount != null) {
                    fbAccount.setNote(fbAccount.getNote() + "邮箱无法用");
                    fbAccountService.updateFbAccount(fbAccount);
                }
            }
        }
        return msg;
    }

//    @Override
    public String unlockEmailByBitBrowser(Email email, ProxyIp proxyIp) {

        FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByEmail(email.getEmail());
        Map<String, Object> createAndUpdateConfig = BiteBrowser.createAndUpdateBrowse();

        createAndUpdateConfig.put("proxyType", "socks5");//['noproxy', 'http', 'https', 'socks5', 'ssh']
        createAndUpdateConfig.put("host", "192.168.1.88");
        createAndUpdateConfig.put("port", "10808");
        createAndUpdateConfig.put("proxyUserName", "");
        createAndUpdateConfig.put("proxyPassword", "");

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
                        openData.forEach((k, v) -> System.out.println(k+"="+v));
                        if (openData != null) {
                            try {
                                //参数配置
                                System.setProperty("webdriver.chrome.driver", openData.get("driver").toString());
                                ChromeOptions options = new ChromeOptions();
                                options.setExperimentalOption("debuggerAddress", openData.get("http").toString());
                                WebDriver webDriver = new ChromeDriver(options);
                                try {
                                    try {
                                        webDriver.get("https://login.live.com");
                                    } catch (Exception e) {
                                        webDriver.get("https://login.live.com");
                                    }
                                    WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30, 1);
//                                    boolean forContent = seleniumService.waitingForContent(10, webDriver, "使用你的 Microsoft 帐户");
                                    boolean forContent = seleniumService.waitingForContent(10, webDriver, "Use your Microsoft account");
                                    if (forContent) {
                                        //输入邮箱
                                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='usernameEntry']"))).sendKeys(email.getEmail());
                                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@data-testid='primaryButton']"))).click();

                                        seleniumService.threadSleep(2);

                                        if (webDriver.getPageSource().contains("We couldn't find a Microsoft account")){
                                            email.setStatus("0");
                                            updateEmail(email);
                                            webDriver.close();
                                            webDriver.quit();
                                            return "0";
                                        }
                                        //判断是否已绑有辅助邮箱
                                        if (webDriver.getPageSource().contains("Use your password")){
                                            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[normalize-space(text())='Use your password']"))).click();
                                        }
                                        if (webDriver.getPageSource().contains("Other ways to sign in")){
                                            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@role='button' and normalize-space(text())='Other ways to sign in']"))).click();
                                            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@role='button' and normalize-space(text())='Use your password']"))).click();
                                        }
                                        seleniumService.threadSleep(2);
                                        //输入密码
                                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='passwordEntry']"))).sendKeys(email.getPassword());
                                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@data-testid='primaryButton']"))).click();


                                    }else {
                                        //输入邮箱
                                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='i0116']"))).sendKeys(email.getEmail());
                                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='idSIButton9']"))).click();
                                        //输入密码
                                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='i0118']"))).sendKeys(email.getPassword());
                                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='idSIButton9']"))).click();
                                    }

                                    String pageSource = webDriver.getPageSource();
                                    if (pageSource.contains("We couldn't find a Microsoft account")){
                                        email.setStatus("0");
                                        updateEmail(email);
                                        webDriver.close();
                                        webDriver.quit();
                                        return "0";
                                    }
                                    if (pageSource.contains("A quick note about your Microsoft account") || pageSource.contains("Sign in faster with your face, fingerprint, or PIN")){
                                        email.setStatus("1");
                                        updateEmail(email);
                                        fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByEmail(email.getEmail());
                                        fbAccountForSell.setEmailStatus("1");
                                        fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
                                        webDriver.close();
                                        webDriver.quit();
                                        return "1";
                                    }
                                    if (pageSource.contains("Setting up your passkey...")){
                                        email.setStatus("1");
                                        updateEmail(email);
                                        fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByEmail(email.getEmail());
                                        fbAccountForSell.setEmailStatus("1");
                                        fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
                                        webDriver.close();
                                        webDriver.quit();
                                        return "1";
                                    }

                                    boolean b = seleniumService.waitingForContent(5, webDriver, "We're updating our terms");
                                    if (b) {
                                        //点击next
                                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@data-testid='primaryButton']"))).click();
                                    }
                                    String mobile = null;
                                    try {
                                        waitingForContent(5, webDriver, "Your account has been locked");
                                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='StartAction']"))).click();

                                        mobile = HaoZhuUtil.getMobile(HaoZhuUtil.token,HaoZhuUtil.sid,0,"17","");
                                        WebElement countryDropdown = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='phoneCountry']")));
                                        Select select = new Select(countryDropdown);
                                        select.selectByValue("CN");
                                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='proofField']"))).sendKeys(mobile);
                                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='nextButton']"))).click();
                                    } catch (Exception e) {
                                        webDriver.close();
                                        webDriver.quit();
                                        return "2";
                                    }
//                                    b = waitingForContent(5, webDriver, "该验证方法目前无效。请尝试其他方法。");
                                    b = waitingForContent(10, webDriver, "Try another verification method");
                                    if (b){
                                        email.setNote("该验证方法目前无效。请尝试其他方法。");
                                        updateEmail(email);
                                        webDriver.close();
                                        webDriver.quit();
                                        return "2";
                                    }
                                    if (webDriver.getPageSource().contains("Help us beat the robots")){
                                        String oldNote = fbAccountForSell.getNote();
                                        fbAccountForSell.setNote("解邮箱机器验证|"+oldNote);
                                        fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
                                        return "";
                                    }
                                    //取码
                                    String code = "";
                                    for (int i = 0; i < 10; i++) {
                                        code = HaoZhuUtil.getCode(HaoZhuUtil.token,HaoZhuUtil.sid,mobile);
                                        if (code .equals("wait")) {
                                            try {
                                                Thread.sleep(10000);
                                            } catch (InterruptedException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }else {
                                            break;
                                        }
                                    }
                                    if (code.equals("wait")) {
                                        LocalDateTime now = LocalDateTime.now();
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
                                        String result = now.format(formatter);
                                        String oldNote = fbAccountForSell.getNote();
                                        fbAccountForSell.setNote("接不到码"+result+oldNote);
                                        fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
                                        email.setNote("接不到码");
                                        updateEmail(email);
                                        webDriver.close();
                                        webDriver.quit();
                                        return "3";
                                    }
                                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='enter-code-input']"))).sendKeys(code);
                                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='nextButton']"))).click();
                                    seleniumService.threadSleep(5);
                                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@id='FinishAction']"))).click();
//                                    boolean waitingForContent = waitingForContent(10, webDriver, "已取消阻止你的帐户");
                                    boolean waitingForContentA = waitingForContent(10, webDriver, "A quick note about your Microsoft account");
                                    boolean waitingForContentB = waitingForContent(3, webDriver, "Sign in faster with your face, fingerprint, or PIN");

                                    if (waitingForContentA || waitingForContentB){
                                        email.setStatus("1");
                                        email.setNote("");
                                        updateEmail(email);
                                        if (fbAccountForSell != null) {
                                            fbAccountForSell.setEmailStatus("1");
                                            fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
                                            OperationLog operationLog = new OperationLog();
                                            operationLog.setOperationAccount(fbAccountForSell.getId());
                                            operationLog.setOperationAccountKeyId(fbAccountForSell.getKeyId());
                                            operationLog.setOperationContent("解锁邮箱");
                                            operationLog.setOperationStatus("成功");
                                            operationLog.setOperationTime(new Date());
                                            operationLogService.insertOperationLog(operationLog);
                                            return "1";
                                        }else {
                                            FbAccount fbAccount = fbAccountService.selectFbAccountByEmail(email.getEmail());
                                            OperationLog operationLog = new OperationLog();
//                                            operationLog.setOperationAccount(fbAccount.getId());
//                                            operationLog.setOperationAccountKeyId(fbAccount.getKeyId());
                                            operationLog.setOperationContent("解锁邮箱");
                                            operationLog.setOperationStatus("成功");
                                            operationLog.setOperationTime(new Date());
                                            operationLogService.insertOperationLog(operationLog);
                                        }

                                        seleniumService.threadSleep(2);
//                                        webDriver.close();
//                                        webDriver.quit();
                                    }else {
                                        fbAccountForSell.setEmailStatus("3");
                                        email.setStatus("3");
                                        updateEmail(email);
                                        fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
                                        seleniumService.threadSleep(2);
//                                        webDriver.close();
//                                        webDriver.quit();
                                    }
                                } catch (Exception e) {
                                    if (webDriver.getPageSource().contains("We couldn't find a Microsoft account")){
                                        fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByEmail(email.getEmail());
                                        fbAccountForSell.setEmailStatus("0");
                                        fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
                                        webDriver.close();
                                        webDriver.quit();
                                        return "0";
                                    }
                                    if (webDriver.getPageSource().contains("We couldn't find a Microsoft account")){
                                        fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByEmail(email.getEmail());
                                        fbAccountForSell.setEmailStatus("0");
                                        fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
                                        webDriver.close();
                                        webDriver.quit();
                                        return "0";
                                    }
                                    if (webDriver.getPageSource().contains("A quick note about your Microsoft account")){
                                        fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByEmail(email.getEmail());
                                        fbAccountForSell.setEmailStatus("1");
                                        fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
                                        webDriver.close();
                                        webDriver.quit();
                                        return "1";
                                    }
//                                    webDriver.close();
//                                    webDriver.quit();
                                    e.printStackTrace();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                    Map<String, Object> closeBrowserConfig = BiteBrowser.closeBrowse();
//                    closeBrowserConfig.put("id",id);
                    OkHttpUtil.post("http://127.0.0.1:54345/browser/close",closeBrowserConfig);
                }
            }
        } catch (Exception e) {
            Map<String, Object> closeBrowserConfig = BiteBrowser.closeBrowse();
//                    closeBrowserConfig.put("id",id);
            try {
                OkHttpUtil.post("http://127.0.0.1:54345/browser/close",closeBrowserConfig);
            } catch (Exception ee) {
                throw new RuntimeException(ee);
            }
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public String unlockEmail(Email email, WebDriver webDriver){
        FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByEmail(email.getEmail());
        try {
            try {
                webDriver.get("https://login.live.com");
            } catch (Exception e) {
                webDriver.get("https://login.live.com");
            }
            WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30, 1);
//                                    boolean forContent = seleniumService.waitingForContent(10, webDriver, "使用你的 Microsoft 帐户");
            boolean forContent = seleniumService.waitingForContent(10, webDriver, "Use your Microsoft account");
            if (forContent) {
                //输入邮箱
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='usernameEntry']"))).sendKeys(email.getEmail());
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@data-testid='primaryButton']"))).click();

                seleniumService.threadSleep(2);

                if (webDriver.getPageSource().contains("We couldn't find a Microsoft account")){
                    email.setStatus("0");
                    updateEmail(email);
                    webDriver.close();
                    webDriver.quit();
                    return "0";
                }
                //判断是否已绑有辅助邮箱
                if (webDriver.getPageSource().contains("Use your password")){
                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[normalize-space(text())='Use your password']"))).click();
                }
                if (webDriver.getPageSource().contains("To verify this is your email, enter it here")){
                    seleniumService.threadSleep(2);
                    if (webDriver.getPageSource().contains("Other ways to sign in")){
                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@role='button' and normalize-space(text())='Other ways to sign in']"))).click();
                    }
                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@role='button' and normalize-space(text())='Use your password']"))).click();
                }
                seleniumService.threadSleep(2);
                //输入密码
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='passwordEntry']"))).sendKeys(email.getPassword());
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@data-testid='primaryButton']"))).click();


            }else {
                //输入邮箱
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='i0116']"))).sendKeys(email.getEmail());
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='idSIButton9']"))).click();
                //输入密码
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='i0118']"))).sendKeys(email.getPassword());
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='idSIButton9']"))).click();
            }

            String pageSource = webDriver.getPageSource();
            if (pageSource.contains("We couldn't find a Microsoft account")){
                email.setStatus("0");
                updateEmail(email);
                webDriver.close();
                webDriver.quit();
                return "0";
            }
            if (pageSource.contains("We have detected unusual activity on your Microsoft account "+email.getEmail()+", or believe your credentials are at risk.")){
                email.setStatus("0");
                updateEmail(email);
                webDriver.close();
                webDriver.quit();
                return "0";
            }
            if (pageSource.contains("A quick note about your Microsoft account") || pageSource.contains("Sign in faster with your face, fingerprint, or PIN")){
                email.setStatus("1");
                updateEmail(email);
                fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByEmail(email.getEmail());
                fbAccountForSell.setEmailStatus("1");
                fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
                webDriver.close();
                webDriver.quit();
                return "1";
            }
            if (pageSource.contains("Setting up your passkey...")){
                email.setStatus("1");
                updateEmail(email);
                fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByEmail(email.getEmail());
                fbAccountForSell.setEmailStatus("1");
                fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
                webDriver.close();
                webDriver.quit();
                return "1";
            }

            boolean b = seleniumService.waitingForContent(5, webDriver, "We're updating our terms");
            if (b) {
                //点击next
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@data-testid='primaryButton']"))).click();
            }
            String mobile = null;
            try {
                waitingForContent(5, webDriver, "Your account has been locked");
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='StartAction']"))).click();

                mobile = HaoZhuUtil.getMobile(HaoZhuUtil.token,HaoZhuUtil.sid,0,"197","");
                WebElement countryDropdown = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='phoneCountry']")));
                Select select = new Select(countryDropdown);
                select.selectByValue("CN");
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='proofField']"))).sendKeys(mobile);
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='nextButton']"))).click();
            } catch (Exception e) {
                webDriver.close();
                webDriver.quit();
                return "2";
            }
//                                    b = waitingForContent(5, webDriver, "该验证方法目前无效。请尝试其他方法。");
            b = waitingForContent(10, webDriver, "Try another verification method");
            if (b){
                email.setNote("该验证方法目前无效。请尝试其他方法。");
                updateEmail(email);
                webDriver.close();
                webDriver.quit();
                return "2";
            }
            if (webDriver.getPageSource().contains("Help us beat the robots")){
                fbAccountForSell.setNote("解邮箱机器验证");
                fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
                return "";
            }
            //取码
            String code = "";
            for (int i = 0; i < 10; i++) {
                code = HaoZhuUtil.getCode(HaoZhuUtil.token,HaoZhuUtil.sid,mobile);
                if (code .equals("wait")) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    break;
                }
            }
            if (code.equals("wait")) {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
                String result = now.format(formatter);
                String oldNote = fbAccountForSell.getNote();
                fbAccountForSell.setNote("接不到码"+result+oldNote);
                fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
                email.setNote("接不到码");
                updateEmail(email);
                webDriver.close();
                webDriver.quit();
                return "3";
            }
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='enter-code-input']"))).sendKeys(code);
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='nextButton']"))).click();
            seleniumService.threadSleep(5);
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@id='FinishAction']"))).click();
//                                    boolean waitingForContent = waitingForContent(10, webDriver, "已取消阻止你的帐户");
            boolean waitingForContentA = waitingForContent(10, webDriver, "A quick note about your Microsoft account");
            boolean waitingForContentB = waitingForContent(3, webDriver, "Sign in faster with your face, fingerprint, or PIN");

            if (waitingForContentA || waitingForContentB){
                email.setStatus("1");
                email.setNote("");
                updateEmail(email);
                if (fbAccountForSell != null) {
                    fbAccountForSell.setEmailStatus("1");
                    fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
                    OperationLog operationLog = new OperationLog();
                    operationLog.setOperationAccount(fbAccountForSell.getId());
                    operationLog.setOperationAccountKeyId(fbAccountForSell.getKeyId());
                    operationLog.setOperationContent("解锁邮箱");
                    operationLog.setOperationStatus("成功");
                    operationLog.setOperationTime(new Date());
                    operationLogService.insertOperationLog(operationLog);
                    return "1";
                }else {
                    FbAccount fbAccount = fbAccountService.selectFbAccountByEmail(email.getEmail());
                    OperationLog operationLog = new OperationLog();
//                                            operationLog.setOperationAccount(fbAccount.getId());
//                                            operationLog.setOperationAccountKeyId(fbAccount.getKeyId());
                    operationLog.setOperationContent("解锁邮箱");
                    operationLog.setOperationStatus("成功");
                    operationLog.setOperationTime(new Date());
                    operationLogService.insertOperationLog(operationLog);
                }

                seleniumService.threadSleep(2);
//                                        webDriver.close();
//                                        webDriver.quit();
            }else {
                fbAccountForSell.setEmailStatus("3");
                email.setStatus("3");
                updateEmail(email);
                fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
                seleniumService.threadSleep(2);
//                                        webDriver.close();
//                                        webDriver.quit();
            }
        } catch (Exception e) {
            if (webDriver.getPageSource().contains("We couldn't find a Microsoft account")){
                fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByEmail(email.getEmail());
                fbAccountForSell.setEmailStatus("0");
                fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
                webDriver.close();
                webDriver.quit();
                return "0";
            }
            if (webDriver.getPageSource().contains("We couldn't find a Microsoft account")){
                fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByEmail(email.getEmail());
                fbAccountForSell.setEmailStatus("0");
                fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
                webDriver.close();
                webDriver.quit();
                return "0";
            }
            if (webDriver.getPageSource().contains("A quick note about your Microsoft account")){
                fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByEmail(email.getEmail());
                fbAccountForSell.setEmailStatus("1");
                fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
                webDriver.close();
                webDriver.quit();
                return "1";
            }
//                                    webDriver.close();
//                                    webDriver.quit();
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 解锁邮箱-加号码
     *
     * @param email
     * @param proxyIp
     * @return
     */
    @Override
    public Email unlockEmailAddTelephone(Email email, ProxyIp proxyIp) {

        Map<String, Object> createAndUpdateConfig = BiteBrowser.createAndUpdateBrowse();
        createAndUpdateConfig.put("proxyType", "socks5");//['noproxy', 'http', 'https', 'socks5', 'ssh']
        createAndUpdateConfig.put("host", proxyIp.getHostname());
        createAndUpdateConfig.put("port", proxyIp.getPort());
        createAndUpdateConfig.put("proxyUserName", proxyIp.getUsername());
        createAndUpdateConfig.put("proxyPassword", proxyIp.getPassword());

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
                        openData.forEach((k, v) -> System.out.println(k+"="+v));
                        if (openData != null) {
                            try {
                                //参数配置
                                System.setProperty("webdriver.chrome.driver", openData.get("driver").toString());
                                ChromeOptions options = new ChromeOptions();
                                options.setExperimentalOption("debuggerAddress", openData.get("http").toString());
                                WebDriver webDriver = new ChromeDriver(options);
                                try {
                                    webDriver.get("https://login.live.com");
                                    WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30, 1);
                                    //输入邮箱
                                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='i0116']"))).sendKeys(email.getEmail());
                                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='idSIButton9']"))).click();
                                    //输入密码
                                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='i0118']"))).sendKeys(email.getPassword());
                                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='idSIButton9']"))).click();
                                    boolean b = waitingForContent(5, webDriver, "你的帐户或密码不正确");
                                    if (b){
                                        email.setNote("账号密码不正确");
                                        updateEmail(email);
                                        webDriver.close();
                                        webDriver.quit();
                                        return email;
                                    }
                                    String pageSource = webDriver.getPageSource();
                                    if (pageSource.contains("该 Microsoft 帐户不存在")){
                                        email.setStatus("0");
                                        updateEmail(email);
                                        webDriver.close();
                                        webDriver.quit();
                                        return email;
                                    }
                                    b = waitingForContent(5, webDriver, "帮助我们保护帐户");
                                    if (b){
                                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='iLandingViewAction']"))).click();
                                    }

                                    String mobile = DefuUtil.getMobile("27030", "167","171");
                                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='DisplayPhoneNumber']"))).sendKeys(mobile);
                                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='iCollectPhoneViewAction']"))).click();
                                    b = waitingForContent(5, webDriver, "机器人");
                                    if (b){
                                        seleniumService.threadSleep(50);
                                    }
                                    //取码
                                    String code = "";
                                    for (int i = 0; i < 8; i++) {
                                        code = DefuUtil.getCode(mobile, "27030");
                                        if (code .equals("wait")) {
                                            try {
                                                Thread.sleep(10000);
                                            } catch (InterruptedException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }else {
                                            break;
                                        }
                                    }
                                    if (code.equals("wait")) {
                                        email.setNote("接不到码");
                                        webDriver.close();
                                        webDriver.quit();
                                        return email;
                                    }
                                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='iOttText']"))).sendKeys(code);
                                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='iVerifyPhoneViewAction']"))).click();
                                    boolean waitingForContent = waitingForContent(5, webDriver, "设置新密码");
                                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='iPassword']"))).sendKeys(email.getPassword()+"!");
                                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='iPasswordViewAction']"))).click();
                                    seleniumService.threadSleep(10);
                                    boolean isChargePassword = waitingForContent(5, webDriver, "你的密码已更改");
                                    if (isChargePassword){
                                        email.setStatus("1");
                                        email.setPassword(email.getPassword()+"!");
                                        email.setNote("");
                                        updateEmail(email);
                                        FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByEmail(email.getEmail());
                                        if (fbAccountForSell != null) {
                                            fbAccountForSell.setEmailStatus("1");
                                            fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
                                            OperationLog operationLog = new OperationLog();
                                            operationLog.setOperationAccount(fbAccountForSell.getId());
                                            operationLog.setOperationAccountKeyId(fbAccountForSell.getKeyId());
                                            operationLog.setOperationContent("解锁邮箱");
                                            operationLog.setOperationStatus("成功");
                                            operationLog.setOperationTime(new Date());
                                            operationLogService.insertOperationLog(operationLog);
                                        }else {
                                            FbAccount fbAccount = fbAccountService.selectFbAccountByEmail(email.getEmail());
                                            OperationLog operationLog = new OperationLog();
                                            operationLog.setOperationAccount(fbAccount.getId());
                                            operationLog.setOperationAccountKeyId(fbAccount.getKeyId());
                                            operationLog.setOperationContent("解锁邮箱");
                                            operationLog.setOperationStatus("成功");
                                            operationLog.setOperationTime(new Date());
                                            operationLogService.insertOperationLog(operationLog);
                                        }

                                        /*webDriver.close();
                                        webDriver.quit();*/
                                    }
                                } catch (Exception e) {
//                                    webDriver.close();
//                                    webDriver.quit();
                                    e.printStackTrace();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                    Map<String, Object> closeBrowserConfig = BiteBrowser.closeBrowse();
                    closeBrowserConfig.put("id",id);
                    OkHttpUtil.post("http://127.0.0.1:54345/browser/close",closeBrowserConfig);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return email;
    }

    /**
     * 检查邮箱
     *
     * @param email
     */
    @Override
    public String CheckEmail(Email email) {
        WebDriver webDriver = seleniumService.openBrowserForEmail(email);
        webDriver.get("https://login.live.com");
        WebDriverWait wait = new WebDriverWait(webDriver, 30, 1);
        //输入邮箱
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='i0116']")))
                .sendKeys(email.getEmail());
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='idSIButton9']")))
                .click();

        boolean b = waitingForContent(5, webDriver, "该 Microsoft 帐户不存在");
        if (!b){
            email.setStatus("0");
            webDriver.quit();
            return "";
        }
        //输入密码，判断是否被释放
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='i0118']")))
                .sendKeys(email.getPassword());
        boolean passwordB = waitingForContent(5, webDriver, "你的帐户或密码不正确");
        if (!passwordB){
            email.setStatus("3");
            email.setNote("密码错误");
            webDriver.quit();
            return "";
        }



        return "";
    }

    /**
     * 临时登录
     *
     * @param email
     * @param password
     */
    @Override
    public void tempLogin(String email, String password, ProxyIp proxyIp) {
        Map<String, Object> createAndUpdateConfig = BiteBrowser.createAndUpdateBrowse();

        createAndUpdateConfig.put("proxyType", proxyIp.getProxyType());//['noproxy', 'http', 'https', 'socks5', 'ssh']
        createAndUpdateConfig.put("host", proxyIp.getHostname());
        createAndUpdateConfig.put("port", proxyIp.getPort());
        createAndUpdateConfig.put("proxyUserName", proxyIp.getUsername());
        createAndUpdateConfig.put("proxyPassword", proxyIp.getPassword());

        try {
            Map<String, Object> resultMap = OkHttpUtil.post("http://127.0.0.1:54345/browser/update", createAndUpdateConfig);
            // 解析返回的 Map
            if (resultMap != null && (boolean) resultMap.get("success")) {
                // 获取 "data" 部分
                Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");

                // 获取 "id" 值
                if (dataMap != null) {
                    String id = (String) dataMap.get("id");
                    System.out.println(id);
                    Map<String, Object> openBrowseConfig = BiteBrowser.openBrowse();
                    openBrowseConfig.put("id", id);
                    Map<String, Object> openResultMap = OkHttpUtil.post("http://127.0.0.1:54345/browser/open", openBrowseConfig);
                    if (openResultMap != null && (boolean) openResultMap.get("success")) {
                        Map<String, Object> openData = (Map<String, Object>) openResultMap.get("data");
                        openData.forEach((k, v) -> System.out.println(k+"="+v));
                        if (openData != null) {
                            try {
                                //参数配置
                                System.setProperty("webdriver.chrome.driver", openData.get("driver").toString());
                                ChromeOptions options = new ChromeOptions();
                                options.setExperimentalOption("debuggerAddress", openData.get("http").toString());
                                WebDriver webDriver = new ChromeDriver(options);
                                try {
                                    webDriver.get("https://login.live.com");
                                    WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30, 1);
//                                    boolean forContent = seleniumService.waitingForContent(10, webDriver, "使用你的 Microsoft 帐户");
                                    boolean forContent = seleniumService.waitingForContent(10, webDriver, "Use your Microsoft account");
                                    if (forContent) {
                                        //输入邮箱
                                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='usernameEntry']"))).sendKeys(email);
                                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@data-testid='primaryButton']"))).click();

                                        seleniumService.threadSleep(2);
                                        //判断是否已绑有辅助邮箱
                                        if (webDriver.getPageSource().contains("Use your password")){
                                            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[normalize-space(text())='Use your password']"))).click();
                                        }
                                        if (webDriver.getPageSource().contains("Other ways to sign in")){
                                            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@role='button' and normalize-space(text())='Other ways to sign in']"))).click();
                                            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@role='button' and normalize-space(text())='Use your password']"))).click();
                                        }
                                        seleniumService.threadSleep(2);
                                        //输入密码
                                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='passwordEntry']"))).sendKeys(password);
                                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@data-testid='primaryButton']"))).click();
                                    }
                                    boolean b = seleniumService.waitingForContent(5, webDriver, "We're updating our terms");
                                    if (b) {
                                        //点击next
                                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@data-testid='primaryButton']"))).click();
                                    }
                                    seleniumService.threadSleep(2);
//                                    webDriver.get("https://outlook.live.com/mail/0/?refd=account.microsoft.com&fref=home.banner.viewinbox");
                                }catch (Exception e){
                                e.printStackTrace();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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

    // 模拟鼠标点击指定坐标位置
    public static void clickAtCoordinates(WebDriver driver, int x, int y) {
        // 使用Actions类模拟鼠标点击
        Actions actions = new Actions(driver);
        // 移动鼠标到指定坐标，并点击
        actions.moveByOffset(x, y).click().perform();
    }



}
