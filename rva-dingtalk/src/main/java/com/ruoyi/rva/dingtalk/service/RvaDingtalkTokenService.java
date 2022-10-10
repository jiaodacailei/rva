package com.ruoyi.rva.dingtalk.service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiGetJsapiTicketRequest;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGetJsapiTicketResponse;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.rva.dingtalk.config.AppConfig;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.taobao.api.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.ruoyi.rva.dingtalk.config.UrlConstant.URL_GET_JSTICKET;
import static com.ruoyi.rva.dingtalk.config.UrlConstant.URL_GET_TOKEN;

/**
 * 获取access_token 和 jsTicket方法
 *
 * @author openapi@dingtalk
 * @date 2020/2/4
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaDingtalkTokenService {

    /**
     * 缓存时间：一小时50分钟
     */
    private static final long CACHE_TTL = 60 * 55 * 2 * 1000;

    private final AppConfig appConfig;

    private final RedisCache redisCache;

    /**
     * 在此方法中，为了避免频繁获取access_token，
     * 在距离上一次获取access_token时间在两个小时之内的情况，
     * 将直接从持久化存储中读取access_token
     * <p>
     * 因为access_token和jsapi_ticket的过期时间都是7200秒
     * 所以在获取access_token的同时也去获取了jsapi_ticket
     * 注：jsapi_ticket是在前端页面JSAPI做权限验证配置的时候需要使用的
     * 具体信息请查看开发者文档--权限验证配置
     *
     * @return accessToken 或错误信息
     */
    public String getAccessToken() {
        // 从持久化存储中读取
        String accessToken = getFromCache("accessToken", "access_token");
        if (accessToken != null) {
            return accessToken;
        }

        DefaultDingTalkClient client = new DefaultDingTalkClient(URL_GET_TOKEN);
        OapiGettokenRequest request = new OapiGettokenRequest();
        OapiGettokenResponse response;

        request.setAppkey(appConfig.getAppKey());
        request.setAppsecret(appConfig.getAppSecret());
        request.setHttpMethod("GET");

        try {
            response = client.execute(request);
        } catch (ApiException e) {
            throw new RuntimeException(e.getErrMsg() + ":" + e.getErrCode(), e);
        }

        accessToken = response.getAccessToken();
        putToCache("accessToken", "access_token", accessToken);
        return accessToken;
    }

    /**
     * 获取JSTicket, 用于js的签名计算
     * 正常的情况下，jsapi_ticket的有效期为7200秒，所以开发者需要在某个地方设计一个定时器，定期去更新jsapi_ticket
     *
     * @return jsTicket或错误信息
     */
    public String getJsTicket() {
        // 从持久化存储中读取
        String ticket = getFromCache("jsticket", "ticket");
        if (ticket != null) {
            return ticket;
        }

        String accessToken = getAccessToken();
        DefaultDingTalkClient client = new DefaultDingTalkClient(URL_GET_JSTICKET);
        OapiGetJsapiTicketRequest request = new OapiGetJsapiTicketRequest();
        OapiGetJsapiTicketResponse response;

        request.setHttpMethod("GET");

        try {
            response = client.execute(request, accessToken);
        } catch (ApiException e) {
            throw new RuntimeException(e.getErrMsg() + ":" + e.getErrCode(), e);
        }

        if (!response.isSuccess()) {
            throw new RuntimeException(response.getErrmsg() + ":" + response.getErrorCode());
        }
        ticket = response.getTicket();
        putToCache("jsticket", "ticket", ticket);
        return ticket;
    }

    /**
     * 模拟从持久化存储中获取token并检查是否已过期
     *
     * @param section 存储key
     * @param field   token字段名
     * @return token值 或 null (过期或未查到)
     */
    private String getFromCache(String section, String field) {
        RvaMap<String, Object> data = redisCache.getCacheObject(section);
        if (data == null) {
            return null;
        }
        RvaMap<String, Object> o = data.getMap(appConfig.getAppKey());
        if (o != null && System.currentTimeMillis() - o.getLong("begin_time") <= CACHE_TTL) {
            return o.getString(field);
        }
        return null;
    }

    private void putToCache(String section, String field, String value) {
        RvaMap<String, Object> fieldObj = new RvaMap<>();
        fieldObj.put(field, value);
        fieldObj.put("begin_time", System.currentTimeMillis());
        RvaMap<String, Object> wrapperObj = new RvaMap<>();
        wrapperObj.put(appConfig.getAppKey(), fieldObj);
        redisCache.setCacheObject(section, wrapperObj);
    }
}
