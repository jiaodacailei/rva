package com.ruoyi.rva.framework.mapper;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaRelationitem;

/**
 * 关系项Mapper接口
 * 
 * @author jiaodacailei
 * @date 2021-07-27
 */
public interface RvaRelationitemMapper 
{
    /**
     * 查询关系项
     * 
     * @param id 关系项ID
     * @return 关系项
     */
    public RvaRelationitem selectRvaRelationitemById(String id);

    /**
     * 查询关系项列表
     * 
     * @param rvaRelationitem 关系项
     * @return 关系项集合
     */
    public List<RvaRelationitem> selectRvaRelationitemList(RvaRelationitem rvaRelationitem);

    /**
     * 新增关系项
     * 
     * @param rvaRelationitem 关系项
     * @return 结果
     */
    public int insertRvaRelationitem(RvaRelationitem rvaRelationitem);

    /**
     * 修改关系项
     * 
     * @param rvaRelationitem 关系项
     * @return 结果
     */
    public int updateRvaRelationitem(RvaRelationitem rvaRelationitem);

    /**
     * 删除关系项
     * 
     * @param id 关系项ID
     * @return 结果
     */
    public int deleteRvaRelationitemById(String id);

    /**
     * 批量删除关系项
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRvaRelationitemByIds(String[] ids);
}
