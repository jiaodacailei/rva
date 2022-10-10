package com.ruoyi.rva.framework.service;

import com.ruoyi.rva.framework.domain.RvaViewbutton;

import java.util.List;

public interface IRvaMetaRuntimeService {

    void createCrudCreateButton(String buttonId, Boolean cascade);

    void createTopDeleteButton(String buttonId);

    void createCrudUpdateButton(String buttonId, Boolean cascade);

    void createInnerDeleteButton(String buttonId);

    void createCrudCloneButton(String buttonId, Boolean cascade);

    void createMoveUpButton(String buttonId);

    void createMoveDownButton(String buttonId);

    void createSubmitCreateButton(String buttonId);

    void createSubmitUpdateButton(String buttonId);

    void createResetButton(String buttonId);

    void createCancelButton(String buttonId);

    void createButtonBySelectForm(String selectedFormView, RvaViewbutton selectedButton);

    void createButtonBySelectCrud(String c0_none_xuanzecrudjiananniu_crud, RvaViewbutton selectedButton);

    void createButtonByCreateTcrud(List<String> contents, List<String> navs, RvaViewbutton selectedButton);

    void createButtonBySelectTcrud(String tcrud, RvaViewbutton selectedButton);

    void createImportButtonBySelectForm(String selectedFormView, RvaViewbutton selectedButton);

    void createExportButton(String buttonId);
}
