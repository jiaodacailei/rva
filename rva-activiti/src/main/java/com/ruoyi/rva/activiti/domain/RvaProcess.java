package com.ruoyi.rva.activiti.domain;

import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.rva.activiti.mapper.RvaProcessTaskMapper;
import com.ruoyi.rva.framework.domain.RvaBaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;

import java.util.Collections;
import java.util.List;

/**
 * 流程定义对象 rva_process
 * 
 * @author jiaodacailei
 * @date 2022-03-22
 */
public class RvaProcess extends RvaBaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private String id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 关联对象，rva_object.id */
    @Excel(name = "关联对象，rva_object.id")
    private String relatedObjId;

    /** 所属对象，rva_object.id */
    @Excel(name = "所属对象，rva_object.id")
    private String objId;

    /** 模块，rva_module.name */
    @Excel(name = "模块，rva_module.name")
    private String module;

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
    public void setRelatedObjId(String relatedObjId) 
    {
        this.relatedObjId = relatedObjId;
    }

    public String getRelatedObjId() 
    {
        return relatedObjId;
    }
    public void setModule(String module) 
    {
        this.module = module;
    }

    public String getModule() 
    {
        return module;
    }
    public void setData(String data) 
    {
        this.data = data;
    }

    public String getData() 
    {
        return data;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    private List<RvaProcessTask> tasks = null;

    public RvaProcess loadData() {
        if (tasks == null) {
            RvaProcessTask search = new RvaProcessTask();
            search.setProcessId(this.getId());
            tasks = SpringUtils.getBean(RvaProcessTaskMapper.class).selectRvaProcessTaskList(search);
            Collections.sort(tasks);
        }
        return this;
    }

    public List<RvaProcessTask> getTasks() {
        loadData();
        return tasks;
    }

    public void setTasks(List<RvaProcessTask> tasks) {
        this.tasks = tasks;
    }
}
