package com.ruoyi.rva.framework.extension.impl.metaapp;

import com.ruoyi.rva.framework.domain.*;
import com.ruoyi.rva.framework.extension.RvaDeleteInterceptor;
import com.ruoyi.rva.framework.mapper.RvaAppMapper;
import com.ruoyi.rva.framework.mapper.RvaAppitemMapper;
import com.ruoyi.rva.framework.service.IRvaMetaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * RvaAppitem删除时，根据RvaApp的cascadeDelete，是否级联删除相应的视图或者应用
 */
@Component(RvaDeleteInterceptor.BEAN_FREFIX + "l0_rva_appitem")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaAppitemDeleteInterceptor implements RvaDeleteInterceptor {

    private final IRvaMetaService metaService;

    private final RvaAppMapper appMapper;

    private final RvaAppitemMapper appitemMapper;

    @Override
    public Boolean preHandle(String keyValue, Map selection, RvaObject object, RvaView list, RvaMap req) {
        RvaAppitem appitem = appitemMapper.selectRvaAppitemById(keyValue);
        RvaApp app = appMapper.selectRvaAppById(appitem.getAppId());
        if (app.isCascadeDelete()) {
            if (appitem.isApp()) {
                metaService.deleteApp(appitem.getRelatedAppId());
            } else if (appitem.isView()) {
                metaService.deleteView(appitem.getRelatedAppId());
            }
        }
        return true;
    }

    @Override
    public void postHandle(String keyValue, Map selection, RvaObject object, RvaView list, RvaMap req) {

    }
}
