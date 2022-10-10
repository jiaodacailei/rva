package com.ruoyi.rva.framework.mapper;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaObject;

/**
 * 对象Mapper接口
 * 
 * @author jiaodacailei
 * @date 2021-11-30
 */
public interface RvaObjectMapper 
{
    /**
     * 查询对象
     * 
     * @param id 对象主键
     * @return 对象
     */
    public RvaObject selectRvaObjectById(String id);

    /**
     * 查询对象列表
     * 
     * @param rvaObject 对象
     * @return 对象集合
     */
    public List<RvaObject> selectRvaObjectList(RvaObject rvaObject);

    /**
     * 新增对象
     * 
     * @param rvaObject 对象
     * @return 结果
     */
    public int insertRvaObject(RvaObject rvaObject);

    /**
     * 修改对象
     * 
     * @param rvaObject 对象
     * @return 结果
     */
    public int updateRvaObject(RvaObject rvaObject);

    /**
     * 删除对象
     * 
     * @param id 对象主键
     * @return 结果
     */
    public int deleteRvaObjectById(String id);

    /**
     * 批量删除对象
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRvaObjectByIds(String[] ids);
}
