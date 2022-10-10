package com.ruoyi.rva.framework.mapper;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaChartDatacolumn;

/**
 * 图数据列Mapper接口
 * 
 * @author jiaodacailei
 * @date 2022-03-11
 */
public interface RvaChartDatacolumnMapper 
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
     * 删除图数据列
     * 
     * @param id 图数据列主键
     * @return 结果
     */
    public int deleteRvaChartDatacolumnById(String id);

    /**
     * 批量删除图数据列
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRvaChartDatacolumnByIds(String[] ids);
}
