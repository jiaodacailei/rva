package com.ruoyi.rva.framework.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.rva.framework.service.IRvaObjectCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.framework.mapper.RvaObjectCommentMapper;
import com.ruoyi.rva.framework.domain.RvaObjectComment;

/**
 * 评论Service业务层处理
 * 
 * @author jiaodacailei
 * @date 2021-12-01
 */
@Service
public class RvaObjectCommentServiceImpl implements IRvaObjectCommentService
{
    @Autowired
    private RvaObjectCommentMapper rvaObjectCommentMapper;

    /**
     * 查询评论
     * 
     * @param id 评论主键
     * @return 评论
     */
    @Override
    public RvaObjectComment selectRvaObjectCommentById(Long id)
    {
        return rvaObjectCommentMapper.selectRvaObjectCommentById(id);
    }

    /**
     * 查询评论列表
     * 
     * @param rvaObjectComment 评论
     * @return 评论
     */
    @Override
    public List<RvaObjectComment> selectRvaObjectCommentList(RvaObjectComment rvaObjectComment)
    {
        return rvaObjectCommentMapper.selectRvaObjectCommentList(rvaObjectComment);
    }

    /**
     * 新增评论
     * 
     * @param rvaObjectComment 评论
     * @return 结果
     */
    @Override
    public int insertRvaObjectComment(RvaObjectComment rvaObjectComment)
    {
        rvaObjectComment.setCreateTime(DateUtils.getNowDate());
        return rvaObjectCommentMapper.insertRvaObjectComment(rvaObjectComment);
    }

    /**
     * 修改评论
     * 
     * @param rvaObjectComment 评论
     * @return 结果
     */
    @Override
    public int updateRvaObjectComment(RvaObjectComment rvaObjectComment)
    {
        rvaObjectComment.setUpdateTime(DateUtils.getNowDate());
        return rvaObjectCommentMapper.updateRvaObjectComment(rvaObjectComment);
    }

    /**
     * 批量删除评论
     * 
     * @param ids 需要删除的评论主键
     * @return 结果
     */
    @Override
    public int deleteRvaObjectCommentByIds(Long[] ids)
    {
        return rvaObjectCommentMapper.deleteRvaObjectCommentByIds(ids);
    }

    /**
     * 删除评论信息
     * 
     * @param id 评论主键
     * @return 结果
     */
    @Override
    public int deleteRvaObjectCommentById(Long id)
    {
        return rvaObjectCommentMapper.deleteRvaObjectCommentById(id);
    }
}
