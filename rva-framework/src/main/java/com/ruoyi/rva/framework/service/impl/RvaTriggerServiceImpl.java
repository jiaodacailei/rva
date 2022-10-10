package com.ruoyi.rva.framework.service.impl;

import java.util.List;

import com.ruoyi.rva.framework.domain.RvaTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.framework.mapper.RvaTriggerMapper;
import com.ruoyi.rva.framework.service.IRvaTriggerService;

/**
 * 触发器Service业务层处理
 * 
 * @author jiaodacailei
 * @date 2021-09-16
 */
@Service
public class RvaTriggerServiceImpl implements IRvaTriggerService 
{
    @Autowired
    private RvaTriggerMapper rvaTriggerMapper;

    /**
     * 查询触发器
     * 
     * @param id 触发器ID
     * @return 触发器
     */
    @Override
    public RvaTrigger selectRvaTriggerById(String id)
    {
        return rvaTriggerMapper.selectRvaTriggerById(id);
    }

    /**
     * 查询触发器列表
     * 
     * @param rvaTrigger 触发器
     * @return 触发器
     */
    @Override
    public List<RvaTrigger> selectRvaTriggerList(RvaTrigger rvaTrigger)
    {
        return rvaTriggerMapper.selectRvaTriggerList(rvaTrigger);
    }

    /**
     * 新增触发器
     * 
     * @param rvaTrigger 触发器
     * @return 结果
     */
    @Override
    public int insertRvaTrigger(RvaTrigger rvaTrigger)
    {
        return rvaTriggerMapper.insertRvaTrigger(rvaTrigger);
    }

    /**
     * 修改触发器
     * 
     * @param rvaTrigger 触发器
     * @return 结果
     */
    @Override
    public int updateRvaTrigger(RvaTrigger rvaTrigger)
    {
        return rvaTriggerMapper.updateRvaTrigger(rvaTrigger);
    }

    /**
     * 批量删除触发器
     * 
     * @param ids 需要删除的触发器ID
     * @return 结果
     */
    @Override
    public int deleteRvaTriggerByIds(String[] ids)
    {
        return rvaTriggerMapper.deleteRvaTriggerByIds(ids);
    }

    /**
     * 删除触发器信息
     * 
     * @param id 触发器ID
     * @return 结果
     */
    @Override
    public int deleteRvaTriggerById(String id)
    {
        return rvaTriggerMapper.deleteRvaTriggerById(id);
    }
}
