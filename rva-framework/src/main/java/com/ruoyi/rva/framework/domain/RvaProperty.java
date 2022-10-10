package com.ruoyi.rva.framework.domain;

import com.ruoyi.rva.framework.util.RvaDateUtils;
import com.ruoyi.rva.framework.util.RvaPinyinUtils;
import com.ruoyi.rva.framework.util.RvaUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 属性对象 rva_property
 *
 * @author jiaodacailei
 * @date 2021-07-27
 */
public class RvaProperty extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**
     * 类型：文本
     */
    public final static String TYPE_VARCHAR = "VARCHAR";
    /**
     * 类型：超大文本
     */
    public final static String TYPE_TEXT = "TEXT";
    /**
     * 类型：小整型
     */
    public final static String TYPE_SMALLINT = "SMALLINT";
    /**
     * 类型：整型
     */
    public final static String TYPE_INTEGER = "INTEGER";
    /**
     * 类型：大整型
     */
    public final static String TYPE_BIGINT = "BIGINT";
    /**
     * 类型：数字
     */
    public final static String TYPE_NUMERIC = "NUMERIC";
    /**
     * 类型：日期
     */
    public final static String TYPE_DATE = "DATE";
    /**
     * 类型：日期时间
     */
    public final static String TYPE_DATETIME = "DATETIME";

    public final static String AUTO_INCREMENT = "AUTO_INCREMENT";

    /**
     * data中的json属性，用作本对象的特殊标记
     */
    public final static String PARAM_TAG = "tag";

    public final static String TAG_SUBMITTER = "submitter";

    public final static String TAG_SUBMIT_TIME = "submitTime";

    public final static String TAG_USER = "user";

    public final static String TAG_IMAGE = "image";

    public final static String COLUMN_UPDATE_TIME = "rva_update_time_";

    /** ID */
    private String id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 数据类型 */
    @Excel(name = "数据类型")
    private String type;

    /** 数据类型补充 */
    @Excel(name = "数据类型补充")
    private String typeDetail;

    /** 主键生成方式 */
    @Excel(name = "主键生成方式")
    private String idGenType;

    /** 必需 */
    @Excel(name = "必需")
    private String required;

    /** 字典类型 */
    @Excel(name = "字典类型")
    private String dictType;

    /** 字典单选 */
    @Excel(name = "字典单选")
    private String dictSelectSingle;

    /** 最大值 */
    @Excel(name = "最大值")
    private String valueMax;

    /** 最小值 */
    @Excel(name = "最小值")
    private String valueMin;

    /** 默认值 */
    @Excel(name = "默认值")
    private String defaultValue;

    /** 小数位数 */
    @Excel(name = "小数位数")
    private Integer numberScale;

    /** 索引 */
    @Excel(name = "索引")
    private Integer idx;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    /** 其他数据 */
    @Excel(name = "其他数据")
    private String data;

    /** 对象ID */
    @Excel(name = "对象ID")
    private String objId;

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
    public void setTypeDetail(String typeDetail)
    {
        this.typeDetail = typeDetail;
    }

    public String getTypeDetail()
    {
        return typeDetail;
    }
    public void setIdGenType(String idGenType)
    {
        this.idGenType = idGenType;
    }

    public String getIdGenType()
    {
        return idGenType;
    }
    public void setRequired(String required)
    {
        this.required = required;
    }

    public String getRequired()
    {
        return required;
    }
    public void setDictType(String dictType)
    {
        this.dictType = dictType;
    }

    public String getDictType()
    {
        return dictType;
    }
    public void setDictSelectSingle(String dictSelectSingle)
    {
        this.dictSelectSingle = dictSelectSingle;
    }

    public String getDictSelectSingle()
    {
        return dictSelectSingle;
    }
    public void setValueMax(String valueMax)
    {
        this.valueMax = valueMax;
    }

    public String getValueMax()
    {
        return valueMax;
    }
    public void setValueMin(String valueMin)
    {
        this.valueMin = valueMin;
    }

    public String getValueMin()
    {
        return valueMin;
    }
    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue()
    {
        return defaultValue;
    }

    public Integer getNumberScale() {
        return numberScale;
    }

    public void setNumberScale(Integer numberScale) {
        this.numberScale = numberScale;
    }

    public void setIdx(Integer idx)
    {
        this.idx = idx;
    }

    public Integer getIdx()
    {
        return idx;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
    public void setData(String data)
    {
        this.data = data;
    }

    public String getData()
    {
        return data;
    }
    public void setObjId(String objId)
    {
        this.objId = objId;
    }

    public String getObjId()
    {
        return objId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("type", getType())
            .append("typeDetail", getTypeDetail())
            .append("idGenType", getIdGenType())
            .append("required", getRequired())
            .append("dictType", getDictType())
            .append("dictSelectSingle", getDictSelectSingle())
            .append("valueMax", getValueMax())
            .append("valueMin", getValueMin())
            .append("defaultValue", getDefaultValue())
            .append("idx", getIdx())
            .append("description", getDescription())
            .append("data", getData())
            .append("objId", getObjId())
            .toString();
    }

    public static String getSqlFieldClause (RvaProperty prop, RvaObject object) {
        return getSqlFieldClause (prop, object.isPrimaryKey(prop.getName()));
    }

    public static String getSqlFieldClause (RvaProperty prop, boolean isPrimaryKey) {
        String metaType = prop.getType();
        String dbType = null;
        String fieldsClause = "`" + prop.getName() + "` ";
        String M_DEFAULT = prop.getDefaultValue();
        dbType = MySqlColumn.getTypes(1, metaType)[0];
        // column
        if (Arrays.asList(RvaProperty.TYPE_VARCHAR, RvaProperty.TYPE_NUMERIC).contains(metaType)) {
            fieldsClause += dbType + "(" + prop.getTypeDetail() + ")";
        } else {
            fieldsClause += dbType;
        }
        // null
        if ("Y".equals(prop.getRequired())) {
            fieldsClause += " NOT NULL ";
        }
        // default
        if (RvaUtils.isNotEmpty(M_DEFAULT)) {
            if (M_DEFAULT.startsWith("~")) {
                fieldsClause += String.format(" DEFAULT %s", M_DEFAULT.substring(1));
            } else {
                fieldsClause += String.format(" DEFAULT '%s'", M_DEFAULT);
            }
        }
        // 自增长
        if (isPrimaryKey && Arrays.asList(RvaProperty.TYPE_INTEGER, RvaProperty.TYPE_SMALLINT, RvaProperty.TYPE_BIGINT).contains(metaType)) {
            fieldsClause += String.format(" %s ", "N".equals(prop.getIdGenType())? "" : prop.getIdGenType());
        }
        if (RvaUtils.isNotEmpty(prop.getDescription())) {
            fieldsClause += " COMMENT '" + prop.getDescription() + "'";
        }
        return fieldsClause;
    }

    public static String getCreateSql(RvaProperty prop, RvaObject object) {
        return String.format("alter table `%s` add column %s", object.getId(), getSqlFieldClause(prop, object));
    }

    public static String getModifySql(RvaProperty prop, String oldName, RvaObject object) {
        if (RvaUtils.isEmpty(oldName)) {
            oldName = prop.getName();
        }
        return String.format("alter table `%s` change column `%s` %s", object.getId(), oldName, getSqlFieldClause(prop, object));
    }

    public static String getDropSql(RvaProperty prop, RvaObject object) {
        return String.format("alter table `%s` drop column `%s`", object.getId(), prop.getName());
    }

    public String getDescriptionName () {
        if (description == null || description.equals("")) {
            return null;
        }
        return description.split("[(（,，]")[0];
    }

    public String getColumn (String jsonPropertyName) {
        String col = this.getName();
        if (RvaUtils.isNotEmpty(jsonPropertyName)) {
            col = String.format("%s->>'$.%s'", col, jsonPropertyName);
        }
        return col;
    }

    public void setJsonProperty (String property, Object value) {
        this.data = RvaUtils.setJsonProperty(this.data, property, value);
    }

    public String getJsonPropertyString (String key) {
        return RvaUtils.parseMap(getData()).getString(key);
    }

    public void setTagSubmitter() {
        setJsonProperty(PARAM_TAG, TAG_SUBMITTER);
    }

    public void setTagSubmitTime() {
        setJsonProperty(PARAM_TAG, TAG_SUBMIT_TIME);
    }

    public void setTagUser() {
        setJsonProperty(PARAM_TAG, TAG_USER);
    }

    public void setTagImage() {
        setJsonProperty(PARAM_TAG, TAG_IMAGE);
    }

    public Boolean isTagSubmitter() {
        return TAG_SUBMITTER.equals(getJsonPropertyString(PARAM_TAG));
    }

    public Boolean isTagSubmitTime() {
        return TAG_SUBMIT_TIME.equals(getJsonPropertyString(PARAM_TAG));
    }

    public Boolean isTagUser() {
        return TAG_USER.equals(getJsonPropertyString(PARAM_TAG));
    }

    public Boolean isTagImage() {
        return TAG_IMAGE.equals(getJsonPropertyString(PARAM_TAG));
    }

    public void setDescriptionAndDict(String description)
    {
        String[] strings = description.split("[()（）]");
        this.description = description;
        if (strings.length > 1) {
            this.dictType = strings[1];
        }
    }

    public void setTypeDetailAndNumberScale(String typeDetail)
    {
        this.typeDetail = typeDetail;
        if (!RvaUtils.isEmpty(typeDetail)) {
            String [] vals = typeDetail.split(",");
            if (vals.length > 1) {
                setNumberScale(Integer.parseInt(vals[1]));
                setValueMax(String.valueOf(Integer.parseInt(vals[0]) - getNumberScale()));
            } else {
                setNumberScale(0);
                setValueMax(String.valueOf(Integer.parseInt(vals[0])));
            }
        }
    }

    public static Boolean match(RvaProperty property, List<String> descs, String type, Boolean like) {
        if (type != null && !property.getType().equals(type)) {
            return false;
        }
        String propertyDescriptionName = property.getDescriptionName();
        if (propertyDescriptionName != null) {
            propertyDescriptionName = propertyDescriptionName.toLowerCase();
        }
        for (int i = 0; i < descs.size(); i ++) {
            String desc = descs.get(i);
            if (like) {
                desc = ".*" + desc + ".*";
            }
            if (RvaUtils.isMatched(desc, propertyDescriptionName) || RvaUtils.isMatched(desc, property.getName())) {
                return true;
            }
        }
        return false;
    }

    public Boolean match(List<String> descs, String type) {
        return match(this, descs, type, false);
    }

    public Boolean match(List<String> descs) {
        return match(descs, null);
    }

    public Boolean matchLike(List<String> descs, String type) {
        return match(this, descs, type, true);
    }

    public Boolean matchLike(List<String> descs) {
        return matchLike(descs, null);
    }

    public Boolean isAutoIncrement() {
        return AUTO_INCREMENT.equals(this.getIdGenType());
    }

    public String getSqlValue(Object val) {
        if (val == null) {
            return "null";
        }
        if (val instanceof Date) {
            return "'" + RvaDateUtils.getDatetime(val) + "'";
        }
        val = val.toString().replace("'", "\\'").replace("\"", "\\\"");
        return String.format("'%s'", val);
    }

    public static void main(String[] args) {
        System.out.println("''".replace("'", "\\'"));
    }
}
