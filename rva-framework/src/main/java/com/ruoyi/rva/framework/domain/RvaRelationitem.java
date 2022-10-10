package com.ruoyi.rva.framework.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 关系项对象 rva_relationitem
 * 
 * @author jiaodacailei
 * @date 2021-07-27
 */
public class RvaRelationitem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private String id;

    /** 关系项名称 */
    @Excel(name = "关系项名称")
    private String name;

    /** 关系ID */
    @Excel(name = "关系ID")
    private String relationId;

    /** 本方关联属性 */
    @Excel(name = "本方关联属性")
    private String propId;

    /** 反向关系项名称 */
    @Excel(name = "反向关系项名称")
    private String relatedName;

    /** 关联对象ID */
    @Excel(name = "关联对象ID")
    private String relatedObjId;

    /** 关联对象属性ID */
    @Excel(name = "关联对象属性ID")
    private String relatedPropId;

    /** 关系对象本方关联属性ID */
    @Excel(name = "关系对象本方关联属性ID")
    private String relationPropId;

    /** 关系对象他方关联属性ID */
    @Excel(name = "关系对象他方关联属性ID")
    private String relationInversePropId;

    /** 索引 */
    @Excel(name = "索引")
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
    public void setRelationId(String relationId) 
    {
        this.relationId = relationId;
    }

    public String getRelationId() 
    {
        return relationId;
    }
    public void setPropId(String propId) 
    {
        this.propId = propId;
    }

    public String getPropId() 
    {
        return propId;
    }
    public void setRelatedName(String relatedName) 
    {
        this.relatedName = relatedName;
    }

    public String getRelatedName() 
    {
        return relatedName;
    }
    public void setRelatedObjId(String relatedObjId) 
    {
        this.relatedObjId = relatedObjId;
    }

    public String getRelatedObjId() 
    {
        return relatedObjId;
    }
    public void setRelatedPropId(String relatedPropId) 
    {
        this.relatedPropId = relatedPropId;
    }

    public String getRelatedPropId() 
    {
        return relatedPropId;
    }
    public void setRelationPropId(String relationPropId) 
    {
        this.relationPropId = relationPropId;
    }

    public String getRelationPropId() 
    {
        return relationPropId;
    }
    public void setRelationInversePropId(String relationInversePropId) 
    {
        this.relationInversePropId = relationInversePropId;
    }

    public String getRelationInversePropId() 
    {
        return relationInversePropId;
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
            .append("relationId", getRelationId())
            .append("propId", getPropId())
            .append("relatedName", getRelatedName())
            .append("relatedObjId", getRelatedObjId())
            .append("relatedPropId", getRelatedPropId())
            .append("relationPropId", getRelationPropId())
            .append("relationInversePropId", getRelationInversePropId())
            .append("idx", getIdx())
            .append("data", getData())
            .toString();
    }
}
