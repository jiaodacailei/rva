package com.ruoyi.rva.framework.service;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaTenant;

/**
 * 租户Service接口
 * 
 * @author jiaodacailei
 * @date 2022-03-05
 */
public interface IRvaTenantService 
{
    /**
     * 查询租户
     * 
     * @param id 租户主键
     * @return 租户
     */
    public RvaTenant selectRvaTenantById(String id);

    /**
     * 查询租户列表
     * 
     * @param rvaTenant 租户
     * @return 租户集合
     */
    public List<RvaTenant> selectRvaTenantList(RvaTenant rvaTenant);

    /**
     * 新增租户
     * 
     * @param rvaTenant 租户
     * @return 结果
     */
    public int insertRvaTenant(RvaTenant rvaTenant);

    /**
     * 修改租户
     * 
     * @param rvaTenant 租户
     * @return 结果
     */
    public int updateRvaTenant(RvaTenant rvaTenant);

    /**
     * 批量删除租户
     * 
     * @param ids 需要删除的租户主键集合
     * @return 结果
     */
    public int deleteRvaTenantByIds(String[] ids);

    /**
     * 删除租户信息
     * 
     * @param id 租户主键
     * @return 结果
     */
    public int deleteRvaTenantById(String id);
}
