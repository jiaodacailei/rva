package com.ruoyi.rva.framework.service;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaModule;

/**
 * 模块Service接口
 *
 * @author jiaodacailei
 * @date 2021-07-27
 */
public interface IRvaModuleService
{
    /**
     * 查询模块
     *
     * @param id 模块ID
     * @return 模块
     */
    public RvaModule selectRvaModuleById(String id);

    /**
     * 查询模块列表
     *
     * @param rvaModule 模块
     * @return 模块集合
     */
    public List<RvaModule> selectRvaModuleList(RvaModule rvaModule);

    /**
     * 新增模块
     *
     * @param rvaModule 模块
     * @return 结果
     */
    public int insertRvaModule(RvaModule rvaModule);

    /**
     * 修改模块
     *
     * @param rvaModule 模块
     * @return 结果
     */
    public int updateRvaModule(RvaModule rvaModule);

    /**
     * 批量删除模块
     *
     * @param ids 需要删除的模块ID
     * @return 结果
     */
    public int deleteRvaModuleByIds(String[] ids);

    /**
     * 删除模块信息
     *
     * @param id 模块ID
     * @return 结果
     */
    public int deleteRvaModuleById(String id);

    String selectDefaultModuleName();
}
