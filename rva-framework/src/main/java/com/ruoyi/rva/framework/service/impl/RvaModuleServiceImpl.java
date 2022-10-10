package com.ruoyi.rva.framework.service.impl;

import java.util.List;

import com.ruoyi.rva.framework.domain.RvaModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.framework.mapper.RvaModuleMapper;
import com.ruoyi.rva.framework.service.IRvaModuleService;
import org.springframework.util.ObjectUtils;

/**
 * 模块Service业务层处理
 *
 * @author jiaodacailei
 * @date 2021-07-27
 */
@Service
public class RvaModuleServiceImpl implements IRvaModuleService {
    @Autowired
    private RvaModuleMapper rvaModuleMapper;

    /**
     * 查询模块
     *
     * @param id 模块ID
     * @return 模块
     */
    @Override
    public RvaModule selectRvaModuleById(String id) {
        return rvaModuleMapper.selectRvaModuleById(id);
    }

    /**
     * 查询模块列表
     *
     * @param rvaModule 模块
     * @return 模块
     */
    @Override
    public List<RvaModule> selectRvaModuleList(RvaModule rvaModule) {
        return rvaModuleMapper.selectRvaModuleList(rvaModule);
    }

    /**
     * 新增模块
     *
     * @param rvaModule 模块
     * @return 结果
     */
    @Override
    public int insertRvaModule(RvaModule rvaModule) {
        return rvaModuleMapper.insertRvaModule(rvaModule);
    }

    /**
     * 修改模块
     *
     * @param rvaModule 模块
     * @return 结果
     */
    @Override
    public int updateRvaModule(RvaModule rvaModule) {
        return rvaModuleMapper.updateRvaModule(rvaModule);
    }

    /**
     * 批量删除模块
     *
     * @param ids 需要删除的模块ID
     * @return 结果
     */
    @Override
    public int deleteRvaModuleByIds(String[] ids) {
        return rvaModuleMapper.deleteRvaModuleByIds(ids);
    }

    /**
     * 删除模块信息
     *
     * @param id 模块ID
     * @return 结果
     */
    @Override
    public int deleteRvaModuleById(String id) {
        return rvaModuleMapper.deleteRvaModuleById(id);
    }

    @Override
    public String selectDefaultModuleName() {
        RvaModule search = new RvaModule();
        search.setIsDefault("Y");
        List<RvaModule> rvaModules = selectRvaModuleList(search);
        return ObjectUtils.isEmpty(rvaModules) ? "rva" : rvaModules.get(0).getName();
    }
}
