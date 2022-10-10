package com.ruoyi.rva.framework.service;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaApp;

/**
 * 应用Service接口
 * 
 * @author jiaodacailei
 * @date 2021-07-27
 */
public interface IRvaAppService 
{
    /**
     * 查询应用
     * 
     * @param id 应用ID
     * @return 应用
     */
    public Object selectRvaAppById(String id);

    /**
     * 查询应用列表
     * 
     * @param rvaApp 应用
     * @return 应用集合
     */
    public List<RvaApp> selectRvaAppList(RvaApp rvaApp);

    /**
     * 新增应用
     * 
     * @param rvaApp 应用
     * @return 结果
     */
    public int insertRvaApp(RvaApp rvaApp);

    /**
     * 修改应用
     * 
     * @param rvaApp 应用
     * @return 结果
     */
    public int updateRvaApp(RvaApp rvaApp);

    /**
     * 批量删除应用
     * 
     * @param ids 需要删除的应用ID
     * @return 结果
     */
    public int deleteRvaAppByIds(String[] ids);

    /**
     * 删除应用信息
     * 
     * @param id 应用ID
     * @return 结果
     */
    public int deleteRvaAppById(String id);
}
