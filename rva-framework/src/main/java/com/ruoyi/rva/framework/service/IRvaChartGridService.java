package com.ruoyi.rva.framework.service;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaChartGrid;

/**
 * 图坐标系Service接口
 * 
 * @author jiaodacailei
 * @date 2022-03-11
 */
public interface IRvaChartGridService 
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
     * 批量删除图坐标系
     * 
     * @param ids 需要删除的图坐标系主键集合
     * @return 结果
     */
    public int deleteRvaChartGridByIds(String[] ids);

    /**
     * 删除图坐标系信息
     * 
     * @param id 图坐标系主键
     * @return 结果
     */
    public int deleteRvaChartGridById(String id);
}
