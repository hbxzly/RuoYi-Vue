package com.ruoyi.account.service;


import com.ruoyi.account.domain.Email;
import com.ruoyi.account.domain.FbAccountForSell;

import java.util.List;

/**
 * emailService接口
 * 
 * @author ruoyi
 * @date 2024-06-21
 */
public interface IEmailService {

    /**
     * 查询email
     * 
     * @param keyId email主键
     * @return email
     */
    public Email selectEmailByKeyId(Long keyId);

    /**
     * 查询email
     * @param email
     * @return
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
     * 批量删除email
     * 
     * @param keyIds 需要删除的email主键集合
     * @return 结果
     */
    public int deleteEmailByKeyIds(Long[] keyIds);

    /**
     * 删除email信息
     * 
     * @param keyId email主键
     * @return 结果
     */
    public int deleteEmailByKeyId(Long keyId);

    /**
     * 导入email数据
     *
     * @param emailList FB数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importEmail(List<Email> emailList, Boolean isUpdateSupport, String operName);


    /**
     * 获取邮件
     * @param email
     * @return
     */
    public String getMessage(Email email);

    /**
     * 解锁邮箱
     * @param email
     * @return
     */
    public Email unlockEmail(Email email);

    /**
     * 检查邮箱
     * @param email
     */
    String CheckEmail(Email email);

    /**
     * 导入数据
     *
     * @param emailList 数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importFbAccountForSell(List<Email> emailList, Boolean isUpdateSupport, String operName);
}
