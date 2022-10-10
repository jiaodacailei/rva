package com.ruoyi.rva.framework.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.framework.mapper.RvaChartGridMapper;
import com.ruoyi.rva.framework.domain.RvaChartGrid;
import com.ruoyi.rva.framework.service.IRvaChartGridService;

/**
 * 图坐标系Service业务层处理
 * 
 * @author jiaodacailei
 * @date 2022-03-11
 */
@Service
public class RvaChartGridServiceImpl implements IRvaChartGridService 
{
    @Autowired
    private RvaChartGridMapper rvaChartGridMapper;

    /**
     * 查询图坐标系
     * 
     * @param id 图坐标系主键
     * @return 图坐标系
     */
    @Override
    public RvaChartGrid selectRvaChartGridById(String id)
    {
        return rvaChartGridMapper.selectRvaChartGridById(id);
    }

    /**
     * 查询图坐标系列表
     * 
     * @param rvaChartGrid 图坐标系
     * @return 图坐标系
     */
    @Override
    public List<RvaChartGrid> selectRvaChartGridList(RvaChartGrid rvaChartGrid)
    {
        return rvaChartGridMapper.selectRvaChartGridList(rvaChartGrid);
    }

    /**
     * 新增图坐标系
     * 
     * @param rvaChartGrid 图坐标系
     * @return 结果
     */
    @Override
    public int insertRvaChartGrid(RvaChartGrid rvaChartGrid)
    {
        rvaChartGrid.setCreateTime(DateUtils.getNowDate());
        return rvaChartGridMapper.insertRvaChartGrid(rvaChartGrid);
    }

    /**
     * 修改图坐标系
     * 
     * @param rvaChartGrid 图坐标系
     * @return 结果
     */
    @Override
    public int updateRvaChartGrid(RvaChartGrid rvaChartGrid)
    {
        rvaChartGrid.setUpdateTime(DateUtils.getNowDate());
        return rvaChartGridMapper.updateRvaChartGrid(rvaChartGrid);
    }

    /**
     * 批量删除图坐标系
     * 
     * @param ids 需要删除的图坐标系主键
     * @return 结果
     */
    @Override
    public int deleteRvaChartGridByIds(String[] ids)
    {
        return rvaChartGridMapper.deleteRvaChartGridByIds(ids);
    }

    /**
     * 删除图坐标系信息
     * 
     * @param id 图坐标系主键
     * @return 结果
     */
    @Override
    public int deleteRvaChartGridById(String id)
    {
        return rvaChartGridMapper.deleteRvaChartGridById(id);
    }
}
