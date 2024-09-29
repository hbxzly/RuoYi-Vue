package com.ruoyi.account.mapper;

import java.util.List;
import com.ruoyi.account.domain.OperationLog;

/**
 * 操作记录Mapper接口
 * 
 * @author ruoyi
 * @date 2024-09-29
 */
public interface OperationLogMapper 
{
    /**
     * 查询操作记录
     * 
     * @param keyId 操作记录主键
     * @return 操作记录
     */
    public OperationLog selectOperationLogByKeyId(Long keyId);

    /**
     * 查询操作记录列表
     * 
     * @param operationLog 操作记录
     * @return 操作记录集合
     */
    public List<OperationLog> selectOperationLogList(OperationLog operationLog);

    /**
     * 新增操作记录
     * 
     * @param operationLog 操作记录
     * @return 结果
     */
    public int insertOperationLog(OperationLog operationLog);

    /**
     * 修改操作记录
     * 
     * @param operationLog 操作记录
     * @return 结果
     */
    public int updateOperationLog(OperationLog operationLog);

    /**
     * 删除操作记录
     * 
     * @param keyId 操作记录主键
     * @return 结果
     */
    public int deleteOperationLogByKeyId(Long keyId);

    /**
     * 批量删除操作记录
     * 
     * @param keyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOperationLogByKeyIds(Long[] keyIds);
}
