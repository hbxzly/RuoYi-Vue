package com.ruoyi.account.mapper;

import java.util.List;
import com.ruoyi.account.domain.FbAccountForSell;

/**
 * 卖号Mapper接口
 * 
 * @author ruoyi
 * @date 2024-09-20
 */
public interface FbAccountForSellMapper 
{
    /**
     * 查询卖号
     * 
     * @param keyId 卖号主键
     * @return 卖号
     */
    public FbAccountForSell selectFbAccountForSellByKeyId(Long keyId);

    /**
     * 查询卖号列表
     * 
     * @param fbAccountForSell 卖号
     * @return 卖号集合
     */
    public List<FbAccountForSell> selectFbAccountForSellList(FbAccountForSell fbAccountForSell);

    /**
     * 新增卖号
     * 
     * @param fbAccountForSell 卖号
     * @return 结果
     */
    public int insertFbAccountForSell(FbAccountForSell fbAccountForSell);

    /**
     * 修改卖号
     * 
     * @param fbAccountForSell 卖号
     * @return 结果
     */
    public int updateFbAccountForSell(FbAccountForSell fbAccountForSell);

    /**
     * 删除卖号
     * 
     * @param keyId 卖号主键
     * @return 结果
     */
    public int deleteFbAccountForSellByKeyId(Long keyId);

    /**
     * 批量删除卖号
     * 
     * @param keyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFbAccountForSellByKeyIds(Long[] keyIds);
}
