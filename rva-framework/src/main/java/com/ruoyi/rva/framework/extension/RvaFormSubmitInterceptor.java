package com.ruoyi.rva.framework.extension;

import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaObject;
import com.ruoyi.rva.framework.domain.RvaView;

/**
 *
 */
public interface RvaFormSubmitInterceptor {

    String BEAN_FREFIX = "rva.form.submit.interceptor.";

    void preHandle(RvaMap<String, Object> fieldValues, RvaObject object, RvaView view, RvaMap req);

    void postHandle(RvaMap<String, Object> fieldValues, RvaObject object, RvaView view, RvaMap req);
}
