package com.ruoyi.rva.framework.mapper;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaViewproperty;

/**
 * 视图属性Mapper接口
 * 
 * @author jiaodacailei
 * @date 2021-07-27
 */
public interface RvaViewpropertyMapper 
{
    /**
     * 查询视图属性
     * 
     * @param id 视图属性ID
     * @return 视图属性
     */
    public RvaViewproperty selectRvaViewpropertyById(String id);

    /**
     * 查询视图属性列表
     * 
     * @param rvaViewproperty 视图属性
     * @return 视图属性集合
     */
    public List<RvaViewproperty> selectRvaViewpropertyList(RvaViewproperty rvaViewproperty);

    /**
     * 新增视图属性
     * 
     * @param rvaViewproperty 视图属性
     * @return 结果
     */
    public int insertRvaViewproperty(RvaViewproperty rvaViewproperty);

    /**
     * 修改视图属性
     * 
     * @param rvaViewproperty 视图属性
     * @return 结果
     */
    public int updateRvaViewproperty(RvaViewproperty rvaViewproperty);

    /**
     * 删除视图属性
     * 
     * @param id 视图属性ID
     * @return 结果
     */
    public int deleteRvaViewpropertyById(String id);

    /**
     * 批量删除视图属性
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRvaViewpropertyByIds(String[] ids);
}
