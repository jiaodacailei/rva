package com.ruoyi.rva.fe.service.impl;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.rva.fe.domain.RvaFeList;
import com.ruoyi.rva.fe.domain.RvaFeListItem;
import com.ruoyi.rva.fe.extension.RvaFeListSQLInterceptor;
import com.ruoyi.rva.fe.mapper.RvaFeListMapper;
import com.ruoyi.rva.fe.service.IRvaFeListRuntimeService;
import com.ruoyi.rva.framework.domain.*;
import com.ruoyi.rva.framework.extension.RvaListSQLInterceptor;
import com.ruoyi.rva.framework.mapper.RvaDataMapper;
import com.ruoyi.rva.framework.mapper.RvaObjectMapper;
import com.ruoyi.rva.framework.mapper.RvaPropertyMapper;
import com.ruoyi.rva.framework.mapper.RvaRelationMapper;
import com.ruoyi.rva.framework.service.IRvaViewService;
import com.ruoyi.rva.framework.util.RvaConstants;
import com.ruoyi.rva.framework.util.RvaUtils;
import com.ruoyi.rva.framework.util.RvaVelocityUtils;
import com.ruoyi.system.service.ISysDictDataService;
import com.ruoyi.system.service.ISysDictTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaFeListRuntimeServiceImpl implements IRvaFeListRuntimeService {

    private final RvaDataMapper rvaDataMapper;

    private final RvaFeListMapper listMapper;

    private final RvaObjectMapper rvaObjectMapper;

    private final RvaVelocityUtils velocityUtils;

    private final IRvaViewService viewService;

    private final RvaPropertyMapper rvaPropertyMapper;

    private final RvaRelationMapper rvaRelationMapper;

    @Override
    public List<RvaMap<String, Object>> selectList(String listId, RvaMap req, String... wheres) {
        RvaFeList list = listMapper.selectRvaFeListById(listId);
        RvaObject object = rvaObjectMapper.selectRvaObjectById(list.getObjId());
        Optional<RvaFeListSQLInterceptor> sqlInterceptor = RvaUtils.getBean(RvaFeListSQLInterceptor.BEAN_FREFIX + list.getId());
        RvaSQL sql = new RvaSQL();
        String category = req.getString("category");
        if (RvaUtils.isNotEmpty(category)) {
            Optional<RvaFeListSQLInterceptor> bean = RvaUtils.getBean(category);
            RvaMap reqData = new RvaMap(req);
            if (bean.isPresent()) {// a. bean
                bean.get().preHandle(sql, object, list, reqData);
                bean.get().postHandle(sql, object, list, reqData);
            } else if (category.indexOf(object.getNo() + ".") >= 0) {// b. sql where
                String where = velocityUtils.parseWithLoginUser(category, reqData);
                sql.where(where);
            } else if (RvaUtils.isNotEmpty(list.getPropNameCategory())) {// c. propNameCategory
                sql.where(object.getNo() + "." + list.getPropNameCategory() + " like '%"+ category + "%'");
            }
        }
        if (RvaUtils.isNotEmpty(list.getLoadSql())) {
            sqlInterceptor.ifPresent(e -> e.preHandle(sql, object, list, req));
            addWhere(req, object, list, sql);
            sql.where(wheres);
            addOrderBys(req, object, list, sql);
            sqlInterceptor.ifPresent(e -> e.postHandle(sql, object, list, req));
            String sqlStr = velocityUtils.parseWithLoginUser(list.getLoadSql(), req) + " and " + sql.getWhere() + " order by " + sql.getOrderBys();
            log.info("selectList-loadSql:\n" + sqlStr);
            return rvaDataMapper.selectList(sqlStr);
        }
        // 处理load_before_sql
        viewService.loadBeforeSql(req, list.getLoadBeforeSql());
        sqlInterceptor.ifPresent(e -> e.preHandle(sql, object, list, req));
        sql.from(object.getId(), object.getNo());
        sql.where(wheres);
        String keyPropId = null;
        String namePropId = null;
        // 处理列表视图，生成查询字段和部分where条件
        for (RvaFeListItem listItem : list.getItems()) {
            // 获取req中的值，用于where
            String value = req.getString(listItem.getId());
            if (listItem.getPropId() != null) {
                RvaProperty property = rvaPropertyMapper.selectRvaPropertyById(listItem.getPropId());
                sql.select(object.getNo(), property.getName(), listItem.getPropSubId(), listItem.getId());
                if (listItem.getRelationId() == null) {
                    sql.whereEqOrLike(object.getNo(), property.getName(), value);
                    if (object.isPrimaryKey(property.getName())) {
                        keyPropId = listItem.getId();
                    }
                    if (object.isNameProp(property.getName())) {
                        namePropId = listItem.getId();
                    }
                }
            }
            if (listItem.getRelationId() != null) {
                RvaRelation relation = rvaRelationMapper.selectRvaRelationById(listItem.getRelationId());
                if (relation.canLeftJoin()) {
                    RvaRelationitem relationitem = relation.getItems().get(0);
                    RvaObject relatedObject = rvaObjectMapper.selectRvaObjectById(relationitem.getRelatedObjId());
                    String propName = object.getProperty(relationitem.getPropId()).getName();
                    String relatedPropName = relatedObject.getProperty(relationitem.getRelatedPropId()).getName();
                    sql.leftJoin(relatedObject.getId(), relation.getId(), String.format("%s.%s = %s.%s", object.getNo(), propName, relation.getId(), relatedPropName));
                    sql.whereEqOrLike(relation.getId(), propName, value);
                }
            }
            sql.selectEmpty(listItem.getId());
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
        addWhere(req, object, list, sql);
        // 处理load_where
        if (RvaUtils.isNotEmpty(list.getLoadWhere())) {
            sql.where(velocityUtils.parseWithLoginUser(list.getLoadWhere(), req));
        }
        // 处理排序
        addOrderBys(req, object, list, sql);
        sqlInterceptor.ifPresent(e -> e.postHandle(sql, object, list, req));
        log.info("selectList:\n" + sql);
        // 设置分页信息
        // String limit = String.format(" limit %d offset %d", req.getInt("limit", 0), req.getInt("offset", 0));
        PageHelper.startPage(req.getInt("pageNum", 1), req.getInt("pageSize", 10));
        // rvaSystemService.startPage();
        List<RvaMap<String, Object>> selectList = rvaDataMapper.selectList(sql.toString());
        // 处理load_after_sql
        viewService.loadAfterSql(req, list.getLoadAfterSql(), selectList);
        // 后续处理，给每行数据加上id和name属性ID
        for (RvaMap<String, Object> map : selectList) {
            viewService.setKeyNameValue(keyPropId, namePropId, map);
        }
        return selectList;
    }

    private void addWhere(RvaMap req, RvaObject object, RvaFeList search, RvaSQL sql) {
        if (RvaUtils.isNotEmpty(search.getPropNameCategory())) {
            String category = req.getString("rvaFeListCategory");
            if (RvaUtils.isNotEmpty(category)) {
                if (category.startsWith(RvaFeList.PREFIX_SQL)) {
                    String where = velocityUtils.parseWithLoginUser(category.substring(RvaFeList.PREFIX_SQL.length()), req);
                    sql.where(where);
                } else {
                    sql.whereEqOrLike(object.getNo(), search.getPropNameCategory(), category);
                }
            }
        }
        if (req.isNotEmpty("rvaFeListSearch")) {
            String searchContent = req.getString("rvaFeListSearch");
            String searchExpression = search.getSearchExpression();
            if (RvaUtils.isNotEmpty(searchExpression)) {
                String where = velocityUtils.parseWithLoginUser(searchExpression, req);
                sql.where(where);
            } else {
                sql.whereEqOrLike(object.getNo(), object.getPropNameName(), "%" + searchContent + "%");
            }
        }
    }

    private void addOrderBys(RvaMap req, RvaObject object, RvaFeList list, RvaSQL sql) {
        List<Map> orderBys = req.getList(RvaConstants.PARAM_ORDER_BY);
        if (orderBys.size() == 0) {
            list.getItems().stream().filter(p -> p.getOrderIndex() >= 0).sorted(new Comparator<RvaFeListItem>() {
                @Override
                public int compare(RvaFeListItem o1, RvaFeListItem o2) {
                    return o1.getOrderIndex() - o2.getOrderIndex();
                }
            }).forEach(p -> {
                Map orderBy = new HashMap();
                orderBys.add(orderBy);
                orderBy.put("prop", p.getId());
                orderBy.put("order", p.getOrderType().equals(RvaViewproperty.LIST_ORDER_ASC) ? "ascending" : "descending");
            });
        }
        addOrderBys(object, list, sql, orderBys);
    }

    private void addOrderBys(RvaObject object, RvaFeList list, RvaSQL sql, List<Map> orderBys) {
        for (Map orderBy : orderBys) {
            String prop = orderBy.get("prop").toString();
            Object order = orderBy.get("order");
            if (RvaUtils.isEmpty(order)) {
                continue;
            }
            RvaFeListItem viewproperty = list.getItem(prop);
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

    private final ISysDictTypeService dictTypeService;

    @Override
    public RvaFeList selectMeta(String listId) {
        RvaFeList list = listMapper.selectRvaFeListById(listId);
        if (RvaUtils.isNotEmpty(list.getDictCategory())) {
            List<SysDictData> dictData = dictTypeService.selectDictDataByType(list.getDictCategory());
            list.setCategories(dictData);
        }
        return list;
    }
}
