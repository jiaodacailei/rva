package com.ruoyi.rva.framework.extension.impl;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaObject;
import com.ruoyi.rva.framework.domain.RvaProperty;
import com.ruoyi.rva.framework.domain.RvaView;
import com.ruoyi.rva.framework.extension.RvaFormSubmitInterceptor;
import com.ruoyi.rva.framework.mapper.RvaObjectMapper;
import com.ruoyi.rva.framework.mapper.RvaSchemaMapper;
import com.ruoyi.rva.framework.util.RvaPinyinUtils;
import com.ruoyi.rva.framework.util.RvaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 参数设置-新增
 */
@Component(RvaFormSubmitInterceptor.BEAN_FREFIX + "c0_sys_config")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysConfigSubmitCreateInterceptor extends RvaFormSubmitBaseInterceptor {

    @Autowired
    private RedisCache redisCache;

    @Override
    public void preHandle(RvaMap<String, Object> fieldValues, RvaObject obj, RvaView view, RvaMap req) {
    }

    @Override
    public void postHandle(RvaMap<String, Object> fieldValues, RvaObject obj, RvaView view, RvaMap req) {
        String key = Constants.SYS_CONFIG_KEY + getRequestParameter("config_key", view, req);
        String value = getRequestParameter("config_value", view, req);
        redisCache.setCacheObject(key, value);
    }
}
