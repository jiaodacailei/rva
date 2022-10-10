package com.ruoyi.rva.framework.domain;

import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.rva.framework.mapper.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Collections;
import java.util.List;

/**
 * 图对象 rva_chart
 * 
 * @author jiaodacailei
 * @date 2022-03-05
 */
public class RvaChart extends RvaBaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private String id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 其他数据 */
    @Excel(name = "其他数据")
    private String data;

    /** 类型 */
    @Excel(name = "类型")
    private String type;

    /** 关联CRUD */
    @Excel(name = "关联CRUD")
    private String crudId;

    /** 查询视图 */
    @Excel(name = "查询视图")
    private String searchId;

    /** 所属对象 */
    @Excel(name = "所属对象")
    private String objId;

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
    public void setData(String data) 
    {
        this.data = data;
    }

    public String getData() 
    {
        return data;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setCrudId(String crudId) 
    {
        this.crudId = crudId;
    }

    public String getCrudId() 
    {
        return crudId;
    }
    public void setSearchId(String searchId) 
    {
        this.searchId = searchId;
    }

    public String getSearchId() 
    {
        return searchId;
    }
    public void setObjId(String objId) 
    {
        this.objId = objId;
    }

    public String getObjId() 
    {
        return objId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("data", getData())
            .append("type", getType())
            .append("crudId", getCrudId())
            .append("searchId", getSearchId())
            .append("objId", getObjId())
            .toString();
    }

    public static final String NAME = "chart";

    public static final String TYPE_BAR = "bar";

    public static final String TYPE_LINE = "line";

    public static final String TYPE_PIE = "pie";

    public static final String TYPE_INHERIT = "inherit";

    private List<RvaChartAxis> axisList = null;

    private List<RvaChartSeries> seriesList = null;

    private List<RvaChartGrid> gridList = null;

    private List<RvaChartDataset> datasetList = null;

    public RvaChart loadData() {
        if (axisList == null) {
            RvaChartAxis search = new RvaChartAxis();
            search.setChartId(this.getId());
            axisList = SpringUtils.getBean(RvaChartAxisMapper.class).selectRvaChartAxisList(search);
            Collections.sort(axisList);
        }
        if (seriesList == null) {
            RvaChartSeries search = new RvaChartSeries();
            search.setChartId(this.getId());
            seriesList = SpringUtils.getBean(RvaChartSeriesMapper.class).selectRvaChartSeriesList(search);
            Collections.sort(seriesList);
        }
        if (gridList == null) {
            RvaChartGrid search = new RvaChartGrid();
            search.setChartId(this.getId());
            gridList = SpringUtils.getBean(RvaChartGridMapper.class).selectRvaChartGridList(search);
            Collections.sort(gridList);
        }
        if (datasetList == null) {
            RvaChartDataset search = new RvaChartDataset();
            search.setChartId(this.getId());
            datasetList = SpringUtils.getBean(RvaChartDatasetMapper.class).selectRvaChartDatasetList(search);
        }
        return this;
    }

    public List<RvaChartAxis> getAxisList() {
        loadData();
        return axisList;
    }

    public List<RvaChartSeries> getSeriesList() {
        loadData();
        return seriesList;
    }

    public List<RvaChartGrid> getGridList() {
        loadData();
        return gridList;
    }

    public List<RvaChartDataset> getDatasetList() {
        loadData();
        return datasetList;
    }
}
