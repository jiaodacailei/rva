package com.ruoyi.rva.framework.service;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaObjectCommentLike;

/**
 * 评论点赞Service接口
 * 
 * @author jiaodacailei
 * @date 2021-12-01
 */
public interface IRvaObjectCommentLikeService 
{
    /**
     * 查询评论点赞
     * 
     * @param id 评论点赞主键
     * @return 评论点赞
     */
    public RvaObjectCommentLike selectRvaObjectCommentLikeById(Long id);

    /**
     * 查询评论点赞列表
     * 
     * @param rvaObjectCommentLike 评论点赞
     * @return 评论点赞集合
     */
    public List<RvaObjectCommentLike> selectRvaObjectCommentLikeList(RvaObjectCommentLike rvaObjectCommentLike);

    /**
     * 新增评论点赞
     * 
     * @param rvaObjectCommentLike 评论点赞
     * @return 结果
     */
    public int insertRvaObjectCommentLike(RvaObjectCommentLike rvaObjectCommentLike);

    /**
     * 修改评论点赞
     * 
     * @param rvaObjectCommentLike 评论点赞
     * @return 结果
     */
    public int updateRvaObjectCommentLike(RvaObjectCommentLike rvaObjectCommentLike);

    /**
     * 批量删除评论点赞
     * 
     * @param ids 需要删除的评论点赞主键集合
     * @return 结果
     */
    public int deleteRvaObjectCommentLikeByIds(Long[] ids);

    /**
     * 删除评论点赞信息
     * 
     * @param id 评论点赞主键
     * @return 结果
     */
    public int deleteRvaObjectCommentLikeById(Long id);
}
