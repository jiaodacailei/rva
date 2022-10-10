package com.ruoyi.rva.framework.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.rva.framework.mapper.RvaObjectMapper;
import com.ruoyi.rva.framework.mapper.RvaTriggerMapper;
import com.ruoyi.rva.framework.mapper.RvaViewbuttonMapper;
import com.ruoyi.rva.framework.mapper.RvaViewpropertyMapper;
import com.ruoyi.rva.framework.util.RvaConstants;
import com.ruoyi.rva.framework.util.RvaUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 视图对象 rva_view
 *
 * @author jiaodacailei
 * @date 2021-07-29
 */
public class RvaView extends RvaBaseEntity
{
    private static final long serialVersionUID = 1L;

    public final static String TYPE_FORM = "form";

    public final static String TYPE_LIST = "list";

    public final static String TYPE_SEARCH = "search";

    public final static String FORM_CREATE = "create";

    public final static String FORM_UPDATE = "update";

    /**
     * url：加载view元数据
     */
    public final static String URL_VIEW = "/rva/view/{view}";

    /**
     * url：加载view列表数据
     */
    public final static String URL_LOAD_LIST = URL_VIEW + "/load/list";

    /**
     * url：加载view创建表单数据
     */
    public final static String URL_LOAD_CREATE = URL_VIEW + "/load/create";

    /**
     * url：加载view克隆表单数据
     */
    public final static String URL_LOAD_CLONE = URL_VIEW + "/load/clone";

    /**
     * url：提交crud创建表单
     */
    public final static String URL_SUBMIT_CREATE = URL_VIEW + "/submit/create";

    /**
     * url：加载view修改表单数据
     */
    public final static String URL_LOAD_UPDATE = URL_VIEW + "/load/update";

    /**
     * url：提交view修改表单
     */
    public final static String URL_SUBMIT_UPDATE = URL_VIEW + "/submit/update";

    /**
     * url：view删除
     */
    public final static String URL_DELETE = URL_VIEW + "/delete";

    /**
     * url：上移-调整索引字段值
     */
    public final static String URL_MOVE_UP = URL_VIEW + "/move/up";

    /**
     * url：下移-调整索引字段值
     */
    public final static String URL_MOVE_DOWN = URL_VIEW + "/move/down";

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

    /** 宽度 */
    @Excel(name = "宽度")
    private Integer width;

    /** 高度 */
    @Excel(name = "高度")
    private Integer height;

    /** 对象ID */
    @Excel(name = "对象ID")
    private String objId;

    /** 列数 */
    @Excel(name = "列数")
    private Integer formColumns;

    /** 只读 */
    @Excel(name = "只读")
    private String formReadonly;

    /** 列表最大行数 */
    @Excel(name = "列表最大行数")
    private Integer listRows;

    /** 是否分页 */
    @Excel(name = "是否分页")
    private String listPaging;

    /** 其他数据 */
    @Excel(name = "其他数据")
    private String data;

    /** 加载数据url */
    @Excel(name = "加载数据url")
    private String loadUrl;

    /** 加载前sql */
    @Excel(name = "加载前sql")
    private String loadBeforeSql;

    /** 表单提交url */
    @Excel(name = "表单提交url")
    private String formSubmitUrl;

    /** 加载后sql */
    @Excel(name = "加载后sql")
    private String loadAfterSql;

    /** 提交前sql */
    @Excel(name = "提交前sql")
    private String formSubmitBeforeSql;

    /** 加载数据sql */
    @Excel(name = "加载数据sql")
    private String loadSql;

    /** 提交后sql */
    @Excel(name = "提交后sql")
    private String formSubmitAfterSql;

    /** 数据加载条件 */
    @Excel(name = "数据加载条件")
    private String loadWhere;

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
    public void setWidth(Integer width)
    {
        this.width = width;
    }

    public Integer getWidth()
    {
        return width;
    }
    public void setHeight(Integer height)
    {
        this.height = height;
    }

