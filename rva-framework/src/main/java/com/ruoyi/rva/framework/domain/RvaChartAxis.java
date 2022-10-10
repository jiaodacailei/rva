package com.ruoyi.rva.framework.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 图轴对象 rva_chart_axis
 * 
 * @author jiaodacailei
 * @date 2022-03-05
 */
public class RvaChartAxis extends RvaBaseEntity implements Comparable<RvaChartAxis>
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private String id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 数据模式(column row)，仅type=category */
    @Excel(name = "数据模式(column row)，仅type=category")
    private String dataType;

    /** 数据索引，rva_chart_dataset返回结果集的行或者列索引，仅type=category */
    @Excel(name = "数据索引，rva_chart_dataset返回结果集的行或者列索引，仅type=category")
    private Integer dataIndex;

    /** 数据集，仅type=category */
    @Excel(name = "数据集，仅type=category")
    private String chartDatasetId;

    /** 坐标系 */
    @Excel(name = "坐标系")
    private String chartGridId;

    /** 其他数据 */
    @Excel(name = "其他数据")
    private String data;

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
    public void setChartDatasetId(String chartDatasetId) 
    {
        this.chartDatasetId = chartDatasetId;
    }

    public String getChartDatasetId() 
    {
        return chartDatasetId;
    }
    public void setChartGridId(String chartGridId) 
    {
        this.chartGridId = chartGridId;
    }

    public String getChartGridId() 
    {
        return chartGridId;
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
            .append("dataType", getDataType())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("dataIndex", getDataIndex())
            .append("updateTime", getUpdateTime())
            .append("chartDatasetId", getChartDatasetId())
            .append("chartGridId", getChartGridId())
            .append("data", getData())
            .append("type", getType())
            .append("chartId", getChartId())
            .append("idx", getIdx())
            .toString();
    }

    public static final String NAME = "chart_axis";

    @Override
    public int compareTo(RvaChartAxis o) {
        return this.getIdx() - o.getIdx();
    }

}
