package com.ruoyi.rva.framework.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.rva.framework.mapper.RvaAppitemMapper;
import com.ruoyi.rva.framework.mapper.RvaChartMapper;
import com.ruoyi.rva.framework.mapper.RvaDataMapper;
import com.ruoyi.rva.framework.mapper.RvaKpiMapper;
import com.ruoyi.rva.framework.service.IRvaSystemService;
import com.ruoyi.rva.framework.util.RvaUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 应用对象 rva_app
 *
 * @author jiaodacailei
 * @date 2021-07-27
 */
public class RvaApp extends BaseEntity {
    private static final long serialVersionUID = 1L;

    public final static String TYPE_CRUD = "crud";

    /**
     * 左边树导航应用或者视图
     */
    public final static String TYPE_TCRUD = "tcrud";

    /**
     * activiti工作流
     */
    public final static String TYPE_ACT = "activiti";

    /**
     * portal，应用门户，将各种类型的应用组合起来
     */
    public final static String TYPE_PORTAL = "portal";

    /**
     * url：selector查询数据
     */
    public final static String URL_CRUD_SEARCH = "/rva/crud/{app}/search";

    /**
     * url：selector获取id的名称
     */
    public final static String URL_CRUD_GETBYIDS = "/rva/crud/{app}/getByIds";

    /**
     * url：加载tcrud的treeNode数据
     */
    public final static String URL_TCRUD_LOAD_NODE = "/rva/tcrud/{app}/load/node";

    /**
     * url：加载tcrud的content数据
     */
    public final static String URL_TCRUD_LOAD_CONTENT = "/rva/tcrud/{app}/load/content";

    /**
     * ID
     */
    private String id;

    /**
     * 名称
     */
    @Excel(name = "名称")
    private String name;

    /**
     * 类型
     */
    @Excel(name = "类型")
    private String type;

    /**
     * 索引
     */
    @Excel(name = "索引")
    private Integer idx;

    /**
     * 对象ID
     */
    @Excel(name = "对象ID")
    private String objId;

    /**
     * 模板
     */
    @Excel(name = "模板")
    private String template;

    /**
     * 列表模板
     */
    @Excel(name = "列表模板")
    private String listTemplate;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private String status;

    /**
     * 包含视图
     */
    @Excel(name = "包含视图")
    @Deprecated
    private String views;

    /**
     * 包含应用
     */
    @Excel(name = "包含应用")
    @Deprecated
    private String apps;

