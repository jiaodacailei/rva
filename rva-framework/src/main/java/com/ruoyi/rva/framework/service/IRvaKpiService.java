package com.ruoyi.rva.framework.service;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaKpi;
import com.ruoyi.rva.framework.domain.RvaMap;

/**
 * KPIService接口
 * 
 * @author jiaodacailei
 * @date 2022-03-22
 */
public interface IRvaKpiService 
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
     * 批量删除KPI
     * 
     * @param ids 需要删除的KPI主键集合
     * @return 结果
     */
    public int deleteRvaKpiByIds(String[] ids);

    /**
     * 删除KPI信息
     * 
     * @param id KPI主键
     * @return 结果
     */
    public int deleteRvaKpiById(String id);

    RvaKpi getData(String kpiId, RvaMap<String, Object> req);
}
