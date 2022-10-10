package com.ruoyi.rva.framework.service;

import com.ruoyi.common.core.domain.entity.SysDictType;
import com.ruoyi.rva.framework.domain.RvaApp;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaObject;
import com.ruoyi.rva.framework.domain.RvaView;
import com.ruoyi.rva.framework.util.RvaPinyinUtils;

import java.util.List;

public interface IRvaMetaService {

    RvaObject createObject(String table);

    /**
     * 修改表objId的字段值
     * @param objId
     * @param req
     * @return
     */
    void updateObject(String objId, RvaMap<String, Object> req);

    /**
     * 删除对象关联的所有元数据，包括对象、属性、关系、应用、视图等
     * @param table
     */
    void deleteAllObjectMeta(String table);

    /**
     * 根据数据库中table的元数据，刷新对象元数据，并更新crudx_*应用的所有数据
     * @param table
     */
    void refreshObject(String table);

    RvaApp createCrud(String table);

    public String createViewId (String objId, String prefix);

    RvaView cloneIndependentView(String srcViewId, Boolean cascaded);

    RvaView createIndependentSearchView(String idSuffix, List<String> propTypes);

    RvaView cloneView(String srcViewId, String prefix, Boolean cascaded);

    void deleteApp(String crudId);

    void deleteView(String viewId);

    void deleteViewbutton(String buttonId);

    void deleteViewproperty(String viewpropertyId);

    void createViewpropertyDict(RvaMap<String, Object> req);

    void updateViewpropertyDict(RvaMap<String, Object> req);

    void deleteViewpropertyDict(String viewpropertyId);

    /**
     * 被IRvaMetaService#createMenuAndButtons(com.ruoyi.rva.framework.domain.RvaMap)代替
     * @param crudId
     * @param parentId
     * @param orderNum
     */
    @Deprecated
    void createMenuAndButtonsByApp(String crudId, Long parentId, Integer orderNum);

    /**
     * RVA管理-应用：列表“菜单”按钮的点击响应
     * @param req
     */
    void createMenuAndButtonsByApp(RvaMap<String, Object> req);

    /**
     * RVA管理-视图：“菜单”按钮的点击响应
     * @param req
     */
    void createMenuAndButtonsByView(RvaMap<String, Object> req);

    void deleteMenuAndButtons(String... appIds);

    void createRvaMenu();

    RvaApp createTreeCrud (List<String> contents, List<String> navs, Boolean clone);

    void addContentsToTreeCrud(List<String> contents, String appId);

    void addNavsToTreeCrud(List<String> navs, String appId);

    SysDictType createDictData(String table, String typeCnName, String defaultValue, String[] data, Boolean containValue);

    default String createPropertyId(String table, String columnName) {
        return (table + "_" + columnName).toLowerCase();
    }

    default String createViewpropertyId(String viewId, String propName) {
        return (viewId + "_" + RvaPinyinUtils.getPinyinLower(propName)).toLowerCase();
    }

    default String createId (String prefix, int num, String suffix) {
        return prefix + num + "_" + suffix;
    }

    RvaApp cloneApp(String appId, Boolean cascaded, Boolean cascadeDelete);

    void deleteApp(List<String> appIds);

    void createListDictButtons(String buttonId);

    RvaMap getCreateSQL(String table);

    /**
     * 获取应用元数据SQL脚本
     * @param appId
     * @return
     */
    RvaMap getAppMetaSQL(String appId);

    /**
     * 获取应用用例文档
     * @param appId
     * @return
     */
    RvaMap getAppDocument(String appId);

    void quickCreateObject(String name, String no, String module, boolean uniApp, boolean tenant);

    RvaObject quickCreateObject(String objName, String objNo, String module, boolean uniApp, boolean tenant, String sqlTemplate);

    void addToBlackList(RvaMap<String, Object> req);

    RvaMap getAppUsage(String appId);

    RvaMap loadBlackList(RvaMap<String, Object> req);

    void deleteBlackList(String viewPropertyId);

    void addButtonToMenu(String buttonId);

    /**
     * 将视图属性【vpIds】的配置，同步到视图【viewIds】的视图属性上
     * @param viewIds 视图ID列表
     * @param vpIds 视图属性ID列表
     */
    void synchronize(List<String> viewIds, List<String> vpIds);

    /**
     * 获取视图元数据SQL脚本
     * @param viewId
     * @return
     */
    RvaMap getViewMetaSQL(String viewId);
}
