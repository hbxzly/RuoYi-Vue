package com.ruoyi.account.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
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

    /**
     * 导入数据
     *
     * @param postsList       用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importPosts(List<Posts> postsList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(postsList) || postsList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Posts posts : postsList) {
            try
            {
                // 验证是否存在这个用户
                Posts p = postsMapper.selectPostsByContent(posts.getContent());
                if (StringUtils.isNull(p)) {
                    postsMapper.insertPosts(posts);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、数据 " + posts.getPictureName() + " 导入成功");
                }
                else if (isUpdateSupport) {
                    postsMapper.updatePosts(posts);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、数据 " + posts.getPictureName() + " 更新成功");
                }
                else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、数据 " + posts.getPictureName() + " 已存在");
                }
            }
            catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、数据 " + posts.getPictureName() + " 导入失败：";
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
