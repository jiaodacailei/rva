package com.ruoyi.rva.fe.service.impl;

import com.ruoyi.rva.fe.domain.RvaFeList;
import com.ruoyi.rva.fe.domain.RvaFeListItem;
import com.ruoyi.rva.fe.mapper.RvaFeListItemMapper;
import com.ruoyi.rva.fe.mapper.RvaFeListMapper;
import com.ruoyi.rva.fe.service.IRvaFeMetaService;
import com.ruoyi.rva.framework.domain.*;
import com.ruoyi.rva.framework.mapper.RvaObjectMapper;
import com.ruoyi.rva.framework.mapper.RvaPropertyMapper;
import com.ruoyi.rva.framework.mapper.RvaSchemaMapper;
import com.ruoyi.rva.framework.service.IRvaMetaService;
import com.ruoyi.rva.framework.util.RvaPinyinUtils;
import com.ruoyi.rva.framework.util.RvaUtils;
import com.ruoyi.rva.framework.util.RvaVelocityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaFeMetaServiceImpl implements IRvaFeMetaService {

    private final RvaObjectMapper objectMapper;

    private final RvaPropertyMapper propertyMapper;

    private final RvaFeListMapper listMapper;

    private final RvaFeListItemMapper listItemMapper;

    private final IRvaMetaService metaService;

    private final RvaVelocityUtils velocityUtils;

    @Override
    public void refreshList(String table) {
        // 1.获取RvaObject元数据（包含属性和关系）
        RvaObject obj = objectMapper.selectRvaObjectById(table);
        // 2.删除默认list相关数据
        String defaultListId = createDefaultListId(table);
        deleteList(defaultListId);
        // 3.创建默认list相关数据
        // 创建list
        RvaFeList list = createDefaultList(obj);
        // 根据属性创建各个listItem
        List<RvaProperty> props = obj.getProperties();
        for (int i = 0; i < props.size(); i++) {
            RvaProperty prop = props.get(i);
            if (prop.match(Arrays.asList("排版模式", "mode"))) {
                list.setPropNameMode(prop.getName());
            }
            if (prop.match(Arrays.asList("禁用", "disabled"))) {
                list.setPropNameDisabled(prop.getName());
            }
            if (prop.match(Arrays.asList("分类", "category"))) {
                list.setPropNameCategory(prop.getName());
            }
            if (prop.match(Arrays.asList("分组", "group"))) {
                list.setPropNameGroup(prop.getName());
            }
            this.createDefaultListItem(obj, prop, i);
        }
        listMapper.updateRvaFeList(list);
        // 更新排序
        updateOrder(obj, list);
    }

    private static String createDefaultListId(String table) {
        return RvaFeList.PREFIX + "x_" + table;
    }

    public void deleteList(String listId) {
        if (RvaUtils.isEmpty(listId)) {
            return;
        }
        RvaFeList list = listMapper.selectRvaFeListById(listId);
        if (list == null) {
            return;
        }
        list.getItems().forEach(item -> {
            if (list.isCascadeDelete()) {
                metaService.deleteView(item.getRelatedView());
            }
            listItemMapper.deleteRvaFeListItemById(item.getId());
        });
        listMapper.deleteRvaFeListById(listId);
    }

    private RvaFeListItem createDefaultListItem(RvaObject obj, RvaProperty prop, int i) {
        RvaFeListItem item = new RvaFeListItem();
        String listId = createDefaultListId(obj.getId());
        item.setId(metaService.createViewpropertyId(listId, prop.getName()));
        item.setName(prop.getDescriptionName());
        item.setPropId(prop.getId());
        item.setListId(listId);
        item.setDict(prop.getDictType());
        item.setType(prop.getType());
        item.setIdx(i);
        if (prop.match(Arrays.asList(RvaFeListItem.POSITION_TITLE, "标题", "名称", "姓名", "name"))) {
            item.setPosition(RvaFeListItem.POSITION_TITLE);
        }
        if (prop.match(Arrays.asList(RvaFeListItem.POSITION_EXCERPT, "摘要"))) {
            item.setPosition(RvaFeListItem.POSITION_EXCERPT);
        }
        if (prop.match(Arrays.asList(RvaFeListItem.POSITION_AVATAR, "图片", "头像", "image"))) {
            item.setPosition(RvaFeListItem.POSITION_AVATAR);
        }
        if (prop.match(Arrays.asList(RvaFeListItem.POSITION_CONTENT, "内容", "详情", "content", "detail"))) {
            item.setPosition(RvaFeListItem.POSITION_CONTENT);
        }
        if (obj.isCreateTime(prop.getName())) {
            item.setOrderIndex(0);
            item.setOrderType(RvaViewproperty.LIST_ORDER_DESC);
        }
        listItemMapper.insertRvaFeListItem(item);
        return item;
    }

    private RvaFeList createDefaultList(RvaObject obj) {
        RvaFeList list = new RvaFeList();
        list.setId(createDefaultListId(obj.getId()));
        list.setName(obj.getName() + "列表");
        list.setObjId(obj.getId());
        listMapper.insertRvaFeList(list);
        return list;
    }

    private void updateOrder(RvaObject obj, RvaFeList list) {
        if (obj.hasNotPropIndex()) {
            return;
        }
        list.getItems().forEach(p -> {
            if (p.getOrderIndex() >= 0) {
                p.setOrderIndex(-1);
                listItemMapper.updateRvaFeListItem(p);
            }
        });
        List<String> nameIndexWhere = obj.getPropNameIndexWhere();
        for (int i = 0; i < nameIndexWhere.size(); i++) {
            String propName = nameIndexWhere.get(i);
            RvaFeListItem p = list.getItemByObjectProperty(obj.getId() + "_" + propName);
            p.setOrderIndex(i);
            p.setOrderType(RvaViewproperty.LIST_ORDER_ASC);
            listItemMapper.updateRvaFeListItem(p);
        }
        RvaFeListItem p = list.getItemByObjectProperty(obj.getId() + "_" + obj.getPropNameIndex());
        p.setOrderIndex(nameIndexWhere.size());
        p.setOrderType(RvaViewproperty.LIST_ORDER_ASC);
        listItemMapper.updateRvaFeListItem(p);
    }

    @Override
    public void cloneList(String listId) {
        RvaFeList list = cloneListById(listId, false, true);
        for (int i = 0; i < list.getItems().size(); i++) {
            RvaFeListItem listItem = list.getItems().get(i);
            String relatedView = listItem.getRelatedView();
            if (RvaUtils.isNotEmpty(relatedView)) {
                RvaView view = metaService.cloneView(relatedView, relatedView.substring(0, 1), false);
                listItem.setRelatedView(view.getId());
            }
            listItem.setListId(list.getId());
            listItem.setId(list.getId() + "_" + i);
            listItemMapper.insertRvaFeListItem(listItem);
        }
    }

    private RvaFeList cloneListById(String listId, Boolean cascaded, Boolean cascadeDelete) {
        RvaFeList list = listMapper.selectRvaFeListById(listId).loadData();
        RvaFeList search = new RvaFeList();
        search.setObjId(list.getObjId());
        List<RvaFeList> apps = listMapper.selectRvaFeListList(search);
        // 获取RvaFeList的ID中前缀的最大数字：crud6_rva_app，num=6
        int num = RvaUtils.getMaxNumber(RvaFeList.PREFIX, apps);
        String newId = metaService.createId(RvaFeList.PREFIX, num, list.getObjId());
        list.setId(newId);
        list.setName(list.getName() + "(clone" + num + ")");
        list.setCascadeDelete(cascadeDelete);
        list.setCascaded(cascaded);
        // 插入list
        listMapper.insertRvaFeList(list);
        return list;
    }

    private final RvaSchemaMapper schemaMapper;

    @Override
    public void quickCreate(String objName, String objNo, String module) {
        RvaObject object = metaService.quickCreateObject(objName, objNo, module, true, true, "/vm/list.table.sql.vm");;
        object.getProperties().forEach(p -> {
            if (p.getName().equals("mode")) {
                p.setDictType("rva_fe_list_paibanmoshi");
                propertyMapper.updateRvaProperty(p);
            }
            if (p.getName().equals("disabled")) {
                p.setDictType("sys_yes_no");
                propertyMapper.updateRvaProperty(p);
            }
        });
        RvaApp crud = metaService.createCrud(object.getId());
        metaService.cloneApp(crud.getId(), false, false);
        refreshList(object.getId());
        cloneList(createDefaultListId(object.getId()));
    }
}
