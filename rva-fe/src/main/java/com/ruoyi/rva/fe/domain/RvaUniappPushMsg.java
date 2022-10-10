package com.ruoyi.rva.fe.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 推送消息明细，每个用户关联的渠道设备一个对象 rva_uniapp_push_msg
 *
 * @author jiaodacailei
 * @date 2022-05-27
 */
public class RvaUniappPushMsg extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    /**
     * 推送状态 - 待推送
     */
    public final static String PUSH_PENDING = "01";
    /**
     * 推送状态 - 已推送
     */
    public final static String PUSH_COMPLETE = "02";

    /** ID */
    private Long id;

    /** ID值，rva_uniapp_msg.id */
    @Excel(name = "ID值，rva_uniapp_msg.id")
    private Long msgId;

    /** 接收者，sys_user.id，rva_uniapp_msg.received_by中的一个 */
    @Excel(name = "接收者，sys_user.id，rva_uniapp_msg.received_by中的一个")
    private Long userId;

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

    /** 推送ID，rva_uniapp_push.cid */
    @Excel(name = "推送ID，rva_uniapp_push.cid")
    private String cid;

    /** 推送状态，01-未推送，02-已推送 */
    @Excel(name = "推送状态，01-未推送，02-已推送")
    private String pushStatus;

    /** 推送渠道 */
    @Excel(name = "推送渠道")
    private String pushChannel;

    public RvaUniappPushMsg() {
    }

    public RvaUniappPushMsg(Long msgId, Long userId, String content, String msgUrl, String msgAppUrl, String msgGroup, String title, Long sendBy, String cid, String channel) {
        super();
        this.msgId=msgId;
        this.userId=userId;
        this.sendBy=sendBy;
        this.content=content;
        this.msgUrl=msgUrl;
        this.msgAppUrl=msgAppUrl;
        this.msgGroup=msgGroup;
        this.title=title;
        this.cid=cid;
        this.pushChannel=channel;
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
    public void setCid(String cid)
    {
        this.cid = cid;
    }

    public String getCid()
    {
        return cid;
    }
    public void setPushStatus(String pushStatus)
    {
        this.pushStatus = pushStatus;
    }

    public String getPushStatus()
    {
        return pushStatus;
    }
    public void setPushChannel(String pushChannel)
    {
        this.pushChannel = pushChannel;
    }

    public String getPushChannel()
    {
        return pushChannel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("msgId", getMsgId())
            .append("userId", getUserId())
            .append("title", getTitle())
            .append("content", getContent())
            .append("msgUrl", getMsgUrl())
            .append("msgAppUrl", getMsgAppUrl())
            .append("msgGroup", getMsgGroup())
            .append("sendBy", getSendBy())
            .append("cid", getCid())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("pushStatus", getPushStatus())
            .append("pushChannel", getPushChannel())
            .toString();
    }
}
