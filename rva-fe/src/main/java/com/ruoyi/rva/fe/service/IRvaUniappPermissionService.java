package com.ruoyi.rva.fe.service;

import java.util.List;
import com.ruoyi.rva.fe.domain.RvaUniappPermission;
import com.ruoyi.system.domain.vo.RouterVo;

/**
 * 移动应用权限Service接口
 *
 * @author jiaodacailei
 * @date 2022-05-12
 */
public interface IRvaUniappPermissionService
{
    /**
     * 查询移动应用权限
     *
     * @param id 移动应用权限主键
     * @return 移动应用权限
     */
    public RvaUniappPermission selectRvaUniappPermissionById(String id);

    /**
     * 查询移动应用权限列表
     *
     * @param rvaUniappPermission 移动应用权限
     * @return 移动应用权限集合
     */
    public List<RvaUniappPermission> selectRvaUniappPermissionList(RvaUniappPermission rvaUniappPermission);

    /**
     * 新增移动应用权限
     *
     * @param rvaUniappPermission 移动应用权限
     * @return 结果
     */
    public int insertRvaUniappPermission(RvaUniappPermission rvaUniappPermission);

    /**
     * 修改移动应用权限
     *
     * @param rvaUniappPermission 移动应用权限
     * @return 结果
     */
    public int updateRvaUniappPermission(RvaUniappPermission rvaUniappPermission);

    /**
     * 批量删除移动应用权限
     *
     * @param ids 需要删除的移动应用权限主键集合
     * @return 结果
     */
    public int deleteRvaUniappPermissionByIds(String[] ids);

    /**
     * 删除移动应用权限信息
     *
     * @param id 移动应用权限主键
     * @return 结果
     */
    public int deleteRvaUniappPermissionById(String id);

    List<RvaUniappPermission> selectMenuTreeByUserId(Long userId, String uniAppId);

    List<RvaUniappPermission> getChildPerms(List<RvaUniappPermission> list, String parentId);

    List<RouterVo> buildMenus(List<RvaUniappPermission> menus);
}
