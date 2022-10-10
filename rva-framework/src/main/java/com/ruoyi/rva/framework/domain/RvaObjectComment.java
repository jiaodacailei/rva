package com.ruoyi.rva.framework.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 评论对象 rva_object_comment
 * 
 * @author jiaodacailei
 * @date 2021-12-01
 */
public class RvaObjectComment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 对象ID，rva_object.id */
    @Excel(name = "对象ID，rva_object.id")
    private String objId;

    /** 对象ID值，对象ID对应表中的ID值，comment_type为0时有效 */
    @Excel(name = "对象ID值，对象ID对应表中的ID值，comment_type为0时有效")
    private String objIdValue;

    /** 用户ID，sys_user.id */
    @Excel(name = "用户ID，sys_user.id")
    private Long userId;

    /** 评论类型（0-新评论 1-回复评论） */
    @Excel(name = "评论类型", readConverterExp = "0=-新评论,1=-回复评论")
    private Integer commentType;

    /** 评论内容 */
    @Excel(name = "评论内容")
    private String commentContent;

    /** 评论图片地址 */
    @Excel(name = "评论图片地址")
    private String commentImage;

    /** 回复用户ID，sys_user.id，comment_type为1时有效 */
    @Excel(name = "回复用户ID，sys_user.id，comment_type为1时有效")
    private Long replyUserId;

    /** 回复评论ID，rva_object_comment.id，comment_type为1时有效 */
    @Excel(name = "回复评论ID，rva_object_comment.id，comment_type为1时有效")
    private Long replyCommentId;

    /** 评论者IP */
    @Excel(name = "评论者IP")
    private String commentIp;

    /** 点赞数 */
    @Excel(name = "点赞数")
    private Long likeCount;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setObjId(String objId) 
    {
        this.objId = objId;
    }

    public String getObjId() 
    {
        return objId;
    }
    public void setObjIdValue(String objIdValue) 
    {
        this.objIdValue = objIdValue;
    }

    public String getObjIdValue() 
    {
        return objIdValue;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setCommentType(Integer commentType) 
    {
        this.commentType = commentType;
    }

    public Integer getCommentType() 
    {
        return commentType;
    }
    public void setCommentContent(String commentContent) 
    {
        this.commentContent = commentContent;
    }

    public String getCommentContent() 
    {
        return commentContent;
    }
    public void setCommentImage(String commentImage) 
    {
        this.commentImage = commentImage;
    }

    public String getCommentImage() 
    {
        return commentImage;
    }
    public void setReplyUserId(Long replyUserId) 
    {
        this.replyUserId = replyUserId;
    }

    public Long getReplyUserId() 
    {
        return replyUserId;
    }
    public void setReplyCommentId(Long replyCommentId) 
    {
        this.replyCommentId = replyCommentId;
    }

    public Long getReplyCommentId() 
    {
        return replyCommentId;
    }
    public void setCommentIp(String commentIp) 
    {
        this.commentIp = commentIp;
    }

    public String getCommentIp() 
    {
        return commentIp;
    }
    public void setLikeCount(Long likeCount) 
    {
        this.likeCount = likeCount;
    }

    public Long getLikeCount() 
    {
        return likeCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("objId", getObjId())
            .append("objIdValue", getObjIdValue())
            .append("userId", getUserId())
            .append("commentType", getCommentType())
            .append("commentContent", getCommentContent())
            .append("commentImage", getCommentImage())
            .append("replyUserId", getReplyUserId())
            .append("replyCommentId", getReplyCommentId())
            .append("commentIp", getCommentIp())
            .append("likeCount", getLikeCount())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