    public Integer getHeight()
    {
        return height;
    }
    public void setObjId(String objId)
    {
        this.objId = objId;
    }

    public String getObjId()
    {
        return objId;
    }
    public void setFormColumns(Integer formColumns)
    {
        this.formColumns = formColumns;
    }

    public Integer getFormColumns()
    {
        return formColumns;
    }
    public void setFormReadonly(String formReadonly)
    {
        this.formReadonly = formReadonly;
    }

    public String getFormReadonly()
    {
        return formReadonly;
    }
    public void setListRows(Integer listRows)
    {
        this.listRows = listRows;
    }

    public Integer getListRows()
    {
        return listRows;
    }
    public void setListPaging(String listPaging)
    {
        this.listPaging = listPaging;
    }

    public String getListPaging()
    {
        return listPaging;
    }
    public void setData(String data)
    {
        this.data = data;
    }

    public String getData()
    {
        return data;
    }
    public void setLoadUrl(String loadUrl)
    {
        this.loadUrl = loadUrl;
    }

    public String getLoadUrl()
    {
        return loadUrl;
    }
    public void setLoadBeforeSql(String loadBeforeSql)
    {
        this.loadBeforeSql = loadBeforeSql;
    }

    public String getLoadBeforeSql()
    {
        return loadBeforeSql;
    }
    public void setFormSubmitUrl(String formSubmitUrl)
    {
        this.formSubmitUrl = formSubmitUrl;
    }

    public String getFormSubmitUrl()
    {
        return formSubmitUrl;
    }
    public void setLoadAfterSql(String loadAfterSql)
    {
        this.loadAfterSql = loadAfterSql;
    }

    public String getLoadAfterSql()
    {
        return loadAfterSql;
    }
    public void setFormSubmitBeforeSql(String formSubmitBeforeSql)
    {
        this.formSubmitBeforeSql = formSubmitBeforeSql;
    }

    public String getFormSubmitBeforeSql()
    {
        return formSubmitBeforeSql;
    }
    public void setLoadSql(String loadSql)
    {
        this.loadSql = loadSql;
    }

    public String getLoadSql()
    {
        return loadSql;
    }
    public void setFormSubmitAfterSql(String formSubmitAfterSql)
    {
        this.formSubmitAfterSql = formSubmitAfterSql;
    }

    public String getFormSubmitAfterSql()
    {
        return formSubmitAfterSql;
    }
    public void setLoadWhere(String loadWhere)
    {
        this.loadWhere = loadWhere;
    }

    public String getLoadWhere()
    {
        return loadWhere;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("type", getType())
            .append("idx", getIdx())
            .append("width", getWidth())
            .append("height", getHeight())
            .append("objId", getObjId())
            .append("formColumns", getFormColumns())
            .append("formReadonly", getFormReadonly())
            .append("listRows", getListRows())
            .append("listPaging", getListPaging())
            .append("data", getData())
            .append("loadUrl", getLoadUrl())
            .append("loadBeforeSql", getLoadBeforeSql())
            .append("formSubmitUrl", getFormSubmitUrl())
            .append("loadAfterSql", getLoadAfterSql())
            .append("formSubmitBeforeSql", getFormSubmitBeforeSql())
            .append("loadSql", getLoadSql())
            .append("formSubmitAfterSql", getFormSubmitAfterSql())
            .append("loadWhere", getLoadWhere())
            .toString();
    }

    public void setProperties(List<RvaViewproperty> properties) {
        this.properties = properties;
    }

    private List<RvaViewproperty> properties = null;

    private List<RvaViewbutton> buttons = null;

    private List<RvaTrigger> triggers = null;

