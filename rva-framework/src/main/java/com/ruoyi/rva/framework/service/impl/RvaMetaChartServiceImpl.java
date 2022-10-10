package com.ruoyi.rva.framework.service.impl;

import com.ruoyi.rva.framework.domain.*;
import com.ruoyi.rva.framework.mapper.*;
import com.ruoyi.rva.framework.service.IRvaMetaChartService;
import com.ruoyi.rva.framework.service.IRvaMetaService;
import com.ruoyi.rva.framework.service.IRvaViewService;
import com.ruoyi.rva.framework.util.RvaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaMetaChartServiceImpl implements IRvaMetaChartService {

    private final RvaChartMapper chartMapper;

    private final RvaChartDatasetMapper datasetMapper;

    private final RvaChartDatacolumnMapper datacolumnMapper;

    private final RvaChartSeriesMapper seriesMapper;

    private final RvaChartAxisMapper axisMapper;

    private final RvaChartGridMapper gridMapper;

    private final RvaAppMapper appMapper;

    private final RvaObjectMapper objectMapper;

    private final RvaViewMapper viewMapper;

    private final RvaViewpropertyMapper viewpropertyMapper;

    private final RvaDataMapper dataMapper;

    private final IRvaMetaService metaService;

    private String createId(String name, String objectId, String suffix) {
        return RvaUtils.join(Arrays.asList(name, objectId, suffix), "_");
    }

    @Override
    public void quickCreate(RvaMap<String, Object> req) {
        String crudId = req.getString("c0_none_tubiao_guanliancrud");
        List<String> categoryPropIds = req.getList("c0_none_tubiao_fenleishuxing");
        if (categoryPropIds.size() == 0) {
            RvaUtils.throwRequiredException("分类属性");
        }
        if (categoryPropIds.size() > 2) {
            RvaUtils.throwRuntimeException("分类属性只能选择1-2个");
        }
        List<String> valuePropIds = req.getList("c0_none_tubiao_shuzhishuxing");
        if (categoryPropIds.size() == 2) {
            if (valuePropIds.size() > 1) {
                RvaUtils.throwRuntimeException("分类属性个数为2时，数值属性只能选择1个");
            }
        }
        String categoryFormula = req.getString("c0_none_tubiao_fenleigongshi");
        String valueFormula = req.getString("c0_none_tubiao_shuzhigongshi");

        // 获取crud/object
        RvaApp crud = appMapper.selectRvaAppById(crudId);
        RvaObject object = objectMapper.selectRvaObjectById(crud.getObjId());
        // 图表
        RvaChart chart = new RvaChart();
        chart.setCrudId(crudId);
        chart.setName(object.getName());
        String suffix = RvaUtils.generateKey32(4);
        chart.setId(createId(RvaChart.NAME, object.getId(), suffix));
        chart.setType(RvaChart.TYPE_BAR);
        chart.setObjId(object.getId());
        chart.setSearchId(crud.getSearchId());
        chart.setCreateUpdateInfo();
        chartMapper.insertRvaChart(chart);

        // 数据集
        RvaChartDataset dataset = new RvaChartDataset();
        dataset.setChartId(chart.getId());
        dataset.setId(createId(RvaChartDataset.NAME, object.getId(), suffix));
        dataset.setCrudId(crudId);
        dataset.setName(object.getName());
        dataset.setCreateUpdateInfo();
        datasetMapper.insertRvaChartDataset(dataset);
        // 分类数据列
        for (int i = 0; i < categoryPropIds.size(); i++) {
            insertDatacolumn(categoryPropIds.get(i), RvaChartDatacolumn.TYPE_CATEGORY, categoryFormula, dataset, i);
        }
        // 数值数据列
        for (int i = 0; i < valuePropIds.size(); i++) {
            insertDatacolumn(valuePropIds.get(i), RvaChartDatacolumn.TYPE_VALUE, valueFormula, dataset, i + categoryPropIds.size());
        }

        // 分类轴X
        List<RvaChartDatacolumn> categoryColumns = dataset.getCategoryColumns();
        List<RvaChartAxis> axisXList = new ArrayList<>();
        for (int i = 0; i < categoryColumns.size(); i++) {
            RvaChartDatacolumn datacolumn = categoryColumns.get(i);
            RvaChartAxis axis = new RvaChartAxis();
            axis.setChartId(chart.getId());
            axis.setId(createId(RvaChartAxis.NAME, object.getId(), suffix + "_" + i));
            axis.setName(datacolumn.getName());
            axis.setIdx(i);
            axis.setType(RvaChartDatacolumn.TYPE_CATEGORY);
            axis.setChartDatasetId(dataset.getId());
            if (i == 0) {
                axis.setDataType(RvaChartDataset.BY_COLUMN);
            } else {
                axis.setDataType(RvaChartDataset.BY_ROW);
            }
            axis.setDataIndex(0);
            axis.setCreateUpdateInfo();
            axisMapper.insertRvaChartAxis(axis);
            axisXList.add(axis);
        }

        // 数值轴Y
        List<RvaChartAxis> axisYList = new ArrayList<>();
        List<RvaChartDatacolumn> valueColumns = dataset.getValueColumns();
        for (int i = 0; i < categoryColumns.size(); i++) {
            axisYList.add(insertValueAxis(object, chart, suffix, axisXList, i, valueColumns.get(0)));
        }

        // 坐标系
        for (int i = 0; i < categoryColumns.size(); i++) {
            RvaChartDatacolumn datacolumn = categoryColumns.get(i);
            RvaChartGrid grid = new RvaChartGrid();
            grid.setId(createId(RvaChartGrid.NAME, object.getId(), suffix + "_" + i));
            grid.setName(datacolumn.getName());
            grid.setChartId(chart.getId());
            grid.setAxisX0(axisXList.get(i).getId());
            grid.setAxisY0(axisYList.get(i).getId());
            grid.setIdx(i);
            if (categoryColumns.size() == 2) {
                if (i == 0) {
                    grid.setGridBottom("55%");
                } else {
                    grid.setGridTop("55%");
                }
            }
            grid.setCreateUpdateInfo();
            gridMapper.insertRvaChartGrid(grid);
        }

        // 系列
        if (categoryColumns.size() == 1) {
            for (int i = 0; i < valueColumns.size(); i++) {
                RvaChartDatacolumn datacolumn = valueColumns.get(i);
                RvaChartSeries chartSeries = createChartSeries(object, chart, suffix, i, datacolumn);

                // 设置数据源
                chartSeries.setDataType(RvaChartDataset.BY_COLUMN);
                chartSeries.setDataIndex(i + 1);// 第一（0）行或者第一（0）列被分类轴使用，故此处从1开始
                // 设置XY轴
                chartSeries.setAxisXId(axisXList.get(0).getId());
                chartSeries.setAxisYId(axisYList.get(0).getId());

                seriesMapper.insertRvaChartSeries(chartSeries);
            }
        } else {
            for (int i = 0; i < categoryColumns.size(); i++) {
                RvaChartDatacolumn datacolumn = categoryColumns.get(i);
                RvaChartSeries chartSeries = createChartSeries(object, chart, suffix, i, datacolumn);

                // 设置数据源
                chartSeries.setDataType(i == 0 ? RvaChartDataset.BY_COLUMN : RvaChartDataset.BY_ROW);
                chartSeries.setDataIndex(1);// 第一（0）行或者第一（0）列被分类轴使用，故此处从1开始
                // 设置XY轴
                chartSeries.setAxisXId(axisXList.get(i).getId());
                chartSeries.setAxisYId(axisYList.get(i).getId());

                seriesMapper.insertRvaChartSeries(chartSeries);
            }
        }

    }

    private RvaChartSeries createChartSeries(RvaObject object, RvaChart chart, String suffix, int i, RvaChartDatacolumn datacolumn) {
        RvaChartSeries chartSeries = new RvaChartSeries();
        chartSeries.setId(createId(RvaChartSeries.NAME, object.getId(), suffix + "_" + i));
        chartSeries.setName(datacolumn.getName());
        chartSeries.setType(RvaChart.TYPE_INHERIT);
        chartSeries.setIdx(i);
        chartSeries.setChartDatasetId(datacolumn.getChartDatasetId());
        chartSeries.setChartId(chart.getId());
        chartSeries.setCreateUpdateInfo();
        return chartSeries;
    }

    private RvaChartAxis insertValueAxis(RvaObject object, RvaChart chart, String suffix, List<RvaChartAxis> axisList, int i, RvaChartDatacolumn datacolumn) {
        RvaChartAxis chartAxis2 = new RvaChartAxis();
        chartAxis2.setType(RvaChartDatacolumn.TYPE_VALUE);
        chartAxis2.setChartId(chart.getId());
        chartAxis2.setId(createId(RvaChartAxis.NAME, object.getId(), suffix + "_" + (axisList.size() + i)));
        chartAxis2.setName(datacolumn.getName());
        chartAxis2.setIdx(i);
        chartAxis2.setCreateUpdateInfo();
        axisMapper.insertRvaChartAxis(chartAxis2);
        return chartAxis2;
    }

    private void insertDatacolumn(String vpId, String type, String formula, RvaChartDataset dataset, int i) {
        RvaViewproperty viewproperty = viewpropertyMapper.selectRvaViewpropertyById(vpId);
        RvaChartDatacolumn datacolumn = new RvaChartDatacolumn();
        datacolumn.setChartDatasetId(dataset.getId());
        datacolumn.setId(dataset.getId() + "_" + i);
        datacolumn.setIdx(i);
        datacolumn.setName(viewproperty.getName());
        datacolumn.setFormula(formula);
        datacolumn.setType(type);
        if (RvaChartDatacolumn.TYPE_CATEGORY.equals(type) && RvaUtils.isNotEmpty(viewproperty.getDictType())) {
            datacolumn.setOptionDict(viewproperty.getDictType());
            datacolumn.setOptionSql(String.format("select dict_value as value, dict_label as label from sys_dict_data where dict_type = '%s' order by dict_sort", viewproperty.getDictType()));
        }
        datacolumn.setViewpropertyId(viewproperty.getId());
        datacolumn.setCreateUpdateInfo();
        datacolumnMapper.insertRvaChartDatacolumn(datacolumn);
    }

    private String getFormulaName(String formula, RvaViewproperty valueProp) {
        if (valueProp == null) {
            return "数量";
        }
        if (RvaUtils.isEmpty(formula)) {
            return "数量";
        }
        if (formula.indexOf("sum") >= 0) {
            return valueProp.getName() + "总和";
        }
        if (formula.indexOf("max") >= 0) {
            return valueProp.getName() + "最大值";
        }
        if (formula.indexOf("min") >= 0) {
            return valueProp.getName() + "最小值";
        }
        if (formula.indexOf("avg") >= 0) {
            return valueProp.getName() + "平均值";
        }
        return valueProp.getName() + "数量";
    }

    @Override
    public void createBySql(RvaMap<String, Object> req) {
        String sql = req.getString("c0_none_sql_sqlyuju");
        List<String> props = req.getList("c0_none_sql_chaxunshuxing");
        String defaultModule = dataMapper.selectDefaultModule();
        String objId = "none";
        // 创建查询视图
        RvaView searchView = metaService.createIndependentSearchView(RvaChart.NAME, props);

        String suffix = RvaUtils.generateKey32(4);
        // 图表
        RvaChart chart = new RvaChart();
        chart.setName("图表" + suffix);
        chart.setId(createId(RvaChart.NAME, objId, suffix));
        chart.setType(RvaChart.TYPE_BAR);
        chart.setJsonProperty("module", defaultModule);
        chart.setObjId(objId);
        if (props.size() > 0) {
            chart.setSearchId(searchView.getId());
        }
        chart.setCreateUpdateInfo();
        chartMapper.insertRvaChart(chart);

        // 数据集
        RvaChartDataset dataset = new RvaChartDataset();
        dataset.setChartId(chart.getId());
        dataset.setId(createId(RvaChartDataset.NAME, objId, suffix));
        dataset.setDataSql(sql);
        dataset.setName("SQL");
        dataset.setCreateUpdateInfo();
        if (props.size() > 0) {
            dataset.setJsonProperty("searchId", searchView.getId());
        }
        datasetMapper.insertRvaChartDataset(dataset);

        // X轴
        RvaChartAxis axisX = new RvaChartAxis();
        axisX.setChartId(chart.getId());
        axisX.setId(createId(RvaChartAxis.NAME, objId, suffix + "_x"));
        axisX.setName("分类");
        axisX.setIdx(0);
        axisX.setType(RvaChartDatacolumn.TYPE_CATEGORY);
        axisX.setChartDatasetId(dataset.getId());
        axisX.setDataType(RvaChartDataset.BY_COLUMN);
        axisX.setDataIndex(0);
        axisX.setCreateUpdateInfo();
        axisMapper.insertRvaChartAxis(axisX);

        // Y轴
        RvaChartAxis axisY = new RvaChartAxis();
        axisY.setChartId(chart.getId());
        axisY.setId(createId(RvaChartAxis.NAME, objId, suffix + "_y"));
        axisY.setName("数值");
        axisY.setIdx(1);
        axisY.setType(RvaChartDatacolumn.TYPE_VALUE);
        axisY.setCreateUpdateInfo();
        axisMapper.insertRvaChartAxis(axisY);

        // 坐标系
        RvaChartGrid grid = new RvaChartGrid();
        grid.setId(createId(RvaChartGrid.NAME, objId, suffix + "_0"));
        grid.setName("SQL坐标系");
        grid.setChartId(chart.getId());
        grid.setAxisX0(axisX.getId());
        grid.setAxisY0(axisY.getId());
        grid.setIdx(0);
        grid.setCreateUpdateInfo();
        gridMapper.insertRvaChartGrid(grid);

        // 系列
        int fieldsCount = getFieldsCount(sql);
        for (int i = 1; i < fieldsCount; i++) {
            RvaChartSeries chartSeries = new RvaChartSeries();
            chartSeries.setId(createId(RvaChartSeries.NAME, objId, suffix + "_" + (i - 1)));
            chartSeries.setName("数值" + (i - 1));
            chartSeries.setType(RvaChart.TYPE_INHERIT);
            chartSeries.setIdx(i - 1);
            chartSeries.setChartDatasetId(dataset.getId());
            chartSeries.setChartId(chart.getId());
            chartSeries.setCreateUpdateInfo();

            // 设置数据源
            chartSeries.setDataType(RvaChartDataset.BY_COLUMN);
            chartSeries.setDataIndex(i);// 第一（0）行或者第一（0）列被分类轴使用，故此处从1开始
            // 设置XY轴
            chartSeries.setAxisXId(axisX.getId());
            chartSeries.setAxisYId(axisY.getId());

            seriesMapper.insertRvaChartSeries(chartSeries);
        }
    }

    private static int getFieldsCount(String sql) {
        String fields = sql.split(" (?!)from ")[0];
        return fields.split(",").length;
    }

    public static void main(String[] args) {
        int fieldsCount = getFieldsCount("select xx, yy frOM xxx");
        System.out.println(fieldsCount);
    }
}
