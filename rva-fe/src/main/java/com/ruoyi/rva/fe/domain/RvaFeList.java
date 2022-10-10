package com.ruoyi.rva.fe.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.rva.fe.mapper.RvaFeListItemMapper;
import com.ruoyi.rva.framework.util.RvaUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 列对象 rva_fe_list
 *
 * @author jiaodacailei
 * @date 2021-12-01
 */
public class RvaFeList extends BaseEntity {
    private static final long serialVersionUID = 1L;

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
     * 排版模式（1-大图 2-左图 3-右图 4-无图 5-聊天群 6-聊天）
     */
    @Excel(name = "排版模式", readConverterExp = "1=-大图,2=-左图,3=-右图,4=-无图,5=-聊天群,6=-聊天")
    private Integer mode;

    /**
     * 强制模式，为Y时，忽略列表项的mode，采用列表的mode
     */
    @Excel(name = "强制模式，为Y时，忽略列表项的mode，采用列表的mode")
    private String forceMode;

    /**
     * 主对象ID，此处为表名
     */
    @Excel(name = "主对象ID，此处为表名")
    private String objId;

    /**
     * 收藏等对象ID，JSON对象格式，key为rva_objectfavorite.type的值，value为表名，用于存放收藏、点赞等数据
     */
    @Excel(name = "收藏等对象ID，JSON对象格式，key为rva_objectfavorite.type的值，value为表名，用于存放收藏、点赞等数据")
    private String objFavoritesIds;

    /**
     * 评论对象ID，用于存放评论数据，此处为表名
     */
    @Excel(name = "评论对象ID，用于存放评论数据，此处为表名")
    private String objCommentsId;

    /**
     * 查询条件，velocity模板，解析后为sql脚本where语句，作为列表查询条件的一部分
     */
    @Excel(name = "查询条件，velocity模板，解析后为sql脚本where语句，作为列表查询条件的一部分")
    private String loadWhere;

    /**
     * 初始行数，列表初始显示行数，0表示无限制，不分页
     */
    @Excel(name = "初始行数，列表初始显示行数，0表示无限制，不分页")
    private Integer rowsInit;

    /**
     * 更新行数，每次上划下拉更新的行数
     */
    @Excel(name = "更新行数，每次上划下拉更新的行数")
    private Integer rowsUpdate;

    /**
     * 显示标题，针对列表项
     */
    @Excel(name = "显示标题，针对列表项")
    private String showTitle;

    /**
     * 显示摘要，针对列表项
     */
    @Excel(name = "显示摘要，针对列表项")
    private String showExcerpt;

    /**
     * 显示图片数，不大于0表示不显示
     */
    @Excel(name = "显示图片数，不大于0表示不显示")
    private Integer showAvatarCount;

    /**
     * 显示额外数量，针对列表项，0表示不显示
     */
    @Excel(name = "显示额外数量，针对列表项，0表示不显示")
    private Integer showExtrasCount;

    /**
     * 显示按钮数量，针对列表项，0表示不显示
     */
    @Excel(name = "显示按钮数量，针对列表项，0表示不显示")
    private Integer showButtonCount;

    /**
     * 排版模式属性名，指定obj_id对应表的某个字段，用于存储排版模式
     */
    @Excel(name = "排版模式属性名，指定obj_id对应表的某个字段，用于存储排版模式")
    private String propNameMode;

    /**
     * 禁用属性名，指定obj_id对应表的某个字段，用于禁用信息
     */
    @Excel(name = "禁用属性名，指定obj_id对应表的某个字段，用于禁用信息")
    private String propNameDisabled;

    /**
     * 分类属性名，指定obj_id对应表的某个字段，关联obj_categories_id主键，用于存储分类数据
     */
    @Excel(name = "分类属性名，指定obj_id对应表的某个字段，关联obj_categories_id主键，用于存储分类数据")
    private String propNameCategory;

    /**
     * 分组属性名，指定obj_id对应表的某个字段，用于存储分组信息，实现动态分组
     */
    @Excel(name = "分组属性名，指定obj_id对应表的某个字段，用于存储分组信息，实现动态分组")
    private String propNameGroup;

    /**
     * 分类字典，prop_name_category的值对应的数据字典
     */
    @Excel(name = "分类字典，prop_name_category的值对应的数据字典")
    private String dictCategory;

    /**
     * 分组字典，prop_name_group的值或者rva_fe_list_item.group_value对应的数据字典
     */
    @Excel(name = "分组字典，prop_name_group的值或者rva_fe_list_item.group_value对应的数据字典")
    private String dictGroup;

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

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Integer getMode() {
        return mode;
    }

    public void setForceMode(String forceMode) {
        this.forceMode = forceMode;
    }

