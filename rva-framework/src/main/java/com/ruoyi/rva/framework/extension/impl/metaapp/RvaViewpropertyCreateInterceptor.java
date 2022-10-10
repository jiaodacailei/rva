package com.ruoyi.rva.framework.extension.impl.metaapp;

import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaObject;
import com.ruoyi.rva.framework.domain.RvaView;
import com.ruoyi.rva.framework.extension.RvaFormSubmitInterceptor;
import com.ruoyi.rva.framework.extension.impl.RvaFormSubmitBaseInterceptor;
import com.ruoyi.rva.framework.util.RvaPinyinUtils;
import com.ruoyi.rva.framework.service.IRvaMetaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * rva-button-config下拉菜单：新建表单建按钮，弹出c10_rva_view后，点击提交按钮时调用本类
 */
@Component(RvaFormSubmitInterceptor.BEAN_FREFIX + "c10_rva_viewproperty")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaViewpropertyCreateInterceptor extends RvaFormSubmitBaseInterceptor {

    private final IRvaMetaService metaService;

    @Override
    public void preHandle(RvaMap<String, Object> fieldValues, RvaObject object, RvaView view, RvaMap req) {
        String viewId = req.getString("c10_rva_view_id");
        setRequestParameter("view_id", viewId, view, req);
        String name = getRequestParameter("name", view, req);
        String id = viewId + "_" + RvaPinyinUtils.getPinyinLower(name);
        setRequestParameter("id", id, view, req);
    }

    @Override
    public void postHandle(RvaMap<String, Object> fieldValues, RvaObject object, RvaView view, RvaMap req) {
        
    }
}
