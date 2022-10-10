package com.ruoyi.rva.framework.service.impl;

import java.util.List;

import com.ruoyi.rva.framework.service.IRvaObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.framework.mapper.RvaObjectMapper;
import com.ruoyi.rva.framework.domain.RvaObject;

/**
 * 对象Service业务层处理
 * 
 * @author jiaodacailei
 * @date 2021-11-30
 */
@Service
public class RvaObjectServiceImpl implements IRvaObjectService
{
    @Autowired
    private RvaObjectMapper rvaObjectMapper;

    /**
     * 查询对象
     * 
     * @param id 对象主键
     * @return 对象
     */
    @Override
    public RvaObject selectRvaObjectById(String id)
    {
        return rvaObjectMapper.selectRvaObjectById(id);
    }

    /**
     * 查询对象列表
     * 
     * @param rvaObject 对象
     * @return 对象
     */
    @Override
    public List<RvaObject> selectRvaObjectList(RvaObject rvaObject)
    {
        return rvaObjectMapper.selectRvaObjectList(rvaObject);
    }

    /**
     * 新增对象
     * 
     * @param rvaObject 对象
     * @return 结果
     */
    @Override
    public int insertRvaObject(RvaObject rvaObject)
    {
        return rvaObjectMapper.insertRvaObject(rvaObject);
    }

    /**
     * 修改对象
     * 
     * @param rvaObject 对象
     * @return 结果
     */
    @Override
    public int updateRvaObject(RvaObject rvaObject)
    {
        return rvaObjectMapper.updateRvaObject(rvaObject);
    }

    /**
     * 批量删除对象
     * 
     * @param ids 需要删除的对象主键
     * @return 结果
     */
    @Override
    public int deleteRvaObjectByIds(String[] ids)
    {
        return rvaObjectMapper.deleteRvaObjectByIds(ids);
    }

    /**
     * 删除对象信息
     * 
     * @param id 对象主键
     * @return 结果
     */
    @Override
    public int deleteRvaObjectById(String id)
    {
        return rvaObjectMapper.deleteRvaObjectById(id);
    }
}
