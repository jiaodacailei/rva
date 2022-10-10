package com.ruoyi.rva.fe.service;

import java.util.List;

import com.ruoyi.rva.fe.domain.RvaUniappMsg;

/**
 * 应用消息Service接口
 *
 * @author jiaodacailei
 * @date 2022-05-12
 */
public interface IRvaUniappMsgService {
    /**
     * 查询应用消息
     *
     * @param id 应用消息主键
     * @return 应用消息
     */
    public RvaUniappMsg selectRvaUniappMsgById(Long id);

    /**
     * 查询应用消息列表
     *
     * @param rvaUniappMsg 应用消息
     * @return 应用消息集合
     */
    public List<RvaUniappMsg> selectRvaUniappMsgList(RvaUniappMsg rvaUniappMsg);

    /**
     * 新增应用消息
     *
     * @param rvaUniappMsg 应用消息
     * @return 结果
     */
    public int insertRvaUniappMsg(RvaUniappMsg rvaUniappMsg);

    /**
     * 修改应用消息
     *
     * @param rvaUniappMsg 应用消息
     * @return 结果
     */
    public int updateRvaUniappMsg(RvaUniappMsg rvaUniappMsg);

    /**
     * 批量删除应用消息
     *
     * @param ids 需要删除的应用消息主键集合
     * @return 结果
     */
    public int deleteRvaUniappMsgByIds(Long[] ids);

    /**
     * 删除应用消息信息
     *
     * @param id 应用消息主键
     * @return 结果
     */
    public int deleteRvaUniappMsgById(Long id);

    public void sendMsg(Long msgId);

    public void sendMsg(String receivedBy, String content, String msgUrl, String msgAppUrl, String msgGroup, String pushChannels, String title, Long sendBy);

    void handlerUniPush();
}
