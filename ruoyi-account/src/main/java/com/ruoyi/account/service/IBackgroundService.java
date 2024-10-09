package com.ruoyi.account.service;

import java.util.List;
import com.ruoyi.account.domain.Background;

/**
 * 背景图Service接口
 * 
 * @author ruoyi
 * @date 2024-09-29
 */
public interface IBackgroundService 
{
    /**
     * 查询背景图
     * 
     * @param keyId 背景图主键
     * @return 背景图
     */
    public Background selectBackgroundByKeyId(Long keyId);

    /**
     * 查询背景图列表
     * 
     * @param background 背景图
     * @return 背景图集合
     */
    public List<Background> selectBackgroundList(Background background);

    /**
     * 新增背景图
     * 
     * @param background 背景图
     * @return 结果
     */
    public int insertBackground(Background background);

    /**
     * 修改背景图
     * 
     * @param background 背景图
     * @return 结果
     */
    public int updateBackground(Background background);

    /**
     * 批量删除背景图
     * 
     * @param keyIds 需要删除的背景图主键集合
     * @return 结果
     */
    public int deleteBackgroundByKeyIds(Long[] keyIds);

    /**
     * 删除背景图信息
     * 
     * @param keyId 背景图主键
     * @return 结果
     */
    public int deleteBackgroundByKeyId(Long keyId);

    /**
     * 导入数据
     *
     * @param backgroundList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importBackground(List<Background> backgroundList, Boolean isUpdateSupport, String operName);
}
