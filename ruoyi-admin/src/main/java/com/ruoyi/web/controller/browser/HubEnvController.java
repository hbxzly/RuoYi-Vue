package com.ruoyi.web.controller.browser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.ruoyi.browser.domain.HubEnv;
import com.ruoyi.browser.service.IHubEnvService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * Hubstudio 环境Controller
 * 
 * @author ruoyi
 * @date 2026-02-02
 */
@RestController
@RequestMapping("/browser/hubEnv")
public class HubEnvController extends BaseController
{
    @Autowired
    private IHubEnvService hubEnvService;

    /**
     * 查询Hubstudio 环境列表
     */
    @PreAuthorize("@ss.hasPermi('browser:hubEnv:list')")
    @GetMapping("/list")
    public TableDataInfo list(HubEnv hubEnv)
    {
        startPage();
        List<HubEnv> list = hubEnvService.selectHubEnvList(hubEnv);
        return getDataTable(list);
    }

    /**
     * 导出Hubstudio 环境列表
     */
    @PreAuthorize("@ss.hasPermi('browser:hubEnv:export')")
    @Log(title = "Hubstudio 环境", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HubEnv hubEnv)
    {
        List<HubEnv> list = hubEnvService.selectHubEnvList(hubEnv);
        ExcelUtil<HubEnv> util = new ExcelUtil<HubEnv>(HubEnv.class);
        util.exportExcel(response, list, "Hubstudio 环境数据");
    }

    /**
     * 获取Hubstudio 环境详细信息
     */
    @PreAuthorize("@ss.hasPermi('browser:hubEnv:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(hubEnvService.selectHubEnvById(id));
    }

    /**
     * 新增Hubstudio 环境
     */
    @PreAuthorize("@ss.hasPermi('browser:hubEnv:add')")
    @Log(title = "Hubstudio 环境", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HubEnv hubEnv)
    {
        return toAjax(hubEnvService.insertHubEnv(hubEnv));
    }

    /**
     * 修改Hubstudio 环境
     */
    @PreAuthorize("@ss.hasPermi('browser:hubEnv:edit')")
    @Log(title = "Hubstudio 环境", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HubEnv hubEnv)
    {
        return toAjax(hubEnvService.updateHubEnv(hubEnv));
    }

    /**
     * 删除Hubstudio 环境
     */
    @PreAuthorize("@ss.hasPermi('browser:hubEnv:remove')")
    @Log(title = "Hubstudio 环境", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(hubEnvService.deleteHubEnvByIds(ids));
    }

    /**
     * 获取最新列表
     * @return
     */
    @GetMapping("/getHubEnvList")
    public AjaxResult getHubEnvList(){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("current", 1);
        params.put("size", 200);
        List<HubEnv> hubEnvList = hubEnvService.getHubEnvList(params);
        if (hubEnvList.isEmpty()){
            return success("空数据");
        }else {
            for (HubEnv hubEnv : hubEnvList){
                hubEnvService.insertHubEnv(hubEnv);
            }
        }
        return success();
    }
}
