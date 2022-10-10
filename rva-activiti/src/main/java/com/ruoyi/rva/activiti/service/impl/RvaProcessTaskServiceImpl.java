package com.ruoyi.rva.activiti.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.activiti.mapper.RvaProcessTaskMapper;
import com.ruoyi.rva.activiti.domain.RvaProcessTask;
import com.ruoyi.rva.activiti.service.IRvaProcessTaskService;

/**
 * 流程任务定义Service业务层处理
 * 
 * @author jiaodacailei
 * @date 2022-03-22
 */
@Service
public class RvaProcessTaskServiceImpl implements IRvaProcessTaskService 
{
    @Autowired
    private RvaProcessTaskMapper rvaProcessTaskMapper;

    /**
     * 查询流程任务定义
     * 
     * @param id 流程任务定义主键
     * @return 流程任务定义
     */
    @Override
    public RvaProcessTask selectRvaProcessTaskById(String id)
    {
        return rvaProcessTaskMapper.selectRvaProcessTaskById(id);
    }

    /**
     * 查询流程任务定义列表
     * 
     * @param rvaProcessTask 流程任务定义
     * @return 流程任务定义
     */
    @Override
    public List<RvaProcessTask> selectRvaProcessTaskList(RvaProcessTask rvaProcessTask)
    {
        return rvaProcessTaskMapper.selectRvaProcessTaskList(rvaProcessTask);
    }

    /**
     * 新增流程任务定义
     * 
     * @param rvaProcessTask 流程任务定义
     * @return 结果
     */
    @Override
    public int insertRvaProcessTask(RvaProcessTask rvaProcessTask)
    {
        rvaProcessTask.setCreateTime(DateUtils.getNowDate());
        return rvaProcessTaskMapper.insertRvaProcessTask(rvaProcessTask);
    }

    /**
     * 修改流程任务定义
     * 
     * @param rvaProcessTask 流程任务定义
     * @return 结果
     */
    @Override
    public int updateRvaProcessTask(RvaProcessTask rvaProcessTask)
    {
        rvaProcessTask.setUpdateTime(DateUtils.getNowDate());
        return rvaProcessTaskMapper.updateRvaProcessTask(rvaProcessTask);
    }

    /**
     * 批量删除流程任务定义
     * 
     * @param ids 需要删除的流程任务定义主键
     * @return 结果
     */
    @Override
    public int deleteRvaProcessTaskByIds(String[] ids)
    {
        return rvaProcessTaskMapper.deleteRvaProcessTaskByIds(ids);
    }

    /**
     * 删除流程任务定义信息
     * 
     * @param id 流程任务定义主键
     * @return 结果
     */
    @Override
    public int deleteRvaProcessTaskById(String id)
    {
        return rvaProcessTaskMapper.deleteRvaProcessTaskById(id);
    }
}
