package com.ruoyi.rva.fe.mapper;

import java.util.List;

import com.ruoyi.rva.fe.domain.RvaUniappPushMsg;
import org.apache.ibatis.annotations.Param;

/**
 * 推送消息明细，每个用户关联的渠道设备一个Mapper接口
 *
 * @author jiaodacailei
 * @date 2022-05-27
 */
public interface RvaUniappPushMsgMapper {
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
     * 删除推送消息明细，每个用户关联的渠道设备一个
     *
     * @param id 推送消息明细，每个用户关联的渠道设备一个主键
     * @return 结果
     */
    public int deleteRvaUniappPushMsgById(Long id);

    /**
     * 批量删除推送消息明细，每个用户关联的渠道设备一个
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRvaUniappPushMsgByIds(Long[] ids);

    List<RvaUniappPushMsg> selectPendingListByPushChannelAndMinutes(@Param("channel") String channel, @Param("minutes") int minutes);
}
