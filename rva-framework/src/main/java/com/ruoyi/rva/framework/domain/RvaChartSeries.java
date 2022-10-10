package com.ruoyi.rva.framework.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 图系列对象 rva_chart_series
 * 
 * @author jiaodacailei
 * @date 2022-03-11
 */
public class RvaChartSeries extends RvaBaseEntity implements Comparable<RvaChartSeries> {

    private static final long serialVersionUID = 1L;

    /** ID */
    private String id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** X轴 */
    @Excel(name = "X轴")
    private String axisXId;

    /** Y轴 */
    @Excel(name = "Y轴")
    private String axisYId;

    /** 数据模式(column row) */
    @Excel(name = "数据模式(column row)")
    private String dataType;

    /** 数据索引，rva_chart_dataset返回结果集的行或者列索引 */
    @Excel(name = "数据索引，rva_chart_dataset返回结果集的行或者列索引")
    private Integer dataIndex;

    /** 其他数据 */
    @Excel(name = "其他数据")
    private String data;

    /** 数据集 */
    @Excel(name = "数据集")
    private String chartDatasetId;

    /** 类型 */
    @Excel(name = "类型")
    private String type;

    /** 所属图表 */
    @Excel(name = "所属图表")
    private String chartId;

    /** 排序 */
    @Excel(name = "排序")
    private Integer idx;

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
    public void setAxisXId(String axisXId) 
    {
        this.axisXId = axisXId;
    }

    public String getAxisXId() 
    {
        return axisXId;
    }
    public void setAxisYId(String axisYId) 
    {
        this.axisYId = axisYId;
    }

    public String getAxisYId() 
    {
        return axisYId;
    }
    public void setDataType(String dataType) 
    {
        this.dataType = dataType;
    }

    public String getDataType() 
    {
        return dataType;
    }
    public void setDataIndex(Integer dataIndex) 
    {
        this.dataIndex = dataIndex;
    }

    public Integer getDataIndex() 
    {
        return dataIndex;
    }
    public void setData(String data) 
    {
        this.data = data;
    }

    public String getData() 
    {
        return data;
    }
    public void setChartDatasetId(String chartDatasetId) 
    {
        this.chartDatasetId = chartDatasetId;
    }

    public String getChartDatasetId() 
    {
        return chartDatasetId;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setChartId(String chartId) 
    {
        this.chartId = chartId;
    }

    public String getChartId() 
    {
        return chartId;
    }
    public void setIdx(Integer idx) 
    {
        this.idx = idx;
    }

    public Integer getIdx() 
    {
        return idx;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("createBy", getCreateBy())
            .append("axisXId", getAxisXId())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("axisYId", getAxisYId())
            .append("updateTime", getUpdateTime())
            .append("dataType", getDataType())
            .append("dataIndex", getDataIndex())
            .append("data", getData())
            .append("chartDatasetId", getChartDatasetId())
            .append("type", getType())
            .append("chartId", getChartId())
            .append("idx", getIdx())
            .toString();
    }

    public static final String NAME = "chart_series";

    @Override
    public int compareTo(RvaChartSeries o) {
        return getIdx() - o.getIdx();
    }
}
