package com.ruoyi.rva.framework.service;

import java.util.List;

import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaRelation;
import com.ruoyi.rva.framework.util.RvaUtils;

/**
 * 关系Service接口
 * 
 * @author jiaodacailei
 * @date 2021-07-27
 */
public interface IRvaRelationService 
{

    default String createId(String selfObjId, String relObjId, String type) {
        return "r_" + selfObjId + "_" + type + "_" + relObjId + "_" + RvaUtils.generateKey32(6);
    }

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
     * 批量删除关系
     * 
     * @param ids 需要删除的关系ID
     * @return 结果
     */
    public int deleteRvaRelationByIds(String[] ids);

    /**
     * 删除关系信息
     * 
     * @param id 关系ID
     * @return 结果
     */
    public int deleteRvaRelationById(String id);

    void quickCreate(RvaMap<String, Object> req);

    void reverse(String relationId);

    RvaRelation createM21 (String objId, String relObjId);

    /**
     * 删除对象（包括关联对象）的字段、关系表、对象属性、对象关系、视图属性、视图触发器等
     * @param id 对象关系ID
     */
    void delete(String id);
}
