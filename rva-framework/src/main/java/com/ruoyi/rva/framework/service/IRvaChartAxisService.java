package com.ruoyi.rva.framework.service;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaChartAxis;

/**
 * 图轴Service接口
 * 
 * @author jiaodacailei
 * @date 2022-03-11
 */
public interface IRvaChartAxisService 
{
    /**
     * 查询图轴
     * 
     * @param id 图轴主键
     * @return 图轴
     */
    public RvaChartAxis selectRvaChartAxisById(String id);

    /**
     * 查询图轴列表
     * 
     * @param rvaChartAxis 图轴
     * @return 图轴集合
     */
    public List<RvaChartAxis> selectRvaChartAxisList(RvaChartAxis rvaChartAxis);

    /**
     * 新增图轴
     * 
     * @param rvaChartAxis 图轴
     * @return 结果
     */
    public int insertRvaChartAxis(RvaChartAxis rvaChartAxis);

    /**
     * 修改图轴
     * 
     * @param rvaChartAxis 图轴
     * @return 结果
     */
    public int updateRvaChartAxis(RvaChartAxis rvaChartAxis);

    /**
     * 批量删除图轴
     * 
     * @param ids 需要删除的图轴主键集合
     * @return 结果
     */
    public int deleteRvaChartAxisByIds(String[] ids);

    /**
     * 删除图轴信息
     * 
     * @param id 图轴主键
     * @return 结果
     */
    public int deleteRvaChartAxisById(String id);
}
