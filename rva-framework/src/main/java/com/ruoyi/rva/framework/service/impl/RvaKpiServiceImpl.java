package com.ruoyi.rva.framework.service.impl;

import java.util.ArrayList;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.rva.framework.domain.RvaKpiItem;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.mapper.RvaDataMapper;
import com.ruoyi.rva.framework.util.RvaUtils;
import com.ruoyi.rva.framework.util.RvaVelocityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.framework.mapper.RvaKpiMapper;
import com.ruoyi.rva.framework.domain.RvaKpi;
import com.ruoyi.rva.framework.service.IRvaKpiService;
import org.springframework.transaction.annotation.Transactional;

/**
 * KPIService业务层处理
 * 
 * @author jiaodacailei
 * @date 2022-03-22
 */
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class RvaKpiServiceImpl implements IRvaKpiService 
{
    private final RvaKpiMapper rvaKpiMapper;

    private final RvaDataMapper dataMapper;

    private final RvaVelocityUtils velocityUtils;

    /**
     * 查询KPI
     * 
     * @param id KPI主键
     * @return KPI
     */
    @Override
    public RvaKpi selectRvaKpiById(String id)
    {
        return rvaKpiMapper.selectRvaKpiById(id);
    }

    /**
     * 查询KPI列表
     * 
     * @param rvaKpi KPI
     * @return KPI
     */
    @Override
    public List<RvaKpi> selectRvaKpiList(RvaKpi rvaKpi)
    {
        return rvaKpiMapper.selectRvaKpiList(rvaKpi);
    }

    /**
     * 新增KPI
     * 
     * @param rvaKpi KPI
     * @return 结果
     */
    @Override
    public int insertRvaKpi(RvaKpi rvaKpi)
    {
        rvaKpi.setCreateTime(DateUtils.getNowDate());
        return rvaKpiMapper.insertRvaKpi(rvaKpi);
    }

    /**
     * 修改KPI
     * 
     * @param rvaKpi KPI
     * @return 结果
     */
    @Override
    public int updateRvaKpi(RvaKpi rvaKpi)
    {
        rvaKpi.setUpdateTime(DateUtils.getNowDate());
        return rvaKpiMapper.updateRvaKpi(rvaKpi);
    }

    /**
     * 批量删除KPI
     * 
     * @param ids 需要删除的KPI主键
     * @return 结果
     */
    @Override
    public int deleteRvaKpiByIds(String[] ids)
    {
        return rvaKpiMapper.deleteRvaKpiByIds(ids);
    }

    /**
     * 删除KPI信息
     * 
     * @param id KPI主键
     * @return 结果
     */
    @Override
    public int deleteRvaKpiById(String id)
    {
        return rvaKpiMapper.deleteRvaKpiById(id);
    }

    @Override
    public RvaKpi getData(String kpiId, RvaMap<String, Object> req) {
        RvaKpi kpi = rvaKpiMapper.selectRvaKpiById(kpiId);
        RvaMap<String, List<RvaMap<String, Object>>> data = new RvaMap<>();
        for (int i = 0; i < kpi.getDataSqlList().size(); i++) {
            String sql = kpi.getDataSqlList().get(i);
            data.put("data" + i, dataMapper.selectList(velocityUtils.parseWithLoginUser(sql, new RvaMap<>(req))));
        }
        List<List<RvaKpiItem>> itemsList = new ArrayList<>();
        for (int i = 0; i < data.get("data0").size() / kpi.getRows(); i ++) {
            RvaMap<String, Object> velocityContext = new RvaMap<>();
            for (int j = 0; j < kpi.getDataSqlList().size(); j++) {
                String key = "data" + j;
                List<RvaMap<String, Object>> rvaMaps = data.get(key);
                velocityContext.put(key, rvaMaps.subList(i, i + kpi.getRows()));
            }
            List<RvaKpiItem> items = new ArrayList<>();
            for (RvaKpiItem item : kpi.getItems()) {
                String value = velocityUtils.parseWithLoginUser(item.getValueExpression(), velocityContext);
                RvaKpiItem rvaKpiItem = RvaUtils.cloneBySetter(item, RvaKpiItem.class);
                rvaKpiItem.setValue(value);
                items.add(rvaKpiItem);
            }
            itemsList.add(items);
        }
        kpi.setItemsList(itemsList);
        return kpi;
    }
}
