package com.ruoyi.rva.framework.service;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaAppitem;

/**
 * 应用Service接口
 * 
 * @author jiaodacailei
 * @date 2021-09-05
 */
public interface IRvaAppitemService 
{
    /**
     * 查询应用
     * 
     * @param id 应用ID
     * @return 应用
     */
    public RvaAppitem selectRvaAppitemById(String id);

    /**
     * 查询应用列表
     * 
     * @param rvaAppitem 应用
     * @return 应用集合
     */
    public List<RvaAppitem> selectRvaAppitemList(RvaAppitem rvaAppitem);

    /**
     * 新增应用
     * 
     * @param rvaAppitem 应用
     * @return 结果
     */
    public int insertRvaAppitem(RvaAppitem rvaAppitem);

    /**
     * 修改应用
     * 
     * @param rvaAppitem 应用
     * @return 结果
     */
    public int updateRvaAppitem(RvaAppitem rvaAppitem);

    /**
     * 批量删除应用
     * 
     * @param ids 需要删除的应用ID
     * @return 结果
     */
    public int deleteRvaAppitemByIds(String[] ids);

    /**
     * 删除应用信息
     * 
     * @param id 应用ID
     * @return 结果
     */
    public int deleteRvaAppitemById(String id);
}
