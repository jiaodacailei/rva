package com.ruoyi.rva.fe.mapper;

import java.util.List;
import com.ruoyi.rva.fe.domain.RvaUniappPermission;
import org.apache.ibatis.annotations.Param;

/**
 * 移动应用权限Mapper接口
 *
 * @author jiaodacailei
 * @date 2022-05-12
 */
public interface RvaUniappPermissionMapper
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
     * 删除移动应用权限
     *
     * @param id 移动应用权限主键
     * @return 结果
     */
    public int deleteRvaUniappPermissionById(String id);

    /**
     * 批量删除移动应用权限
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRvaUniappPermissionByIds(String[] ids);

    List<RvaUniappPermission> selectList(@Param("userId") Long userId, @Param("uniAppId")String uniAppId);
}
