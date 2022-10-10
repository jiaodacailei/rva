package com.ruoyi.rva.framework.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.rva.framework.domain.*;
import com.ruoyi.rva.framework.mapper.*;
import com.ruoyi.rva.framework.service.IRvaViewService;
import com.ruoyi.rva.framework.util.RvaUtils;
import com.ruoyi.rva.framework.util.RvaVelocityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.framework.service.IRvaChartService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 图Service业务层处理
 *
 * @author jiaodacailei
 * @date 2022-03-05
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaChartServiceImpl implements IRvaChartService {
    private final RvaChartMapper chartMapper;

    /**
     * 查询图表
     *
     * @param id 图表主键值
     * @return 图表
     */
    @Override
    public RvaChart selectRvaChartById(String id) {
        return chartMapper.selectRvaChartById(id);
    }

    private final IRvaViewService viewService;

    private final RvaChartDatasetMapper datasetMapper;

    private final RvaAppMapper appMapper;

    private final RvaDataMapper dataMapper;

    private final RvaViewMapper viewMapper;

    private final RvaVelocityUtils velocityUtils;

    @Override
    public RvaMap<String, Object> getDataset(String datasetId, RvaMap<String, Object> req) {
        RvaChartDataset dataset = datasetMapper.selectRvaChartDatasetById(datasetId);
        RvaApp crud = RvaUtils.isEmpty(dataset.getCrudId()) ? null : appMapper.selectRvaAppById(dataset.getCrudId());
        List<RvaMap<String, Object>> list = null;
        if (crud != null) {
            RvaSQL listSQL = viewService.getListSQL(req, crud.getListId(), crud.getSearchId());
            listSQL.clearSelects();
            listSQL.clearOrderBys();
            RvaView listView = viewMapper.selectRvaViewById(crud.getListId());
            RvaMap<String, Object> listFieldsExpression = listView.getListFieldsExpression();
            Boolean groupBy = RvaUtils.isNotEmpty(dataset.getValueColumns().get(0).getFormula());
            for (int i = 0; i < dataset.getColumns().size(); i++) {
                RvaChartDatacolumn datacolumn = dataset.getColumns().get(i);
                String column = velocityUtils.parseWithLoginUser(datacolumn.getFormula(), new RvaMap<>(listFieldsExpression).rvaPut("column", listFieldsExpression.get(datacolumn.getViewpropertyId())));
                if (column == null) {
                    column = listFieldsExpression.get(datacolumn.getViewpropertyId()).toString();
                }
                listSQL.select(column + " as " + datacolumn.getViewpropertyId());
                if (datacolumn.isCategory() && groupBy) {
                    listSQL.getSql().GROUP_BY(column);
                }
            }
            list = dataMapper.selectList(listSQL.toString());
            List<List> results = new ArrayList<>();
            if (dataset.getCategoryColumns().size() == 1) {
                if (list.size() > 0) {
                    RvaMap<String, Object> header = new RvaMap<>();
                    List<String> keys = new ArrayList<>();
                    List<String> names = new ArrayList<>();
                    for (int i = 0; i < dataset.getColumns().size(); i++) {
                        RvaChartDatacolumn datacolumn = dataset.getColumns().get(i);
                        header.put(datacolumn.getViewpropertyId(), datacolumn.getName());
                        keys.add(datacolumn.getViewpropertyId());
                        names.add(datacolumn.getName());
                    }
                    results = RvaUtils.listMap2List(list, keys);
                    results.add(0, names);
                    list.add(0, header);
                }
                return new RvaMap<String, Object>("dsMeta", dataset).rvaPut("dsData", list).rvaPut("dsArrayData", results);
            }
        } else {
            String dataSql = velocityUtils.parseWithLoginUser(dataset.getDataSql(), req);
            list = dataMapper.selectList(dataSql);
            List<List> results = new ArrayList<>();
            List<RvaMap<String, Object>> list2 = new ArrayList<>();
            List<String> keys = list.size() > 0 ? list.get(0).getRvaKeys() : new ArrayList<>();
            if (list.size() > 0) {
                results = RvaUtils.listMap2List(list, keys);
                results.add(0, keys);
                list.forEach(row -> {
                    RvaMap<String, Object> map = new RvaMap<>();
                    list2.add(map);
                    for (int i = 0; i < keys.size(); i++) {
                        String key = keys.get(i);
                        map.put("column" + i, row.get(key));
                    }
                });
                RvaMap<String, Object> header = new RvaMap<>("columns", keys.size());
                for (int i = 0; i < keys.size(); i++) {
                    String key = keys.get(i);
                    header.put("column" + i, key);
                }
                list2.add(0, header);
            }
            return new RvaMap<String, Object>("dsMeta", dataset).rvaPut("dsData", list2).rvaPut("dsArrayData", results);
        }
        return null;
    }

}
