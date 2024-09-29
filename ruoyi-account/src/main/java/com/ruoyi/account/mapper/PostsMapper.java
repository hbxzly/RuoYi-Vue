package com.ruoyi.account.mapper;

import java.util.List;
import com.ruoyi.account.domain.Posts;

/**
 * 帖子Mapper接口
 * 
 * @author ruoyi
 * @date 2024-09-29
 */
public interface PostsMapper 
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
     * 删除帖子
     * 
     * @param keyId 帖子主键
     * @return 结果
     */
    public int deletePostsByKeyId(Long keyId);

    /**
     * 批量删除帖子
     * 
     * @param keyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePostsByKeyIds(Long[] keyIds);
}
