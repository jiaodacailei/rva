package com.ruoyi.rva.fe.service.impl;

import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.rva.fe.domain.RvaUniappPush;
import com.ruoyi.rva.fe.mapper.RvaUniappPushMapper;
import com.ruoyi.rva.fe.service.IRvaUniappPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

/**
 * 消息推送Service业务层处理
 *
 * @author jiaodacailei
 * @date 2022-05-14
 */
@Service
public class RvaUniappPushServiceImpl implements IRvaUniappPushService {
    @Autowired
    private RvaUniappPushMapper rvaUniappPushMapper;

    @Autowired
    private TokenService tokenService;

    /**
     * 查询消息推送
     *
     * @param id 消息推送主键
     * @return 消息推送
     */
    @Override
    public RvaUniappPush selectRvaUniappPushById(Long id) {
        return rvaUniappPushMapper.selectRvaUniappPushById(id);
    }

    /**
     * 查询消息推送列表
     *
     * @param rvaUniappPush 消息推送
     * @return 消息推送
     */
    @Override
    public List<RvaUniappPush> selectRvaUniappPushList(RvaUniappPush rvaUniappPush) {
        return rvaUniappPushMapper.selectRvaUniappPushList(rvaUniappPush);
    }

    /**
     * 新增消息推送
     *
     * @param rvaUniappPush 消息推送
     * @return 结果
     */
    @Override
    public int insertRvaUniappPush(RvaUniappPush rvaUniappPush) {
        return rvaUniappPushMapper.insertRvaUniappPush(rvaUniappPush);
    }

    /**
     * 修改消息推送
     *
     * @param rvaUniappPush 消息推送
     * @return 结果
     */
    @Override
    public int updateRvaUniappPush(RvaUniappPush rvaUniappPush) {
        rvaUniappPush.setUpdateTime(DateUtils.getNowDate());
        return rvaUniappPushMapper.updateRvaUniappPush(rvaUniappPush);
    }

    /**
     * 批量删除消息推送
     *
     * @param ids 需要删除的消息推送主键
     * @return 结果
     */
    @Override
    public int deleteRvaUniappPushByIds(Long[] ids) {
        return rvaUniappPushMapper.deleteRvaUniappPushByIds(ids);
    }

    /**
     * 删除消息推送信息
     *
     * @param id 消息推送主键
     * @return 结果
     */
    @Override
    public int deleteRvaUniappPushById(Long id) {
        return rvaUniappPushMapper.deleteRvaUniappPushById(id);
    }

    /**
     * CID  unique
     *
     * @param uniappPush
     */
    @Override
    public void add(RvaUniappPush uniappPush) {
        String cid = uniappPush.getCid();
        if(ObjectUtils.isEmpty(cid)){
            return;
        }
        uniappPush.setId(null);
        uniappPush.setUpdateTime(new Date());
        String token = uniappPush.getToken();
        LoginUser loginUser = tokenService.getUserFromToken(token);
        Long userId = loginUser.getUserId();
        uniappPush.setUserId(userId);
        RvaUniappPush rvaUniappPush = new RvaUniappPush();
        rvaUniappPush.setCid(cid);
        rvaUniappPush.setPushChannel(uniappPush.getPushChannel());
        List<RvaUniappPush> rvaUniappPushes = this.selectRvaUniappPushList(rvaUniappPush);
        if (!ObjectUtils.isEmpty(rvaUniappPushes)) {
            Long id = rvaUniappPushes.get(0).getId();
            uniappPush.setId(id);
            this.updateRvaUniappPush(uniappPush);
        }else {
            this.insertRvaUniappPush(uniappPush);
        }


    }
}
