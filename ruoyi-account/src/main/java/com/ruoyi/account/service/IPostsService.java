package com.ruoyi.account.service;

import java.util.List;
import com.ruoyi.account.domain.Posts;

/**
 * 帖子Service接口
 * 
 * @author ruoyi
 * @date 2024-09-29
 */
public interface IPostsService 
{
    /**
     * 查询帖子
     * 
     * @param keyId 帖子主键
     * @return 帖子
     */
    public Posts selectPostsByKeyId(Long keyId);

    /**
     * 查询帖子列表
     * 
     * @param posts 帖子
     * @return 帖子集合
     */
    public List<Posts> selectPostsList(Posts posts);

    /**
     * 新增帖子
     * 
     * @param posts 帖子
     * @return 结果
     */
    public int insertPosts(Posts posts);

    /**
     * 修改帖子
     * 
     * @param posts 帖子
     * @return 结果
     */
    public int updatePosts(Posts posts);

    /**
     * 批量删除帖子
     * 
     * @param keyIds 需要删除的帖子主键集合
     * @return 结果
     */
    public int deletePostsByKeyIds(Long[] keyIds);

    /**
     * 删除帖子信息
     * 
     * @param keyId 帖子主键
     * @return 结果
     */
    public int deletePostsByKeyId(Long keyId);

    /**
     * 导入数据
     *
     * @param postsList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importPosts(List<Posts> postsList, Boolean isUpdateSupport, String operName);
}
