package com.ruoyi.account.service;

import java.util.List;
import com.ruoyi.account.domain.CreateInfo;
import com.ruoyi.account.domain.FbAccountForSell;
import org.openqa.selenium.WebDriver;

/**
 * 创建信息Service接口
 * 
 * @author ruoyi
 * @date 2025-01-08
 */
public interface ICreateInfoService {
    /**
     * 查询创建信息
     *
     * @param keyId 创建信息主键
     * @return 创建信息
     */
    public CreateInfo selectCreateInfoByKeyId(Long keyId);

    /**
     * 查询创建信息列表
     *
     * @param createInfo 创建信息
     * @return 创建信息集合
     */
    public List<CreateInfo> selectCreateInfoList(CreateInfo createInfo);

    /**
     * 新增创建信息
     *
     * @param createInfo 创建信息
     * @return 结果
     */
    public int insertCreateInfo(CreateInfo createInfo);

    /**
     * 修改创建信息
     *
     * @param createInfo 创建信息
     * @return 结果
     */
    public int updateCreateInfo(CreateInfo createInfo);

    /**
     * 批量删除创建信息
     *
     * @param keyIds 需要删除的创建信息主键集合
     * @return 结果
     */
    public int deleteCreateInfoByKeyIds(Long[] keyIds);

    /**
     * 删除创建信息信息
     *
     * @param keyId 创建信息主键
     * @return 结果
     */
    public int deleteCreateInfoByKeyId(Long keyId);

    /**
     * 导入数据
     *
     * @param createInfoList  数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    public String importCreateInfo(List<CreateInfo> createInfoList, Boolean isUpdateSupport, String operName);


    /**
     * 通过OE创建账号
     *
     * @param webDriver
     * @param createInfo
     * @throws InterruptedException
     */
    public void createAccountByOE(WebDriver webDriver, CreateInfo createInfo) throws InterruptedException;

    /**
     * 登录
     *
     * @param webDriver
     * @param createInfo
     * @return
     */
    public String login(WebDriver webDriver, CreateInfo createInfo);

    /**
     * 检查是否登录
     *
     * @param createInfo
     * @param webDriver
     * @return
     */
    public boolean isLogin(CreateInfo createInfo, WebDriver webDriver);

    /**
     * 打开浏览器
     *
     * @param createInfo
     * @return
     */
    public WebDriver openBrowser(CreateInfo createInfo);

    /**
     * 关闭浏览器
     *
     * @param createInfo
     */
    public void closeBrowser(CreateInfo createInfo);

    /**
     * 换头像
     * @param webDriver
     * @param createInfo
     * @return
     */
    public String updateAccountAvatar(WebDriver webDriver, CreateInfo createInfo);

    /**
     * 添加邮箱
     * @param webDriver
     * @param createInfo
     * @return
     */
    public String updateAccountAddEmail(WebDriver webDriver, CreateInfo createInfo);

    /**
     * 开启双重验证
     * @param webDriver
     * @param createInfo
     * @return
     */
    public String updateAccountOpenTwoFactor(WebDriver webDriver, CreateInfo createInfo);

}
