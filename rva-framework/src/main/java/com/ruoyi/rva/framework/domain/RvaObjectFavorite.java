package com.ruoyi.rva.framework.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 收藏对象 rva_object_favorite
 * 
 * @author jiaodacailei
 * @date 2021-12-01
 */
public class RvaObjectFavorite extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 对象ID，rva_object.id */
    @Excel(name = "对象ID，rva_object.id")
    private String objId;

    /** ID值，对象ID对应表中的ID值 */
    @Excel(name = "ID值，对象ID对应表中的ID值")
    private String objIdValue;

    /** 用户ID，sys_user.id */
    @Excel(name = "用户ID，sys_user.id")
    private Long userId;

    /** 类型（favorite-收藏 like-点赞 subscribe-预约 follow-关注 read-已读） */
    @Excel(name = "类型", readConverterExp = "f=avorite-收藏,l=ike-点赞,s=ubscribe-预约,f=ollow-关注,r=ead-已读")
    private String type;

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
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("objId", getObjId())
            .append("objIdValue", getObjIdValue())
            .append("userId", getUserId())
            .append("type", getType())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
