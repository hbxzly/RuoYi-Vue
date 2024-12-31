package com.ruoyi.account.mapper;

import com.ruoyi.account.domain.FbAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FbAccountMapper {

    /**
     * 查询账号
     *
     * @param keyId 账号主键
     * @return 账号
     */
    public FbAccount selectFbAccountByKeyId(Long keyId);

    /**
     * 查询账号
     * @param id
     * @return
     */
    FbAccount selectFbAccountById(String id);

    /**
     * 批量查询账号
     * @param keyIds 账号主键
     * @return 账号集合
     */
    public List<FbAccount> selectFbAccountByKeyIds(Long[] keyIds);

    /**
     * 查询账号列表
     *
     * @param fbAccount 账号
     * @return 账号集合
     */
    public List<FbAccount> selectFbAccountList(FbAccount fbAccount);

    /**
     * 新增账号
     *
     * @param fbAccount 账号
     * @return 结果
     */
    public int insertFbAccount(FbAccount fbAccount);

    /**
     * 修改账号
     *
     * @param fbAccount 账号
     * @return 结果
     */
    public int updateFbAccount(FbAccount fbAccount);

    /**
     * 删除账号
     *
     * @param keyId 账号主键
     * @return 结果
     */
    public int deleteFbAccountByKeyId(Long keyId);

    /**
     * 批量删除账号
     *
     * @param keyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFbAccountByKeyIds(Long[] keyIds);

    int countByFbAccount(FbAccount fbAccount);

    int deleteByFbAccount(FbAccount fbAccount);


    int insert(FbAccount fbAccount);

    List<FbAccount> selectByFbAccount(FbAccount fbAccount);

    int updateBrowserStatus(@Param("fbAccount") FbAccount fbAccount,@Param("status") String status);
}
