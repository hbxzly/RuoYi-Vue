package com.ruoyi.web.controller.account;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ruoyi.account.domain.*;
import com.ruoyi.account.mapper.FbAccountForSellMapper;
import com.ruoyi.account.mapper.FbAccountMapper;
import com.ruoyi.account.service.*;
import com.ruoyi.account.util.FBAccountUtil;
import org.openqa.selenium.WebDriver;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 卖号Controller
 * 
 * @author ruoyi
 * @date 2024-11-01
 */
@RestController
@RequestMapping("/account/sell")
public class FbAccountForSellController extends BaseController {

    @Autowired
    private IFbAccountForSellService fbAccountForSellService;

    @Autowired
    private ISeleniumService seleniumService;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private IProxyIpService proxyIpService;

    @Autowired
    FbAccountForSellMapper fbAccountForSellMapper;

    HashMap<String,WebDriver> webDriverMap = new HashMap<>();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void init() {
        scheduler.scheduleAtFixedRate(this::cleanUpWebDrivers, 0, 1, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void destroy() {
        scheduler.shutdownNow();
    }

    private void cleanUpWebDrivers() {
        webDriverMap.entrySet().removeIf(entry -> {
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
    }

    /**
     * 查询卖号列表
     */
    @PreAuthorize("@ss.hasPermi('account:sell:list')")
    @GetMapping("/list")
    public TableDataInfo list(FbAccountForSell fbAccountForSell) {
        startPage();
        List<FbAccountForSell> list = fbAccountForSellService.selectFbAccountForSellList(fbAccountForSell);
        return getDataTable(list);
    }

    @PostMapping("/batchSearch")
    public TableDataInfo batchSearch(@RequestBody FbAccountForSell account) {
        startPage(); // 若依分页
        List<FbAccountForSell> list = fbAccountForSellService.batchSearch(account);
        return getDataTable(list);
    }

    /**
     * 导出卖号列表
     */
    @PreAuthorize("@ss.hasPermi('account:sell:export')")
    @Log(title = "卖号", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FbAccountForSell fbAccountForSell)
    {
        List<FbAccountForSell> list = fbAccountForSellService.selectFbAccountForSellList(fbAccountForSell);
        ExcelUtil<FbAccountForSell> util = new ExcelUtil<FbAccountForSell>(FbAccountForSell.class);
        util.exportExcel(response, list, "卖号数据");
    }

    /**
     * 获取卖号详细信息
     */
    @PreAuthorize("@ss.hasPermi('account:sell:query')")
    @GetMapping(value = "/{keyId}")
    public AjaxResult getInfo(@PathVariable("keyId") Long keyId)
    {
        return success(fbAccountForSellService.selectFbAccountForSellByKeyId(keyId));
    }

    /**
     * 新增卖号
     */
    @PreAuthorize("@ss.hasPermi('account:sell:add')")
    @Log(title = "卖号", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FbAccountForSell fbAccountForSell)
    {
        return toAjax(fbAccountForSellService.insertFbAccountForSell(fbAccountForSell));
    }

    /**
     * 修改卖号
     */
    @PreAuthorize("@ss.hasPermi('account:sell:edit')")
    @Log(title = "卖号", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FbAccountForSell fbAccountForSell)
    {
        return toAjax(fbAccountForSellService.updateFbAccountForSell(fbAccountForSell));
    }

    /**
     * 删除卖号
     */
    @PreAuthorize("@ss.hasPermi('account:sell:remove')")
    @Log(title = "卖号", businessType = BusinessType.DELETE)
	@DeleteMapping("/{keyIds}")
    public AjaxResult remove(@PathVariable Long[] keyIds)
    {
        return toAjax(fbAccountForSellService.deleteFbAccountForSellByKeyIds(keyIds));
    }

    /**
     * 导入数据
     * @param file
     * @param updateSupport
     * @return
     * @throws Exception
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<FbAccountForSell> util = new ExcelUtil<FbAccountForSell>(FbAccountForSell.class);
        List<FbAccountForSell> fbAccountForSells = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = fbAccountForSellService.importFbAccountForSell(fbAccountForSells, updateSupport, operName);
        return success(message);
    }

    /**
     * 导入模板
     * @param response
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<FbAccountForSell> util = new ExcelUtil<FbAccountForSell>(FbAccountForSell.class);
        util.importTemplateExcel(response, "帖子数据");
    }


    /**
     * 检测
     */
    @GetMapping("/checkAccount/{ids}")
    @ResponseBody
    public AjaxResult checkAccount(@PathVariable Long[] ids) {
        List<FbAccountForSell> fbAccountForSells = fbAccountForSellService.selectFbAccountForSellListByAccountIds(ids);
        for (FbAccountForSell fbAccountForSell : fbAccountForSells) {
//            fbAccountForSellService.openBitBrowser(fbAccountForSell);
            WebDriver webDriver = null;
            try {
                webDriver = seleniumService.openBrowserForAccountSell(fbAccountForSell);
                fbAccountForSellService.loginFbAccountForSell(webDriver, fbAccountForSell);
                if ( !fbAccountForSellService.isLogin(webDriver, fbAccountForSell)){
                    webDriver.quit();
                    if (!fbAccountForSell.getNote().equals("账号或密码无效") && !fbAccountForSell.getNote().equals("无法登录-需要输入验证码")
                            && !fbAccountForSell.getNote().equals("无法登录-秘钥错误") && !fbAccountForSell.getNote().equals("无法登录-需要WhatsApp验证码")
                            && !fbAccountForSell.getNote().equals("无法登录-账号被锁") && !fbAccountForSell.getNote().equals("无法登录-改过密码") ){
                        fbAccountForSell.setNote("无法登录-未知情况");
                        fbAccountForSell.setCanLogin("0");
                        fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
                    }
                    continue;
                }
                if (fbAccountForSellService.isLogin(webDriver, fbAccountForSell)){
                    fbAccountForSell.setNote("");
                    fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
                }
                Thread.sleep(1000);
                fbAccountForSellService.getAccountNameAndFriendNumber(webDriver, fbAccountForSell);
            } catch (Exception e) {
                e.printStackTrace();
            }
            webDriver.quit();
        }

        return success();
    }

    /**
     * 卖出
     */
    @PreAuthorize("@ss.hasPermi('account:sell:edit')")
    @Log(title = "卖号", businessType = BusinessType.UPDATE)
    @GetMapping("/updateSellForSell/{keyId}")
    @ResponseBody
    public AjaxResult updateSellForSell(@PathVariable Long keyId) {
        FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByKeyId(keyId);
        fbAccountForSell.setIsSell("1");
        fbAccountForSell.setSellDate(LocalDate.now());
        fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
        return success();
    }

    /**
     * 打开账号
     */
    @GetMapping("/openBrowser/{keyId}")
    @ResponseBody
    public AjaxResult openBrowser(@PathVariable Long keyId) {
        FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByKeyId(keyId);
        WebDriver webDriver = seleniumService.openBrowserForAccountSell(fbAccountForSell);
        webDriverMap.put(fbAccountForSell.getId(), webDriver);
        fbAccountForSellService.loginFbAccountForSell(webDriver, fbAccountForSell);
        return success();
    }

    /**
     * 关闭账号
     */
    @GetMapping("/closeBrowser/{keyId}")
    @ResponseBody
    public AjaxResult closeBrowser(@PathVariable Long keyId) {
        FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByKeyId(keyId);
        webDriverMap.get(fbAccountForSell.getId()).quit();
        webDriverMap.remove(fbAccountForSell.getId());
        return success();
    }


    @GetMapping("/accountPost/{keyIds}")
    @ResponseBody
    public void accountPost(@PathVariable Long[] keyIds){
        for (Long keyId : keyIds) {
            FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByKeyId(keyId);
            WebDriver webDriver = seleniumService.openBrowserForAccountSell(fbAccountForSell);
            fbAccountForSellService.loginFbAccountForSell(webDriver, fbAccountForSell);
            if (fbAccountForSellService.isLogin(webDriver, fbAccountForSell)){
                fbAccountForSellService.post(fbAccountForSell,webDriver);
            }
            webDriver.quit();
            webDriverMap.remove(fbAccountForSell.getId());
        }
    }

    /**
     * 添加好友
     */
    @GetMapping("/addFriend")
    @ResponseBody
    public AjaxResult addFriend(@RequestParam("keyIds") List<Long> keyIds, @RequestParam("id") String id){

        /*List<FbAccountForSell> fbAccountForSellList = fbAccountForSellService.selectFbAccountForSellListByAccountIds(keyIds.toArray(new Long[0]));
        for (FbAccountForSell fbAccountForSell : fbAccountForSellList) {
            WebDriver webDriver = seleniumService.openBrowserForAccountSell(fbAccountForSell);
            webDriver.get("https://www.facebook.com/");
            boolean login = fbAccountForSellService.isLogin(webDriver, fbAccountForSell);
            if (!login){
                fbAccountForSellService.loginFbAccountForSell(webDriver, fbAccountForSell );
            }
            login = fbAccountForSellService.isLogin(webDriver, fbAccountForSell);
            if (login){
                fbAccountForSellService.addFriend(fbAccountForSell,id,webDriver);
            }
            webDriver.quit();
            webDriverMap.remove(fbAccountForSell.getId());
        }*/
        // 构造查询条件
        FbAccountForSellQuery query = new FbAccountForSellQuery();
        query.setAfterKeyId(3361L);
        query.setBeforeKeyId(29L);
        query.setRegion("中文");
        query.setCanLogin("1");
        query.setIsSell("0");

        // 获取所有账号
        List<FbAccountForSell> fbAccountForSellList = fbAccountForSellMapper.selectFbAccountForSellListByQuery(query);

        // 获取备注为0807的账号，作为被加好友的目标
        FbAccountForSell accountForSell = new FbAccountForSell();
        accountForSell.setNote("rich");
        accountForSell.setIsSell("0");
        List<FbAccountForSell> accountForSells = fbAccountForSellService.selectFbAccountForSellList(accountForSell);

        int batchSize = 11; // 每个目标最多被加13次
        int operatorIndex = 0; // 操作者索引

        for (FbAccountForSell target : accountForSells) {
            int count = 0;
            while (count < batchSize && operatorIndex < fbAccountForSellList.size()) {
                if (count > 1 && count % 5 == 0) {
                    System.out.println("该换了");
                    seleniumService.changeIP();// 每处理5个账号执行一次方法A
                    seleniumService.threadSleep(30);
                }
                FbAccountForSell operator = fbAccountForSellList.get(operatorIndex);
                WebDriver webDriver = fbAccountForSellService.openBrowser(operator);
                fbAccountForSellService.loginFbAccountForSell(webDriver, operator);
                fbAccountForSellService.addFriend(operator, target.getId(), webDriver);
                webDriver.quit();
                operatorIndex++; // 用完一个操作者账号
                count++;         // 当前目标账号被加好友计数+1
            }

            // 如果操作者已经用完，提前结束
            if (operatorIndex >= fbAccountForSellList.size()) {
                System.out.println("操作者账号已用完，提前终止加好友流程");
                break;
            }
        }
        return success();
    }

    /**
     * 计算页面
     */
    @GetMapping("/jumpPage")
    @ResponseBody
    public AjaxResult jumpPage(FbAccountForSell fbAccountForSell){
        List<FbAccountForSell> list = fbAccountForSellService.selectFbAccountForSellList(fbAccountForSell);
        if (list.size() == 1){
            FbAccountForSell targetAccount = list.get(0);
            // 查询数据库中所有的记录（这里假设数据库中记录的顺序就是你所需要的顺序）
            List<FbAccountForSell> allAccounts = fbAccountForSellService.selectFbAccountForSellListNoId(fbAccountForSell);
            // 通过 indexOf 方法获取 targetAccount 在所有记录中的位置
            int index = allAccounts.indexOf(targetAccount);
            return success(index); // 返回成功结果，包含位置

        }
        return success(-1);
    }

    @GetMapping("/checkAccountActive/{keyIds}")
    @ResponseBody
    public AjaxResult checkAccountActive(@PathVariable Long[] keyIds) {
        // 创建固定大小的线程池（可以根据 CPU 核心数调整）
        int threadCount = Runtime.getRuntime().availableProcessors(); // 获取 CPU 核心数
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        for (Long keyId : keyIds) {
            executor.submit(() -> {  // 提交任务到线程池
                try {
                    FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByKeyId(keyId);
                    boolean b = FBAccountUtil.checkAccountActive(fbAccountForSell.getId());
                    if (!b) {
                        fbAccountForSell.setCanLogin("0");
                        fbAccountForSell.setNote("被封/被锁");
                        fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
                    }
                    if (b) {
                        fbAccountForSell.setCanLogin("1");
                        fbAccountForSell.setNote(fbAccountForSell.getNote().replace("被封/被锁", ""));
                        fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        return success();
    }

    @GetMapping("/changeAccountName")
    @ResponseBody
    public AjaxResult changeAccountName(@RequestParam("keyId") List<Long> keyId, @RequestParam("accountName") String accountName){
        FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellListByAccountIds(keyId.toArray(new Long[0])).get(0);
        fbAccountForSellService.changeAccountName(fbAccountForSell,accountName);
        return success();
    }

    @GetMapping("/createPage")
    @ResponseBody
    public AjaxResult createPage(@RequestParam("keyId") List<Long> keyId, @RequestParam("pageName") String pageName){
        List<FbAccountForSell> fbAccountForSellList = fbAccountForSellService.selectFbAccountForSellListByAccountIds(keyId.toArray(new Long[0]));
        for (FbAccountForSell fbAccountForSell : fbAccountForSellList) {
            String createPageResult = null;
            try {
                createPageResult = fbAccountForSellService.createPage(fbAccountForSell, pageName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (createPageResult.equals("true")){
                break;
            }
        }
        return success();
    }

    @GetMapping("/confirmAddFriend/{keyId}")
    @ResponseBody
    public AjaxResult confirmAddFriend(@PathVariable Long keyId){
        FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByKeyId(keyId);
        fbAccountForSellService.confirmAddFriend(fbAccountForSell);
        return success();
    }

    @GetMapping("/getEmail/{keyId}")
    @ResponseBody
    public AjaxResult getEmail(@PathVariable Long keyId){
        FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByKeyId(keyId);
        Email email = emailService.selectEmailByEmail(fbAccountForSell.getEmail());
        if (email == null){
            email = new Email();
            email.setEmail(fbAccountForSell.getEmail());
            email.setPassword(fbAccountForSell.getPassword());
        }
        String message = "";
        try {
            message = emailService.getMessage(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success(message);
    }

    @GetMapping("/unlockEmail/{keyId}")
    @ResponseBody
    public AjaxResult unlockEmail(@PathVariable Long keyId){
        FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByKeyId(keyId);
        Email email = emailService.selectEmailByEmail(fbAccountForSell.getEmail());
        List<ProxyIp> proxyIps = proxyIpService.selectProxyIpListByStatus("1");
        ProxyIp proxyIp = proxyIps.get(proxyIps.size() - 1);
        emailService.unlockEmail(email,proxyIp);

        return success();
    }

    @PostMapping("/getAccountInfo")
    @ResponseBody
    public AjaxResult getAccountInfo(@RequestBody Map<String, Object> payload) throws JsonProcessingException {
        // 从请求体中解析 keyIds 和 selectedOptions
        List<String> keyIds = (List<String>) payload.get("keyIds");
        List<String> selectedOptions = (List<String>) payload.get("selectedOptions");

        int count = 0; // 计数器

        for (String id : keyIds) {
            try {
                FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellById(id);
                WebDriver webDriver = fbAccountForSellService.openBrowser(fbAccountForSell);

                try {
                    fbAccountForSellService.loginFbAccountForSell(webDriver, fbAccountForSell);

                    if (selectedOptions.contains("账号名字")) {
                        // TODO: 填入对应逻辑
                    }
                    if (selectedOptions.contains("名字好友")) {
                        fbAccountForSellService.getAccountNameAndFriendNumber(webDriver, fbAccountForSell);
                    }
                    if (selectedOptions.contains("登录记录")) {
                        fbAccountForSellService.getAccountLoginLog(webDriver, fbAccountForSell);
                    }
                    if (selectedOptions.contains("是否商城")) {
                        fbAccountForSellService.getIsMarketplace(webDriver, fbAccountForSell);
                    }
                    if (selectedOptions.contains("主页数量")) {
                        fbAccountForSellService.getAccountPage(webDriver, fbAccountForSell);
                    }
                    if (selectedOptions.contains("BM数量")) {
                        fbAccountForSellService.getAccountBm(webDriver, fbAccountForSell);
                    }
                    if (selectedOptions.contains("广告状态")) {
                        fbAccountForSellService.getAccountAdStatus(webDriver, fbAccountForSell);
                    }
                    if (selectedOptions.contains("帖子数量")) {
                        fbAccountForSellService.getAccountPostCount(webDriver, fbAccountForSell);
                    }
                } catch (Exception innerEx) {
                    System.err.println("处理账号 [" + id + "] 时出错: " + innerEx.getMessage());
                    innerEx.printStackTrace();
                } finally {
                    webDriver.quit(); // 无论是否出错都关闭浏览器
                    count++;
                    if (count > 1 && count % 5 == 0) {
//                    if (count > 1) {
                        System.out.println("该换了");
                        seleniumService.changeIP();// 每处理5个账号执行一次方法A
                        seleniumService.threadSleep(30);
                    }
                }


            } catch (Exception outerEx) {
                System.err.println("账号 [" + id + "] 无法初始化或登录，跳过。错误信息: " + outerEx.getMessage());
                outerEx.printStackTrace();
            }

        }

        return success();
    }

    @GetMapping("/get2FACode/{id}")
    public AjaxResult get2FACode(@PathVariable String id) {
        FbAccountForSell account = fbAccountForSellService.selectFbAccountForSellById(id);
        if (account == null || account.getSecretKey() == null) {
            return AjaxResult.error("账号不存在或未绑定双重验证码");
        }

        String code = FBAccountUtil.getGoogleVerificationCode(account.getSecretKey());

        return success(code);
    }

}

