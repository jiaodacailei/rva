package com.ruoyi.rva.framework.mapper;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaKpiItem;

/**
 * KPI项Mapper接口
 * 
 * @author jiaodacailei
 * @date 2022-03-22
 */
public interface RvaKpiItemMapper 
{
    /**
     * 查询KPI项
     * 
     * @param id KPI项主键
     * @return KPI项
     */
    public RvaKpiItem selectRvaKpiItemById(String id);

    /**
     * 查询KPI项列表
     * 
     * @param rvaKpiItem KPI项
     * @return KPI项集合
     */
    public List<RvaKpiItem> selectRvaKpiItemList(RvaKpiItem rvaKpiItem);

    /**
     * 新增KPI项
     * 
     * @param rvaKpiItem KPI项
     * @return 结果
     */
    public int insertRvaKpiItem(RvaKpiItem rvaKpiItem);

    /**
     * 修改KPI项
     * 
     * @param rvaKpiItem KPI项
     * @return 结果
     */
    public int updateRvaKpiItem(RvaKpiItem rvaKpiItem);

    /**
     * 删除KPI项
     * 
     * @param id KPI项主键
     * @return 结果
     */
    public int deleteRvaKpiItemById(String id);

    /**
     * 批量删除KPI项
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRvaKpiItemByIds(String[] ids);
}
