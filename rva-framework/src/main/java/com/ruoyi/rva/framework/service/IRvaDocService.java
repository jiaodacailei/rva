package com.ruoyi.rva.framework.service;

import com.ruoyi.rva.framework.domain.RvaApp;
import com.ruoyi.rva.framework.domain.RvaView;
import com.ruoyi.rva.framework.domain.RvaViewproperty;

import java.util.List;

/**
 * 生成需求用例文档服务
 */
public interface IRvaDocService {

    String getViewpropertyDoc (RvaViewproperty viewproperty);

    RvaApp getApp(String appId);

    RvaView getView(String viewId);

    List<RvaViewproperty> getHiddensWithoutPK(RvaView view);
}
