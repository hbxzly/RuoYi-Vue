package com.ruoyi.account.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.account.mapper.BackgroundMapper;
import com.ruoyi.account.domain.Background;
import com.ruoyi.account.service.IBackgroundService;

/**
 * 背景图Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-09-29
 */
@Service
public class BackgroundServiceImpl implements IBackgroundService 
{
    @Autowired
    private BackgroundMapper backgroundMapper;

    /**
     * 查询背景图
     * 
     * @param keyId 背景图主键
     * @return 背景图
     */
    @Override
    public Background selectBackgroundByKeyId(Long keyId)
    {
        return backgroundMapper.selectBackgroundByKeyId(keyId);
    }

    /**
     * 查询背景图列表
     * 
     * @param background 背景图
     * @return 背景图
     */
    @Override
    public List<Background> selectBackgroundList(Background background)
    {
        return backgroundMapper.selectBackgroundList(background);
    }

    /**
     * 新增背景图
     * 
     * @param background 背景图
     * @return 结果
     */
    @Override
    public int insertBackground(Background background)
    {
        return backgroundMapper.insertBackground(background);
    }

    /**
     * 修改背景图
     * 
     * @param background 背景图
     * @return 结果
     */
    @Override
    public int updateBackground(Background background)
    {
        return backgroundMapper.updateBackground(background);
    }

    /**
     * 批量删除背景图
     * 
     * @param keyIds 需要删除的背景图主键
     * @return 结果
     */
    @Override
    public int deleteBackgroundByKeyIds(Long[] keyIds)
    {
        return backgroundMapper.deleteBackgroundByKeyIds(keyIds);
    }

    /**
     * 删除背景图信息
     * 
     * @param keyId 背景图主键
     * @return 结果
     */
    @Override
    public int deleteBackgroundByKeyId(Long keyId)
    {
        return backgroundMapper.deleteBackgroundByKeyId(keyId);
    }
}
