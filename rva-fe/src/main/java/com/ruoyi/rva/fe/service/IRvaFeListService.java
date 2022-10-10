package com.ruoyi.rva.fe.service;

import java.util.List;
import com.ruoyi.rva.fe.domain.RvaFeList;

/**
 * 列Service接口
 * 
 * @author jiaodacailei
 * @date 2021-12-01
 */
public interface IRvaFeListService 
{
    /**
     * 查询列
     * 
     * @param id 列主键
     * @return 列
     */
    public RvaFeList selectRvaFeListById(String id);

    /**
     * 查询列列表
     * 
     * @param rvaFeList 列
     * @return 列集合
     */
    public List<RvaFeList> selectRvaFeListList(RvaFeList rvaFeList);

    /**
     * 新增列
     * 
     * @param rvaFeList 列
     * @return 结果
     */
    public int insertRvaFeList(RvaFeList rvaFeList);

    /**
     * 修改列
     * 
     * @param rvaFeList 列
     * @return 结果
     */
    public int updateRvaFeList(RvaFeList rvaFeList);

    /**
     * 批量删除列
     * 
     * @param ids 需要删除的列主键集合
     * @return 结果
     */
    public int deleteRvaFeListByIds(String[] ids);

    /**
     * 删除列信息
     * 
     * @param id 列主键
     * @return 结果
     */
    public int deleteRvaFeListById(String id);
}
