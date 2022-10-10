package com.ruoyi.rva.framework.mapper;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaLog;

/**
 * 日志Mapper接口
 * 
 * @author jiaodacailei
 * @date 2022-03-20
 */
public interface RvaLogMapper 
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
     * 新增日志
     * 
     * @param rvaLog 日志
     * @return 结果
     */
    public int insertRvaLog(RvaLog rvaLog);

    /**
     * 修改日志
     * 
     * @param rvaLog 日志
     * @return 结果
     */
    public int updateRvaLog(RvaLog rvaLog);

    /**
     * 删除日志
     * 
     * @param id 日志主键
     * @return 结果
     */
    public int deleteRvaLogById(Long id);

    /**
     * 批量删除日志
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRvaLogByIds(Long[] ids);
}