    public RvaView loadData () {
        if (buttons == null) {
            RvaViewbutton search = new RvaViewbutton();
            search.setViewId(getId());
            buttons = SpringUtils.getBean(RvaViewbuttonMapper.class).selectRvaViewbuttonList(search);
            Collections.sort(buttons, new Comparator<RvaViewbutton>() {
                @Override
                public int compare(RvaViewbutton o1, RvaViewbutton o2) {
                    return o1.getIdx() - o2.getIdx();
                }
            });
        }
        if (properties == null) {
            RvaViewproperty search = new RvaViewproperty();
            search.setViewId(this.getId());
            properties = SpringUtils.getBean(RvaViewpropertyMapper.class).selectRvaViewpropertyList(search);
            Collections.sort(properties, new Comparator<RvaViewproperty>() {
                @Override
                public int compare(RvaViewproperty o1, RvaViewproperty o2) {
                    return o1.getIdx() - o2.getIdx();
                }
            });
            properties.forEach(prop -> {
                if (RvaViewproperty.TYPE_BUTTON.equals(prop.getType()) && RvaUtils.isEmpty(prop.getListButtons())) {
                    buttons.stream().filter(button -> RvaViewbutton.POSITION_INNER.equals(button.getPosition())).forEach(button-> {
                        if (RvaUtils.isNotEmpty(prop.getListButtons())) {
                            prop.setListButtons(prop.getListButtons() + "," + button.getId());
                        } else {
                            prop.setListButtons(button.getId());
                        }
                    });
                }
            });
        }
        if (triggers == null) {
            RvaTrigger search = new RvaTrigger();
            search.setViewId(getId());
            triggers = SpringUtils.getBean(RvaTriggerMapper.class).selectRvaTriggerList(search);
            Collections.sort(triggers);
        }
        return this;
    }

    public List<RvaViewproperty> getProperties() {
        loadData();
        return properties;
    }

    public List<RvaViewproperty> getPropertiesWithoutHidden() {
        return getProperties().stream().filter(p -> !RvaViewproperty.TYPE_HIDDEN.equals(p.getType())).collect(Collectors.toList());
    }

    public List<RvaViewproperty> getPropertiesRequired() {
        return getProperties().stream().filter(p -> "Y".equals(p.getFormRequired()) && !RvaViewproperty.TYPE_HIDDEN.equals(p.getType())).collect(Collectors.toList());
    }

    public List<RvaViewproperty> getPropertiesText() {
        return getProperties().stream().filter(p -> RvaViewproperty.TYPE_TEXT.equals(p.getType()) || RvaViewproperty.TYPE_TEXTAREA.equals(p.getType())).collect(Collectors.toList());
    }

    public List<RvaViewproperty> getHiddens() {
        return getProperties().stream().filter(p -> RvaViewproperty.TYPE_HIDDEN.equals(p.getType())).collect(Collectors.toList());
    }

    public List<RvaViewbutton> getButtons() {
        loadData();
        return buttons;
    }

    public List<RvaTrigger> getTriggers() {
        loadData();
        return triggers;
    }

    public RvaViewproperty getProperty (String propId) {
        for (RvaViewproperty viewproperty : getProperties()) {
            if (viewproperty.getId().equals(propId)) {
                return viewproperty;
            }
        }
        return null;
    }

    public RvaViewproperty getPropertyByObjectProperty (String propId) {
        for (RvaViewproperty viewproperty : getProperties()) {
            if (propId.equals(viewproperty.getPropId())) {
                return viewproperty;
            }
        }
        return null;
    }

    public RvaViewproperty getKeyProperty (RvaObject object) {
        return getPropertyByObjectProperty(object.getKeyProperty().getId());
    }

    public RvaViewproperty getNameProperty (RvaObject object) {
        return getPropertyByObjectProperty(object.getNameProperty().getId());
    }

    public RvaViewbutton getButton (String buttonId) {
        for (RvaViewbutton viewbutton : getButtons()) {
            if (viewbutton.getId().equals(buttonId)) {
                return viewbutton;
            }
        }
        return null;
    }

