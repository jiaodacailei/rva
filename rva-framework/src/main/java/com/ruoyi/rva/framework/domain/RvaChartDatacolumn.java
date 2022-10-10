package com.ruoyi.rva.framework.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 图数据列对象 rva_chart_datacolumn
 * 
 * @author jiaodacailei
 * @date 2022-03-11
 */
public class RvaChartDatacolumn extends RvaBaseEntity implements Comparable<RvaChartDatacolumn>
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private String id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 类型 */
    @Excel(name = "类型")
    private String type;

    /** 列表属性，rva_chart_dataset.crud_id对应应用的列表视图属性ID */
    @Excel(name = "列表属性，rva_chart_dataset.crud_id对应应用的列表视图属性ID")
    private String viewpropertyId;

    /** 公式 */
    @Excel(name = "公式")
    private String formula;

    /** 选项字典 */
    @Excel(name = "选项字典")
    private String optionDict;

    /** 选项sql */
    @Excel(name = "选项sql")
    private String optionSql;

    /** 排序 */
    @Excel(name = "排序")
    private Integer idx;

    /** 数据集 */
    @Excel(name = "数据集")
    private String chartDatasetId;

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
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setViewpropertyId(String viewpropertyId) 
    {
        this.viewpropertyId = viewpropertyId;
    }

    public String getViewpropertyId() 
    {
        return viewpropertyId;
    }
    public void setFormula(String formula) 
    {
        this.formula = formula;
    }

    public String getFormula() 
    {
        return formula;
    }
    public void setOptionDict(String optionDict) 
    {
        this.optionDict = optionDict;
    }

    public String getOptionDict() 
    {
        return optionDict;
    }
    public void setOptionSql(String optionSql) 
    {
        this.optionSql = optionSql;
    }

    public String getOptionSql() 
    {
        return optionSql;
    }
    public void setIdx(Integer idx) 
    {
        this.idx = idx;
    }

    public Integer getIdx() 
    {
        return idx;
    }
    public void setChartDatasetId(String chartDatasetId) 
    {
        this.chartDatasetId = chartDatasetId;
    }

    public String getChartDatasetId() 
    {
        return chartDatasetId;
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
            .append("type", getType())
            .append("viewpropertyId", getViewpropertyId())
            .append("formula", getFormula())
            .append("optionDict", getOptionDict())
            .append("optionSql", getOptionSql())
            .append("idx", getIdx())
            .append("chartDatasetId", getChartDatasetId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("data", getData())
            .toString();
    }

    public static final String TYPE_CATEGORY = "category";

    public static final String TYPE_VALUE = "value";

    @Override
    public int compareTo(RvaChartDatacolumn o) {
        if (this.isCategory() && !o.isCategory()) {
            return -1;
        }
        return getIdx() - o.getIdx();
    }

    public Boolean isCategory () {
        return TYPE_CATEGORY.equals(this.type);
    }
}
