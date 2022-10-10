package com.ruoyi.rva.framework.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 评论点赞对象 rva_object_comment_like
 * 
 * @author jiaodacailei
 * @date 2021-12-01
 */
public class RvaObjectCommentLike extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 对象ID，rva_object.id */
    @Excel(name = "对象ID，rva_object.id")
    private String objId;

    /** 评论ID，关联rva_object_comment.id */
    @Excel(name = "评论ID，关联rva_object_comment.id")
    private String objIdValue;

    /** 用户ID，sys_user.id */
    @Excel(name = "用户ID，sys_user.id")
    private Long userId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setObjId(String objId) 
    {
        this.objId = objId;
    }

    public String getObjId() 
    {
        return objId;
    }
    public void setObjIdValue(String objIdValue) 
    {
        this.objIdValue = objIdValue;
    }

    public String getObjIdValue() 
    {
        return objIdValue;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("objId", getObjId())
            .append("objIdValue", getObjIdValue())
            .append("userId", getUserId())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
