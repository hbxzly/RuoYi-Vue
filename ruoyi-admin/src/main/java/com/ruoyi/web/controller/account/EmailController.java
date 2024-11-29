package com.ruoyi.web.controller.account;

import com.ruoyi.account.domain.Email;
import com.ruoyi.account.service.IEmailService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * emailController
 * 
 * @author ruoyi
 * @date 2024-06-21
 */
@RestController
@RequestMapping("/account/email")
public class EmailController extends BaseController
{
    @Autowired
    private IEmailService emailService;

    /**
     * 查询email列表
     */
    @PreAuthorize("@ss.hasPermi('account:email:list')")
    @GetMapping("/list")
    public TableDataInfo list(Email email)
    {
        startPage();
        List<Email> list = emailService.selectEmailList(email);
        return getDataTable(list);
    }

    /**
     * 导出email列表
     */
    @PreAuthorize("@ss.hasPermi('account:email:export')")
    @Log(title = "email", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Email email)
    {
        List<Email> list = emailService.selectEmailList(email);
        ExcelUtil<Email> util = new ExcelUtil<Email>(Email.class);
        util.exportExcel(response, list, "email数据");
    }

    /**
     * 获取email详细信息
     */
    @PreAuthorize("@ss.hasPermi('account:email:query')")
    @GetMapping(value = "/{keyId}")
    public AjaxResult getInfo(@PathVariable("keyId") Long keyId)
    {
        return success(emailService.selectEmailByKeyId(keyId));
    }


    /**
     * 导入数据
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<Email> util = new ExcelUtil<>(Email.class);
        List<Email> emailList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = emailService.importEmail(emailList, updateSupport, operName);
        return success(message);
    }

    /**
     * 模板下载
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<Email> util = new ExcelUtil<>(Email.class);
        util.importTemplateExcel(response, "email数据");
    }

    /**
     * 新增email
     */
    @PreAuthorize("@ss.hasPermi('account:email:add')")
    @Log(title = "email", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Email email)
    {
        return toAjax(emailService.insertEmail(email));
    }

    /**
     * 修改email
     */
    @PreAuthorize("@ss.hasPermi('account:email:edit')")
    @Log(title = "email", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Email email) {
        return toAjax(emailService.updateEmail(email));
    }

    /**
     * 删除email
     */
    @PreAuthorize("@ss.hasPermi('account:email:remove')")
    @Log(title = "email", businessType = BusinessType.DELETE)
	@DeleteMapping("/{keyIds}")
    public AjaxResult remove(@PathVariable Long[] keyIds) {
        return toAjax(emailService.deleteEmailByKeyIds(keyIds));
    }

    /**
     * 获取email
     */
    @PreAuthorize("@ss.hasPermi('account:email:edit')")
    @PutMapping("/getMessage")
    public AjaxResult getMessage(@RequestBody Email email) {
//        String message = emailService.getMessage(email);
//        email.setLastTimeMessage(message);
//        String mailMessage = EmailUtil.getMessage(email);
//        email.setLastTimeMessage(mailMessage);
//        emailService.updateEmail(email);
        return success();
    }

    /**
     * 获取email
     */
    @PreAuthorize("@ss.hasPermi('account:email:edit')")
    @GetMapping("/unlockEmail/{keyIds}")
    @ResponseBody
    public AjaxResult unlockEmail(@PathVariable Long[] keyIds) {
        for (Long keyId : keyIds) {
            Email email = emailService.selectEmailByKeyId(keyId);
            emailService.unlockEmail(email);
        }
        return success();
    }
}
