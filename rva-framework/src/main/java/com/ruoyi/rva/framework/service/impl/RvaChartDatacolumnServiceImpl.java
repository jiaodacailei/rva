package com.ruoyi.rva.framework.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.framework.mapper.RvaChartDatacolumnMapper;
import com.ruoyi.rva.framework.domain.RvaChartDatacolumn;
import com.ruoyi.rva.framework.service.IRvaChartDatacolumnService;

/**
 * 图数据列Service业务层处理
 * 
 * @author jiaodacailei
 * @date 2022-03-11
 */
@Service
public class RvaChartDatacolumnServiceImpl implements IRvaChartDatacolumnService 
{
    @Autowired
    private RvaChartDatacolumnMapper rvaChartDatacolumnMapper;

    /**
     * 查询图数据列
     * 
     * @param id 图数据列主键
     * @return 图数据列
     */
    @Override
    public RvaChartDatacolumn selectRvaChartDatacolumnById(String id)
    {
        return rvaChartDatacolumnMapper.selectRvaChartDatacolumnById(id);
    }

    /**
     * 查询图数据列列表
     * 
     * @param rvaChartDatacolumn 图数据列
     * @return 图数据列
     */
    @Override
    public List<RvaChartDatacolumn> selectRvaChartDatacolumnList(RvaChartDatacolumn rvaChartDatacolumn)
    {
        return rvaChartDatacolumnMapper.selectRvaChartDatacolumnList(rvaChartDatacolumn);
    }

    /**
     * 新增图数据列
     * 
     * @param rvaChartDatacolumn 图数据列
     * @return 结果
     */
    @Override
    public int insertRvaChartDatacolumn(RvaChartDatacolumn rvaChartDatacolumn)
    {
        rvaChartDatacolumn.setCreateTime(DateUtils.getNowDate());
        return rvaChartDatacolumnMapper.insertRvaChartDatacolumn(rvaChartDatacolumn);
    }

    /**
     * 修改图数据列
     * 
     * @param rvaChartDatacolumn 图数据列
     * @return 结果
     */
    @Override
    public int updateRvaChartDatacolumn(RvaChartDatacolumn rvaChartDatacolumn)
    {
        rvaChartDatacolumn.setUpdateTime(DateUtils.getNowDate());
        return rvaChartDatacolumnMapper.updateRvaChartDatacolumn(rvaChartDatacolumn);
    }

    /**
     * 批量删除图数据列
     * 
     * @param ids 需要删除的图数据列主键
     * @return 结果
     */
    @Override
    public int deleteRvaChartDatacolumnByIds(String[] ids)
    {
        return rvaChartDatacolumnMapper.deleteRvaChartDatacolumnByIds(ids);
    }

    /**
     * 删除图数据列信息
     * 
     * @param id 图数据列主键
     * @return 结果
     */
    @Override
    public int deleteRvaChartDatacolumnById(String id)
    {
        return rvaChartDatacolumnMapper.deleteRvaChartDatacolumnById(id);
    }
}
