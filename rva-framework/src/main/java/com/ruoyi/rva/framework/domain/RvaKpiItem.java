package com.ruoyi.rva.framework.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * KPI项对象 rva_kpi_item
 * 
 * @author jiaodacailei
 * @date 2022-03-22
 */
public class RvaKpiItem extends RvaBaseEntity implements Comparable<RvaKpiItem>
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private String id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 图标 */
    @Excel(name = "图标")
    private String icon;

    /** 值表达式 */
    @Excel(name = "值表达式")
    private String valueExpression;

    /** KPI，rva_kpi.id */
    @Excel(name = "KPI，rva_kpi.id")
    private String kpiId;

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
    public void setIcon(String icon) 
    {
        this.icon = icon;
    }

    public String getIcon() 
    {
        return icon;
    }
    public void setValueExpression(String valueExpression) 
    {
        this.valueExpression = valueExpression;
    }

    public String getValueExpression() 
    {
        return valueExpression;
    }
    public void setKpiId(String kpiId) 
    {
        this.kpiId = kpiId;
    }

    public String getKpiId() 
    {
        return kpiId;
    }
    public void setData(String data) 
    {
        this.data = data;
    }

    public String getData() 
    {
        return data;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    @Override
    public int compareTo(RvaKpiItem o) {
        return getIdx() - o.getIdx();
    }

    private String value = null;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
