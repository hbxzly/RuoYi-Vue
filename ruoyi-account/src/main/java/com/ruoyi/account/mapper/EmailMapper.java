package com.ruoyi.account.mapper;



import com.ruoyi.account.domain.Email;

import java.util.List;

/**
 * emailMapper接口
 *
 * @author ruoyi
 * @date 2024-06-21
 */
public interface EmailMapper
{
    /**
     * 查询email
     *
     * @param keyId email主键
     * @return email
     */
    public Email selectEmailByKeyId(Long keyId);

    /**
     * 查询email
     *
     * @param email
     * @return email
     */
    public Email selectEmailByEmail(String email);

    /**
     * 查询email列表
     *
     * @param email email
     * @return email集合
     */
    public List<Email> selectEmailList(Email email);

    /**
     * 新增email
     *
     * @param email email
     * @return 结果
     */
    public int insertEmail(Email email);

    /**
     * 修改email
     *
     * @param email email
     * @return 结果
     */
    public int updateEmail(Email email);

    /**
     * 删除email
     *
     * @param keyId email主键
     * @return 结果
     */
    public int deleteEmailByKeyId(Long keyId);

    /**
     * 批量删除email
     *
     * @param keyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEmailByKeyIds(Long[] keyIds);

    
}