    /**
     * 其他数据
     */
    @Excel(name = "其他数据")
    private String data;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getObjId() {
        return objId;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

    public void setListTemplate(String listTemplate) {
        this.listTemplate = listTemplate;
    }

    public String getListTemplate() {
        return listTemplate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getViews() {
        return views;
    }

    public void setApps(String apps) {
        this.apps = apps;
    }

    public String getApps() {
        return apps;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("type", getType())
                .append("idx", getIdx())
                .append("objId", getObjId())
                .append("template", getTemplate())
                .append("listTemplate", getListTemplate())
                .append("status", getStatus())
                .append("views", getViews())
                .append("apps", getApps())
                .append("data", getData())
                .toString();
    }

    private static String getAppUrl(String url, String appId) {
        return url.replace("{app}", appId);
    }

    public static String getUrlCrudSearch(String appId) {
        return getAppUrl(URL_CRUD_SEARCH, appId);
    }

    public static String getUrlCrudByIds(String appId) {
        return getAppUrl(URL_CRUD_GETBYIDS, appId);
    }

    public static String getUrlTreeCrudLoadNode(String appId) {
        return getAppUrl(URL_TCRUD_LOAD_NODE, appId);
    }

    public static String getUrlTreeCrudLoadContent(String appId) {
        return getAppUrl(URL_TCRUD_LOAD_CONTENT, appId);
    }

    private List<RvaAppitem> appItems = null;

    public RvaApp loadData() {
        if (appItems == null) {
            RvaAppitem search = new RvaAppitem();
            search.setAppId(this.getId());
            appItems = SpringUtils.getBean(RvaAppitemMapper.class).selectRvaAppitemList(search);
            SysUser loginUser = SpringUtils.getBean(IRvaSystemService.class).getLoginUser();
            String itemIds = SpringUtils.getBean(RvaDataMapper.class).selectString("select GROUP_CONCAT(yingyongxiang_cd34) from sys_role_appitem_a4e7 where jiaosexinxibiao_5877 = " + loginUser.getRoleId());
            if (itemIds != null) {
                List<RvaAppitem> newItems = new ArrayList<>();
                appItems.forEach(item -> {
                    if (Arrays.asList(itemIds.split(",")).contains(item.getId())) {
                        return;
                    }
                    newItems.add(item);
                });
                appItems = newItems;
            }
            Collections.sort(appItems);
        }
        String searchId = this.getShareSearchIdByRvaAppitem(appItems);
        for (RvaAppitem appitem : appItems) {
            appitem.setShareSearchId(searchId);
        }
        return this;
    }

    public List<RvaAppitem> getAppItems() {
        loadData();
        return this.appItems;
    }

    public void setAppItems(List<RvaAppitem> appItems) {
        this.appItems = appItems;
    }

    public String getAppItemRelatedIdByType(String type) {
        for (RvaAppitem appItem : getAppItems()) {
            if (type.equals(appItem.getType())) {
                return appItem.getRelatedAppId();
            }
        }
        return null;
    }

    public String getCreateId() {
        return getAppItemRelatedIdByType(RvaView.FORM_CREATE);
    }

    public String getUpdateId() {
        return getAppItemRelatedIdByType(RvaView.FORM_UPDATE);
    }

    public String getListId() {
        return getAppItemRelatedIdByType(RvaView.TYPE_LIST);
    }

    public String getSearchId() {
        return getAppItemRelatedIdByType(RvaView.TYPE_SEARCH);
    }

    public List<RvaAppitem> getAppItemsByType(String type) {
        List<RvaAppitem> items = new ArrayList<>();
        for (RvaAppitem appItem : getAppItems()) {
            if (type.equals(appItem.getType())) {
                items.add(appItem);
            }
        }
        Collections.sort(items);
        if ("content".equals(type)) {
            String searchId = this.getShareSearchIdByRvaAppitem(items);
            for (RvaAppitem appitem : items) {
                appitem.setShareSearchId(searchId);
            }
        }
        return items;
    }

    private String getShareSearchIdByRvaAppitem(List<RvaAppitem> appItems) {
        if (ObjectUtils.isEmpty(appItems)) {
            return "";
        }


        RvaAppitem firstItem = appItems.get(0);
        String relatedAppType = firstItem.getRelatedAppType();
        String relatedAppId = firstItem.getRelatedAppId();
        String searchId = null;
        String shareQueryContext = "N";
        switch (relatedAppType) {
            case RvaAppitem.APP_CRUD:
                RvaAppitem queryRvaAppitem = new RvaAppitem();
                queryRvaAppitem.setAppId(relatedAppId);
                queryRvaAppitem.setType(RvaAppitem.TYPE_SEARCH);
                List<RvaAppitem> rvaAppitems = SpringUtils.getBean(RvaAppitemMapper.class).selectRvaAppitemList(queryRvaAppitem);

                if (!RvaUtils.isEmpty(rvaAppitems)) {
                    RvaAppitem rvaAppitem = rvaAppitems.get(0);
                    shareQueryContext = rvaAppitem.getJsonPropertyString("shareQueryContext");
                    if ("Y".equals(shareQueryContext)) {
                        searchId = rvaAppitem.getRelatedAppId();
                    }
                }
                break;
            case RvaAppitem.APP_KPI:
                RvaKpi rvaKpi = SpringUtils.getBean(RvaKpiMapper.class).selectRvaKpiById(relatedAppId);
                shareQueryContext = rvaKpi.getJsonPropertyString("shareQueryContext");
                if ("Y".equals(shareQueryContext)) {
                    searchId = rvaKpi.getSearchId();
                }
                break;
            case RvaAppitem.APP_CHART:
                RvaChart rvaChart = SpringUtils.getBean(RvaChartMapper.class).selectRvaChartById(relatedAppId);
                shareQueryContext = rvaChart.getJsonPropertyString("shareQueryContext");
                if ("Y".equals(shareQueryContext)) {
                    searchId = rvaChart.getSearchId();
                }
                break;
        }
        return searchId;
    }

    private String devMode = "Y";

    public String getDevMode() {
        return devMode;
    }

    public RvaApp setDevMode(String devMode) {
        this.devMode = devMode;
        return this;
    }

    public void setJsonProperty(String property, Object value) {
        this.data = RvaUtils.setJsonProperty(this.data, property, value);
    }

    public void setCascadeDelete(Boolean cascadeDelete) {
        setJsonProperty("cascadeDelete", cascadeDelete ? "Y" : "N");
    }

    public Boolean isCascadeDelete() {
        if (TYPE_CRUD.equals(this.type)) {
            return true;
        }
        return "Y".equals(RvaUtils.parseMap(this.data).getString("cascadeDelete"));
    }

    public void setCascaded(Boolean cascaded) {
        setJsonProperty("cascaded", cascaded ? "Y" : "N");
    }

    public String getJsonPropertyString(String key) {
        return RvaUtils.parseMap(getData()).getString(key);
    }
}
