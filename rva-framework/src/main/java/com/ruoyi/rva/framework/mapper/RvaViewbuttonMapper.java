package com.ruoyi.rva.framework.mapper;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaViewbutton;

/**
 * 视图属性Mapper接口
 * 
 * @author jiaodacailei
 * @date 2021-07-27
 */
public interface RvaViewbuttonMapper 
{
    /**
     * 查询视图属性
     * 
     * @param id 视图属性ID
     * @return 视图属性
     */
    public RvaViewbutton selectRvaViewbuttonById(String id);

    /**
     * 查询视图属性列表
     * 
     * @param rvaViewbutton 视图属性
     * @return 视图属性集合
     */
    public List<RvaViewbutton> selectRvaViewbuttonList(RvaViewbutton rvaViewbutton);

    /**
     * 新增视图属性
     * 
     * @param rvaViewbutton 视图属性
     * @return 结果
     */
    public int insertRvaViewbutton(RvaViewbutton rvaViewbutton);

    /**
     * 修改视图属性
     * 
     * @param rvaViewbutton 视图属性
     * @return 结果
     */
    public int updateRvaViewbutton(RvaViewbutton rvaViewbutton);

    /**
     * 删除视图属性
     * 
     * @param id 视图属性ID
     * @return 结果
     */
    public int deleteRvaViewbuttonById(String id);

    /**
     * 批量删除视图属性
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRvaViewbuttonByIds(String[] ids);
}
