package com.ruoyi.web.controller.account;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.account.domain.CreateInfo;
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
import com.ruoyi.account.domain.ProxyIp;
import com.ruoyi.account.service.IProxyIpService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 代理Controller
 * 
 * @author ruoyi
 * @date 2025-02-28
 */
@RestController
@RequestMapping("/account/ip")
public class ProxyIpController extends BaseController
{
    @Autowired
    private IProxyIpService proxyIpService;

    /**
     * 查询代理列表
     */
    @PreAuthorize("@ss.hasPermi('account:ip:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProxyIp proxyIp)
    {
        startPage();
        List<ProxyIp> list = proxyIpService.selectProxyIpList(proxyIp);
        return getDataTable(list);
    }

    /**
     * 导出代理列表
     */
    @PreAuthorize("@ss.hasPermi('account:ip:export')")
    @Log(title = "代理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ProxyIp proxyIp)
    {
        List<ProxyIp> list = proxyIpService.selectProxyIpList(proxyIp);
        ExcelUtil<ProxyIp> util = new ExcelUtil<ProxyIp>(ProxyIp.class);
        util.exportExcel(response, list, "代理数据");
    }

    /**
     * 获取代理详细信息
     */
    @PreAuthorize("@ss.hasPermi('account:ip:query')")
    @GetMapping(value = "/{keyId}")
    public AjaxResult getInfo(@PathVariable("keyId") Long keyId)
    {
        return success(proxyIpService.selectProxyIpByKeyId(keyId));
    }

    /**
     * 新增代理
     */
    @PreAuthorize("@ss.hasPermi('account:ip:add')")
    @Log(title = "代理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProxyIp proxyIp)
    {
        return toAjax(proxyIpService.insertProxyIp(proxyIp));
    }

    /**
     * 修改代理
     */
    @PreAuthorize("@ss.hasPermi('account:ip:edit')")
    @Log(title = "代理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProxyIp proxyIp)
    {
        return toAjax(proxyIpService.updateProxyIp(proxyIp));
    }

    /**
     * 删除代理
     */
    @PreAuthorize("@ss.hasPermi('account:ip:remove')")
    @Log(title = "代理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{keyIds}")
    public AjaxResult remove(@PathVariable Long[] keyIds)
    {
        return toAjax(proxyIpService.deleteProxyIpByKeyIds(keyIds));
    }

    /**
     * 导入模板
     * @param response
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<ProxyIp> util = new ExcelUtil<ProxyIp>(ProxyIp.class);
        util.importTemplateExcel(response, "导入信息");
    }

    /**
     * 导入数据
     * @param file
     * @param updateSupport
     * @return
     * @throws Exception
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<ProxyIp> util = new ExcelUtil<ProxyIp>(ProxyIp.class);
        List<ProxyIp> proxyIpList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = proxyIpService.importCreateInfo(proxyIpList, updateSupport, operName);
        return success(message);
    }
}
