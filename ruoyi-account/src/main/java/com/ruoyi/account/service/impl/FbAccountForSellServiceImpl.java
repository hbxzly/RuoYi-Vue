package com.ruoyi.account.service.impl;


import java.awt.event.KeyEvent;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ruoyi.account.domain.FbAccount;
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
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.ruoyi.account.mapper.FbAccountForSellMapper;
import com.ruoyi.account.domain.FbAccountForSell;
import com.ruoyi.account.service.IFbAccountForSellService;



/**
 * 卖号Service业务层处理
 *
 * @author ruoyi
 * @date 2024-11-01
 */
@Component
@Service
public class FbAccountForSellServiceImpl implements IFbAccountForSellService {

    @Autowired
    private FbAccountForSellMapper fbAccountForSellMapper;

    @Autowired
    private ISeleniumService seleniumService;

    @Autowired
    private IPostsService postsService;

    @Autowired
    private IOperationLogService operationLogService;


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
     * 查询卖号
     *
     * @param keyId 卖号主键
     * @return 卖号
     */
    @Override
    public FbAccountForSell selectFbAccountForSellByKeyId(Long keyId) {
        return fbAccountForSellMapper.selectFbAccountForSellByKeyId(keyId);
    }

    /**
     * 查询卖号列表
     *
     * @param fbAccountForSell 卖号
     * @return 卖号
     */
    @Override
    public List<FbAccountForSell> selectFbAccountForSellList(FbAccountForSell fbAccountForSell) {
        return fbAccountForSellMapper.selectFbAccountForSellList(fbAccountForSell);
    }

    /**
     * 查询卖号列表
     *
     * @param fbAccountForSell 卖号
     * @return 卖号集合
     */
    @Override
    public List<FbAccountForSell> selectFbAccountForSellListNoId(FbAccountForSell fbAccountForSell) {
        return fbAccountForSellMapper.selectFbAccountForSellListNoId(fbAccountForSell);
    }

    /**
     * 新增卖号
     *
     * @param fbAccountForSell 卖号
     * @return 结果
     */
    @Override
    public int insertFbAccountForSell(FbAccountForSell fbAccountForSell) {
        return fbAccountForSellMapper.insertFbAccountForSell(fbAccountForSell);
    }

    /**
     * 修改卖号
     *
     * @param fbAccountForSell 卖号
     * @return 结果
     */
    @Override
    public int updateFbAccountForSell(FbAccountForSell fbAccountForSell) {
        return fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
    }

    /**
     * 批量删除卖号
     *
     * @param keyIds 需要删除的卖号主键
     * @return 结果
     */
    @Override
    public int deleteFbAccountForSellByKeyIds(Long[] keyIds) {
        return fbAccountForSellMapper.deleteFbAccountForSellByKeyIds(keyIds);
    }

    /**
     * 删除卖号信息
     *
     * @param keyId 卖号主键
     * @return 结果
     */
    @Override
    public int deleteFbAccountForSellByKeyId(Long keyId) {
        return fbAccountForSellMapper.deleteFbAccountForSellByKeyId(keyId);
    }

