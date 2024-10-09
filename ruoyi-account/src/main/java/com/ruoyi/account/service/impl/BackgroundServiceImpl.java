package com.ruoyi.account.service.impl;

import java.util.List;

import com.ruoyi.account.domain.Posts;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
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

    /**
     * 导入数据
     *
     * @param backgroundList  用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importBackground(List<Background> backgroundList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(backgroundList) || backgroundList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Background background : backgroundList) {
            try
            {
                // 验证是否存在这个用户
                Background b = backgroundMapper.selectBackgroundByBackgroundName(background.getBackgroundName());
                if (StringUtils.isNull(b)) {
                    backgroundMapper.insertBackground(background);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、数据 " + background.getBackgroundName() + " 导入成功");
                }
                else if (isUpdateSupport) {
                    backgroundMapper.updateBackground(background);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、数据 " + background.getBackgroundName() + " 更新成功");
                }
                else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、数据 " + background.getBackgroundName() + " 已存在");
                }
            }
            catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、数据 " + background.getBackgroundName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
