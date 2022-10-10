package com.ruoyi.rva.activiti.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.activiti.mapper.RvaProcessMapper;
import com.ruoyi.rva.activiti.domain.RvaProcess;
import com.ruoyi.rva.activiti.service.IRvaProcessService;

/**
 * 流程定义Service业务层处理
 * 
 * @author jiaodacailei
 * @date 2022-03-22
 */
@Service
public class RvaProcessServiceImpl implements IRvaProcessService 
{
    @Autowired
    private RvaProcessMapper rvaProcessMapper;

    /**
     * 查询流程定义
     * 
     * @param id 流程定义主键
     * @return 流程定义
     */
    @Override
    public RvaProcess selectRvaProcessById(String id)
    {
        return rvaProcessMapper.selectRvaProcessById(id);
    }

    /**
     * 查询流程定义列表
     * 
     * @param rvaProcess 流程定义
     * @return 流程定义
     */
    @Override
    public List<RvaProcess> selectRvaProcessList(RvaProcess rvaProcess)
    {
        return rvaProcessMapper.selectRvaProcessList(rvaProcess);
    }

    /**
     * 新增流程定义
     * 
     * @param rvaProcess 流程定义
     * @return 结果
     */
    @Override
    public int insertRvaProcess(RvaProcess rvaProcess)
    {
        rvaProcess.setCreateTime(DateUtils.getNowDate());
        return rvaProcessMapper.insertRvaProcess(rvaProcess);
    }

    /**
     * 修改流程定义
     * 
     * @param rvaProcess 流程定义
     * @return 结果
     */
    @Override
    public int updateRvaProcess(RvaProcess rvaProcess)
    {
        rvaProcess.setUpdateTime(DateUtils.getNowDate());
        return rvaProcessMapper.updateRvaProcess(rvaProcess);
    }

    /**
     * 批量删除流程定义
     * 
     * @param ids 需要删除的流程定义主键
     * @return 结果
     */
    @Override
    public int deleteRvaProcessByIds(String[] ids)
    {
        return rvaProcessMapper.deleteRvaProcessByIds(ids);
    }

    /**
     * 删除流程定义信息
     * 
     * @param id 流程定义主键
     * @return 结果
     */
    @Override
    public int deleteRvaProcessById(String id)
    {
        return rvaProcessMapper.deleteRvaProcessById(id);
    }
}
