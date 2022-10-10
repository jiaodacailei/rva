package com.ruoyi.rva.fe.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 消息用户关联对象 rva_uniapp_msg_user
 *
 * @author jiaodacailei
 * @date 2022-05-27
 */
public class RvaUniappMsgUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**
     * 类型-未读
     */
    public final static String MSG_TYPE_UNREAD = "01";
    /**
     * 类型-已读
     */
    public final static String MSG_TYPE_READ = "02";

    /** ID */
    private Long id;

    /** ID值，rva_uniapp_msg.id */
    @Excel(name = "ID值，rva_uniapp_msg.id")
    private Long msgId;

    /** 接收者，sys_user.id，rva_uniapp_msg.received_by中的一个 */
    @Excel(name = "接收者，sys_user.id，rva_uniapp_msg.received_by中的一个")
    private Long userId;

    /** 阅读状态,01-未读，02-已读 */
    @Excel(name = "阅读状态,01-未读，02-已读")
    private String readStatus;

    /** 标题，rva_uniapp_msg.title */
    @Excel(name = "标题，rva_uniapp_msg.title")
    private String title;

    /** 内容，rva_uniapp_msg.content */
    @Excel(name = "内容，rva_uniapp_msg.content")
    private String content;

    /** 消息链接，ruoyi-ui页面地址，rva_uniapp_msg.msg_url */
    @Excel(name = "消息链接，ruoyi-ui页面地址，rva_uniapp_msg.msg_url")
    private String msgUrl;

    /** 消息链接，rpp页面地址，rva_uniapp_msg.msg_app_url */
    @Excel(name = "消息链接，rpp页面地址，rva_uniapp_msg.msg_app_url")
    private String msgAppUrl;

    /** 分组，rva_uniapp_msg.msg_group */
    @Excel(name = "分组，rva_uniapp_msg.msg_group")
    private String msgGroup;

    /** 发送者，sys_user.id，rva_uniapp_msg.send_by */
    @Excel(name = "发送者，sys_user.id，rva_uniapp_msg.send_by")
    private Long sendBy;

    public RvaUniappMsgUser() {
    }

    public RvaUniappMsgUser(Long msgId, Long userId, String content, String msgUrl, String msgAppUrl, String msgGroup, String title, Long sendBy) {
        super();
        this.msgId=msgId;
        this.userId=userId;
        this.sendBy=sendBy;
        this.content=content;
        this.msgUrl=msgUrl;
        this.msgAppUrl=msgAppUrl;
        this.msgGroup=msgGroup;
        this.title=title;


    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setMsgId(Long msgId)
    {
        this.msgId = msgId;
    }

    public Long getMsgId()
    {
        return msgId;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setReadStatus(String readStatus)
    {
        this.readStatus = readStatus;
    }

    public String getReadStatus()
    {
        return readStatus;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }
    public void setMsgUrl(String msgUrl)
    {
        this.msgUrl = msgUrl;
    }

    public String getMsgUrl()
    {
        return msgUrl;
    }
    public void setMsgAppUrl(String msgAppUrl)
    {
        this.msgAppUrl = msgAppUrl;
    }

    public String getMsgAppUrl()
    {
        return msgAppUrl;
    }
    public void setMsgGroup(String msgGroup)
    {
        this.msgGroup = msgGroup;
    }

    public String getMsgGroup()
    {
        return msgGroup;
    }
    public void setSendBy(Long sendBy)
    {
        this.sendBy = sendBy;
    }

    public Long getSendBy()
    {
        return sendBy;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("msgId", getMsgId())
            .append("userId", getUserId())
            .append("readStatus", getReadStatus())
            .append("title", getTitle())
            .append("content", getContent())
            .append("msgUrl", getMsgUrl())
            .append("msgAppUrl", getMsgAppUrl())
            .append("msgGroup", getMsgGroup())
            .append("sendBy", getSendBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
