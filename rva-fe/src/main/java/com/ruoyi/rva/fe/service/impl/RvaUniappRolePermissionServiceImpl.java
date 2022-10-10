package com.ruoyi.rva.fe.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.fe.mapper.RvaUniappRolePermissionMapper;
import com.ruoyi.rva.fe.domain.RvaUniappRolePermission;
import com.ruoyi.rva.fe.service.IRvaUniappRolePermissionService;

/**
 * 应用权限角色关联Service业务层处理
 * 
 * @author jiaodacailei
 * @date 2022-05-12
 */
@Service
public class RvaUniappRolePermissionServiceImpl implements IRvaUniappRolePermissionService 
{
    @Autowired
    private RvaUniappRolePermissionMapper rvaUniappRolePermissionMapper;

    /**
     * 查询应用权限角色关联
     * 
     * @param permissionId 应用权限角色关联主键
     * @return 应用权限角色关联
     */
    @Override
    public RvaUniappRolePermission selectRvaUniappRolePermissionByPermissionId(String permissionId)
    {
        return rvaUniappRolePermissionMapper.selectRvaUniappRolePermissionByPermissionId(permissionId);
    }

    /**
     * 查询应用权限角色关联列表
     * 
     * @param rvaUniappRolePermission 应用权限角色关联
     * @return 应用权限角色关联
     */
    @Override
    public List<RvaUniappRolePermission> selectRvaUniappRolePermissionList(RvaUniappRolePermission rvaUniappRolePermission)
    {
        return rvaUniappRolePermissionMapper.selectRvaUniappRolePermissionList(rvaUniappRolePermission);
    }

    /**
     * 新增应用权限角色关联
     * 
     * @param rvaUniappRolePermission 应用权限角色关联
     * @return 结果
     */
    @Override
    public int insertRvaUniappRolePermission(RvaUniappRolePermission rvaUniappRolePermission)
    {
        return rvaUniappRolePermissionMapper.insertRvaUniappRolePermission(rvaUniappRolePermission);
    }

    /**
     * 修改应用权限角色关联
     * 
     * @param rvaUniappRolePermission 应用权限角色关联
     * @return 结果
     */
    @Override
    public int updateRvaUniappRolePermission(RvaUniappRolePermission rvaUniappRolePermission)
    {
        return rvaUniappRolePermissionMapper.updateRvaUniappRolePermission(rvaUniappRolePermission);
    }

    /**
     * 批量删除应用权限角色关联
     * 
     * @param permissionIds 需要删除的应用权限角色关联主键
     * @return 结果
     */
    @Override
    public int deleteRvaUniappRolePermissionByPermissionIds(String[] permissionIds)
    {
        return rvaUniappRolePermissionMapper.deleteRvaUniappRolePermissionByPermissionIds(permissionIds);
    }

    /**
     * 删除应用权限角色关联信息
     * 
     * @param permissionId 应用权限角色关联主键
     * @return 结果
     */
    @Override
    public int deleteRvaUniappRolePermissionByPermissionId(String permissionId)
    {
        return rvaUniappRolePermissionMapper.deleteRvaUniappRolePermissionByPermissionId(permissionId);
    }
}
