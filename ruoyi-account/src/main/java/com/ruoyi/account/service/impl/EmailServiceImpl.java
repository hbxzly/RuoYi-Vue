package com.ruoyi.account.service.impl;

import com.ruoyi.account.domain.Email;
import com.ruoyi.account.domain.FbAccountForSell;
import com.ruoyi.account.mapper.EmailMapper;
import com.ruoyi.account.service.IEmailService;
import com.ruoyi.account.service.ISeleniumService;
import com.ruoyi.account.util.DefuUtil;
import com.ruoyi.account.util.EmailUtil;
import com.ruoyi.account.util.RandomUitl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.impl.SysUserServiceImpl;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<Email> selectEmailList(Email email)
    {
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
        return emailMapper.insertEmail(email);
    }

    /**
     * 修改email
     * 
     * @param email email
     * @return 结果
     */
    @Override
    public int updateEmail(Email email)
    {
        return emailMapper.updateEmail(email);
    }

    /**
     * 批量删除email
     * 
     * @param keyIds 需要删除的email主键
     * @return 结果
     */
    @Override
    public int deleteEmailByKeyIds(Long[] keyIds)
    {
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
                    emailMapper.updateEmail(email);
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
    public String getMessage(Email email) {

        return "EmailUtil.getMessage(email)";
    }

    @Override
    public Email unlockEmail(Email email) {
            WebDriver webDriver = seleniumService.openBrowserForEmail(email);
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
                emailMapper.updateEmail(email);
                webDriver.quit();
                return email;
            }
            b = waitingForContent(5, webDriver, "我们即将更新条款");
            if (b){
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='iNext']"))).click();
            }
            waitingForContent(5, webDriver, "锁定");
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='StartAction']"))).click();

            String mobile = DefuUtil.getMobile("27030", "167","171");
            WebElement countryDropdown = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='phoneCountry']")));
            Select select = new Select(countryDropdown);
            select.selectByValue("CN");
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='proofField']"))).sendKeys(mobile);
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='nextButton']"))).click();
            b = waitingForContent(5, webDriver, "该验证方法目前无效。请尝试其他方法。");
            if (b){
                email.setNote("该验证方法目前无效。请尝试其他方法。");
                emailMapper.updateEmail(email);
                webDriver.quit();
            }
            //取码
            String code = "";
            for (int i = 0; i < 7; i++) {
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
                webDriver.quit();
                return email;
            }
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='enter-code-input']"))).sendKeys(code);
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='nextButton']"))).click();
            boolean waitingForContent = waitingForContent(5, webDriver, "已取消阻止你的帐户");
            if (waitingForContent){
                email.setStatus("1");
                email.setNote("");
                emailMapper.updateEmail(email);
                webDriver.quit();
            }
        } catch (Exception e) {
            webDriver.quit();
            e.printStackTrace();
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
     * 导入数据
     *
     * @param emailList       数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importFbAccountForSell(List<Email> emailList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(emailList) || emailList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Email email : emailList) {
            try {
                // 验证是否存在这个用户
                Email e = emailMapper.selectEmailByEmail(email.getEmail());
                if (StringUtils.isNull(e)) {
                    emailMapper.insertEmail(e);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、数据 " + email.getEmail() + " 导入成功");
                } else if (isUpdateSupport) {
                    emailMapper.updateEmail(email);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、数据 " + email.getEmail() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、数据 " + email.getEmail() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、数据 " + email.getEmail() + " 导入失败：";
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
