package com.ruoyi.rva.fe.service.impl;

import java.util.List;

import com.ruoyi.rva.fe.service.IRvaFeListItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.fe.mapper.RvaFeListItemMapper;
import com.ruoyi.rva.fe.domain.RvaFeListItem;

/**
 * 列项Service业务层处理
 * 
 * @author jiaodacailei
 * @date 2021-12-01
 */
@Service
public class RvaFeListItemServiceImpl implements IRvaFeListItemService
{
    @Autowired
    private RvaFeListItemMapper rvaFeListItemMapper;

    /**
     * 查询列项
     * 
     * @param id 列项主键
     * @return 列项
     */
    @Override
    public RvaFeListItem selectRvaFeListItemById(String id)
    {
        return rvaFeListItemMapper.selectRvaFeListItemById(id);
    }

    /**
     * 查询列项列表
     * 
     * @param rvaFeListItem 列项
     * @return 列项
     */
    @Override
    public List<RvaFeListItem> selectRvaFeListItemList(RvaFeListItem rvaFeListItem)
    {
        return rvaFeListItemMapper.selectRvaFeListItemList(rvaFeListItem);
    }

    /**
     * 新增列项
     * 
     * @param rvaFeListItem 列项
     * @return 结果
     */
    @Override
    public int insertRvaFeListItem(RvaFeListItem rvaFeListItem)
    {
        return rvaFeListItemMapper.insertRvaFeListItem(rvaFeListItem);
    }

    /**
     * 修改列项
     * 
     * @param rvaFeListItem 列项
     * @return 结果
     */
    @Override
    public int updateRvaFeListItem(RvaFeListItem rvaFeListItem)
    {
        return rvaFeListItemMapper.updateRvaFeListItem(rvaFeListItem);
    }

    /**
     * 批量删除列项
     * 
     * @param ids 需要删除的列项主键
     * @return 结果
     */
    @Override
    public int deleteRvaFeListItemByIds(String[] ids)
    {
        return rvaFeListItemMapper.deleteRvaFeListItemByIds(ids);
    }

    /**
     * 删除列项信息
     * 
     * @param id 列项主键
     * @return 结果
     */
    @Override
    public int deleteRvaFeListItemById(String id)
    {
        return rvaFeListItemMapper.deleteRvaFeListItemById(id);
    }
}
