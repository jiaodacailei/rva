package com.ruoyi.rva.fe.mapper;

import java.util.List;
import com.ruoyi.rva.fe.domain.RvaUniappPush;

/**
 * 消息推送Mapper接口
 * 
 * @author jiaodacailei
 * @date 2022-05-14
 */
public interface RvaUniappPushMapper 
{
    /**
     * 查询消息推送
     * 
     * @param id 消息推送主键
     * @return 消息推送
     */
    public RvaUniappPush selectRvaUniappPushById(Long id);

    /**
     * 查询消息推送列表
     * 
     * @param rvaUniappPush 消息推送
     * @return 消息推送集合
     */
    public List<RvaUniappPush> selectRvaUniappPushList(RvaUniappPush rvaUniappPush);

    /**
     * 新增消息推送
     * 
     * @param rvaUniappPush 消息推送
     * @return 结果
     */
    public int insertRvaUniappPush(RvaUniappPush rvaUniappPush);

    /**
     * 修改消息推送
     * 
     * @param rvaUniappPush 消息推送
     * @return 结果
     */
    public int updateRvaUniappPush(RvaUniappPush rvaUniappPush);

    /**
     * 删除消息推送
     * 
     * @param id 消息推送主键
     * @return 结果
     */
    public int deleteRvaUniappPushById(Long id);

    /**
     * 批量删除消息推送
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRvaUniappPushByIds(Long[] ids);
}
