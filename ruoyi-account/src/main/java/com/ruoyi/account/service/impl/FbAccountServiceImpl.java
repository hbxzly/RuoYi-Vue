package com.ruoyi.account.service.impl;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ruoyi.account.domain.OperationLog;
import com.ruoyi.account.domain.Posts;
import com.ruoyi.account.service.IOperationLogService;
import com.ruoyi.account.service.IPostsService;
import com.ruoyi.account.service.ISeleniumService;
import com.ruoyi.account.util.BrowserConfig;
import com.ruoyi.account.util.FBAccountUtil;
import com.ruoyi.account.util.RandomUitl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.ruoyi.account.mapper.FbAccountMapper;
import com.ruoyi.account.domain.FbAccount;
import com.ruoyi.account.service.IFbAccountService;


/**
 * 账号Service业务层处理
 *
 * @author ruoyi
 * @date 2024-12-12
 */
@Service
@Component
public class FbAccountServiceImpl implements IFbAccountService {

    @Autowired
    private FbAccountMapper fbAccountMapper;

    @Autowired
    private IOperationLogService operationLogService;

    @Autowired
    private ISeleniumService seleniumService;

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
                    FbAccount fbAccount = fbAccountMapper.selectFbAccountById(id);
                    fbAccountMapper.updateBrowserStatus(fbAccount,"0");
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
            fbAccountMapper.updateBrowserStatus(fbAccountMapper.selectFbAccountById(id),"0");
            return true; // 移除无效进程
        });

    }


    /**
     * 查询账号
     *
     * @param keyId 账号主键
     * @return 账号
     */
    @Override
    public FbAccount selectFbAccountByKeyId(Long keyId) {
        return fbAccountMapper.selectFbAccountByKeyId(keyId);
    }

    /**
     * 批量查询账号
     * @param keyIds 账号主键
     * @return 账号
     */
    @Override
    public List<FbAccount> selectFbAccountByKeyIds(Long[] keyIds) {
        return fbAccountMapper.selectFbAccountByKeyIds(keyIds);
    }

    /**
     * 查询账号列表
     *
     * @param fbAccount 账号
     * @return 账号
     */
    @Override
    public List<FbAccount> selectFbAccountList(FbAccount fbAccount) {
        return fbAccountMapper.selectFbAccountList(fbAccount);
    }

    /**
     * 查询账号列表
     *
     * @param fbAccount 账号
     * @return 账号集合
     */
    @Override
    public List<FbAccount> selectFbAccountListNoId(FbAccount fbAccount) {
        return fbAccountMapper.selectFbAccountListNoId(fbAccount);
    }

    /**
     * 新增账号
     *
     * @param fbAccount 账号
     * @return 结果
     */
    @Override
    public int insertFbAccount(FbAccount fbAccount) {
        return fbAccountMapper.insertFbAccount(fbAccount);
    }

    /**
     * 修改账号
     *
     * @param fbAccount 账号
     * @return 结果
     */
    @Override
    public int updateFbAccount(FbAccount fbAccount) {
        return fbAccountMapper.updateFbAccount(fbAccount);
    }

    /**
     * 批量删除账号
     *
     * @param keyIds 需要删除的账号主键
     * @return 结果
     */
    @Override
    public int deleteFbAccountByKeyIds(Long[] keyIds) {
        List<FbAccount> fbAccounts = fbAccountMapper.selectFbAccountByKeyIds(keyIds);
        for (FbAccount fbAccount : fbAccounts) {
            // 指定要重命名的文件的路径
            String oldFilePath = fbAccount.getDataSource()+fbAccount.getBrowserProfile(); // 旧文件路径
            String newFilePath = fbAccount.getDataSource()+fbAccount.getBrowserProfile()+"待删除"; // 新文件路径
            // 创建 File 对象，表示旧文件
            File oldFile = new File(oldFilePath);
            // 创建 File 对象，表示新文件
            File newFile = new File(newFilePath);
            oldFile.renameTo(newFile);
        }
        return fbAccountMapper.deleteFbAccountByKeyIds(keyIds);
    }

    /**
     * 删除账号信息
     *
     * @param keyId 账号主键
     * @return 结果
     */
    @Override
    public int deleteFbAccountByKeyId(Long keyId) {
        FbAccount fbAccount = fbAccountMapper.selectFbAccountByKeyId(keyId);
        // 指定要重命名的文件的路径
        String oldFilePath = fbAccount.getDataSource()+fbAccount.getBrowserProfile(); // 旧文件路径
        String newFilePath = fbAccount.getDataSource()+fbAccount.getBrowserProfile()+"待删除"; // 新文件路径
        // 创建 File 对象，表示旧文件
        File oldFile = new File(oldFilePath);
        // 创建 File 对象，表示新文件
        File newFile = new File(newFilePath);
        oldFile.renameTo(newFile);
        return fbAccountMapper.deleteFbAccountByKeyId(keyId);
    }

    /**
     * 登录账号
     *
     * @param fbAccount
     * @param webDriver
     * @return
     */
    @Override
    public boolean login(FbAccount fbAccount, WebDriver webDriver) {

        webDriver.get("https://www.facebook.com");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30, 1);
        if (!isLogin(fbAccount,webDriver)) {
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
                seleniumService.waitingForContent(10, webDriver, "• Facebook");
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
                        webDriver.findElement(By.id("approvals_code")).sendKeys(FBAccountUtil.getGoogleVerificationCode(fbAccount.getSecretKey()));
                        webDriver.findElement(By.id("checkpointSubmitButton")).click();
                        Thread.sleep(2000);
                        webDriver.findElement(By.id("checkpointSubmitButton")).click();
                        Thread.sleep(2000);
                        webDriver.get("https://www.facebook.com");
                    }
                    Element element = document.select("input[type=text]").first();
                    if (element != null) {
                        WebElement approvalsCode = webDriver.findElement(By.xpath("//input[@type='text']"));
                        approvalsCode.sendKeys(FBAccountUtil.getGoogleVerificationCode(fbAccount.getSecretKey()));
                        WebElement submitButton = webDriver.findElement(By.xpath("(//div[@role='button'])[2]"));
                        submitButton.click();
                        if (seleniumService.waitingForContent(2, webDriver, "currentColor")) {
                            fbAccount.setNote("无法登录-秘钥错误");
                            fbAccountMapper.updateFbAccount(fbAccount);
                            return false;
                        }
                        Thread.sleep(3000);
                        webDriver.get("https://www.facebook.com");
                        if (!isLogin(fbAccount,webDriver)){
                            return false;
                        }
                    }
                    int size = document.select("[role=button]").size();
                    if (size == 1) {
                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@role='button']"))).click();
                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[type='radio'][value='1']"))).click();
                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div[3]/div/div/div[1]/div/div[2]/div/div/div/div/div/div/div[4]/div[3]/div/div/div/div/div/div/div/div/div[1]/div/span/span"))).click();
                        WebElement approvalsCode = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='text']")));
                        approvalsCode.sendKeys(FBAccountUtil.getGoogleVerificationCode(fbAccount.getSecretKey()));
                        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/div/div[1]/div[1]/div/div[2]/div[2]/div/div/div/div/div[3]/div/div[1]/div/div/div/div[1]/div/span/span"))).click();
                        for (int i = 0; i < 10; i++) {
                            if (!isLogin(fbAccount,webDriver)) {
                                Thread.sleep(1000);
                            }
                        }
                        webDriver.get("https://www.facebook.com");
                        isLogin(fbAccount,webDriver);
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

    /**
     * 判断账号是否已经登录
     *
     * @param fbAccount
     * @param webDriver
     * @return
     */
    @Override
    public boolean isLogin(FbAccount fbAccount, WebDriver webDriver) {
        // 提取重复字符串为常量
        final String STATUS_ACTIVE = "1";
        final String STATUS_LOCKED = "4";
        final String STATUS_BANNED = "5";

        // 获取页面 URL 和 Cookies
        Set<Cookie> cookies = webDriver.manage().getCookies();
        String pageSource = webDriver.getPageSource();

        // 检查是否处于检查点页面
        if (webDriver.getCurrentUrl().contains("/checkpoint/")) {
            seleniumService.waitingForContent(5, webDriver, "xlink:href=\"https://scontent-tpe1-1.xx.fbcdn.net");

            // 处理检查点对应的情况
            if (pageSource.contains("/images/checkpoint/epsilon/comet/intro.png")) {
                updateFbAccountStatus(fbAccount, STATUS_LOCKED);
                return false;
            }
            if (pageSource.contains("https://static.xx.fbcdn.net/rsrc.php/v4/yX/r/ACJE6Qz3VpL.png")) {
                updateFbAccountStatus(fbAccount, STATUS_BANNED);
                return false;
            }

            // 默认标记为锁定状态
            updateFbAccountStatus(fbAccount,  STATUS_LOCKED);
            return false;
        }

        // 检查 Cookies 是否包含特定项
        boolean hasCUser = cookies.stream().anyMatch(cookie -> "c_user".equals(cookie.getName()));
        boolean hasPresence = cookies.stream().anyMatch(cookie -> "presence".equals(cookie.getName()));

        if (hasCUser) {
            if (!STATUS_ACTIVE.equals(fbAccount.getStatus())) {
                updateFbAccountStatus(fbAccount, STATUS_ACTIVE);
            }
            return hasPresence;
        }

        return false; // 统一处理其他情况
    }

    /**
     * 添加好友
     *
     * @param fbAccount
     * @param id
     * @param webDriver
     * @return
     */
    @Override
    public void addFriend(FbAccount fbAccount, String id, WebDriver webDriver) {
        OperationLog operationLog = new OperationLog();
        WebDriverWait wait = new WebDriverWait(webDriver, 30, 1);
        webDriver.get("https://www.facebook.com/" + id);
        webDriver.manage().window().maximize();
        seleniumService.waitingForContent(10,webDriver,"https://static.xx.fbcdn.net/rsrc.php/v4/yK/r/r2FA830xjtI.png");
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@src='https://static.xx.fbcdn.net/rsrc.php/v4/yK/r/r2FA830xjtI.png']"))).click();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@src='https://static.xx.fbcdn.net/rsrc.php/v4/yo/r/Qg9sXPTnmFb.png']")));
            operationLog.setOperationAccount(fbAccount.getId());
            operationLog.setOperationContent("添加"+id+"好友");
            operationLog.setOperationStatus("成功");
            operationLog.setOperationTime(new Date());
            operationLogService.insertOperationLog(operationLog);
        }catch (Exception e){
            operationLog.setOperationAccount(fbAccount.getId());
            operationLog.setOperationContent("添加"+id+"好友");
            operationLog.setOperationStatus("失败");
            operationLog.setOperationTime(new Date());
            operationLogService.insertOperationLog(operationLog);
            e.printStackTrace();
        }
    }

    /**
     * 导入数据
     *
     * @param fbAccountList   数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importFbAccountForSell(List<FbAccount> fbAccountList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(fbAccountList) || fbAccountList.isEmpty()) {
            throw new ServiceException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (FbAccount fbAccount : fbAccountList) {
            try {
                // 验证是否存在这个用户
                FbAccount f = fbAccountMapper.selectFbAccountById(fbAccount.getId());
                if (StringUtils.isNull(f)) {
                    fbAccount.setBrowserProfile(RandomUitl.generateRandomFilename());
                    fbAccount.setUa(RandomUitl.generateRandomPCUserAgent());
                    fbAccountMapper.insertFbAccount(fbAccount);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、数据 " + fbAccount.getId() + " 导入成功");
                } else if (isUpdateSupport) {
                    fbAccountMapper.updateFbAccount(fbAccount);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、数据 " + fbAccount.getId() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、数据 " + fbAccount.getId() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、数据 " + fbAccount.getId() + " 导入失败：";
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
     * 检测账号
     *
     * @param fbAccount
     * @param webDriver
     */
    @Override
    public void checkAccount(FbAccount fbAccount, WebDriver webDriver) {

        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30, 1);
        webDriver.get("https://www.facebook.com/" + fbAccount.getId());
        String target = "https://static.xx.fbcdn.net/rsrc.php/v4/yz/r/AqoGWewwdNN.png";
        seleniumService.waitingForContent(30, webDriver, target);

        String pageSource = webDriver.getPageSource();

        //账号名字
        String regex = "\"USER_ID\":\"" + fbAccount.getId() + "\".*?\"NAME\":\"(.*?)\"";
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

            // 获取 NAME 的值
            fbAccount.setName(decodedName);
        }
        //好有数量
        Document document = Jsoup.parse(pageSource);
        Element element = document.select("h1:containsOwn(" + fbAccount.getName() + ")").first();
        element = getNthParent(element, 5);
        element.select("a[href*='sk=friends']").first();
        if (element != null) {
            // 提取文本并解析数字
            String friendCountText = element.text();
            regex = "\\D+(\\d+)\\D+"; // 匹配非数字开头，跟一个数字，再跟非数字结尾

            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(friendCountText);
            Long number = 0L;
            if (matcher.find()) {
                number = (long) Integer.parseInt(matcher.group(1)); // 提取数字部分
            }  // 获取 好友数量 部分
            fbAccount.setFriendNumber(number);
        }

        //能否广告
        webDriver.get("https://www.facebook.com/business-support-home/" + fbAccount.getId());
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()='" + fbAccount.getName() + "']")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        pageSource = webDriver.getPageSource();
        // 使用 Jsoup 解析 HTML 源代码
        document = Jsoup.parse(pageSource);
        // 查找具有特定 class 属性的 div 元素
        element = document.select("div:containsOwn(" + fbAccount.getName() + ")").first();
        element = getNthParent(element, 4);
        List<String> list = new ArrayList<>();
        list = getAllText(element, list);
        if (list.size() == 3) {
            fbAccount.setCanAds("0");
        }
        if (list.size() == 1) {
            fbAccount.setCanAds("1");
        }

        fbAccountMapper.updateFbAccount(fbAccount);
    }

    /**
     * 发贴
     *
     * @param fbAccount
     * @param webDriver
     */
    @Override
    public void post(FbAccount fbAccount, WebDriver webDriver) {
        try {
            webDriver.manage().window().maximize();
            seleniumService.simulateKeyPress(KeyEvent.VK_ESCAPE);
            int[] numbers = {3, 4, 5};
            Random random = new Random();
            int randomIndex = random.nextInt(numbers.length);
            for (int i = 0; i < numbers[randomIndex]; i++) {
                seleniumService.simulateKeyPress(KeyEvent.VK_PAGE_DOWN);
                seleniumService.threadSleep(1000);
            }
            seleniumService.simulateKeyPress(KeyEvent.VK_HOME);
            seleniumService.threadSleep(1000);
            WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30, 1);
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@src='https://static.xx.fbcdn.net/rsrc.php/v4/y7/r/Ivw7nhRtXyo.png']"))).click();
            seleniumService.threadSleep(1000);
            String pageSource = webDriver.getPageSource();
            //如果仅朋友可见
            String xpath = "";
            if (pageSource.contains("https://static.xx.fbcdn.net/rsrc.php/v4/yJ/r/zPcex_q0TM1.png")){
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@src='https://static.xx.fbcdn.net/rsrc.php/v4/yJ/r/zPcex_q0TM1.png']"))).click();
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@src='https://static.xx.fbcdn.net/rsrc.php/v4/yD/r/KV7QFf-Yspp.png']"))).click();
                for (int i = 0; i < 3; i++) {
                    seleniumService.simulateKeyPress(KeyEvent.VK_TAB);
                    seleniumService.threadSleep(300);
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
            seleniumService.clickAtCoordinates(webDriver,location.x, location.y+70);
            seleniumService.threadSleep(2000);
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
            seleniumService.simulateKeyPress(KeyEvent.VK_F5);
            random = new Random();
            randomIndex = random.nextInt(numbers.length);
            for (int i = 0; i < numbers[randomIndex]; i++) {
                seleniumService.simulateKeyPress(KeyEvent.VK_PAGE_DOWN);
                seleniumService.threadSleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 打开浏览器
     * @param fbAccount
     * @return
     */
    @Override
    public WebDriver openBrowser(FbAccount fbAccount) {
        if (!processMap.containsKey(fbAccount.getId())){
            List<Integer> beforeListWindows = seleniumService.getListWindows();
            WebDriver webDriver = seleniumService.openBrowser(BrowserConfig.getFbAccountBrowserConfig(fbAccount));
            fbAccountMapper.updateBrowserStatus(fbAccount,"1");
            List<Integer> afterListWindows = seleniumService.getListWindows();
            Integer extraProcessId = seleniumService.findExtraProcessId(beforeListWindows, afterListWindows);
            webDriverMap.put(fbAccount.getId(),webDriver);
            processMap.put(fbAccount.getId(),extraProcessId);
            return webDriver;
        }
        if (processMap.containsKey(fbAccount.getId())){
            showBrowser(fbAccount);
            return null;
        }
        return null;
    }

    /**
     * 关闭浏览器
     * @param fbAccount
     */
    @Override
    public void closeBrowser(FbAccount fbAccount) {
        fbAccountMapper.updateBrowserStatus(fbAccount,"0");
        if (processMap.containsKey(fbAccount.getId())){
            processMap.remove(fbAccount.getId());
        }
        if (webDriverMap.containsKey(fbAccount.getId())){
            webDriverMap.get(fbAccount.getId()).quit();
            webDriverMap.remove(fbAccount.getId());
        }
    }

    /**
     * 显示浏览器
     *
     * @param fbAccount
     */
    @Override
    public void showBrowser(FbAccount fbAccount) {
        seleniumService.showBrowser(processMap.get(fbAccount.getId()));
    }

    /**
     * 关闭全部浏览器
     */
    @Override
    public void closeAllBrowser() {
        Iterator<Map.Entry<String, WebDriver>> iterator = webDriverMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, WebDriver> entry = iterator.next();
            String key = entry.getKey();
            WebDriver value = entry.getValue();

            try {
                value.quit(); // 关闭浏览器
                fbAccountMapper.updateBrowserStatus(fbAccountMapper.selectFbAccountById(key), "0");
            } catch (Exception e) {
                e.printStackTrace(); // 打印异常日志
            }

           iterator.remove(); // 安全移除元素
        }
    }

    /**
     * 更新 FbAccount 状态的工具方法
     */
    private void updateFbAccountStatus(FbAccount fbAccount, String status) {
        if (status != null) {
            fbAccount.setStatus(status);
        }
        fbAccountMapper.updateFbAccount(fbAccount);
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

}

