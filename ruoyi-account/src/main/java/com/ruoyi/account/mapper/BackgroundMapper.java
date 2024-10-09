package com.ruoyi.account.mapper;

import java.util.List;
import com.ruoyi.account.domain.Background;

/**
 * 背景图Mapper接口
 * 
 * @author ruoyi
 * @date 2024-09-29
 */
public interface BackgroundMapper 
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
     * 删除背景图
     * 
     * @param keyId 背景图主键
     * @return 结果
     */
    public int deleteBackgroundByKeyId(Long keyId);

    /**
     * 批量删除背景图
     * 
     * @param keyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBackgroundByKeyIds(Long[] keyIds);

    /**
     * 查询背景图
     * @param BackgroundName 背景图名字
     * @return
     */
    public Background selectBackgroundByBackgroundName(String BackgroundName);
}
