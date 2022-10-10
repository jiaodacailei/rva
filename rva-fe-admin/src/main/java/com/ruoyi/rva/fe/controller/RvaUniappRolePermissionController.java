package com.ruoyi.rva.fe.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.rva.fe.domain.RvaUniappRolePermission;
import com.ruoyi.rva.fe.service.IRvaUniappRolePermissionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 应用权限角色关联Controller
 * 
 * @author jiaodacailei
 * @date 2022-05-12
 */
@RestController
@RequestMapping("/fe/permission")
public class RvaUniappRolePermissionController extends BaseController
{
    @Autowired
    private IRvaUniappRolePermissionService rvaUniappRolePermissionService;

    /**
     * 查询应用权限角色关联列表
     */
    @PreAuthorize("@ss.hasPermi('fe:permission:list')")
    @GetMapping("/list")
    public TableDataInfo list(RvaUniappRolePermission rvaUniappRolePermission)
    {
        startPage();
        List<RvaUniappRolePermission> list = rvaUniappRolePermissionService.selectRvaUniappRolePermissionList(rvaUniappRolePermission);
        return getDataTable(list);
    }

    /**
     * 导出应用权限角色关联列表
     */
    @PreAuthorize("@ss.hasPermi('fe:permission:export')")
    @Log(title = "应用权限角色关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RvaUniappRolePermission rvaUniappRolePermission)
    {
        List<RvaUniappRolePermission> list = rvaUniappRolePermissionService.selectRvaUniappRolePermissionList(rvaUniappRolePermission);
        ExcelUtil<RvaUniappRolePermission> util = new ExcelUtil<RvaUniappRolePermission>(RvaUniappRolePermission.class);
        util.exportExcel(response, list, "应用权限角色关联数据");
    }

    /**
     * 获取应用权限角色关联详细信息
     */
    @PreAuthorize("@ss.hasPermi('fe:permission:query')")
    @GetMapping(value = "/{permissionId}")
    public AjaxResult getInfo(@PathVariable("permissionId") String permissionId)
    {
        return AjaxResult.success(rvaUniappRolePermissionService.selectRvaUniappRolePermissionByPermissionId(permissionId));
    }

    /**
     * 新增应用权限角色关联
     */
    @PreAuthorize("@ss.hasPermi('fe:permission:add')")
    @Log(title = "应用权限角色关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RvaUniappRolePermission rvaUniappRolePermission)
    {
        return toAjax(rvaUniappRolePermissionService.insertRvaUniappRolePermission(rvaUniappRolePermission));
    }

    /**
     * 修改应用权限角色关联
     */
    @PreAuthorize("@ss.hasPermi('fe:permission:edit')")
    @Log(title = "应用权限角色关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RvaUniappRolePermission rvaUniappRolePermission)
    {
        return toAjax(rvaUniappRolePermissionService.updateRvaUniappRolePermission(rvaUniappRolePermission));
    }

    /**
     * 删除应用权限角色关联
     */
    @PreAuthorize("@ss.hasPermi('fe:permission:remove')")
    @Log(title = "应用权限角色关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{permissionIds}")
    public AjaxResult remove(@PathVariable String[] permissionIds)
    {
        return toAjax(rvaUniappRolePermissionService.deleteRvaUniappRolePermissionByPermissionIds(permissionIds));
    }
}
