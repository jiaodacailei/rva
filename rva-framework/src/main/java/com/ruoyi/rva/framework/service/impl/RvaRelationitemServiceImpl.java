package com.ruoyi.rva.framework.service.impl;

import java.util.List;

import com.ruoyi.rva.framework.domain.RvaRelationitem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.framework.mapper.RvaRelationitemMapper;
import com.ruoyi.rva.framework.service.IRvaRelationitemService;

/**
 * 关系项Service业务层处理
 * 
 * @author jiaodacailei
 * @date 2021-07-27
 */
@Service
public class RvaRelationitemServiceImpl implements IRvaRelationitemService 
{
    @Autowired
    private RvaRelationitemMapper rvaRelationitemMapper;

    /**
     * 查询关系项
     * 
     * @param id 关系项ID
     * @return 关系项
     */
    @Override
    public RvaRelationitem selectRvaRelationitemById(String id)
    {
        return rvaRelationitemMapper.selectRvaRelationitemById(id);
    }

    /**
     * 查询关系项列表
     * 
     * @param rvaRelationitem 关系项
     * @return 关系项
     */
    @Override
    public List<RvaRelationitem> selectRvaRelationitemList(RvaRelationitem rvaRelationitem)
    {
        return rvaRelationitemMapper.selectRvaRelationitemList(rvaRelationitem);
    }

    /**
     * 新增关系项
     * 
     * @param rvaRelationitem 关系项
     * @return 结果
     */
    @Override
    public int insertRvaRelationitem(RvaRelationitem rvaRelationitem)
    {
        return rvaRelationitemMapper.insertRvaRelationitem(rvaRelationitem);
    }

    /**
     * 修改关系项
     * 
     * @param rvaRelationitem 关系项
     * @return 结果
     */
    @Override
    public int updateRvaRelationitem(RvaRelationitem rvaRelationitem)
    {
        return rvaRelationitemMapper.updateRvaRelationitem(rvaRelationitem);
    }

    /**
     * 批量删除关系项
     * 
     * @param ids 需要删除的关系项ID
     * @return 结果
     */
    @Override
    public int deleteRvaRelationitemByIds(String[] ids)
    {
        return rvaRelationitemMapper.deleteRvaRelationitemByIds(ids);
    }

    /**
     * 删除关系项信息
     * 
     * @param id 关系项ID
     * @return 结果
     */
    @Override
    public int deleteRvaRelationitemById(String id)
    {
        return rvaRelationitemMapper.deleteRvaRelationitemById(id);
    }
}
