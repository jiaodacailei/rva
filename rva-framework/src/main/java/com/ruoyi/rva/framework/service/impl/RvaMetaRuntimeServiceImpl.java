package com.ruoyi.rva.framework.service.impl;

import com.ruoyi.rva.framework.domain.*;
import com.ruoyi.rva.framework.mapper.*;
import com.ruoyi.rva.framework.util.RvaUtils;
import com.ruoyi.rva.framework.service.IRvaMetaRuntimeService;
import com.ruoyi.rva.framework.service.IRvaMetaService;
import com.ruoyi.rva.framework.service.IRvaViewbuttonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaMetaRuntimeServiceImpl implements IRvaMetaRuntimeService {

    private final RvaAppMapper appMapper;

    private final RvaAppitemMapper appitemMapper;

    private final RvaViewbuttonMapper viewbuttonMapper;

    private final RvaViewMapper viewMapper;

    private final RvaObjectMapper objectMapper;

    private final IRvaMetaService metaService;

    private final IRvaViewbuttonService viewbuttonService;

    @Override
    public void createCrudCreateButton(String buttonId, Boolean cascade) {
        RvaViewbutton viewbutton = viewbuttonMapper.selectRvaViewbuttonById(buttonId);
        RvaApp rvaApp = getAppByListViewButton(viewbutton);
        RvaView cloneView = metaService.cloneView(rvaApp.getCreateId(), RvaView.FORM_CREATE.substring(0, 1) + "c", cascade);
        // 创建按钮
        viewbuttonService.createListCreateButton(null, viewbutton.getIdx(), viewbutton.getViewId(), cloneView.getId(), cascade);
    }

    private RvaApp getAppByListViewButton(RvaViewbutton viewbutton) {
        RvaAppitem search = new RvaAppitem();
        search.setRelatedAppType(RvaAppitem.APP_LIST);
        search.setType(RvaView.TYPE_LIST);
        search.setRelatedAppId(viewbutton.getViewId());
        List<RvaAppitem> appitems = appitemMapper.selectRvaAppitemList(search);
        if (appitems.size() == 0) {
            throw new RuntimeException("未查询到相关应用！");
        }
        if (appitems.size() > 1) {
            throw new RuntimeException("相关应用超过一个！");
        }
        return appMapper.selectRvaAppById(appitems.get(0).getAppId());
    }

    @Override
    public void createTopDeleteButton(String buttonId) {
        RvaViewbutton viewbutton = viewbuttonMapper.selectRvaViewbuttonById(buttonId);
        viewbuttonService.createListTopDeleteButton(null, viewbutton.getIdx(), viewbutton.getViewId());
    }

    @Override
    public void createCrudUpdateButton(String buttonId, Boolean cascade) {
        RvaViewbutton viewbutton = viewbuttonMapper.selectRvaViewbuttonById(buttonId);
        RvaApp rvaApp = getAppByListViewButton(viewbutton);
        RvaView cloneView = metaService.cloneView(rvaApp.getUpdateId(), RvaView.FORM_UPDATE.substring(0, 1) + "c", cascade);
        // 创建按钮
        viewbuttonService.createListUpdateButton(null, viewbutton.getIdx(), viewbutton.getViewId(), cloneView.getId(), cascade);
    }

    @Override
    public void createInnerDeleteButton(String buttonId) {
        RvaViewbutton viewbutton = viewbuttonMapper.selectRvaViewbuttonById(buttonId);
        viewbuttonService.createListInnerDeleteButton(null, viewbutton.getIdx(), viewbutton.getViewId());
    }

    @Override
    public void createCrudCloneButton(String buttonId, Boolean cascade) {
        RvaViewbutton viewbutton = viewbuttonMapper.selectRvaViewbuttonById(buttonId);
        RvaApp rvaApp = getAppByListViewButton(viewbutton);
        // 获取创建视图
        RvaView cloneView = metaService.cloneView(rvaApp.getCreateId(), RvaView.FORM_CREATE.substring(0, 1) + "c", cascade);
        String loadUrl = cloneView.getLoadUrl();
        if (RvaUtils.isNotEmpty(loadUrl)) {
            cloneView.setLoadUrl(loadUrl.replace("/load/create", "/load/clone"));
        }
        RvaView list = viewMapper.selectRvaViewById(viewbutton.getViewId());
        RvaObject obj = objectMapper.selectRvaObjectById(list.getObjId());
        cloneView.setLoadWhereOfUpdateView(obj);
        viewMapper.updateRvaView(cloneView);
        // 创建按钮
        viewbuttonService.createListCloneButton(null, viewbutton.getIdx(), viewbutton.getViewId(), cloneView.getId(), cascade);
    }

    @Override
    public void createMoveUpButton(String buttonId) {
        RvaViewbutton viewbutton = viewbuttonMapper.selectRvaViewbuttonById(buttonId);
        viewbuttonService.createListMoveUpButton(null, viewbutton.getIdx(), viewbutton.getViewId());
    }

    @Override
    public void createMoveDownButton(String buttonId) {
        RvaViewbutton viewbutton = viewbuttonMapper.selectRvaViewbuttonById(buttonId);
        viewbuttonService.createListMoveDownButton(null, viewbutton.getIdx(), viewbutton.getViewId());
    }

    @Override
    public void createSubmitCreateButton(String buttonId) {
        RvaViewbutton viewbutton = viewbuttonMapper.selectRvaViewbuttonById(buttonId);
        viewbuttonService.createFormSubmitCreateButton(null, viewbutton.getIdx(), viewbutton.getViewId());
    }

    @Override
    public void createSubmitUpdateButton(String buttonId) {
        RvaViewbutton viewbutton = viewbuttonMapper.selectRvaViewbuttonById(buttonId);
        viewbuttonService.createFormSubmitUpdateButton(null, viewbutton.getIdx(), viewbutton.getViewId());
    }

    @Override
    public void createResetButton(String buttonId) {
        RvaViewbutton viewbutton = viewbuttonMapper.selectRvaViewbuttonById(buttonId);
        viewbuttonService.createFormResetButton(null, viewbutton.getIdx(), viewbutton.getViewId());
    }

    @Override
    public void createCancelButton(String buttonId) {
        RvaViewbutton viewbutton = viewbuttonMapper.selectRvaViewbuttonById(buttonId);
        viewbuttonService.createFormCancelButton(null, viewbutton.getIdx(), viewbutton.getViewId());
    }

    @Override
    public void createButtonBySelectForm(String selectedFormView, RvaViewbutton selectedButton) {
        RvaView cloneView = metaService.cloneView(selectedFormView, selectedFormView.substring(0, 1) + "c", true);
        // 克隆selectedButton的部分属性，创建新的按钮
        if (RvaViewbutton.POSITION_TOP.equals(selectedButton.getPosition())) {
            viewbuttonService.createListTopFormButton(null, cloneView.getName(), selectedButton.getIcon(),
                    selectedButton.getIdx(), selectedButton.getCls(), selectedButton.getViewId(),
                    RvaViewbutton.SELECT_NONE, cloneView.getId(), true);
        } else {
            viewbuttonService.createListInnerFormButton(null, cloneView.getName(), selectedButton.getIcon(),
                    selectedButton.getIdx(), selectedButton.getCls(), selectedButton.getViewId(),
                    cloneView.getId(), true);
        }
    }

    @Override
    public void createButtonBySelectCrud(String selectedCrud, RvaViewbutton selectedButton) {
        RvaApp app = metaService.cloneApp(selectedCrud, true, true);
        // 克隆selectedButton的部分属性，创建新的按钮
        if (RvaViewbutton.POSITION_TOP.equals(selectedButton.getPosition())) {
            viewbuttonService.createListTopCrudButton(null, app.getName(), selectedButton.getIcon(),
                    selectedButton.getIdx(), selectedButton.getCls(), selectedButton.getViewId(),
                    RvaViewbutton.SELECT_NONE, app.getId(), true);
        } else {
            viewbuttonService.createListInnerCrudButton(null, app.getName(), selectedButton.getIcon(),
                    selectedButton.getIdx(), selectedButton.getCls(), selectedButton.getViewId(),
                    app.getId(), true);
        }
    }

    @Override
    public void createButtonByCreateTcrud(List<String> contents, List<String> navs, RvaViewbutton selectedButton) {
        List<String> contentIds = new ArrayList<>();
        contents.forEach(id -> {
            contentIds.add(id.split(":")[1]);
        });
        RvaApp treeCrud = metaService.createTreeCrud(contentIds, navs, true);
        createTcrudButton(selectedButton, treeCrud);
    }

    private void createTcrudButton(RvaViewbutton selectedButton, RvaApp treeCrud) {
        // 克隆selectedButton的部分属性，创建新的按钮
        if (RvaViewbutton.POSITION_TOP.equals(selectedButton.getPosition())) {
            viewbuttonService.createListTopTcrudButton(null, treeCrud.getName(), selectedButton.getIcon(),
                    selectedButton.getIdx(), selectedButton.getCls(), selectedButton.getViewId(),
                    RvaViewbutton.SELECT_NONE, treeCrud.getId(), true);
        } else {
            viewbuttonService.createListInnerTcrudButton(null, treeCrud.getName(), selectedButton.getIcon(),
                    selectedButton.getIdx(), selectedButton.getCls(), selectedButton.getViewId(),
                    treeCrud.getId(), true);
        }
    }

    @Override
    public void createButtonBySelectTcrud(String tcrud, RvaViewbutton selectedButton) {
        RvaApp app = metaService.cloneApp(tcrud, true, true);
        createTcrudButton(selectedButton, app);
    }

    @Override
    public void createImportButtonBySelectForm(String selectedFormView, RvaViewbutton selectedButton) {
        // 克隆selectedButton的部分属性，创建新的按钮
        RvaViewbutton button = viewbuttonService.createListTopFormButton(null, "批量导入", "el-icon-upload2",
                selectedButton.getIdx(), RvaViewbutton.CLS_PRIMARY, selectedButton.getViewId(),
                RvaViewbutton.SELECT_NONE, "c0_none_piliangdaoru", false);
        button.setParam("createViewId", selectedFormView);
        viewbuttonMapper.updateRvaViewbutton(button);
    }

    @Override
    public void createExportButton(String buttonId) {
        RvaViewbutton viewbutton = viewbuttonMapper.selectRvaViewbuttonById(buttonId);
        // 创建按钮
        RvaViewbutton eb = viewbuttonService.createListTopJsButton(null, "导出", "",
                0, viewbutton.getCls(), viewbutton.getViewId(),
                RvaViewbutton.SELECT_NONE, "download");
        eb.setActionUrl("/rva/view/export");
        viewbuttonMapper.updateRvaViewbutton(eb);
    }
}
