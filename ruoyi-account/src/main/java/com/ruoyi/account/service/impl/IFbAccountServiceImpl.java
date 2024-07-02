package com.ruoyi.account.service.impl;

import com.ruoyi.account.domain.FbAccount;
import com.ruoyi.account.mapper.FbAccountMapper;
import com.ruoyi.account.service.IFbAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class IFbAccountServiceImpl implements IFbAccountService {


    private final FbAccountMapper fbAccountMapper;

    @Autowired
    public IFbAccountServiceImpl(FbAccountMapper fbAccountMapper) {
        this.fbAccountMapper = fbAccountMapper;
    }

    @Override
    public int countByFbAccount(FbAccount fbAccount) {
        return fbAccountMapper.countByFbAccount(fbAccount);
    }

    @Override
    public int deleteByFbAccount(FbAccount fbAccount) {
        return fbAccountMapper.deleteByFbAccount(fbAccount);
    }

    @Override
    public int insert(FbAccount fbAccount) {
        return fbAccountMapper.insert(fbAccount);
    }

    @Override
    public List<FbAccount> selectByFbAccount(FbAccount fbAccount) {
        return fbAccountMapper.selectByFbAccount(fbAccount);
    }

    @Override
    public FbAccount selectOneByFbAccountId(String id) {
        return fbAccountMapper.selectOneByFbAccountId(id);
    }

    @Override
    public int updateFbAccount(FbAccount fbAccount) {
        return fbAccountMapper.updateFbAccount(fbAccount);
    }

    @Override
    public void addFbAccount(FbAccount fbAccount) {
        fbAccountMapper.insert(fbAccount);
    }

    @Override
    public void deleteFbAccount(String[] fbaccountIds) {
        for (String id : fbaccountIds) {
            FbAccount fbAccount = selectOneByFbAccountId(id);
            deleteByFbAccount(fbAccount);

                // 指定要重命名的文件的路径
                String oldFilePath = fbAccount.getDataSource()+fbAccount.getBrowserProfile(); // 旧文件路径
                String newFilePath = fbAccount.getDataSource()+fbAccount.getBrowserProfile()+"待删除"; // 新文件路径

                // 创建 File 对象，表示旧文件
                File oldFile = new File(oldFilePath);

                // 创建 File 对象，表示新文件
                File newFile = new File(newFilePath);

                // 尝试重命名文件
                if (oldFile.exists()) {
                    if (oldFile.renameTo(newFile)) {
                        System.out.println("文件已成功重命名。");
                    } else {
                        System.out.println("无法重命名文件。");
                    }
                } else {
                    System.out.println("旧文件不存在。");
                }


        }
    }
}
