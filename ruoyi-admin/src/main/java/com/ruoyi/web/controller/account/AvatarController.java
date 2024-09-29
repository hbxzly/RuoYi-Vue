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
import com.ruoyi.account.domain.Avatar;
import com.ruoyi.account.service.IAvatarService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 头像Controller
 * 
 * @author ruoyi
 * @date 2024-09-29
 */
@RestController
@RequestMapping("/account/avatar")
public class AvatarController extends BaseController
{
    @Autowired
    private IAvatarService avatarService;

    /**
     * 查询头像列表
     */
    @PreAuthorize("@ss.hasPermi('account:avatar:list')")
    @GetMapping("/list")
    public TableDataInfo list(Avatar avatar)
    {
        startPage();
        List<Avatar> list = avatarService.selectAvatarList(avatar);
        return getDataTable(list);
    }

    /**
     * 导出头像列表
     */
    @PreAuthorize("@ss.hasPermi('account:avatar:export')")
    @Log(title = "头像", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Avatar avatar)
    {
        List<Avatar> list = avatarService.selectAvatarList(avatar);
        ExcelUtil<Avatar> util = new ExcelUtil<Avatar>(Avatar.class);
        util.exportExcel(response, list, "头像数据");
    }

    /**
     * 获取头像详细信息
     */
    @PreAuthorize("@ss.hasPermi('account:avatar:query')")
    @GetMapping(value = "/{keyId}")
    public AjaxResult getInfo(@PathVariable("keyId") Long keyId)
    {
        return success(avatarService.selectAvatarByKeyId(keyId));
    }

    /**
     * 新增头像
     */
    @PreAuthorize("@ss.hasPermi('account:avatar:add')")
    @Log(title = "头像", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Avatar avatar)
    {
        return toAjax(avatarService.insertAvatar(avatar));
    }

    /**
     * 修改头像
     */
    @PreAuthorize("@ss.hasPermi('account:avatar:edit')")
    @Log(title = "头像", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Avatar avatar)
    {
        return toAjax(avatarService.updateAvatar(avatar));
    }

    /**
     * 删除头像
     */
    @PreAuthorize("@ss.hasPermi('account:avatar:remove')")
    @Log(title = "头像", businessType = BusinessType.DELETE)
	@DeleteMapping("/{keyIds}")
    public AjaxResult remove(@PathVariable Long[] keyIds)
    {
        return toAjax(avatarService.deleteAvatarByKeyIds(keyIds));
    }
}
