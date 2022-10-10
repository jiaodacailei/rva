package com.ruoyi.rva.framework.service;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaLog;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaView;

/**
 * 日志Service接口
 * 
 * @author jiaodacailei
 * @date 2022-03-20
 */
public interface IRvaLogService 
{
    /**
     * 查询日志
     * 
     * @param id 日志主键
     * @return 日志
     */
    public RvaLog selectRvaLogById(Long id);

    /**
     * 查询日志列表
     * 
     * @param rvaLog 日志
     * @return 日志集合
     */
    public List<RvaLog> selectRvaLogList(RvaLog rvaLog);

    /**
     * 查询日志列表
     * @param keyValue 表单视图主键值
     * @param view 表单视图
     * @param formData 表单数据
     * @param req 请求数据
     * @return
     */
    public List<RvaLog> selectRvaLogList(String keyValue, RvaView view, RvaMap<String, Object> formData, RvaMap<String, Object> req);

    /**
     * 新增日志
     * 
     * @param rvaLog 日志
     * @return 结果
     */
    public int insertRvaLog(RvaLog rvaLog);

    /**
     * 表单提交时，插入日志
     * @param viewId 表单视图ID
     * @param keyPropValue 表单主键值
     * @param req 表单提交数据
     */
    public void insertRvaLog(String viewId, String keyPropValue, RvaMap req);

    /**
     * 修改日志
     * 
     * @param rvaLog 日志
     * @return 结果
     */
    public int updateRvaLog(RvaLog rvaLog);

    /**
     * 批量删除日志
     * 
     * @param ids 需要删除的日志主键集合
     * @return 结果
     */
    public int deleteRvaLogByIds(Long[] ids);

    /**
     * 删除日志信息
     * 
     * @param id 日志主键
     * @return 结果
     */
    public int deleteRvaLog(Long id);

    /**
     * 删除日志信息
     * @param objId
     * @param keyValue
     * @param logTable
     * @return
     */
    public int deleteRvaLog(String objId, String keyValue, String logTable);
}
