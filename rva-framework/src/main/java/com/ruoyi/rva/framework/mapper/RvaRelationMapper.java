package com.ruoyi.rva.framework.mapper;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaRelation;

/**
 * 关系Mapper接口
 * 
 * @author jiaodacailei
 * @date 2021-07-27
 */
public interface RvaRelationMapper 
{
    /**
     * 查询关系
     * 
     * @param id 关系ID
     * @return 关系
     */
    public RvaRelation selectRvaRelationById(String id);

    /**
     * 查询关系列表
     * 
     * @param rvaRelation 关系
     * @return 关系集合
     */
    public List<RvaRelation> selectRvaRelationList(RvaRelation rvaRelation);

    /**
     * 新增关系
     * 
     * @param rvaRelation 关系
     * @return 结果
     */
    public int insertRvaRelation(RvaRelation rvaRelation);

    /**
     * 修改关系
     * 
     * @param rvaRelation 关系
     * @return 结果
     */
    public int updateRvaRelation(RvaRelation rvaRelation);

    /**
     * 删除关系
     * 
     * @param id 关系ID
     * @return 结果
     */
    public int deleteRvaRelationById(String id);

    /**
     * 批量删除关系
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRvaRelationByIds(String[] ids);
}
