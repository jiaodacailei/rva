package com.ruoyi.rva.framework.extension.impl;

import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaObject;
import com.ruoyi.rva.framework.domain.RvaView;
import com.ruoyi.rva.framework.extension.RvaFormSubmitInterceptor;
import com.ruoyi.rva.framework.service.IRvaMetaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(RvaFormSubmitInterceptor.BEAN_FREFIX + "c0_none")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaObjectSubmitCreate2Interceptor extends RvaFormSubmitBaseInterceptor {

    @Override
    public void preHandle(RvaMap<String, Object> fieldValues, RvaObject object, RvaView view, RvaMap req) {
    }

    private final IRvaMetaService metaService;

    @Override
    public void postHandle(RvaMap<String, Object> fieldValues, RvaObject obj, RvaView view, RvaMap req) {
        String table = getRequestParameter("table", view, req);
        metaService.createObject(table);
    }
}
