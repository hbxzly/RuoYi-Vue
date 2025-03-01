package com.ruoyi.web.controller.account;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.account.domain.FbAccountForSell;
import com.ruoyi.account.util.FBAccountUtil;
import org.openqa.selenium.WebDriver;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.account.domain.CreateInfo;
import com.ruoyi.account.service.ICreateInfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 创建信息Controller
 * 
 * @author ruoyi
 * @date 2025-01-08
 */
@RestController
@RequestMapping("/account/info")
public class CreateInfoController extends BaseController
{
    @Autowired
    private ICreateInfoService createInfoService;

    /**
     * 查询创建信息列表
     */
    @PreAuthorize("@ss.hasPermi('account:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(CreateInfo createInfo)
    {
        startPage();
        List<CreateInfo> list = createInfoService.selectCreateInfoList(createInfo);
        return getDataTable(list);
    }

    /**
     * 导出创建信息列表
     */
    @PreAuthorize("@ss.hasPermi('account:info:export')")
    @Log(title = "创建信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CreateInfo createInfo)
    {
        List<CreateInfo> list = createInfoService.selectCreateInfoList(createInfo);
        ExcelUtil<CreateInfo> util = new ExcelUtil<CreateInfo>(CreateInfo.class);
        util.exportExcel(response, list, "创建信息数据");
    }

    /**
     * 获取创建信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('account:info:query')")
    @GetMapping(value = "/{keyId}")
    public AjaxResult getInfo(@PathVariable("keyId") Long keyId)
    {
        return success(createInfoService.selectCreateInfoByKeyId(keyId));
    }

    /**
     * 新增创建信息
     */
    @PreAuthorize("@ss.hasPermi('account:info:add')")
    @Log(title = "创建信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CreateInfo createInfo)
    {
        return toAjax(createInfoService.insertCreateInfo(createInfo));
    }

    /**
     * 修改创建信息
     */
    @PreAuthorize("@ss.hasPermi('account:info:edit')")
    @Log(title = "创建信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CreateInfo createInfo)
    {
        return toAjax(createInfoService.updateCreateInfo(createInfo));
    }

    /**
     * 删除创建信息
     */
    @PreAuthorize("@ss.hasPermi('account:info:remove')")
    @Log(title = "创建信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{keyIds}")
    public AjaxResult remove(@PathVariable Long[] keyIds)
    {
        return toAjax(createInfoService.deleteCreateInfoByKeyIds(keyIds));
    }

    /**
     * 导入模板
     * @param response
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<CreateInfo> util = new ExcelUtil<CreateInfo>(CreateInfo.class);
        util.importTemplateExcel(response, "创号信息");
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
        ExcelUtil<CreateInfo> util = new ExcelUtil<CreateInfo>(CreateInfo.class);
        List<CreateInfo> createInfoList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = createInfoService.importCreateInfo(createInfoList, updateSupport, operName);
        return success(message);
    }

    @GetMapping("/openBrowser/{keyIds}")
    @ResponseBody
    public AjaxResult openBrowser(@PathVariable Long[] keyIds){
        for (Long keyId : keyIds) {
            CreateInfo createInfo = createInfoService.selectCreateInfoByKeyId(keyId);
            WebDriver webDriver = createInfoService.openBrowser(createInfo);
            createInfoService.login(webDriver, createInfo);
        }
        return success();
    }

    @PostMapping("/updateAccountInfo")
    @ResponseBody
    public AjaxResult updateAccountInfo(@RequestBody Map<String, Object> payload){
        // 从请求体中解析 keyIds 和 selectedOptions
        List<Integer> keyIds = (List<Integer>) payload.get("keyIds");
        List<String> selectedOptions = (List<String>) payload.get("selectedOptions");

        for (Integer keyId : keyIds) {
            CreateInfo createInfo = createInfoService.selectCreateInfoByKeyId(Long.valueOf(keyId));
            WebDriver webDriver = createInfoService.openBrowser(createInfo);
            createInfoService.login(webDriver, createInfo);
            if (selectedOptions.contains("上传头像")) {
                createInfoService.updateAccountAvatar(webDriver, createInfo);
            }
            if (selectedOptions.contains("添加邮箱")){
                createInfoService.updateAccountAddEmail(webDriver, createInfo);
            }
            if (selectedOptions.contains("开启双重验证")){
                createInfoService.updateAccountOpenTwoFactor(webDriver, createInfo);
            }
            createInfoService.closeBrowser(createInfo);
        }

        return success();
    }

    @GetMapping("/create/{keyIds}")
    @ResponseBody
    public AjaxResult create(@PathVariable Long[] keyIds){
        for (Long keyId:keyIds){
            CreateInfo createInfo = createInfoService.selectCreateInfoByKeyId(keyId);
            WebDriver webDriver = createInfoService.openBrowser(createInfo);
            try {
                createInfoService.createAccountByOE(webDriver,createInfo);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return success();
    }

    @GetMapping("/checkAccountActive/{keyIds}")
    @ResponseBody
    public AjaxResult checkAccountActive(@PathVariable Long[] keyIds){
        // 创建固定大小的线程池（可以根据 CPU 核心数调整）
        int threadCount = Runtime.getRuntime().availableProcessors(); // 获取 CPU 核心数
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        for (Long keyId:keyIds){
            executor.submit(() -> {  // 提交任务到线程池
                try {
                    CreateInfo createInfo = createInfoService.selectCreateInfoByKeyId(keyId);
                    boolean b = FBAccountUtil.checkAccountActive(String.valueOf(createInfo.getId()));
                    if (!b) {
                        createInfo.setCreateStatus("被锁/被封");
                        createInfoService.updateCreateInfo(createInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        return success();
    }
}
