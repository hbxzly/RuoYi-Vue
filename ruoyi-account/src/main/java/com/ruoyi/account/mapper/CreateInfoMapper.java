package com.ruoyi.account.mapper;

import java.util.List;
import com.ruoyi.account.domain.CreateInfo;

/**
 * 创建信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-01-08
 */
public interface CreateInfoMapper 
{
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
     * 删除创建信息
     * 
     * @param keyId 创建信息主键
     * @return 结果
     */
    public int deleteCreateInfoByKeyId(Long keyId);

    /**
     * 批量删除创建信息
     * 
     * @param keyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCreateInfoByKeyIds(Long[] keyIds);

    /**
     * 查询创建信息
     * @param password 密码
     * @return
     */
    public  CreateInfo selectCreateInfoByPassword(String password);
}
