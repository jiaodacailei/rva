package com.ruoyi.rva.framework.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.framework.mapper.RvaTenantMapper;
import com.ruoyi.rva.framework.domain.RvaTenant;
import com.ruoyi.rva.framework.service.IRvaTenantService;

/**
 * 租户Service业务层处理
 * 
 * @author jiaodacailei
 * @date 2022-03-05
 */
@Service
public class RvaTenantServiceImpl implements IRvaTenantService 
{
    @Autowired
    private RvaTenantMapper rvaTenantMapper;

    /**
     * 查询租户
     * 
     * @param id 租户主键
     * @return 租户
     */
    @Override
    public RvaTenant selectRvaTenantById(String id)
    {
        return rvaTenantMapper.selectRvaTenantById(id);
    }

    /**
     * 查询租户列表
     * 
     * @param rvaTenant 租户
     * @return 租户
     */
    @Override
    public List<RvaTenant> selectRvaTenantList(RvaTenant rvaTenant)
    {
        return rvaTenantMapper.selectRvaTenantList(rvaTenant);
    }

    /**
     * 新增租户
     * 
     * @param rvaTenant 租户
     * @return 结果
     */
    @Override
    public int insertRvaTenant(RvaTenant rvaTenant)
    {
        rvaTenant.setCreateTime(DateUtils.getNowDate());
        return rvaTenantMapper.insertRvaTenant(rvaTenant);
    }

    /**
     * 修改租户
     * 
     * @param rvaTenant 租户
     * @return 结果
     */
    @Override
    public int updateRvaTenant(RvaTenant rvaTenant)
    {
        rvaTenant.setUpdateTime(DateUtils.getNowDate());
        return rvaTenantMapper.updateRvaTenant(rvaTenant);
    }

    /**
     * 批量删除租户
     * 
     * @param ids 需要删除的租户主键
     * @return 结果
     */
    @Override
    public int deleteRvaTenantByIds(String[] ids)
    {
        return rvaTenantMapper.deleteRvaTenantByIds(ids);
    }

    /**
     * 删除租户信息
     * 
     * @param id 租户主键
     * @return 结果
     */
    @Override
    public int deleteRvaTenantById(String id)
    {
        return rvaTenantMapper.deleteRvaTenantById(id);
    }
}
