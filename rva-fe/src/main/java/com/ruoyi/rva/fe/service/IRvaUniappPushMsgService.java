package com.ruoyi.rva.fe.service;

import java.util.List;
import com.ruoyi.rva.fe.domain.RvaUniappPushMsg;

/**
 * 推送消息明细，每个用户关联的渠道设备一个Service接口
 *
 * @author jiaodacailei
 * @date 2022-05-27
 */
public interface IRvaUniappPushMsgService
{
    /**
     * 查询推送消息明细，每个用户关联的渠道设备一个
     *
     * @param id 推送消息明细，每个用户关联的渠道设备一个主键
     * @return 推送消息明细，每个用户关联的渠道设备一个
     */
    public RvaUniappPushMsg selectRvaUniappPushMsgById(Long id);

    /**
     * 查询推送消息明细，每个用户关联的渠道设备一个列表
     *
     * @param rvaUniappPushMsg 推送消息明细，每个用户关联的渠道设备一个
     * @return 推送消息明细，每个用户关联的渠道设备一个集合
     */
    public List<RvaUniappPushMsg> selectRvaUniappPushMsgList(RvaUniappPushMsg rvaUniappPushMsg);

    /**
     * 新增推送消息明细，每个用户关联的渠道设备一个
     *
     * @param rvaUniappPushMsg 推送消息明细，每个用户关联的渠道设备一个
     * @return 结果
     */
    public int insertRvaUniappPushMsg(RvaUniappPushMsg rvaUniappPushMsg);

    /**
     * 修改推送消息明细，每个用户关联的渠道设备一个
     *
     * @param rvaUniappPushMsg 推送消息明细，每个用户关联的渠道设备一个
     * @return 结果
     */
    public int updateRvaUniappPushMsg(RvaUniappPushMsg rvaUniappPushMsg);

    /**
     * 批量删除推送消息明细，每个用户关联的渠道设备一个
     *
     * @param ids 需要删除的推送消息明细，每个用户关联的渠道设备一个主键集合
     * @return 结果
     */
    public int deleteRvaUniappPushMsgByIds(Long[] ids);

    /**
     * 删除推送消息明细，每个用户关联的渠道设备一个信息
     *
     * @param id 推送消息明细，每个用户关联的渠道设备一个主键
     * @return 结果
     */
    public int deleteRvaUniappPushMsgById(Long id);

    List<RvaUniappPushMsg> selectPendingListByPushChannelAndMinutes(String channel, int minutes);
}
