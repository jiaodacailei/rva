package com.ruoyi.rva.framework.service.impl;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.ruoyi.common.utils.sql.SqlUtil;
import com.ruoyi.rva.framework.domain.*;
import com.ruoyi.rva.framework.mapper.RvaDataMapper;
import com.ruoyi.rva.framework.mapper.RvaKpiItemMapper;
import com.ruoyi.rva.framework.mapper.RvaKpiMapper;
import com.ruoyi.rva.framework.service.IRvaMetaKPIService;
import com.ruoyi.rva.framework.service.IRvaMetaService;
import com.ruoyi.rva.framework.util.RvaJsonUtils;
import com.ruoyi.rva.framework.util.RvaPinyinUtils;
import com.ruoyi.rva.framework.util.RvaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaMetaKPIServiceImpl implements IRvaMetaKPIService {

    private final IRvaMetaService metaService;

    private final RvaDataMapper dataMapper;

    private final RvaKpiMapper kpiMapper;

    private final RvaKpiItemMapper kpiItemMapper;

    @Override
    public void quickCreate(RvaMap<String, Object> req) {
        String name = req.getString("c0_none_quickcreate_name");
        if (RvaUtils.isEmpty(name)) {
            RvaUtils.throwRequiredException("名称");
        }
        List<String> sqls = req.getList("c0_none_quickcreate_sqls");
        List<String> props = req.getList("c0_none_quickcreate_chaxunshuxing");
        Integer rows = req.getInt("c0_none_quickcreate_xingshu", 1);
        if (sqls.size() == 0) {
            RvaUtils.throwRequiredException("SQLs");
        }
        // 创建查询视图
        RvaView searchView = metaService.createIndependentSearchView(RvaKpi.NAME, props);

        // 创建
        RvaKpi kpi = new RvaKpi();
        kpi.setModule(dataMapper.selectDefaultModule());
        kpi.setId(kpi.getModule() + "_" + RvaKpi.NAME + "_" + RvaPinyinUtils.getPinyinLower(name) + "_" + RvaUtils.generateKey32(3));
        kpi.setObjId(RvaObject.NONE);
        kpi.setDataSql(RvaJsonUtils.writeAsString(sqls));
        kpi.setName(name);
        if (searchView != null) {
            kpi.setSearchId(searchView.getId());
        }
        kpi.setRows(rows);
        kpiMapper.insertRvaKpi(kpi);
        for (int i = 0; i < sqls.size(); i++) {
            String sql = sqls.get(i);
            insertRvaKpiItems(kpi.getId(), sql, i, rows);
        }
    }

    private void insertRvaKpiItems(String kpiId, String sql, Integer index, Integer row) {
        List<String> columns = RvaSQL.parseSelectColumns(sql);
        if (columns.size() == 0) {
            RvaUtils.throwFormatException("SQL", "标准SQL语法");
        }
        int i = 0;
        for (int r = 0; r < row; r ++) {
            for (int c = 0; c < columns.size(); c ++) {
                String column = columns.get(i);
                RvaKpiItem item = new RvaKpiItem();
                item.setKpiId(kpiId);
                item.setIdx(index * 10000 + i);
                item.setId(kpiId + "_" + item.getIdx());
                item.setName(String.format("data%d[%d]['%s']", index, r, column.replace("'", "")));
                item.setIcon("el-icon-coin");
                item.setValueExpression(String.format("${%s}", item.getName()));
                kpiItemMapper.insertRvaKpiItem(item);
                i ++;
            }
        }
    }
}
