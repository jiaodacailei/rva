package com.ruoyi.rva.framework.mapper;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaProperty;

/**
 * 属性Mapper接口
 * 
 * @author jiaodacailei
 * @date 2021-07-27
 */
public interface RvaPropertyMapper 
{
    /**
     * 查询属性
     * 
     * @param id 属性ID
     * @return 属性
     */
    public RvaProperty selectRvaPropertyById(String id);

    /**
     * 查询属性列表
     * 
     * @param rvaProperty 属性
     * @return 属性集合
     */
    public List<RvaProperty> selectRvaPropertyList(RvaProperty rvaProperty);

    /**
     * 新增属性
     * 
     * @param rvaProperty 属性
     * @return 结果
     */
    public int insertRvaProperty(RvaProperty rvaProperty);

    /**
     * 修改属性
     * 
     * @param rvaProperty 属性
     * @return 结果
     */
    public int updateRvaProperty(RvaProperty rvaProperty);

    /**
     * 删除属性
     * 
     * @param id 属性ID
     * @return 结果
     */
    public int deleteRvaPropertyById(String id);

    /**
     * 批量删除属性
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRvaPropertyByIds(String[] ids);
}
