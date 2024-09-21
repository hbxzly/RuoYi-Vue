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
import com.ruoyi.account.domain.FbAccountForSell;
import com.ruoyi.account.service.IFbAccountForSellService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 卖号Controller
 * 
 * @author ruoyi
 * @date 2024-09-20
 */
@RestController
@RequestMapping("/account/fbAccountForSell")
public class FbAccountForSellController extends BaseController
{
    @Autowired
    private IFbAccountForSellService fbAccountForSellService;

    /**
     * 查询卖号列表
     */
    @PreAuthorize("@ss.hasPermi('account:fbAccountForSell:list')")
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
    @PreAuthorize("@ss.hasPermi('account:fbAccountForSell:export')")
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
    @PreAuthorize("@ss.hasPermi('account:fbAccountForSell:query')")
    @GetMapping(value = "/{keyId}")
    public AjaxResult getInfo(@PathVariable("keyId") Long keyId)
    {
        return success(fbAccountForSellService.selectFbAccountForSellByKeyId(keyId));
    }

    /**
     * 新增卖号
     */
    @PreAuthorize("@ss.hasPermi('account:fbAccountForSell:add')")
    @Log(title = "卖号", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FbAccountForSell fbAccountForSell)
    {
        return toAjax(fbAccountForSellService.insertFbAccountForSell(fbAccountForSell));
    }

    /**
     * 修改卖号
     */
    @PreAuthorize("@ss.hasPermi('account:fbAccountForSell:edit')")
    @Log(title = "卖号", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FbAccountForSell fbAccountForSell)
    {
        return toAjax(fbAccountForSellService.updateFbAccountForSell(fbAccountForSell));
    }

    /**
     * 删除卖号
     */
    @PreAuthorize("@ss.hasPermi('account:fbAccountForSell:remove')")
    @Log(title = "卖号", businessType = BusinessType.DELETE)
	@DeleteMapping("/{keyIds}")
    public AjaxResult remove(@PathVariable Long[] keyIds)
    {
        return toAjax(fbAccountForSellService.deleteFbAccountForSellByKeyIds(keyIds));
    }
}