    /**
     * 导入数据
     *
     * @param FbAccountForSellList 数据列表
     * @param isUpdateSupport      是否更新支持，如果已存在，则进行更新数据
     * @param operName             操作用户
     * @return 结果
     */
    @Override
    public String importFbAccountForSell(List<FbAccountForSell> FbAccountForSellList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(FbAccountForSellList) || FbAccountForSellList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (FbAccountForSell fbAccountForSell : FbAccountForSellList) {
            try {
                // 验证是否存在这个用户
                FbAccountForSell f = fbAccountForSellMapper.selectFbAccountForSellById(fbAccountForSell.getId());
                if (StringUtils.isNull(f)) {
                    fbAccountForSell.setBrowserProfile(RandomUitl.generateRandomFilename());
                    fbAccountForSell.setUa(RandomUitl.generateRandomPCUserAgent());
                    fbAccountForSellMapper.insertFbAccountForSell(fbAccountForSell);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、数据 " + fbAccountForSell.getId() + " 导入成功");
                } else if (isUpdateSupport) {
                    fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、数据 " + fbAccountForSell.getId() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、数据 " + fbAccountForSell.getId() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、数据 " + fbAccountForSell.getId() + " 导入失败：";
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
     * 登录账号
     *
     * @param webDriver
     * @param fbAccountForSell
     */
    @Override
    public String loginFbAccountForSell(WebDriver webDriver, FbAccountForSell fbAccountForSell) {
        try {
            webDriver.get("https://www.facebook.com");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 清除所有 cookies
            webDriver.manage().window().maximize();
//        webDriver.manage().deleteAllCookies();
//        webDriver.navigate().refresh();
            WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30, 1);
            try {
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("login")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                WebElement email = webDriver.findElement(By.name("email"));
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
                    for (char c : fbAccountForSell.getEmail().toCharArray()) {
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
                    for (char c : fbAccountForSell.getPassword().toCharArray()) {
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
                seleniumService.threadSleep(1000);
                seleniumService.waitingForContent(10, webDriver, "• Facebook");
                String pageSource = webDriver.getPageSource();
                Document document = Jsoup.parse(pageSource);
                if (pageSource.contains("输入你看到的验证码")) {
                    fbAccountForSell.setNote("无法登录-需要输入验证码");
                    fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
                    return "";
                }
                if (pageSource.contains("WhatsApp")) {
                    fbAccountForSell.setNote("无法登录-需要WhatsApp验证码");
                    fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
                    return "";
                }
                if (pageSource.contains("账号或密码无效")) {
                    fbAccountForSell.setNote("无法登录-账号或密码无效");
                    fbAccountForSell.setCanLogin("0");
                    fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
                    return "";
                }
                if (pageSource.contains("你输入的是旧密码")) {
                    fbAccountForSell.setNote("无法登录-密码无效");
                    fbAccountForSell.setCanLogin("0");
                    fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
                    return "";
                }

                //新版双重验证码输入
                if (document.select("#approvals_code").first() != null) {
                    webDriver.findElement(By.id("approvals_code")).sendKeys(FBAccountUtil.getGoogleVerificationCode(fbAccountForSell.getSecretKey()));
                    webDriver.findElement(By.id("checkpointSubmitButton")).click();
                    Thread.sleep(2000);
                    webDriver.findElement(By.id("checkpointSubmitButton")).click();
                    Thread.sleep(2000);
                    webDriver.get("https://www.facebook.com");
                }
                Element element = document.select("input[type=text]").first();
                if (element != null) {
                    WebElement approvalsCode = webDriver.findElement(By.xpath("//input[@type='text']"));
                    approvalsCode.sendKeys(FBAccountUtil.getGoogleVerificationCode(fbAccountForSell.getSecretKey()));
                    approvalsCode.sendKeys(Keys.ENTER);
                    if (seleniumService.waitingForContent(2, webDriver, "currentColor")) {
                        fbAccountForSell.setNote("无法登录-秘钥错误");
                        fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
                        return "";
                    }
                    Thread.sleep(3000);
                    webDriver.get("https://www.facebook.com");
                }
                int size = document.select("[role=button]").size();
                if (size == 1) {
                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@role='button']"))).click();
                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[type='radio'][value='1']"))).click();
                    List<WebElement> elements = webDriver.findElements(By.xpath("//div[@data-visualcompletion='ignore']"));
                    elements.get(elements.size() - 1).findElement(By.xpath("..")).click();
                    WebElement approvalsCode = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='text']")));
                    approvalsCode.sendKeys(FBAccountUtil.getGoogleVerificationCode(fbAccountForSell.getSecretKey()));
                    approvalsCode.sendKeys(Keys.ENTER);
                    if (seleniumService.waitingForContent(2, webDriver, "currentColor")) {
                        fbAccountForSell.setNote("无法登录-秘钥错误");
                        fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
                        return "";
                    }
                    for (int i = 0; i < 10; i++) {
                        if (!isLogin(webDriver, fbAccountForSell)){
                            Thread.sleep(1000);
                        }
                    }
                    webDriver.get("https://www.facebook.com");
                }

                return "";


            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (RuntimeException e) {
            e.printStackTrace(

            );
        }
        return "";
    }

    /**
     * 获取信息（简体）
     *
     * @param webDriver
     * @param fbAccountForSell
     */
    @Override
    public void getAccountMarketplaceAndNameAndFriendInSimplified(WebDriver webDriver, FbAccountForSell fbAccountForSell) {

        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30, 1);
        webDriver.get("https://facebook.com/marketplace/?ref=bookmark");
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/notifications/']")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (webDriver.getCurrentUrl().contains("ineligible")) {
            fbAccountForSell.setCanLogin("1");
            fbAccountForSell.setIsMarketplace("0");
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        } else {
            fbAccountForSell.setCanLogin("1");
            fbAccountForSell.setIsMarketplace("1");
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }
        webDriver.get("https://www.facebook.com/" + fbAccountForSell.getId());
        String target = "https://static.xx.fbcdn.net/rsrc.php/v4/yz/r/AqoGWewwdNN.png";
        seleniumService.waitingForContent(30, webDriver, target);

        String pageSource = webDriver.getPageSource();

        //账号名字
        String regex = "\"USER_ID\":\"" + fbAccountForSell.getId() + "\".*?\"NAME\":\"(.*?)\"";
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
            fbAccountForSell.setName(decodedName);
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }

        //好有数量
        Document document = Jsoup.parse(pageSource);
        Element element = document.select("h1:containsOwn(" + fbAccountForSell.getName() + ")").first();
        element = getNthParent(element, 5);
        element.select("a[href*='sk=friends']").first();
        if (element != null) {
            // 提取文本并解析数字
            String friendCountText = element.text();
            regex = "\\D+(\\d+)\\D+"; // 匹配非数字开头，跟一个数字，再跟非数字结尾

            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(friendCountText);
            String number = "0";
            if (matcher.find()) {
                number = matcher.group(1); // 提取数字部分
            }  // 获取 好友数量 部分
            fbAccountForSell.setFriendNumber(number);
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }

        //帖子数量
        try {
            webDriver.get("https://www.facebook.com/" + fbAccountForSell.getId() + "/allactivity?activity_history=false&category_key=YOURACTIVITYPOSTSSCHEMA&manage_mode=false&should_load_landing_page=false");
            try {
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(text(),'" + fbAccountForSell.getName() + "')]")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            pageSource = webDriver.getPageSource();
            document = Jsoup.parse(pageSource);
            element = document.select("a:containsOwn(" + fbAccountForSell.getName() + ")").first();
            String lastsPostsTime = getNthParent(element, 18).children().first().text();
            fbAccountForSell.setLastPostsTime(lastsPostsTime);
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);

            int countPosts = document.select("a:containsOwn(" + fbAccountForSell.getName() + ")").size();

            if (countPosts > 0) {
                fbAccountForSell.setPostsNumber(String.valueOf(countPosts));
                fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
            } else {
                fbAccountForSell.setPostsNumber("0");
                fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
            }
        } catch (Exception e) {
            fbAccountForSell.setPostsNumber("0");
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
            e.printStackTrace();
        }

        //BM数量
        try {
            webDriver.get("https://www.facebook.com/business-support-home/?landing_page=overview&source=link");
            try {
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()='" + fbAccountForSell.getName() + "']")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String currentUrl = webDriver.getCurrentUrl();
            if (!currentUrl.contains(fbAccountForSell.getId())) {
                pageSource = webDriver.getPageSource();
                // 使用 Jsoup 解析 HTML 源代码
                document = Jsoup.parse(pageSource);
                element = document.select("div:containsOwn(" + fbAccountForSell.getName() + ")").first();
                element = getNthParent(element, 12);
                Elements elements = element.siblingElements();
                String html = elements.get(0).html();
                pattern = Pattern.compile("business-support-home");
                matcher = pattern.matcher(html);
                int countBM = 0;
                while (matcher.find()) {
                    countBM++;
                }
                fbAccountForSell.setBmNumber(String.valueOf(countBM));
                fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
            } else {
                fbAccountForSell.setBmNumber("0");
                fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
            }

            //能否广告
            webDriver.get("https://www.facebook.com/business-support-home/" + fbAccountForSell.getId());
            try {
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()='" + fbAccountForSell.getName() + "']")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            pageSource = webDriver.getPageSource();
            // 使用 Jsoup 解析 HTML 源代码
            document = Jsoup.parse(pageSource);
            // 查找具有特定 class 属性的 div 元素
            element = document.select("div:containsOwn(" + fbAccountForSell.getName() + ")").first();
            element = getNthParent(element, 4);
            List<String> list = new ArrayList<>();
            list = getAllText(element, list);
            if (list.size() == 3) {
                fbAccountForSell.setCanAds("0");
                fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
            }
            if (list.size() == 1) {
                fbAccountForSell.setCanAds("1");
                fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //主页数量
        webDriver.get("https://www.facebook.com/pages/?category=your_pages&ref=bookmarks");
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/pages/?category=invites&ref=bookmarks']")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        pageSource = webDriver.getPageSource();
        pattern = Pattern.compile("/latest/inbox/all");
        matcher = pattern.matcher(pageSource);
        int countPage = 0;
        while (matcher.find()) {
            countPage++;
        }
        if (countPage > 0) {
            fbAccountForSell.setPageNumber(String.valueOf(countPage));
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        } else {
            fbAccountForSell.setPageNumber("0");
            fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
        }
    }


    /**
     * 获取账号语言
     *
     * @param webDriver
     * @param fbAccountForSell
     * @return
     */
    @Override
    public String getAccountLanguage(WebDriver webDriver, FbAccountForSell fbAccountForSell) {
        webDriver.get("https://www.facebook.com/settings/?tab=language_and_region");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String pageSource = webDriver.getPageSource();
        Document document = Jsoup.parse(pageSource);
        String text = document.select("div[role=main]").first().text();
        //English  中文(台灣)  中文(香港)   中文(简体)
        if (text.contains("English")) {
            return "English";
        } else if (text.contains("中文(台灣)")) {
            return "中文(台灣)";
        } else if (text.contains("中文(香港)")) {
            return "中文(香港)";
        } else if (text.contains("中文(简体)")) {
            return "中文(简体)";
        } else {
            return "未知语言";
        }
    }

    /**
     * 查询账号
     *
     * @param ids
     * @return
     */
    @Override
    public List<FbAccountForSell> selectFbAccountForSellListByAccountIds(Long[] ids) {
        return fbAccountForSellMapper.selectFbAccountForSellListByAccountIds(ids);
    }

    /**
     * 判断是否在线
     *
     * @param webDriver
     * @return
     */
    @Override
    public boolean isLogin(WebDriver webDriver, FbAccountForSell fbAccountForSell) {
        // 提取重复字符串为常量
        final String STATUS_ACTIVE = "1";
        final String STATUS_DISABLED = "0";

        // 获取页面 URL 和 Cookies
        for (int i = 0; i < 30; i++) {
            Set<Cookie> cookies = webDriver.manage().getCookies();
            System.out.println("第"+i);
            if (cookies.size()>0) {
                break;
            }
            seleniumService.threadSleep(1000);
        }
        Set<Cookie> cookies = webDriver.manage().getCookies();
        System.out.println("输出cookie--------------------------------------------");
        for (Cookie cookie : cookies) {
            System.out.println("name: " + cookie.getName() + ", value: " + cookie.getValue());
        }
        System.out.println("输出cookie--------------------------------------------");
        String pageSource = webDriver.getPageSource();

        // 检查是否处于检查点页面
        if (webDriver.getCurrentUrl().contains("/checkpoint/")) {
            seleniumService.waitingForContent(5, webDriver, "xlink:href=\"https://scontent-tpe1-1.xx.fbcdn.net");

            // 处理检查点对应的情况
            if (pageSource.contains("/images/checkpoint/epsilon/comet/intro.png")) {
                updateFbAccountStatus(fbAccountForSell, "", STATUS_DISABLED);
                return false;
            }
            if (pageSource.contains("https://static.xx.fbcdn.net/rsrc.php/v4/yX/r/ACJE6Qz3VpL.png")) {
                updateFbAccountStatus(fbAccountForSell, "", STATUS_DISABLED);
                return false;
            }

            // 默认标记为锁定状态
            updateFbAccountStatus(fbAccountForSell, "", STATUS_DISABLED);
            return false;
        }
        // 检查 Cookies 是否包含特定项
        boolean hasCUser = cookies.stream().anyMatch(cookie -> "c_user".equals(cookie.getName()));
        boolean hasPresence = cookies.stream().anyMatch(cookie -> "presence".equals(cookie.getName()));

        if (hasCUser) {
            if (!STATUS_ACTIVE.equals(fbAccountForSell.getCanLogin())) {
                updateFbAccountStatus(fbAccountForSell, null, STATUS_ACTIVE);
            }
            return hasPresence;
        }

        return false; // 统一处理其他情况
    }

    /**
     * 发贴
     *
     * @param fbAccountForSell
     * @param webDriver
     */
    @Override
    public void post(FbAccountForSell fbAccountForSell, WebDriver webDriver) {

        OperationLog operationLog = new OperationLog();

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
            if (pageSource.contains("https://static.xx.fbcdn.net/rsrc.php/v4/yJ/r/zPcex_q0TM1.png")) {
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
            seleniumService.clickAtCoordinates(webDriver, location.x, location.y + 70);
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
            seleniumService.threadSleep(2000);
            pageSource = webDriver.getPageSource();
            if (pageSource.contains(postsList.get(randomIndex).getContent())){
                operationLog.setOperationAccount(fbAccountForSell.getId());
                operationLog.setOperationContent("卖号发帖");
                operationLog.setOperationStatus("成功");
                operationLog.setOperationTime(new Date());
                operationLogService.insertOperationLog(operationLog);
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
     * 添加好友
     *
     * @param fbAccountForSell
     * @param id
     * @param webDriver
     */
    @Override
    public void addFriend(FbAccountForSell fbAccountForSell, String id, WebDriver webDriver) {
        OperationLog operationLog = new OperationLog();
        WebDriverWait wait = new WebDriverWait(webDriver, 30, 1);
        webDriver.get("https://www.facebook.com/" + id);
        try {
            webDriver.manage().window().maximize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        seleniumService.waitingForContent(10,webDriver,"https://static.xx.fbcdn.net/rsrc.php/v4/yK/r/r2FA830xjtI.png");
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@src='https://static.xx.fbcdn.net/rsrc.php/v4/yK/r/r2FA830xjtI.png']"))).click();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@src='https://static.xx.fbcdn.net/rsrc.php/v4/yo/r/Qg9sXPTnmFb.png']")));
            operationLog.setOperationAccount(fbAccountForSell.getId());
            operationLog.setOperationContent("卖号添加"+id+"好友");
            operationLog.setOperationStatus("成功");
            operationLog.setOperationTime(new Date());
            operationLogService.insertOperationLog(operationLog);
        }catch (Exception e){
            operationLog.setOperationAccount(fbAccountForSell.getId());
            operationLog.setOperationContent("卖号添加"+id+"好友");
            operationLog.setOperationStatus("失败");
            operationLog.setOperationTime(new Date());
            operationLogService.insertOperationLog(operationLog);
            e.printStackTrace();
        }
    }

    /**
     * 打开浏览器
     *
     * @param fbAccountForSell
     * @return
     */
    @Override
    public WebDriver openBrowser(FbAccountForSell fbAccountForSell) {
        if (!processMap.containsKey(fbAccountForSell.getId())){
            List<Integer> beforeListWindows = seleniumService.getListWindows();
            WebDriver webDriver = seleniumService.openBrowser(BrowserConfig.getFbAccountForSellBrowserConfig(fbAccountForSell));
            fbAccountForSellMapper.updateFbAccountForSellBrowserStatus(fbAccountForSell,"1");
            List<Integer> afterListWindows = seleniumService.getListWindows();
            Integer extraProcessId = seleniumService.findExtraProcessId(beforeListWindows, afterListWindows);
            webDriverMap.put(fbAccountForSell.getId(),webDriver);
            processMap.put(fbAccountForSell.getId(),extraProcessId);
            return webDriver;
        }
        if (processMap.containsKey(fbAccountForSell.getId())){
            showBrowser(fbAccountForSell);
            return null;
        }
        return null;
    }

    /**
     * 显示浏览器
     *
     * @param fbAccountForSell
     */
    @Override
    public void showBrowser(FbAccountForSell fbAccountForSell) {
        seleniumService.showBrowser(processMap.get(fbAccountForSell.getId()));
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

    /**
     * 更新 FbAccount 状态的工具方法
     */
    private void updateFbAccountStatus(FbAccountForSell fbAccountForSell, String note, String status) {
        if (note != null) {
            fbAccountForSell.setNote(note);
        }
        if (status != null) {
            fbAccountForSell.setCanLogin(status);
        }
        fbAccountForSellMapper.updateFbAccountForSell(fbAccountForSell);
    }
}



