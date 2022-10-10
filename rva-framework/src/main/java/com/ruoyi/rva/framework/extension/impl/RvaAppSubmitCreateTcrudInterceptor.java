package com.ruoyi.rva.framework.extension.impl;

import com.ruoyi.rva.framework.domain.RvaAppitem;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaObject;
import com.ruoyi.rva.framework.domain.RvaView;
import com.ruoyi.rva.framework.extension.RvaFormSubmitInterceptor;
import com.ruoyi.rva.framework.service.IRvaMetaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component(RvaFormSubmitInterceptor.BEAN_FREFIX + "c1_none_create_tcrud")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaAppSubmitCreateTcrudInterceptor extends RvaFormSubmitBaseInterceptor {

    @Override
    public void preHandle(RvaMap<String, Object> fieldValues, RvaObject object, RvaView view, RvaMap req) {
    }

    private final IRvaMetaService metaService;

    @Override
    public void postHandle(RvaMap<String, Object> fieldValues, RvaObject obj, RvaView view, RvaMap req) {
        List<String> navs = new ArrayList<>();
        getRequestParameterList(RvaAppitem.TYPE_NAV, view, req).forEach(id -> {
            navs.add((String) id);
        });
        List<String> contents = new ArrayList<>();
        getRequestParameterList("content", view, req).forEach(id -> {
            contents.add(id.toString().split(":")[1]);
        });
        String clone = getRequestParameter("clone", view, req);
        metaService.createTreeCrud(contents, navs, "Y".equals(clone));
    }
}
