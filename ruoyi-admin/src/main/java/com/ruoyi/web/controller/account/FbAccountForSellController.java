package com.ruoyi.web.controller.account;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.account.domain.Posts;
import com.ruoyi.account.service.ISeleniumService;
import org.openqa.selenium.WebDriver;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.account.domain.FbAccountForSell;
import com.ruoyi.account.service.IFbAccountForSellService;
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
public class FbAccountForSellController extends BaseController
{
    @Autowired
    private IFbAccountForSellService fbAccountForSellService;

    @Autowired
    private ISeleniumService seleniumService;

    /**
     * 查询卖号列表
     */
    @PreAuthorize("@ss.hasPermi('account:sell:list')")
    @GetMapping("/list")
    public TableDataInfo list(FbAccountForSell fbAccountForSell)
    {
        startPage();
        List<FbAccountForSell> list = fbAccountForSellService.selectFbAccountForSellList(fbAccountForSell);
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
    public void importTemplate(HttpServletResponse response)
    {
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

            WebDriver webDriver = null;
            try {
                webDriver = seleniumService.openBrowserForAccountSell(fbAccountForSell);
                fbAccountForSellService.loginFbAccountForSell(webDriver, fbAccountForSell);
                String loginStatus = fbAccountForSellService.isLogin(webDriver);
                if (loginStatus != "true"){
                    webDriver.close();
                    if (!fbAccountForSell.getNote().equals("账号或密码无效") && !fbAccountForSell.getNote().equals("需要输入验证码")){
                        fbAccountForSell.setNote("无法登录-未知情况");
                        fbAccountForSell.setCanLogin("0");
                        fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
                    }
                    continue;
                }
                if (fbAccountForSell.getNote().contains("无法登录-未知情况")){
                    fbAccountForSell.setNote(fbAccountForSell.getNote().replace("无法登录-未知情况",""));
                    fbAccountForSellService.updateFbAccountForSell(fbAccountForSell);
                }
                Thread.sleep(1000);
                fbAccountForSellService.getAccountMarketplaceAndNameAndFriendInSimplified(webDriver, fbAccountForSell);
            } catch (Exception e) {
                e.printStackTrace();
            }
            webDriver.close();
        }

        return success();
    }





}
