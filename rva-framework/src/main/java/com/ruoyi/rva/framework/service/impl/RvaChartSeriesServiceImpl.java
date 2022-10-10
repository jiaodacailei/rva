package com.ruoyi.rva.framework.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.framework.mapper.RvaChartSeriesMapper;
import com.ruoyi.rva.framework.domain.RvaChartSeries;
import com.ruoyi.rva.framework.service.IRvaChartSeriesService;

/**
 * 图系列Service业务层处理
 * 
 * @author jiaodacailei
 * @date 2022-03-11
 */
@Service
public class RvaChartSeriesServiceImpl implements IRvaChartSeriesService 
{
    @Autowired
    private RvaChartSeriesMapper rvaChartSeriesMapper;

    /**
     * 查询图系列
     * 
     * @param id 图系列主键
     * @return 图系列
     */
    @Override
    public RvaChartSeries selectRvaChartSeriesById(String id)
    {
        return rvaChartSeriesMapper.selectRvaChartSeriesById(id);
    }

    /**
     * 查询图系列列表
     * 
     * @param rvaChartSeries 图系列
     * @return 图系列
     */
    @Override
    public List<RvaChartSeries> selectRvaChartSeriesList(RvaChartSeries rvaChartSeries)
    {
        return rvaChartSeriesMapper.selectRvaChartSeriesList(rvaChartSeries);
    }

    /**
     * 新增图系列
     * 
     * @param rvaChartSeries 图系列
     * @return 结果
     */
    @Override
    public int insertRvaChartSeries(RvaChartSeries rvaChartSeries)
    {
        rvaChartSeries.setCreateTime(DateUtils.getNowDate());
        return rvaChartSeriesMapper.insertRvaChartSeries(rvaChartSeries);
    }

    /**
     * 修改图系列
     * 
     * @param rvaChartSeries 图系列
     * @return 结果
     */
    @Override
    public int updateRvaChartSeries(RvaChartSeries rvaChartSeries)
    {
        rvaChartSeries.setUpdateTime(DateUtils.getNowDate());
        return rvaChartSeriesMapper.updateRvaChartSeries(rvaChartSeries);
    }

    /**
     * 批量删除图系列
     * 
     * @param ids 需要删除的图系列主键
     * @return 结果
     */
    @Override
    public int deleteRvaChartSeriesByIds(String[] ids)
    {
        return rvaChartSeriesMapper.deleteRvaChartSeriesByIds(ids);
    }

    /**
     * 删除图系列信息
     * 
     * @param id 图系列主键
     * @return 结果
     */
    @Override
    public int deleteRvaChartSeriesById(String id)
    {
        return rvaChartSeriesMapper.deleteRvaChartSeriesById(id);
    }
}
