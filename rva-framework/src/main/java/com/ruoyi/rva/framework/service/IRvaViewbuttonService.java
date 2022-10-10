package com.ruoyi.rva.framework.service;

import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaViewbutton;

/**
 * 视图属性Service接口
 * 
 * @author jiaodacailei
 * @date 2021-07-27
 */
public interface IRvaViewbuttonService 
{
    /**
     * 批量删除视图属性
     *
     * @param ids 需要删除的视图属性ID
     * @return 结果
     */
    public int deleteRvaViewbuttonByIds(String[] ids);

    RvaViewbutton createListCreateButton(String idSuffix, Integer index, String viewId, String relatedViewId, Boolean cascadeDelete);

    RvaViewbutton createListUpdateButton(String idSuffix, Integer index, String viewId, String relatedViewId, Boolean cascadeDelete);

    RvaViewbutton createListTopDeleteButton(String idSuffix, Integer index, String viewId);

    RvaViewbutton createListInnerDeleteButton(String idSuffix, Integer index, String viewId);

    RvaViewbutton createListCloneButton(String idSuffix, Integer index, String viewId, String relatedViewId, Boolean cascadeDelete);

    RvaViewbutton createListMoveUpButton(String idSuffix, Integer index, String viewId);

    RvaViewbutton createListMoveDownButton(String idSuffix, Integer index, String viewId);

    RvaViewbutton createListTopFormButton(String idSuffix, String name, String icon, Integer index, String cls, String viewId, String selectType, String relatedViewId, Boolean cascadeDelete);

    RvaViewbutton createListInnerFormButton(String idSuffix, String name, String icon, Integer index, String cls, String viewId, String relatedViewId, Boolean cascadeDelete);

    RvaViewbutton createListTopCrudButton(String idSuffix, String name, String icon, Integer index, String cls, String viewId, String selectType, String relatedAppId, Boolean cascadeDelete);

    RvaViewbutton createListInnerCrudButton(String idSuffix, String name, String icon, Integer index, String cls, String viewId, String relatedAppId, Boolean cascadeDelete);

    RvaViewbutton createListTopTcrudButton(String idSuffix, String name, String icon, Integer index, String cls, String viewId, String selectType, String relatedAppId, Boolean cascadeDelete);

    RvaViewbutton createListInnerTcrudButton(String idSuffix, String name, String icon, Integer index, String cls, String viewId, String relatedAppId, Boolean cascadeDelete);

    RvaViewbutton createListTopAjaxButton(String idSuffix, String name, String icon, Integer index, String cls, String viewId, String selectType, String actionUrl);

    RvaViewbutton createListInnerAjaxButton(String idSuffix, String name, String icon, Integer index, String cls, String viewId, String actionUrl);

    RvaViewbutton createListTopJsButton(String idSuffix, String name, String icon, Integer index, String cls, String viewId, String selectType, String action);

    RvaViewbutton createListInnerJsButton(String idSuffix, String name, String icon, Integer index, String cls, String viewId, String action);

    RvaViewbutton createFormSubmitCreateButton(String idSuffix, Integer index, String viewId);

    RvaViewbutton createFormSubmitUpdateButton(String idSuffix, Integer index, String viewId);

    RvaViewbutton createFormResetButton(String idSuffix, Integer index, String viewId);

    RvaViewbutton createFormCancelButton(String idSuffix, Integer index, String viewId);

    RvaViewbutton createFormAjaxButton(String idSuffix, String name, String icon, Integer index, String cls, String viewId, String actionUrl);

    RvaViewbutton createFormJsButton(String idSuffix, String name, String icon, Integer index, String cls, String viewId, String action);

    RvaMap<String, Object> submitSql(String id, RvaMap rvaMap);
}
