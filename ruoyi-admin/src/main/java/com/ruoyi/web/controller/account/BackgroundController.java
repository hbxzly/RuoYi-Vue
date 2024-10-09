package com.ruoyi.web.controller.account;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.account.domain.Avatar;
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
import com.ruoyi.account.domain.Background;
import com.ruoyi.account.service.IBackgroundService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 背景图Controller
 * 
 * @author ruoyi
 * @date 2024-09-29
 */
@RestController
@RequestMapping("/account/background")
public class BackgroundController extends BaseController {
    @Autowired
    private IBackgroundService backgroundService;

    /**
     * 查询背景图列表
     */
    @PreAuthorize("@ss.hasPermi('account:background:list')")
    @GetMapping("/list")
    public TableDataInfo list(Background background) {
        startPage();
        List<Background> list = backgroundService.selectBackgroundList(background);
        return getDataTable(list);
    }

    /**
     * 导出背景图列表
     */
    @PreAuthorize("@ss.hasPermi('account:background:export')")
    @Log(title = "背景图", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Background background) {
        List<Background> list = backgroundService.selectBackgroundList(background);
        ExcelUtil<Background> util = new ExcelUtil<Background>(Background.class);
        util.exportExcel(response, list, "背景图数据");
    }

    /**
     * 获取背景图详细信息
     */
    @PreAuthorize("@ss.hasPermi('account:background:query')")
    @GetMapping(value = "/{keyId}")
    public AjaxResult getInfo(@PathVariable("keyId") Long keyId) {
        return success(backgroundService.selectBackgroundByKeyId(keyId));
    }

    /**
     * 新增背景图
     */
    @PreAuthorize("@ss.hasPermi('account:background:add')")
    @Log(title = "背景图", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Background background) {
        return toAjax(backgroundService.insertBackground(background));
    }

    /**
     * 修改背景图
     */
    @PreAuthorize("@ss.hasPermi('account:background:edit')")
    @Log(title = "背景图", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Background background) {
        return toAjax(backgroundService.updateBackground(background));
    }

    /**
     * 删除背景图
     */
    @PreAuthorize("@ss.hasPermi('account:background:remove')")
    @Log(title = "背景图", businessType = BusinessType.DELETE)
	@DeleteMapping("/{keyIds}")
    public AjaxResult remove(@PathVariable Long[] keyIds) {
        return toAjax(backgroundService.deleteBackgroundByKeyIds(keyIds));
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
        ExcelUtil<Background> util = new ExcelUtil<Background>(Background.class);
        List<Background> backgroundList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = backgroundService.importBackground(backgroundList, updateSupport, operName);
        return success(message);
    }

    /**
     * 导入模板
     * @param response
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<Background> util = new ExcelUtil<Background>(Background.class);
        util.importTemplateExcel(response, "头像数据");
    }
}
