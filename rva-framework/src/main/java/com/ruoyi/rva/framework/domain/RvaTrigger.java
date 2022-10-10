package com.ruoyi.rva.framework.domain;

import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.rva.framework.mapper.RvaTriggeractionMapper;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;

import java.util.Collections;
import java.util.List;

/**
 * 触发器对象 rva_trigger
 * 
 * @author jiaodacailei
 * @date 2021-09-16
 */
public class RvaTrigger implements Comparable<RvaTrigger>
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private String id;

    /** 参数 */
    @Excel(name = "参数")
    private String params;

    /** 条件 */
    @Excel(name = "条件")
    private String triggerIf;

    /** 视图ID */
    @Excel(name = "视图ID")
    private String viewId;

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
    public void setParams(String params) 
    {
        this.params = params;
    }

    public String getParams() 
    {
        return params;
    }
    public void setTriggerIf(String triggerIf) 
    {
        this.triggerIf = triggerIf;
    }

    public String getTriggerIf() 
    {
        return triggerIf;
    }
    public void setViewId(String viewId) 
    {
        this.viewId = viewId;
    }

    public String getViewId() 
    {
        return viewId;
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
            .append("params", getParams())
            .append("triggerIf", getTriggerIf())
            .append("viewId", getViewId())
            .append("idx", getIdx())
            .append("data", getData())
            .toString();
    }

    @Override
    public int compareTo(RvaTrigger o) {
        return this.getIdx() - o.getIdx();
    }

    public List<RvaTriggeraction> getActions () {
        RvaTriggeraction search = new RvaTriggeraction();
        search.setTriggerId(getId());
        List<RvaTriggeraction> actions = SpringUtils.getBean(RvaTriggeractionMapper.class).selectRvaTriggeractionList(search);
        Collections.sort(actions);
        return actions;
    }
}
