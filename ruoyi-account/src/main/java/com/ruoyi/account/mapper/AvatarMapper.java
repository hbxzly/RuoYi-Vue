package com.ruoyi.account.mapper;

import java.util.List;
import com.ruoyi.account.domain.Avatar;

/**
 * 头像Mapper接口
 * 
 * @author ruoyi
 * @date 2024-09-29
 */
public interface AvatarMapper 
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
     * 删除头像
     * 
     * @param keyId 头像主键
     * @return 结果
     */
    public int deleteAvatarByKeyId(Long keyId);

    /**
     * 批量删除头像
     * 
     * @param keyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAvatarByKeyIds(Long[] keyIds);

    /**
     * 查询头像
     * @param avatarName 头像名称
     * @return
     */
    public Avatar selectAvatarByName(String avatarName);
}
