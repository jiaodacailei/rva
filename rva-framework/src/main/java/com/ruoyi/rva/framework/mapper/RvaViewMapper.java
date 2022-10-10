package com.ruoyi.rva.framework.mapper;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaView;

/**
 * 视图Mapper接口
 * 
 * @author jiaodacailei
 * @date 2021-07-27
 */
public interface RvaViewMapper 
{
    /**
     * 查询视图
     * 
     * @param id 视图ID
     * @return 视图
     */
    public RvaView selectRvaViewById(String id);

    /**
     * 查询视图列表
     * 
     * @param rvaView 视图
     * @return 视图集合
     */
    public List<RvaView> selectRvaViewList(RvaView rvaView);

    /**
     * 新增视图
     * 
     * @param rvaView 视图
     * @return 结果
     */
    public int insertRvaView(RvaView rvaView);

    /**
     * 修改视图
     * 
     * @param rvaView 视图
     * @return 结果
     */
    public int updateRvaView(RvaView rvaView);

    /**
     * 删除视图
     * 
     * @param id 视图ID
     * @return 结果
     */
    public int deleteRvaViewById(String id);

    /**
     * 批量删除视图
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRvaViewByIds(String[] ids);
}
