package com.ruoyi.rva.framework.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.framework.mapper.RvaChartAxisMapper;
import com.ruoyi.rva.framework.domain.RvaChartAxis;
import com.ruoyi.rva.framework.service.IRvaChartAxisService;

/**
 * 图轴Service业务层处理
 * 
 * @author jiaodacailei
 * @date 2022-03-11
 */
@Service
public class RvaChartAxisServiceImpl implements IRvaChartAxisService 
{
    @Autowired
    private RvaChartAxisMapper rvaChartAxisMapper;

    /**
     * 查询图轴
     * 
     * @param id 图轴主键
     * @return 图轴
     */
    @Override
    public RvaChartAxis selectRvaChartAxisById(String id)
    {
        return rvaChartAxisMapper.selectRvaChartAxisById(id);
    }

    /**
     * 查询图轴列表
     * 
     * @param rvaChartAxis 图轴
     * @return 图轴
     */
    @Override
    public List<RvaChartAxis> selectRvaChartAxisList(RvaChartAxis rvaChartAxis)
    {
        return rvaChartAxisMapper.selectRvaChartAxisList(rvaChartAxis);
    }

    /**
     * 新增图轴
     * 
     * @param rvaChartAxis 图轴
     * @return 结果
     */
    @Override
    public int insertRvaChartAxis(RvaChartAxis rvaChartAxis)
    {
        rvaChartAxis.setCreateTime(DateUtils.getNowDate());
        return rvaChartAxisMapper.insertRvaChartAxis(rvaChartAxis);
    }

    /**
     * 修改图轴
     * 
     * @param rvaChartAxis 图轴
     * @return 结果
     */
    @Override
    public int updateRvaChartAxis(RvaChartAxis rvaChartAxis)
    {
        rvaChartAxis.setUpdateTime(DateUtils.getNowDate());
        return rvaChartAxisMapper.updateRvaChartAxis(rvaChartAxis);
    }

    /**
     * 批量删除图轴
     * 
     * @param ids 需要删除的图轴主键
     * @return 结果
     */
    @Override
    public int deleteRvaChartAxisByIds(String[] ids)
    {
        return rvaChartAxisMapper.deleteRvaChartAxisByIds(ids);
    }

    /**
     * 删除图轴信息
     * 
     * @param id 图轴主键
     * @return 结果
     */
    @Override
    public int deleteRvaChartAxisById(String id)
    {
        return rvaChartAxisMapper.deleteRvaChartAxisById(id);
    }
}
