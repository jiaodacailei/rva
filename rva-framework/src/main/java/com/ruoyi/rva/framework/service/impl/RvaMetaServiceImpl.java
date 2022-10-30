package com.ruoyi.rva.framework.service.impl;

import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.entity.SysDictType;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.rva.framework.domain.*;
import com.ruoyi.rva.framework.extension.RvaObjectDeleteExtension;
import com.ruoyi.rva.framework.mapper.*;
import com.ruoyi.rva.framework.service.*;
import com.ruoyi.rva.framework.util.*;
import com.ruoyi.system.mapper.SysMenuMapper;
import com.ruoyi.system.service.ISysDictDataService;
import com.ruoyi.system.service.ISysDictTypeService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RVA元数据管理
 *
 * @author jiaodacailei
 * @date 2021-07-27
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaMetaServiceImpl implements IRvaMetaService {

    private final RvaObjectMapper objectMapper;

    private final RvaPropertyMapper propertyMapper;

    private final RvaRelationMapper relationMapper;

    private final RvaRelationitemMapper relationitemMapper;

    private final RvaAppMapper appMapper;

    private final RvaAppitemMapper appitemMapper;

    private final RvaViewMapper viewMapper;

    private final RvaViewpropertyMapper viewpropertyMapper;

    private final RvaViewbuttonMapper viewbuttonMapper;

    private final RvaSchemaMapper schemaMapper;

    private final RvaDataMapper dataMapper;

    private final ISysDictTypeService dictTypeService;

    private final ISysDictDataService dictDataService;

    private final IRvaViewbuttonService viewbuttonService;

    private final IRvaViewService viewService;

    private final RvaKpiMapper kpiMapper;

    private final RvaChartMapper chartMapper;

    private final IRvaModuleService moduleService;

    @Override
    public RvaObject createObject(String table) {
        // 刪除对象、应用、视图的元数据
        RvaObject rvaObject = objectMapper.selectRvaObjectById(table);
        if (rvaObject != null) {
            RvaUtils.throwExistsException("对象");
        }
        // 创建对象的元数据
        return createObjectMeta(table);
    }

    @Override
    public void updateObject(String objId, RvaMap<String, Object> req) {
        String keyValue = req.getString(RvaConstants.PROP_KEY_VALUE);
        RvaObject object = objectMapper.selectRvaObjectById(objId);
        dataMapper.updateWhereMap(objId, req.getMap("fieldValues"), new RvaMap<>(object.getPropNameKey(), keyValue), false);
    }

    private RvaObject createObjectMeta(String table) {
        // 插入元数据
        RvaObject obj = new RvaObject();
        obj.setId(table);
        int idx = table.indexOf('_');
        obj.setNo(table.substring(idx + 1));
        String tableComment = schemaMapper.queryTableComment(table);
        obj.setName(RvaUtils.getString(tableComment, table));
        Long aLong = dataMapper.selectLong("select max(idx) from rva_object");
        obj.setIdx(aLong == null ? 0L : aLong + 1);
        obj.setStatus("0");
        String moduleName = idx > 0 ? table.substring(0, idx) : moduleService.selectDefaultModuleName();
        obj.setModule(moduleName);
        // 插入列元数据
        List<MySqlColumn> cols = schemaMapper.queryColumns(table);
        for (int i = 0; i < cols.size(); i++) {
            MySqlColumn column = cols.get(i);
            if (RvaProperty.COLUMN_UPDATE_TIME.equals(column.getField())) {
                continue;
            }
            if (column.isPrimaryKey()) {
                obj.addPropNameKey(column.getField());
                obj.setStatus("1");
            }
            RvaProperty property = createProperty(table, column, i);
            String propertyName = property.getName();
            // 设置名称字段
            if (property.match(Arrays.asList("名称", "姓名", "标题", "name", "title"))) {
                obj.setPropNameName(propertyName);
            }
            // 设置创建人字段
            if (property.match(Arrays.asList("创建人", "创建者", "create_by"))) {
                obj.setPropNameCreateBy(propertyName);
            }
            // 设置修改人字段
            if (property.match(Arrays.asList("修改人", "修改者", "更新者", "更新人", "update_by"))) {
                obj.setPropNameUpdateBy(propertyName);
            }
            // 设置创建时间字段
            if (property.match(Arrays.asList("创建时间", "create_time"), RvaProperty.TYPE_DATETIME)) {
                obj.setPropNameCreateTime(propertyName);
            }
            // 设置修改时间字段
            if (property.match(Arrays.asList("修改时间", "更新时间", "update_time"), RvaProperty.TYPE_DATETIME)) {
                obj.setPropNameUpdateTime(propertyName);
            }
            // 设置部门字段
            if (property.match(Arrays.asList("部门", "dept_id"))) {
                obj.setPropNameDept(propertyName);
            }
            // 设置JSON字段
            if (property.match(Arrays.asList("其他数据", "data"))) {
                obj.setPropNameJson(propertyName);
            }
            // 图片
            if (property.matchLike(Arrays.asList("图片", "image"))) {
                property.setTagImage();
                propertyMapper.updateRvaProperty(property);
            }
            // 设置排序字段
            if (property.match(Arrays.asList("排序", "idx", "sort"))) {
                obj.setPropNameIndex(propertyName);
            }
            // 设置删除标志字段
            if (property.match(Arrays.asList("删除标记", "删除标志", "del", "del_flag"))) {
                obj.setPropNameDel(propertyName);
            }
        }
        if (RvaUtils.isEmpty(obj.getPropNameName())) {
            obj.setPropNameName(cols.get(1).getField());
        }
        obj.setPropIndexMax(cols.size());
        // 插入表元数据
        objectMapper.insertRvaObject(obj);
        return obj;
    }

    @Override
    public SysDictType createDictData(String table, String typeCnName, String defaultValue, String[] data, Boolean containValue) {
        String type = table.toLowerCase() + "_" + RvaPinyinUtils.getPinyinLower(typeCnName);
        RvaMap<String, Object> dict_type = new RvaMap<>("dict_type", type);
        dataMapper.deleteWhereMap("sys_dict_type", dict_type);
        dataMapper.deleteWhereMap("sys_dict_data", dict_type);
        SysDictType dictType = new SysDictType();
        dictType.setDictType(type);
        dictType.setDictName(typeCnName);
        dictType.setStatus("0");
        dictType.setCreateBy("admin");
        dictType.setCreateTime(new Date());
        dictTypeService.insertDictType(dictType);
        for (int i = 0; i < data.length; i += (containValue ? 2 : 1)) {
            String value = containValue ? data[i] : String.valueOf(RvaUtils.generateFixedLenNo(2, i + 1));
            String label = containValue ? data[i + 1] : data[i];
            SysDictData dictData = new SysDictData();
            dictData.setDictType(type);
            dictData.setDictLabel(label);
            dictData.setDictSort(Long.valueOf(i));
            dictData.setDictValue(value);
            dictData.setIsDefault("N");
            if (defaultValue != null && defaultValue.contains(value)) {
                dictData.setIsDefault("Y");
            }
            dictData.setCreateBy("admin");
            dictData.setCreateTime(new Date());
            dictDataService.insertDictData(dictData);
        }
        return dictType;
    }

    private RvaProperty createProperty(String table, MySqlColumn col, Integer index) {
        RvaProperty property = new RvaProperty();
        property.setId(createPropertyId(table, col.getField()));
        property.setName(col.getField());
        property.setDescriptionAndDict(col.getDescription());
        property.setType(col.getLogicType());
        property.setTypeDetailAndNumberScale(col.getTypeDetail());
        property.setRequired(col.isNull() ? "N" : "Y");
        property.setDefaultValue(col.getDefaultValue());
        property.setIdx(index + 1);
        property.setIdGenType(RvaUtils.isEmpty(col.getExtra()) ? "N" : col.getExtra().toUpperCase());
        property.setObjId(table);
        if (!RvaUtils.isEmpty(property.getDictType())) {
            String[] strings = parseDictData(property.getDictType()).toArray(new String[0]);
            SysDictType dictData = createDictData(table, property.getDescriptionName(), property.getDefaultValue(), strings, true);
            property.setDictType(dictData.getDictType());
        }
        propertyMapper.insertRvaProperty(property);
        return property;
    }

    @Override
    public void deleteAllObjectMeta(String table) {
        RvaExtensionUtils.getExtensions(RvaObjectDeleteExtension.class, RvaObjectDeleteExtension.BEAN_FREFIX).forEach(e -> e.preDelete(table));
        // 刪除元数据
        dataMapper.delete("delete from rva_triggeraction where trigger_id in (select id from rva_trigger where view_id in (select id from rva_view where obj_id = '" + table + "'))");
        dataMapper.delete("delete from rva_trigger where view_id in (select id from rva_view where obj_id = '" + table + "')");
        dataMapper.delete("delete from rva_viewbutton where view_id in (select id from rva_view where obj_id = '" + table + "')");
        dataMapper.delete("delete from rva_viewproperty where view_id in (select id from rva_view where obj_id = '" + table + "')");
        dataMapper.deleteWhereMap("rva_view", new RvaMap<>("obj_id", table));
        dataMapper.delete("delete from rva_appitem where app_id in (select id from rva_app where obj_id = '" + table + "')");
        dataMapper.deleteWhereMap("rva_app", new RvaMap<>("obj_id", table));
        deleteObjectMeta(table);
    }

    public void deleteObjectMeta(String table) {
        // 刪除元数据
        dataMapper.deleteWhereMap("rva_property", new RvaMap<>("obj_id", table));
        dataMapper.delete("delete from rva_relationitem where relation_id in (select id from rva_relation where obj_id = '" + table + "')");
        dataMapper.deleteWhereMap("rva_relation", new RvaMap<>("obj_id", table));
        dataMapper.deleteWhereMap("rva_object", new RvaMap<>("id", table));
    }

    @Override
    public void refreshObject(String table) {
        deleteObjectMeta(table);
        createObjectMeta(table);
        createCrud(table);
    }

    private final static String DEFAULT_TAG = "x";

    private String createDefaultCrudId(String objId) {
        return createCrudId(DEFAULT_TAG, objId);
    }

    private String createAppId(String appType, String tag, String objId) {
        return String.format("%s%s_%s", appType, tag, objId);
    }

    private String createCrudId(String tag, String objId) {
        return String.format("%s%s_%s", RvaApp.TYPE_CRUD, tag, objId);
    }

    private String createViewId(String prefix, String tag, String objId) {
        return String.format("%s%s_%s", prefix, tag, objId);
    }

    private String createFormViewId(String tag, String objId, boolean isCreate) {
        String prefix = RvaView.FORM_UPDATE.substring(0, 1);
        if (isCreate) {
            prefix = RvaView.FORM_CREATE.substring(0, 1);
        }
        return createViewId(prefix, tag, objId);
    }

    private String createDefaultFormId(String objId, boolean isCreate) {
        return createFormViewId(DEFAULT_TAG, objId, isCreate);
    }

    private String createListViewId(String tag, String objId) {
        return createViewId(RvaView.TYPE_LIST.substring(0, 1), tag, objId);
    }

    private String createDefaultListViewId(String objId) {
        return createListViewId(DEFAULT_TAG, objId);
    }

    private String createSearchViewId(String tag, String objId) {
        return createViewId(RvaView.TYPE_SEARCH.substring(0, 1), tag, objId);
    }

    private String createDefaultSearchViewId(String objId) {
        return createSearchViewId(DEFAULT_TAG, objId);
    }

    private RvaView createDefaultListView(RvaObject obj) {
        RvaView list = new RvaView();
        list.setHeight(500);
        list.setId(createDefaultListViewId(obj.getId()));
        list.setIdx(0);
        list.setName(obj.getName() + "列表");
        list.setObjId(obj.getId());
        list.setWidth(720);
        list.setType(RvaView.TYPE_LIST);
        list.setListPaging("Y");
        list.setListRows(10);
        list.setLoadUrl(String.format("%s?searchId=%s", RvaView.URL_LOAD_LIST.replace("{view}", list.getId()), createDefaultSearchViewId(obj.getId())));
        viewMapper.insertRvaView(list);
        return list;
    }

    private RvaView createDefaultFormView(RvaObject obj, boolean isCreate) {
        RvaView form = new RvaView();
        form.setId(createDefaultFormId(obj.getId(), isCreate));
        form.setType(RvaView.TYPE_FORM);
        form.setIdx(0);
        String desc = "新建";
        if (!isCreate) {
            desc = "修改";
        }
        form.setName(desc + obj.getName());
        form.setObjId(obj.getId());
        form.setFormColumns(1);
        form.setWidth(900);
        form.setHeight(600);
        String url = isCreate ? RvaView.URL_LOAD_CREATE : RvaView.URL_LOAD_UPDATE;
        form.setLoadUrl(url.replace("{view}", form.getId()));
        // 设置修改视图加载时的where语句：表别名.主键字段 = '${列表视图中的主键属性ID}'
        form.setLoadWhereOfUpdateView(obj);
        viewMapper.insertRvaView(form);
        return form;
    }

    private RvaView createDefaultSearchView(RvaObject obj) {
        RvaView form = new RvaView();
        form.setId(createDefaultSearchViewId(obj.getId()));
        form.setIdx(0);
        form.setName("查询" + obj.getName());
        form.setType(RvaView.TYPE_SEARCH);
        form.setObjId(obj.getId());
        viewMapper.insertRvaView(form);
        return form;
    }

    private RvaViewproperty createDefaultListProp(RvaObject obj, RvaProperty prop, int i) {
        RvaViewproperty lp = new RvaViewproperty();
        String listViewId = createDefaultListViewId(obj.getId());
        lp.setId(createViewpropertyId(listViewId, prop.getName()));
        lp.setIdx(prop.getIdx());
        lp.setName(prop.getDescriptionName());
        lp.setPropId(prop.getId());
        lp.setViewId(listViewId);
        if (RvaUtils.isNotEmpty(prop.getDictType())) {
            lp.setType(RvaViewproperty.TYPE_DICTIONARY);
        } else {
            lp.setType(RvaViewproperty.TYPE_TEXT);
        }
        lp.setDictType(prop.getDictType());
        lp.setWidth(160);
        if (obj.isCreateTime(prop.getName())) {
            lp.setListOrderIdx(0);
            lp.setListOrderType(RvaViewproperty.LIST_ORDER_DESC);
            lp.setType(RvaViewproperty.TYPE_DATETIME);
        }
        if (RvaProperty.TYPE_DATETIME.equals(prop.getType()) || prop.isTagSubmitTime()) {
            lp.setType(RvaViewproperty.TYPE_DATETIME);
        }
        if (obj.isCreateBy(prop.getName()) || obj.isUpdateBy(prop.getName()) || prop.isTagSubmitter() || prop.isTagUser()) {
            lp.setParamFormatter(RvaConstants.EXPRESSION_SQL_USER_NAME);
        }
        if (prop.isTagImage()) {
            lp.setType(RvaViewproperty.TYPE_IMAGE);
        }
        viewpropertyMapper.insertRvaViewproperty(lp);
        return lp;
    }

    private RvaViewproperty createDefaultListProp(RvaObject obj, RvaRelation relation, int i) {
        RvaViewproperty lp = new RvaViewproperty();
        String listViewId = createDefaultListViewId(obj.getId());
        lp.setId(createViewpropertyId(listViewId, relation.getProp().getName() + "_" + RvaUtils.generateKey32(2)));
        lp.setIdx(relation.getIdx());
        lp.setName(relation.getName());
        lp.setRelationId(relation.getId());
        lp.setViewId(listViewId);
        lp.setType(RvaViewproperty.TYPE_TEXT);
        lp.setWidth(160);
        //
        if (relation.isM21()) {
            RvaViewproperty viewproperty = getViewpropertyByRelatedProp(relation, listViewId);
            lp.setIdx(viewproperty.getIdx());
            viewproperty.setName(lp.getName() + "ID");
            viewproperty.setType(RvaViewproperty.TYPE_HIDDEN);
            viewpropertyMapper.updateRvaViewproperty(viewproperty);
        }
        viewpropertyMapper.insertRvaViewproperty(lp);
        return lp;
    }

    private RvaViewproperty getViewpropertyByRelatedProp(RvaRelation relation, String listViewId) {
        String sql = String.format("select id from rva_viewproperty where prop_id = '%s' and view_id = '%s'", relation.getProp().getId(), listViewId);
        String vpId = dataMapper.selectString(sql);
        RvaViewproperty viewproperty = viewpropertyMapper.selectRvaViewpropertyById(vpId);
        return viewproperty;
    }

    private RvaViewproperty createDefaultListButtonProp(RvaObject obj, int i) {
        RvaViewproperty lp = new RvaViewproperty();
        String listViewId = createDefaultListViewId(obj.getId());
        lp.setId(createViewpropertyId(listViewId, "operation_" + RvaUtils.generateKey32(3)));
        lp.setIdx(i);
        lp.setName("操作");
        lp.setViewId(listViewId);
        lp.setType(RvaViewproperty.TYPE_BUTTON);
        // 设置
        // lp.setListButtons(RvaUtils.join(Arrays.asList(buttonIds), ","));
        viewpropertyMapper.insertRvaViewproperty(lp);
        return lp;
    }

    private RvaViewproperty createDefaultFormProp(RvaObject obj, RvaProperty prop, boolean isCreate) {
        RvaViewproperty fp = new RvaViewproperty();
        String formViewId = createDefaultFormId(obj.getId(), isCreate);
        fp.setId(createViewpropertyId(formViewId, prop.getName()));
        fp.setIdx(prop.getIdx());
        fp.setViewId(formViewId);
        fp.setName(prop.getDescriptionName());
        fp.setPropId(prop.getId());
        this.setFormPropType(prop, fp);
        fp.setDictType(prop.getDictType());
        if (!RvaUtils.isEmpty(prop.getDictType())) {
            fp.setType(RvaViewproperty.TYPE_SELECT);
            fp.setFormSelectorSingle(prop.getDictSelectSingle());
        }
        fp.setFormRequired(prop.getRequired());
        fp.setFormColSpan(1);
        fp.setFormRowSpan(1);
        fp.setFormSubmit("Y");
        if (isCreate) {
            if (obj.isCreateBy(prop.getName()) || obj.isUpdateBy(prop.getName())) {// 创建人/修改人
                fp.setFormInitValue(RvaConstants.EXPRESSION_LOGIN_USER);
                fp.setFormInitReplaceEmpty("Y");
                fp.setType(RvaViewproperty.TYPE_HIDDEN);
            } else if (obj.isCreateTime(prop.getName()) || obj.isUpdateTime(prop.getName())) {// 创建时间/修改时间
                fp.setFormInitValue(RvaConstants.EXPRESSION_NOW);
                fp.setFormInitReplaceEmpty("Y");
                fp.setFormSubmitValue(RvaConstants.EXPRESSION_NOW);
                fp.setFormSubmitReplaceEmpty("Y");
                fp.setFormSubmitReplace("Y");
                fp.setType(RvaViewproperty.TYPE_HIDDEN);
            } else if (prop.isTagSubmitter()) {// 提交人
                fp.setFormInitValue(RvaConstants.EXPRESSION_LOGIN_USER);
                fp.setFormInitReplaceEmpty("Y");
                fp.setType(RvaViewproperty.TYPE_SELECTOR);
                fp.setFormRelatedCrud("crud0_sys_user");
                fp.setFormReadonly("Y");
            } else if (prop.isTagUser()) {// 用户
                fp.setFormInitValue(RvaConstants.EXPRESSION_LOGIN_USER);
                fp.setFormInitReplaceEmpty("Y");
                fp.setType(RvaViewproperty.TYPE_OBJECTSELECTOR);
                fp.setFormRelatedCrud("crud0_sys_user");
                fp.setFormReadonly("N");
            } else if (prop.isTagSubmitTime()) {// 提交时间
                fp.setFormInitValue(RvaConstants.EXPRESSION_NOW);
                fp.setFormInitReplaceEmpty("Y");
                fp.setFormSubmitValue(RvaConstants.EXPRESSION_NOW);
                fp.setFormSubmitReplaceEmpty("Y");
                fp.setFormSubmitReplace("Y");
                fp.setType(RvaViewproperty.TYPE_DATETIME);
                fp.setFormReadonly("Y");
            }
        } else {
            if (obj.isCreateBy(prop.getName())) {// 创建人
                fp.setType(RvaViewproperty.TYPE_SELECTOR);
                fp.setFormRelatedCrud("crud0_sys_user");
                fp.setFormReadonly("Y");
                fp.setFormSubmit("N");
            } else if (obj.isCreateTime(prop.getName())) {// 创建时间
                fp.setType(RvaViewproperty.TYPE_DATETIME);
                fp.setFormReadonly("Y");
                fp.setFormSubmit("N");
            } else if (obj.isUpdateBy(prop.getName())) {// 修改人
                fp.setFormSubmitValue(RvaConstants.EXPRESSION_LOGIN_USER);
                fp.setFormSubmitReplace("Y");// 有值时替换为上述表达式的值
                fp.setFormSubmitReplaceEmpty("Y");// 无值时替换为上述表达式的值
                fp.setType(RvaViewproperty.TYPE_SELECTOR);
                fp.setFormRelatedCrud("crud0_sys_user");
                fp.setFormReadonly("Y");
            } else if (obj.isUpdateTime(prop.getName())) {// 修改时间
                fp.setFormSubmitValue(RvaConstants.EXPRESSION_NOW);
                fp.setFormSubmitReplace("Y");// 有值时替换为上述表达式的值
                fp.setFormSubmitReplaceEmpty("Y");// 无值时替换为上述表达式的值
                fp.setType(RvaViewproperty.TYPE_DATETIME);
                fp.setFormReadonly("Y");
            } else if (prop.isTagSubmitter()) {// 提交人
                fp.setFormInitValue(RvaConstants.EXPRESSION_LOGIN_USER);
                fp.setFormInitReplaceEmpty("Y");
                fp.setFormInitReplace("Y");
                fp.setType(RvaViewproperty.TYPE_SELECTOR);
                fp.setFormRelatedCrud("crud0_sys_user");
                fp.setFormReadonly("Y");
            } else if (prop.isTagSubmitTime()) {// 提交时间
                fp.setFormInitValue(RvaConstants.EXPRESSION_NOW);
                fp.setFormInitReplaceEmpty("Y");
                fp.setFormInitReplace("Y");
                fp.setFormSubmitValue(RvaConstants.EXPRESSION_NOW);
                fp.setFormSubmitReplaceEmpty("Y");
                fp.setFormSubmitReplace("Y");
                fp.setType(RvaViewproperty.TYPE_DATETIME);
                fp.setFormReadonly("Y");
            } else if (prop.isTagUser()) {// 用户
                fp.setType(RvaViewproperty.TYPE_OBJECTSELECTOR);
                fp.setFormRelatedCrud("crud0_sys_user");
            }
        }
        if (prop.isTagImage()) {
            fp.setType(RvaViewproperty.TYPE_IMAGE);
        }
        if (!RvaUtils.isEmpty(prop.getDefaultValue())) {
            fp.setFormInitValue(prop.getDefaultValue());
        }
        Boolean primaryKey = obj.isPrimaryKey(prop.getName());
        if (primaryKey) {
            fp.setType(RvaViewproperty.TYPE_HIDDEN);
            if (isCreate && !"N".equals(prop.getIdGenType())) {
                fp.setFormRequired("N");
            }
        }
        if (primaryKey && !isCreate) {
            fp.setFormReadonly("Y");
        }
        viewpropertyMapper.insertRvaViewproperty(fp);
        return fp;
    }

    private RvaViewproperty createDefaultFormProp(RvaObject obj, RvaRelation relation, boolean isCreate) {
        RvaViewproperty fp = new RvaViewproperty();
        String formViewId = createDefaultFormId(obj.getId(), isCreate);
        fp.setId(createViewpropertyId(formViewId, relation.getProp().getName() + "_" + RvaUtils.generateKey32(2)));
        fp.setIdx(relation.getIdx() + 1000);
        fp.setViewId(formViewId);
        fp.setName(relation.getName());
        fp.setRelationId(relation.getId());
        fp.setFormColSpan(1);
        fp.setFormRowSpan(1);
        fp.setFormSubmit("Y");
        String relCrud = createCrudId("0", relation.getRelatedObj().getId());
        fp.setFormRelatedCrud(relCrud);
        if (relation.is12M()) {
            fp.setType(RvaViewproperty.TYPE_CRUD);
        }
        if (relation.isM21()) {
            fp.setFormSelectorSingle("Y");
            fp.setType(RvaViewproperty.TYPE_OBJECTSELECTOR);
        }
        if (relation.isM2M()) {
            fp.setFormSelectorSingle("N");
            fp.setType(RvaViewproperty.TYPE_OBJECTSELECTOR);
        }
        if (relation.isM21()) {
            RvaViewproperty viewproperty = getViewpropertyByRelatedProp(relation, formViewId);
            fp.setIdx(viewproperty.getIdx());
            viewpropertyMapper.deleteRvaViewpropertyById(viewproperty.getId());
        }
        viewpropertyMapper.insertRvaViewproperty(fp);
        return fp;
    }

    private void setFormPropType(RvaProperty prop, RvaViewproperty fp) {
        fp.setType(RvaViewproperty.TYPE_TEXT);
        switch (prop.getType()) {
            case RvaProperty.TYPE_DATE:
                fp.setType(RvaViewproperty.TYPE_DATE);
                break;
            case RvaProperty.TYPE_DATETIME:
                fp.setType(RvaViewproperty.TYPE_DATETIME);
                break;
            case RvaProperty.TYPE_INTEGER:
            case RvaProperty.TYPE_SMALLINT:
                fp.setType(RvaViewproperty.TYPE_NUMBER);
                Double max = Math.pow(10, RvaUtils.getInt(prop.getValueMax(), 10)) - 1;
                Double min = 0.0;
                fp.setFormValueMax(String.valueOf(max.intValue()));
                fp.setFormValueMin(String.valueOf(min.intValue()));
                // 设置数值控件允许的小数位数
                fp.setNumberScale(0);
                break;
            case RvaProperty.TYPE_NUMERIC:
                fp.setType(RvaViewproperty.TYPE_NUMBER);
                max = Math.pow(10, RvaUtils.getInt(prop.getValueMax(), 20)) - 1;
                min = 0.0;
                fp.setFormValueMax(max.toString());
                fp.setFormValueMin(min.toString());
                // 设置数值控件允许的小数位数
                fp.setNumberScale(prop.getNumberScale());
                break;
            case RvaProperty.TYPE_VARCHAR:
                fp.setFormValueMin("0");
                fp.setFormValueMax(prop.getValueMax());
                Integer length = RvaUtils.getInt(prop.getValueMax(), 0);
                if (length < 200) {
                    fp.setType(RvaViewproperty.TYPE_TEXT);
                } else if (length < 500) {
                    fp.setType(RvaViewproperty.TYPE_TEXTAREA);
                    fp.setHeight(100);
                } else {
                    fp.setType(RvaViewproperty.TYPE_TEXTAREA);
                    fp.setHeight(200);
                }
                break;
            case RvaProperty.TYPE_TEXT:
                fp.setType(RvaViewproperty.TYPE_TEXTAREA);
                fp.setHeight(200);
                break;
        }
    }

    /**
     * 创建nameProperty对应的查询字段
     *
     * @param obj
     * @return
     */
    private RvaViewproperty createDefaultSearchProp(RvaObject obj) {
        RvaProperty prop = obj.getNameProperty();
        if (prop == null) {
            return null;
        }
        RvaViewproperty sp = new RvaViewproperty();
        String searchViewId = createDefaultSearchViewId(obj.getId());
        sp.setId(createViewpropertyId(searchViewId, prop.getName()));
        sp.setIdx(0);
        sp.setName(prop.getDescriptionName());
        sp.setPropId(prop.getId());
        sp.setType(RvaViewproperty.TYPE_TEXT);
        sp.setSearchType(RvaViewproperty.SEARCH_LIKE);
        sp.setWidth(200);
        sp.setViewId(searchViewId);
        viewpropertyMapper.insertRvaViewproperty(sp);
        return sp;
    }

    /**
     * 列表视图新建按钮
     *
     * @param listId
     * @return
     */
    private RvaViewbutton createDefaultListCreateButton(String listId, String createId) {
        return viewbuttonService.createListCreateButton(RvaView.FORM_CREATE, 0, listId, createId, false);
    }

    /**
     * 列表视图修改按钮
     *
     * @param listId
     * @return
     */
    private RvaViewbutton createDefaultListUpdateButton(String listId, String updateId) {
        return viewbuttonService.createListUpdateButton(RvaView.FORM_UPDATE, 0, listId, updateId, false);
    }

    /**
     * 列表视图删除按钮
     *
     * @param listId
     * @return
     */
    private RvaViewbutton createDefaultListDeleteButton(String listId, String position) {
        String idSuffix = "delete_" + position;
        if (RvaViewbutton.POSITION_INNER.equals(position)) {
            return viewbuttonService.createListInnerDeleteButton(idSuffix, 3, listId);
        }
        return viewbuttonService.createListTopDeleteButton(idSuffix, 1, listId);
    }

    /**
     * 表单视图提交按钮
     *
     * @param formId 表单视图ID
     * @return
     */
    private RvaViewbutton createDefaultFormSubmitButton(String formId, boolean create) {
        String type = "submit_" + RvaView.FORM_UPDATE;
        if (create) {
            type = "submit_" + RvaView.FORM_CREATE;
            return viewbuttonService.createFormSubmitCreateButton(type, 0, formId);
        }
        return viewbuttonService.createFormSubmitUpdateButton(type, 0, formId);
    }

    /**
     * 表单视图重置按钮
     *
     * @param formId 表单视图ID
     * @return
     */
    private RvaViewbutton createDefaultFormResetButton(String formId, Boolean isCreate) {
        String idSuffix = "reset_" + getFormType(isCreate);
        return viewbuttonService.createFormResetButton(idSuffix, 1, formId);
    }

    private static String getFormType(Boolean isCreate) {
        return isCreate ? RvaView.FORM_CREATE : RvaView.FORM_UPDATE;
    }

    /**
     * 表单视图取消按钮
     *
     * @param formId 表单视图ID
     * @return
     */
    private RvaViewbutton createDefaultFormCancelButton(String formId, Boolean isCreate) {
        String idSuffix = "close_" + getFormType(isCreate);
        return viewbuttonService.createFormCancelButton(idSuffix, 2, formId);
    }

    private RvaApp createDefaultCrud(RvaObject obj) {
        RvaApp crud = new RvaApp();
        crud.setId(createDefaultCrudId(obj.getId()));
        crud.setIdx(0);
        crud.setName(obj.getName());
        crud.setObjId(obj.getId());
        crud.setStatus("2");
        crud.setTemplate("rva/app/" + RvaApp.TYPE_CRUD);
        crud.setType(RvaApp.TYPE_CRUD);
        appMapper.insertRvaApp(crud);
        String[] types = {RvaView.FORM_CREATE, RvaView.FORM_UPDATE, RvaView.TYPE_LIST, RvaView.TYPE_SEARCH};
        String[] viewIds = {createDefaultFormId(obj.getId(), true), createDefaultFormId(obj.getId(), false),
                createDefaultListViewId(obj.getId()), createDefaultSearchViewId(obj.getId())};
        for (int i = 0; i < types.length; i++) {
            createCrudAppItem(crud.getId(), types[i], i, viewIds[i]);
        }
        return crud;
    }

    private void createCrudAppItem(String appId, String type, int index, String viewId) {
        RvaAppitem appitem = new RvaAppitem();
        appitem.setAppId(appId);
        appitem.setId(appId + "_" + type);
        appitem.setIdx(index);
        RvaView view = viewMapper.selectRvaViewById(viewId);
        appitem.setName(view.getName());
        appitem.setType(type);
        appitem.setRelatedAppId(viewId);
        appitem.setRelatedAppType(view.getType());
        appitemMapper.insertRvaAppitem(appitem);
    }

    @Override
    public RvaApp createCrud(String table) {
        // 1.获取RvaObject元数据（包含属性和关系）
        RvaObject obj = objectMapper.selectRvaObjectById(table);
        // 2.删除默认crud相关数据
        String defaultCrudId = createDefaultCrudId(table);
        deleteApp(defaultCrudId);
        // 3.创建默认crud相关数据
        // 创建列表视图
        RvaView list = this.createDefaultListView(obj);
        // 创建“新建”表单视图
        RvaView create = this.createDefaultFormView(obj, true);
        // 创建“修改”表单视图
        RvaView update = this.createDefaultFormView(obj, false);
        // 创建查询视图
        RvaView search = this.createDefaultSearchView(obj);
        // 根据属性创建各个视图属性
        List<RvaProperty> props = obj.getProperties();
        for (int i = 0; i < props.size(); i++) {
            RvaProperty prop = props.get(i);
            if (prop.getName().equals(obj.getPropNameJson())) {
                continue;
            }
            // 如果不是主键，并且是外键，则略过
            this.createDefaultListProp(obj, prop, i);
            this.createDefaultFormProp(obj, prop, true);
            this.createDefaultFormProp(obj, prop, false);
        }
        // 根据关系创建各个视图属性
        for (int i = 0; i < obj.getRelations().size(); i++) {
            RvaRelation relation = obj.getRelations().get(i);
            // 如果不是主键，并且是外键，则略过
            this.createDefaultListProp(obj, relation, i);
            this.createDefaultFormProp(obj, relation, true);
            this.createDefaultFormProp(obj, relation, false);
        }
        this.createDefaultSearchProp(obj);
        // 创建列表视图按钮
        RvaViewbutton createButton = createDefaultListCreateButton(list.getId(), create.getId());
        createDefaultListDeleteButton(list.getId(), RvaViewbutton.POSITION_TOP);
        RvaViewbutton updateButton = createDefaultListUpdateButton(list.getId(), update.getId());
        RvaViewbutton deleteButton = createDefaultListDeleteButton(list.getId(), RvaViewbutton.POSITION_INNER);
        // 创建列表操作列
        createDefaultListButtonProp(obj, 999);
        // 创建“新建”表单视图按钮
        createDefaultFormSubmitButton(create.getId(), true);
        createDefaultFormResetButton(create.getId(), true);
        createDefaultFormCancelButton(create.getId(), true);
        // 创建“修改”表单视图按钮
        createDefaultFormSubmitButton(update.getId(), false);
        createDefaultFormResetButton(update.getId(), false);
        createDefaultFormCancelButton(update.getId(), false);
        // 更新排序
        updateOrder(obj, list);
        // 创建crud
        return createDefaultCrud(obj);
    }

    private void updateOrder(RvaObject obj, RvaView list) {
        if (obj.hasNotPropIndex()) {
            return;
        }
        list.getProperties().forEach(p -> {
            if (p.getListOrderIdx() >= 0) {
                p.setListOrderIdx(-1);
                viewpropertyMapper.updateRvaViewproperty(p);
            }
        });
        List<String> nameIndexWhere = obj.getPropNameIndexWhere();
        for (int i = 0; i < nameIndexWhere.size(); i++) {
            String propName = nameIndexWhere.get(i);
            RvaViewproperty p = list.getPropertyByObjectProperty(obj.getId() + "_" + propName);
            p.setListOrderIdx(i);
            p.setListOrderType(RvaViewproperty.LIST_ORDER_ASC);
            viewpropertyMapper.updateRvaViewproperty(p);
        }
        RvaViewproperty p = list.getPropertyByObjectProperty(obj.getId() + "_" + obj.getPropNameIndex());
        p.setListOrderIdx(nameIndexWhere.size());
        p.setListOrderType(RvaViewproperty.LIST_ORDER_ASC);
        viewpropertyMapper.updateRvaViewproperty(p);
    }

    public RvaApp cloneCrud(String appId, Boolean cascaded) {
        RvaApp crud = cloneById(appId, cascaded, true);
        int forms = 0;
        for (int i = 0; i < crud.getAppItems().size(); i++) {
            RvaAppitem appitem = crud.getAppItems().get(i);
            if (appitem.getRelatedAppType().equals(RvaView.TYPE_FORM)) {
                forms++;
                if (forms > 2) {
                    continue;
                }
            }
            String view = cloneViewInApp(appitem.getRelatedAppId());
            createCrudAppItem(crud.getId(), appitem.getType(), i, view);
        }
        // 更新update.loadWhere
        RvaApp oldCrud = appMapper.selectRvaAppById(appId);
        RvaView oldList = viewMapper.selectRvaViewById(oldCrud.getListId());
        RvaApp newCrud = appMapper.selectRvaAppById(crud.getId());
        RvaView newList = viewMapper.selectRvaViewById(newCrud.getListId());
        RvaView newUpdate = viewMapper.selectRvaViewById(newCrud.getUpdateId());
        newUpdate.setLoadWhere(newUpdate.getLoadWhere().replace(oldList.getId(), newList.getId()));
        viewMapper.updateRvaView(newUpdate);
        return newCrud;
    }

    private RvaApp cloneById(String appId, Boolean cascaded, Boolean cascadeDelete) {
        RvaApp app = appMapper.selectRvaAppById(appId).loadData();
        RvaApp search = new RvaApp();
        search.setObjId(app.getObjId());
        List<RvaApp> apps = appMapper.selectRvaAppList(search);
        // 获取RvaApp的ID中前缀的最大数字：crud6_rva_app，num=6
        int num = RvaUtils.getMaxNumber(app.getType(), apps);
        if (!appId.startsWith(app.getType())) {
            num++;
        }
        String newId = createId(app.getType(), num, app.getObjId());
        app.setId(newId);
        app.setStatus("1");
        app.setIdx(num);
        app.setName(app.getName());
        app.setViews(null);
        app.setApps(null);
        app.setCascadeDelete(cascadeDelete);
        app.setCascaded(cascaded);
        // CRUD
        appMapper.insertRvaApp(app);
        return app;
    }

    private String cloneViewInApp(String viewId) {
        RvaView view = cloneView(viewId, viewId.substring(0, 1), false);
        return view.getId();
    }

    private String copyViewId(String srcViewId, String prefix) {
        RvaView srcView = viewMapper.selectRvaViewById(srcViewId);
        return createViewId(srcView.getObjId(), prefix);
    }

    public String createViewId(String objId, String prefix) {
        RvaView search = new RvaView();
        search.setObjId(objId);
        List<RvaView> views = viewMapper.selectRvaViewList(search);
        int num = RvaUtils.getMaxNumber(prefix, views);
        String viewId = prefix + num + "_" + objId;
        return viewId;
    }

    @Override
    public RvaView cloneIndependentView(String srcViewId, Boolean cascaded) {
        return this.cloneView(srcViewId, srcViewId.substring(0, 1) + "c", cascaded);
    }

    @Override
    public RvaView createIndependentSearchView(String idSuffix, List<String> propTypes) {
        String objId = "none";
        // 创建查询视图
        String searchViewId = createViewId(objId, RvaView.TYPE_SEARCH.substring(0, 1)) + "_" + idSuffix;
        ;
        if (propTypes.size() > 0) {
            // 查询视图
            RvaView view = new RvaView();
            view.setId(searchViewId);
            view.setName("查询图表");
            view.setType(RvaView.TYPE_SEARCH);
            view.setIdx(0);
            view.setObjId(objId);
            viewMapper.insertRvaView(view);
            // 查询视图属性
            for (int i = 0; i < propTypes.size(); i++) {
                String prop = propTypes.get(i);
                RvaViewproperty viewproperty = new RvaViewproperty();
                viewproperty.setId(view.getId() + "_" + prop);
                viewproperty.setName(prop);
                viewproperty.setType(prop);
                viewproperty.setIdx(i);
                viewproperty.setSearchTypeByType();
                viewproperty.setViewId(searchViewId);
                viewpropertyMapper.insertRvaViewproperty(viewproperty);
            }
            return view;
        }
        return null;
    }

    @Override
    public RvaView cloneView(String srcViewId, String prefix, Boolean cascaded) {
        String newViewId = copyViewId(srcViewId, prefix);
        RvaView srcView = viewMapper.selectRvaViewById(srcViewId);
        RvaView newView = RvaUtils.cloneBySetter(srcView, RvaView.class);
        // 列表属性
        List<RvaViewproperty> props = newView.getProperties();
        for (RvaViewproperty prop : props) {
            prop.setId(this.copyViewPropertyOrButtonId(prop.getId(), newView.getId(), newViewId));
            if (RvaViewproperty.TYPE_BUTTON.equals(prop.getType()) && RvaUtils.isNotEmpty(prop.getListButtons())) {
                String buttonIds = this.copyViewPropertyOrButtonId(prop.getListButtons(), newView.getId(), newViewId);
                prop.setListButtons(buttonIds);
            }
            prop.setViewId(newViewId);
            viewpropertyMapper.insertRvaViewproperty(prop);
        }
        // 列表按钮
        List<RvaViewbutton> btns = newView.getButtons();
        for (RvaViewbutton btn : btns) {
            btn.setId(this.copyViewPropertyOrButtonId(btn.getId(), newView.getId(), newViewId));
            btn.setViewId(newViewId);
            if (RvaUtils.isNotEmpty(btn.getActionUrl())) {
                btn.setActionUrl(btn.getActionUrl().replace(newView.getId(), newViewId));
            }
            // 处理ActionDialogAppId，根据button.data.cascadeDelete来决定是否克隆应用
            if (RvaUtils.isNotEmpty(btn.getActionDialogAppId())) {
                if (btn.isCascadeDelete()) {
                    RvaApp cloneApp = cloneApp(btn.getActionDialogAppId(), true, true);
                    btn.setActionDialogAppId(cloneApp.getId());
                }
            }
            if (RvaUtils.isNotEmpty(btn.getActionDialogViewId())) {
                if (btn.isCascadeDelete()) {
                    RvaView cloneSingleView = this.cloneIndependentView(btn.getActionDialogViewId(), true);
                    btn.setActionDialogViewId(cloneSingleView.getId());
                } else {
                    btn.setActionDialogViewId(btn.getActionDialogViewId().substring(0, 1) + newViewId.substring(1));
                }
            }
            viewbuttonMapper.insertRvaViewbutton(btn);
        }
        newView.setId(newViewId);
        if (RvaUtils.isNotEmpty(newView.getLoadUrl())) {
            newView.setLoadUrl(newView.getLoadUrl().replace(srcView.getId(), newView.getId()));
            if (newView.getType().equals(RvaView.TYPE_LIST)) {
                newView.setLoadUrl(newView.getLoadUrl().replace("s" + srcView.getId().substring(1), "s" + newView.getId().substring(1)));
            }
        }
        newView.setCascaded(cascaded);
        viewMapper.insertRvaView(newView);
        return newView;
    }

    private String copyViewPropertyOrButtonId(String id, String oldView, String newView) {
        return id.replace(oldView, newView);
    }

    @Override
    public void deleteApp(String appId) {
        if (RvaUtils.isEmpty(appId)) {
            return;
        }
        RvaApp app = appMapper.selectRvaAppById(appId);
        if (app == null) {
            return;
        }
        app.getAppItems().forEach(rvaAppitem -> {
            if (app.isCascadeDelete()) {
                if (rvaAppitem.isView()) {
                    deleteView(rvaAppitem.getRelatedAppId());
                }
                if (rvaAppitem.isApp()) {
                    deleteApp(rvaAppitem.getRelatedAppId());
                }
            }
            appitemMapper.deleteRvaAppitemById(rvaAppitem.getId());
        });
        appMapper.deleteRvaAppById(appId);
    }

    public void deleteView(String viewId) {
        if (RvaUtils.isEmpty(viewId)) {
            return;
        }
        RvaView view = viewMapper.selectRvaViewById(viewId);
        if (view == null) {
            return;
        }
        view.getProperties().forEach(p -> {
            deleteViewpropertyCascaded(p);
        });
        view.getButtons().forEach(b -> {
            deleteViewbuttonCascaded(b);
        });
        dataMapper.delete("delete from rva_viewbutton where view_id = '" + viewId + "'");
        dataMapper.delete("delete from rva_viewproperty where view_id = '" + viewId + "'");
        viewMapper.deleteRvaViewById(viewId);
    }

    /**
     * 删除视图属性级联的数据
     *
     * @param p
     */
    private void deleteViewpropertyCascaded(RvaViewproperty p) {
        if (p.isCascadeDelete()) {
            this.deleteApp(p.getFormRelatedCrud());
        }
    }

    /**
     * 删除视图按钮级联的数据
     *
     * @param b
     */
    private void deleteViewbuttonCascaded(RvaViewbutton b) {
        if (b.isCascadeDelete()) {
            this.deleteApp(b.getActionDialogAppId());
            this.deleteView(b.getActionDialogViewId());
        }
    }

    @Override
    public void deleteViewbutton(String buttonId) {
        RvaViewbutton viewbutton = viewbuttonMapper.selectRvaViewbuttonById(buttonId);
        deleteViewbuttonCascaded(viewbutton);
        List<RvaMap<String, Object>> vpIds = dataMapper.selectList("select id from rva_viewproperty where list_buttons like '%" + buttonId + "%'");
        for (int i = 0; i < vpIds.size(); i++) {
            RvaViewproperty rvaViewproperty = viewpropertyMapper.selectRvaViewpropertyById(vpIds.get(i).getString("id"));
            String listButtons = rvaViewproperty.getListButtons();
            if (RvaUtils.isEmpty(listButtons)) {
                continue;
            }
            List<String> ids = new ArrayList<>();
            String[] buttons = listButtons.split(",");
            Boolean contains = false;
            for (int i1 = 0; i1 < buttons.length; i1++) {
                if (!buttons[i1].equals(buttonId)) {
                    ids.add(buttons[i1]);
                } else {
                    contains = true;
                }
            }
            if (contains) {
                rvaViewproperty.setListButtons(RvaUtils.join(ids));
                viewpropertyMapper.updateRvaViewproperty(rvaViewproperty);
            }
        }
        viewbuttonMapper.deleteRvaViewbuttonById(buttonId);
    }

    @Override
    public void deleteViewproperty(String viewpropertyId) {
        RvaViewproperty p = viewpropertyMapper.selectRvaViewpropertyById(viewpropertyId);
        deleteViewpropertyCascaded(p);
        viewpropertyMapper.deleteRvaViewpropertyById(viewpropertyId);
    }

    @Override
    public void createViewpropertyDict(RvaMap<String, Object> req) {
        String dictType = viewService.submitCreateView("c0_sys_dict_type", req);
        RvaViewproperty viewproperty = viewpropertyMapper.selectRvaViewpropertyById(req.getMap("rvaAppParams").getString("id"));
        viewproperty.setDictType(dictType);
        RvaView view = viewMapper.selectRvaViewById(viewproperty.getViewId());
        if (view.getType().equals(RvaView.TYPE_LIST)) {
            viewproperty.setType(RvaViewproperty.TYPE_DICTIONARY);
        } else {
            viewproperty.setType(RvaViewproperty.TYPE_SELECT);
        }
        viewpropertyMapper.updateRvaViewproperty(viewproperty);
    }

    @Override
    public void updateViewpropertyDict(RvaMap<String, Object> req) {
        String dictType = viewService.submitUpdateView("u0_sys_dict_type", req);
        SysDictData query = new SysDictData();
        query.setDictType(dictType);
        List<SysDictData> sysDictData = dictDataService.selectDictDataList(query);
        DictUtils.setDictCache(dictType, sysDictData);
    }

    @Override
    public void deleteViewpropertyDict(String viewpropertyId) {
        RvaViewproperty p = viewpropertyMapper.selectRvaViewpropertyById(viewpropertyId);
        Long aLong = dataMapper.selectLong(String.format("select count(*) from rva_viewproperty where prop_id != '%s' and dict_type = '%s'", p.getPropId(), p.getDictType()));
        if (aLong > 0) {
            RvaUtils.throwRuntimeException("该字典有其他视图或者应用关联，无法删除");
        }
        dataMapper.update(String.format("update rva_viewproperty set dict_type = null, type = '%s' where prop_id = '%s' and dict_type = '%s'", RvaViewproperty.TYPE_TEXT, p.getPropId(), p.getDictType()));
        dataMapper.deleteWhereMap("sys_dict_data", new RvaMap("dict_type", p.getDictType()));
        dataMapper.deleteWhereMap("sys_dict_type", new RvaMap("dict_type", p.getDictType()));
        DictUtils.removeDictCache(p.getDictType());
    }

    private final SysMenuMapper menuMapper;

    @Override
    @Deprecated
    public void createMenuAndButtonsByApp(String appId, Long parentId, Integer orderNum) {
        createMenuAndButtonsByApp(appId, parentId, orderNum, false, true);
    }

    @Override
    public void createMenuAndButtonsByApp(RvaMap<String, Object> req) {
        viewService.submitCreateView("cc1_sys_menu", req);
        List<Map> selection = req.getList("selection");
        createMenuAndButtonsByApp(selection.get(0).get("keyPropValue").toString(), null, null, false, false);
    }

    @Override
    public void createMenuAndButtonsByView(RvaMap<String, Object> req) {
        String menuId = viewService.submitCreateView("cc0_sys_menu", req);
        List<Map> selection = req.getList("selection");
        String viewId = selection.get(0).get("keyPropValue").toString();
        createViewButtonsMenu(Long.parseLong(menuId), viewMapper.selectRvaViewById(viewId));
    }

    private void createMenuAndButtonsByApp(String appId, Long parentId, Integer orderNum, Boolean btn, Boolean menuInsert) {
        RvaApp app = appMapper.selectRvaAppById(appId);
        if (app == null) {
            return;
        }
        SysMenu menu = new SysMenu();
        if (!btn) {
            if (menuInsert) {
                // dataMapper.deleteWhereMap("sys_menu", new RvaMap<>("path", appId));
                menu.setIcon("build");
                menu.setMenuName(app.getName());
                menu.setMenuType("C");
                menu.setCreateBy("admin");
                menu.setComponent(app.getTemplate());
                menu.setIsCache("0");
                menu.setIsFrame("1");
                menu.setOrderNum(RvaUtils.generateFixedLenNo(4, orderNum));
                menu.setPerms(appId);
                menu.setParentId(parentId);
                menu.setPath(appId);
                menu.setStatus("0");
                menu.setVisible("0");
                menu.setCreateTime(new Date());
                menu.setRemark(app.getName() + "菜单");
                menuMapper.insertMenu(menu);
            } else {
                SysMenu sysMenu = new SysMenu();
                sysMenu.setPath(appId);
                List<SysMenu> sysMenus = menuMapper.selectMenuList(sysMenu);
                menu = sysMenus.get(0);
            }
        } else {
            menu = createButtonMenu(app.getId(), orderNum, app.getName(), parentId);
        }
        // 按钮
        for (int i = 0; i < app.getAppItems().size(); i++) {
            RvaAppitem appitem = app.getAppItems().get(i);
            if (!appitem.isPermission()) {
                continue;
            }
            if (appitem.isView()) {
                RvaView v = viewMapper.selectRvaViewById(appitem.getRelatedAppId());
                SysMenu vMenu = createButtonMenu(v.getId(), i, v.getName(), menu.getMenuId());
                createViewButtonsMenu(vMenu.getMenuId(), v);
            }
            if (appitem.isApp()) {
                createMenuAndButtonsByApp(appitem.getRelatedAppId(), menu.getMenuId(), i, true, true);
            }
        }
    }

    private SysMenu createButtonMenu(String id, int index, String name, Long parentId) {
        // dataMapper.deleteWhereMap("sys_menu", new RvaMap<String, Object>("perms", id).rvaPut("menu_type", "F"));
        SysMenu btnMenu = new SysMenu();
        btnMenu.setIcon("#");
        btnMenu.setMenuName(name);
        btnMenu.setMenuType("F");
        btnMenu.setCreateBy("admin");
        btnMenu.setIsCache("0");
        btnMenu.setIsFrame("1");
        btnMenu.setOrderNum(RvaUtils.generateFixedLenNo(4, index));
        btnMenu.setPerms(id);
        btnMenu.setParentId(parentId);
        btnMenu.setStatus("0");
        btnMenu.setVisible("0");
        btnMenu.setCreateTime(new Date());
        btnMenu.setRemark(name + "按钮");
        menuMapper.insertMenu(btnMenu);
        return btnMenu;
    }

    private void createViewButtonsMenu(Long parentId, RvaView v) {
        v.getButtons().forEach(btn -> {
            createButtonMenu(btn.getId(), btn.getIdx(), v.getName() + "-" + btn.getName(), parentId);
        });
    }

    @Override
    public void deleteMenuAndButtons(String... appIds) {
        for (String appId : appIds) {
            Long menuId = dataMapper.selectLong("select menu_id from sys_menu where path = '" + appId + "'");
            deleteMenuAndChildren(menuId);
        }
    }

    private void deleteMenuAndChildren(Long menuId) {
        List<RvaMap<String, Object>> list = dataMapper.selectList(String.format("select menu_id from sys_menu where parent_id = %d", menuId));
        list.forEach(m -> {
            deleteMenuAndChildren(m.getLong("menu_id"));
        });
        dataMapper.deleteWhereMap("sys_role_menu", new RvaMap<>("menu_id", menuId));
        dataMapper.deleteWhereMap("sys_menu", new RvaMap<>("menu_id", menuId));
    }

    @Override
    public void createRvaMenu() {
        dataMapper.deleteWhereMap("sys_menu", new RvaMap<>("path", "rva"));
        SysMenu menu = new SysMenu();
        menu.setIcon("tool");
        menu.setMenuName("RVA管理");
        menu.setMenuType("M");
        menu.setCreateBy("admin");
        menu.setIsCache("0");
        menu.setIsFrame("1");
        menu.setOrderNum("5");
        menu.setParentId(0L);
        menu.setPath("rva");
        menu.setStatus("0");
        menu.setVisible("0");
        menu.setCreateTime(new Date());
        menu.setRemark("RVA管理目录");
        menuMapper.insertMenu(menu);
        createMenuAndButtonsByApp("crud0_rva_module", menu.getMenuId(), 1);
        createMenuAndButtonsByApp("crud1_rva_object", menu.getMenuId(), 2);
        createMenuAndButtonsByApp("crud1_rva_app", menu.getMenuId(), 3);
        createMenuAndButtonsByApp("tcrud0_rva_view", menu.getMenuId(), 4);
    }

    @Override
    public RvaApp createTreeCrud(List<String> contents, List<String> navs, Boolean clone) {
        RvaApp tcrud = createTcrud(contents, clone);
        addNavsToTreeCrud(navs, tcrud);
        addContentsToTreeCrud(contents, tcrud);
        return tcrud;
    }

    @Override
    public void addContentsToTreeCrud(List<String> contents, String appId) {
        RvaApp app = appMapper.selectRvaAppById(appId);
        addContentsToTreeCrud(contents, app);
    }

    @Override
    public void addNavsToTreeCrud(List<String> navs, String appId) {
        RvaApp app = appMapper.selectRvaAppById(appId);
        addNavsToTreeCrud(navs, app);
    }

    private void addContentsToTreeCrud(List<String> contents, RvaApp tcrud) {
        List<RvaAppitem> contentItems = tcrud.getAppItemsByType(RvaAppitem.TYPE_CONTENT);
        for (int i = 0; i < contents.size(); i++) {
            String contentId = contents.get(i);
            RvaAppitem appitem = new RvaAppitem();
            appitem.setAppId(tcrud.getId());
            appitem.setType(RvaAppitem.TYPE_CONTENT);
            appitem.setId(createAppitemId(tcrud, i + contentItems.size(), appitem));
            appitem.setIdx(i + contentItems.size());
            // 设置关联数据
            appitem.setRelatedAppId(contentId);
            RvaApp contentApp = appMapper.selectRvaAppById(contentId);
            if (contentApp != null) {
                appitem.setName(contentApp.getName());
                appitem.setRelatedAppType(contentApp.getType());
                if (tcrud.isCascadeDelete()) {
                    RvaApp cloneApp = cloneApp(contentId, true, true);
                    appitem.setRelatedAppId(cloneApp.getId());
                }
            }
            RvaView contentView = viewMapper.selectRvaViewById(contentId);
            if (contentView != null) {
                appitem.setName(contentView.getName());
                appitem.setRelatedAppType(contentView.getType());
                if (tcrud.isCascadeDelete()) {
                    RvaView view = cloneIndependentView(contentId, true);
                    appitem.setRelatedAppId(view.getId());
                }
            }
            RvaKpi contentKpi = kpiMapper.selectRvaKpiById(contentId);
            if (contentKpi != null) {
                appitem.setName(contentKpi.getName());
                appitem.setRelatedAppType(RvaKpi.NAME);
                if (tcrud.isCascadeDelete()) {
                    // appitem.setRelatedAppId(view.getId());
                }
            }
            RvaChart contentChart = chartMapper.selectRvaChartById(contentId);
            if (contentChart != null) {
                appitem.setName(contentChart.getName());
                appitem.setRelatedAppType(contentChart.NAME);
                if (tcrud.isCascadeDelete()) {
                    // appitem.setRelatedAppId(view.getId());
                }
            }
            appitemMapper.insertRvaAppitem(appitem);
        }
    }

    private void addNavsToTreeCrud(List<String> navs, RvaApp tcrud) {
        List<RvaAppitem> navItems = tcrud.getAppItemsByType(RvaAppitem.TYPE_NAV);
        //
        String parentId = null;
        if (navItems.size() > 0) {
            parentId = navItems.get(navItems.size() - 1).getId();
        }
        for (int i = 0; i < navs.size(); i++) {
            String navId = navs.get(i);
            RvaAppitem appitem = new RvaAppitem();
            appitem.setAppId(tcrud.getId());
            appitem.setType(RvaAppitem.TYPE_NAV);
            appitem.setId(createAppitemId(tcrud, i + navItems.size(), appitem));
            appitem.setIdx(i + navItems.size());
            RvaApp navApp = appMapper.selectRvaAppById(navId);
            appitem.setName(navApp.getName());
            appitem.setSubType("list");
            appitem.setParentId(parentId);
            appitem.setRelatedAppId(navId);
            if (tcrud.isCascadeDelete()) {
                RvaApp cloneApp = cloneApp(navId, true, true);
                appitem.setRelatedAppId(cloneApp.getId());
            }
            appitem.setRelatedAppType(navApp.getType());
            appitemMapper.insertRvaAppitem(appitem);
            parentId = appitem.getId();
        }
    }

    private String createAppitemId(RvaApp tcrud, int i, RvaAppitem appitem) {
        return String.format("%s_%s_%d", tcrud.getId(), appitem.getType(), i) + RvaUtils.generateKey32(4);
    }

    private String createObjIdSql(String table, String appId) {
        return String.format("select obj_id from %s where id = '%s'", table, appId);
    }

    private RvaApp createTcrud(List<String> contentIds, Boolean clone) {
        String appId = contentIds.get(0);
        String sql = RvaUtils.join(Arrays.asList(createObjIdSql("rva_app", appId), createObjIdSql("rva_view", appId), createObjIdSql("rva_kpi", appId), createObjIdSql("rva_chart", appId)), " union all ");
        String objId = dataMapper.selectString(sql);
        String name = "导航";
        if (RvaUtils.isNotEmpty(objId) && !RvaObject.NONE.equals(objId)) {
            name += objectMapper.selectRvaObjectById(objId).getName();
        }
        RvaApp search = new RvaApp();
        search.setObjId(objId);
        search.setType(RvaApp.TYPE_TCRUD);
        List<RvaApp> apps = appMapper.selectRvaAppList(search);
        // 获取crud的ID中前缀的最大数字：crud6_mapp，num=6
        String prefix = RvaApp.TYPE_TCRUD;
        int num = RvaUtils.getMaxNumber(prefix, apps);
        String tcrudId = prefix + num + "_" + objId;
        // tcrud
        RvaApp tcrud = new RvaApp();
        tcrud.setId(tcrudId);
        tcrud.setStatus("1");
        tcrud.setIdx(num);
        tcrud.setType(RvaApp.TYPE_TCRUD);
        tcrud.setTemplate("rva/app/" + RvaApp.TYPE_TCRUD);
        tcrud.setObjId(objId);
        tcrud.setName(name);
        // tcrud.setApps(RvaJsonUtils.writeAsString(new RvaMap("tcrud", contentIds).rvaPut("treeCruds", treeCruds)));
        // tcrud.setData(RvaJsonUtils.writeAsString(new RvaMap("urlLoadNode", RvaApp.getUrlTreeCrudLoadNode(tcrudId)).rvaPut("urlLoadContent", RvaApp.getUrlTreeCrudLoadContent(tcrudId))));
        tcrud.setCascadeDelete(clone);
        appMapper.insertRvaApp(tcrud);
        return tcrud;
    }

    @Override
    public RvaApp cloneApp(String appId, Boolean cascade, Boolean cascadeDelete) {
        RvaApp app = appMapper.selectRvaAppById(appId);
        if (RvaApp.TYPE_CRUD.equals(app.getType())) {
            return cloneCrud(appId, cascade);
        }
        app = cloneById(appId, cascade, cascadeDelete);
        for (int i = 0; i < app.getAppItems().size(); i++) {
            RvaAppitem rvaAppitem = RvaJsonUtils.readAsTypeByData(app.getAppItems().get(i), RvaAppitem.class);
            rvaAppitem.setAppId(app.getId());
            if (RvaAppitem.APP_CRUD.equals(rvaAppitem.getRelatedAppType())) {
                RvaApp cloneCrud = cloneCrud(rvaAppitem.getRelatedAppId(), cascade);
                rvaAppitem.setRelatedAppId(cloneCrud.getId());
            } else if (rvaAppitem.isApp()) {
                RvaApp cloneApp = cloneApp(rvaAppitem.getRelatedAppId(), cascade, cascadeDelete);
                rvaAppitem.setRelatedAppId(cloneApp.getId());
            } else if (rvaAppitem.isView()) {
                RvaView cloneSingleView = cloneIndependentView(rvaAppitem.getRelatedAppId(), cascade);
                rvaAppitem.setRelatedAppId(cloneSingleView.getId());
            }
            rvaAppitem.setId(createAppitemId(app, i, rvaAppitem));
            appitemMapper.insertRvaAppitem(rvaAppitem);
        }
        return app;
    }

    @Override
    public void deleteApp(List<String> appIds) {
        for (String appId : appIds) {
            deleteApp(appId);
        }
        String[] strings = new String[appIds.size()];
        this.deleteMenuAndButtons(appIds.toArray(strings));
    }

    /**
     * @param dict 支持格式：0正常 1-异常 C菜单 123 选项  123-'read me' key-tom's 01-tom's
     * @return
     */
    private static List<String> parseDictData(String dict) {
        Pattern pattern = Pattern.compile("([0-9a-zA-Z]*)?-?('([^']*)'|([^ ]*))( |$)");
        Matcher matcher = pattern.matcher(dict);
        int n = 0;
        List<String> strings = new ArrayList<>();
        while (matcher.find()) {
            // log.info("------------------");
            String key = matcher.group(1).trim();
            String value = matcher.group(2).trim();
            if (RvaUtils.isEmpty(key) && RvaUtils.isEmpty(value)) {
                continue;
            }
            if (RvaUtils.isEmpty(key)) {
                key = RvaUtils.generateFixedLenNo(2, n);
            }
            if (RvaUtils.isEmpty(value)) {
                value = key;
            }
            // log.info(key + "," + value);
            strings.add(key);
            strings.add(value);
            n++;
        }
        return strings;
    }

    @Override
    public void createListDictButtons(String buttonId) {
        RvaViewbutton viewbutton = viewbuttonMapper.selectRvaViewbuttonById(buttonId);
        RvaView view = viewMapper.selectRvaViewById(viewbutton.getViewId());
        Set<String> dictTypes = new HashSet<>();
        view.getProperties().forEach(p -> {
            if (RvaUtils.isNotEmpty(p.getDictType())) {
                dictTypes.add(p.getDictType());
            }
        });
        view.getButtons().forEach(b -> {
            if (RvaUtils.isEmpty(b.getActionDialogViewId())) {
                return;
            }
            RvaView dialogView = viewMapper.selectRvaViewById(b.getActionDialogViewId());
            dialogView.getProperties().forEach(p -> {
                if (RvaUtils.isNotEmpty(p.getDictType())) {
                    dictTypes.add(p.getDictType());
                }
            });
        });
        dictTypes.forEach(dictType -> {
            SysDictType sysDictType = dictTypeService.selectDictTypeByType(dictType);
            RvaViewbutton b = new RvaViewbutton();
            b.setId(view.getId() + "_dict_" + RvaUtils.generateKey32(4));
            b.setName(sysDictType.getDictName());
            b.setType(RvaViewbutton.TYPE_FORM);
            b.setCls(RvaViewbutton.CLS_PRIMARY);
            b.setAction(RvaViewbutton.ACTION_DIALOG);
            b.setPosition(RvaViewbutton.POSITION_TOP);
            b.setViewId(view.getId());
            b.setIdx(viewbutton.getIdx() + 1);
            b.setActionDialogViewId("u0_sys_dict_type");
            b.setParam("dict_type", dictType);
            b.setIcon("el-icon-notebook-2");
            b.setSelectType(RvaViewbutton.SELECT_NONE);
            viewbuttonMapper.insertRvaViewbutton(b);
        });
    }

    @Override
    public RvaMap getCreateSQL(String table) {
        StringBuffer sb = new StringBuffer();
        getObjectMetaSql(table, sb);
        return getMetaSqlData("c0_none_jianbiaojiaoben", "c0_none_jianbiaojiaoben_jiaoben", sb.toString());
    }

    private void getObjectMetaSql(String table, StringBuffer sb) {
        RvaObject object = objectMapper.selectRvaObjectById(table);

        sb.append("-- object : " + table + " -- start --\n");

        sb.append("-- object DDL : " + table + " -- start --\n");
        // 建表脚本
        sb.append(RvaObject.getDropTableSql(table) + ";\n");
        sb.append(RvaObject.getCreateTableSql(object) + ";\n");
        sb.append("-- object DDL : " + table + " -- end --\n");

        // 关系元数据SQL
        sb.append("-- object relation : " + table + " -- start --\n");
        // 关系元数据删除SQL
        sb.append(getDeleteSQLIn("rva_relationitem", "relation_id", String.format("select id from rva_relation where obj_id = '%s'", table)));
        sb.append(getDeleteSQL("rva_relation", "obj_id", table));
        // 关系元数据插入SQL
        for (RvaRelation relation : object.getRelations()) {
            for (RvaRelationitem item : relation.getItems()) {
                sb.append(getInsertSQL("rva_relationitem", item));
            }
            sb.append(getInsertSQL("rva_relation", relation));
            // 关系表ID
            String relationObjId = relation.getRelationObjId();
            if (RvaUtils.isEmpty(relationObjId) || table.equals(relationObjId)) {
                continue;
            }
            getObjectMetaSql(relationObjId, sb);
        }
        sb.append("-- object relation : " + table + " -- end --\n");

        // 属性元数据SQL
        sb.append("-- object property : " + table + " -- start --\n");
        // 属性元数据删除SQL
        sb.append(getDeleteSQL("rva_property", "obj_id", table));
        // 属性元数据插入SQL
        for (RvaProperty property : object.getProperties()) {
            sb.append(getInsertSQL("rva_property", property));
        }
        sb.append("-- object property : " + table + " -- end --\n");

        // 对象元数据SQL
        sb.append("-- object meta : " + table + " -- start --\n");
        // 对象元数据删除SQL
        sb.append(getDeleteSQL("rva_object", "id", table));
        // 对象元数据插入SQL
        sb.append(getInsertSQL("rva_object", object));
        sb.append("-- object meta : " + table + " -- end --\n");

        sb.append("-- object : " + table + " -- end --\n");
    }

    private static String getDeleteSQL(String table, String column, String val) {
        return String.format("delete from %s where %s = '%s';\n", table, column, val);
    }

    private static String getDeleteSQLIn(String table, String column, String sql) {
        return String.format("delete from %s where %s in (%s);\n", table, column, sql);
    }

    @Override
    public RvaMap getAppMetaSQL(String appId) {
        StringBuffer sb = new StringBuffer();
        getAppMetaSQL(appId, sb, new ArrayList<String>());
        return getMetaSqlData("c0_none_yuanshujujiaoben", "c0_none_yuanshujujiaoben_jiaoben", sb.toString());
    }

    private final IRvaDocService docService;

    @Override
    public RvaMap getAppDocument(String appId) {
        RvaApp app = appMapper.selectRvaAppById(appId);
        String string = velocityUtils.mergeToString("/vm/usecase.doc.crud.vm", new RvaMap("app", app).rvaPut("docService", docService));
        return getMetaSqlData("c0_none_yuanshujujiaoben", "c0_none_yuanshujujiaoben_jiaoben", string);
    }

    private void getViewMetaSQL(String viewId, StringBuffer sb, List<String> ids) {
        if (ids.contains(viewId)) {
            sb.append("-- duplicated view : " + viewId + "\n");
            return;
        }
        ids.add(viewId);
        sb.append("-- view : " + viewId + " -- start --\n");
        RvaView view = viewMapper.selectRvaViewById(viewId);
        if (view == null) {
            sb.append("-- view : " + viewId + " -- end --\n");
            return;
        }

        // rva_viewproperty sql
        sb.append("-- view - rva_viewproperty : " + viewId + " -- start --\n");
        sb.append(getDeleteSQL("rva_viewproperty", "view_id", viewId));
        for (RvaViewproperty property : view.getProperties()) {
            sb.append(getInsertSQL("rva_viewproperty", property));
        }
        sb.append("-- view - rva_viewproperty : " + viewId + " -- end --\n");

        // rva_viewbutton sql
        sb.append("-- view - rva_viewbutton : " + viewId + " -- start --\n");
        sb.append(getDeleteSQL("rva_viewbutton", "view_id", viewId));
        for (RvaViewbutton button : view.getButtons()) {
            sb.append(getInsertSQL("rva_viewbutton", button));
        }
        sb.append("-- view - rva_viewbutton : " + viewId + " -- end --\n");

        // rva_trigger/rva_triggeraction sql
        sb.append("-- view - rva_trigger : " + viewId + " -- start --\n");
        sb.append(getDeleteSQLIn("rva_triggeraction", "trigger_id", String.format("select id from rva_trigger where view_id = '%s'", viewId)));
        sb.append(getDeleteSQL("rva_trigger", "view_id", viewId));
        for (RvaTrigger trigger : view.getTriggers()) {
            sb.append(getInsertSQL("rva_trigger", trigger));
            for (RvaTriggeraction action : trigger.getActions()) {
                sb.append(getInsertSQL("rva_triggeraction", action));
            }
        }
        sb.append("-- view - rva_trigger : " + viewId + " -- end --\n");

        // rva_view sql
        sb.append("-- view - rva_view : " + viewId + " -- start --\n");
        sb.append(getDeleteSQL("rva_view", "id", viewId));
        sb.append(getInsertSQL("rva_view", view));
        sb.append("-- view - rva_view : " + viewId + " -- end --\n");

        sb.append("-- view : " + viewId + " -- end --\n");
    }

    private void getAppMetaSQL(String appId, StringBuffer sb, List<String> ids) {
        if (ids.contains(appId)) {
            sb.append("-- duplicated app : " + appId + "\n");
            return;
        }
        ids.add(appId);
        sb.append("-- app : " + appId + " -- start --\n");
        RvaApp app = appMapper.selectRvaAppById(appId);
        List<RvaAppitem> appItems = app.getAppItems();
        for (int i = 0; i < appItems.size(); i++) {
            RvaAppitem appitem = appItems.get(i);
            String relatedAppType = appitem.getRelatedAppType();
            if (RvaAppitem.APP_DIRECTORY.equals(relatedAppType)) {
                continue;
            }
            if (Arrays.asList(RvaAppitem.APP_CRUD, RvaAppitem.APP_TCRUD).contains(relatedAppType)) {
                getAppMetaSQL(appitem.getRelatedAppId(), sb, ids);
            } else {// view
                getViewMetaSQL(appitem.getRelatedAppId(), sb, ids);
            }
        }

        sb.append("-- app - rva_appitem : " + appId + " -- start --\n");
        sb.append(getDeleteSQL("rva_appitem", "app_id", appId));
        for (int i = 0; i < appItems.size(); i++) {
            sb.append(getInsertSQL("rva_appitem", appItems.get(i)));
        }
        sb.append("-- app - rva_appitem : " + appId + " -- end --\n");

        sb.append("-- app - rva_app : " + appId + " -- start --\n");
        sb.append(getDeleteSQL("rva_app", "id", appId));
        sb.append(getInsertSQL("rva_app", app));
        sb.append("-- app - rva_app : " + appId + " -- end --\n");

        sb.append("-- app : " + appId + " -- end --\n");
    }

    @SneakyThrows
    private String getInsertSQL(String table, Object data) {
        SQL sql = new SQL();
        RvaObject object = objectMapper.selectRvaObjectById(table);
        sql = sql.INSERT_INTO(table);
        List<RvaProperty> properties = object.getProperties();
        for (int i = 0; i < properties.size(); i++) {
            RvaProperty p = properties.get(i);
            Method method = data.getClass().getMethod(RvaUtils.getCamelCase("get_" + p.getName(), false));
            Object val = method.invoke(data);
            sql = sql.INTO_COLUMNS("`" + p.getName() + "`").INTO_VALUES(p.getSqlValue(val));
        }
        return sql.toString() + ";\n";
    }

    private final RvaVelocityUtils velocityUtils;

    @Override
    public void quickCreateObject(String objName, String objNo, String module, boolean uniApp, boolean tenant) {
        // "/vm/default.table.sql.vm"
        this.quickCreateObject(objName, objNo, module, uniApp, tenant, "/vm/default.table.sql.vm");
    }

    @Override
    public RvaObject quickCreateObject(String objName, String objNo, String module, boolean uniApp, boolean tenant, String sqlTemplate) {
        if (objNo == null && objName == null) {
            RvaUtils.throwRequiredAllException("对象名称", "对象编号");
        }
        if (objNo == null) {
            objNo = RvaPinyinUtils.getPinyinLower(objName);
        }
        if (objName == null) {
            objName = objNo;
        }
        String table = module + "_" + objNo;
        if (schemaMapper.queryTableCount(table) == 1) {
            RvaUtils.throwExistsException(table);
        }
        String sql = velocityUtils.mergeToString(sqlTemplate, new RvaMap<String, Object>("table", table).rvaPut("name", objName).rvaPut("uniApp", uniApp).rvaPut("tenant", tenant));
        schemaMapper.execute(sql);
        this.deleteAllObjectMeta(table);
        return this.createObjectMeta(table);
    }

    @Override
    public void addToBlackList(RvaMap<String, Object> req) {
        String viewPropertyId = req.getMap("rvaAppParams").getString("id");
        List<String> roleIds = req.getList("c0_none_heimingdan_jiaose");

        String type = req.getString("c0_none_heimingdan_leixing");
        RvaViewproperty rvaViewproperty = viewpropertyMapper.selectRvaViewpropertyById(viewPropertyId);
        String propId = rvaViewproperty.getPropId();
        String propSubId = rvaViewproperty.getPropSubId();
        String objValue = "rva_property:" + (RvaUtils.isNotEmpty(propSubId) ? propId + ":" + propSubId : propId);
        String viewValue = "rva_viewproperty:" + viewPropertyId;
        //删除该属性的所有黑名单
        this.deleteBlackList(viewPropertyId);
        for (String roleId : roleIds) {
            String str = dataMapper.selectString("SELECT property_blacklist FROM sys_role WHERE role_id=" + roleId);
            List<String> blacklist = new ArrayList<>();
            String nValue = "";
            if (RvaUtils.isNotEmpty(str)) {
                blacklist = RvaUtils.asList(str.split(","));
            }
            if ("01".equals(type)) {
                nValue = objValue;
            } else {
                nValue = viewValue;
            }
            if (!blacklist.contains(nValue)) {
                blacklist.add(nValue);
            }
            String blacklistStr = RvaUtils.join(blacklist);
            dataMapper.updateWhereMap("sys_role",
                    new RvaMap<>("property_blacklist", blacklistStr),
                    new RvaMap<>("role_id", roleId),
                    true
            );
        }


    }

    @Override
    public RvaMap getAppUsage(String appId) {
        List<RvaMap<String, Object>> usageList = appMapper.getUsage(appId);
        List<String> appIds = appMapper.getObjectAppIds(appId);
        List<RvaMap<String, Object>> results = new ArrayList<>();
        appIds.forEach(aid -> {
            results.add(new RvaMap<String, Object>("app", aid).rvaPut("usageType", "viewproperty").rvaPut("count", 0));
            results.add(new RvaMap<String, Object>("app", aid).rvaPut("usageType", "viewbutton").rvaPut("count", 0));
            results.add(new RvaMap<String, Object>("app", aid).rvaPut("usageType", "menu").rvaPut("count", 0));
            results.add(new RvaMap<String, Object>("app", aid).rvaPut("usageType", "appitem").rvaPut("count", 0));
        });
        usageList.forEach(u -> {
            List<String> apps = Arrays.asList(u.getString("app").split(","));
            String usage = u.getString("usageType");
            RvaMap<String, Object> map = results.stream().filter(r -> {
                return apps.contains(r.get("app")) && r.get("usageType").equals(usage);
            }).findFirst().get();
            map.put("count", map.getInt("count") + 1);
        });
        Collections.sort(results, new Comparator<RvaMap<String, Object>>() {

            private int getAppNum(RvaMap<String, Object> o1) {
                return RvaUtils.getInt(o1.getString("app").split("_")[0].substring(4), -1);
            }

            @Override
            public int compare(RvaMap<String, Object> o1, RvaMap<String, Object> o2) {
                if (getAppNum(o1) == getAppNum(o2)) {
                    return o1.getString("usageType").compareTo(o2.getString("usageType"));
                }
                return getAppNum(o1) - getAppNum(o2);
            }
        });
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < results.size(); i++) {
            RvaMap<String, Object> row = results.get(i);
            if (i % 4 == 0) {
                sb.append("------------ ");
                sb.append(row.getString("app"));
                sb.append(" ------------\n");
            }
            sb.append(row.getString("app"));
            sb.append(" - ");
            sb.append(row.getString("usageType"));
            sb.append(" : ");
            sb.append(row.getInt("count", 0));
            sb.append("\n");
        }
        results.forEach(row -> {

        });
        RvaView view = viewMapper.selectRvaViewById("c0_none_shiyongqingkuang");
        RvaMap formData = new RvaMap("c0_none_shiyongqingkuang_shiyongqingkuang", sb.toString());
        return new RvaMap("viewData", view).rvaPut("formData", formData);
    }

    @Override
    public RvaMap loadBlackList(RvaMap<String, Object> req) {
        String viewPropertyId = req.getMap("rvaAppParams").getString("id");
        RvaViewproperty rvaViewproperty = viewpropertyMapper.selectRvaViewpropertyById(viewPropertyId);
        String propId = rvaViewproperty.getPropId();
        String propSubId = rvaViewproperty.getPropSubId();

        String objValue = "rva_property:" + (RvaUtils.isNotEmpty(propSubId) ? propId + ":" + propSubId : propId);
        String viewValue = "rva_viewproperty:" + viewPropertyId;
        List<RvaMap<String, Object>> rvaMaps = dataMapper.selectList("SELECT role_id FROM sys_role WHERE property_blacklist LIKE '%" + objValue + "%'");
        String type = "";
        if (RvaUtils.isNotEmpty(rvaMaps) && rvaMaps.size() > 0) {
            type = "01";
        } else {
            rvaMaps = dataMapper.selectList("SELECT role_id FROM sys_role WHERE property_blacklist LIKE '%" + viewValue + "%'");
            if (RvaUtils.isNotEmpty(rvaMaps) && rvaMaps.size() > 0) {
                type = "02";
            }
        }
        ArrayList<String> roleIds = new ArrayList<>();
        for (RvaMap rvaMap : rvaMaps) {
            roleIds.add(rvaMap.getString("role_id"));
        }
        RvaView view = viewMapper.selectRvaViewById("c0_none_heimingdan");


        RvaMap formData = new RvaMap("c0_none_heimingdan_leixing", type).rvaPut("c0_none_heimingdan_jiaose", roleIds);
        return new RvaMap("viewData", view).rvaPut("formData", formData);
    }

    @Override
    public void deleteBlackList(String viewPropertyId) {
        RvaViewproperty rvaViewproperty = viewpropertyMapper.selectRvaViewpropertyById(viewPropertyId);
        String propId = rvaViewproperty.getPropId();
        String propSubId = rvaViewproperty.getPropSubId();
        String objValue = "rva_property:" + (RvaUtils.isNotEmpty(propSubId) ? propId + ":" + propSubId : propId);
        String viewValue = "rva_viewproperty:" + viewPropertyId;
        List<RvaMap<String, Object>> dbRvaMaps = dataMapper.selectList("SELECT role_id,property_blacklist FROM sys_role WHERE property_blacklist LIKE '%" + objValue + "%' or property_blacklist LIKE '%" + viewValue + "%'");
        for (RvaMap rvaMap : dbRvaMaps) {
            String dbRoleId = rvaMap.getString("role_id");
            String dbPropertyBlacklistStr = rvaMap.getString("property_blacklist");
            List<String> dbBlacklist = RvaUtils.asList(dbPropertyBlacklistStr.split(","));
            if (!dbBlacklist.remove(objValue)) {
                dbBlacklist.remove(viewValue);
            }
            dataMapper.updateWhereMap("sys_role",
                    new RvaMap<>("property_blacklist", RvaUtils.join(dbBlacklist)),
                    new RvaMap<>("role_id", dbRoleId),
                    true
            );
        }

    }

    @Override
    public void addButtonToMenu(String buttonId) {
        // 删除按钮对应的菜单及其子菜单
        String sql = String.format("select menu_id from sys_menu where perms = '%s'", buttonId);
        List<RvaMap<String, Object>> rvaMaps = dataMapper.selectList(sql);
        rvaMaps.forEach(m -> {
            deleteMenuAndChildren(m.getLong("menu_id"));
        });
        // 查询按钮视图对应的菜单
        RvaViewbutton viewbutton = viewbuttonMapper.selectRvaViewbuttonById(buttonId);
        RvaView view = viewMapper.selectRvaViewById(viewbutton.getViewId());
        SysMenu menu = new SysMenu();
        menu.setPerms(view.getId());
        List<SysMenu> menus = menuMapper.selectMenuList(menu);
        // 插入按钮菜单及其子菜单
        menus.forEach(m -> {
            SysMenu buttonMenu = createButtonMenu(viewbutton.getId(), 99, view.getName() + "-" + viewbutton.getName(), m.getMenuId());
            if (RvaUtils.isNotEmpty(viewbutton.getActionDialogViewId())) {
                RvaView v = viewMapper.selectRvaViewById(viewbutton.getActionDialogViewId());
                createViewButtonsMenu(buttonMenu.getMenuId(), v);
            }
            if (RvaUtils.isNotEmpty(viewbutton.getActionDialogAppId())) {
                createMenuAndButtonsByApp(viewbutton.getActionDialogAppId(), buttonMenu.getMenuId(), 99, true, false);
            }
        });

    }

    @Override
    public void synchronize(List<String> viewIds, List<String> vpIds) {
        viewIds.forEach(viewId -> {
            RvaView view = viewMapper.selectRvaViewById(viewId);
            vpIds.forEach(vpId -> {
                RvaViewproperty viewproperty = viewpropertyMapper.selectRvaViewpropertyById(vpId);
                RvaViewproperty vp = findProperty(view, viewproperty);
                if (vp == null) {
                    String id = copyViewPropertyOrButtonId(viewproperty.getId(), viewproperty.getViewId(), viewId);
                    RvaViewproperty vp1 = viewpropertyMapper.selectRvaViewpropertyById(id);
                    if (vp1 != null) {
                        id += RvaUtils.generateKey32(3);
                    }
                    viewproperty.setId(id);
                    viewproperty.setViewId(viewId);
                    viewpropertyMapper.insertRvaViewproperty(viewproperty);
                } else {
                    viewproperty.setId(vp.getId());
                    viewproperty.setViewId(viewId);
                    viewpropertyMapper.updateRvaViewproperty(viewproperty);
                }
            });
        });
    }

    private RvaViewproperty findProperty(RvaView view, RvaViewproperty viewproperty) {
        if (RvaUtils.isEmpty(viewproperty.getPropId()) && RvaUtils.isEmpty(viewproperty.getRelationId())) {
            return null;
        }
        for (int i = 0; i < view.getProperties().size(); i++) {
            RvaViewproperty vp = view.getProperties().get(i);
            if (RvaUtils.isEmpty(vp.getPropId()) && RvaUtils.isEmpty(vp.getRelationId())) {
                continue;
            }
            if (getString(vp).equals(getString(viewproperty))) {
                return vp;
            }
        }
        return null;
    }

    private static String getString(RvaViewproperty vp) {
        return vp.getPropId() + "==" + vp.getRelationId();
    }

    private RvaMap getMetaSqlData(String viewId, String sqlViewPropId, String sqlData) {
        RvaView view = viewMapper.selectRvaViewById(viewId);
        RvaMap formData = new RvaMap(sqlViewPropId, sqlData);
        return new RvaMap("viewData", view).rvaPut("formData", formData);
    }

    @Override
    public RvaMap getViewMetaSQL(String viewId) {
        StringBuffer stringBuffer = new StringBuffer();
        List<String> ids = new ArrayList<>();
        getViewMetaSQL(viewId, stringBuffer, ids);
        return getMetaSqlData("c0_none_shituyuanshujujiaoben", "c0_none_shituyuanshujujiaoben_jiaoben", stringBuffer.toString());
    }

}
