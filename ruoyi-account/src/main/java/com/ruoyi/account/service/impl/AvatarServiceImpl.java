package com.ruoyi.account.service.impl;

import java.util.List;

import com.ruoyi.account.domain.Background;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.account.mapper.AvatarMapper;
import com.ruoyi.account.domain.Avatar;
import com.ruoyi.account.service.IAvatarService;

/**
 * 头像Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-09-29
 */
@Service
public class AvatarServiceImpl implements IAvatarService 
{
    @Autowired
    private AvatarMapper avatarMapper;

    /**
     * 查询头像
     * 
     * @param keyId 头像主键
     * @return 头像
     */
    @Override
    public Avatar selectAvatarByKeyId(Long keyId)
    {
        return avatarMapper.selectAvatarByKeyId(keyId);
    }

    /**
     * 查询头像列表
     * 
     * @param avatar 头像
     * @return 头像
     */
    @Override
    public List<Avatar> selectAvatarList(Avatar avatar)
    {
        return avatarMapper.selectAvatarList(avatar);
    }

    /**
     * 新增头像
     * 
     * @param avatar 头像
     * @return 结果
     */
    @Override
    public int insertAvatar(Avatar avatar)
    {
        return avatarMapper.insertAvatar(avatar);
    }

    /**
     * 修改头像
     * 
     * @param avatar 头像
     * @return 结果
     */
    @Override
    public int updateAvatar(Avatar avatar)
    {
        return avatarMapper.updateAvatar(avatar);
    }

    /**
     * 批量删除头像
     * 
     * @param keyIds 需要删除的头像主键
     * @return 结果
     */
    @Override
    public int deleteAvatarByKeyIds(Long[] keyIds)
    {
        return avatarMapper.deleteAvatarByKeyIds(keyIds);
    }

    /**
     * 删除头像信息
     * 
     * @param keyId 头像主键
     * @return 结果
     */
    @Override
    public int deleteAvatarByKeyId(Long keyId)
    {
        return avatarMapper.deleteAvatarByKeyId(keyId);
    }

    /**
     * 导入数据
     *
     * @param avatarList      数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importAvatar(List<Avatar> avatarList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(avatarList) || avatarList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Avatar avatar : avatarList) {
            try
            {
                // 验证是否存在这个用户
                Avatar a = avatarMapper.selectAvatarByName(avatar.getAvatarName());
                if (StringUtils.isNull(a)) {
                    avatarMapper.insertAvatar(avatar);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、数据 " + avatar.getAvatarName() + " 导入成功");
                }
                else if (isUpdateSupport) {
                    avatarMapper.updateAvatar(avatar);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、数据 " + avatar.getAvatarName() + " 更新成功");
                }
                else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、数据 " + avatar.getAvatarName() + " 已存在");
                }
            }
            catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、数据 " + avatar.getAvatarName() + " 导入失败：";
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
