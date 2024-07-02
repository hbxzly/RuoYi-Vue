package com.ruoyi.account.mapper;

import com.ruoyi.account.domain.FbAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FbAccountMapper {

    int countByFbAccount(FbAccount fbAccount);

    int deleteByFbAccount(FbAccount fbAccount);

    FbAccount selectOneByFbAccountId(String id);

    int insert(FbAccount fbAccount);

    List<FbAccount> selectByFbAccount(FbAccount fbAccount);

    int updateFbAccount(FbAccount fbAccount);

    int updateBrowserStatus(@Param("fbAccount") FbAccount fbAccount,@Param("status") String status);
}
