package com.ruoyi.rva.framework.service.impl;

import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaView;
import com.ruoyi.rva.framework.domain.RvaViewbutton;
import com.ruoyi.rva.framework.mapper.RvaDataMapper;
import com.ruoyi.rva.framework.service.IRvaViewService;
import com.ruoyi.rva.framework.util.RvaUtils;
import com.ruoyi.rva.framework.util.RvaVelocityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.framework.mapper.RvaViewbuttonMapper;
import com.ruoyi.rva.framework.service.IRvaViewbuttonService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 视图属性Service业务层处理
 * 
 * @author jiaodacailei
 * @date 2021-07-27
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaViewbuttonServiceImpl implements IRvaViewbuttonService 
{
    private final RvaViewbuttonMapper rvaViewbuttonMapper;

    /**
     * 批量删除视图属性
     * 
     * @param ids 需要删除的视图属性ID
     * @return 结果
     */
    @Override
    public int deleteRvaViewbuttonByIds(String[] ids)
    {
        for (String buttonId : ids) {
            RvaViewbutton viewbutton = rvaViewbuttonMapper.selectRvaViewbuttonById(buttonId);

        }
        return rvaViewbuttonMapper.deleteRvaViewbuttonByIds(ids);
    }

    public RvaViewbutton createListCreateButton(String idSuffix, Integer index, String viewId, String relatedViewId, Boolean cascadeDelete) {
        return createListTopFormButton (idSuffix, "新建", "el-icon-plus", index, RvaViewbutton.CLS_PRIMARY, viewId, RvaViewbutton.SELECT_NONE, relatedViewId, cascadeDelete);
    }

    public RvaViewbutton createListUpdateButton(String idSuffix, Integer index, String viewId, String relatedViewId, Boolean cascadeDelete) {
        return createListInnerFormButton(idSuffix, "修改", "el-icon-edit", index, RvaViewbutton.CLS_PRIMARY, viewId, relatedViewId, cascadeDelete);
    }

    public RvaViewbutton createListTopDeleteButton(String idSuffix, Integer index, String viewId) {
        String url = RvaView.URL_DELETE.replace("{view}", viewId);
        return createListTopAjaxButton(idSuffix, "删除", "el-icon-delete", index, RvaViewbutton.CLS_DANGER, viewId, RvaViewbutton.SELECT_MULTIPLE, url);
    }

    public RvaViewbutton createListInnerDeleteButton(String idSuffix, Integer index, String viewId) {
        String url = RvaView.URL_DELETE.replace("{view}", viewId);
        return createListInnerAjaxButton(idSuffix, "删除", "el-icon-delete", index, RvaViewbutton.CLS_DANGER, viewId, url);
    }

    public RvaViewbutton createListCloneButton(String idSuffix, Integer index, String viewId, String relatedViewId, Boolean cascadeDelete) {
        return createListInnerFormButton(idSuffix, "克隆", "el-icon-document-copy", index, RvaViewbutton.CLS_PRIMARY, viewId, relatedViewId, cascadeDelete);
    }

    public RvaViewbutton createListMoveUpButton(String idSuffix, Integer index, String viewId) {
        String url = RvaView.URL_MOVE_UP.replace("{view}", viewId);
        return createListTopAjaxButton(idSuffix, "上移", "el-icon-top", index, RvaViewbutton.CLS_PRIMARY, viewId, RvaViewbutton.SELECT_MULTIPLE, url);
    }

    public RvaViewbutton createListMoveDownButton(String idSuffix, Integer index, String viewId) {
        String url = RvaView.URL_MOVE_DOWN.replace("{view}", viewId);
        return createListTopAjaxButton(idSuffix, "下移", "el-icon-bottom", index, RvaViewbutton.CLS_PRIMARY, viewId, RvaViewbutton.SELECT_MULTIPLE, url);
    }

    public RvaViewbutton createListTopFormButton(String idSuffix, String name, String icon, Integer index, String cls, String viewId, String selectType, String relatedViewId, Boolean cascadeDelete) {
        RvaViewbutton lb = createListButton(idSuffix, name, RvaViewbutton.TYPE_FORM, icon, index, cls, viewId, selectType, RvaViewbutton.POSITION_TOP, cascadeDelete);
        lb.setAction(RvaViewbutton.ACTION_DIALOG);
        lb.setActionDialogViewId(relatedViewId);
        rvaViewbuttonMapper.insertRvaViewbutton(lb);
        return lb;
    }

    public RvaViewbutton createListInnerFormButton(String idSuffix, String name, String icon, Integer index, String cls, String viewId, String relatedViewId, Boolean cascadeDelete) {
        RvaViewbutton lb = createListButton(idSuffix, name, RvaViewbutton.TYPE_FORM, icon, index, cls, viewId, RvaViewbutton.SELECT_SINGLE, RvaViewbutton.POSITION_INNER, cascadeDelete);
        lb.setAction(RvaViewbutton.ACTION_DIALOG);
        lb.setActionDialogViewId(relatedViewId);
        rvaViewbuttonMapper.insertRvaViewbutton(lb);
        return lb;
    }

    @Override
    public RvaViewbutton createListTopCrudButton(String idSuffix, String name, String icon, Integer index, String cls, String viewId, String selectType, String relatedAppId, Boolean cascadeDelete) {
        RvaViewbutton lb = createListButton(idSuffix, name, RvaViewbutton.TYPE_CRUD, icon, index, cls, viewId, selectType, RvaViewbutton.POSITION_TOP, cascadeDelete);
        lb.setAction(RvaViewbutton.ACTION_DIALOG);
        lb.setActionDialogAppId(relatedAppId);
        rvaViewbuttonMapper.insertRvaViewbutton(lb);
        return lb;
    }

    @Override
    public RvaViewbutton createListInnerCrudButton(String idSuffix, String name, String icon, Integer index, String cls, String viewId, String relatedAppId, Boolean cascadeDelete) {
        RvaViewbutton lb = createListButton(idSuffix, name, RvaViewbutton.TYPE_CRUD, icon, index, cls, viewId, RvaViewbutton.SELECT_SINGLE, RvaViewbutton.POSITION_INNER, cascadeDelete);
        lb.setAction(RvaViewbutton.ACTION_DIALOG);
        lb.setActionDialogAppId(relatedAppId);
        rvaViewbuttonMapper.insertRvaViewbutton(lb);
        return lb;
    }

    @Override
    public RvaViewbutton createListTopTcrudButton(String idSuffix, String name, String icon, Integer index, String cls, String viewId, String selectType, String relatedAppId, Boolean cascadeDelete) {
        RvaViewbutton lb = createListButton(idSuffix, name, RvaViewbutton.TYPE_TCRUD, icon, index, cls, viewId, selectType, RvaViewbutton.POSITION_TOP, cascadeDelete);
        lb.setAction(RvaViewbutton.ACTION_DIALOG);
        lb.setActionDialogAppId(relatedAppId);
        rvaViewbuttonMapper.insertRvaViewbutton(lb);
        return lb;
    }

    @Override
    public RvaViewbutton createListInnerTcrudButton(String idSuffix, String name, String icon, Integer index, String cls, String viewId, String relatedAppId, Boolean cascadeDelete) {
        RvaViewbutton lb = createListButton(idSuffix, name, RvaViewbutton.TYPE_TCRUD, icon, index, cls, viewId, RvaViewbutton.SELECT_SINGLE, RvaViewbutton.POSITION_INNER, cascadeDelete);
        lb.setAction(RvaViewbutton.ACTION_DIALOG);
        lb.setActionDialogAppId(relatedAppId);
        rvaViewbuttonMapper.insertRvaViewbutton(lb);
        return lb;
    }

    public RvaViewbutton createListTopAjaxButton(String idSuffix, String name, String icon, Integer index, String cls, String viewId, String selectType, String actionUrl) {
        RvaViewbutton lb = createListButton(idSuffix, name, RvaViewbutton.TYPE_AJAX, icon, index, cls, viewId, selectType, RvaViewbutton.POSITION_TOP, false);
        lb.setActionUrl(actionUrl);
        lb.setActionSuccess(RvaViewbutton.JS_LOAD_LIST);
        rvaViewbuttonMapper.insertRvaViewbutton(lb);
        return lb;
    }

    public RvaViewbutton createListInnerAjaxButton(String idSuffix, String name, String icon, Integer index, String cls, String viewId, String actionUrl) {
        RvaViewbutton lb = createListButton(idSuffix, name, RvaViewbutton.TYPE_AJAX, icon, index, cls, viewId, RvaViewbutton.SELECT_SINGLE, RvaViewbutton.POSITION_INNER, false);
        lb.setActionUrl(actionUrl);
        lb.setActionSuccess(RvaViewbutton.JS_LOAD_LIST);
        rvaViewbuttonMapper.insertRvaViewbutton(lb);
        return lb;
    }

    public RvaViewbutton createListTopJsButton(String idSuffix, String name, String icon, Integer index, String cls, String viewId, String selectType, String action) {
        RvaViewbutton lb = createListButton(idSuffix, name, RvaViewbutton.TYPE_JS, icon, index, cls, viewId, selectType, RvaViewbutton.POSITION_TOP, false);
        lb.setAction(action);
        rvaViewbuttonMapper.insertRvaViewbutton(lb);
        return lb;
    }

    public RvaViewbutton createListInnerJsButton(String idSuffix, String name, String icon, Integer index, String cls, String viewId, String action) {
        RvaViewbutton lb = createListButton(idSuffix, name, RvaViewbutton.TYPE_AJAX, icon, index, cls, viewId, RvaViewbutton.SELECT_SINGLE, RvaViewbutton.POSITION_INNER, false);
        lb.setAction(action);
        rvaViewbuttonMapper.insertRvaViewbutton(lb);
        return lb;
    }

    public RvaViewbutton createFormSubmitCreateButton(String idSuffix, Integer index, String viewId) {
        String url = RvaView.URL_SUBMIT_CREATE.replace("{view}", viewId);
        return createFormAjaxButton(idSuffix, "提交", "el-icon-check", index, RvaViewbutton.CLS_PRIMARY, viewId, url);
    }

    public RvaViewbutton createFormSubmitUpdateButton(String idSuffix, Integer index, String viewId) {
        String url = RvaView.URL_SUBMIT_UPDATE.replace("{view}", viewId);
        return createFormAjaxButton(idSuffix, "提交", "el-icon-check", index, RvaViewbutton.CLS_PRIMARY, viewId, url);
    }

    public RvaViewbutton createFormResetButton(String idSuffix, Integer index, String viewId) {
        return createFormJsButton(idSuffix, "重置", "el-icon-refresh-left", index, RvaViewbutton.CLS_WARNING, viewId, RvaViewbutton.JS_RESET_VIEW);
    }

    public RvaViewbutton createFormCancelButton(String idSuffix, Integer index, String viewId) {
        return createFormJsButton(idSuffix, "取消", "el-icon-close", index, null, viewId, RvaViewbutton.JS_CLOSE_VIEW);
    }

    public RvaViewbutton createFormAjaxButton(String idSuffix, String name, String icon, Integer index, String cls, String viewId, String actionUrl) {
        RvaViewbutton lb = createButton(idSuffix, name, RvaViewbutton.TYPE_AJAX, icon, index, cls, RvaViewbutton.POSITION_FORM, viewId, false);
        lb.setActionUrl(actionUrl);
        lb.setActionSuccess(RvaViewbutton.JS_LOAD_LIST);
        rvaViewbuttonMapper.insertRvaViewbutton(lb);
        return lb;
    }

    public RvaViewbutton createFormJsButton(String idSuffix, String name, String icon, Integer index, String cls, String viewId, String action) {
        RvaViewbutton lb = createButton(idSuffix, name, RvaViewbutton.TYPE_JS, icon, index, cls, RvaViewbutton.POSITION_FORM, viewId, false);
        lb.setAction(action);
        rvaViewbuttonMapper.insertRvaViewbutton(lb);
        return lb;
    }

    private RvaViewbutton createListButton(String idSuffix, String name, String type, String icon, Integer index, String cls, String viewId, String selectType, String position, Boolean cascadeDelete) {
        RvaViewbutton lb = createButton(idSuffix, name, type, icon, index, cls, position, viewId, cascadeDelete);
        lb.setSelectType(selectType);
        return lb;
    }

    private RvaViewbutton createButton(String idSuffix, String name, String type, String icon, Integer index, String cls, String position, String viewId, Boolean cascadeDelete) {
        RvaViewbutton lb = new RvaViewbutton();
        lb.setIcon(icon);
        if (RvaUtils.isEmpty(idSuffix)) {
            idSuffix = RvaUtils.generateKey32(6);
        }
        lb.setId(createButtonId(viewId, idSuffix));
        lb.setIdx(index);
        lb.setCls(cls);
        lb.setName(name);
        lb.setType(type);
        lb.setViewId(viewId);
        lb.setCascadeDelete(cascadeDelete);
        lb.setPosition(position);
        RvaViewbutton search = new RvaViewbutton();
        search.setPosition(lb.getPosition());
        search.setViewId(lb.getViewId());
        List<RvaViewbutton> list = rvaViewbuttonMapper.selectRvaViewbuttonList(search);
        Collections.sort(list, new Comparator<RvaViewbutton>() {
            @Override
            public int compare(RvaViewbutton o1, RvaViewbutton o2) {
                return o1.getIdx() - o2.getIdx();
            }
        });
        if (list.size() == 0 || index < 0) {
            lb.setIdx(0);
        } else {
            int max = 0;
            for (int i = 0; i < list.size(); i++) {
                if (index >= i) {
                    max = i;
                }
            }
            if (index > max) {
                lb.setIdx(list.size());
                for (int i = 0; i < list.size(); i ++) {
                    RvaViewbutton b = list.get(i);
                    updateButtonIndex(i, b);
                }
            } else {
                lb.setIdx(max);
                for (int i = 0; i < list.size(); i ++) {
                    RvaViewbutton b = list.get(i);
                    Boolean update = false;
                    if (i >= max) {
                        updateButtonIndex(i + 1, b);
                    } else {
                        updateButtonIndex(i, b);
                    }
                }
            }
        }
        return lb;
    }

    private void updateButtonIndex(int i, RvaViewbutton b) {
        if (!b.getIdx().equals(i)) {
            b.setIdx(i);
            rvaViewbuttonMapper.updateRvaViewbutton(b);
        }
    }

    private String createButtonId(String viewId, String buttonSuffix) {
        return viewId + "_" + buttonSuffix;
    }

    private final IRvaViewService viewService;


    private final RvaDataMapper dataMapper;

    private final RvaVelocityUtils velocityUtils;

    @Override
    public RvaMap<String, Object> submitSql(String id, RvaMap req) {
        RvaViewbutton viewbutton = rvaViewbuttonMapper.selectRvaViewbuttonById(id);
        List<String> sqls = viewService.parseSqls(viewbutton.getActionUrl());
        RvaMap<String, Object> results = new RvaMap<>();
        for (int i = 0; i < sqls.size(); i++) {
            String sql = velocityUtils.parseWithLoginUser(sqls.get(i), req);
            String[] sqlArr = sql.split(";");
            Boolean query = false;
            for (int i1 = 0; i1 < sqlArr.length; i1++) {
                if (sqlArr[i1].trim().startsWith("SELECT ")) {
                    query = true;
                }
            }
            if (query) {
                List<RvaMap<String, Object>> rvaMaps = dataMapper.selectList(sql);
                results.rvaPut(String.valueOf(i), rvaMaps);
            } else {
                int update = dataMapper.update(sql);
                results.rvaPut(String.valueOf(i), update);
            }
        }
        return results;
    }
}
