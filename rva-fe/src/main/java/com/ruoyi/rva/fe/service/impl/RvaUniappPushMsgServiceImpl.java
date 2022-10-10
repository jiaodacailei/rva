package com.ruoyi.rva.fe.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.fe.mapper.RvaUniappPushMsgMapper;
import com.ruoyi.rva.fe.domain.RvaUniappPushMsg;
import com.ruoyi.rva.fe.service.IRvaUniappPushMsgService;

/**
 * 推送消息明细，每个用户关联的渠道设备一个Service业务层处理
 *
 * @author jiaodacailei
 * @date 2022-05-27
 */
@Service
public class RvaUniappPushMsgServiceImpl implements IRvaUniappPushMsgService
{
    @Autowired
    private RvaUniappPushMsgMapper rvaUniappPushMsgMapper;

    /**
     * 查询推送消息明细，每个用户关联的渠道设备一个
     *
     * @param id 推送消息明细，每个用户关联的渠道设备一个主键
     * @return 推送消息明细，每个用户关联的渠道设备一个
     */
    @Override
    public RvaUniappPushMsg selectRvaUniappPushMsgById(Long id)
    {
        return rvaUniappPushMsgMapper.selectRvaUniappPushMsgById(id);
    }

    /**
     * 查询推送消息明细，每个用户关联的渠道设备一个列表
     *
     * @param rvaUniappPushMsg 推送消息明细，每个用户关联的渠道设备一个
     * @return 推送消息明细，每个用户关联的渠道设备一个
     */
    @Override
    public List<RvaUniappPushMsg> selectRvaUniappPushMsgList(RvaUniappPushMsg rvaUniappPushMsg)
    {
        return rvaUniappPushMsgMapper.selectRvaUniappPushMsgList(rvaUniappPushMsg);
    }

    /**
     * 新增推送消息明细，每个用户关联的渠道设备一个
     *
     * @param rvaUniappPushMsg 推送消息明细，每个用户关联的渠道设备一个
     * @return 结果
     */
    @Override
    public int insertRvaUniappPushMsg(RvaUniappPushMsg rvaUniappPushMsg)
    {
        rvaUniappPushMsg.setCreateTime(DateUtils.getNowDate());
        return rvaUniappPushMsgMapper.insertRvaUniappPushMsg(rvaUniappPushMsg);
    }

    /**
     * 修改推送消息明细，每个用户关联的渠道设备一个
     *
     * @param rvaUniappPushMsg 推送消息明细，每个用户关联的渠道设备一个
     * @return 结果
     */
    @Override
    public int updateRvaUniappPushMsg(RvaUniappPushMsg rvaUniappPushMsg)
    {
        rvaUniappPushMsg.setUpdateTime(DateUtils.getNowDate());
        return rvaUniappPushMsgMapper.updateRvaUniappPushMsg(rvaUniappPushMsg);
    }

    /**
     * 批量删除推送消息明细，每个用户关联的渠道设备一个
     *
     * @param ids 需要删除的推送消息明细，每个用户关联的渠道设备一个主键
     * @return 结果
     */
    @Override
    public int deleteRvaUniappPushMsgByIds(Long[] ids)
    {
        return rvaUniappPushMsgMapper.deleteRvaUniappPushMsgByIds(ids);
    }

    /**
     * 删除推送消息明细，每个用户关联的渠道设备一个信息
     *
     * @param id 推送消息明细，每个用户关联的渠道设备一个主键
     * @return 结果
     */
    @Override
    public int deleteRvaUniappPushMsgById(Long id)
    {
        return rvaUniappPushMsgMapper.deleteRvaUniappPushMsgById(id);
    }

    @Override
    public List<RvaUniappPushMsg> selectPendingListByPushChannelAndMinutes(String channel, int minutes) {
        return rvaUniappPushMsgMapper.selectPendingListByPushChannelAndMinutes(channel,minutes);
    }
}
