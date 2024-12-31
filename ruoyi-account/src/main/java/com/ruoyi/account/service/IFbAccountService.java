package com.ruoyi.account.service;

import java.util.List;
import com.ruoyi.account.domain.FbAccount;
import com.ruoyi.account.domain.FbAccountForSell;
import com.ruoyi.account.domain.Posts;
import org.openqa.selenium.WebDriver;

/**
 * 账号Service接口
 *
 * @author ruoyi
 * @date 2024-12-12
 */
public interface IFbAccountService {
    /**
     * 查询账号
     *
     * @param keyId 账号主键
     * @return 账号
     */
    public FbAccount selectFbAccountByKeyId(Long keyId);

    /**
     * 批量查询账号
     *
     * @param keyIds 账号主键
     * @return 账号
     */
    public List<FbAccount> selectFbAccountByKeyIds(Long[] keyIds);

    /**
     * 查询账号列表
     *
     * @param fbAccount 账号
     * @return 账号集合
     */
    public List<FbAccount> selectFbAccountList(FbAccount fbAccount);

    /**
     * 新增账号
     *
     * @param fbAccount 账号
     * @return 结果
     */
    public int insertFbAccount(FbAccount fbAccount);

    /**
     * 修改账号
     *
     * @param fbAccount 账号
     * @return 结果
     */
    public int updateFbAccount(FbAccount fbAccount);

    /**
     * 批量删除账号
     *
     * @param keyIds 需要删除的账号主键集合
     * @return 结果
     */
    public int deleteFbAccountByKeyIds(Long[] keyIds);

    /**
     * 删除账号信息
     *
     * @param keyId 账号主键
     * @return 结果
     */
    public int deleteFbAccountByKeyId(Long keyId);


    /**
     * 登录账号
     * @param fbAccount
     * @param webDriver
     * @return
     */
    public boolean login(FbAccount fbAccount, WebDriver webDriver);

    /**
     * 判断账号是否已经登录
     * @param fbAccount
     * @param webDriver
     * @return
     */
    public boolean isLogin(FbAccount fbAccount, WebDriver webDriver);

    /**
     * 添加好友
     * @param fbAccount
     * @param id
     * @param webDriver
     */
    public void addFriend(FbAccount fbAccount,String id, WebDriver webDriver);


    /**
     * 导入数据
     *
     * @param fbAccountList 数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importFbAccountForSell(List<FbAccount> fbAccountList, Boolean isUpdateSupport, String operName);


    /**
     * 检测账号
     * @param fbAccount
     * @param webDriver
     */
    public void checkAccount(FbAccount fbAccount, WebDriver webDriver);

    /**
     * 发贴
     * @param fbAccount
     * @param webDriver
     */
    public void post(FbAccount fbAccount,  WebDriver webDriver);
}
