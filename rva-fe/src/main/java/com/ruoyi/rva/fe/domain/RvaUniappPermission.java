package com.ruoyi.rva.fe.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 移动应用权限对象 rva_uniapp_permission
 *
 * @author jiaodacailei
 * @date 2022-05-12
 */
public class RvaUniappPermission extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 权限ID */
    private String id;

    /** 应用ID，不设则为公共权限 */
    @Excel(name = "应用ID，不设则为公共权限")
    private String uniappId;

    /** 菜单名称 */
    @Excel(name = "菜单名称")
    private String name;

    /** 父菜单ID */
    @Excel(name = "父菜单ID")
    private String parentId;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Integer orderNum;

    /** 路由地址 */
    @Excel(name = "路由地址")
    private String path;

    /** 路由参数 */
    @Excel(name = "路由参数")
    private String query;

    /** 是否为外链（0是 1否） */
    @Excel(name = "是否为外链", readConverterExp = "0=是,1=否")
    private Integer isFrame;

    /** 是否缓存（0缓存 1不缓存） */
    @Excel(name = "是否缓存", readConverterExp = "0=缓存,1=不缓存")
    private Integer isCache;

    /** 菜单类型 */
    @Excel(name = "菜单类型")
    private String menuType;

    /** 菜单状态 */
    @Excel(name = "菜单状态")
    private String visible;

    /** 菜单状态 */
    @Excel(name = "菜单状态")
    private String status;

    /** 菜单图标 */
    @Excel(name = "菜单图标")
    private String icon;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String tenantId;

    /** 其他数据 */
    @Excel(name = "其他数据")
    private String data;

    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    private List <RvaUniappPermission> children;

    public List<RvaUniappPermission> getChildren() {
        return children;
    }

    public void setChildren(List<RvaUniappPermission> children) {
        this.children = children;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setUniappId(String uniappId)
    {
        this.uniappId = uniappId;
    }

    public String getUniappId()
    {
        return uniappId;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }

    public String getParentId()
    {
        return parentId;
    }
    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
    }

    public Integer getOrderNum()
    {
        return orderNum;
    }
    public void setPath(String path)
    {
        this.path = path;
    }

    public String getPath()
    {
        return path;
    }
    public void setQuery(String query)
    {
        this.query = query;
    }

    public String getQuery()
    {
        return query;
    }
    public void setIsFrame(Integer isFrame)
    {
        this.isFrame = isFrame;
    }

    public Integer getIsFrame()
    {
        return isFrame;
    }
    public void setIsCache(Integer isCache)
    {
        this.isCache = isCache;
    }

    public Integer getIsCache()
    {
        return isCache;
    }
    public void setMenuType(String menuType)
    {
        this.menuType = menuType;
    }

    public String getMenuType()
    {
        return menuType;
    }
    public void setVisible(String visible)
    {
        this.visible = visible;
    }

    public String getVisible()
    {
        return visible;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getIcon()
    {
        return icon;
    }
    public void setTenantId(String tenantId)
    {
        this.tenantId = tenantId;
    }

    public String getTenantId()
    {
        return tenantId;
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
            .append("uniappId", getUniappId())
            .append("name", getName())
            .append("parentId", getParentId())
            .append("orderNum", getOrderNum())
            .append("path", getPath())
            .append("query", getQuery())
            .append("isFrame", getIsFrame())
            .append("isCache", getIsCache())
            .append("menuType", getMenuType())
            .append("visible", getVisible())
            .append("status", getStatus())
            .append("icon", getIcon())
            .append("remark", getRemark())
            .append("tenantId", getTenantId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("data", getData())
            .toString();
    }
}
