package com.ruoyi.account.service;

import java.util.List;
import com.ruoyi.account.domain.CreateDevice;
import com.ruoyi.account.domain.CreateInfo;
import com.ruoyi.account.domain.FbAccountForSell;
import io.appium.java_client.AppiumDriver;

/**
 * 创建设备Service接口
 * 
 * @author ruoyi
 * @date 2025-07-15
 */
public interface ICreateDeviceService 
{
    /**
     * 查询创建设备
     * 
     * @param keyId 创建设备主键
     * @return 创建设备
     */
    public CreateDevice selectCreateDeviceByKeyId(Long keyId);

    /**
     * 查询创建设备
     *
     * @param keyIds 创建设备主键
     * @return 创建设备
     */
    public List<CreateDevice> selectCreateDeviceByKeyIds(Long[] keyIds);

    /**
     * 查询创建设备列表
     * 
     * @param createDevice 创建设备
     * @return 创建设备集合
     */
    public List<CreateDevice> selectCreateDeviceList(CreateDevice createDevice);

    /**
     * 通过账号ID查找设备
     * @param id
     * @return
     */
    public CreateDevice selectCreateDeviceByCreateId(String id);

    /**
     * 新增创建设备
     * 
     * @param createDevice 创建设备
     * @return 结果
     */
    public int insertCreateDevice(CreateDevice createDevice);

    /**
     * 修改创建设备
     * 
     * @param createDevice 创建设备
     * @return 结果
     */
    public int updateCreateDevice(CreateDevice createDevice);

    /**
     * 批量删除创建设备
     * 
     * @param keyIds 需要删除的创建设备主键集合
     * @return 结果
     */
    public int deleteCreateDeviceByKeyIds(Long[] keyIds);

    /**
     * 删除创建设备信息
     * 
     * @param keyId 创建设备主键
     * @return 结果
     */
    public int deleteCreateDeviceByKeyId(Long keyId);

    /**
     * 打开APP
     */
    public AppiumDriver openApp(CreateDevice createDevice);


    /**
     * 创建账号
     * @param appiumDriver
     * @param createInfo
     * @return
     */
    public String CreateAccounnt(AppiumDriver appiumDriver, CreateDevice createDevice, CreateInfo createInfo);

    /**
     * 登录账号
     * @param appiumDriver
     * @param fbAccountForSell
     */
    public void loginAccount(AppiumDriver appiumDriver, FbAccountForSell fbAccountForSell);

    /**
     * 搜索没绑定账号的设备
     * @return
     */
    public CreateDevice selectMinNoAccountDevice();
}
