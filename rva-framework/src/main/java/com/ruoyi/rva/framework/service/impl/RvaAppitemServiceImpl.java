package com.ruoyi.rva.framework.service.impl;

import java.util.List;

import com.ruoyi.rva.framework.domain.RvaAppitem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.framework.mapper.RvaAppitemMapper;
import com.ruoyi.rva.framework.service.IRvaAppitemService;

/**
 * 应用Service业务层处理
 * 
 * @author jiaodacailei
 * @date 2021-09-05
 */
@Service
public class RvaAppitemServiceImpl implements IRvaAppitemService 
{
    @Autowired
    private RvaAppitemMapper rvaAppitemMapper;

    /**
     * 查询应用
     * 
     * @param id 应用ID
     * @return 应用
     */
    @Override
    public RvaAppitem selectRvaAppitemById(String id)
    {
        return rvaAppitemMapper.selectRvaAppitemById(id);
    }

    /**
     * 查询应用列表
     * 
     * @param rvaAppitem 应用
     * @return 应用
     */
    @Override
    public List<RvaAppitem> selectRvaAppitemList(RvaAppitem rvaAppitem)
    {
        return rvaAppitemMapper.selectRvaAppitemList(rvaAppitem);
    }

    /**
     * 新增应用
     * 
     * @param rvaAppitem 应用
     * @return 结果
     */
    @Override
    public int insertRvaAppitem(RvaAppitem rvaAppitem)
    {
        return rvaAppitemMapper.insertRvaAppitem(rvaAppitem);
    }

    /**
     * 修改应用
     * 
     * @param rvaAppitem 应用
     * @return 结果
     */
    @Override
    public int updateRvaAppitem(RvaAppitem rvaAppitem)
    {
        return rvaAppitemMapper.updateRvaAppitem(rvaAppitem);
    }

    /**
     * 批量删除应用
     * 
     * @param ids 需要删除的应用ID
     * @return 结果
     */
    @Override
    public int deleteRvaAppitemByIds(String[] ids)
    {
        return rvaAppitemMapper.deleteRvaAppitemByIds(ids);
    }

    /**
     * 删除应用信息
     * 
     * @param id 应用ID
     * @return 结果
     */
    @Override
    public int deleteRvaAppitemById(String id)
    {
        return rvaAppitemMapper.deleteRvaAppitemById(id);
    }
}
