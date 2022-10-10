package com.ruoyi.rva.framework.domain;

import com.ruoyi.rva.framework.util.RvaJsonUtils;
import com.ruoyi.rva.framework.util.RvaUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 视图属性对象 rva_viewbutton
 * 
 * @author jiaodacailei
 * @date 2021-07-28
 */
public class RvaViewbutton extends RvaBaseEntity
{
    private static final long serialVersionUID = 1L;

    /**
     * 类型：打开Dialog
     */
    public final static String TYPE_FORM = "form";
    /**
     * 类型：请求Json，post
     */
    public final static String TYPE_CRUD = "crud";
    /**
     * 类型：打开Tcrud
     */
    public final static String TYPE_TCRUD = "tcrud";
    /**
     * 类型：重置
     */
    public final static String TYPE_AJAX = "ajax";
    /**
     * 类型：取消
     */
    public final static String TYPE_JS = "js";

    /**
     * selectType选项：单选
     */
    public final static String SELECT_SINGLE = "single";

    /**
     * selectType选项：多选
     */
    public final static String SELECT_MULTIPLE = "multiple";

    /**
     * selectType选项：默认值，不论选择与否
     */
    public final static String SELECT_NONE = "none";

    /**
     * selectType选项：单选或不选
     */
    public final static String SELECT_SINGLE_NONE = "singleornone";

    /**
     * position选项：默认值，列表上方
     */
    public final static String POSITION_TOP = "top";

    /**
     * position选项：列表内
     */
    public final static String POSITION_INNER = "inner";

    /**
     * position选项：表单内
     */
    public final static String POSITION_FORM = "form";

    /**
     * cls选项：默认值，主样式
     */
    public final static String CLS_PRIMARY = "primary";

    /**
     * cls选项：成功
     */
    public final static String CLS_SUCCESS = "success";

    /**
     * cls选项：信息
     */
    public final static String CLS_INFO = "info";

    /**
     * cls选项：警告
     */
    public final static String CLS_WARNING = "warning";

    /**
     * cls选项：危险
     */
    public final static String CLS_DANGER = "danger";

    /**
     * action选项：默认值，弹框
     */
    public final static String ACTION_DIALOG = "dialog";

    /**
     * action选项：增加一个新tab，实际上是一个新路由
     */
    public final static String ACTION_TAB = "tab";

    /**
     * type=js时的action选项：loadList/reloadView/reload/resetView/closeView
     */
    public final static String JS_LOAD_LIST = "loadList";

    /**
     * type=js时的action选项：reloadView
     */
    public final static String JS_RELOAD_VIEW = "reloadView";

    /**
     * type=js时的action选项：reload
     */
    public final static String JS_RELOAD = "reload";

    /**
     * type=js时的action选项：resetView
     */
    public final static String JS_RESET_VIEW = "resetView";

    /**
     * type=js时的action选项：closeView
     */
    public final static String JS_CLOSE_VIEW = "closeView";

    /** ID */
    private String id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 类型 */
    @Excel(name = "类型")
    private String type;

    /** 索引 */
    @Excel(name = "索引")
    private Integer idx;

    /** 图标 */
    @Excel(name = "图标")
    private String icon;

    /** 样式 */
    @Excel(name = "样式")
    private String cls;

    /** 位置(inner-内部 top-顶部 bottom-底部) */
    @Excel(name = "位置(inner-内部 top-顶部 bottom-底部)")
    private String position;

    /** 只读显示 */
    @Excel(name = "只读显示")
    private String readonlyShow;

    /** 动作(dialog-弹框 ajax-后端调用 js-前端调用) */
    @Excel(name = "动作(dialog-弹框 ajax-后端调用 js-前端调用)")
    private String action;

    /** 动作Url */
    @Excel(name = "动作Url")
    private String actionUrl;

    /** 弹框视图 */
    @Excel(name = "弹框视图")
    private String actionDialogViewId;

    /** 弹框应用 */
    @Excel(name = "弹框应用")
    private String actionDialogAppId;

    /** 动作成功 */
    @Excel(name = "动作成功")
    private String actionSuccess;

    /** selectType */
    @Excel(name = "selectType")
    private String selectType;

    /** 动作失败 */
    @Excel(name = "动作失败")
    private String actionFailure;

    /** 显示条件 */
    @Excel(name = "显示条件")
    private String showIf;

    /** 视图ID */
    @Excel(name = "视图ID")
    private String viewId;

