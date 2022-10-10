package com.ruoyi.rva.activiti.mapper;

import java.util.List;

import com.ruoyi.rva.activiti.domain.RvaProcess;

/**
 * 流程定义Mapper接口
 * 
 * @author jiaodacailei
 * @date 2022-03-22
 */
public interface RvaProcessMapper 
{
    /**
     * 查询流程定义
     * 
     * @param id 流程定义主键
     * @return 流程定义
     */
    public RvaProcess selectRvaProcessById(String id);

    /**
     * 查询流程定义列表
     * 
     * @param rvaProcess 流程定义
     * @return 流程定义集合
     */
    public List<RvaProcess> selectRvaProcessList(RvaProcess rvaProcess);

    /**
     * 新增流程定义
     * 
     * @param rvaProcess 流程定义
     * @return 结果
     */
    public int insertRvaProcess(RvaProcess rvaProcess);

    /**
     * 修改流程定义
     * 
     * @param rvaProcess 流程定义
     * @return 结果
     */
    public int updateRvaProcess(RvaProcess rvaProcess);

    /**
     * 删除流程定义
     * 
     * @param id 流程定义主键
     * @return 结果
     */
    public int deleteRvaProcessById(String id);

    /**
     * 批量删除流程定义
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRvaProcessByIds(String[] ids);
}
