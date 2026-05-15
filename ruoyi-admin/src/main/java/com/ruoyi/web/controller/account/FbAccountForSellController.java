package com.ruoyi.web.controller.account;

import java.time.LocalDate;
import java.util.*;
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
import com.ruoyi.account.service.*;
import com.ruoyi.account.util.FBAccountUtil;
import com.ruoyi.account.util.VmCommandHolder;
import com.ruoyi.browser.builder.HubEnvParamBuilder;
import com.ruoyi.browser.client.HubstudioClient;
import com.ruoyi.browser.convert.HubstudioConvert;
import com.ruoyi.browser.domain.HubEnv;
import com.ruoyi.browser.service.IHubEnvService;
import io.appium.java_client.AppiumDriver;
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

    @Autowired
    private ICreateDeviceService createDeviceService;

    @Autowired
    HubstudioClient hubstudioClient;

    @Autowired
    IHubEnvService hubEnvService;

    @Autowired
    HubstudioConvert hubstudioConvert;

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

    @Autowired
    private VmCommandHolder vmCommandHolder;

    @GetMapping("/getCommand")
    public String getCommand() {
        String cmd = vmCommandHolder.getCommand();
        // 打印值
        vmCommandHolder.clear();  // 避免重复执行
        return cmd;
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
    public AjaxResult  batchSearch(@RequestBody FbAccountForSell account) {

        List<FbAccountForSell> list = fbAccountForSellService.batchSearch(account);
        return AjaxResult.success(list);
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
     * 获取卖号详细信息
     */
    @GetMapping(value = "/getSellForShow")
    @ResponseBody
    public AjaxResult getSellForShow(@RequestParam("ids") List<Long> ids)
    {
        return success(fbAccountForSellService.selectFbAccountForSellListByAccountIds(ids.toArray(new Long[0])));
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
        List<Long> containerCode = new ArrayList<>();
        for (Long keyId : keyIds){
            FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByKeyId(keyId);
            HubEnv hubEnv = hubEnvService.selectHubEnvByAccountName(fbAccountForSell.getId());
            if (hubEnv != null){
                containerCode.add(hubEnv.getContainerCode());
                hubEnvService.closeEnv(hubEnv);
                hubEnvService.deleteHubEnvById(hubEnv.getId());
            }
        }
        if (containerCode.size() > 0){
            System.out.println("开始删除");
            System.out.println(containerCode);
            Map<String, Object> deleteEnvParam = HubEnvParamBuilder.builderDeleteEnvParam(containerCode);
            hubstudioClient.deleteEnv(deleteEnvParam);
        }
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
                        String oldNote = fbAccountForSell.getNote();
                        fbAccountForSell.setNote("无法登录-未知情况|"+oldNote);
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
    @GetMapping("/updateSellForSell/{keyIds}")
    @ResponseBody
    public AjaxResult updateSellForSell(@PathVariable Long[] keyIds) {
        for (Long keyId : keyIds) {
            FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByKeyId(keyId);
            fbAccountForSell.setIsSell("1");
            fbAccountForSell.setSellDate(LocalDate.now());
            fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
        }

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
     * 打开账号
     */
    @GetMapping("/openBitBrowser/{keyId}")
    @ResponseBody
    public AjaxResult openBitBrowser(@PathVariable Long keyId) {
        FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByKeyId(keyId);
        WebDriver webDriver = seleniumService.openBitBrowser(new HashMap<>());
        webDriverMap.put(fbAccountForSell.getId(), webDriver);
        fbAccountForSellService.loginFbAccountForSell(webDriver, fbAccountForSell);
        return success();
    }

    @GetMapping("/openHubBrowser/{keyId}")
    @ResponseBody
    public AjaxResult openHubBrowser(@PathVariable Long keyId) {
        FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByKeyId(keyId);

        //accountName 是账号ID
        HubEnv hubEnv =
                hubEnvService.selectHubEnvByAccountName(fbAccountForSell.getId());

        if (hubEnv == null) {
            hubEnv = hubEnvService.createAndBindEnv(fbAccountForSell);
        }

        WebDriver webDriver =hubstudioClient.openEnv(hubEnv.getContainerCode());

        fbAccountForSellService.loginFbAccountForSell(webDriver,fbAccountForSellService.selectFbAccountForSellByKeyId(keyId));

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
    public AjaxResult addFriend(@RequestParam(value = "keyIds",defaultValue = "") List<Long> keyIds, @RequestParam(value = "id", defaultValue = "") String id, @RequestParam(value = "startKeyId", defaultValue = "") Long startKeyId,
                                    @RequestParam(value = "endKeyId",defaultValue = "") Long endKeyId, @RequestParam(value = "region",defaultValue = "") String region, @RequestParam(value = "note",defaultValue = "") String note){
        if (!keyIds.isEmpty()){
            if (id.equals("") || id == null){
                return error("添加id为空");
            }
            List<FbAccountForSell> fbAccountForSellList = fbAccountForSellService.selectFbAccountForSellListByAccountIds(keyIds.toArray(new Long[0]));
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
            }
            return success();
        }

        if (null == startKeyId || null == endKeyId || null == region || null == note){
            return error("请填写完整信息");
        }
        // 构造查询条件
        FbAccountForSellQuery query = new FbAccountForSellQuery();
        query.setRegion(region);
        query.setCanLogin("1");
        query.setIsSell("0");

        // 获取所有账号
        List<FbAccountForSell> fbAccountForSellList = fbAccountForSellMapper.selectFbAccountForSellListByQuery(query);

        // 获取备注为0807的账号，作为被加好友的目标
        FbAccountForSell accountForSell = new FbAccountForSell();
        accountForSell.setNote(note);
        accountForSell.setIsSell("0");
        List<FbAccountForSell> accountForSells = fbAccountForSellService.selectFbAccountForSellList(accountForSell);

        int batchSize = 9; // 每个目标最多被加13次
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
                        String oldNote = fbAccountForSell.getNote();
                        fbAccountForSell.setNote("被封/被锁|"+oldNote);
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

    /**
     *
     *修改名字
     */
    @GetMapping("/changeAccountName")
    @ResponseBody
    public AjaxResult changeAccountName(@RequestParam("keyId") List<Long> keyId, @RequestParam(value = "accountName", defaultValue = "") String accountName){
        if (!accountName.equals("")){
            if (keyId.size() == 1){
                FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellById(keyId.get(0).toString());
                WebDriver webDriver = hubEnvService.openOrCreateAndOpenEnvForFb(fbAccountForSell.getKeyId());
                fbAccountForSellService.changeAccountName(webDriver, fbAccountForSell, accountName);
                fbAccountForSellService.closeBrowser(fbAccountForSell);
                return success();
            }else {
                return error("不支持对多个账号修改同一名字");
            }
        }
        for (Long id : keyId){
            FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellById(Long.toString(id));
            WebDriver webDriver = hubEnvService.openOrCreateAndOpenEnvForFb(fbAccountForSell.getKeyId());
            fbAccountForSellService.changeRandomAccountName(webDriver, fbAccountForSell, accountName);
            fbAccountForSellService.closeBrowser(fbAccountForSell);
        }
        return success();
    }

    /**
     *
     * 修改备注
     */
    @GetMapping("/changeAccountNote")
    @ResponseBody
    public AjaxResult changeAccountNote(@RequestParam("id") List<Long> id, @RequestParam(value = "accountNote", defaultValue = "") String accountNote){

        List<FbAccountForSell> fbAccountForSellList = fbAccountForSellService.selectFbAccountForSellListByAccountIds(id.toArray(new Long[0]));
        for (FbAccountForSell fbAccountForSell : fbAccountForSellList) {
            if (accountNote.isEmpty()){
                fbAccountForSell.setNote(accountNote);
                fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
            }else {
            String note = fbAccountForSell.getNote();
            fbAccountForSell.setNote(accountNote+" | "+note);
            fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
            }
        }
        return success();
    }

    @GetMapping("/createPage")
    @ResponseBody
    public AjaxResult createPage(@RequestParam("keyId") List<Long> keyId, @RequestParam("pageName") String pageName){
        List<FbAccountForSell> fbAccountForSellList = fbAccountForSellService.selectFbAccountForSellListByAccountIds(keyId.toArray(new Long[0]));
        for (FbAccountForSell fbAccountForSell : fbAccountForSellList) {
            String createPageResult = null;
            try {
                WebDriver webDriver = hubEnvService.openOrCreateAndOpenEnvForFb(fbAccountForSell.getKeyId());
                createPageResult = fbAccountForSellService.createPage(webDriver, fbAccountForSell, pageName);
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

    /**
     * 获取邮件
     */
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

    @GetMapping("/unlockEmail/{keyIds}")
    @ResponseBody
    public AjaxResult unlockEmail(@PathVariable Long[] keyIds){

        for (Long keyId : keyIds){
            FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByKeyId(keyId);
            Email email = emailService.selectEmailByEmail(fbAccountForSell.getEmail());
            if (email == null){
                email = new Email();
                email.setEmail(fbAccountForSell.getEmail());
                email.setPassword(fbAccountForSell.getPassword());
                email.setAccountId(fbAccountForSell.getId());
                email.setIsBoundAccount("1");
                emailService.insertEmail(email);
            }
            List<ProxyIp> proxyIps = proxyIpService.selectProxyIpListByStatus("1");
            ProxyIp proxyIp = proxyIps.get(proxyIps.size() - 1);
            Map<String, Object> map = hubEnvService.openOrCreateAndOpenEnvForEmail(proxyIp);
            Object webDriver = map.get("webDriver");
            emailService.unlockEmail(email, (WebDriver) webDriver);
        }
        return success();
    }

    @GetMapping("/unlockWSVerify/{keyIds}")
    @ResponseBody
    public AjaxResult unlockWSVerify(@PathVariable Long[] keyIds){
        for (Long keyId : keyIds){
            FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByKeyId(keyId);
            HubEnv hubEnv = hubEnvService.selectHubEnvByAccountName(fbAccountForSell.getId());
            WebDriver webDriver = hubEnvService.openOrCreateAndOpenEnvForFb(fbAccountForSell.getKeyId());
            fbAccountForSellService.loginFbAccountForSell(webDriver,fbAccountForSell);
            fbAccountForSellService.superAccount(webDriver,fbAccountForSell);
            HubEnv latestHubEnv = hubEnvService.selectHubEnvByAccountName(fbAccountForSell.getId());
//            hubEnvService.closeEnv(latestHubEnv);
        }
        return success();
    }


    @PostMapping("/getAccountInfo")
    @ResponseBody
    public AjaxResult getAccountInfo(@RequestBody Map<String, Object> payload) throws JsonProcessingException {

        // 从请求体中解析 keyIds 和 selectedOptions
        List<String> keyIds = (List<String>) payload.get("keyIds");
        List<String> selectedOptions = (List<String>) payload.get("selectedOptions");


        List<HubEnv> hubEnvList = new ArrayList<>();
        List<FbAccountForSell> accountList = new ArrayList<>();

        for (String id : keyIds) {
            FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellById(id);

            //accountName 是账号ID
            HubEnv hubEnv =
                    hubEnvService.selectHubEnvByAccountName(fbAccountForSell.getId());

            if (hubEnv == null) {
                hubEnv = hubEnvService.createAndBindEnv(fbAccountForSell);
            }

            hubEnvList.add(hubEnv);
            accountList.add(fbAccountForSell);
        }

        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 0; i < hubEnvList.size(); i++) {

            // ⭐ 关键：必须用 final 变量
            final HubEnv hubEnv = hubEnvList.get(i);
            final FbAccountForSell fbAccountForSell = accountList.get(i);

            executor.submit(() -> {
                WebDriver webDriver = null;
                try {

                    webDriver = hubstudioClient.openEnv(hubEnv.getContainerCode());

                    fbAccountForSellService.loginFbAccountForSell(webDriver, fbAccountForSell);

                    if (selectedOptions.contains("单纯登录")) {
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
                    if(selectedOptions.contains("台湾号")){
                        fbAccountForSellService.changeTWAccount(webDriver, fbAccountForSell);
                    }

                } catch (Exception e) {
                    System.err.println("账号异常: " + fbAccountForSell.getId());
                    e.printStackTrace();
                } finally {
                    webDriver.quit();
                }
            });
        }

        /*for (String id : keyIds) {
            try {
                FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellById(id);
                WebDriver webDriver = hubEnvService.openOrCreateAndOpenEnvForFb(fbAccountForSell.getKeyId());

                try {
                    fbAccountForSellService.loginFbAccountForSell(webDriver, fbAccountForSell);

                    if (selectedOptions.contains("单纯登录")) {
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
                    if(selectedOptions.contains("台湾号")){
                        fbAccountForSellService.changeTWAccount(webDriver, fbAccountForSell);
                    }
                } catch (Exception innerEx) {
                    System.err.println("处理账号 [" + id + "] 时出错: " + innerEx.getMessage());
                    innerEx.printStackTrace();
                } finally {
                    webDriver.quit(); // 无论是否出错都关闭浏览器
                }
            } catch (Exception outerEx) {
                System.err.println("账号 [" + id + "] 无法初始化或登录，跳过。错误信息: " + outerEx.getMessage());
                outerEx.printStackTrace();
            }
        }*/
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

    @GetMapping("/loginEmail/{keyId}")
    @ResponseBody
    public AjaxResult loginEmail(@PathVariable Long keyId){
        FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellByKeyId(keyId);
        List<ProxyIp> proxyIps = proxyIpService.selectProxyIpListByStatus("1");
        ProxyIp proxyIp = proxyIps.get(proxyIps.size() - 1);
        emailService.tempLogin(fbAccountForSell.getEmail(),fbAccountForSell.getEmailPassword(),proxyIp);
        return success();
    }

    @GetMapping("/loginInPhone/{keyId}")
    @ResponseBody
    public AjaxResult loginInPhone(@PathVariable Long keyId){
        // FB账号
        FbAccountForSell fbAccountForSell =
                fbAccountForSellService.selectFbAccountForSellByKeyId(keyId);

        if (fbAccountForSell == null){
            return error("账号不存在");
        }

        // 2️⃣ 先查是否已有绑定设备
        CreateDevice createDevice =
                createDeviceService.selectCreateDeviceByCreateId(fbAccountForSell.getId());

        // 3️⃣ 如果没有，再查空闲设备
        if (createDevice == null){
            createDevice = createDeviceService.selectMinNoAccountDevice();
        }

        // 4️⃣ 如果还是没有
        if (createDevice == null){
            return error("没有可用设备");
        }

        try {

            // 锁定设备（防止并发重复使用）
            createDevice.setCreateAccountId(
                    fbAccountForSell.getId()
            );

            createDeviceService.updateCreateDevice(createDevice);

            // 打开APP
            AppiumDriver appiumDriver =
                    createDeviceService.openApp(createDevice);

            createDevice.setCreateAccountId(fbAccountForSell.getId());
            createDeviceService.updateCreateDevice(createDevice);

            String oldNote = fbAccountForSell.getNote();
            if(!oldNote.contains("已登手机")){
                fbAccountForSell.setNote("已登手机|"+oldNote);
                fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
            }

            // 登录
            createDeviceService.loginAccount(
                    appiumDriver,
                    fbAccountForSell
            );

            return success("登录成功");

        } catch (Exception e) {

            e.printStackTrace();

            return error(e.getMessage());

        }
    }




}

