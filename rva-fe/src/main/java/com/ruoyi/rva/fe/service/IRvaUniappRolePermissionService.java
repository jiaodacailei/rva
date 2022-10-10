package com.ruoyi.rva.fe.service;

import java.util.List;
import com.ruoyi.rva.fe.domain.RvaUniappRolePermission;

/**
 * 应用权限角色关联Service接口
 * 
 * @author jiaodacailei
 * @date 2022-05-12
 */
public interface IRvaUniappRolePermissionService 
{
    /**
     * 查询应用权限角色关联
     * 
     * @param permissionId 应用权限角色关联主键
     * @return 应用权限角色关联
     */
    public RvaUniappRolePermission selectRvaUniappRolePermissionByPermissionId(String permissionId);

    /**
     * 查询应用权限角色关联列表
     * 
     * @param rvaUniappRolePermission 应用权限角色关联
     * @return 应用权限角色关联集合
     */
    public List<RvaUniappRolePermission> selectRvaUniappRolePermissionList(RvaUniappRolePermission rvaUniappRolePermission);

    /**
     * 新增应用权限角色关联
     * 
     * @param rvaUniappRolePermission 应用权限角色关联
     * @return 结果
     */
    public int insertRvaUniappRolePermission(RvaUniappRolePermission rvaUniappRolePermission);

    /**
     * 修改应用权限角色关联
     * 
     * @param rvaUniappRolePermission 应用权限角色关联
     * @return 结果
     */
    public int updateRvaUniappRolePermission(RvaUniappRolePermission rvaUniappRolePermission);

    /**
     * 批量删除应用权限角色关联
     * 
     * @param permissionIds 需要删除的应用权限角色关联主键集合
     * @return 结果
     */
    public int deleteRvaUniappRolePermissionByPermissionIds(String[] permissionIds);

    /**
     * 删除应用权限角色关联信息
     * 
     * @param permissionId 应用权限角色关联主键
     * @return 结果
     */
    public int deleteRvaUniappRolePermissionByPermissionId(String permissionId);
}
