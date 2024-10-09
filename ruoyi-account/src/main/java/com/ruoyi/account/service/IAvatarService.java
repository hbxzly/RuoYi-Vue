package com.ruoyi.account.service;

import java.util.List;
import com.ruoyi.account.domain.Avatar;

/**
 * 头像Service接口
 * 
 * @author ruoyi
 * @date 2024-09-29
 */
public interface IAvatarService 
{
    /**
     * 查询头像
     * 
     * @param keyId 头像主键
     * @return 头像
     */
    public Avatar selectAvatarByKeyId(Long keyId);

    /**
     * 查询头像列表
     * 
     * @param avatar 头像
     * @return 头像集合
     */
    public List<Avatar> selectAvatarList(Avatar avatar);

    /**
     * 新增头像
     * 
     * @param avatar 头像
     * @return 结果
     */
    public int insertAvatar(Avatar avatar);

    /**
     * 修改头像
     * 
     * @param avatar 头像
     * @return 结果
     */
    public int updateAvatar(Avatar avatar);

    /**
     * 批量删除头像
     * 
     * @param keyIds 需要删除的头像主键集合
     * @return 结果
     */
    public int deleteAvatarByKeyIds(Long[] keyIds);

    /**
     * 删除头像信息
     * 
     * @param keyId 头像主键
     * @return 结果
     */
    public int deleteAvatarByKeyId(Long keyId);

    /**
     * 导入数据
     *
     * @param avatarList 数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importAvatar(List<Avatar> avatarList, Boolean isUpdateSupport, String operName);
}
