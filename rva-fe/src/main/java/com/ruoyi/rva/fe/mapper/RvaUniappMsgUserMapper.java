package com.ruoyi.rva.fe.mapper;

import java.util.List;
import com.ruoyi.rva.fe.domain.RvaUniappMsgUser;

/**
 * 消息用户关联Mapper接口
 *
 * @author jiaodacailei
 * @date 2022-05-27
 */
public interface RvaUniappMsgUserMapper
{
    /**
     * 查询消息用户关联
     *
     * @param id 消息用户关联主键
     * @return 消息用户关联
     */
    public RvaUniappMsgUser selectRvaUniappMsgUserById(Long id);

    /**
     * 查询消息用户关联列表
     *
     * @param rvaUniappMsgUser 消息用户关联
     * @return 消息用户关联集合
     */
    public List<RvaUniappMsgUser> selectRvaUniappMsgUserList(RvaUniappMsgUser rvaUniappMsgUser);

    /**
     * 新增消息用户关联
     *
     * @param rvaUniappMsgUser 消息用户关联
     * @return 结果
     */
    public int insertRvaUniappMsgUser(RvaUniappMsgUser rvaUniappMsgUser);

    /**
     * 修改消息用户关联
     *
     * @param rvaUniappMsgUser 消息用户关联
     * @return 结果
     */
    public int updateRvaUniappMsgUser(RvaUniappMsgUser rvaUniappMsgUser);

    /**
     * 删除消息用户关联
     *
     * @param id 消息用户关联主键
     * @return 结果
     */
    public int deleteRvaUniappMsgUserById(Long id);

    /**
     * 批量删除消息用户关联
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRvaUniappMsgUserByIds(Long[] ids);
}
