package com.ruoyi.rva.framework.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.rva.framework.util.RvaUtils;
import com.ruoyi.rva.framework.mapper.RvaPropertyMapper;
import com.ruoyi.rva.framework.mapper.RvaRelationMapper;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 对象对象 rva_object
 *
 * @author jiaodacailei
 * @date 2021-12-01
 */
public class RvaObject extends RvaBaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private String id;

    /** 编号 */
    @Excel(name = "编号")
    private String no;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 索引 */
    @Excel(name = "索引")
    private Long idx;

    /** 模块前缀 */
    @Excel(name = "模块前缀")
    private String module;

    /** 状态（0-关系表 1-普通表） */
    @Excel(name = "状态", readConverterExp = "0=-关系表,1=-普通表")
    private String status;

    /** 说明 */
    @Excel(name = "说明")
    private String description;

    /** 名称属性名 */
    @Excel(name = "名称属性名")
    private String propNameName;

    /** 逻辑删除属性名 */
    @Excel(name = "逻辑删除属性名")
    private String propNameDel;

    /** 层级编号属性名 */
    @Excel(name = "层级编号属性名")
    private String propNameNo;

    /** 主键属性名 */
    @Excel(name = "主键属性名")
    private String propNameKey;

    /** 创建人属性名 */
    @Excel(name = "创建人属性名")
    private String propNameCreateBy;

    /** 创建时间属性名 */
    @Excel(name = "创建时间属性名")
    private String propNameCreateTime;

    /** 修改人属性名 */
    @Excel(name = "修改人属性名")
    private String propNameUpdateBy;

    /** 修改时间属性名 */
    @Excel(name = "修改时间属性名")
    private String propNameUpdateTime;

    /** 唯一键 */
    @Excel(name = "唯一键")
    private String uniques;

    /** 属性索引最大值 */
    @Excel(name = "属性索引最大值")
    private Integer propIndexMax;

    /** 关系索引最大值 */
    @Excel(name = "关系索引最大值")
    private Integer relationIndexMax;

    /** 其他数据 */
    @Excel(name = "其他数据")
    private String data;

    /** 收藏表 */
    @Excel(name = "收藏表")
    private String objFavoriteId;

    /** 评论表 */
    @Excel(name = "评论表")
    private String objCommentId;

    /** 评论点赞表 */
    @Excel(name = "评论点赞表")
    private String objCommentLikeId;

    /** 收藏数属性名 */
    @Excel(name = "收藏数属性名")
    private String propNameFavorites;

    /** 点赞数属性名 */
    @Excel(name = "点赞数属性名")
    private String propNameLikes;

    /** 预约数属性名 */
    @Excel(name = "预约数属性名")
    private String propNameSubscribes;

    /** 关注数属性名 */
    @Excel(name = "关注数属性名")
    private String propNameFollows;

    /** 已读数属性名 */
    @Excel(name = "已读数属性名")
    private String propNameReads;

    /** 已读人数属性名 */
    @Excel(name = "已读人数属性名")
    private String propNameReaders;

    /** 置顶属性名 */
    @Excel(name = "置顶属性名")
    private String propNameSticky;

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setNo(String no)
    {
        this.no = no;
    }

    public String getNo()
    {
        return no;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setIdx(Long idx)
    {
        this.idx = idx;
    }

    public Long getIdx()
    {
        return idx;
    }
    public void setModule(String module)
    {
        this.module = module;
    }

    public String getModule()
    {
        return module;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
    public void setPropNameName(String propNameName)
    {
        this.propNameName = propNameName;
    }

    public String getPropNameName()
    {
        return propNameName;
    }
    public void setPropNameDel(String propNameDel)
    {
        this.propNameDel = propNameDel;
    }

    public String getPropNameDel()
    {
        return propNameDel;
    }
    public void setPropNameNo(String propNameNo)
    {
        this.propNameNo = propNameNo;
    }

    public String getPropNameNo()
    {
        return propNameNo;
    }
    public void setPropNameKey(String propNameKey)
    {
        this.propNameKey = propNameKey;
    }

    public String getPropNameKey()
    {
        return propNameKey;
    }
    public void setPropNameCreateBy(String propNameCreateBy)
    {
        this.propNameCreateBy = propNameCreateBy;
    }

    public String getPropNameCreateBy()
    {
        return propNameCreateBy;
    }
    public void setPropNameCreateTime(String propNameCreateTime)
    {
        this.propNameCreateTime = propNameCreateTime;
    }

    public String getPropNameCreateTime()
    {
        return propNameCreateTime;
    }
    public void setPropNameUpdateBy(String propNameUpdateBy)
    {
        this.propNameUpdateBy = propNameUpdateBy;
    }

    public String getPropNameUpdateBy()
    {
        return propNameUpdateBy;
    }
    public void setPropNameUpdateTime(String propNameUpdateTime)
    {
        this.propNameUpdateTime = propNameUpdateTime;
    }

    public String getPropNameUpdateTime()
    {
        return propNameUpdateTime;
    }
    public void setUniques(String uniques)
    {
        this.uniques = uniques;
    }

    public String getUniques()
    {
        return uniques;
    }
    public void setPropIndexMax(Integer propIndexMax)
    {
        this.propIndexMax = propIndexMax;
    }

    public Integer getPropIndexMax()
    {
        return propIndexMax;
    }
    public void setRelationIndexMax(Integer relationIndexMax)
    {
        this.relationIndexMax = relationIndexMax;
    }

    public Integer getRelationIndexMax()
    {
        return relationIndexMax;
    }
    public void setData(String data)
    {
        this.data = data;
    }

    public String getData()
    {
        return data;
    }
    public void setObjFavoriteId(String objFavoriteId)
    {
        this.objFavoriteId = objFavoriteId;
    }

    public String getObjFavoriteId()
    {
        return objFavoriteId;
    }
    public void setObjCommentId(String objCommentId)
    {
        this.objCommentId = objCommentId;
    }

    public String getObjCommentId()
    {
        return objCommentId;
    }
    public void setObjCommentLikeId(String objCommentLikeId)
    {
        this.objCommentLikeId = objCommentLikeId;
    }

    public String getObjCommentLikeId()
    {
        return objCommentLikeId;
    }
    public void setPropNameFavorites(String propNameFavorites)
    {
        this.propNameFavorites = propNameFavorites;
    }

    public String getPropNameFavorites()
    {
        return propNameFavorites;
    }
    public void setPropNameLikes(String propNameLikes)
    {
        this.propNameLikes = propNameLikes;
    }

    public String getPropNameLikes()
    {
        return propNameLikes;
    }
    public void setPropNameSubscribes(String propNameSubscribes)
    {
        this.propNameSubscribes = propNameSubscribes;
    }

    public String getPropNameSubscribes()
    {
        return propNameSubscribes;
    }
    public void setPropNameFollows(String propNameFollows)
    {
        this.propNameFollows = propNameFollows;
    }

    public String getPropNameFollows()
    {
        return propNameFollows;
    }
    public void setPropNameReads(String propNameReads)
    {
        this.propNameReads = propNameReads;
    }

    public String getPropNameReads()
    {
        return propNameReads;
    }
    public void setPropNameReaders(String propNameReaders)
    {
        this.propNameReaders = propNameReaders;
    }

    public String getPropNameReaders()
    {
        return propNameReaders;
    }
    public void setPropNameSticky(String propNameSticky)
    {
        this.propNameSticky = propNameSticky;
    }

    public String getPropNameSticky()
    {
        return propNameSticky;
    }


    public void addPropNameKey (String columnName) {
        if (RvaUtils.isEmpty(getPropNameKey())) {
            setPropNameKey(columnName);
        } else {
            setPropNameKey(getPropNameKey() + "," + columnName);
        }
    }

    private List<RvaProperty> properties = null;

    private List<RvaRelation> relations = null;

    public void loadData () {
        if (properties == null) {
            RvaProperty search = new RvaProperty();
            search.setObjId(this.getId());
            properties = SpringUtils.getBean(RvaPropertyMapper.class).selectRvaPropertyList(search);
            Collections.sort(properties, new Comparator<RvaProperty>() {
                @Override
                public int compare(RvaProperty o1, RvaProperty o2) {
                    return o1.getIdx() - o2.getIdx();
                }
            });
        }
        if (relations == null) {
            RvaRelation search = new RvaRelation();
            search.setObjId(getId());
            relations = SpringUtils.getBean(RvaRelationMapper.class).selectRvaRelationList(search);
            relations.forEach(rvaRelation -> {
                rvaRelation.loadData();
            });
            Collections.sort(relations, new Comparator<RvaRelation>() {
                @Override
                public int compare(RvaRelation o1, RvaRelation o2) {
                    return o1.getIdx() - o2.getIdx();
                }
            });
        }
    }

    public void setProperties(List<RvaProperty> properties) {
        this.properties = properties;
    }

    public List<RvaProperty> getProperties() {
        loadData();
        return properties;
    }

    public List<RvaRelation> getRelations() {
        loadData();
        return relations;
    }

    public RvaRelation getRelation(String relationId) {
        for (RvaRelation relation : getRelations()) {
            if (relation.getId().equals(relationId)) {
                return relation;
            }
        }
        return null;
    }

    public Boolean isPrimaryKey (String propName) {
        String[] split = this.getPropNameKey().split(",");
        return Arrays.asList(split).contains(propName);
    }

    public RvaProperty getPropertyByName (String propName) {
        for (RvaProperty property : getProperties()) {
            if (property.getName().equals(propName)) {
                return property;
            }
        }
        return null;
    }

    public RvaProperty getNameProperty () {
        return getPropertyByName(getPropNameName());
    }

    public Boolean isCreateBy (String propName) {
        return propName.equals(getPropNameCreateBy());
    }

    public Boolean isCreateTime (String propName) {
        return propName.equals(getPropNameCreateTime());
    }

    public Boolean isUpdateBy (String propName) {
        return propName.equals(getPropNameUpdateBy());
    }

    public Boolean isUpdateTime (String propName) {
        return propName.equals(getPropNameUpdateTime());
    }

    public Boolean isNameProp(String propName) {
        return propName.equals(getPropNameName());
    }

    public Boolean isParentProp (String propName) {
        return propName.equals(getPropNameParent());
    }

    public void initPropNames () {
        if (RvaUtils.isEmpty(getPropNameKey())) {
            setPropNameKey(this.getProperties().get(0).getName());
        }
        if (RvaUtils.isEmpty(getPropNameName())) {
            // Arrays.asList("名称", "姓名", "标题", "name", "title").contains(property.getDescription())
            for (RvaProperty property : getProperties()) {
                if (Arrays.asList("名称", "姓名", "标题", "name", "title").contains(property.getDescriptionName())) {
                    setPropNameName(property.getName());
                }
            }
        }
    }

    public RvaProperty getKeyProperty () {
        for (RvaProperty property : getProperties()) {
            if (isPrimaryKey(property.getName())) {
                return property;
            }
        }
        return null;
    }

    public RvaProperty getProperty (String propId) {
        for (RvaProperty property : getProperties()) {
            if (property.getId().equals(propId)) {
                return property;
            }
        }
        return null;
    }

    public static String getCreateTableSql(RvaObject object) {
        List<RvaProperty> props = object.getProperties();
        String sql = "";
        String ids = "";
        for (RvaProperty prop : props) {
            if (RvaProperty.COLUMN_UPDATE_TIME.equalsIgnoreCase(prop.getName())) {
                continue;
            }
            sql += "    " + RvaProperty.getSqlFieldClause(prop, object) + ",\n";
            if (object.isPrimaryKey(prop.getName())) {
                if (ids != "") {
                    ids += ",";
                }
                ids += prop.getName();
            }
        }
        sql += "    " + RvaProperty.COLUMN_UPDATE_TIME + " timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳'";
        if (ids != "") {
            sql += ",primary key (" + ids + ")";
        }
        sql = "create table " + object.getId() + " (\n" + sql + "\n) comment '" + object.getName() + "'";
        return sql;
    }

    public static String getDropTableSql (String table) {
        return String.format("drop table if exists %s", table);
    }

    public String getPropNameIndex () {
        return getJsonPropertyString("propNameIndex");
    }

    public void setPropNameIndex (String propName) {
        setJsonProperty("propNameIndex", propName);
    }

    public Boolean hasNotPropIndex () {
        return RvaUtils.isEmpty(getPropNameIndex ());
    }

    public Boolean hasPropIndex () {
        return RvaUtils.isNotEmpty(getPropNameIndex ());
    }

    public List<String> getPropNameIndexWhere () {
        return getJsonPropertyStringList("propNameIndexWhere");
    }

    public String getRequestValue (String propName, RvaMap req) {
        RvaProperty property = getPropertyByName(propName);
        return req.getStringBySuffix(property.getId());
    }

    public String getRequestKeyValue (RvaMap req) {
        return getRequestValue(getPropNameKey(), req);
    }

    public String getRequestIndexValue (RvaMap req) {
        return getRequestValue(getPropNameIndex(), req);
    }

    public RvaMap<String, Object> getRequestIndexData (RvaMap req) {
        RvaMap<String, Object> data = new RvaMap<>();
        getPropNameIndexWhere ().forEach(iw -> {
            data.put(iw, getRequestValue(iw, req));
        });
        data.put(getPropNameKey(), getRequestKeyValue(req));
        data.put(getPropNameIndex(), getRequestIndexValue(req));
        return data;
    }

    public String getPropNameDept () {
        return getJsonPropertyString("propNameDept");
    }

    public void setPropNameDept(String dept) {
        this.setJsonProperty("propNameDept", dept);
    }

    public String getPropNameJson () {
        return getJsonPropertyString("propNameJson");
    }

    public void setPropNameJson(String dept) {
        this.setJsonProperty("propNameJson", dept);
    }

    public String getPropNameParent () {
        return getJsonPropertyString("propNameParent");
    }

    public void setPropNameParent(String parent) {
        this.setJsonProperty("propNameParent", parent);
    }

    public Boolean hasPropParent () {
        return RvaUtils.isNotEmpty(getPropNameParent ());
    }

    public String getPropNameTenant () {
        return getJsonPropertyString("propNameTenant");
    }

    public void setPropNameTenant(String tenant) {
        this.setJsonProperty("propNameTenant", tenant);
    }

    public Boolean hasPropTenant () {
        return RvaUtils.isNotEmpty(getPropNameTenant ());
    }

    public static final String NONE = "none";

    public String getKeyValue (RvaMap<String, Object> fieldValues) {
        String val = fieldValues.getString(getKeyProperty().getName());
        if (RvaUtils.isNotEmpty(val)) {
            return val;
        }
        return fieldValues.getString("id");
    }
    
    public Boolean hasPropDel () {
        return RvaUtils.isNotEmpty(getPropNameDel ());
    }
}
