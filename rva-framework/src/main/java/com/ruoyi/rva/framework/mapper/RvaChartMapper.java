package com.ruoyi.rva.framework.mapper;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaChart;

/**
 * 图Mapper接口
 * 
 * @author jiaodacailei
 * @date 2022-03-05
 */
public interface RvaChartMapper 
{
    /**
     * 查询图
     * 
     * @param id 图主键
     * @return 图
     */
    public RvaChart selectRvaChartById(String id);

    /**
     * 查询图列表
     * 
     * @param rvaChart 图
     * @return 图集合
     */
    public List<RvaChart> selectRvaChartList(RvaChart rvaChart);

    /**
     * 新增图
     * 
     * @param rvaChart 图
     * @return 结果
     */
    public int insertRvaChart(RvaChart rvaChart);

    /**
     * 修改图
     * 
     * @param rvaChart 图
     * @return 结果
     */
    public int updateRvaChart(RvaChart rvaChart);

    /**
     * 删除图
     * 
     * @param id 图主键
     * @return 结果
     */
    public int deleteRvaChartById(String id);

    /**
     * 批量删除图
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRvaChartByIds(String[] ids);
}
