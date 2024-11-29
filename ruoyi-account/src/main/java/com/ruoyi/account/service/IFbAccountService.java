package com.ruoyi.account.service;

import com.ruoyi.account.domain.FbAccount;
import org.openqa.selenium.WebDriver;

import java.util.List;

public interface IFbAccountService {

    int countByFbAccount(FbAccount fbAccount);

    /**
     * 删除账号
     * @param fbAccount
     * @return
     */
    int deleteByFbAccount(FbAccount fbAccount);

    /**
     * 添加账号
     * @param fbAccount
     * @return
     */
    int insert(FbAccount fbAccount);

    /**
     * 条件查询
     * @param fbAccount
     * @return
     */
    List<FbAccount> selectByFbAccount(FbAccount fbAccount);

    /**
     * 查询账号列表
     *
     * @param fbAccount 账号
     * @return 账号集合
     */
    public List<FbAccount> selectFbAccountList(FbAccount fbAccount);

    

    /**
     * 查询单个账号
     * @param id
     * @return
     */
    FbAccount selectOneByFbAccountId(String id);

    /**
     * 修改账号
     * @param fbAccount
     * @return
     */
    int updateFbAccount(FbAccount fbAccount);

    /**
     * 添加账号
     * @param fbAccount
     */
    void addFbAccount(FbAccount fbAccount);

    /**
     * 根据ID删除账号
     * @param fbaccountIds
     */
    void deleteFbAccount(String[] fbaccountIds);

    /**
     * 检查所有账号信息
     * @param webDriver
     * @param fbAccount
     */
    String checkFbAccount(WebDriver webDriver, FbAccount fbAccount);

}
