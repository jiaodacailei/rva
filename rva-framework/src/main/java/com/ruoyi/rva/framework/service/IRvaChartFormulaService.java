package com.ruoyi.rva.framework.service;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaChartFormula;

/**
 * 图公式Service接口
 * 
 * @author jiaodacailei
 * @date 2022-03-04
 */
public interface IRvaChartFormulaService 
{
    /**
     * 查询图公式
     * 
     * @param id 图公式主键
     * @return 图公式
     */
    public RvaChartFormula selectRvaChartFormulaById(Long id);

    /**
     * 查询图公式列表
     * 
     * @param rvaChartFormula 图公式
     * @return 图公式集合
     */
    public List<RvaChartFormula> selectRvaChartFormulaList(RvaChartFormula rvaChartFormula);

    /**
     * 新增图公式
     * 
     * @param rvaChartFormula 图公式
     * @return 结果
     */
    public int insertRvaChartFormula(RvaChartFormula rvaChartFormula);

    /**
     * 修改图公式
     * 
     * @param rvaChartFormula 图公式
     * @return 结果
     */
    public int updateRvaChartFormula(RvaChartFormula rvaChartFormula);

    /**
     * 批量删除图公式
     * 
     * @param ids 需要删除的图公式主键集合
     * @return 结果
     */
    public int deleteRvaChartFormulaByIds(Long[] ids);

    /**
     * 删除图公式信息
     * 
     * @param id 图公式主键
     * @return 结果
     */
    public int deleteRvaChartFormulaById(Long id);
}
