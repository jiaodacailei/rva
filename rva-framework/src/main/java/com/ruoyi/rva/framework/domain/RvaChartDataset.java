package com.ruoyi.rva.framework.domain;

import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.rva.framework.mapper.*;
import com.ruoyi.rva.framework.util.RvaUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 图数据集对象 rva_chart_dataset
 * 
 * @author jiaodacailei
 * @date 2022-03-11
 */
public class RvaChartDataset extends RvaBaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private String id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 关联CRUD */
    @Excel(name = "关联CRUD")
    private String crudId;

    /** 图表 */
    @Excel(name = "图表")
    private String chartId;

    /** 其他数据 */
    @Excel(name = "其他数据")
    private String data;

    /** SQL */
    @Excel(name = "SQL")
    private String dataSql;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setCrudId(String crudId) 
    {
        this.crudId = crudId;
    }

    public String getCrudId() 
    {
        return crudId;
    }
    public void setChartId(String chartId) 
    {
        this.chartId = chartId;
    }

    public String getChartId() 
    {
        return chartId;
    }
    public void setData(String data) 
    {
        this.data = data;
    }

    public String getData() 
    {
        return data;
    }
    public void setDataSql(String dataSql) 
    {
        this.dataSql = dataSql;
    }

    public String getDataSql() 
    {
        return dataSql;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("crudId", getCrudId())
            .append("chartId", getChartId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("data", getData())
            .append("dataSql", getDataSql())
            .toString();
    }

    public static final String NAME = "chart_ds";

    private List<RvaChartDatacolumn> columns = null;

    private RvaApp crud = null;

    public RvaChartDataset loadData() {
        if (columns == null) {
            RvaChartDatacolumn search = new RvaChartDatacolumn();
            search.setChartDatasetId(this.getId());
            columns = SpringUtils.getBean(RvaChartDatacolumnMapper.class).selectRvaChartDatacolumnList(search);
            Collections.sort(columns);
        }
        if (crud == null && RvaUtils.isNotEmpty(crudId)) {
            crud = SpringUtils.getBean(RvaAppMapper.class).selectRvaAppById(crudId);
        }
        return this;
    }

    public List<RvaChartDatacolumn> getColumns() {
        loadData();
        return columns;
    }

    public List<RvaChartDatacolumn> getCategoryColumns() {
        return getColumns().stream().filter(c -> {
            return RvaChartDatacolumn.TYPE_CATEGORY.equals(c.getType());
        }).collect(Collectors.toList());
    }

    public List<RvaChartDatacolumn> getValueColumns() {
        return getColumns().stream().filter(c -> {
            return RvaChartDatacolumn.TYPE_VALUE.equals(c.getType());
        }).collect(Collectors.toList());
    }

    public static final String BY_COLUMN = "column";

    public static final String BY_ROW = "row";

    public RvaApp getCrud() {
        loadData();
        return crud;
    }

    public void setSearchId (String searchId) {
        this.setJsonProperty("searchId", searchId);
    }

    public String getSearchId () {
        if (getCrud() == null) {
            return this.getJsonPropertyString("searchId");
        }
        return getCrud().getSearchId();
    }
}
