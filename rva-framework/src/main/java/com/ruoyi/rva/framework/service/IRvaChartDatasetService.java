package com.ruoyi.rva.framework.service;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaChartDataset;

/**
 * 图数据集Service接口
 * 
 * @author jiaodacailei
 * @date 2022-03-11
 */
public interface IRvaChartDatasetService 
{
    /**
     * 查询图数据集
     * 
     * @param id 图数据集主键
     * @return 图数据集
     */
    public RvaChartDataset selectRvaChartDatasetById(String id);

    /**
     * 查询图数据集列表
     * 
     * @param rvaChartDataset 图数据集
     * @return 图数据集集合
     */
    public List<RvaChartDataset> selectRvaChartDatasetList(RvaChartDataset rvaChartDataset);

    /**
     * 新增图数据集
     * 
     * @param rvaChartDataset 图数据集
     * @return 结果
     */
    public int insertRvaChartDataset(RvaChartDataset rvaChartDataset);

    /**
     * 修改图数据集
     * 
     * @param rvaChartDataset 图数据集
     * @return 结果
     */
    public int updateRvaChartDataset(RvaChartDataset rvaChartDataset);

    /**
     * 批量删除图数据集
     * 
     * @param ids 需要删除的图数据集主键集合
     * @return 结果
     */
    public int deleteRvaChartDatasetByIds(String[] ids);

    /**
     * 删除图数据集信息
     * 
     * @param id 图数据集主键
     * @return 结果
     */
    public int deleteRvaChartDatasetById(String id);
}
