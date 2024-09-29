package com.ruoyi.account.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.account.mapper.OperationLogMapper;
import com.ruoyi.account.domain.OperationLog;
import com.ruoyi.account.service.IOperationLogService;

/**
 * 操作记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-09-29
 */
@Service
public class OperationLogServiceImpl implements IOperationLogService 
{
    @Autowired
    private OperationLogMapper operationLogMapper;

    /**
     * 查询操作记录
     * 
     * @param keyId 操作记录主键
     * @return 操作记录
     */
    @Override
    public OperationLog selectOperationLogByKeyId(Long keyId)
    {
        return operationLogMapper.selectOperationLogByKeyId(keyId);
    }

    /**
     * 查询操作记录列表
     * 
     * @param operationLog 操作记录
     * @return 操作记录
     */
    @Override
    public List<OperationLog> selectOperationLogList(OperationLog operationLog)
    {
        return operationLogMapper.selectOperationLogList(operationLog);
    }

    /**
     * 新增操作记录
     * 
     * @param operationLog 操作记录
     * @return 结果
     */
    @Override
    public int insertOperationLog(OperationLog operationLog)
    {
        return operationLogMapper.insertOperationLog(operationLog);
    }

    /**
     * 修改操作记录
     * 
     * @param operationLog 操作记录
     * @return 结果
     */
    @Override
    public int updateOperationLog(OperationLog operationLog)
    {
        return operationLogMapper.updateOperationLog(operationLog);
    }

    /**
     * 批量删除操作记录
     * 
     * @param keyIds 需要删除的操作记录主键
     * @return 结果
     */
    @Override
    public int deleteOperationLogByKeyIds(Long[] keyIds)
    {
        return operationLogMapper.deleteOperationLogByKeyIds(keyIds);
    }

    /**
     * 删除操作记录信息
     * 
     * @param keyId 操作记录主键
     * @return 结果
     */
    @Override
    public int deleteOperationLogByKeyId(Long keyId)
    {
        return operationLogMapper.deleteOperationLogByKeyId(keyId);
    }
}
