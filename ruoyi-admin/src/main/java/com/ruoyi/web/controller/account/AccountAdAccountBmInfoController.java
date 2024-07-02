package com.ruoyi.web.controller.account;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.account.domain.AccountAdAccountBmInfo;
import com.ruoyi.account.domain.FbAccount;
import com.ruoyi.account.service.IAccountAdAccountBmInfoService;
import com.ruoyi.account.service.IFbAccountService;
import com.ruoyi.account.service.ISeleniumService;
import com.ruoyi.account.service.impl.IFbAccountServiceImpl;
import com.ruoyi.account.service.impl.ISeleniumServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 账号账户信息Controller
 *
 * @author ruoyi
 * @date 2024-03-02
 */
@RestController
@RequestMapping("/account/info")
public class AccountAdAccountBmInfoController extends BaseController
{
    @Autowired
    private IAccountAdAccountBmInfoService accountAdAccountBmInfoService;

    @Autowired
    private IFbAccountService fbAccountService;

    @Autowired
    private ISeleniumService seleniumService;

    /**
     * 查询账号账户信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(AccountAdAccountBmInfo accountAdAccountBmInfo)
    {
        startPage();
        List<AccountAdAccountBmInfo> list = accountAdAccountBmInfoService.selectAccountAdAccountBmInfoList(accountAdAccountBmInfo);
        return getDataTable(list);
    }

    /**
     * 导出账号账户信息列表
     */
    @Log(title = "账号账户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AccountAdAccountBmInfo accountAdAccountBmInfo)
    {
        List<AccountAdAccountBmInfo> list = accountAdAccountBmInfoService.selectAccountAdAccountBmInfoList(accountAdAccountBmInfo);
        ExcelUtil<AccountAdAccountBmInfo> util = new ExcelUtil<AccountAdAccountBmInfo>(AccountAdAccountBmInfo.class);
        util.exportExcel(response, list, "账号账户信息数据");
    }

    /**
     * 获取账号账户信息详细信息
     */
    @GetMapping(value = "/{keyId}")
    public AjaxResult getInfo(@PathVariable("keyId") Long keyId)
    {
        return success(accountAdAccountBmInfoService.selectAccountAdAccountBmInfoByKeyId(keyId));
    }

    /**
     * 新增账号账户信息
     */
    @Log(title = "账号账户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AccountAdAccountBmInfo accountAdAccountBmInfo)
    {
        return toAjax(accountAdAccountBmInfoService.insertAccountAdAccountBmInfo(accountAdAccountBmInfo));
    }

    /**
     * 修改账号账户信息
     */
    @Log(title = "账号账户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AccountAdAccountBmInfo accountAdAccountBmInfo) {
        return toAjax(accountAdAccountBmInfoService.updateAccountAdAccountBmInfo(accountAdAccountBmInfo));
    }

    /**
     * 修改账号账户信息
     */
    @PutMapping(value = "/openAds")
    public AjaxResult openAds(@RequestBody AccountAdAccountBmInfo accountAdAccountBmInfo) {
        FbAccount fbAccount = fbAccountService.selectOneByFbAccountId(accountAdAccountBmInfo.getAccountId());
        seleniumService.openAdvertise(fbAccount,accountAdAccountBmInfo.getAdAccountId());
        return AjaxResult.success();
    }

    /**
     * 删除账号账户信息
     */
    @Log(title = "账号账户信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{keyIds}")
    public AjaxResult remove(@PathVariable Long[] keyIds)
    {
        return toAjax(accountAdAccountBmInfoService.deleteAccountAdAccountBmInfoByKeyIds(keyIds));
    }
}
