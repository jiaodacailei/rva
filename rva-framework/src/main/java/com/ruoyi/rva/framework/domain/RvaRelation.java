package com.ruoyi.rva.framework.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.rva.framework.mapper.RvaObjectMapper;
import com.ruoyi.rva.framework.mapper.RvaPropertyMapper;
import com.ruoyi.rva.framework.mapper.RvaRelationitemMapper;
import com.ruoyi.rva.framework.util.RvaUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 关系对象 rva_relation
 * 
 * @author jiaodacailei
 * @date 2021-07-27
 */
public class RvaRelation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**
     * 一对多
     */
    public final static String TYPE_12M = "12M";
    /**
     * 多对一
     */
    public final static String TYPE_M21 = "M21";
    /**
     * 一对多，实际一对一（父对子）
     */
    public final static String TYPE_1M = "1M";
    /**
     * 多对一，实际一对一（子对父）
     */
    public final static String TYPE_M1 = "M1";
    /**
     * 多对多
     */
    public final static String TYPE_M2M = "M2M";
    /**
     * 多对（复合对象：即多个对象）多，例如，email的收件人，有可能是多个用户，或者多个用户组，用户和用户组是不同的对象
     */
    public final static String TYPE_M2CM = "M2CM";
    /**
     * 多对（复合对象：即多个对象）一，类似于TYPE_M2CM
     */
    public final static String TYPE_M2C1 = "M2C1";

    /** ID */
    private String id;

    /** 关系名称 */
    @Excel(name = "关系名称")
    private String name;

    /** 类型 */
    @Excel(name = "类型")
    private String type;

    /** 对象ID */
    @Excel(name = "对象ID")
    private String objId;

    /** 关联对象属性ID */
    @Excel(name = "关联对象属性ID")
    private String propObjId;

    /** 反向关系名称 */
    @Excel(name = "反向关系名称")
    private String relatedName;

    /** 关系对象ID */
    @Excel(name = "关系对象ID")
    private String relationObjId;

    /** 关系对象关联对象属性ID */
    @Excel(name = "关系对象关联对象属性ID")
    private String relationObjPropId;

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
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setObjId(String objId) 
    {
        this.objId = objId;
    }

    public String getObjId() 
    {
        return objId;
    }
    public void setPropObjId(String propObjId) 
    {
        this.propObjId = propObjId;
    }

    public String getPropObjId() 
    {
        return propObjId;
    }
    public void setRelatedName(String relatedName) 
    {
        this.relatedName = relatedName;
    }

    public String getRelatedName() 
    {
        return relatedName;
    }
    public void setRelationObjId(String relationObjId) 
    {
        this.relationObjId = relationObjId;
    }

    public String getRelationObjId() 
    {
        return relationObjId;
    }
    public void setRelationObjPropId(String relationObjPropId) 
    {
        this.relationObjPropId = relationObjPropId;
    }

    public String getRelationObjPropId() 
    {
        return relationObjPropId;
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
            .append("type", getType())
            .append("objId", getObjId())
            .append("propObjId", getPropObjId())
            .append("relatedName", getRelatedName())
            .append("relationObjId", getRelationObjId())
            .append("relationObjPropId", getRelationObjPropId())
            .append("idx", getIdx())
            .append("data", getData())
            .toString();
    }

    private List<RvaRelationitem> items = null;

    public void loadData () {
        if (items == null) {
            RvaRelationitem search = new RvaRelationitem();
            search.setRelationId(getId());
            items = SpringUtils.getBean(RvaRelationitemMapper.class).selectRvaRelationitemList(search);
            Collections.sort(items, new Comparator<RvaRelationitem>() {
                @Override
                public int compare(RvaRelationitem o1, RvaRelationitem o2) {
                    return o1.getIdx() - o2.getIdx();
                }
            });
        }
    }

    public List<RvaRelationitem> getItems() {
        loadData();
        return items;
    }

    public static Boolean canLeftJoin (String type) {
        return Arrays.asList(TYPE_1M, TYPE_M1, TYPE_M21).contains(type);
    }

    @JsonIgnore
    public Boolean canLeftJoin () {
        return canLeftJoin(type);
    }

    public Boolean is12M () {
        return TYPE_12M.equals(type);
    }

    public Boolean isM2M () {
        return TYPE_M2M.equals(type);
    }

    public Boolean isM21 () {
        return TYPE_M21.equals(type);
    }

    @JsonIgnore
    public RvaObject getRelatedObj () {
        checkItems();
        return getObj (getItems().get(0).getRelatedObjId(), "关联对象");
    }

    @JsonIgnore
    public RvaObject getRelationObj () {
        checkItems();
        return getObj (getRelationObjId(), "关系对象");
    }

    private RvaObject getObj (String relatedObjId, String objName) {
        if (RvaUtils.isEmpty(relatedObjId)) {
            RvaUtils.throwNotSetException("关系项." + objName);
        }
        RvaObject relatedObj = SpringUtils.getBean(RvaObjectMapper.class).selectRvaObjectById(relatedObjId);
        if (relatedObj == null) {
            RvaUtils.throwQueryException(objName, objName + "ID=" + relatedObjId);
        }
        return relatedObj;
    }

    @JsonIgnore
    public RvaProperty getProp () {
        checkItems();
        return getProp(getItems().get(0).getPropId(), "本方属性");
    }

    @JsonIgnore
    public RvaProperty getRelatedProp () {
        checkItems();
        return getProp(getItems().get(0).getRelatedPropId(), "关联属性");
    }

    @JsonIgnore
    public RvaProperty getRelationInverseProp () {
        checkItems();
        return getProp(getItems().get(0).getRelationInversePropId(), "关系对象属性2");
    }

    @JsonIgnore
    public RvaProperty getRelationProp () {
        checkItems();
        return getProp(getItems().get(0).getRelationPropId(), "关系对象属性1");
    }

    private RvaProperty getProp (String propId, String propName) {
        if (RvaUtils.isEmpty(propId)) {
            RvaUtils.throwNotSetException("关系项." + propName);
        }
        RvaProperty prop = SpringUtils.getBean(RvaPropertyMapper.class).selectRvaPropertyById(propId);
        if (prop == null) {
            RvaUtils.throwQueryException(propName, propName + "ID=" + propId);
        }
        return prop;
    }

    private void checkItems() {
        if (getItems() == null || getItems().size() == 0) {
            RvaUtils.throwNotSetException("关系项");
        }
    }
}
