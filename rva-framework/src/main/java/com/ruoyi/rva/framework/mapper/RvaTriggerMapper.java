package com.ruoyi.rva.framework.mapper;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaTrigger;

/**
 * 触发器Mapper接口
 * 
 * @author jiaodacailei
 * @date 2021-09-16
 */
public interface RvaTriggerMapper 
{
    /**
     * 查询触发器
     * 
     * @param id 触发器ID
     * @return 触发器
     */
    public RvaTrigger selectRvaTriggerById(String id);

    /**
     * 查询触发器列表
     * 
     * @param rvaTrigger 触发器
     * @return 触发器集合
     */
    public List<RvaTrigger> selectRvaTriggerList(RvaTrigger rvaTrigger);

    /**
     * 新增触发器
     * 
     * @param rvaTrigger 触发器
     * @return 结果
     */
    public int insertRvaTrigger(RvaTrigger rvaTrigger);

    /**
     * 修改触发器
     * 
     * @param rvaTrigger 触发器
     * @return 结果
     */
    public int updateRvaTrigger(RvaTrigger rvaTrigger);

    /**
     * 删除触发器
     * 
     * @param id 触发器ID
     * @return 结果
     */
    public int deleteRvaTriggerById(String id);

    /**
     * 批量删除触发器
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRvaTriggerByIds(String[] ids);
}
