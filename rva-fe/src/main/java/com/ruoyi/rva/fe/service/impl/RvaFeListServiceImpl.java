package com.ruoyi.rva.fe.service.impl;

import java.util.List;

import com.ruoyi.rva.fe.service.IRvaFeListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.fe.mapper.RvaFeListMapper;
import com.ruoyi.rva.fe.domain.RvaFeList;

/**
 * 列Service业务层处理
 * 
 * @author jiaodacailei
 * @date 2021-12-01
 */
@Service
public class RvaFeListServiceImpl implements IRvaFeListService
{
    @Autowired
    private RvaFeListMapper rvaFeListMapper;

    /**
     * 查询列
     * 
     * @param id 列主键
     * @return 列
     */
    @Override
    public RvaFeList selectRvaFeListById(String id)
    {
        return rvaFeListMapper.selectRvaFeListById(id);
    }

    /**
     * 查询列列表
     * 
     * @param rvaFeList 列
     * @return 列
     */
    @Override
    public List<RvaFeList> selectRvaFeListList(RvaFeList rvaFeList)
    {
        return rvaFeListMapper.selectRvaFeListList(rvaFeList);
    }

    /**
     * 新增列
     * 
     * @param rvaFeList 列
     * @return 结果
     */
    @Override
    public int insertRvaFeList(RvaFeList rvaFeList)
    {
        return rvaFeListMapper.insertRvaFeList(rvaFeList);
    }

    /**
     * 修改列
     * 
     * @param rvaFeList 列
     * @return 结果
     */
    @Override
    public int updateRvaFeList(RvaFeList rvaFeList)
    {
        return rvaFeListMapper.updateRvaFeList(rvaFeList);
    }

    /**
     * 批量删除列
     * 
     * @param ids 需要删除的列主键
     * @return 结果
     */
    @Override
    public int deleteRvaFeListByIds(String[] ids)
    {
        return rvaFeListMapper.deleteRvaFeListByIds(ids);
    }

    /**
     * 删除列信息
     * 
     * @param id 列主键
     * @return 结果
     */
    @Override
    public int deleteRvaFeListById(String id)
    {
        return rvaFeListMapper.deleteRvaFeListById(id);
    }
}
