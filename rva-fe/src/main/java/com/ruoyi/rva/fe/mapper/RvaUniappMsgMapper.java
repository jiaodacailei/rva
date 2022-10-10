package com.ruoyi.rva.fe.mapper;

import java.util.List;
import com.ruoyi.rva.fe.domain.RvaUniappMsg;
import org.apache.ibatis.annotations.Param;

/**
 * 应用消息Mapper接口
 *
 * @author jiaodacailei
 * @date 2022-05-25
 */
public interface RvaUniappMsgMapper
{
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
     * 删除应用消息
     *
     * @param id 应用消息主键
     * @return 结果
     */
    public int deleteRvaUniappMsgById(Long id);

    /**
     * 批量删除应用消息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRvaUniappMsgByIds(Long[] ids);

}
