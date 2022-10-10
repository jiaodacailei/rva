package com.ruoyi.rva.framework.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.rva.framework.service.IRvaObjectCommentLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.framework.mapper.RvaObjectCommentLikeMapper;
import com.ruoyi.rva.framework.domain.RvaObjectCommentLike;

/**
 * 评论点赞Service业务层处理
 * 
 * @author jiaodacailei
 * @date 2021-12-01
 */
@Service
public class RvaObjectCommentLikeServiceImpl implements IRvaObjectCommentLikeService
{
    @Autowired
    private RvaObjectCommentLikeMapper rvaObjectCommentLikeMapper;

    /**
     * 查询评论点赞
     * 
     * @param id 评论点赞主键
     * @return 评论点赞
     */
    @Override
    public RvaObjectCommentLike selectRvaObjectCommentLikeById(Long id)
    {
        return rvaObjectCommentLikeMapper.selectRvaObjectCommentLikeById(id);
    }

    /**
     * 查询评论点赞列表
     * 
     * @param rvaObjectCommentLike 评论点赞
     * @return 评论点赞
     */
    @Override
    public List<RvaObjectCommentLike> selectRvaObjectCommentLikeList(RvaObjectCommentLike rvaObjectCommentLike)
    {
        return rvaObjectCommentLikeMapper.selectRvaObjectCommentLikeList(rvaObjectCommentLike);
    }

    /**
     * 新增评论点赞
     * 
     * @param rvaObjectCommentLike 评论点赞
     * @return 结果
     */
    @Override
    public int insertRvaObjectCommentLike(RvaObjectCommentLike rvaObjectCommentLike)
    {
        rvaObjectCommentLike.setCreateTime(DateUtils.getNowDate());
        return rvaObjectCommentLikeMapper.insertRvaObjectCommentLike(rvaObjectCommentLike);
    }

    /**
     * 修改评论点赞
     * 
     * @param rvaObjectCommentLike 评论点赞
     * @return 结果
     */
    @Override
    public int updateRvaObjectCommentLike(RvaObjectCommentLike rvaObjectCommentLike)
    {
        rvaObjectCommentLike.setUpdateTime(DateUtils.getNowDate());
        return rvaObjectCommentLikeMapper.updateRvaObjectCommentLike(rvaObjectCommentLike);
    }

    /**
     * 批量删除评论点赞
     * 
     * @param ids 需要删除的评论点赞主键
     * @return 结果
     */
    @Override
    public int deleteRvaObjectCommentLikeByIds(Long[] ids)
    {
        return rvaObjectCommentLikeMapper.deleteRvaObjectCommentLikeByIds(ids);
    }

    /**
     * 删除评论点赞信息
     * 
     * @param id 评论点赞主键
     * @return 结果
     */
    @Override
    public int deleteRvaObjectCommentLikeById(Long id)
    {
        return rvaObjectCommentLikeMapper.deleteRvaObjectCommentLikeById(id);
    }
}
