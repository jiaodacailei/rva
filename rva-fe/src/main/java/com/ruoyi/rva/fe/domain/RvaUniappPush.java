package com.ruoyi.rva.fe.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 消息推送对象 rva_uniapp_push
 *
 * @author jiaodacailei
 * @date 2022-05-14
 */
public class RvaUniappPush extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** cid,APP-PLUS-cid,H5-sessionId */
    @Excel(name = "cid,APP-PLUS-cid,H5-sessionId")
    private String cid;

    /** 应用ID */
    @Excel(name = "应用ID")
    private String uniappId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 设备平台类型 */
    @Excel(name = "设备平台类型")
    private String devicePlatform;

    /** 设备品牌 */
    @Excel(name = "设备品牌")
    private String deviceBrand;

    /** 设备系统 */
    @Excel(name = "设备系统")
    private String deviceSystem;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String tenantId;

    /** 用户登录凭证 */
    @Excel(name = "用户登录凭证")
    private String token;

    /**
     * 推送渠道 uniPush-uniapp推送，dingPush-钉钉推送，wechatPush-微信推送
     */
    private String pushChannel;

    public String getPushChannel() {
        return pushChannel;
    }

    public void setPushChannel(String pushChannel) {
        this.pushChannel = pushChannel;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCid(String cid)
    {
        this.cid = cid;
    }

    public String getCid()
    {
        return cid;
    }
    public void setUniappId(String uniappId)
    {
        this.uniappId = uniappId;
    }

    public String getUniappId()
    {
        return uniappId;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setDevicePlatform(String devicePlatform)
    {
        this.devicePlatform = devicePlatform;
    }

    public String getDevicePlatform()
    {
        return devicePlatform;
    }
    public void setDeviceBrand(String deviceBrand)
    {
        this.deviceBrand = deviceBrand;
    }

    public String getDeviceBrand()
    {
        return deviceBrand;
    }
    public void setDeviceSystem(String deviceSystem)
    {
        this.deviceSystem = deviceSystem;
    }

    public String getDeviceSystem()
    {
        return deviceSystem;
    }
    public void setTenantId(String tenantId)
    {
        this.tenantId = tenantId;
    }

    public String getTenantId()
    {
        return tenantId;
    }
    public void setToken(String token)
    {
        this.token = token;
    }

    public String getToken()
    {
        return token;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("cid", getCid())
            .append("uniappId", getUniappId())
            .append("userId", getUserId())
            .append("devicePlatform", getDevicePlatform())
            .append("deviceBrand", getDeviceBrand())
            .append("deviceSystem", getDeviceSystem())
            .append("tenantId", getTenantId())
            .append("token", getToken())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
