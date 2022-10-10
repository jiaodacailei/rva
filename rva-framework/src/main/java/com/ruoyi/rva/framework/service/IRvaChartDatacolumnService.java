package com.ruoyi.rva.framework.service;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaChartDatacolumn;

/**
 * 图数据列Service接口
 * 
 * @author jiaodacailei
 * @date 2022-03-11
 */
public interface IRvaChartDatacolumnService 
{
    /**
     * 查询图数据列
     * 
     * @param id 图数据列主键
     * @return 图数据列
     */
    public RvaChartDatacolumn selectRvaChartDatacolumnById(String id);

    /**
     * 查询图数据列列表
     * 
     * @param rvaChartDatacolumn 图数据列
     * @return 图数据列集合
     */
    public List<RvaChartDatacolumn> selectRvaChartDatacolumnList(RvaChartDatacolumn rvaChartDatacolumn);

    /**
     * 新增图数据列
     * 
     * @param rvaChartDatacolumn 图数据列
     * @return 结果
     */
    public int insertRvaChartDatacolumn(RvaChartDatacolumn rvaChartDatacolumn);

    /**
     * 修改图数据列
     * 
     * @param rvaChartDatacolumn 图数据列
     * @return 结果
     */
    public int updateRvaChartDatacolumn(RvaChartDatacolumn rvaChartDatacolumn);

    /**
     * 批量删除图数据列
     * 
     * @param ids 需要删除的图数据列主键集合
     * @return 结果
     */
    public int deleteRvaChartDatacolumnByIds(String[] ids);

    /**
     * 删除图数据列信息
     * 
     * @param id 图数据列主键
     * @return 结果
     */
    public int deleteRvaChartDatacolumnById(String id);
}
