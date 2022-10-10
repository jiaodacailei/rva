package com.ruoyi.rva.framework.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 触发动作对象 rva_triggeraction
 * 
 * @author jiaodacailei
 * @date 2021-09-16
 */
public class RvaTriggeraction extends BaseEntity implements Comparable<RvaTriggeraction>
{
    private static final long serialVersionUID = 1L;

    public static final String ACTION_SET_VALUE = "setValue";

    public static final String ACTION_SHOW = "show";

    public static final String ACTION_HIDE = "hide";

    public static final String ACTION_ENABLE = "enable";

    public static final String ACTION_DISABLE = "disable";


    /** ID */
    private String id;

    /** 动作对象 */
    @Excel(name = "动作对象")
    private String actionSubject;

    /** 动作 */
    @Excel(name = "动作")
    private String action;

    /** 动作参数 */
    @Excel(name = "动作参数")
    private String actionParams;

    /** 其他数据 */
    @Excel(name = "其他数据")
    private String data;

    /** 索引 */
    @Excel(name = "索引")
    private Integer idx;

    /** 触发器ID */
    @Excel(name = "触发器ID")
    private String triggerId;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setActionSubject(String actionSubject) 
    {
        this.actionSubject = actionSubject;
    }

    public String getActionSubject() 
    {
        return actionSubject;
    }
    public void setAction(String action) 
    {
        this.action = action;
    }

    public String getAction() 
    {
        return action;
    }
    public void setActionParams(String actionParams) 
    {
        this.actionParams = actionParams;
    }

    public String getActionParams() 
    {
        return actionParams;
    }
    public void setData(String data) 
    {
        this.data = data;
    }

    public String getData() 
    {
        return data;
    }
    public void setIdx(Integer idx) 
    {
        this.idx = idx;
    }

    public Integer getIdx() 
    {
        return idx;
    }
    public void setTriggerId(String triggerId) 
    {
        this.triggerId = triggerId;
    }

    public String getTriggerId() 
    {
        return triggerId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("actionSubject", getActionSubject())
            .append("action", getAction())
            .append("actionParams", getActionParams())
            .append("data", getData())
            .append("idx", getIdx())
            .append("triggerId", getTriggerId())
            .toString();
    }

    @Override
    public int compareTo(RvaTriggeraction o) {
        return this.idx - o.getIdx();
    }
}
