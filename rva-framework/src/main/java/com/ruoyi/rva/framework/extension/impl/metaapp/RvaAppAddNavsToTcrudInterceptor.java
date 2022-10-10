package com.ruoyi.rva.framework.extension.impl.metaapp;

import com.ruoyi.rva.framework.domain.RvaAppitem;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaObject;
import com.ruoyi.rva.framework.domain.RvaView;
import com.ruoyi.rva.framework.extension.RvaFormSubmitInterceptor;
import com.ruoyi.rva.framework.extension.impl.RvaFormSubmitBaseInterceptor;
import com.ruoyi.rva.framework.service.IRvaMetaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * rva-tcrud-config菜单：配置应用项 -> “新建导航项”按钮 -> 提交
 */
@Component(RvaFormSubmitInterceptor.BEAN_FREFIX + "cc2_none")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaAppAddNavsToTcrudInterceptor extends RvaFormSubmitBaseInterceptor {

    @Override
    public void preHandle(RvaMap<String, Object> fieldValues, RvaObject object, RvaView view, RvaMap req) {
    }

    private final IRvaMetaService metaService;

    @Override
    public void postHandle(RvaMap<String, Object> fieldValues, RvaObject obj, RvaView view, RvaMap req) {
        String appId = req.getMap("rvaAppParams").getString("id");
        List<String> navs = new ArrayList<>();
        getRequestParameterList(RvaAppitem.TYPE_NAV, view, req).forEach(id -> {
            navs.add(id.toString().split(":")[1]);
        });
        metaService.addNavsToTreeCrud(navs, appId);
    }
}
