package com.ruoyi.rva.framework.mapper;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaKpi;

/**
 * KPIMapper接口
 * 
 * @author jiaodacailei
 * @date 2022-03-22
 */
public interface RvaKpiMapper 
{
    /**
     * 查询KPI
     * 
     * @param id KPI主键
     * @return KPI
     */
    public RvaKpi selectRvaKpiById(String id);

    /**
     * 查询KPI列表
     * 
     * @param rvaKpi KPI
     * @return KPI集合
     */
    public List<RvaKpi> selectRvaKpiList(RvaKpi rvaKpi);

    /**
     * 新增KPI
     * 
     * @param rvaKpi KPI
     * @return 结果
     */
    public int insertRvaKpi(RvaKpi rvaKpi);

    /**
     * 修改KPI
     * 
     * @param rvaKpi KPI
     * @return 结果
     */
    public int updateRvaKpi(RvaKpi rvaKpi);

    /**
     * 删除KPI
     * 
     * @param id KPI主键
     * @return 结果
     */
    public int deleteRvaKpiById(String id);

    /**
     * 批量删除KPI
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRvaKpiByIds(String[] ids);
}
