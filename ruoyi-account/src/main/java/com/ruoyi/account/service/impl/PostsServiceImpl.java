package com.ruoyi.account.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.account.mapper.PostsMapper;
import com.ruoyi.account.domain.Posts;
import com.ruoyi.account.service.IPostsService;

/**
 * 帖子Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-09-29
 */
@Service
public class PostsServiceImpl implements IPostsService 
{
    @Autowired
    private PostsMapper postsMapper;

    /**
     * 查询帖子
     * 
     * @param keyId 帖子主键
     * @return 帖子
     */
    @Override
    public Posts selectPostsByKeyId(Long keyId)
    {
        return postsMapper.selectPostsByKeyId(keyId);
    }

    /**
     * 查询帖子列表
     * 
     * @param posts 帖子
     * @return 帖子
     */
    @Override
    public List<Posts> selectPostsList(Posts posts)
    {
        return postsMapper.selectPostsList(posts);
    }

    /**
     * 新增帖子
     * 
     * @param posts 帖子
     * @return 结果
     */
    @Override
    public int insertPosts(Posts posts)
    {
        return postsMapper.insertPosts(posts);
    }

    /**
     * 修改帖子
     * 
     * @param posts 帖子
     * @return 结果
     */
    @Override
    public int updatePosts(Posts posts)
    {
        return postsMapper.updatePosts(posts);
    }

    /**
     * 批量删除帖子
     * 
     * @param keyIds 需要删除的帖子主键
     * @return 结果
     */
    @Override
    public int deletePostsByKeyIds(Long[] keyIds)
    {
        return postsMapper.deletePostsByKeyIds(keyIds);
    }

    /**
     * 删除帖子信息
     * 
     * @param keyId 帖子主键
     * @return 结果
     */
    @Override
    public int deletePostsByKeyId(Long keyId)
    {
        return postsMapper.deletePostsByKeyId(keyId);
    }
}
