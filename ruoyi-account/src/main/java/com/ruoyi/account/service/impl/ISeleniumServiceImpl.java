package com.ruoyi.account.service.impl;

import com.ruoyi.account.domain.*;
import com.ruoyi.account.mapper.FbAccountMapper;
import com.ruoyi.account.service.ISeleniumService;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.ptr.IntByReference;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.*;


@Service
public class ISeleniumServiceImpl implements ISeleniumService {

    @Autowired
    private FbAccountMapper fbAccountMapper;

    //webDriver集合
    public static Map<String, WebDriver> webDriverMap;

    //进程集合
    public static Map<String, Integer> processMap;


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
                option.addArguments("--disable-notifications");//限制通知
                option.addArguments("--user-agent=" + fbAccount.getUa());
                option.setExperimentalOption("useAutomationExtension", false);
                option.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
                List<Integer> beforeProcessIdList = getListWindows();
                WebDriver webDriver = new ChromeDriver(option);
                List<Integer> afterProcessIdList = getListWindows();
                changeBrowserStatus(fbAccount, "1");
                processMap.put(fbAccount.getId(), findExtraProcessId(beforeProcessIdList, afterProcessIdList));
                webDriverMap.put(fbAccount.getId(), webDriver);
                return webDriver;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            showBrowser(processMap.get(fbAccount.getId()));
            return null;
        }

    }

    /**
     * 打开打单个浏览器
     *
     * @param configMap
     * @return
     */
    @Override
    public WebDriver openBrowser(Map<String, String> configMap) {
        try {
            //参数配置
            System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
            ChromeOptions option = new ChromeOptions();
            option.addArguments("--remote-allow-origins=*");
            option.addArguments("--disable-notifications");//限制通知
            option.setExperimentalOption("useAutomationExtension", false);
            option.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
            if (configMap.containsKey("userDataDir")) {
                option.addArguments("--user-data-dir=" + configMap.get("userDataDir"));
            }
            if (configMap.containsKey("userAgent")) {
                option.addArguments("--user-agent=" + configMap.get("userAgent"));
            }
            WebDriver webDriver = new ChromeDriver(option);
            return webDriver;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 打开打单个浏览器
     *
     * @param fbAccountForSell
     * @return
     */
    @Override
    public WebDriver openBrowserForAccountSellForTest(FbAccountForSell fbAccountForSell) {
        try {
            //参数配置
            System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
            ChromeOptions option = new ChromeOptions();
//            option.addArguments("--user-data-dir=" + fbAccountForSell.getFilePath() + fbAccountForSell.getBrowserProfile());
            option.addArguments("--remote-allow-origins=*");
            option.addArguments("--disable-notifications");//限制通知
            option.addArguments("--user-agent=" + fbAccountForSell.getUa());
            option.setExperimentalOption("useAutomationExtension", false);
            option.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
            WebDriver webDriver = new ChromeDriver(option);
            return webDriver;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 打开打单个浏览器
     *
     * @param fbAccountForSell
     * @return
     */
    @Override
    public WebDriver openBrowserForAccountSell(FbAccountForSell fbAccountForSell) {
        try {
            //参数配置
            System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
            ChromeOptions option = new ChromeOptions();
            option.addArguments("--user-data-dir=" + fbAccountForSell.getFilePath() + fbAccountForSell.getBrowserProfile());
            option.addArguments("--remote-allow-origins=*");
            option.addArguments("--disable-notifications");//限制通知
            option.addArguments("--user-agent=" + fbAccountForSell.getUa());
            option.setExperimentalOption("useAutomationExtension", false);
            option.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
            WebDriver webDriver = new ChromeDriver(option);
            return webDriver;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
    public void showBrowser(Integer pId) {
        User32 user32 = User32.INSTANCE;
        user32.EnumWindows((hWnd, arg1) -> {
            IntByReference processId = new IntByReference();
            user32.GetWindowThreadProcessId(hWnd, processId);
            int windowProcessId = processId.getValue();
            if (user32.IsWindowVisible(hWnd) && windowProcessId == pId) {
                user32.ShowWindow(hWnd, User32.SW_MINIMIZE);
                user32.ShowWindow(hWnd, User32.SW_RESTORE);
                user32.SetForegroundWindow(hWnd);
            }
            return true;
        }, null);
    }

    /**
     * 模拟按键(单键）
     * @param keyName
     */
    @Override
    public void simulateKeyPress(int keyName) {
        try {
            Robot robot = new Robot();
            // 模拟按下和释放键
            robot.keyPress(keyName);
            robot.keyRelease(keyName);
        } catch ( AWTException e) {
            System.out.println("Invalid key name: " + keyName);
            e.printStackTrace();
        }
    }


    /**
     * 模拟按键(单键）
     * @param keyName1,keyName2
     */
    @Override
    public void simulateKeyPress(int keyName1, int keyName2) {
        try {
            Robot robot = new Robot();
            // 模拟按下和释放键
            robot.keyPress(keyName1);
            robot.keyPress(keyName2);
            robot.keyRelease(keyName1);
            robot.keyRelease(keyName2);
        } catch ( AWTException e) {
            System.out.println("模拟按键失败");
            e.printStackTrace();
        }
    }


    /**
     * 获取任务栏窗口进程ID
     * @return
     */
    @Override
    public List<Integer> getListWindows() {
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
     * 检查进程ID是否活跃
     *
     * @param processId
     * @return
     */
    @Override
    public boolean isProcessAlive(Integer processId) {
        try {
            // 根据操作系统执行检测，例如：
            // - Windows: 使用 `tasklist` 或 `wmic`
            // - Linux: 使用 `ps` 或直接检查 `/proc`
            Process process = Runtime.getRuntime().exec("tasklist /FI \"PID eq " + processId + "\"");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains(String.valueOf(processId))) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            return false; // 如果检测失败，认为进程已结束
        }
    }


    /**
     * 返回新开浏览器的进程ID
     * @param beforeProcessIdList 打开之前的进程
     * @param afterProcessIdList  打开之后的进程
     * @return
     */
    @Override
    public Integer findExtraProcessId(List<Integer> beforeProcessIdList, List<Integer> afterProcessIdList) {

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

    /**
     * 等待内容
     * @param time
     * @param webDriver
     * @param content
     * @return
     */
    @Override
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

    /**
     * 为邮箱打开浏览器
     * @param email
     * @return
     */
    @Override
    public WebDriver openBrowserForEmail(Email email) {
        try {
            //参数配置
            System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
            ChromeOptions option = new ChromeOptions();
//            option.addArguments("--user-data-dir=" + fbAccountForSell.getFilePath() + fbAccountForSell.getBrowserProfile());
            option.addArguments("--remote-allow-origins=*");
            option.addArguments("--disable-notifications");//限制通知
            option.setExperimentalOption("useAutomationExtension", false);
            option.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
            WebDriver webDriver = new ChromeDriver(option);
            return webDriver;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 模拟鼠标点击指定坐标位置
     * @param driver
     * @param x
     * @param y
     */
    @Override
    public void clickAtCoordinates(WebDriver driver, int x, int y) {
        // 使用Actions类模拟鼠标点击
        Actions actions = new Actions(driver);
        // 移动鼠标到指定坐标，并点击
        actions.moveByOffset(x, y).click().perform();
    }

    /**
     * 等待时间
     *
     * @param seconds
     */
    @Override
    public void threadSleep(int seconds) {
        try {
            Thread.sleep(seconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过页面代码获取xpath
     * @param pageSource
     * @param sourceCode
     * @return
     */
    @Override
    public String getXpathBySourceCode(String pageSource, String sourceCode){
        // 使用JSoup解析HTML
        Document doc = Jsoup.parse(pageSource);

        // 查找文档中的所有元素
        Elements elements = doc.getAllElements();

        // 使用自定义过滤器匹配完整的HTML标签
        Element matchedElement = null;
        for (Element element : elements) {
            // 获取元素的外部HTML表示
            String outerHtml = element.outerHtml();
            if (outerHtml.equals(sourceCode)) {
                matchedElement = element;
                break;
            }
        }

        // 如果找到了匹配的元素
        if (matchedElement != null) {
            // 构造XPath路径
            String xpath = getXPath(matchedElement);
            return xpath.replace("/#root[1]","");
        } else {
            System.out.println("未找到匹配的元素");
            return null;
        }
    }

    // 生成元素的XPath路径
    private static String getXPath(Element element) {
        StringBuilder xpath = new StringBuilder();
        while (element != null) {
            int index = getElementIndex(element);
            String tagName = element.tagName();
            xpath.insert(0, "/" + tagName + "[" + index + "]");
            element = element.parent();
        }
        return xpath.toString();
    }

    // 获取元素在兄弟元素中的索引位置
    private static int getElementIndex(Element element) {
        // 如果元素没有父元素，说明它是根元素，索引为1
        if (element.parent() == null) {
            return 1;
        }
        Elements siblings = element.parent().children();
        int index = 1;
        for (Element sibling : siblings) {
            if (sibling.tagName().equals(element.tagName())) {
                if (sibling.equals(element)) {
                    return index;
                }
                index++;
            }
        }
        return index;
    }

}
