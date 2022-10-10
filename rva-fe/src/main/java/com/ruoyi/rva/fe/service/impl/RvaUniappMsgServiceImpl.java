package com.ruoyi.rva.fe.service.impl;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.rva.fe.domain.*;
import com.ruoyi.rva.fe.mapper.RvaUniappMsgMapper;
import com.ruoyi.rva.fe.mapper.RvaUniappPushMapper;
import com.ruoyi.rva.fe.service.IRvaUniappMsgService;
import com.ruoyi.rva.fe.service.IRvaUniappMsgUserService;
import com.ruoyi.rva.fe.service.IRvaUniappPushMsgService;
import com.ruoyi.rva.fe.util.UnipushUtil;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.util.RvaJsonUtils;
import com.ruoyi.rva.framework.util.RvaUtils;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.ISysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

/**
 * 应用消息Service业务层处理
 *
 * @author jiaodacailei
 * @date 2022-05-12
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaUniappMsgServiceImpl implements IRvaUniappMsgService {

    private final RvaUniappMsgMapper rvaUniappMsgMapper;
    private final ISysConfigService sysConfigService;
    private final RvaUniappPushMapper pushMapper;
    private final IRvaUniappMsgUserService rvaUniappMsgUserService;
    private final IRvaUniappPushMsgService rvaUniappPushMsgService;

    private final SysUserMapper userMapper;

    /**
     * 查询应用消息
     *
     * @param id 应用消息主键
     * @return 应用消息
     */
    @Override
    public RvaUniappMsg selectRvaUniappMsgById(Long id) {
        return rvaUniappMsgMapper.selectRvaUniappMsgById(id);
    }

    /**
     * 查询应用消息列表
     *
     * @param rvaUniappMsg 应用消息
     * @return 应用消息
     */
    @Override
    public List<RvaUniappMsg> selectRvaUniappMsgList(RvaUniappMsg rvaUniappMsg) {
        return rvaUniappMsgMapper.selectRvaUniappMsgList(rvaUniappMsg);
    }

    /**
     * 新增应用消息
     *
     * @param rvaUniappMsg 应用消息
     * @return 结果
     */
    @Override
    public int insertRvaUniappMsg(RvaUniappMsg rvaUniappMsg) {
        rvaUniappMsg.setCreateTime(DateUtils.getNowDate());
        return rvaUniappMsgMapper.insertRvaUniappMsg(rvaUniappMsg);
    }

    /**
     * 修改应用消息
     *
     * @param rvaUniappMsg 应用消息
     * @return 结果
     */
    @Override
    public int updateRvaUniappMsg(RvaUniappMsg rvaUniappMsg) {
        rvaUniappMsg.setUpdateTime(DateUtils.getNowDate());
        return rvaUniappMsgMapper.updateRvaUniappMsg(rvaUniappMsg);
    }

    /**
     * 批量删除应用消息
     *
     * @param ids 需要删除的应用消息主键
     * @return 结果
     */
    @Override
    public int deleteRvaUniappMsgByIds(Long[] ids) {
        return rvaUniappMsgMapper.deleteRvaUniappMsgByIds(ids);
    }

    /**
     * 删除应用消息信息
     *
     * @param id 应用消息主键
     * @return 结果
     */
    @Override
    public int deleteRvaUniappMsgById(Long id) {
        return rvaUniappMsgMapper.deleteRvaUniappMsgById(id);
    }

    @Override
    public void sendMsg(Long msgId) {
        RvaUniappMsg rvaUniappMsg = this.selectRvaUniappMsgById(msgId);
        String pushChannels = rvaUniappMsg.getPushChannel();
        if (RvaUtils.isEmpty(pushChannels)) {
            pushChannels = sysConfigService.selectConfigByKey("rva.push.config");
            rvaUniappMsg.setPushChannel(pushChannels);
            rvaUniappMsgMapper.updateRvaUniappMsg(rvaUniappMsg);
        }
        this.handleMsgRelation(rvaUniappMsg);
    }

    private void handleMsgRelation(RvaUniappMsg rvaUniappMsg) {
        String receivedBy = rvaUniappMsg.getReceivedBy();
        String[] userIds = receivedBy.split(",");
        String content = rvaUniappMsg.getContent();
        Long msgId = rvaUniappMsg.getId();
        String msgUrl = rvaUniappMsg.getMsgUrl();
        String msgAppUrl = rvaUniappMsg.getMsgAppUrl();
        String msgGroup = rvaUniappMsg.getMsgGroup();
        String title = rvaUniappMsg.getTitle();
        Long sendBy = rvaUniappMsg.getSendBy();
        String pushChannels = rvaUniappMsg.getPushChannel();
        for (String userIdStr : userIds) {
            Long userId = RvaUtils.getLong(userIdStr);
            RvaUniappMsgUser rvaUniappMsgUser = new RvaUniappMsgUser(msgId, userId, content, msgUrl, msgAppUrl, msgGroup, title, sendBy);
            //消息用户关联
            rvaUniappMsgUserService.insertRvaUniappMsgUser(rvaUniappMsgUser);
            if (!ObjectUtils.isEmpty(pushChannels)) {
                String[] channels = pushChannels.split(",");
                for (String channel : channels) {
                    // 钉钉消息
                    if (RvaUniappMsg.PUSH_CHANNEL_DING_PUSH.equals(channel)) {
                        SysUser user = userMapper.selectUserById(userId);
                        if (user != null && RvaUtils.isNotEmpty(user.getDingId())) {
                            RvaUniappPushMsg rvaUniappPushMsg = new RvaUniappPushMsg(msgId, userId, content, msgUrl, msgAppUrl, msgGroup, title, sendBy, user.getDingId(), channel);
                            rvaUniappPushMsg.setCreateTime(new Date());
                            //推送明细
                            rvaUniappPushMsgService.insertRvaUniappPushMsg(rvaUniappPushMsg);
                        }
                    } else {
                        RvaUniappPush pushSearch = new RvaUniappPush();
                        pushSearch.setUserId(userId);
                        pushSearch.setPushChannel(channel);
                        List<RvaUniappPush> pushes = pushMapper.selectRvaUniappPushList(pushSearch);
                        for (RvaUniappPush push : pushes) {
                            String cid = push.getCid();
                            RvaUniappPushMsg rvaUniappPushMsg = new RvaUniappPushMsg(msgId, userId, content, msgUrl, msgAppUrl, msgGroup, title, sendBy, cid, channel);
                            rvaUniappPushMsg.setCreateTime(new Date());
                            //推送明细
                            rvaUniappPushMsgService.insertRvaUniappPushMsg(rvaUniappPushMsg);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void sendMsg(String receivedBy, String content, String msgUrl, String msgAppUrl, String msgGroup, String pushChannels, String title, Long sendBy) {
        if (RvaUtils.isEmpty(pushChannels)) {
            pushChannels = sysConfigService.selectConfigByKey("rva.push.config");
        }
        RvaUniappMsg rvaUniappMsg = new RvaUniappMsg(receivedBy, content, msgUrl, msgAppUrl, msgGroup, pushChannels, title, sendBy);
        this.insertRvaUniappMsg(rvaUniappMsg);
        this.handleMsgRelation(rvaUniappMsg);
    }

    @Override
    public void handlerUniPush() {
        List<RvaUniappPushMsg> rvaUniappPushMsgList = rvaUniappPushMsgService.selectPendingListByPushChannelAndMinutes(RvaUniappMsg.PUSH_CHANNEL_UNI_PUSH,60);
        for (RvaUniappPushMsg rvaUniappPushMsg : rvaUniappPushMsgList) {
            RvaMap<Object, Object> map = new RvaMap<>();
            map.put("msgUrl", rvaUniappPushMsg.getMsgUrl());
            map.put("msgAppUrl", rvaUniappPushMsg.getMsgAppUrl());
            if (UnipushUtil.sendMsg(rvaUniappPushMsg.getCid(), rvaUniappPushMsg.getTitle(), rvaUniappPushMsg.getContent(), RvaJsonUtils.writeAsString(map))) {
                rvaUniappPushMsg.setPushStatus(RvaUniappPushMsg.PUSH_COMPLETE);
                rvaUniappPushMsgService.updateRvaUniappPushMsg(rvaUniappPushMsg);
            }
        }
    }
}
