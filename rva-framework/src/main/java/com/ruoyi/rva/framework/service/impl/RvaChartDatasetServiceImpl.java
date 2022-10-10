package com.ruoyi.rva.framework.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.framework.mapper.RvaChartDatasetMapper;
import com.ruoyi.rva.framework.domain.RvaChartDataset;
import com.ruoyi.rva.framework.service.IRvaChartDatasetService;

/**
 * 图数据集Service业务层处理
 * 
 * @author jiaodacailei
 * @date 2022-03-11
 */
@Service
public class RvaChartDatasetServiceImpl implements IRvaChartDatasetService 
{
    @Autowired
    private RvaChartDatasetMapper rvaChartDatasetMapper;

    /**
     * 查询图数据集
     * 
     * @param id 图数据集主键
     * @return 图数据集
     */
    @Override
    public RvaChartDataset selectRvaChartDatasetById(String id)
    {
        return rvaChartDatasetMapper.selectRvaChartDatasetById(id);
    }

    /**
     * 查询图数据集列表
     * 
     * @param rvaChartDataset 图数据集
     * @return 图数据集
     */
    @Override
    public List<RvaChartDataset> selectRvaChartDatasetList(RvaChartDataset rvaChartDataset)
    {
        return rvaChartDatasetMapper.selectRvaChartDatasetList(rvaChartDataset);
    }

    /**
     * 新增图数据集
     * 
     * @param rvaChartDataset 图数据集
     * @return 结果
     */
    @Override
    public int insertRvaChartDataset(RvaChartDataset rvaChartDataset)
    {
        rvaChartDataset.setCreateTime(DateUtils.getNowDate());
        return rvaChartDatasetMapper.insertRvaChartDataset(rvaChartDataset);
    }

    /**
     * 修改图数据集
     * 
     * @param rvaChartDataset 图数据集
     * @return 结果
     */
    @Override
    public int updateRvaChartDataset(RvaChartDataset rvaChartDataset)
    {
        rvaChartDataset.setUpdateTime(DateUtils.getNowDate());
        return rvaChartDatasetMapper.updateRvaChartDataset(rvaChartDataset);
    }

    /**
     * 批量删除图数据集
     * 
     * @param ids 需要删除的图数据集主键
     * @return 结果
     */
    @Override
    public int deleteRvaChartDatasetByIds(String[] ids)
    {
        return rvaChartDatasetMapper.deleteRvaChartDatasetByIds(ids);
    }

    /**
     * 删除图数据集信息
     * 
     * @param id 图数据集主键
     * @return 结果
     */
    @Override
    public int deleteRvaChartDatasetById(String id)
    {
        return rvaChartDatasetMapper.deleteRvaChartDatasetById(id);
    }
}
