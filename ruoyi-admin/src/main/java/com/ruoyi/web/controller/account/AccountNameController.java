package com.ruoyi.web.controller.account;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.ruoyi.account.domain.AccountName;
import com.ruoyi.account.service.IAccountNameService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 名字Controller
 * 
 * @author ruoyi
 * @date 2025-09-17
 */
@RestController
@RequestMapping("/account/name")
public class AccountNameController extends BaseController
{
    @Autowired
    private IAccountNameService accountNameService;

    /**
     * 查询名字列表
     */
    @PreAuthorize("@ss.hasPermi('account:name:list')")
    @GetMapping("/list")
    public TableDataInfo list(AccountName accountName)
    {
        startPage();
        List<AccountName> list = accountNameService.selectAccountNameList(accountName);
        return getDataTable(list);
    }

    /**
     * 导出名字列表
     */
    @PreAuthorize("@ss.hasPermi('account:name:export')")
    @Log(title = "名字", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AccountName accountName)
    {
        List<AccountName> list = accountNameService.selectAccountNameList(accountName);
        ExcelUtil<AccountName> util = new ExcelUtil<AccountName>(AccountName.class);
        util.exportExcel(response, list, "名字数据");
    }

    /**
     * 获取名字详细信息
     */
    @PreAuthorize("@ss.hasPermi('account:name:query')")
    @GetMapping(value = "/{keyId}")
    public AjaxResult getInfo(@PathVariable("keyId") Long keyId)
    {
        return success(accountNameService.selectAccountNameByKeyId(keyId));
    }

    /**
     * 新增名字
     */
    @PreAuthorize("@ss.hasPermi('account:name:add')")
    @Log(title = "名字", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AccountName accountName)
    {
        return toAjax(accountNameService.insertAccountName(accountName));
    }

    /**
     * 修改名字
     */
    @PreAuthorize("@ss.hasPermi('account:name:edit')")
    @Log(title = "名字", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AccountName accountName)
    {
        return toAjax(accountNameService.updateAccountName(accountName));
    }

    /**
     * 删除名字
     */
    @PreAuthorize("@ss.hasPermi('account:name:remove')")
    @Log(title = "名字", businessType = BusinessType.DELETE)
	@DeleteMapping("/{keyIds}")
    public AjaxResult remove(@PathVariable Long[] keyIds)
    {
        return toAjax(accountNameService.deleteAccountNameByKeyIds(keyIds));
    }
}
