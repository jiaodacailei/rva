package com.ruoyi.rva.fe.service.impl;

import java.util.Date;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.fe.mapper.RvaUniappMsgUserMapper;
import com.ruoyi.rva.fe.domain.RvaUniappMsgUser;
import com.ruoyi.rva.fe.service.IRvaUniappMsgUserService;

/**
 * 消息用户关联Service业务层处理
 *
 * @author jiaodacailei
 * @date 2022-05-27
 */
@Service
public class RvaUniappMsgUserServiceImpl implements IRvaUniappMsgUserService
{
    @Autowired
    private RvaUniappMsgUserMapper rvaUniappMsgUserMapper;

    /**
     * 查询消息用户关联
     *
     * @param id 消息用户关联主键
     * @return 消息用户关联
     */
    @Override
    public RvaUniappMsgUser selectRvaUniappMsgUserById(Long id)
    {
        return rvaUniappMsgUserMapper.selectRvaUniappMsgUserById(id);
    }

    /**
     * 查询消息用户关联列表
     *
     * @param rvaUniappMsgUser 消息用户关联
     * @return 消息用户关联
     */
    @Override
    public List<RvaUniappMsgUser> selectRvaUniappMsgUserList(RvaUniappMsgUser rvaUniappMsgUser)
    {
        return rvaUniappMsgUserMapper.selectRvaUniappMsgUserList(rvaUniappMsgUser);
    }

    /**
     * 新增消息用户关联
     *
     * @param rvaUniappMsgUser 消息用户关联
     * @return 结果
     */
    @Override
    public int insertRvaUniappMsgUser(RvaUniappMsgUser rvaUniappMsgUser)
    {
        rvaUniappMsgUser.setCreateTime(DateUtils.getNowDate());
        rvaUniappMsgUser.setUpdateTime(DateUtils.getNowDate());
        return rvaUniappMsgUserMapper.insertRvaUniappMsgUser(rvaUniappMsgUser);
    }

    /**
     * 修改消息用户关联
     *
     * @param rvaUniappMsgUser 消息用户关联
     * @return 结果
     */
    @Override
    public int updateRvaUniappMsgUser(RvaUniappMsgUser rvaUniappMsgUser)
    {
        rvaUniappMsgUser.setUpdateTime(DateUtils.getNowDate());
        return rvaUniappMsgUserMapper.updateRvaUniappMsgUser(rvaUniappMsgUser);
    }

    /**
     * 批量删除消息用户关联
     *
     * @param ids 需要删除的消息用户关联主键
     * @return 结果
     */
    @Override
    public int deleteRvaUniappMsgUserByIds(Long[] ids)
    {
        return rvaUniappMsgUserMapper.deleteRvaUniappMsgUserByIds(ids);
    }

    /**
     * 删除消息用户关联信息
     *
     * @param id 消息用户关联主键
     * @return 结果
     */
    @Override
    public int deleteRvaUniappMsgUserById(Long id)
    {
        return rvaUniappMsgUserMapper.deleteRvaUniappMsgUserById(id);
    }

    @Override
    public void read(Long id) {
        RvaUniappMsgUser rvaUniappMsgUser = this.selectRvaUniappMsgUserById(id);
        rvaUniappMsgUser.setReadStatus(RvaUniappMsgUser.MSG_TYPE_READ);
        rvaUniappMsgUser.setUpdateTime(new Date());
        this.updateRvaUniappMsgUser(rvaUniappMsgUser);
    }
}
