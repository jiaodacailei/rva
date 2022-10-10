package com.ruoyi.rva.fe.service;

import java.util.List;
import com.ruoyi.rva.fe.domain.RvaFeListItem;

/**
 * 列项Service接口
 * 
 * @author jiaodacailei
 * @date 2021-12-01
 */
public interface IRvaFeListItemService 
{
    /**
     * 查询列项
     * 
     * @param id 列项主键
     * @return 列项
     */
    public RvaFeListItem selectRvaFeListItemById(String id);

    /**
     * 查询列项列表
     * 
     * @param rvaFeListItem 列项
     * @return 列项集合
     */
    public List<RvaFeListItem> selectRvaFeListItemList(RvaFeListItem rvaFeListItem);

    /**
     * 新增列项
     * 
     * @param rvaFeListItem 列项
     * @return 结果
     */
    public int insertRvaFeListItem(RvaFeListItem rvaFeListItem);

    /**
     * 修改列项
     * 
     * @param rvaFeListItem 列项
     * @return 结果
     */
    public int updateRvaFeListItem(RvaFeListItem rvaFeListItem);

    /**
     * 批量删除列项
     * 
     * @param ids 需要删除的列项主键集合
     * @return 结果
     */
    public int deleteRvaFeListItemByIds(String[] ids);

    /**
     * 删除列项信息
     * 
     * @param id 列项主键
     * @return 结果
     */
    public int deleteRvaFeListItemById(String id);
}
