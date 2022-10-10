package com.ruoyi.rva.framework.service.impl;

import java.util.List;

import com.ruoyi.rva.framework.domain.RvaTriggeraction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.framework.mapper.RvaTriggeractionMapper;
import com.ruoyi.rva.framework.service.IRvaTriggeractionService;

/**
 * 触发动作Service业务层处理
 * 
 * @author jiaodacailei
 * @date 2021-09-16
 */
@Service
public class RvaTriggeractionServiceImpl implements IRvaTriggeractionService 
{
    @Autowired
    private RvaTriggeractionMapper rvaTriggeractionMapper;

    /**
     * 查询触发动作
     * 
     * @param id 触发动作ID
     * @return 触发动作
     */
    @Override
    public RvaTriggeraction selectRvaTriggeractionById(String id)
    {
        return rvaTriggeractionMapper.selectRvaTriggeractionById(id);
    }

    /**
     * 查询触发动作列表
     * 
     * @param rvaTriggeraction 触发动作
     * @return 触发动作
     */
    @Override
    public List<RvaTriggeraction> selectRvaTriggeractionList(RvaTriggeraction rvaTriggeraction)
    {
        return rvaTriggeractionMapper.selectRvaTriggeractionList(rvaTriggeraction);
    }

    /**
     * 新增触发动作
     * 
     * @param rvaTriggeraction 触发动作
     * @return 结果
     */
    @Override
    public int insertRvaTriggeraction(RvaTriggeraction rvaTriggeraction)
    {
        return rvaTriggeractionMapper.insertRvaTriggeraction(rvaTriggeraction);
    }

    /**
     * 修改触发动作
     * 
     * @param rvaTriggeraction 触发动作
     * @return 结果
     */
    @Override
    public int updateRvaTriggeraction(RvaTriggeraction rvaTriggeraction)
    {
        return rvaTriggeractionMapper.updateRvaTriggeraction(rvaTriggeraction);
    }

    /**
     * 批量删除触发动作
     * 
     * @param ids 需要删除的触发动作ID
     * @return 结果
     */
    @Override
    public int deleteRvaTriggeractionByIds(String[] ids)
    {
        return rvaTriggeractionMapper.deleteRvaTriggeractionByIds(ids);
    }

    /**
     * 删除触发动作信息
     * 
     * @param id 触发动作ID
     * @return 结果
     */
    @Override
    public int deleteRvaTriggeractionById(String id)
    {
        return rvaTriggeractionMapper.deleteRvaTriggeractionById(id);
    }
}
