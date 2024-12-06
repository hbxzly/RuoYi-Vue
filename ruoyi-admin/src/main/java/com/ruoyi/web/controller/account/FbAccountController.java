package com.ruoyi.web.controller.account;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.account.domain.Avatar;
import com.ruoyi.account.domain.Background;
import com.ruoyi.account.domain.FbAccount;
import com.ruoyi.account.domain.Posts;
import com.ruoyi.account.service.*;
import com.ruoyi.account.util.AccountUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 岗位信息操作处理
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/account/fbAccount")
public class FbAccountController extends BaseController {

    @Autowired
    private IFbAccountService fbAccountService;

    @Autowired
    private ISeleniumService seleniumService;

    @Autowired
    private IAvatarService avatarService;

    @Autowired
    private IPostsService postsService;

    @Autowired
    private IBackgroundService backgroundService;


    /**
     * 获取账号列表
     *
     * @param fbAccount
     * @return
     */
    @PreAuthorize("@ss.hasPermi('account:fbAccount:list')")
    @GetMapping("/list")
    @ResponseBody
    public TableDataInfo list(FbAccount fbAccount) {
        startPage();
        List<FbAccount> list = fbAccountService.selectByFbAccount(fbAccount);
        return getDataTable(list);
    }

    /**
     * 根据账户ID获取账号
     *
     * @param id
     * @return
     */
    @PreAuthorize("@ss.hasPermi('account:fbAccount:query')")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public AjaxResult getAccount(@PathVariable String id) {

        return success(fbAccountService.selectOneByFbAccountId(id));
    }


    /**
     * 根据账户ID删除账号
     *
     * @param fbaccountIds
     * @return
     */
    @PreAuthorize("@ss.hasPermi('account:fbAccount:delete')")
    @DeleteMapping(value = "/{fbaccountIds}")
    @ResponseBody
    public AjaxResult getAccount(@PathVariable String[] fbaccountIds) {

        fbAccountService.deleteFbAccount(fbaccountIds);
        return success();
    }

