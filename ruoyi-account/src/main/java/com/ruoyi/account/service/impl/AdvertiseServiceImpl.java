package com.ruoyi.account.service.impl;

import java.util.List;

import com.ruoyi.account.domain.Advertise;
import com.ruoyi.account.mapper.AdvertiseMapper;
import com.ruoyi.account.service.IAdvertiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-25
 */
@Service
public class AdvertiseServiceImpl implements IAdvertiseService
{
    @Autowired
    private AdvertiseMapper advertiseMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param keyId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public Advertise selectAdvertiseByKeyId(Long keyId)
    {
        return advertiseMapper.selectAdvertiseByKeyId(keyId);
    }

    /**
     * 查询【请填写功能名称】
     *
     * @param adAccountId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public Advertise selectAdvertiseByAdAccountId(String adAccountId) {
        return advertiseMapper.selectAdvertiseByAdAccountId(adAccountId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param advertise 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<Advertise> selectAdvertiseList(Advertise advertise)
    {
        return advertiseMapper.selectAdvertiseList(advertise);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param advertise 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertAdvertise(Advertise advertise)
    {
        return advertiseMapper.insertAdvertise(advertise);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param advertise 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateAdvertise(Advertise advertise)
    {
        return advertiseMapper.updateAdvertise(advertise);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param keyIds 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteAdvertiseByKeyIds(Long[] keyIds)
    {
        return advertiseMapper.deleteAdvertiseByKeyIds(keyIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param keyId 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteAdvertiseByKeyId(Long keyId)
    {
        return advertiseMapper.deleteAdvertiseByKeyId(keyId);
    }



    @Override
    public List<Advertise> selectAllAdvertise() {
        return advertiseMapper.selectAllAdvertise();
    }

    /**
     * 完善个人号、BM信息
     */
    @Override
    public void completeInfo() {
        advertiseMapper.completeInfo();
    }
}
