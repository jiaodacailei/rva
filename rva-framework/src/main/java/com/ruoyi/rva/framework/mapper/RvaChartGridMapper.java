package com.ruoyi.rva.framework.mapper;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaChartGrid;

/**
 * 图坐标系Mapper接口
 * 
 * @author jiaodacailei
 * @date 2022-03-11
 */
public interface RvaChartGridMapper 
{
    /**
     * 查询图坐标系
     * 
     * @param id 图坐标系主键
     * @return 图坐标系
     */
    public RvaChartGrid selectRvaChartGridById(String id);

    /**
     * 查询图坐标系列表
     * 
     * @param rvaChartGrid 图坐标系
     * @return 图坐标系集合
     */
    public List<RvaChartGrid> selectRvaChartGridList(RvaChartGrid rvaChartGrid);

    /**
     * 新增图坐标系
     * 
     * @param rvaChartGrid 图坐标系
     * @return 结果
     */
    public int insertRvaChartGrid(RvaChartGrid rvaChartGrid);

    /**
     * 修改图坐标系
     * 
     * @param rvaChartGrid 图坐标系
     * @return 结果
     */
    public int updateRvaChartGrid(RvaChartGrid rvaChartGrid);

    /**
     * 删除图坐标系
     * 
     * @param id 图坐标系主键
     * @return 结果
     */
    public int deleteRvaChartGridById(String id);

    /**
     * 批量删除图坐标系
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRvaChartGridByIds(String[] ids);
}
