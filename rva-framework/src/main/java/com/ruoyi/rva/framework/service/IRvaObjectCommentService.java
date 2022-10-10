package com.ruoyi.rva.framework.service;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaObjectComment;

/**
 * 评论Service接口
 * 
 * @author jiaodacailei
 * @date 2021-12-01
 */
public interface IRvaObjectCommentService 
{
    /**
     * 查询评论
     * 
     * @param id 评论主键
     * @return 评论
     */
    public RvaObjectComment selectRvaObjectCommentById(Long id);

    /**
     * 查询评论列表
     * 
     * @param rvaObjectComment 评论
     * @return 评论集合
     */
    public List<RvaObjectComment> selectRvaObjectCommentList(RvaObjectComment rvaObjectComment);

    /**
     * 新增评论
     * 
     * @param rvaObjectComment 评论
     * @return 结果
     */
    public int insertRvaObjectComment(RvaObjectComment rvaObjectComment);

    /**
     * 修改评论
     * 
     * @param rvaObjectComment 评论
     * @return 结果
     */
    public int updateRvaObjectComment(RvaObjectComment rvaObjectComment);

    /**
     * 批量删除评论
     * 
     * @param ids 需要删除的评论主键集合
     * @return 结果
     */
    public int deleteRvaObjectCommentByIds(Long[] ids);

    /**
     * 删除评论信息
     * 
     * @param id 评论主键
     * @return 结果
     */
    public int deleteRvaObjectCommentById(Long id);
}
