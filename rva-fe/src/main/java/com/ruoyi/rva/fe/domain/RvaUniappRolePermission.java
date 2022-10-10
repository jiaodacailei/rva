package com.ruoyi.rva.fe.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 应用权限角色关联对象 rva_uniapp_role_permission
 * 
 * @author jiaodacailei
 * @date 2022-05-12
 */
public class RvaUniappRolePermission extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 权限ID */
    @Excel(name = "权限ID")
    private String permissionId;

    /** 应用ID */
    @Excel(name = "应用ID")
    private String uniappId;

    /** 角色ID */
    @Excel(name = "角色ID")
    private Long roleId;

    public void setPermissionId(String permissionId) 
    {
        this.permissionId = permissionId;
    }

    public String getPermissionId() 
    {
        return permissionId;
    }
    public void setUniappId(String uniappId) 
    {
        this.uniappId = uniappId;
    }

    public String getUniappId() 
    {
        return uniappId;
    }
    public void setRoleId(Long roleId) 
    {
        this.roleId = roleId;
    }

    public Long getRoleId() 
    {
        return roleId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("permissionId", getPermissionId())
            .append("uniappId", getUniappId())
            .append("roleId", getRoleId())
            .toString();
    }
}