    /** 其他数据 */
    @Excel(name = "其他数据")
    private String data;

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
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setIdx(Integer idx) 
    {
        this.idx = idx;
    }

    public Integer getIdx() 
    {
        return idx;
    }
    public void setIcon(String icon) 
    {
        this.icon = icon;
    }

    public String getIcon() 
    {
        return icon;
    }
    public void setCls(String cls) 
    {
        this.cls = cls;
    }

    public String getCls() 
    {
        return cls;
    }
    public void setPosition(String position) 
    {
        this.position = position;
    }

    public String getPosition() 
    {
        return position;
    }
    public void setReadonlyShow(String readonlyShow) 
    {
        this.readonlyShow = readonlyShow;
    }

    public String getReadonlyShow() 
    {
        return readonlyShow;
    }
    public void setAction(String action) 
    {
        this.action = action;
    }

    public String getAction() 
    {
        return action;
    }
    public void setActionUrl(String actionUrl) 
    {
        this.actionUrl = actionUrl;
    }

    public String getActionUrl() 
    {
        return actionUrl;
    }
    public void setActionDialogViewId(String actionDialogViewId) 
    {
        this.actionDialogViewId = actionDialogViewId;
    }

    public String getActionDialogViewId() 
    {
        return actionDialogViewId;
    }
    public void setActionDialogAppId(String actionDialogAppId) 
    {
        this.actionDialogAppId = actionDialogAppId;
    }

    public String getActionDialogAppId() 
    {
        return actionDialogAppId;
    }
    public void setActionSuccess(String actionSuccess) 
    {
        this.actionSuccess = actionSuccess;
    }

    public String getActionSuccess() 
    {
        return actionSuccess;
    }
    public void setSelectType(String selectType) 
    {
        this.selectType = selectType;
    }

    public String getSelectType() 
    {
        return selectType;
    }
    public void setActionFailure(String actionFailure) 
    {
        this.actionFailure = actionFailure;
    }

    public String getActionFailure() 
    {
        return actionFailure;
    }
    public void setShowIf(String showIf) 
    {
        this.showIf = showIf;
    }

    public String getShowIf() 
    {
        return showIf;
    }
    public void setViewId(String viewId) 
    {
        this.viewId = viewId;
    }

    public String getViewId() 
    {
        return viewId;
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
            .append("type", getType())
            .append("idx", getIdx())
            .append("icon", getIcon())
            .append("cls", getCls())
            .append("position", getPosition())
            .append("readonlyShow", getReadonlyShow())
            .append("action", getAction())
            .append("actionUrl", getActionUrl())
            .append("actionDialogViewId", getActionDialogViewId())
            .append("actionDialogAppId", getActionDialogAppId())
            .append("actionSuccess", getActionSuccess())
            .append("selectType", getSelectType())
            .append("actionFailure", getActionFailure())
            .append("showIf", getShowIf())
            .append("viewId", getViewId())
            .append("data", getData())
            .toString();
    }

    public void setCascadeDelete (Boolean cascadeDelete) {
        setJsonProperty("cascadeDelete", cascadeDelete ? "Y" : "N");
    }

    public Boolean isCascadeDelete () {
        return "Y".equals(RvaUtils.parseMap(this.data).getString("cascadeDelete"));
    }

    /**
     *
     * @param data
     * @return
     */
    public static RvaViewbutton create(Object data) {
        return RvaJsonUtils.readAsTypeByData(data, RvaViewbutton.class);
    }

    public Boolean isPositionTop () {
        return POSITION_TOP.equals(getPosition());
    }

    public Boolean isPositionInner () {
        return POSITION_INNER.equals(getPosition());
    }

    public Boolean isTypeForm () {
        return TYPE_FORM.equals(getType());
    }

    private final static String PARAM_DATA = "param";

    public RvaMap getParams () {
        return getJsonPropertyMap(PARAM_DATA);
    }

    public void setParam(String key, Object val) {
        RvaMap map = getJsonPropertyMap(PARAM_DATA).rvaPut(key, val);
        setJsonProperty(PARAM_DATA, map);
    }

    public String getConfirmMessage() {
        String confirmMessage = getJsonPropertyString("confirmMessage", "");
        if (RvaUtils.isEmpty(confirmMessage) && CLS_DANGER.equals(this.cls)) {
            confirmMessage = "该操作比较危险，请再次确认！";
        }
        return confirmMessage;
    }
}
