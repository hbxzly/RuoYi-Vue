package com.ruoyi.account.mapper;

import java.util.List;
import com.ruoyi.account.domain.CreateDevice;

/**
 * 创建设备Mapper接口
 * 
 * @author ruoyi
 * @date 2025-07-15
 */
public interface CreateDeviceMapper 
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
     * 通过账号ID查找设备
     * @param id
     * @return
     */
    public CreateDevice selectCreateDeviceByCreateId(String id);


    /**
     * 查询创建设备列表
     * 
     * @param createDevice 创建设备
     * @return 创建设备集合
     */
    public List<CreateDevice> selectCreateDeviceList(CreateDevice createDevice);

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
     * 删除创建设备
     * 
     * @param keyId 创建设备主键
     * @return 结果
     */
    public int deleteCreateDeviceByKeyId(Long keyId);

    /**
     * 批量删除创建设备
     * 
     * @param keyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCreateDeviceByKeyIds(Long[] keyIds);

    /**
     * 搜索没绑定账号设备
     * @return
     */
    public CreateDevice selectMinNoAccountDevice();
}
