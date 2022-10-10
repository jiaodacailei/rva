package com.ruoyi.rva.framework.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.framework.mapper.RvaChartFormulaMapper;
import com.ruoyi.rva.framework.domain.RvaChartFormula;
import com.ruoyi.rva.framework.service.IRvaChartFormulaService;

/**
 * 图公式Service业务层处理
 * 
 * @author jiaodacailei
 * @date 2022-03-04
 */
@Service
public class RvaChartFormulaServiceImpl implements IRvaChartFormulaService 
{
    @Autowired
    private RvaChartFormulaMapper rvaChartFormulaMapper;

    /**
     * 查询图公式
     * 
     * @param id 图公式主键
     * @return 图公式
     */
    @Override
    public RvaChartFormula selectRvaChartFormulaById(Long id)
    {
        return rvaChartFormulaMapper.selectRvaChartFormulaById(id);
    }

    /**
     * 查询图公式列表
     * 
     * @param rvaChartFormula 图公式
     * @return 图公式
     */
    @Override
    public List<RvaChartFormula> selectRvaChartFormulaList(RvaChartFormula rvaChartFormula)
    {
        return rvaChartFormulaMapper.selectRvaChartFormulaList(rvaChartFormula);
    }

    /**
     * 新增图公式
     * 
     * @param rvaChartFormula 图公式
     * @return 结果
     */
    @Override
    public int insertRvaChartFormula(RvaChartFormula rvaChartFormula)
    {
        rvaChartFormula.setCreateTime(DateUtils.getNowDate());
        return rvaChartFormulaMapper.insertRvaChartFormula(rvaChartFormula);
    }

    /**
     * 修改图公式
     * 
     * @param rvaChartFormula 图公式
     * @return 结果
     */
    @Override
    public int updateRvaChartFormula(RvaChartFormula rvaChartFormula)
    {
        rvaChartFormula.setUpdateTime(DateUtils.getNowDate());
        return rvaChartFormulaMapper.updateRvaChartFormula(rvaChartFormula);
    }

    /**
     * 批量删除图公式
     * 
     * @param ids 需要删除的图公式主键
     * @return 结果
     */
    @Override
    public int deleteRvaChartFormulaByIds(Long[] ids)
    {
        return rvaChartFormulaMapper.deleteRvaChartFormulaByIds(ids);
    }

    /**
     * 删除图公式信息
     * 
     * @param id 图公式主键
     * @return 结果
     */
    @Override
    public int deleteRvaChartFormulaById(Long id)
    {
        return rvaChartFormulaMapper.deleteRvaChartFormulaById(id);
    }
}
