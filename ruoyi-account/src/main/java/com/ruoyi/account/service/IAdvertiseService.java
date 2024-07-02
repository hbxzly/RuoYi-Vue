package com.ruoyi.account.service;

import java.util.List;

import com.ruoyi.account.domain.Advertise;

/**
 * 广告信息Service接口
 *
 * @author ruoyi
 * @date 2023-09-25
 */
public interface IAdvertiseService
{
    /**
     * 查询广告信息
     *
     * @param keyId 广告信息主键
     * @return 广告信息
     */
    public Advertise selectAdvertiseByKeyId(Long keyId);

    /**
     * 查询广告信息
     *
     * @param adAccountId 广告账户ID
     * @return 广告信息
     */
    public Advertise selectAdvertiseByAdAccountId(String adAccountId);

    /**
     * 查询广告信息列表
     *
     * @param advertise 广告信息
     * @return 广告信息集合
     */
    public List<Advertise> selectAdvertiseList(Advertise advertise);

    /**
     * 新增广告信息
     *
     * @param advertise 广告信息
     * @return 结果
     */
    public int insertAdvertise(Advertise advertise);

    /**
     * 修改广告信息
     *
     * @param advertise 广告信息
     * @return 结果
     */
    public int updateAdvertise(Advertise advertise);

    /**
     * 批量删除广告信息
     *
     * @param keyIds 需要删除的广告信息主键集合
     * @return 结果
     */
    public int deleteAdvertiseByKeyIds(Long[] keyIds);

    /**
     * 删除广告信息
     *
     * @param keyId 广告主键
     * @return 结果
     */
    public int deleteAdvertiseByKeyId(Long keyId);

    /**
     *查询所有广告信息
     * @return
     */
    public List<Advertise> selectAllAdvertise();
}
