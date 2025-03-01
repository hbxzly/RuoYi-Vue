package com.ruoyi.web.controller.account;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.account.domain.FbAccountForSell;
import com.ruoyi.account.service.ISeleniumService;
import org.openqa.selenium.WebDriver;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.account.domain.FbAccount;
import com.ruoyi.account.service.IFbAccountService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 账号Controller
 *
 * @author ruoyi
 * @date 2024-12-12
 */
@RestController
@RequestMapping("/account/fbAccount")
public class FbAccountController extends BaseController
{
    @Autowired
    private IFbAccountService fbAccountService;

    @Autowired
    private ISeleniumService seleniumService;

    /**
     * 查询账号列表
     */
    @PreAuthorize("@ss.hasPermi('account:fbAccount:list')")
    @GetMapping("/list")
    public TableDataInfo list(FbAccount fbAccount) {
        startPage();
        List<FbAccount> list = fbAccountService.selectFbAccountList(fbAccount);
        return getDataTable(list);
    }

    /**
     * 导出账号列表
     */
    @PreAuthorize("@ss.hasPermi('account:fbAccount:export')")
    @Log(title = "账号", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FbAccount fbAccount) {
        List<FbAccount> list = fbAccountService.selectFbAccountList(fbAccount);
        ExcelUtil<FbAccount> util = new ExcelUtil<FbAccount>(FbAccount.class);
        util.exportExcel(response, list, "账号数据");
    }

    /**
     * 获取账号详细信息
     */
    @PreAuthorize("@ss.hasPermi('account:fbAccount:query')")
    @GetMapping(value = "/{keyId}")
    public AjaxResult getInfo(@PathVariable("keyId") Long keyId) {
        return success(fbAccountService.selectFbAccountByKeyId(keyId));
    }

    /**
     * 新增账号
     */
    @PreAuthorize("@ss.hasPermi('account:fbAccount:add')")
    @Log(title = "账号", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FbAccount fbAccount) {
        return toAjax(fbAccountService.insertFbAccount(fbAccount));
    }

    /**
     * 修改账号
     */
    @PreAuthorize("@ss.hasPermi('account:fbAccount:edit')")
    @Log(title = "账号", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FbAccount fbAccount) {
        return toAjax(fbAccountService.updateFbAccount(fbAccount));
    }

    /**
     * 删除账号
     */
    @PreAuthorize("@ss.hasPermi('account:fbAccount:remove')")
    @Log(title = "账号", businessType = BusinessType.DELETE)
    @DeleteMapping("/{keyIds}")
    public AjaxResult remove(@PathVariable Long[] keyIds) {
        return toAjax(fbAccountService.deleteFbAccountByKeyIds(keyIds));
    }

    /**
     * 打开账号
     */
    @GetMapping("/openBrowser/{keyIds}")
    @ResponseBody
    public AjaxResult openBrowser(@PathVariable Long[] keyIds){
        List<FbAccount> fbAccounts = fbAccountService.selectFbAccountByKeyIds(keyIds);
        for (FbAccount fbAccount : fbAccounts) {
            WebDriver webDriver = fbAccountService.openBrowser(fbAccount);
            fbAccountService.login(fbAccount, webDriver);
        }

        return success();
    }

    /**
     * 关闭浏览器
     */
    @GetMapping("/closeBrowser/{keyIds}")
    @ResponseBody
    public AjaxResult closeBrowser(@PathVariable Long[] keyIds){
        List<FbAccount> fbAccounts = fbAccountService.selectFbAccountByKeyIds(keyIds);
        for (FbAccount fbAccount : fbAccounts) {
            fbAccountService.closeBrowser(fbAccount);
        }
        return success();
    }

    /**
     * 添加好友
     */
    @GetMapping("/addFriend")
    @ResponseBody
    public AjaxResult addFriend(@RequestParam("keyIds") List<Long> keyIds, @RequestParam("id") String id){
        List<FbAccount> fbAccounts = fbAccountService.selectFbAccountByKeyIds(keyIds.toArray(new Long[0]));
        for (FbAccount fbAccount : fbAccounts) {
            WebDriver webDriver = seleniumService.openBrowser(fbAccount);
            fbAccountService.login(fbAccount, webDriver);
            if (fbAccountService.isLogin(fbAccount,webDriver)){
                fbAccountService.addFriend(fbAccount,id,webDriver);
            }
            seleniumService.closeBrowser(fbAccount);
        }
        return success();
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
        ExcelUtil<FbAccount> util = new ExcelUtil<FbAccount>(FbAccount.class);
        List<FbAccount> fbAccountList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = fbAccountService.importFbAccountForSell(fbAccountList, updateSupport, operName);
        return success(message);
    }

    /**
     * 导入模板
     * @param response
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<FbAccount> util = new ExcelUtil<FbAccount>(FbAccount.class);
        util.importTemplateExcel(response, "账号数据");
    }

    @GetMapping("/checkAccount/{keyIds}")
    @ResponseBody
    public void checkAccount(@PathVariable Long[] keyIds){
        List<FbAccount> fbAccounts = fbAccountService.selectFbAccountByKeyIds(keyIds);
        for (FbAccount fbAccount : fbAccounts) {
            WebDriver webDriver = seleniumService.openBrowser(fbAccount);
            fbAccountService.login(fbAccount, webDriver);
            if (fbAccountService.isLogin(fbAccount,webDriver)){
                fbAccountService.checkAccount(fbAccount,webDriver);
            }
            seleniumService.closeBrowser(fbAccount);
        }
    }

    @GetMapping("/accountPost/{keyIds}")
    @ResponseBody
    public void accountPost(@PathVariable Long[] keyIds){
        List<FbAccount> fbAccounts = fbAccountService.selectFbAccountByKeyIds(keyIds);
        for (FbAccount fbAccount : fbAccounts) {
            WebDriver webDriver = seleniumService.openBrowser(fbAccount);
            fbAccountService.login(fbAccount, webDriver);
            if (fbAccountService.isLogin(fbAccount,webDriver)){
                fbAccountService.post(fbAccount,webDriver);
            }
            seleniumService.closeBrowser(fbAccount);
        }
    }

    @GetMapping("/closeAllBrowser")
    @ResponseBody
    public void closeAllBrowser(){
        fbAccountService.closeAllBrowser();
    }

    /**
     * 计算页面
     */
    @GetMapping("/jumpPage")
    @ResponseBody
    public AjaxResult jumpPage(FbAccount fbAccount){
        List<FbAccount> list = fbAccountService.selectFbAccountList(fbAccount);
        if (list.size() == 1){
            FbAccount targetFbAccount = list.get(0);
            List<FbAccount> allFbAccount = fbAccountService.selectFbAccountListNoId(fbAccount);
            int i = allFbAccount.indexOf(targetFbAccount);
            return success(i);
        }
        return success(-1);
    }

}