    /**
     * 修改账号
     */
    @PreAuthorize("@ss.hasPermi('account:fbAccount:edit')")
    @Log(title = "账号管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @ResponseBody
    public AjaxResult update(@RequestBody FbAccount fbAccount) {
        return toAjax(fbAccountService.updateFbAccount(fbAccount));
    }

    /**
     * 添加账号
     */
    @PreAuthorize("@ss.hasPermi('account:fbAccount:add')")
    @Log(title = "账号管理", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/add")
    @ResponseBody
    public AjaxResult addAccount(@RequestBody String account) {
        List<FbAccount> fbAccountList = AccountUtil.getFbAccount(account);
        for (FbAccount fbAccount : fbAccountList) {
            if (fbAccountService.selectOneByFbAccountId(fbAccount.getId()) == null) {
                fbAccountService.addFbAccount(fbAccount);
            }
        }
        return success();
    }

    /**
     * 导出账号列表
     */
    @PreAuthorize("@ss.hasPermi('system:account:export')")
    @Log(title = "账号", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FbAccount fbAccount)
    {
        List<FbAccount> list = fbAccountService.selectFbAccountList(fbAccount);
        ExcelUtil<FbAccount> util = new ExcelUtil<FbAccount>(FbAccount.class);
        util.exportExcel(response, list, "账号");
    }


    /**
     * 根据账户ID打开浏览器
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/openBrowser/{id}")
    @ResponseBody
    public AjaxResult openBrowser(@PathVariable String id) {
        FbAccount fbAccount = fbAccountService.selectOneByFbAccountId(id);
        seleniumService.openBrowser(fbAccount);
        FbAccount fb = fbAccountService.selectOneByFbAccountId(id);
        seleniumService.login(fb);
        /*List<FbAccount> fbAccounts = fbAccountService.selectByFbAccount(new FbAccount());
        for (FbAccount fbAccount : fbAccounts) {
                if (fbAccount.getNote() == null || !fbAccount.getNote().contains("成功")) {
                    seleniumService.openBrowser(fbAccount);
                    seleniumService.login(fbAccount);
                    seleniumService.closeBrowser(fbAccount);
                }
        }*/
        return success();
    }

    /**
     * 创BM
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/createBM/{id}")
    @ResponseBody
    public AjaxResult createBM(@PathVariable String id) {
        seleniumService.createBM(fbAccountService.selectOneByFbAccountId(id));
        return success();
    }


    /**
     * 根据账户ID批量打开浏览器
     *
     * @param ids
     * @return
     */
    @GetMapping(value = "/multipleOpenBrowser/{ids}")
    @ResponseBody
    public AjaxResult multipleOpenBrowser(@PathVariable String[] ids) {
        return success(seleniumService.multipleOpenBrowser(ids));
    }

    /**
     * 根据账户ID展示浏览器
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/showBrowser/{id}")
    @ResponseBody
    public AjaxResult showBrowser(@PathVariable String id) {
        seleniumService.showBrowser(fbAccountService.selectOneByFbAccountId(id));
        return success();
    }

    /**
     * 根据账户ID关闭浏览器
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/closeBrowser/{id}")
    @ResponseBody
    public AjaxResult closeBrowser(@PathVariable String id) {
        return success(seleniumService.closeBrowser(fbAccountService.selectOneByFbAccountId(id)));
    }

    /**
     * 关闭所有浏览器
     *
     * @param
     * @return
     */
    @GetMapping(value = "/closeAllBrowser")
    @ResponseBody
    public AjaxResult closeALLBrowser() {
        return success(seleniumService.closeAllBrowser());
    }


    /**
     * 更多操作
     *
     * @param data
     * @return
     */
    @PostMapping(value = "/moreOperate/")
    @ResponseBody
    public AjaxResult moreOperate(@RequestBody String data) {

        // 创建ObjectMapper对象
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(data);
            String operateId = jsonNode.get("operateId").asText();
            JsonNode ids = jsonNode.get("ids");
            ArrayList<String> idList = new ArrayList<>();
            for (JsonNode id : ids) {
                idList.add(id.asText());
            }
            String[] idArray = idList.toArray(new String[0]);
            switch (operateId) {
                case "0"://查看账号品质
                    seleniumService.checkAccount(idArray);
                    break;
                case "1"://修改浏览器用户名
                    for (String id : idArray) {
                        seleniumService.updateBrowserProfile(id);
                    }
                    break;
                case "2"://获取用户名
                    seleniumService.getAccountName(idArray);
                    break;
                case "3"://解限一步
                    seleniumService.unlimitedAccountOneStep(idArray);
                    break;
                case "4"://解限二步

                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return success();
    }

    /**
     * 查看BM
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/checkBM/")
    @ResponseBody
    public AjaxResult checkBM(@RequestBody String[] ids) {
        seleniumService.checkBM(ids);
        return success();
    }

    /**
     * 查看账号品质
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/checkAccount/")
    @ResponseBody
    public AjaxResult checkAccount(@RequestBody String[] ids) {
        seleniumService.checkAccount(ids);
        return success();
    }

    /**
     * 收集广告信息
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/collectAdsInfo/{id}")
    @ResponseBody
    public AjaxResult collectAdsInfo(@PathVariable String id) {
        seleniumService.loadAdAccountInfo(id);
        return success();
    }

    /**
     * 打开广告管理工具
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/openAds/{id}")
    @ResponseBody
    public AjaxResult openAds(@PathVariable String id) {
        seleniumService.openAds(id);
        return success();
    }


    /**
     * 批量添加好友
     *
     * @param data
     * @return
     */
    @PostMapping(value = "/batchAddFriend/")
    @ResponseBody
    public AjaxResult batchAddFriend(@RequestBody String data) {

        // 创建ObjectMapper对象
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(data);
            // 获取operationAccount数组
            JsonNode operationAccountsNode = jsonNode.get("operationAccount");
            ArrayList<String> operationAccountList = new ArrayList<>();
            for (JsonNode account : operationAccountsNode) {
                operationAccountList.add(account.asText());
            }
            String[] operationAccountArray = operationAccountList.toArray(new String[0]);
            JsonNode friendAccount = jsonNode.get("friendAccount");
            String[] friendAccountArray = friendAccount.asText().split("\\n");
            for (String operationAccountId : operationAccountArray) {
                seleniumService.batchAddFriend(operationAccountId,friendAccountArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success();
    }


    /**
     * 检查账号状态信息
     *
     * @param ids
     * @return
     */
    @GetMapping(value = "/checkAccountInfo/{ids}")
    @ResponseBody
    public AjaxResult checkAccountInfo(@PathVariable String[] ids) {
        for (String id : ids){
          seleniumService.checkAccountInfo(fbAccountService.selectOneByFbAccountId(id));
        }
        return success();
    }


    /**
     * 修改密码
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/changePassword/{id}")
    @ResponseBody
    public AjaxResult changePassword(@PathVariable String id) {
        FbAccount fbAccount = fbAccountService.selectOneByFbAccountId(id);
        FbAccount account = seleniumService.changePassword(fbAccount);
        account.setBrowserStatus("1");
        fbAccountService.updateFbAccount(account);
        return success();
    }

    /**
     * 解锁
     * @param id
     * @return
     */
    @GetMapping(value = "/unlockAccount/{id}")
    @ResponseBody
    public AjaxResult unlockAccount(@PathVariable String id) {
        FbAccount fbAccount = fbAccountService.selectOneByFbAccountId(id);
        seleniumService.unlockAccount(fbAccount);
        return success();
    }

    /**
     * 创建主页
     * @return
     */
    @PostMapping(value = "/createPage/")
    @ResponseBody
    public AjaxResult createPage(@RequestBody Map<String, Object> data){

        List<String> ids = (List<String>) data.get("ids");
        List<String> pageNames = (List<String>) data.get("pageName");
        List<Avatar> avatarList = avatarService.selectAvatarList(new Avatar());
        List<Background> backgroundList = backgroundService.selectBackgroundList(new Background());
        List<Posts> postsList = postsService.selectPostsList(new Posts());


        // 创建一个新的Map用于存储键值对
        Map<String, String> idPageMap = new HashMap<>();

        // 将ids和pageNames合并成键值对
        for (int i = 0; i < ids.size(); i++) {
            idPageMap.put(ids.get(i), pageNames.get(i));
        }

        // 使用 forEach 遍历 idPageMap
        idPageMap.forEach((id, pageName) -> {
            Random random = new Random();
            int randomAvatarIndex = random.nextInt(avatarList.size());
            Avatar randomAvatar = avatarList.get(randomAvatarIndex);
            avatarList.remove(randomAvatarIndex);
            int randomBackgroundIndex = random.nextInt(backgroundList.size());
            Background randomBackground = backgroundList.get(randomBackgroundIndex);
            backgroundList.remove(randomBackgroundIndex);
            List<Posts> selectedPosts = new ArrayList<>();
            if (postsList.size() >= 3) {
                Collections.shuffle(postsList);
                selectedPosts = new ArrayList<>(postsList.subList(0, 3));
            } else {
                selectedPosts = new ArrayList<>(postsList); // 如果少于 3 个，返回所有
            }
            FbAccount fbAccount = fbAccountService.selectOneByFbAccountId(id);
            try {
                seleniumService.createPage(fbAccount, pageName, randomAvatar, randomBackground, selectedPosts);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        return success();
    }

    /**
     * 检测
     */
    @GetMapping("/checkFbAccountInfo/{ids}")
    @ResponseBody
    public AjaxResult checkFbAccountInfo(@PathVariable String[] ids) {
        for (String id : ids) {
            FbAccount fbAccount = fbAccountService.selectOneByFbAccountId(id);
            WebDriver webDriver = seleniumService.openBrowser(fbAccount);
            fbAccountService.checkFbAccount(webDriver,fbAccount);
            seleniumService.closeBrowser(fbAccount);
        }
        return success();
    }

}
