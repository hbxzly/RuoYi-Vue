package com.ruoyi.account.mapper;

import com.ruoyi.account.domain.FbAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FbAccountMapper {

    int countByFbAccount(FbAccount fbAccount);

    int deleteByFbAccount(FbAccount fbAccount);

    FbAccount selectOneByFbAccountId(String id);

    /**
     * 查询账号列表
     *
     * @param fbAccount 账号
     * @return 账号集合
     */
    public List<FbAccount> selectFbAccountList(FbAccount fbAccount);

    int insert(FbAccount fbAccount);

    List<FbAccount> selectByFbAccount(FbAccount fbAccount);

    int updateFbAccount(FbAccount fbAccount);

    int updateBrowserStatus(@Param("fbAccount") FbAccount fbAccount,@Param("status") String status);
}
