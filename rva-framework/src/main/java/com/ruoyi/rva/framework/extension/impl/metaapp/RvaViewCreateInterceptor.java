package com.ruoyi.rva.framework.extension.impl.metaapp;

import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaObject;
import com.ruoyi.rva.framework.domain.RvaView;
import com.ruoyi.rva.framework.domain.RvaViewbutton;
import com.ruoyi.rva.framework.extension.RvaFormSubmitInterceptor;
import com.ruoyi.rva.framework.extension.impl.RvaFormSubmitBaseInterceptor;
import com.ruoyi.rva.framework.util.RvaPinyinUtils;
import com.ruoyi.rva.framework.mapper.RvaViewMapper;
import com.ruoyi.rva.framework.mapper.RvaViewbuttonMapper;
import com.ruoyi.rva.framework.service.IRvaMetaService;
import com.ruoyi.rva.framework.service.IRvaViewbuttonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * rva-button-config下拉菜单：新建表单建按钮，弹出c10_rva_view后，点击提交按钮时调用本类
 */
@Component(RvaFormSubmitInterceptor.BEAN_FREFIX + "c10_rva_view")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaViewCreateInterceptor extends RvaFormSubmitBaseInterceptor {

    private final IRvaMetaService metaService;

    private final IRvaViewbuttonService viewbuttonService;

    private  final RvaViewbuttonMapper viewbuttonMapper;

    private  final RvaViewMapper viewMapper;

    @Override
    public void preHandle(RvaMap<String, Object> fieldValues, RvaObject object, RvaView view, RvaMap req) {
        String name = getRequestParameter("name", view, req);
        String viewId = metaService.createViewId("none_" + RvaPinyinUtils.getPinyinLower(name), RvaView.FORM_CREATE.substring(0, 1));
        setRequestParameter("id", viewId, view, req);
        // 设置加载url
        setRequestParameter("load_url", RvaView.URL_LOAD_CREATE.replace("{view}", viewId), view, req);
    }

    @Override
    public void postHandle(RvaMap<String, Object> fieldValues, RvaObject object, RvaView view, RvaMap req) {
        // 创建表单视图内的按钮：提交、重置、取消
        String id = getRequestParameter("id", view, req);
        RvaViewbutton button = viewbuttonService.createFormSubmitCreateButton(null, 0, id);
        button.setActionUrl(getRequestParameter("form_submit_url", view, req));
        viewbuttonMapper.updateRvaViewbutton(button);
        viewbuttonService.createFormResetButton(null, 1, id);
        viewbuttonService.createFormCancelButton(null, 2, id);
        // 创建弹出表单视图的按钮
        String name = getRequestParameter("name", view, req);
        // 获取触发本次调用的按钮
        RvaViewbutton triggerBtn = RvaViewbutton.create(req.getMap("rvaAppParams"));
        // 克隆triggerBtn的部分属性，创建新的按钮
        if (RvaViewbutton.POSITION_TOP.equals(triggerBtn.getPosition())) {
            viewbuttonService.createListTopFormButton(null, name, triggerBtn.getIcon(),
                    triggerBtn.getIdx() + 1, triggerBtn.getCls(), triggerBtn.getViewId(),
                    RvaViewbutton.SELECT_NONE, id, true);
        } else {
            viewbuttonService.createListInnerFormButton(null, name, triggerBtn.getIcon(),
                    triggerBtn.getIdx() + 1,triggerBtn.getCls(), triggerBtn.getViewId(),
                    id, true);
        }
        // 设置视图cascaded
        RvaView rvaView = viewMapper.selectRvaViewById(id);
        rvaView.setCascaded(true);
        viewMapper.updateRvaView(rvaView);
    }
}
