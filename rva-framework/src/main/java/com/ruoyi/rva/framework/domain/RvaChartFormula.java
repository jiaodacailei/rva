package com.ruoyi.rva.framework.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 图公式对象 rva_chart_formula
 * 
 * @author jiaodacailei
 * @date 2022-03-04
 */
public class RvaChartFormula extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 其他数据 */
    @Excel(name = "其他数据")
    private String data;

    /** 类型 */
    @Excel(name = "类型")
    private String type;

    /** 适用类型 */
    @Excel(name = "适用类型")
    private String applicableTo;

    /** 公式 */
    @Excel(name = "公式")
    private String formula;

    /** 说明 */
    @Excel(name = "说明")
    private String description;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
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
    public void setApplicableTo(String applicableTo) 
    {
        this.applicableTo = applicableTo;
    }

    public String getApplicableTo() 
    {
        return applicableTo;
    }
    public void setFormula(String formula) 
    {
        this.formula = formula;
    }

    public String getFormula() 
    {
        return formula;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
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
            .append("applicableTo", getApplicableTo())
            .append("formula", getFormula())
            .append("description", getDescription())
            .toString();
    }
}
