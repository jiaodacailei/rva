package com.ruoyi.rva.framework.extension;

import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaObject;
import com.ruoyi.rva.framework.domain.RvaView;

import java.util.Map;

public interface RvaDeleteInterceptor {

    String BEAN_FREFIX = "rva.delete.interceptor.";

    Boolean preHandle(String keyValue, Map selection, RvaObject object, RvaView list, RvaMap req);

    void postHandle(String keyValue, Map selection, RvaObject object, RvaView list, RvaMap req);
}
