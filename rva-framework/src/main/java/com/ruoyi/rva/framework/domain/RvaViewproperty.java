package com.ruoyi.rva.framework.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.rva.framework.mapper.RvaObjectMapper;
import com.ruoyi.rva.framework.mapper.RvaPropertyMapper;
import com.ruoyi.rva.framework.mapper.RvaRelationMapper;
import com.ruoyi.rva.framework.mapper.RvaViewMapper;
import com.ruoyi.rva.framework.util.RvaConstants;
import com.ruoyi.rva.framework.util.RvaUtils;
import com.ruoyi.system.service.ISysDictDataService;
import com.ruoyi.system.service.ISysDictTypeService;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.*;

/**
 * 视图属性对象 rva_viewproperty
 *
 * @author jiaodacailei
 * @date 2021-08-26
 */
public class RvaViewproperty extends RvaBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 隐藏
     */
    public final static String TYPE_HIDDEN = "hidden";

    /**
     * 文本
     */
    public final static String TYPE_TEXT = "textfield";

    /**
     * 文本数组
     */
    public final static String TYPE_TEXT_ARRAY = "textarray";

    /**
     * 级联选择
     */
    public final static String TYPE_CASCADER = "cascader";

    /**
     * 数据字典
     */
    public final static String TYPE_DICTIONARY = "dictionary";

    /**
     * 图标
     */
    public final static String TYPE_ICON = "icon";

    /**
     * 附件
     */
    public final static String TYPE_FILE = "file";

    /**
     * 图片
     */
    public final static String TYPE_IMAGE = "image";

    public final static String TYPE_NUMBER = "numberfield";

    public final static String TYPE_SELECTOR = "selector";

    public final static String TYPE_OBJECTSELECTOR = "objectselector";

    public final static String TYPE_SELECT = "select";

    public final static String TYPE_CRUD = "crud";

    public final static String TYPE_DATETIME = "datetime";

    public final static String TYPE_DATE = "date";

    public final static String TYPE_TEXTAREA = "textarea";

    public final static String TYPE_COMBO = "combo";

    public final static String TYPE_CHECK = "checkbox";

    public final static String TYPE_RADIO = "radio";

    public final static String TYPE_COMBOM21 = "comboM21";

    public final static String TYPE_ORGSEARCH = "orgSearch";

    public final static String TYPE_FIELDSET = "fieldset";

    /**
     * 时间范围，查询用
     */
    public final static String TYPE_DATETIME_RANGE = "datetimerange";

    /**
     * 按钮，操作列
     */
    public final static String TYPE_BUTTON = "button";

    /**
     * searchType的选项值，表示：模糊匹配.
     */
    public final static String SEARCH_LIKE = "like";

    /**
     * searchType的选项值，表示：完全匹配.
     */
    public final static String SEARCH_EQUAL = "equal";

    /**
     * searchType的选项值，表示：数值区间.
     */
    public final static String SEARCH_NUMBER_RANGE = "numberrange";

    /**
     * searchType的选项值，表示：日期时间区间.
     */
    public final static String SEARCH_DATETIME_RANGE = "datetimerange";


    /**
     * searchType的选项值，表示：根据velocity表达式匹配.
     */
    public final static String SEARCH_EXPRESSION = "expression";

    /**
     * listOrderType的选项值，表示：升序.
     */
    public final static String LIST_ORDER_ASC = "ASC";

    /**
     * listOrderType的选项值，表示：降序.
     */
    public final static String LIST_ORDER_DESC = "DESC";

    /**
     * data中的配置参数，如果视图属性有这个参数，则需要在结果集中多返回一个参数，key是${id}__value.
     */
    public final static String PARAM_FORMATTER = "formatter";

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
     * 属性ID
     */
    @Excel(name = "属性ID")
    private String propId;

    /**
     * 属性子ID
     */
    @Excel(name = "属性子ID")
    private String propSubId;

    /**
     * 关系ID
     */
    @Excel(name = "关系ID")
    private String relationId;

    /**
     * 宽度
     */
    @Excel(name = "宽度")
    private Integer width;

    /**
     * 高度
     */
    @Excel(name = "高度")
    private Integer height;

    /**
     * 加载值
     */
    @Excel(name = "加载值")
    private String formInitValue;

    /**
     * 空时替换加载值
     */
    @Excel(name = "空时替换加载值")
    private String formInitReplaceEmpty;

    /**
     * 非空替换加载值
     */
    @Excel(name = "非空替换加载值")
    private String formInitReplace;

    /**
     * 提交值
     */
    @Excel(name = "提交值")
    private String formSubmitValue;

    /**
     * 空时替换提交值
     */
    @Excel(name = "空时替换提交值")
    private String formSubmitReplaceEmpty;

    /**
     * 非空替换提交值
     */
    @Excel(name = "非空替换提交值")
    private String formSubmitReplace;

    /**
     * 是否提交
     */
    @Excel(name = "是否提交")
    private String formSubmit;

    /**
     * 跨行
     */
    @Excel(name = "跨行")
    private Integer formRowSpan;

    /**
     * 跨列
     */
    @Excel(name = "跨列")
    private Integer formColSpan;

    /**
     * 必需
     */
    @Excel(name = "必需")
    private String formRequired;

    /**
     * 最大值
     */
    @Excel(name = "最大值")
    private String formValueMax;

    /**
     * 最小值
     */
    @Excel(name = "最小值")
    private String formValueMin;

    /**
     * 只读
     */
    @Excel(name = "只读")
    private String formReadonly;

    /**
     * 关联CRUD
     */
    @Excel(name = "关联CRUD")
    private String formRelatedCrud;

    /**
     * selector单选
     */
    @Excel(name = "selector单选")
    private String formSelectorSingle;

    /**
     * inputor查询url
     */
    @Excel(name = "inputor查询url")
    private String formInputorSearch;

    /**
     * inputor数据url
     */
    @Excel(name = "inputor数据url")
    private String formInputorData;

    /**
     * 过滤属性ID
     */
    @Excel(name = "过滤属性ID")
    private String formFilterProp;

    /**
     * 过滤属性值
     */
    @Excel(name = "过滤属性值")
    private String formFilterValue;

    /**
     * 排序方式
     */
    @Excel(name = "排序方式")
    private String listOrderType;

    /**
     * 排序索引
     */
    @Excel(name = "排序索引")
    private Integer listOrderIdx;

    /**
     * sql获取值
     */
    @Excel(name = "sql获取值")
    private String listSql;

    /**
     * 表达式获取值
     */
    @Excel(name = "表达式获取值")
    private String listExpression;

    /**
     * 按钮
     */
    @Excel(name = "按钮")
    private String listButtons;

    /**
     * 查询过滤器
     */
    @Excel(name = "查询过滤器")
    private String searchFilter;

    /**
     * 查询类型
     */
    @Excel(name = "查询类型")
    private String searchType;

    /**
     * 查询表达式
     */
    @Excel(name = "查询表达式")
    private String searchExpression;

    /**
     * 小数位数
     */
    @Excel(name = "小数位数")
    private Integer numberScale;

    /**
     * 字典类型
     */
    @Excel(name = "字典类型")
    private String dictType;

    /**
     * 视图ID
     */
    @Excel(name = "视图ID")
    private String viewId;

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

    public void setPropId(String propId) {
        this.propId = propId;
    }

    public String getPropId() {
        return propId;
    }

    public void setPropSubId(String propSubId) {
        this.propSubId = propSubId;
    }

    public String getPropSubId() {
        return propSubId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getWidth() {
        return width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getHeight() {
        return height;
    }

    public void setFormInitValue(String formInitValue) {
        this.formInitValue = formInitValue;
    }

    public String getFormInitValue() {
        return formInitValue;
    }

    public void setFormInitReplaceEmpty(String formInitReplaceEmpty) {
        this.formInitReplaceEmpty = formInitReplaceEmpty;
    }

    public String getFormInitReplaceEmpty() {
        return formInitReplaceEmpty;
    }

    public void setFormInitReplace(String formInitReplace) {
        this.formInitReplace = formInitReplace;
    }

    public String getFormInitReplace() {
        return formInitReplace;
    }

    public void setFormSubmitValue(String formSubmitValue) {
        this.formSubmitValue = formSubmitValue;
    }

    public String getFormSubmitValue() {
        return formSubmitValue;
    }

    public void setFormSubmitReplaceEmpty(String formSubmitReplaceEmpty) {
        this.formSubmitReplaceEmpty = formSubmitReplaceEmpty;
    }

    public String getFormSubmitReplaceEmpty() {
        return formSubmitReplaceEmpty;
    }

    public void setFormSubmitReplace(String formSubmitReplace) {
        this.formSubmitReplace = formSubmitReplace;
    }

    public String getFormSubmitReplace() {
        return formSubmitReplace;
    }

    public void setFormSubmit(String formSubmit) {
        this.formSubmit = formSubmit;
    }

    public String getFormSubmit() {
        return formSubmit;
    }

    public void setFormRowSpan(Integer formRowSpan) {
        this.formRowSpan = formRowSpan;
    }

    public Integer getFormRowSpan() {
        return formRowSpan;
    }

    public void setFormColSpan(Integer formColSpan) {
        this.formColSpan = formColSpan;
    }

    public Integer getFormColSpan() {
        return formColSpan;
    }

    public void setFormRequired(String formRequired) {
        this.formRequired = formRequired;
    }

    public String getFormRequired() {
        return formRequired;
    }

    public void setFormValueMax(String formValueMax) {
        this.formValueMax = formValueMax;
    }

    public String getFormValueMax() {
        return formValueMax;
    }

    public void setFormValueMin(String formValueMin) {
        this.formValueMin = formValueMin;
    }

    public String getFormValueMin() {
        return formValueMin;
    }

    public void setFormReadonly(String formReadonly) {
        this.formReadonly = formReadonly;
    }

    public String getFormReadonly() {
        return formReadonly;
    }

    public void setFormRelatedCrud(String formRelatedCrud) {
        this.formRelatedCrud = formRelatedCrud;
    }

    public String getFormRelatedCrud() {
        return formRelatedCrud;
    }

    public void setFormSelectorSingle(String formSelectorSingle) {
        this.formSelectorSingle = formSelectorSingle;
    }

    public String getFormSelectorSingle() {
        return formSelectorSingle;
    }

    public void setFormInputorSearch(String formInputorSearch) {
        this.formInputorSearch = formInputorSearch;
    }

    public String getFormInputorSearch() {
        return formInputorSearch;
    }

    public void setFormInputorData(String formInputorData) {
        this.formInputorData = formInputorData;
    }

    public String getFormInputorData() {
        return formInputorData;
    }

    public void setFormFilterProp(String formFilterProp) {
        this.formFilterProp = formFilterProp;
    }

    public String getFormFilterProp() {
        return formFilterProp;
    }

    public void setFormFilterValue(String formFilterValue) {
        this.formFilterValue = formFilterValue;
    }

    public String getFormFilterValue() {
        return formFilterValue;
    }

    public void setListOrderType(String listOrderType) {
        this.listOrderType = listOrderType;
    }

    public String getListOrderType() {
        return listOrderType;
    }

    public void setListOrderIdx(Integer listOrderIdx) {
        this.listOrderIdx = listOrderIdx;
    }

    public Integer getListOrderIdx() {
        return listOrderIdx;
    }

    public void setListSql(String listSql) {
        this.listSql = listSql;
    }

    public String getListSql() {
        return listSql;
    }

    public void setListExpression(String listExpression) {
        this.listExpression = listExpression;
    }

    public String getListExpression() {
        return listExpression;
    }

    public void setListButtons(String listButtons) {
        this.listButtons = listButtons;
    }

    public String getListButtons() {
        return listButtons;
    }

    public void setSearchFilter(String searchFilter) {
        this.searchFilter = searchFilter;
    }

    public String getSearchFilter() {
        return searchFilter;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchExpression(String searchExpression) {
        this.searchExpression = searchExpression;
    }

    public String getSearchExpression() {
        return searchExpression;
    }

    public void setNumberScale(Integer numberScale) {
        this.numberScale = numberScale;
    }

    public Integer getNumberScale() {
        return numberScale;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getDictType() {
        return dictType;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public String getViewId() {
        return viewId;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public Boolean hasMultipleValues() {
        if (TYPE_CHECK.equals(type)) {
            return true;
        }
        return Arrays.asList(TYPE_SELECT, TYPE_SELECTOR, TYPE_OBJECTSELECTOR).contains(type) && "N".equals(formSelectorSingle);
    }

    private Map<String, Object> relatedCrudViewData = new HashMap<>();

    public Map<String, Object> getRelatedCrudViewData() {
        return relatedCrudViewData;
    }

    public void setRelatedCrudViewData(Map<String, Object> relatedCrudViewData) {
        this.relatedCrudViewData = relatedCrudViewData;
    }

    public void setCascadeDelete(Boolean cascade) {
        setJsonProperty("cascadeDelete", cascade ? "Y" : "N");
    }

    public Boolean isCascadeDelete() {
        return "Y".equals(RvaUtils.parseMap(this.data).getString("cascadeDelete"));
    }

    @JsonIgnore
    public Boolean hasRelation() {
        return RvaUtils.isNotEmpty(getRelationId());
    }

    @JsonIgnore
    public RvaRelation getRelation() {
        if (hasRelation()) {
            RvaRelation relation = SpringUtils.getBean(RvaRelationMapper.class).selectRvaRelationById(getRelationId());
            if (relation == null) {
                RvaUtils.throwQueryException("关系", "relationId=" + getRelationId());
            }
            return relation;
        }
        return null;
    }

    @JsonIgnore
    public Boolean hasProperty() {
        return RvaUtils.isNotEmpty(getPropId());
    }

    public String getParamFormatter() {
        return this.getJsonPropertyString(PARAM_FORMATTER);
    }

    public void setParamFormatter(String formatter) {
        this.setJsonProperty(PARAM_FORMATTER, formatter);
    }

    /**
     * 根据查询视图属性的控件类型，设置其默认的查询类型
     */
    public void setSearchTypeByType() {
        if (RvaViewproperty.TYPE_TEXT.equals(this.type)) {
            this.searchType = RvaViewproperty.SEARCH_LIKE;
        } else if (RvaViewproperty.TYPE_DATETIME_RANGE.equals(this.type)) {
            this.searchType = RvaViewproperty.SEARCH_DATETIME_RANGE;
        } else {
            this.searchType = RvaViewproperty.SEARCH_EQUAL;
        }
    }

    public Boolean hasJsonArrayValue() {
        return Arrays.asList(TYPE_TEXT_ARRAY, TYPE_CASCADER).contains(type);
    }

    public List<SysDictData> getDictDataList() {
        if (RvaUtils.isEmpty(dictType)) {
            return new ArrayList<>();
        }
        List<SysDictData> sysDictData = SpringUtils.getBean(ISysDictTypeService.class).selectDictDataByType(this.dictType);
        if (sysDictData == null) {
            return new ArrayList<>();
        }
        return sysDictData;
    }

    public String getDictName(String val) {
        List<SysDictData> dictDataList = this.getDictDataList();
        for (SysDictData sysDictData : dictDataList) {
            if (val.equals(sysDictData.getDictValue())) {
                return sysDictData.getDictLabel();
            }
        }
        return "";
    }

}
