package com.ruoyi.rva.activiti.service;

import java.util.List;
import com.ruoyi.rva.activiti.domain.RvaProcess;

/**
 * 流程定义Service接口
 * 
 * @author jiaodacailei
 * @date 2022-03-22
 */
public interface IRvaProcessService 
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
     * 批量删除流程定义
     * 
     * @param ids 需要删除的流程定义主键集合
     * @return 结果
     */
    public int deleteRvaProcessByIds(String[] ids);

    /**
     * 删除流程定义信息
     * 
     * @param id 流程定义主键
     * @return 结果
     */
    public int deleteRvaProcessById(String id);
}
