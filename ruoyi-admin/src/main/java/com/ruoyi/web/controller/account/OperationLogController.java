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
import com.ruoyi.account.domain.OperationLog;
import com.ruoyi.account.service.IOperationLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 操作记录Controller
 * 
 * @author ruoyi
 * @date 2024-09-29
 */
@RestController
@RequestMapping("/account/operation")
public class OperationLogController extends BaseController
{
    @Autowired
    private IOperationLogService operationLogService;

    /**
     * 查询操作记录列表
     */
    @PreAuthorize("@ss.hasPermi('account:operation:list')")
    @GetMapping("/list")
    public TableDataInfo list(OperationLog operationLog)
    {
        startPage();
        List<OperationLog> list = operationLogService.selectOperationLogList(operationLog);
        return getDataTable(list);
    }

    /**
     * 导出操作记录列表
     */
    @PreAuthorize("@ss.hasPermi('account:operation:export')")
    @Log(title = "操作记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OperationLog operationLog)
    {
        List<OperationLog> list = operationLogService.selectOperationLogList(operationLog);
        ExcelUtil<OperationLog> util = new ExcelUtil<OperationLog>(OperationLog.class);
        util.exportExcel(response, list, "操作记录数据");
    }

    /**
     * 获取操作记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('account:operation:query')")
    @GetMapping(value = "/{keyId}")
    public AjaxResult getInfo(@PathVariable("keyId") Long keyId)
    {
        return success(operationLogService.selectOperationLogByKeyId(keyId));
    }

    /**
     * 新增操作记录
     */
    @PreAuthorize("@ss.hasPermi('account:operation:add')")
    @Log(title = "操作记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OperationLog operationLog)
    {
        return toAjax(operationLogService.insertOperationLog(operationLog));
    }

    /**
     * 修改操作记录
     */
    @PreAuthorize("@ss.hasPermi('account:operation:edit')")
    @Log(title = "操作记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OperationLog operationLog)
    {
        return toAjax(operationLogService.updateOperationLog(operationLog));
    }

    /**
     * 删除操作记录
     */
    @PreAuthorize("@ss.hasPermi('account:operation:remove')")
    @Log(title = "操作记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{keyIds}")
    public AjaxResult remove(@PathVariable Long[] keyIds)
    {
        return toAjax(operationLogService.deleteOperationLogByKeyIds(keyIds));
    }
}
