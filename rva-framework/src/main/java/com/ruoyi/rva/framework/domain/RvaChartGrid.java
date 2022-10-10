package com.ruoyi.rva.framework.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 图坐标系对象 rva_chart_grid
 * 
 * @author jiaodacailei
 * @date 2022-03-08
 */
public class RvaChartGrid extends RvaBaseEntity implements Comparable<RvaChartGrid>
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private String id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** top */
    @Excel(name = "top")
    private String gridTop;

    /** bottom */
    @Excel(name = "bottom")
    private String gridBottom;

    /** left */
    @Excel(name = "left")
    private String gridLeft;

    /** right */
    @Excel(name = "right")
    private String gridRight;

    /** width */
    @Excel(name = "width")
    private String gridWidth;

    /** height */
    @Excel(name = "height")
    private String gridHeight;

    /** x0轴 */
    @Excel(name = "x0轴")
    private String axisX0;

    /** x1轴 */
    @Excel(name = "x1轴")
    private String axisX1;

    /** y0轴 */
    @Excel(name = "y0轴")
    private String axisY0;

    /** y1轴 */
    @Excel(name = "y1轴")
    private String axisY1;

    /** 所属图表 */
    @Excel(name = "所属图表")
    private String chartId;

    /** 排序 */
    @Excel(name = "排序")
    private Integer idx;

    /** 其他数据 */
    @Excel(name = "其他数据")
    private String data;

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
    public void setGridTop(String gridTop) 
    {
        this.gridTop = gridTop;
    }

    public String getGridTop() 
    {
        return gridTop;
    }
    public void setGridBottom(String gridBottom) 
    {
        this.gridBottom = gridBottom;
    }

    public String getGridBottom() 
    {
        return gridBottom;
    }
    public void setGridLeft(String gridLeft) 
    {
        this.gridLeft = gridLeft;
    }

    public String getGridLeft() 
    {
        return gridLeft;
    }
    public void setGridRight(String gridRight) 
    {
        this.gridRight = gridRight;
    }

    public String getGridRight() 
    {
        return gridRight;
    }
    public void setGridWidth(String gridWidth) 
    {
        this.gridWidth = gridWidth;
    }

    public String getGridWidth() 
    {
        return gridWidth;
    }
    public void setGridHeight(String gridHeight) 
    {
        this.gridHeight = gridHeight;
    }

    public String getGridHeight() 
    {
        return gridHeight;
    }
    public void setAxisX0(String axisX0) 
    {
        this.axisX0 = axisX0;
    }

    public String getAxisX0() 
    {
        return axisX0;
    }
    public void setAxisX1(String axisX1) 
    {
        this.axisX1 = axisX1;
    }

    public String getAxisX1() 
    {
        return axisX1;
    }
    public void setAxisY0(String axisY0) 
    {
        this.axisY0 = axisY0;
    }

    public String getAxisY0() 
    {
        return axisY0;
    }
    public void setAxisY1(String axisY1) 
    {
        this.axisY1 = axisY1;
    }

    public String getAxisY1() 
    {
        return axisY1;
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
    public void setData(String data) 
    {
        this.data = data;
    }

    public String getData() 
    {
        return data;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("gridTop", getGridTop())
            .append("gridBottom", getGridBottom())
            .append("gridLeft", getGridLeft())
            .append("gridRight", getGridRight())
            .append("gridWidth", getGridWidth())
            .append("gridHeight", getGridHeight())
            .append("axisX0", getAxisX0())
            .append("axisX1", getAxisX1())
            .append("axisY0", getAxisY0())
            .append("axisY1", getAxisY1())
            .append("chartId", getChartId())
            .append("idx", getIdx())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("data", getData())
            .toString();
    }

    @Override
    public int compareTo(RvaChartGrid o) {
        return getIdx() - o.getIdx();
    }

    public static final String NAME = "chart_grid";
}