    public void setCascaded (Boolean cascaded) {
        setJsonProperty("cascaded", cascaded ? "Y" : "N");
    }

    public void setLoadWhereOfUpdateView (RvaObject obj) {
        this.setLoadWhere(obj.getNo() + "." + obj.getPropNameKey() + " = '${selection.get(0)['" + RvaConstants.PROP_KEY_VALUE + "']}'");
    }

    @JsonIgnore
    public RvaMap<String, Object> getListFieldsExpression() {
        RvaMap<String, Object> fields = new RvaMap<>();
        for (RvaViewproperty viewproperty: getProperties()) {
            RvaRelation relation = viewproperty.getRelation();
            if (relation != null) {
                RvaProperty prop = null;
                RvaObject relatedObj = relation.getRelatedObj();
                if (RvaUtils.isEmpty(viewproperty.getPropId())) {
                    prop = relatedObj.getNameProperty();
                } else {
                    prop = relatedObj.getProperty(viewproperty.getPropId());
                }
                String fieldExpression = RvaSQL.createSelectFieldExpression(viewproperty.getRelationId(), prop.getName(), viewproperty.getPropSubId());
                fields.rvaPut(viewproperty.getId(), fieldExpression);
            } else if (RvaUtils.isNotEmpty(viewproperty.getPropId())) {
                RvaObject object = SpringUtils.getBean(RvaObjectMapper.class).selectRvaObjectById(this.objId);
                RvaProperty prop = object.getProperty(viewproperty.getPropId());
                String fieldExpression = RvaSQL.createSelectFieldExpression(object.getNo(), prop.getName(), viewproperty.getPropSubId());
                if (RvaUtils.isNotEmpty(viewproperty.getPropSubId())) {
                    fields.getMap(prop.getId()).put(viewproperty.getPropSubId(), fieldExpression);
                } else {
                    fields.rvaPut(prop.getId(), fieldExpression);
                }
                fields.rvaPut(viewproperty.getId(), fieldExpression);
            }
        }
        return fields;
    }

    public String getLogTable() {
        return getJsonPropertyString("logTable", "N");
    }

    public void setLogTable(String logTable) {
        setJsonProperty("logTable", logTable);
    }

    public String getLogFkValue() {
        return getJsonPropertyString("logFkValue");
    }

    public void setLogFkValue(String logFkValue) {
        setJsonProperty("logFkValue", logFkValue);
    }

    public String getLogKeyValue() {
        return getJsonPropertyString("logKeyValue");
    }

    public void setLogKeyValue(String logKeyValue) {
        setJsonProperty("logKeyValue", logKeyValue);
    }

    public String getLogName() {
        return getJsonPropertyString("logName");
    }

    public void setLogName(String logName) {
        setJsonProperty("logName", logName);
    }

    public String getLogWhere() {
        return getJsonPropertyString("logWhere");
    }

    public void setLogWhere(String logWhere) {
        setJsonProperty("logWhere", logWhere);
    }

    public Boolean isLogShow() {
        return "Y".equals(getJsonPropertyString("logShow", "N"));
    }

    public void setLogShow() {
        setJsonProperty("logShow", "Y");
    }

    public Boolean isNestedTable() {
        return "Y".equals(getJsonPropertyString("isNestedTable", "N"));
    }

    public String getShieldProperties() {
        return getJsonPropertyString("shieldProperties");
    }

    public List<RvaViewproperty> getOrderByProperties() {
        List<RvaViewproperty> orderBys = new ArrayList<>();
        for (RvaViewproperty property : this.getProperties()) {
            if (property.getListOrderIdx() >= 0) {
                orderBys.add(property);
            }
        }
        Collections.sort(orderBys, new Comparator<RvaViewproperty>() {
            @Override
            public int compare(RvaViewproperty o1, RvaViewproperty o2) {
                return o1.getListOrderIdx() - o2.getListOrderIdx();
            }
        });
        return orderBys;
    }
}
