package com.ruoyi.rva.framework.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.framework.mapper.RvaKpiItemMapper;
import com.ruoyi.rva.framework.domain.RvaKpiItem;
import com.ruoyi.rva.framework.service.IRvaKpiItemService;

/**
 * KPI项Service业务层处理
 * 
 * @author jiaodacailei
 * @date 2022-03-22
 */
@Service
public class RvaKpiItemServiceImpl implements IRvaKpiItemService 
{
    @Autowired
    private RvaKpiItemMapper rvaKpiItemMapper;

    /**
     * 查询KPI项
     * 
     * @param id KPI项主键
     * @return KPI项
     */
    @Override
    public RvaKpiItem selectRvaKpiItemById(String id)
    {
        return rvaKpiItemMapper.selectRvaKpiItemById(id);
    }

    /**
     * 查询KPI项列表
     * 
     * @param rvaKpiItem KPI项
     * @return KPI项
     */
    @Override
    public List<RvaKpiItem> selectRvaKpiItemList(RvaKpiItem rvaKpiItem)
    {
        return rvaKpiItemMapper.selectRvaKpiItemList(rvaKpiItem);
    }

    /**
     * 新增KPI项
     * 
     * @param rvaKpiItem KPI项
     * @return 结果
     */
    @Override
    public int insertRvaKpiItem(RvaKpiItem rvaKpiItem)
    {
        rvaKpiItem.setCreateTime(DateUtils.getNowDate());
        return rvaKpiItemMapper.insertRvaKpiItem(rvaKpiItem);
    }

    /**
     * 修改KPI项
     * 
     * @param rvaKpiItem KPI项
     * @return 结果
     */
    @Override
    public int updateRvaKpiItem(RvaKpiItem rvaKpiItem)
    {
        rvaKpiItem.setUpdateTime(DateUtils.getNowDate());
        return rvaKpiItemMapper.updateRvaKpiItem(rvaKpiItem);
    }

    /**
     * 批量删除KPI项
     * 
     * @param ids 需要删除的KPI项主键
     * @return 结果
     */
    @Override
    public int deleteRvaKpiItemByIds(String[] ids)
    {
        return rvaKpiItemMapper.deleteRvaKpiItemByIds(ids);
    }

    /**
     * 删除KPI项信息
     * 
     * @param id KPI项主键
     * @return 结果
     */
    @Override
    public int deleteRvaKpiItemById(String id)
    {
        return rvaKpiItemMapper.deleteRvaKpiItemById(id);
    }
}
