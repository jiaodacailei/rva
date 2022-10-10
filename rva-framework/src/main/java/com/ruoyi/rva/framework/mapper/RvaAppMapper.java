package com.ruoyi.rva.framework.mapper;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaApp;
import com.ruoyi.rva.framework.domain.RvaMap;

/**
 * 应用Mapper接口
 * 
 * @author jiaodacailei
 * @date 2021-07-27
 */
public interface RvaAppMapper 
{
    /**
     * 查询应用
     * 
     * @param id 应用ID
     * @return 应用
     */
    public RvaApp selectRvaAppById(String id);

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
     * 删除应用
     * 
     * @param id 应用ID
     * @return 结果
     */
    public int deleteRvaAppById(String id);

    /**
     * 批量删除应用
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRvaAppByIds(String[] ids);

    /**
     * 查询appId对应对象的所有关联应用的使用情况
     * @param appId
     * @return
     */
    List<RvaMap<String, Object>> getUsage(String appId);

    /**
     * 获取具有相同对象ID（appId对应应用的对象ID）的应用ID列表
     * @param appId
     * @return
     */
    List<String> getObjectAppIds(String appId);
}
