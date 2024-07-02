package com.ruoyi.web.controller.account;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.account.domain.Advertise;
import com.ruoyi.account.domain.FbAccount;
import com.ruoyi.account.service.IAdvertiseService;
import com.ruoyi.account.service.IFbAccountService;
import com.ruoyi.account.service.ISeleniumService;
import com.ruoyi.account.util.AccountUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 广告信息Controller
 *
 * @author ruoyi
 * @date 2023-09-25
 */
@RestController
@RequestMapping("/account/advertise")
public class AdvertiseController extends BaseController
{
    @Autowired
    private IAdvertiseService advertiseService;

    @Autowired
    private ISeleniumService iSeleniumService;

    @Autowired
    private IFbAccountService iFbAccountService;

    /**
     * 查询广告投放列表
     */
    @PreAuthorize("@ss.hasPermi('accoun:advertise:list')")
    @GetMapping("/list")
    public TableDataInfo list(Advertise advertise)
    {
        startPage();
        List<Advertise> list = advertiseService.selectAdvertiseList(advertise);
        return getDataTable(list);
    }

    /**
     * 导出广告信息列表
     */
    @PreAuthorize("@ss.hasPermi('accoun:advertise:export')")
    @Log(title = "广告信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Advertise advertise)
    {
        List<Advertise> list = advertiseService.selectAdvertiseList(advertise);
        ExcelUtil<Advertise> util = new ExcelUtil<Advertise>(Advertise.class);
        util.exportExcel(response, list, "广告信息数据");
    }

    /**
     * 获取广告信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('accoun:advertise:query')")
    @GetMapping(value = "/{keyId}")
    public AjaxResult getInfo(@PathVariable("keyId") Long keyId){
        return success(advertiseService.selectAdvertiseByKeyId(keyId));
    }

    /**
     * 新增广告信息
     */
    @PreAuthorize("@ss.hasPermi('accoun:advertise:add')")
    @PostMapping
    public AjaxResult add(@RequestBody String advertise) {
        List<Advertise> advertiseList = AccountUtil.getAdvertise(advertise);
        for (Advertise advertiseTemp:advertiseList) {
            advertiseService.insertAdvertise(advertiseTemp);
        }
        return success();
    }

    /**
     * 修改广告信息
     */
    @PreAuthorize("@ss.hasPermi('accoun:advertise:edit')")
    @Log(title = "广告信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Advertise advertise)
    {
        return toAjax(advertiseService.updateAdvertise(advertise));
    }

    /**
     * 删除广告信息
     */
    @PreAuthorize("@ss.hasPermi('accoun:advertise:remove')")
    @Log(title = "广告信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{keyIds}")
    public AjaxResult remove(@PathVariable Long[] keyIds)
    {
        return toAjax(advertiseService.deleteAdvertiseByKeyIds(keyIds));
    }

    /**
     * 打开广告管理工具
     */
    @PreAuthorize("@ss.hasPermi('accoun:advertise:query')")
    @PostMapping (value = "/openAd/{adAccountId}")
    public AjaxResult openAd(@PathVariable("adAccountId") String adAccountId){
        Advertise advertise = advertiseService.selectAdvertiseByAdAccountId(adAccountId);
        FbAccount fbAccount = iFbAccountService.selectOneByFbAccountId(advertise.getAuthorizedAccount());
        iSeleniumService.openAdvertise(fbAccount, adAccountId);
        return success();
    }

    /**
     * 打开广告截图页面
     * @param keyIds
     * @return
     */
    @PreAuthorize("@ss.hasPermi('accoun:advertise:query')")
    @GetMapping (value = "/multipleOpenScreenshotPage/{keyIds}")
    @ResponseBody
    public AjaxResult multipleOpenScreenshotPage(@PathVariable Long[] keyIds){
       iSeleniumService.openScreenshotPage(keyIds);
       return success();
    }
}
