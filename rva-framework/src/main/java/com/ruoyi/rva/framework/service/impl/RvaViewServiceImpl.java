package com.ruoyi.rva.framework.service.impl;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.rva.framework.domain.*;
import com.ruoyi.rva.framework.extension.*;
import com.ruoyi.rva.framework.mapper.*;
import com.ruoyi.rva.framework.service.IRvaLogService;
import com.ruoyi.rva.framework.service.IRvaSystemService;
import com.ruoyi.rva.framework.service.IRvaViewService;
import com.ruoyi.rva.framework.service.IRvaViewpropertyService;
import com.ruoyi.rva.framework.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.ruoyi.framework.aspectj.DataScopeAspect.*;

/**
 * 视图Service业务层处理
 *
 * @author jiaodacailei
 * @date 2021-09-02
 */
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class RvaViewServiceImpl implements IRvaViewService {
    private final RvaDataMapper rvaDataMapper;

    private final RvaViewMapper rvaViewMapper;

    private final RvaObjectMapper rvaObjectMapper;

    private final RvaPropertyMapper rvaPropertyMapper;

    private final RvaRelationMapper rvaRelationMapper;

    private final RvaVelocityUtils velocityUtils;

    private final IRvaSystemService rvaSystemService;

    private final IRvaLogService logService;

    private final static String FORM_URL = "loadFormUrl";

    @Override
    public RvaView selectRvaViewById(String viewId, Map<String, Object> req) {
        RvaView rvaView = rvaViewMapper.selectRvaViewById(viewId);
        if (req.containsKey(FORM_URL)) {
            rvaView.setLoadUrl(req.get(FORM_URL).toString());
        }
        this.filterPropertyBlacklist(rvaView);
        return rvaView;
    }

    private void filterPropertyBlacklist(RvaView rvaView) {
        List<RvaViewproperty> properties = rvaView.getProperties();
        List<String> blacklist = rvaSystemService.getPropertyBlacklist();
        if (RvaUtils.isNotEmpty(properties) && RvaUtils.isNotEmpty(blacklist)) {
            ArrayList<RvaViewproperty> rvaViewproperties = new ArrayList<>(properties.size());
            rvaViewproperties.addAll(properties);
            int size = properties.size();
            for (int i = 0; i < size; i++) {
                RvaViewproperty p = properties.get(i);
                String id = p.getId();
                String propId = p.getPropId();
                String propSubId = p.getPropSubId();
                String objValue = "rva_property:" + (RvaUtils.isNotEmpty(propSubId) ? propId + ":" + propSubId : propId);
                String viewValue = "rva_viewproperty:" + id;
                if (blacklist.contains(objValue) || blacklist.contains(viewValue)) {
                    rvaViewproperties.remove(p);
                }
            }
            rvaView.setProperties(rvaViewproperties);
        }
    }

    @Override
    public RvaMap selectListViewData(String listId, String searchId, RvaMap req) {
        List<RvaMap<String, Object>> listData = selectList(listId, searchId, req);
        RvaView list = rvaViewMapper.selectRvaViewById(listId);
        return new RvaMap("viewData", list).rvaPut("listData", listData);
    }

    @Override
    public List<RvaMap<String, Object>> selectList(String listId, String searchId, RvaMap req, String... wheres) {
        RvaView list = rvaViewMapper.selectRvaViewById(listId);
        RvaView search = RvaUtils.isEmpty(searchId) ? null : rvaViewMapper.selectRvaViewById(searchId);
        RvaObject object = rvaObjectMapper.selectRvaObjectById(list.getObjId());
        Optional<RvaListSQLInterceptor> sqlInterceptor = RvaUtils.getBean(RvaListSQLInterceptor.BEAN_FREFIX + list.getId());
        if (RvaUtils.isNotEmpty(list.getLoadSql())) {
            RvaSQL rvaSQL = new RvaSQL();
            sqlInterceptor.ifPresent(e -> e.preHandle(rvaSQL, object, list, search, req));
            addWhere(req, object, list, search, rvaSQL);
            rvaSQL.where(wheres);
            addOrderBys(req, object, list, rvaSQL);
            sqlInterceptor.ifPresent(e -> e.postHandle(rvaSQL, object, list, search, req));
            String sql = velocityUtils.parseWithLoginUser(list.getLoadSql(), req) + " and " + rvaSQL.getWhere() + " order by " + rvaSQL.getOrderBys();
            log.info("selectList-loadSql:\n" + sql);
            return rvaDataMapper.selectList(sql);
        }
        // 构造sql
        RvaSQL sql = getListSQL(req, listId, searchId, wheres);

        // 获取主键、名称字段、上级字段对应的视图ID
        String keyPropId = null;
        String namePropId = null;
        String parentPropId = null;
        for (RvaViewproperty p : list.getProperties()) {
            RvaProperty prop = object.getProperty(p.getPropId());
            if (prop == null) {
                continue;
            }
            // 查找主键属性和名称属性
            if (object.isPrimaryKey(prop.getName())) {
                keyPropId = p.getId();
            }
            if (object.isNameProp(prop.getName())) {
                namePropId = p.getId();
            }
            if (object.isParentProp(prop.getName()) && list.isNestedTable()) {
                parentPropId = p.getId();
            }
        }

        // 设置分页信息
        if (parentPropId == null) {
            PageHelper.startPage(req.getInt("pageNum", 1), req.getInt("pageSize", 10));
        }
        // rvaSystemService.startPage();
        List<RvaMap<String, Object>> selectList = rvaDataMapper.selectList(sql.toString());

        if (parentPropId != null) {
            selectList = getNestedList(selectList, keyPropId, parentPropId);
        }

        // 后续处理，给每行数据加上id和name属性ID
        updateRows(req, list, keyPropId, namePropId, selectList);

        // 处理load_after_sql
        loadAfterSql(req, list.getLoadAfterSql(), selectList);
        return selectList;
    }

    private List<RvaMap<String, Object>> getNestedList(List<RvaMap<String, Object>> selectList, String keyPropId, String parentPropId) {
        List<RvaMap<String, Object>> nestedList = new ArrayList<>();
        for (RvaMap<String, Object> row : selectList) {
            if (!addToParent(row, keyPropId, parentPropId, selectList)) {
                nestedList.add(row);
            }
        }
        return nestedList;
    }

    private Boolean addToParent(RvaMap<String, Object> row2, String keyPropId, String parentPropId, List<RvaMap<String, Object>> selectList) {
        // 获取上级ID值
        String parentId = row2.getString(parentPropId);
        for (RvaMap<String, Object> row : selectList) {
            // 获取主键值
            String keyValue = row.getString(keyPropId);
            if (keyValue.equals(parentId)) {
                row.addToList("children", row2);
                return true;
            }
        }
        return false;
    }

    private void updateRows(RvaMap req, RvaView list, String keyPropId, String namePropId, List<RvaMap<String, Object>> selectList) {
        for (RvaMap<String, Object> row : selectList) {
            setKeyNameValue(keyPropId, namePropId, row);
            RvaMap velocityContext = new RvaMap<>(row).rvaPutAll(req).rvaPut("row", row).rvaPut("request", req);
            list.getProperties().forEach(prop -> {
                // 处理inner按钮
                handleButtonColumn(list, row, velocityContext, prop);
                // 处理formatter
                handleColumnFormatter(list, row, velocityContext, prop);
            });
            // 处理嵌套数据
            List<RvaMap<String, Object>> children = row.getList("children");
            if (children != null && children.size() > 0) {
                updateRows(req, list, keyPropId, namePropId, children);
            }
        }
    }

    public RvaSQL getListSQL(RvaMap req, String listId, String searchId, String... wheres) {
        // 获取元数据
        RvaView list = rvaViewMapper.selectRvaViewById(listId);
        RvaView search = RvaUtils.isEmpty(searchId) ? null : rvaViewMapper.selectRvaViewById(searchId);
        RvaObject object = rvaObjectMapper.selectRvaObjectById(list.getObjId());
        Optional<RvaListSQLInterceptor> sqlInterceptor = RvaUtils.getBean(RvaListSQLInterceptor.BEAN_FREFIX + list.getId());
        // 通过before_sql设置req
        loadBeforeSql(req, list.getLoadBeforeSql());
        RvaSQL sql = new RvaSQL();
        sqlInterceptor.ifPresent(e -> e.preHandle(sql, object, list, search, req));
        sql.from(object.getId(), object.getNo());
        sql.where(wheres);
        // 处理列表视图，生成查询字段和部分where条件
        for (RvaViewproperty p : list.getProperties()) {
            if (RvaUtils.isNotEmpty(p.getListExpression())) {
                sql.selectExpression(p.getListExpression(), p.getId());
                continue;
            }
            // 获取req中的值，用于where
            String value = req.getString(p.getId());
            RvaRelation relation = p.getRelation();
            if (relation == null) {
                RvaProperty prop = object.getProperty(p.getPropId());
                if (prop != null) {
                    sql.select(object.getNo(), prop.getName(), p.getPropSubId(), p.getId());
                    // 增加过滤条件
                    sql.whereEqOrLike(object.getNo(), prop.getName(), value);
                }
            } else {// relation != null
                RvaObject relatedObj = relation.getRelatedObj();
                // 此时，视图属性关联的属性，必须是relatedObj的属性，否则，就使用relatedObj.nameProperty
                RvaProperty relatedProp = RvaUtils.isEmpty(p.getPropId()) ? null : relatedObj.getProperty(p.getPropId());
                if (relatedProp == null) {
                    if (RvaUtils.isNotEmpty(p.getPropId())) {
                        RvaUtils.throwQueryException("属性", "属性ID=" + p.getPropId());
                    }
                    relatedProp = relatedObj.getNameProperty();
                }
                if (relation.isM21()) {
                    sql.select(p.getRelationId(), relatedProp.getName(), p.getPropSubId(), p.getId());
                    sql.leftJoin(relation.getRelatedObj().getId(), relation.getId(), String.format("%s.%s = %s.%s", object.getNo(), relation.getProp().getName(), relation.getId(), relation.getRelatedProp().getName()));
                    // 增加过滤条件
                    sql.whereEqOrLike(p.getRelationId(), relatedProp.getName(), value);
                } else if (relation.is12M()) {
                    // select group_concat(${relatedProp.name}) from ${relation.relatedObj.id} where ${relation.relatedProp.name} = ${object.no}.${relation.prop.name}
                    sql.selectExpression(String.format("(select group_concat(%s) from %s where %s = %s.%s)", relatedProp.getName(), relation.getRelatedObj().getId(), relation.getRelatedProp().getName(), object.getNo(), relation.getProp().getName()), p.getId());
                } else if (relation.isM2M()) {
                    // select group_concat(${relatedProp.name}) from ${relation.relatedObj.id} relatedObj left join ${relation.relationObj.id} relationObj on (relatedObj.${relation.relatedProp.name} = relationObj.${relation.relationInverseProp.name}) where relationObj.${relation.relationProp.name} = ${object.no}.${relation.prop.name}
                    String sqlTmpl = "(select group_concat(${relatedProp.name}) from ${relation.relatedObj.id} relatedObj left join ${relation.relationObj.id} relationObj on (relatedObj.${relation.relatedProp.name} = relationObj.${relation.relationInverseProp.name}) where relationObj.${relation.relationProp.name} = ${object.no}.${relation.prop.name})";
                    RvaMap<String, Object> context = new RvaMap<String, Object>("relatedProp", relatedProp).rvaPut("relation", relation).rvaPut("object", object);
                    String expression = velocityUtils.parse(sqlTmpl, context);
                    sql.selectExpression(expression, p.getId());
                }
            }
            sql.selectEmpty(p.getId());
        }
        // 处理对象属性，生成where条件
        object.getProperties().forEach(rvaProperty -> {
            sql.whereEqOrLike(object.getNo(), rvaProperty.getName(), req.getString(rvaProperty.getId()));
        });
        // 处理关联对象属性，生成where条件
        object.getRelations().forEach(relation -> {
            RvaRelationitem relationitem = relation.getItems().get(0);
            RvaObject relatedObject = rvaObjectMapper.selectRvaObjectById(relationitem.getRelatedObjId());
            relatedObject.getProperties().forEach(rvaProperty -> {
                sql.whereEqOrLike(relation.getId(), rvaProperty.getName(), req.getString(rvaProperty.getId()));
            });
        });
        // 处理查询视图，生成where条件
        if (search != null) {
            addWhere(req, object, list, search, sql);
        }
        // 处理load_where
        String loadWhere = null;
        String loadWhereAppend = "N";
        if (req.isNotEmpty("loadWhere")) {
            loadWhere = req.getString("loadWhere");
            loadWhereAppend = req.getString("loadWhereAppend", "N");
        } else if (req.isNotEmpty("rvaAppParams")) {
            RvaMap params = req.getMap("rvaAppParams");
            if (params.isNotEmpty("loadWhere")) {
                loadWhere = params.getString("loadWhere");
                loadWhereAppend = params.getString("loadWhereAppend", "N");
            }
        }
        if (RvaUtils.isNotEmpty(loadWhere) && loadWhere.indexOf(":") > 0) {
            if (loadWhere.startsWith(list.getId() + ":")) {
                loadWhere = loadWhere.substring(list.getId().length() + 1);
            } else {
                loadWhere = null;
            }
        }
        if (RvaUtils.isEmpty(loadWhere) || "Y".equals(loadWhereAppend)) {
            addWhere(req, list, search, object, sql, list.getLoadWhere());
        }
        addWhere(req, list, search, object, sql, loadWhere);
        //增加数据权限过滤
        addDataScope(sql, object);
        // 处理tenant
        if (object.hasPropTenant()) {
            sql.whereEqOrLike(object.getNo(), object.getPropNameTenant(), getCurrentTenantId());
        }
        // 处理排序
        addOrderBys(req, object, list, sql);
        if ("Y".equals(loadWhereAppend)) {
            sqlInterceptor.ifPresent(e -> e.postHandle(sql, object, list, search, req));
        }
        log.info("selectList:\n" + sql);
        return sql;
    }

    private void addWhere(RvaMap req, RvaView list, RvaView search, RvaObject object, RvaSQL sql, String loadWhere) {
        if (RvaUtils.isNotEmpty(loadWhere)) {
            String loadWhereSql = velocityUtils.parseWithLoginUser(loadWhere, req);
            int index = loadWhereSql.indexOf(' ');
            String beanName = loadWhereSql;
            if (index > 0) {
                beanName = loadWhereSql.substring(0, index);
            }
            Optional<RvaListSQLInterceptor> loadWhereSqlInterceptor = RvaUtils.getBean(beanName);
            if (loadWhereSqlInterceptor.isPresent()) {
                loadWhereSqlInterceptor.get().preHandle(sql, object, list, search, req);
                loadWhereSqlInterceptor.get().postHandle(sql, object, list, search, req);
                if (index > 0) {
                    loadWhereSql = loadWhereSql.substring(index + 1);
                    sql.where(loadWhereSql);
                }
            } else {
                sql.where(loadWhereSql);
            }
        }
    }

    private void addDataScope(RvaSQL sql, RvaObject object) {
        String tableAlias = object.getNo();
        SysUser loginUser = rvaSystemService.getLoginUser();
        String propNameDept = object.getPropNameDept();
        if (RvaUtils.isEmpty(propNameDept) || loginUser.isAdmin()) {
            return;
        }

        StringBuilder sqlString = new StringBuilder();

        for (SysRole role : loginUser.getRoles()) {
            String dataScope = role.getDataScope();
            if (DATA_SCOPE_ALL.equals(dataScope)) {
                sqlString = new StringBuilder();
                break;
            } else if (DATA_SCOPE_CUSTOM.equals(dataScope)) {
                sqlString.append(StringUtils.format(
                        " OR {}.{} IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ", tableAlias, propNameDept,
                        role.getRoleId()));
            } else if (DATA_SCOPE_DEPT.equals(dataScope)) {
                sqlString.append(StringUtils.format(" OR {}.{} = {} ", tableAlias, propNameDept, loginUser.getDeptId()));
            } else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope)) {
                sqlString.append(StringUtils.format(
                        " OR {}.{} IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )",
                        tableAlias, propNameDept, loginUser.getDeptId(), loginUser.getDeptId()));
            } else if (DATA_SCOPE_SELF.equals(dataScope)) {
                sqlString.append(StringUtils.format(" OR {}.create_by= {} ", tableAlias, loginUser.getUserId()));
            }
        }

        if (StringUtils.isNotBlank(sqlString.toString())) {
            sql.where(sqlString.substring(4));
        }

    }


    /**
     * 处理inner按钮
     *
     * @param list
     * @param map
     * @param velocityContext new RvaMap<>(map).rvaPutAll(req)
     * @param prop
     */
    private void handleButtonColumn(RvaView list, RvaMap<String, Object> map, RvaMap velocityContext, RvaViewproperty prop) {
        if (!RvaViewproperty.TYPE_BUTTON.equals(prop.getType()) || RvaUtils.isEmpty(prop.getListButtons())) {
            return;
        }
        String[] buttons = prop.getListButtons().split(",");
        List<RvaViewbutton> wanted = new ArrayList<>();
        for (String buttonId : buttons) {
            RvaViewbutton button = RvaUtils.cloneBySetter(list.getButton(buttonId), RvaViewbutton.class);
            if (velocityUtils.validateWithLoginUser(button.getShowIf(), velocityContext)) {
                wanted.add(button);
                button.setType(velocityUtils.parseWithLoginUser(button.getType(), velocityContext));
                button.setActionUrl(velocityUtils.parseWithLoginUser(button.getActionUrl(), velocityContext));
                button.setActionDialogAppId(velocityUtils.parseWithLoginUser(button.getActionDialogAppId(), velocityContext));
                button.setActionDialogViewId(velocityUtils.parseWithLoginUser(button.getActionDialogViewId(), velocityContext));
            }
        }
        map.put(prop.getId(), wanted);
    }

    /**
     * 处理formatter
     *
     * @param list            列表视图
     * @param row             列表行数据
     * @param velocityContext new RvaMap<>(row).rvaPutAll(req)
     * @param prop            列表视图属性
     */
    private void handleColumnFormatter(RvaView list, RvaMap<String, Object> row, RvaMap velocityContext, RvaViewproperty prop) {
        String formatter = prop.getParamFormatter();
        if (RvaUtils.isEmpty(formatter)) {
            return;
        }
        String value = row.getString(prop.getId());
        velocityContext = new RvaMap(velocityContext).rvaPut("currentPropValue", value);
        formatter = velocityUtils.parseWithLoginUser(formatter, velocityContext);
        if (RvaUtils.isEmpty(formatter)) {
            return;
        }
        String formattedValue = null;
        if (formatter.indexOf(' ') > 0) {// sql
            formattedValue = rvaDataMapper.selectString(formatter);
        } else {
            Optional<RvaListValueFormatter> listValueFormatter = RvaUtils.getBean(formatter, RvaListValueFormatter.class);
            if (listValueFormatter.isPresent()) {
                formattedValue = listValueFormatter.get().formatValue(list, row, velocityContext, prop, value);
            }
        }
        if (RvaUtils.isNotEmpty(formattedValue)) {
            row.put(prop.getId() + "__value", formattedValue);
        }
    }

    private void addWhere(RvaMap req, RvaObject object, RvaView list, RvaView search, RvaSQL sql) {
        for (RvaViewproperty viewproperty : search.getProperties()) {
            // 获取req中的值，用于where
            Object value = req.get(viewproperty.getId());
            if (RvaUtils.isEmpty(value)) {
                continue;
            }
            String expression = viewproperty.getSearchExpression();
            Optional<RvaListSQLInterceptor> bean = RvaUtils.getBean(expression);
            if (bean.isPresent()) {
                bean.get().preHandle(sql, object, list, search, new RvaMap(req).rvaPut("currentPropValue", value));
                bean.get().postHandle(sql, object, list, search, new RvaMap(req).rvaPut("currentPropValue", value));
                return;
            }
            if (RvaViewproperty.SEARCH_EQUAL.equals(viewproperty.getSearchType())) {
                expression = "${column} = '${value}'";
            } else if (RvaViewproperty.SEARCH_LIKE.equals(viewproperty.getSearchType())) {
                expression = "${column} like '%${value}%'";
            } else if (RvaViewproperty.SEARCH_NUMBER_RANGE.equals(viewproperty.getSearchType())) {//数值区间
                expression = RvaUtils.getFileString("vm/search.number.range.vm");
            } else if (RvaViewproperty.SEARCH_DATETIME_RANGE.equals(viewproperty.getSearchType())) {//日期时间区间
                expression = "${column} > '${value[0]}' and ${column} <=  '${value[1]}'";
            }
            RvaMap<String, Object> velocityData = new RvaMap<>("value", value);
            if (viewproperty.getPropId() != null) {
                RvaProperty property = rvaPropertyMapper.selectRvaPropertyById(viewproperty.getPropId());
                if (viewproperty.getRelationId() == null) {
                    velocityData.put("column", object.getNo() + "." + property.getColumn(viewproperty.getPropSubId()));
                } else {
                    RvaRelation relation = rvaRelationMapper.selectRvaRelationById(viewproperty.getRelationId());
                    if (relation.canLeftJoin()) {
                        velocityData.put("column", relation.getId() + "." + property.getColumn(viewproperty.getPropSubId()));
                    }
                }
            }
            String parse = velocityUtils.parseWithLoginUser(expression, velocityData);
            sql.where(parse);
        }
    }

    private void addOrderBys(RvaMap req, RvaObject object, RvaView list, RvaSQL sql) {
        List<Map> orderBys = req.getList(RvaConstants.PARAM_ORDER_BY);
        if (orderBys.size() == 0) {
            list.getProperties().stream().filter(p -> p.getListOrderIdx() >= 0).sorted(new Comparator<RvaViewproperty>() {
                @Override
                public int compare(RvaViewproperty o1, RvaViewproperty o2) {
                    return o1.getListOrderIdx() - o2.getListOrderIdx();
                }
            }).forEach(p -> {
                Map orderBy = new HashMap();
                orderBys.add(orderBy);
                orderBy.put("prop", p.getId());
                orderBy.put("order", p.getListOrderType().equals(RvaViewproperty.LIST_ORDER_ASC) ? "ascending" : "descending");
            });
        }
        addOrderBys(object, list, sql, orderBys);
    }

    private void addOrderBys(RvaObject object, RvaView list, RvaSQL sql, List<Map> orderBys) {
        for (Map orderBy : orderBys) {
            String prop = orderBy.get("prop").toString();
            Object order = orderBy.get("order");
            if (RvaUtils.isEmpty(order)) {
                continue;
            }
            RvaViewproperty viewproperty = list.getProperty(prop);
            if (viewproperty.getPropId() != null) {
                RvaProperty property = rvaPropertyMapper.selectRvaPropertyById(viewproperty.getPropId());
                if (viewproperty.getRelationId() == null) {
                    sql.orderByOnce(object.getNo(), property.getColumn(viewproperty.getPropSubId()), order.toString());
                } else {
                    RvaRelation relation = rvaRelationMapper.selectRvaRelationById(viewproperty.getRelationId());
                    if (relation.canLeftJoin()) {
                        sql.orderByOnce(relation.getId(), property.getColumn(viewproperty.getPropSubId()), order.toString());
                    }
                }
            }
        }
    }

    public void loadBeforeSql(RvaMap req, String loadBeforeSql) {
        if (RvaUtils.isEmpty(loadBeforeSql)) {
            return;
        }
        List<String> sqls = parseSqls(loadBeforeSql);
        for (String s : sqls) {
            String sql = velocityUtils.parseWithLoginUser(s, req);
            List<RvaMap<String, Object>> beforeList = rvaDataMapper.selectList(sql);
            for (RvaMap<String, Object> row : beforeList) {
                req.putAll(row);
            }
        }
    }

    public void loadAfterSql(RvaMap req, String loadAfterSql, List<RvaMap<String, Object>> selectList) {
        if (RvaUtils.isEmpty(loadAfterSql)) {
            return;
        }
        List<String> sqls = parseSqls(loadAfterSql);
        for (RvaMap<String, Object> row : selectList) {
            for (String sql : sqls) {
                String s = velocityUtils.parseWithLoginUser(sql, new RvaMap<>(req).rvaPutAll(row));
                List<RvaMap<String, Object>> rvaMaps = rvaDataMapper.selectList(s);
                for (RvaMap<String, Object> r : rvaMaps) {
                    row.putAll(r);
                }
            }
            List<RvaMap<String, Object>> children = row.getList("children");
            if (children != null && children.size() > 0) {
                loadAfterSql(req, loadAfterSql, children);
            }
        }
    }

    @Override
    public RvaMap selectCreateViewData(String viewId, RvaMap req) {
        RvaView view = rvaViewMapper.selectRvaViewById(viewId);
        loadBeforeSql(req, view.getLoadBeforeSql());
        // List<Map> selection = req.parseList("selection");

        RvaMap<String, Object> formData = new RvaMap<>();
        List<RvaMap<String, Object>> formDataList = new ArrayList<>();
        formDataList.add(formData);
        loadAfterSql(req, view.getLoadAfterSql(), formDataList);
        req.rvaPutAll(formData);
        view.getProperties().forEach(p -> {
            String val = null;
            if (RvaUtils.isNotEmpty(p.getPropId())) {
                val = req.getString(p.getPropId());
            }
            if (req.isNotEmpty(p.getId())) {
                val = req.getString(p.getId());
            }
            val = velocityUtils.parseWithLoginUser(val, req);
            val = getValueByInit(val, req, p);
            formData.put(p.getId(), val);
            setFormRelatedCrudData(null, formData, p, req, true, false);
            setFormMultipleValues(formData, p, val);
        });

        this.filterPropertyBlacklist(view);
        shieldProperties(view, formData, req);
        return new RvaMap("viewData", view).rvaPut("formData", formData);
    }

    @Override
    public RvaMap selectCloneViewData(String viewId, RvaMap req) {
        return selectUpdateViewData(viewId, req, true, true);
    }

    /**
     * 设置表单视图关联crud控件的值到formData中
     *
     * @param keyValue 加载表单视图ID属性值，create为true时，无须此参数，填null即可
     * @param formData 表单视图加载数据
     * @param p        表单视图属性
     * @param request
     * @param create   是否新建视图
     * @param clone    当前表单加载是否克隆
     */
    private void setFormRelatedCrudData(String keyValue, RvaMap<String, Object> formData, RvaViewproperty p, RvaMap request, Boolean create, Boolean clone) {
        if (RvaViewproperty.TYPE_CRUD.equals(p.getType())) {
            RvaMap req = new RvaMap(formData).rvaPutAll(request);
            List crudData = new ArrayList<>();
            RvaApp relatedApp = rvaAppMapper.selectRvaAppById(p.getFormRelatedCrud());
            if (!create) {
                String viewId = clone ? relatedApp.getCreateId() : relatedApp.getUpdateId();
                RvaView view = rvaViewMapper.selectRvaViewById(viewId);
                RvaRelation relation = p.getRelation();
                if (relation != null && relation.is12M()) {
                    RvaObject relatedObj = relation.getRelatedObj();
                    String orderProp = relatedObj.getPropNameIndex();
                    if (RvaUtils.isEmpty(orderProp)) {
                        orderProp = relatedObj.getKeyProperty().getName();
                    }
                    view.setLoadWhere(String.format("%s.%s = '%s' order by %s.%s", relatedObj.getNo(), relation.getRelatedProp().getName(), keyValue, relatedObj.getNo(), orderProp));
                }
                RvaMap updateViewData = selectUpdateViewData(view, req, clone, false);
                p.getRelatedCrudViewData().put(RvaView.FORM_UPDATE, updateViewData);
                crudData = (List) updateViewData.get("formDataList");
            }
            RvaMap createViewData = selectCreateViewData(relatedApp.getCreateId(), req);
            p.getRelatedCrudViewData().put(RvaView.FORM_CREATE, createViewData);
            Map createFormData = (Map) createViewData.get("formData");
            createFormData.put(RvaView.FORM_CREATE, true);
            crudData.add(createFormData);
            if (clone) {
                crudData.forEach(data -> ((Map) data).put(RvaView.FORM_CREATE, true));
            }
            formData.put(p.getId(), crudData);
        }
    }

    private void setFormMultipleValues(RvaMap<String, Object> formData, RvaViewproperty p, String val) {
        if (p.hasMultipleValues()) {
            List<String> vals = new ArrayList<>();
            if (RvaUtils.isNotEmpty(val)) {
                for (String s : val.split(",")) {
                    vals.add(s);
                }
            }
            formData.put(p.getId(), vals);
        }
        if (p.hasJsonArrayValue() && RvaUtils.isNotEmpty(val)) {
            formData.put(p.getId(), RvaJsonUtils.readAsList(val, String.class));
        }
    }

    /**
     * form加载时，根据FormInitValue替换值
     *
     * @param req
     * @param p
     * @return
     */
    private String getValueByInit(String val, RvaMap req, RvaViewproperty p) {
        if (RvaUtils.isNotEmpty(p.getFormInitValue())) {
            if (RvaUtils.isNotEmpty(val)) {
                if ("Y".equals(p.getFormInitReplace())) {
                    val = velocityUtils.parseWithLoginUser(p.getFormInitValue(), req);
                }
            } else {
                if ("Y".equals(p.getFormInitReplaceEmpty())) {
                    val = velocityUtils.parseWithLoginUser(p.getFormInitValue(), req);
                }
            }
        }
        return val;
    }

    @Override
    public String submitCreateView(String viewId, RvaMap req) {
        RvaView create = rvaViewMapper.selectRvaViewById(viewId);
        return submitCreateView(create, req);
    }

    private String submitCreateView(RvaView create, RvaMap req) {
        RvaObject object = rvaObjectMapper.selectRvaObjectById(create.getObjId());// create.getObjId()为none时，object为null
        RvaMap<String, Object> fieldValues = new RvaMap<>();
        Optional<RvaFormSubmitInterceptor> bean = RvaUtils.getBean(RvaFormSubmitInterceptor.BEAN_FREFIX + create.getId());
        bean.ifPresent(rvaFormSubmitInterceptor -> {
            rvaFormSubmitInterceptor.preHandle(fieldValues, object, create, req);
        });
        executeSubmitSql(req, create.getFormSubmitBeforeSql());
        Map<RvaRelation, RvaMap<String, Object>> m21FieldValues = new HashMap<>();
        List<String> rel12MUpdateSqls = new ArrayList<>();
        Map<RvaRelation, RvaMap<String, Object>> m2MFieldValues = new HashMap<>();
        create.getProperties().forEach(p -> {

            if ("Y".equals(p.getFormRequired()) && req.isEmpty(p.getId())) {
                RvaProperty prop = object.getProperty(p.getPropId());
                if (prop == null || !object.isPrimaryKey(prop.getName())) {
                    String requiredIf = p.getJsonPropertyString("requiredIf");
                    if (RvaUtils.isEmpty(requiredIf) || velocityUtils.validateWithLoginUser(requiredIf, req)) {
                        RvaUtils.throwRequiredException(p.getName());
                    }
                }
            }
            if (object == null) {
                return;
            }
            // formSubmit == Y 时，才提交
            if ("N".equals(p.getFormSubmit())) {
                return;
            }
            if (RvaViewproperty.TYPE_CRUD.equals(p.getType())) {
                return;
            }
            String formValue = getFormValue(req, p);
            RvaRelation relation = p.getRelation();
            if (relation == null) {// 关系未设置
                if (p.hasProperty()) {// 关系未设置，属性已设置
                    RvaProperty prop = object.getProperty(p.getPropId());
                    if (prop == null) {
                        RvaUtils.throwQueryException("属性", "属性ID=" + p.getPropId());
                    }
                    setFieldValues(fieldValues, p, prop.getName(), formValue);
                }
            } else if (RvaUtils.isNotEmpty(formValue)) {
                RvaObject relatedObj = relation.getRelatedObj();
                if (relation.is12M()) {
                    // 此时无法入库，需要本方对象的主键值，只能等本方对象入库后再处理
                    RvaProperty relatedProp = relation.getRelatedProp();
                    String sql = String.format("update %s set %s = '${id}' where %s in ('%s')",
                            relatedObj.getId(), relatedProp.getName(), relatedObj.getKeyProperty().getName(),
                            formValue.replace(",", "','"));
                    rel12MUpdateSqls.add(sql);
                } else if (relation.isM2M()) {
                    RvaMap<String, Object> fvs = new RvaMap<>();
                    m2MFieldValues.put(relation, fvs);
                    // insert关系表，需要本方对象的主键值，只能等本方对象入库后再处理
                    setFieldValues(fvs, p, relation.getRelationInverseProp().getName(), formValue);
                } else if (relation.isM21()) {
                    if (p.hasProperty()) {// 关系已设置，属性已设置
                        RvaProperty relatedProp = relatedObj.getProperty(p.getPropId());
                        if (relatedProp == null) {
                            RvaUtils.throwQueryException("属性", "属性ID=" + p.getPropId());
                        }
                        // 此时关系已设置，并且获取到relatedObj的属性
                        RvaMap<String, Object> fvs = m21FieldValues.get(relation);
                        if (fvs == null) {
                            fvs = new RvaMap<>();
                            m21FieldValues.put(relation, fvs);
                        }
                        // insert关联表，后面还需要将id回写到本对象的外键字段上
                        setFieldValues(fvs, p, relatedProp.getName(), formValue);
                    } else {// 关系已设置，属性未设置
                        fieldValues.put(relation.getProp().getName(), formValue);
                    }
                }
            }
        });

        String keyValue = null;
        if (object != null) {
            this.setDeptPropValue(fieldValues, create, object, req);
            // 处理多对一
            for (RvaRelation relation : m21FieldValues.keySet()) {
                RvaObject relatedObj = relation.getRelatedObj();
                RvaMap<String, Object> values = m21FieldValues.get(relation);
                insertFieldValues(relatedObj, values);
                fieldValues.put(relation.getProp().getName(), getKeyValue(relatedObj, values));
                // updateIndexValue(req, object);
            }
            insertFieldValues(object, fieldValues);
            // 处理一对多
            keyValue = getKeyValue(object, fieldValues);
            for (String sql : rel12MUpdateSqls) {
                rvaDataMapper.update(sql.replace("${id}", keyValue));
            }
            // 处理多对多
            for (RvaRelation relation : m2MFieldValues.keySet()) {
                RvaObject relationObj = relation.getRelationObj();
                RvaMap<String, Object> values = m2MFieldValues.get(relation);
                String[] vals = values.get(relation.getRelationInverseProp().getName()).toString().split(",");
                for (String val : vals) {
                    RvaMap<String, Object> fs = new RvaMap<>(relation.getRelationProp().getName(), keyValue);
                    fs.put(relation.getRelationInverseProp().getName(), val);
                    insert(relationObj.getId(), fs);
                }
            }
            updateIndexValue(keyValue, req, fieldValues, object);
        }
        processCrud(req, create, keyValue);
        executeSubmitSql(new RvaMap(RvaConstants.PROP_KEY_VALUE, keyValue).rvaPutAll(req), create.getFormSubmitAfterSql());
        bean.ifPresent(rvaFormSubmitInterceptor -> {
            rvaFormSubmitInterceptor.postHandle(fieldValues, object, create, req);
        });
        // 记录日志
        logService.insertRvaLog(create.getId(), keyValue, req);
        return keyValue;
    }

    private int insert(String tableName, Map<String, Object> fieldValues) {
        RvaObject object = rvaObjectMapper.selectRvaObjectById(tableName);
        if (object.hasPropTenant()) {
            String tenentId = getCurrentTenantId();
            fieldValues.put(object.getPropNameTenant(), tenentId);
        }
        return rvaDataMapper.insert(tableName, fieldValues);
    }

    private String getCurrentTenantId() {
        String tenentId = rvaSystemService.getLoginUser().getTenantId();
        if (RvaUtils.isEmpty(tenentId)) {
            tenentId = RvaTenant.DEFAULT;
        }
        return tenentId;
    }

    private void setDeptPropValue(RvaMap<String, Object> fieldValues, RvaView create, RvaObject object, RvaMap req) {
        String propNameDept = object.getPropNameDept();
        if (!RvaUtils.isEmpty(propNameDept)) {
            RvaProperty rvaProperty = object.getPropertyByName(object.getPropNameDept());
            String propertyId = rvaProperty.getId();
            RvaViewproperty propertyByObjectProperty = create.getPropertyByObjectProperty(propertyId);
            if (RvaUtils.isEmpty(propertyByObjectProperty) || RvaUtils.isEmpty(getFormValue(req, propertyByObjectProperty))) {
                Long deptId = rvaSystemService.getLoginUser().getDeptId();
                if (RvaUtils.isNotEmpty(deptId)) {
                    fieldValues.put(propNameDept, deptId);
                }
            }
        }
    }

    private String getKeyValue(RvaObject object, RvaMap<String, Object> fieldValues) {
        if (object == null) {
            return null;
        }
        return object.getKeyValue(fieldValues);
    }

    private void insertFieldValues(RvaObject object, RvaMap<String, Object> fieldValues) {
        RvaProperty keyProperty = object.getKeyProperty();
        String keyValue = fieldValues.getString(keyProperty.getName());
        if (RvaUtils.isEmpty(keyValue)) {
            if (RvaProperty.TYPE_VARCHAR.equals(keyProperty.getType())) {
                keyValue = object.getNo() + "_" + RvaUtils.generateKey32();
                fieldValues.put(keyProperty.getName(), keyValue);
            }
        } else if (keyProperty.isAutoIncrement()) {
            fieldValues = new RvaMap<>(fieldValues);
            fieldValues.remove(keyProperty.getName());
        }
        checkUnique(object, fieldValues, true);
        insert(object.getId(), fieldValues);
    }

    private void executeSubmitSql(RvaMap req, String submitSql) {
        parseSqls(submitSql).forEach(sql -> {
            sql = velocityUtils.parseWithLoginUser(sql, req);
            if (RvaUtils.isNotEmpty(sql)) {
                rvaDataMapper.update(sql);
            }
        });
    }

    private void updateIndexValue(String keyValue, RvaMap req, RvaMap fieldValues, RvaObject object) {
        if (object.hasPropIndex()) {
            RvaMap<String, Object> indexData = object.getRequestIndexData(req);
            for (String propName : indexData.keySet()) {
                if (fieldValues.containsKey(propName)) {
                    indexData.put(propName, fieldValues.get(propName));
                }
            }
            this.updateIndex(keyValue, object.getRequestIndexValue(req), object, indexData);
        }
    }

    // object.uniques == [["", ""],[""]]
    private void checkUnique(RvaObject object, RvaMap<String, Object> fieldValues, Boolean insert) {
        String uniques = object.getUniques();
        if (RvaUtils.isEmpty(uniques)) {
            return;
        }
        List<List> uniqueList = RvaJsonUtils.readAsList(uniques, List.class);
        for (List unique : uniqueList) {
            RvaSQL sql = new RvaSQL();
            sql.select("count(*)");
            sql.from(object.getId());
            for (Object prop : unique) {
                Object val = fieldValues.get(prop);
                if (val == null) {
                    sql.where(prop.toString() + " is null");
                } else {
                    sql.where(String.format(prop + " = '%s'", val));
                }
            }
            if (!insert) {
                for (String key : object.getPropNameKey().split(",")) {
                    sql.where(String.format("%s != '%s'", key, fieldValues.get(key)));
                }
            }
            Long count = rvaDataMapper.selectLong(sql.toString());
            if (count > 0) {
                throw new RuntimeException("违反唯一性约束：" + unique);
            }
        }
    }

    private void processCrud(RvaMap req, RvaView view, String keyValue) {
        view.getProperties().forEach(p -> {
            if (RvaViewproperty.TYPE_CRUD.equals(p.getType())) {
                List<Map> list = req.getList(p.getId());
                RvaApp relatedApp = rvaAppMapper.selectRvaAppById(p.getFormRelatedCrud());
                RvaRelation relation = p.getRelation();
                String createId = relatedApp.getCreateId();
                String updateId = relatedApp.getUpdateId();
                submitFormViews(req, keyValue, list, relation, createId, updateId);
            }
        });
    }

    private List<String> submitFormViews(RvaMap req, String keyValue, List<Map> list, RvaRelation relation, String createId, String updateId) {
        List<String> keyVals = new ArrayList<>();
        for (Map r : list) {
            RvaMap rowReq = new RvaMap(req).rvaPutAll(r);
            if (r.containsKey("create")) {
                RvaView create = rvaViewMapper.selectRvaViewById(createId);
                if (relation != null && relation.is12M()) {
                    RvaViewproperty vp = create.getPropertyByObjectProperty(relation.getRelatedProp().getId());
                    if (vp == null) {
                        RvaUtils.throwQueryException("视图属性", "属性ID=" + relation.getRelatedProp().getId());
                    }
                    rowReq.put(vp.getId(), keyValue);
                }
                keyVals.add(submitCreateView(create, rowReq));
            } else if (r.containsKey("del")) {
                deleteByUpdate(updateId, rowReq);
            } else {
                keyVals.add(submitUpdateView(updateId, rowReq));
            }
        }
        return keyVals;
    }

    private void deleteByUpdate(String updateId, RvaMap req) {
        RvaView update = rvaViewMapper.selectRvaViewById(updateId);
        RvaObject object = rvaObjectMapper.selectRvaObjectById(update.getObjId());
        Optional<RvaDeleteInterceptor> bean = RvaUtils.getBean(RvaDeleteInterceptor.BEAN_FREFIX + update.getId());
        update.getProperties().forEach(p -> {
            RvaProperty prop = object.getProperty(p.getPropId());
            if (prop == null) {
                return;
            }
            if (object.isPrimaryKey(prop.getName())) {
                String keyValue = req.getString(p.getId());
                bean.ifPresent(b -> {
                    if (b.preHandle(keyValue, null, object, update, req)) {
                        rvaDataMapper.deleteWhereMap(object.getId(), new RvaMap<>(prop.getName(), keyValue));
                    }
                });
                if (!bean.isPresent()) {
                    rvaDataMapper.deleteWhereMap(object.getId(), new RvaMap<>(prop.getName(), keyValue));
                }
                bean.ifPresent(b -> b.postHandle(keyValue, null, object, update, req));
            }
        });
    }

    /**
     * 获取form表单视图的视图属性p对应的提交值
     *
     * @param req 提交请求数据
     * @param p   视图属性
     * @return
     */
    private String getFormValue(RvaMap req, RvaViewproperty p) {
        // 根据FormSubmitValue替换提交值
        if (RvaUtils.isNotEmpty(p.getFormSubmitValue())) {
            String val = velocityUtils.parseWithLoginUser(p.getFormSubmitValue(), req);
            if (RvaUtils.isNotEmpty(req.get(p.getId()))) {
                if ("Y".equals(p.getFormSubmitReplace())) {
                    req.put(p.getId(), val);
                }
            } else {
                if ("Y".equals(p.getFormSubmitReplaceEmpty())) {
                    req.put(p.getId(), val);
                }
            }
        }
        if (p.hasMultipleValues()) {
            String join = RvaUtils.join(req.getList(p.getId()));
            if ("".equals(join)) {
                join = null;
            }
            return join;
        }
        if (p.hasJsonArrayValue()) {
            return RvaJsonUtils.writeAsString(req.getList(p.getId()));
        }
        return req.getString(p.getId());
    }

    private void setFieldValues(RvaMap<String, Object> fieldValues, RvaViewproperty p, String propName, String formValue) {
        if (RvaUtils.isEmpty(p.getPropSubId())) {
            fieldValues.put(propName, formValue);
            // } else if (RvaUtils.isNotEmpty(formValue)) {
        } else {
            Map map = (Map) fieldValues.get(propName);
            if (map == null) {
                map = new HashMap();
            }
            map.put(p.getPropSubId(), formValue == null ? "" : formValue);
            fieldValues.put(propName, map);
        }
    }

    @Override
    public RvaMap selectUpdateViewData(String viewId, RvaMap req, Boolean noDataThrow) {
        return selectUpdateViewData(viewId, req, false, noDataThrow);
    }

    private RvaMap selectUpdateViewData(String viewId, RvaMap req, Boolean clone, Boolean noDataThrow) {
        RvaView view = rvaViewMapper.selectRvaViewById(viewId);
        this.filterPropertyBlacklist(view);
        return selectUpdateViewData(view, req, clone, noDataThrow);
    }

    private RvaMap selectUpdateViewData(RvaView view, RvaMap req, Boolean clone, Boolean noDataThrow) {
        RvaObject object = rvaObjectMapper.selectRvaObjectById(view.getObjId());
        loadBeforeSql(req, view.getLoadBeforeSql());
        RvaSQL sql = new RvaSQL();
        sql.from(object.getId(), object.getNo());
        view.getProperties().forEach(p -> {
            if (RvaViewproperty.TYPE_CRUD.equals(p.getType())) {
                return;
            }
            RvaRelation relation = p.getRelation();
            if (relation == null) {
                RvaProperty prop = object.getProperty(p.getPropId());
                if (prop == null) {
                    return;
                }
                sql.select(object.getNo(), prop.getName(), p.getPropSubId(), p.getId());
                return;
            }
            // relation != null
            if (relation.isM21()) {
                if (p.hasProperty()) {
                    RvaProperty relatedProp = getRelatedProp(p, relation);
                    sql.leftJoin(relation.getRelatedObj().getId(), relation.getId(), String.format("%s.%s = %s.%s", object.getNo(), relation.getProp().getName(), relation.getId(), relation.getRelatedProp().getName()));
                    sql.select(relation.getId(), relatedProp.getName(), p.getPropSubId(), p.getId());
                } else {
                    sql.select(object.getNo(), relation.getProp().getName(), p.getPropSubId(), p.getId());
                }
                return;
            }
            RvaProperty relatedProp = null;
            if (p.hasProperty()) {
                relatedProp = getRelatedProp(p, relation);
            } else {
                relatedProp = relation.getRelatedObj().getKeyProperty();
            }
            if (relation.is12M()) {
                // select group_concat(${relatedProp.name}) from ${relation.relatedObj.id} where ${relation.relatedProp.name} = ${object.no}.${relation.prop.name}
                sql.selectExpression(String.format("(select group_concat(%s) from %s where %s = %s.%s)", relatedProp.getName(), relation.getRelatedObj().getId(), relation.getRelatedProp().getName(), object.getNo(), relation.getProp().getName()), p.getId());
            } else if (relation.isM2M()) {
                // select group_concat(${relatedProp.name}) from ${relation.relatedObj.id} relatedObj left join ${relation.relationObj.id} relationObj on (relatedObj.${relation.relatedProp.name} = relationObj.${relation.relationInverseProp.name}) where relationObj.${relation.relationProp.name} = ${object.no}.${relation.prop.name}
                String sqlTmpl = "(select group_concat(${relatedProp.name}) from ${relation.relatedObj.id} relatedObj left join ${relation.relationObj.id} relationObj on (relatedObj.${relation.relatedProp.name} = relationObj.${relation.relationInverseProp.name}) where relationObj.${relation.relationProp.name} = ${object.no}.${relation.prop.name})";
                RvaMap<String, Object> context = new RvaMap<String, Object>("relatedProp", relatedProp).rvaPut("relation", relation).rvaPut("object", object);
                String expression = velocityUtils.parse(sqlTmpl, context);
                sql.selectExpression(expression, p.getId());
            }
        });
        if (RvaUtils.isEmpty(view.getLoadWhere())) {
            // throw new RuntimeException("load_where不能为空！");
        } else {
            String where = velocityUtils.parseWithLoginUser(view.getLoadWhere(), req);
            sql.where(where);
        }
        List<RvaMap<String, Object>> formDataList = rvaDataMapper.selectList(sql.toString());
        if (formDataList.size() == 0) {
            if (noDataThrow) {
                throw new RuntimeException("未查询到数据！");
            }
            shieldProperties(view, null, req);
            return new RvaMap("viewData", view).rvaPut("formDataList", formDataList);
        }
        loadAfterSql(req, view.getLoadAfterSql(), formDataList);
        RvaViewproperty nameProperty = view.getNameProperty(object);
        String namePropId = nameProperty == null ? null : nameProperty.getId();
        String keyPropId = view.getKeyProperty(object).getId();
        for (RvaMap<String, Object> formData : formDataList) {
            setKeyNameValue(keyPropId, namePropId, formData);
            view.getProperties().forEach(p -> {
                String val = formData.getString(p.getId());
                val = getValueByInit(val, new RvaMap(req).rvaPutAll(formData), p);
                formData.put(p.getId(), val);
                String keyValue = formData.getString(keyPropId);
                setFormRelatedCrudData(keyValue, formData, p, req, false, clone);
                setFormMultipleValues(formData, p, val);
            });
        }

        shieldProperties(view, formDataList.get(0), req);

        // 获取日志数据
        if (view.isLogShow()) {
            for (RvaMap<String, Object> formData : formDataList) {
                List logList = logService.selectRvaLogList(formData.getString(keyPropId), view, formData, req);
                formData.put("logs", logList);
            }
        }
        return new RvaMap("viewData", view).rvaPut("formData", formDataList.get(0)).rvaPut("formDataList", formDataList);
    }

    private void shieldProperties(RvaView view, RvaMap<String, Object> formData, RvaMap<String, Object> req) {
        String shieldProperties = view.getShieldProperties();
        if (RvaUtils.isEmpty(shieldProperties)) {
            return;
        }
        String propIds = velocityUtils.parseWithLoginUser(shieldProperties, new RvaMap<>(formData == null ? new RvaMap<>() : formData).rvaPutAll(req));
        if (RvaUtils.isEmpty(propIds)) {
            return;
        }
        for (String propId : propIds.split(",")) {
            propId = propId.trim();
            RvaViewproperty property = view.getProperty(propId);
            if (property == null) {
                continue;
            }
            property.setType(RvaViewproperty.TYPE_HIDDEN);
        }
    }

    private RvaProperty getRelatedProp(RvaViewproperty p, RvaRelation relation) {
        RvaProperty relatedProp = relation.getRelatedObj().getProperty(p.getPropId());
        if (relatedProp == null) {
            RvaUtils.throwQueryException("属性", "属性ID=" + p.getPropId());
        }
        return relatedProp;
    }

    @Override
    public String submitUpdateView(String viewId, RvaMap req) {
        RvaView update = rvaViewMapper.selectRvaViewById(viewId);
        RvaObject object = rvaObjectMapper.selectRvaObjectById(update.getObjId());
        RvaMap<String, Object> fieldValues = new RvaMap<>();
        RvaMap<String, Object> where = new RvaMap<>();
        Optional<RvaFormSubmitInterceptor> bean = RvaUtils.getBean(RvaFormSubmitInterceptor.BEAN_FREFIX + update.getId());
        bean.ifPresent(rvaFormSubmitInterceptor -> {
            rvaFormSubmitInterceptor.preHandle(fieldValues, object, update, req);
        });
        executeSubmitSql(req, update.getFormSubmitBeforeSql());
        // 需要缓存的关系数据
        Map<RvaRelation, RvaMap<String, Object>> m21FieldValues = new HashMap<>();
        // 多对多关系
        List<String> relM2MDeleteSqls = new ArrayList<>();
        Map<RvaRelation, RvaMap<String, Object>> m2MFieldValues = new HashMap<>();
        List<String> rel12MUpdateSqls = new ArrayList<>();
        update.getProperties().forEach(p -> {
            if ("Y".equals(p.getFormRequired()) && req.isEmpty(p.getId())) {
                String requiredIf = p.getJsonPropertyString("requiredIf");
                if (RvaUtils.isEmpty(requiredIf) || velocityUtils.validateWithLoginUser(requiredIf, req)) {
                    RvaUtils.throwRequiredException(p.getName());
                }
            }
            // formSubmit == Y 时，才提交
            if ("N".equals(p.getFormSubmit())) {
                return;
            }
            if (RvaViewproperty.TYPE_CRUD.equals(p.getType())) {
                return;
            }
            String formValue = getFormValue(req, p);
            RvaRelation relation = p.getRelation();
            if (relation == null) {// 关系未设置
                if (p.hasProperty()) {// 关系未设置，属性已设置
                    RvaProperty prop = object.getProperty(p.getPropId());
                    if (prop == null) {
                        RvaUtils.throwQueryException("属性", "属性ID=" + p.getPropId());
                    }
                    if (object.isPrimaryKey(prop.getName())) {
                        if (req.isEmpty(p.getId())) {
                            throw new RuntimeException("主键值不能为空！");
                        }
                        where.put(prop.getName(), req.getString(p.getId()));
                    } else {
                        setFieldValues(fieldValues, p, prop.getName(), formValue);
                    }
                }
            } else {
                RvaObject relatedObj = relation.getRelatedObj();
                if (relation.isM21()) {
                    if (p.hasProperty()) {// 关系已设置，属性已设置
                        RvaProperty relatedProp = relatedObj.getProperty(p.getPropId());
                        if (relatedProp == null) {
                            RvaUtils.throwQueryException("属性", "属性ID=" + p.getPropId());
                        }
                        // 此时关系已设置，并且获取到relatedObj的属性
                        RvaMap<String, Object> fvs = m21FieldValues.get(relation);
                        if (fvs == null) {
                            fvs = new RvaMap<>();
                            m21FieldValues.put(relation, fvs);
                        }
                        // update关联表，此时暂不更新，后续统一更新
                        setFieldValues(fvs, p, relatedProp.getName(), formValue);
                    } else {// 关系已设置，属性未设置
                        fieldValues.put(relation.getProp().getName(), formValue);
                    }
                } else if (relation.is12M()) {
                    // 清除关系数据，此时无法入库，需要本方对象的主键值
                    RvaProperty relatedProp = relation.getRelatedProp();
                    String sql = String.format("update %s set %s = null where %s = '${id}'",
                            relatedObj.getId(), relatedProp.getName(), relatedProp.getName());
                    rel12MUpdateSqls.add(sql);
                    if (RvaUtils.isNotEmpty(formValue)) {
                        sql = String.format("update %s set %s = '${id}' where %s in ('%s')",
                                relatedObj.getId(), relatedProp.getName(), relatedObj.getKeyProperty().getName(),
                                formValue.replace(",", "','"));
                        rel12MUpdateSqls.add(sql);
                    }
                } else if (relation.isM2M()) {
                    // 清除关系数据，此时无法入库，需要本方对象的主键值
                    String sql = String.format("delete from %s where %s = '${id}'",
                            relation.getRelationObj().getId(), relation.getRelationProp().getName());
                    relM2MDeleteSqls.add(sql);
                    if (RvaUtils.isNotEmpty(formValue)) {
                        RvaMap<String, Object> fvs = new RvaMap<>();
                        m2MFieldValues.put(relation, fvs);
                        // insert关系表，需要本方对象的主键值，只能等本方对象入库后再处理
                        setFieldValues(fvs, p, relation.getRelationInverseProp().getName(), formValue);
                    }
                }

            }
        });
        String keyValue = where.getString(object.getKeyProperty().getName());
        if (RvaUtils.isEmpty(keyValue)) {
            throw new RuntimeException("主键值不能为空！");
        }
        // 处理多对一
        for (RvaRelation relation : m21FieldValues.keySet()) {
            RvaObject relatedObj = relation.getRelatedObj();
            RvaMap<String, Object> values = m21FieldValues.get(relation);
            checkUnique(relatedObj, values, false);
            String updateWhereSql = String.format("%s = (select %s from %s where %s = '%s')", relatedObj.getKeyProperty().getName(), relation.getProp().getName(), object.getId(), object.getKeyProperty().getName(), keyValue);
            rvaDataMapper.updateWhereSql(relatedObj.getId(), values, updateWhereSql, true);
        }
        // 处理一对多
        for (String sql : rel12MUpdateSqls) {
            rvaDataMapper.update(sql.replace("${id}", keyValue));
        }
        // 处理多对多：清除已有关系数据
        for (String sql : relM2MDeleteSqls) {
            rvaDataMapper.update(sql.replace("${id}", keyValue));
        }
        // 处理多对多：插入关系数据
        for (RvaRelation relation : m2MFieldValues.keySet()) {
            RvaObject relationObj = relation.getRelationObj();
            RvaMap<String, Object> values = m2MFieldValues.get(relation);
            String[] vals = values.get(relation.getRelationInverseProp().getName()).toString().split(",");
            for (String val : vals) {
                RvaMap<String, Object> fs = new RvaMap<>(relation.getRelationProp().getName(), keyValue);
                fs.put(relation.getRelationInverseProp().getName(), val);
                insert(relationObj.getId(), fs);
            }
        }
        // 本方数据入库
        checkUnique(object, new RvaMap<>(fieldValues).rvaPutAll(where), false);
        rvaDataMapper.updateWhereMap(object.getId(), fieldValues, where, true);
        updateIndexValue(keyValue, req, fieldValues, object);
        processCrud(req, update, keyValue);
        executeSubmitSql(new RvaMap(RvaConstants.PROP_KEY_VALUE, keyValue).rvaPutAll(req), update.getFormSubmitAfterSql());
        bean.ifPresent(rvaFormSubmitInterceptor -> {
            rvaFormSubmitInterceptor.postHandle(fieldValues, object, update, req);
        });
        // 记录日志
        logService.insertRvaLog(update.getId(), keyValue, req);
        return keyValue;
    }

    @Override
    public void delete(String viewId, RvaMap req) {
        if (req.isEmpty("selection")) {
            throw new RuntimeException("请至少选择一行数据！");
        }
        RvaView view = rvaViewMapper.selectRvaViewById(viewId);
        RvaObject object = rvaObjectMapper.selectRvaObjectById(view.getObjId());
        Optional<RvaDeleteInterceptor> bean = RvaUtils.getBean(RvaDeleteInterceptor.BEAN_FREFIX + view.getId());
        List<Map<String, Object>> selectionKeyValues = getSelectionValues(viewId, req, Arrays.asList(object.getPropNameKey()));
        for (int i = 0; i < selectionKeyValues.size(); i++) {
            String keyValue = selectionKeyValues.get(i).get(object.getPropNameKey()).toString();
            Map selection = (Map) req.getList("selection").get(i);
            Map<String, Object> whereMap = new RvaMap<>();
            whereMap.put(object.getPropNameKey(), keyValue);
            bean.ifPresent(b -> {
                if (b.preHandle(keyValue, selection, object, view, req)) {
                    executeSubmitSql(new RvaMap(selection).rvaPutAll(req), view.getFormSubmitBeforeSql());
                    rvaDataMapper.deleteWhereMap(object.getId(), whereMap);
                    logService.deleteRvaLog(object.getId(), keyValue, view.getLogTable());
                }
            });
            if (!bean.isPresent()) {
                executeSubmitSql(new RvaMap(selection).rvaPutAll(req), view.getFormSubmitBeforeSql());
                rvaDataMapper.deleteWhereMap(object.getId(), whereMap);
                logService.deleteRvaLog(object.getId(), keyValue, view.getLogTable());
            }
            // this.updateIndex (null, null, object, new RvaMap<>(selection));
            executeSubmitSql(new RvaMap(selection).rvaPutAll(req), view.getFormSubmitAfterSql());
            bean.ifPresent(b -> b.postHandle(keyValue, selection, object, view, req));
        }
    }

    @Override
    public RvaMap selectFormsViewData(String createView, String updateView, RvaMap req) {
        RvaMap create = selectCreateViewData(createView, req);
        create.getMap("formData").put(RvaView.FORM_CREATE, true);
        RvaMap update = selectUpdateViewData(updateView, req, false);
        return new RvaMap(RvaView.FORM_CREATE, create).rvaPut(RvaView.FORM_UPDATE, update);
    }

    @Override
    public List<String> submitFormViews(String createView, String updateView, RvaMap req) {
        return submitFormViews(req, null, req.getList(createView), null, createView, updateView);
    }

    private List<Map<String, Object>> getSelectionValues(String viewId, RvaMap rvaMap, List<String> propNames) {
        List<Map<String, Object>> values = new ArrayList<>();
        RvaView list = rvaViewMapper.selectRvaViewById(viewId);
        RvaObject object = rvaObjectMapper.selectRvaObjectById(list.getObjId());
        rvaMap.getList("selection").forEach(selection -> {
            Map<String, Object> value = new HashMap<>();
            values.add(value);
            list.getProperties().forEach(p -> {
                RvaProperty prop = object.getProperty(p.getPropId());
                if (prop == null) {
                    return;
                }
                if (propNames.contains(prop.getName())) {
                    value.put(prop.getName(), ((Map) selection).get(p.getId()));
                }
            });
        });
        return values;
    }

    @Override
    public void moveUp(String viewId, RvaMap req) {
        move(viewId, req, true);
    }

    private void move(String viewId, RvaMap rvaMap, Boolean moveUp) {
        if (rvaMap.getList("selection").size() < 1 || rvaMap.getList("selection").size() > 2) {
            throw new RuntimeException("请选择一行或两行数据！");
        }
        RvaView list = rvaViewMapper.selectRvaViewById(viewId);
        RvaObject object = rvaObjectMapper.selectRvaObjectById(list.getObjId());
        if (object.hasNotPropIndex()) {
            throw new RuntimeException(String.format("%s 没有配置索引字段！", object.getName()));
        }
        List<String> propNames = new RvaList<String>(object.getPropNameKey(), object.getPropNameIndex())
                .rvaAddAll(object.getPropNameIndexWhere()); // 条件字段，propNameIndex对应的字段，是在条件字段在分组中进行排序的
        List<Map<String, Object>> selectionValues = getSelectionValues(viewId, rvaMap, propNames);
        Map<String, Object> selectionValue = selectionValues.get(0);
        List<RvaMap<String, Object>> selectList = getIndexList(object, propNames, selectionValue);
        if (selectionValues.size() == 1) {
            for (int i = 0; i < selectList.size(); i++) {
                RvaMap<String, Object> row = selectList.get(i);
                if (row.getString(propNames.get(0)).equals(selectionValue.get(propNames.get(0)).toString())) {
                    if (i > 0 && moveUp) {
                        RvaMap<String, Object> row2 = moveUp(propNames, selectList, i, row);
                        updateRowIndex(object, propNames, row);
                        updateRowIndex(object, propNames, row2);
                        break;
                    }
                    if (i < selectList.size() - 1 && !moveUp) {
                        RvaMap<String, Object> row2 = moveDown(propNames, selectList, i, row);
                        updateRowIndex(object, propNames, row);
                        updateRowIndex(object, propNames, row2);
                        break;
                    }
                }
            }
        } else {
            Map<String, Object> selectionValue2 = selectionValues.get(1);
            int index = 0, index2 = 0;
            for (int i = 0; i < selectList.size(); i++) {
                RvaMap<String, Object> row = selectList.get(i);
                String keyValue = row.getString(propNames.get(0));
                if (keyValue.equals(selectionValue.get(propNames.get(0)).toString())) {
                    index = i;
                }
                if (keyValue.equals(selectionValue2.get(propNames.get(0)).toString())) {
                    index2 = i;
                }
            }
            if (moveUp) {
                moveUpTo(selectList, index, index2);
            } else {
                moveDownTo(selectList, index, index2);
            }
            updateAllIndexes(object, propNames, selectList);
        }
    }

    private void updateAllIndexes(RvaObject object, List<String> propNames, List<RvaMap<String, Object>> selectList) {
        for (int i = 0; i < selectList.size(); i++) {
            RvaMap<String, Object> row = selectList.get(i);
            row.put(propNames.get(1), i);
            updateRowIndex(object, propNames, row);
        }
    }

    /**
     * @param propNames
     * @param selectList
     * @param i          当前行在selectList中的位置
     * @param row
     * @return
     */
    private RvaMap<String, Object> moveUp(List<String> propNames, List<RvaMap<String, Object>> selectList, int i, RvaMap<String, Object> row) {
        RvaMap<String, Object> prevRow = selectList.get(i - 1);
        String prevIndex = prevRow.get(propNames.get(1)).toString();
        prevRow.put(propNames.get(1), row.get(propNames.get(1)));
        row.put(propNames.get(1), prevIndex);
        return prevRow;
    }

    private RvaMap<String, Object> moveDown(List<String> propNames, List<RvaMap<String, Object>> selectList, int i, RvaMap<String, Object> row) {
        RvaMap<String, Object> nextRow = selectList.get(i + 1);
        String nextIndex = nextRow.get(propNames.get(1)).toString();
        nextRow.put(propNames.get(1), row.get(propNames.get(1)));
        row.put(propNames.get(1), nextIndex);
        return nextRow;
    }

    /**
     * selectList中位置Math.max(index, index2)的行，移动到Math.min(index, index2)的位置之前
     *
     * @param selectList
     * @param index
     * @param index2
     */
    private void moveUpTo(List<RvaMap<String, Object>> selectList, int index, int index2) {
        int min = Math.min(index, index2);
        int max = Math.max(index, index2);
        RvaMap<String, Object> row = selectList.get(max);
        selectList.remove(max);
        selectList.add(min, row);
    }

    /**
     * selectList中位置Math.min(index, index2)的行，移动到Math.max(index, index2)的位置之后
     *
     * @param selectList
     * @param index
     * @param index2
     */
    private void moveDownTo(List<RvaMap<String, Object>> selectList, int index, int index2) {
        int min = Math.min(index, index2);
        int max = Math.max(index, index2);
        RvaMap<String, Object> row = selectList.get(min);
        selectList.add(max + 1, row);
        selectList.remove(min);
    }

    private void updateIndex(String currentKeyValue, String currentIndexValue, RvaObject object, RvaMap<String, Object> selectionValue) {
        List<String> propNames = new RvaList<String>(object.getPropNameKey(), object.getPropNameIndex())
                .rvaAddAll(object.getPropNameIndexWhere()); // 条件字段，propNameIndex对应的字段，是在条件字段在分组中进行排序的
        List<RvaMap<String, Object>> indexList = getIndexList(object, propNames, selectionValue);
        Iterator<RvaMap<String, Object>> iterator = indexList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getString(propNames.get(0)).equals(currentKeyValue)) {
                iterator.remove();
            }
        }
        if (RvaUtils.isNotEmpty(currentKeyValue)) {
            RvaMap current = new RvaMap(propNames.get(0), currentKeyValue);
            Integer currentIndex = RvaUtils.getInt(currentIndexValue, -1);
            if (currentIndex < 0 || indexList.size() == 0) {
                indexList.add(current);
            } else {
                int max = 0;
                for (int i = 0; i < indexList.size(); i++) {
                    RvaMap<String, Object> row = indexList.get(i);
                    if (currentIndex >= i) {
                        max = i;
                    }
                }
                if (currentIndex > max) {
                    indexList.add(current);
                } else {
                    indexList.add(max, current);
                }
            }
        }
        updateAllIndexes(object, propNames, indexList);
    }

    private List<RvaMap<String, Object>> getIndexList(RvaObject object, List<String> propNames, Map<String, Object> selectionVal) {
        RvaSQL sql = new RvaSQL();
        sql.from(object.getId(), object.getNo());
        sql.select(object.getNo(), propNames.get(0));// 主键
        sql.select(object.getNo(), propNames.get(1));// 索引列
        RvaMap selectionValue = new RvaMap(selectionVal);
        for (int i = 2; i < propNames.size(); i++) {
            sql.where(String.format("%s.%s = '%s'", object.getNo(), propNames.get(i), selectionValue.get(propNames.get(i)).toString()));
        }
        sql.orderBy(String.format("%s.%s ASC", object.getNo(), propNames.get(1)));
        List<RvaMap<String, Object>> selectList = rvaDataMapper.selectList(sql.toString());
        return selectList;
    }

    private void updateRowIndex(RvaObject object, List<String> propNames, RvaMap<String, Object> row) {
        rvaDataMapper.updateWhereMap(object.getId(), new RvaMap<>(propNames.get(1), row.getString(propNames.get(1))),
                new RvaMap<>(propNames.get(0), row.getString(propNames.get(0))), true);
    }

    @Override
    public void moveDown(String viewId, RvaMap req) {
        move(viewId, req, false);
    }

    @Override
    public RvaMap<String, Object> selectSummariesData(String listId, String searchId, RvaMap req, String... wheres) {
        RvaView list = rvaViewMapper.selectRvaViewById(listId);
        RvaSQL listSQL = getListSQL(req, listId, searchId, wheres);
        listSQL.clearSelects();
        listSQL.clearOrderBys();
        RvaMap<String, Object> listFieldsExpression = list.getListFieldsExpression();
        for (RvaViewproperty p : list.getProperties()) {
            String viewPropertyId = p.getId();
            String formula = p.getJsonPropertyString("summariesExpression");
            if (RvaUtils.isEmpty(formula)) {
                continue;
            }
            String column = velocityUtils.parseWithLoginUser(formula, new RvaMap<>(listFieldsExpression).rvaPut("column", listFieldsExpression.get(viewPropertyId)));
            listSQL.select(column + " as " + viewPropertyId);
        }
        if (RvaUtils.isNotEmpty(listSQL)) {
            List<RvaMap<String, Object>> rvaMaps = rvaDataMapper.selectList(listSQL.toString());
            return rvaMaps.isEmpty() ? null : rvaMaps.get(0);
        }
        return null;

    }

    @Override
    public void importExcel(String filePath, String createViewId, RvaMap rvaMap) {
        // 资源路径
        String filePrefix = rvaSystemService.getFilePrefix();
        // 数据库资源地址
        String downloadPath = filePrefix + filePath;
        Workbook book = RvaExcelUtil.getBookByUrl(downloadPath);
        List<Map<String, Object>> data = RvaExcelUtil.getDataWithColumnIndex(book.getSheetAt(0));
        RvaView view = rvaViewMapper.selectRvaViewById(createViewId);
        List<RvaViewproperty> propertiesWithoutHidden = view.getPropertiesWithoutHidden();
        RvaMap viewFormData = selectCreateViewData(view.getId(), rvaMap);
        data.forEach(map -> {
            RvaMap<String, Object> req = new RvaMap<>(viewFormData.getMap("formData"));
            map.keySet().forEach(key -> {
                RvaExcelUtil.each(map, (index, k, v) -> {
                    if (index >= propertiesWithoutHidden.size()) {
                        return;
                    }
                    RvaViewproperty viewproperty = propertiesWithoutHidden.get(index);
                    req.put(viewproperty.getId(), v);
                });
            });
            this.submitCreateView(createViewId, req);
        });
    }

    @Override
    public void print(HttpServletResponse response, String beanName, RvaMap<String, Object> req) {
        Optional<RvaPrinterInterceptor> bean = RvaUtils.getBean(RvaPrinterInterceptor.BEAN_FREFIX + beanName);
        try {
            String html = bean.get().getHtml(req);
            response.setContentType("text/html");
            response.setCharacterEncoding("utf-8");
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(html.getBytes());
            outputStream.close();
        } catch (IOException e) {
            RvaUtils.throwRuntimeException("生成打印单错误", e);
            RvaUtils.throwRuntimeException(e.getMessage(), e);
            RvaUtils.throwRuntimeException("生成打印单错误", e);
        }
    }


    @Override
    public void hide(String viewId, RvaMap req) {
        if (req.isEmpty("selection")) {
            throw new RuntimeException("请至少选择一行数据！");
        }
        RvaView view = rvaViewMapper.selectRvaViewById(viewId);
        RvaObject object = rvaObjectMapper.selectRvaObjectById(view.getObjId());
        List<Map<String, Object>> selectionKeyValues = getSelectionValues(viewId, req, Arrays.asList(object.getPropNameKey()));

        ArrayList<String> ids = new ArrayList<>();
        for (Map map : selectionKeyValues) {
            RvaMap rvaMap = new RvaMap<>(map);
            String id = rvaMap.getString("id");
            ids.add("'" + id + "'");
        }
        String idsStr = RvaUtils.join(ids, ",");
        String sql = "UPDATE rva_viewproperty SET type = '%s' WHERE id IN (%s)";
        rvaDataMapper.update(String.format(sql, RvaViewproperty.TYPE_HIDDEN, idsStr));
    }

    private final RvaAppMapper rvaAppMapper;
}
