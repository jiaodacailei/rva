package com.ruoyi.rva.framework.domain;

import com.ruoyi.rva.framework.util.RvaUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Arrays;

/**
 * 应用对象 rva_appitem
 *
 * @author jiaodacailei
 * @date 2021-09-05
 */
public class RvaAppitem extends RvaBaseEntity implements Comparable<RvaAppitem>
{
    private static final long serialVersionUID = 1L;

    /**
     * relatedAppType选项
     */
    public static final String APP_DIRECTORY = "directory";

    /**
     * relatedAppType选项
     */
    public static final String APP_FORM = RvaView.TYPE_FORM;

    /**
     * relatedAppType选项
     */
    public static final String APP_LIST = RvaView.TYPE_LIST;

    /**
     * relatedAppType选项
     */
    public static final String APP_SEARCH = RvaView.TYPE_SEARCH;

    /**
     * relatedAppType选项
     */
    public static final String APP_CRUD = RvaApp.TYPE_CRUD;

    /**
     * relatedAppType选项
     */
    public static final String APP_TCRUD = RvaApp.TYPE_TCRUD;

    /**
     * relatedAppType选项
     */
    public static final String APP_KPI = "kpi";

    /**
     * relatedAppType选项
     */
    public static final String APP_CHART = "chart";

    /**
     * type选项，新建表单视图
     */
    public static final String TYPE_CREATE = RvaView.FORM_CREATE;

    /**
     * type选项，修改表单视图
     */
    public static final String TYPE_UPDATE = RvaView.FORM_UPDATE;

    /**
     * type选项，列表视图
     */
    public static final String TYPE_LIST = RvaView.TYPE_LIST;

    /**
     * type选项，查询视图
     */
    public static final String TYPE_SEARCH = RvaView.TYPE_SEARCH;

    /**
     * type选项，tcrud导航项
     */
    public static final String TYPE_NAV = "nav";

    /**
     * type选项，tcrud内容项
     */
    public static final String TYPE_CONTENT = "content";

    /** ID */
    private String id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 索引 */
    @Excel(name = "索引")
    private Integer idx;

    /** 应用类型(form-表单视图 crud-增删改查 tcrud-应用组合) */
    @Excel(name = "应用类型(directory-目录 form-表单视图 crud-增删改查 tcrud-应用组合)")
    private String relatedAppType;

    /** 应用ID */
    @Excel(name = "应用ID")
    private String relatedAppId;

    /** 条件 */
    @Excel(name = "条件")
    private String showIf;

    /** 类型 */
    @Excel(name = "类型")
    private String type;

    /** 子类型 */
    @Excel(name = "子类型")
    private String subType;

    /** ID */
    @Excel(name = "ID")
    private String parentId;

    /** 应用ID */
    @Excel(name = "应用ID")
    private String appId;

    /** 其他数据 */
    @Excel(name = "其他数据")
    private String data;

    /**
     * 共享查询ID
     */
    private String shareSearchId;


    public String getShareSearchId() {
        return shareSearchId;
    }

    public void setShareSearchId(String shareSearchId) {
        this.shareSearchId = shareSearchId;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setIdx(Integer idx)
    {
        this.idx = idx;
    }

    public Integer getIdx()
    {
        return idx;
    }
    public void setRelatedAppType(String relatedAppType)
    {
        this.relatedAppType = relatedAppType;
    }

    public String getRelatedAppType()
    {
        return relatedAppType;
    }
    public void setRelatedAppId(String relatedAppId)
    {
        this.relatedAppId = relatedAppId;
    }

    public String getRelatedAppId()
    {
        return relatedAppId;
    }
    public void setShowIf(String showIf)
    {
        this.showIf = showIf;
    }

    public String getShowIf()
    {
        return showIf;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setSubType(String subType)
    {
        this.subType = subType;
    }

    public String getSubType()
    {
        return subType;
    }
    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }

    public String getParentId()
    {
        return parentId;
    }
    public void setAppId(String appId)
    {
        this.appId = appId;
    }

    public String getAppId()
    {
        return appId;
    }
    public void setData(String data)
    {
        this.data = data;
    }

    public String getData()
    {
        return data;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("idx", getIdx())
            .append("relatedAppType", getRelatedAppType())
            .append("relatedAppId", getRelatedAppId())
            .append("showIf", getShowIf())
            .append("type", getType())
            .append("subType", getSubType())
            .append("parentId", getParentId())
            .append("appId", getAppId())
            .append("data", getData())
            .toString();
    }

    @Override
    public int compareTo(RvaAppitem o) {
        return this.getIdx() - o.getIdx();
    }

    public Boolean isView () {
        return Arrays.asList(APP_FORM, APP_LIST, APP_SEARCH).contains(this.getRelatedAppType());
    }

    public Boolean isApp () {
        if (APP_DIRECTORY.equals(this.relatedAppType) || RvaUtils.isEmpty(relatedAppType)) {
            return false;
        }
        return !isView();
    }

    public Boolean isPermission() {
        return "Y".equals(getJsonPropertyString("isPermission", "Y"));
    }

    public void setPermission(Boolean permission) {
        setJsonProperty("isPermission", permission ? "Y" : "N");
    }
}
