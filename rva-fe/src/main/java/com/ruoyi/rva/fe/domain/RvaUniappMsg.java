package com.ruoyi.rva.fe.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 应用消息对象 rva_uniapp_msg
 *
 * @author jiaodacailei
 * @date 2022-05-25
 */
public class RvaUniappMsg extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**
     * 推送渠道 - uniPush
     */
    public final static String PUSH_CHANNEL_UNI_PUSH = "uniPush";

    /**
     * 推送渠道 - dingPush
     */
    public final static String PUSH_CHANNEL_DING_PUSH = "dingPush";
    /**
     * 推送渠道 - wechatPush
     */
    public final static String PUSH_CHANNEL_WECHAT_PUSH = "wechatPush";
    /**
     * 推送渠道 - mobileMsg
     */
    public final static String PUSH_CHANNEL_MOBILE_MSG = "mobileMsg";
    /**
     * 推送渠道 - webSocket
     */
    public final static String PUSH_CHANNEL_WEBSOCKET = "webSocket";


    /**
     * ID
     */
    private Long id;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 消息链接 */
    @Excel(name = "消息链接")
    private String msgUrl;

    /** 发送者 */
    @Excel(name = "发送者")
    private Long sendBy;

    /** 消息链接，rpp页面地址 */
    @Excel(name = "消息链接，rpp页面地址")
    private String msgAppUrl;

    /** 接收者 */
    @Excel(name = "接收者")
    private String receivedBy;

    /** 分组 */
    @Excel(name = "分组")
    private String msgGroup;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String tenantId;

    /** 其他数据 */
    @Excel(name = "其他数据")
    private String data;

    /**
     * 推送通道 多选 uniPush，dingPush
     */
    private String pushChannel;

    /**
     * 消息内容
     */
    private String content;

    public RvaUniappMsg() {
    }

    public RvaUniappMsg(String receivedBy, String content, String msgUrl, String msgAppUrl, String msgGroup, String pushChannels, String title, Long sendBy) {
        super();
        this.receivedBy = receivedBy;
        this.content = content;
        this.msgUrl = msgUrl;
        this.msgAppUrl = msgAppUrl;
        this.msgGroup = msgGroup;
        this.pushChannel = pushChannels;
        this.title = title;
        this.sendBy = sendBy;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }
    public void setMsgUrl(String msgUrl)
    {
        this.msgUrl = msgUrl;
    }

    public String getMsgUrl()
    {
        return msgUrl;
    }
    public void setSendBy(Long sendBy)
    {
        this.sendBy = sendBy;
    }

    public Long getSendBy()
    {
        return sendBy;
    }
    public void setMsgAppUrl(String msgAppUrl)
    {
        this.msgAppUrl = msgAppUrl;
    }

    public String getMsgAppUrl()
    {
        return msgAppUrl;
    }
    public void setReceivedBy(String receivedBy)
    {
        this.receivedBy = receivedBy;
    }

    public String getReceivedBy()
    {
        return receivedBy;
    }
    public void setMsgGroup(String msgGroup)
    {
        this.msgGroup = msgGroup;
    }

    public String getMsgGroup()
    {
        return msgGroup;
    }
    public void setTenantId(String tenantId)
    {
        this.tenantId = tenantId;
    }

    public String getTenantId()
    {
        return tenantId;
    }
    public void setData(String data)
    {
        this.data = data;
    }

    public String getData()
    {
        return data;
    }
    public void setPushChannel(String pushChannel)
    {
        this.pushChannel = pushChannel;
    }

    public String getPushChannel()
    {
        return pushChannel;
    }

}
