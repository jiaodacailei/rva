package com.ruoyi.rva.framework.service;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaTriggeraction;

/**
 * 触发动作Service接口
 * 
 * @author jiaodacailei
 * @date 2021-09-16
 */
public interface IRvaTriggeractionService 
{
    /**
     * 查询触发动作
     * 
     * @param id 触发动作ID
     * @return 触发动作
     */
    public RvaTriggeraction selectRvaTriggeractionById(String id);

    /**
     * 查询触发动作列表
     * 
     * @param rvaTriggeraction 触发动作
     * @return 触发动作集合
     */
    public List<RvaTriggeraction> selectRvaTriggeractionList(RvaTriggeraction rvaTriggeraction);

    /**
     * 新增触发动作
     * 
     * @param rvaTriggeraction 触发动作
     * @return 结果
     */
    public int insertRvaTriggeraction(RvaTriggeraction rvaTriggeraction);

    /**
     * 修改触发动作
     * 
     * @param rvaTriggeraction 触发动作
     * @return 结果
     */
    public int updateRvaTriggeraction(RvaTriggeraction rvaTriggeraction);

    /**
     * 批量删除触发动作
     * 
     * @param ids 需要删除的触发动作ID
     * @return 结果
     */
    public int deleteRvaTriggeractionByIds(String[] ids);

    /**
     * 删除触发动作信息
     * 
     * @param id 触发动作ID
     * @return 结果
     */
    public int deleteRvaTriggeractionById(String id);
}
