package com.ruoyi.rva.framework.extension.impl;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaObject;
import com.ruoyi.rva.framework.domain.RvaProperty;
import com.ruoyi.rva.framework.domain.RvaView;
import com.ruoyi.rva.framework.extension.RvaDeleteInterceptor;
import com.ruoyi.rva.framework.mapper.RvaObjectMapper;
import com.ruoyi.rva.framework.mapper.RvaSchemaMapper;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.service.ISysConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component(RvaDeleteInterceptor.BEAN_FREFIX + "l0_sys_config")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysConfigDeleteInterceptor implements RvaDeleteInterceptor {


    @Autowired
    private ISysConfigService configService;
    @Autowired
    private RedisCache redisCache;

    @Override
    public Boolean preHandle(String keyValue, Map selection, RvaObject object, RvaView view, RvaMap req) {
        SysConfig config = configService.selectConfigById(Long.parseLong(keyValue));
        if (StringUtils.equals(UserConstants.YES, config.getConfigType())) {
            throw new ServiceException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
        }
        String key = Constants.SYS_CONFIG_KEY + config.getConfigKey();
        redisCache.deleteObject(key);
        return true;
    }

    @Override
    public void postHandle(String keyValue, Map selection, RvaObject object, RvaView view, RvaMap req) {

    }
}
