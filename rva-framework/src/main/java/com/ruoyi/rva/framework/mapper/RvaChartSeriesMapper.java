package com.ruoyi.rva.framework.mapper;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaChartSeries;

/**
 * 图系列Mapper接口
 * 
 * @author jiaodacailei
 * @date 2022-03-11
 */
public interface RvaChartSeriesMapper 
{
    /**
     * 查询图系列
     * 
     * @param id 图系列主键
     * @return 图系列
     */
    public RvaChartSeries selectRvaChartSeriesById(String id);

    /**
     * 查询图系列列表
     * 
     * @param rvaChartSeries 图系列
     * @return 图系列集合
     */
    public List<RvaChartSeries> selectRvaChartSeriesList(RvaChartSeries rvaChartSeries);

    /**
     * 新增图系列
     * 
     * @param rvaChartSeries 图系列
     * @return 结果
     */
    public int insertRvaChartSeries(RvaChartSeries rvaChartSeries);

    /**
     * 修改图系列
     * 
     * @param rvaChartSeries 图系列
     * @return 结果
     */
    public int updateRvaChartSeries(RvaChartSeries rvaChartSeries);

    /**
     * 删除图系列
     * 
     * @param id 图系列主键
     * @return 结果
     */
    public int deleteRvaChartSeriesById(String id);

    /**
     * 批量删除图系列
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRvaChartSeriesByIds(String[] ids);
}