    public String getForceMode() {
        return forceMode;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjFavoritesIds(String objFavoritesIds) {
        this.objFavoritesIds = objFavoritesIds;
    }

    public String getObjFavoritesIds() {
        return objFavoritesIds;
    }

    public void setObjCommentsId(String objCommentsId) {
        this.objCommentsId = objCommentsId;
    }

    public String getObjCommentsId() {
        return objCommentsId;
    }

    public void setLoadWhere(String loadWhere) {
        this.loadWhere = loadWhere;
    }

    public String getLoadWhere() {
        return loadWhere;
    }

    public void setRowsInit(Integer rowsInit) {
        this.rowsInit = rowsInit;
    }

    public Integer getRowsInit() {
        return rowsInit;
    }

    public void setRowsUpdate(Integer rowsUpdate) {
        this.rowsUpdate = rowsUpdate;
    }

    public Integer getRowsUpdate() {
        return rowsUpdate;
    }

    public void setShowTitle(String showTitle) {
        this.showTitle = showTitle;
    }

    public String getShowTitle() {
        return showTitle;
    }

    public void setShowExcerpt(String showExcerpt) {
        this.showExcerpt = showExcerpt;
    }

    public String getShowExcerpt() {
        return showExcerpt;
    }

    public void setShowAvatarCount(Integer showAvatarCount) {
        this.showAvatarCount = showAvatarCount;
    }

    public Integer getShowAvatarCount() {
        return showAvatarCount;
    }

    public void setShowExtrasCount(Integer showExtrasCount) {
        this.showExtrasCount = showExtrasCount;
    }

    public Integer getShowExtrasCount() {
        return showExtrasCount;
    }

    public void setShowButtonCount(Integer showButtonCount) {
        this.showButtonCount = showButtonCount;
    }

    public Integer getShowButtonCount() {
        return showButtonCount;
    }

    public void setPropNameMode(String propNameMode) {
        this.propNameMode = propNameMode;
    }

    public String getPropNameMode() {
        return propNameMode;
    }

    public void setPropNameDisabled(String propNameDisabled) {
        this.propNameDisabled = propNameDisabled;
    }

    public String getPropNameDisabled() {
        return propNameDisabled;
    }

    public void setPropNameCategory(String propNameCategory) {
        this.propNameCategory = propNameCategory;
    }

    public String getPropNameCategory() {
        return propNameCategory;
    }

    public void setPropNameGroup(String propNameGroup) {
        this.propNameGroup = propNameGroup;
    }

    public String getPropNameGroup() {
        return propNameGroup;
    }

    public void setDictCategory(String dictCategory) {
        this.dictCategory = dictCategory;
    }

    public String getDictCategory() {
        return dictCategory;
    }

    public void setDictGroup(String dictGroup) {
        this.dictGroup = dictGroup;
    }

    public String getDictGroup() {
        return dictGroup;
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
                .append("mode", getMode())
                .append("forceMode", getForceMode())
                .append("objId", getObjId())
                .append("objFavoritesIds", getObjFavoritesIds())
                .append("objCommentsId", getObjCommentsId())
                .append("loadWhere", getLoadWhere())
                .append("rowsInit", getRowsInit())
                .append("rowsUpdate", getRowsUpdate())
                .append("showTitle", getShowTitle())
                .append("showExcerpt", getShowExcerpt())
                .append("showAvatarCount", getShowAvatarCount())
                .append("showExtrasCount", getShowExtrasCount())
                .append("showButtonCount", getShowButtonCount())
                .append("propNameMode", getPropNameMode())
                .append("propNameDisabled", getPropNameDisabled())
                .append("propNameCategory", getPropNameCategory())
                .append("propNameGroup", getPropNameGroup())
                .append("dictCategory", getDictCategory())
                .append("dictGroup", getDictGroup())
                .append("data", getData())
                .toString();
    }

    public final static String PREFIX = "list";

    public final static String PREFIX_SQL = "@@SQL@@";

    private List<RvaFeListItem> items = null;

    public RvaFeList loadData() {
        if (items == null) {
            RvaFeListItem search = new RvaFeListItem();
            search.setListId(this.getId());
            items = SpringUtils.getBean(RvaFeListItemMapper.class).selectRvaFeListItemList(search);
            Collections.sort(items);
        }
        return this;
    }

    public List<RvaFeListItem> getItems() {
        loadData();
        return this.items;
    }

    public void setItems(List<RvaFeListItem> items) {
        this.items = items;
    }

    public RvaFeListItem getItem(String itemId) {
        return getItems().stream().filter(item -> item.getId().equals(itemId)).findFirst().get();
    }

    public Boolean isCascadeDelete() {
        return "Y".equals(RvaUtils.parseMap(this.data).getString("cascadeDelete"));
    }

    public RvaFeListItem getItemByObjectProperty(String propId) {
        for (RvaFeListItem item : getItems()) {
            if (propId.equals(item.getPropId())) {
                return item;
            }
        }
        return null;
    }

    public void setJsonProperty(String property, Object value) {
        this.data = RvaUtils.setJsonProperty(this.data, property, value);
    }

    public void setCascadeDelete(Boolean cascadeDelete) {
        setJsonProperty("cascadeDelete", cascadeDelete ? "Y" : "N");
    }

    public void setCascaded(Boolean cascaded) {
        setJsonProperty("cascaded", cascaded ? "Y" : "N");
    }

    public String getLoadSql() {
        return RvaUtils.parseMap(this.data).getString("loadSql");
    }

    public void setLoadSql(String loadSql) {
        setJsonProperty("loadSql", loadSql);
    }

    public String getSearchExpression() {
        return RvaUtils.parseMap(this.data).getString("searchExpression");
    }

    public void setSearchExpression(String searchExpression) {
        setJsonProperty("searchExpression", searchExpression);
    }

    public String getLoadBeforeSql() {
        return RvaUtils.parseMap(this.data).getString("loadBeforeSql");
    }

    public void setLoadBeforeSql(String loadSql) {
        setJsonProperty("loadBeforeSql", loadSql);
    }

    public String getLoadAfterSql() {
        return RvaUtils.parseMap(this.data).getString("loadAfterSql");
    }

    public void setLoadAfterSql(String loadSql) {
        setJsonProperty("loadAfterSql", loadSql);
    }

    private List<SysDictData> categories = new ArrayList<>();

    public List<SysDictData> getCategories() {
        return categories;
    }

    public void setCategories(List<SysDictData> categories) {
        this.categories = categories;
    }
}
