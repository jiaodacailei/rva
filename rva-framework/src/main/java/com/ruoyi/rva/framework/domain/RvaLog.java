package com.ruoyi.rva.framework.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruoyi.rva.framework.util.RvaUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 日志对象 rva_log
 * 
 * @author jiaodacailei
 * @date 2022-03-20
 */
public class RvaLog extends RvaBaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 视图 */
    @Excel(name = "视图")
    private String viewId;

    /** 对象 */
    @Excel(name = "对象")
    private String objId;

    /** 主键值，obj_id对应表的主键值 */
    @Excel(name = "主键值，obj_id对应表的主键值")
    private String keyValue;

    /** 外键值，obj_id对应表的外键值 */
    @Excel(name = "外键值，obj_id对应表的外键值")
    private String fkValue;

    /** 表单数据 */
    @Excel(name = "表单数据")
    private String formData;

    /** 视图数据 */
    @Excel(name = "表单数据")
    private String viewData;

    /** 其他数据 */
    @Excel(name = "其他数据")
    private String data;

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
    public void setViewId(String viewId) 
    {
        this.viewId = viewId;
    }

    public String getViewId() 
    {
        return viewId;
    }
    public void setObjId(String objId) 
    {
        this.objId = objId;
    }

    public String getObjId() 
    {
        return objId;
    }
    public void setKeyValue(String keyValue) 
    {
        this.keyValue = keyValue;
    }

    public String getKeyValue() 
    {
        return keyValue;
    }
    public void setFkValue(String fkValue) 
    {
        this.fkValue = fkValue;
    }

    public String getFkValue() 
    {
        return fkValue;
    }

    @JsonIgnore
    public String getFormData() {
        return formData;
    }

    public void setFormData(String formData) {
        this.formData = formData;
    }

    @JsonIgnore
    public String getViewData() {
        return viewData;
    }

    public void setViewData(String viewData) {
        this.viewData = viewData;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public String getData() 
    {
        return data;
    }

    private String table = "rva_log";

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        if (!"Y".equals(table)) {
            this.table = table;
        }
    }

    private String logWhere = null;

    public String getLogWhere() {
        return logWhere;
    }

    public void setLogWhere(String logWhere) {
        this.logWhere = logWhere;
    }

    public RvaMap<String, Object> getView () {
        return RvaUtils.parseMap(this.viewData);
    }

    public RvaMap<String, Object> getForm () {
        return RvaUtils.parseMap(this.formData);
    }
}
