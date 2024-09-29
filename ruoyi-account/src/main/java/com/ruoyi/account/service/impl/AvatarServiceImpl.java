package com.ruoyi.account.service.impl;

import java.util.List;
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
}
