package com.ruoyi.rva.activiti.mapper;

import java.util.List;

import com.ruoyi.rva.activiti.domain.RvaProcessTask;

/**
 * 流程任务定义Mapper接口
 * 
 * @author jiaodacailei
 * @date 2022-03-22
 */
public interface RvaProcessTaskMapper 
{
    /**
     * 查询流程任务定义
     * 
     * @param id 流程任务定义主键
     * @return 流程任务定义
     */
    public RvaProcessTask selectRvaProcessTaskById(String id);

    /**
     * 查询流程任务定义列表
     * 
     * @param rvaProcessTask 流程任务定义
     * @return 流程任务定义集合
     */
    public List<RvaProcessTask> selectRvaProcessTaskList(RvaProcessTask rvaProcessTask);

    /**
     * 新增流程任务定义
     * 
     * @param rvaProcessTask 流程任务定义
     * @return 结果
     */
    public int insertRvaProcessTask(RvaProcessTask rvaProcessTask);

    /**
     * 修改流程任务定义
     * 
     * @param rvaProcessTask 流程任务定义
     * @return 结果
     */
    public int updateRvaProcessTask(RvaProcessTask rvaProcessTask);

    /**
     * 删除流程任务定义
     * 
     * @param id 流程任务定义主键
     * @return 结果
     */
    public int deleteRvaProcessTaskById(String id);

    /**
     * 批量删除流程任务定义
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRvaProcessTaskByIds(String[] ids);
}
