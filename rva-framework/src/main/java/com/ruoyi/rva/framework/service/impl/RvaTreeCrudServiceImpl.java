package com.ruoyi.rva.framework.service.impl;

import com.ruoyi.rva.framework.domain.RvaApp;
import com.ruoyi.rva.framework.domain.RvaAppitem;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaObject;
import com.ruoyi.rva.framework.mapper.RvaObjectMapper;
import com.ruoyi.rva.framework.util.RvaConstants;
import com.ruoyi.rva.framework.util.RvaUtils;
import com.ruoyi.rva.framework.util.RvaVelocityUtils;
import com.ruoyi.rva.framework.mapper.RvaAppMapper;
import com.ruoyi.rva.framework.service.IRvaCrudService;
import com.ruoyi.rva.framework.service.IRvaTreeCrudService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaTreeCrudServiceImpl implements IRvaTreeCrudService {

    private final IRvaCrudService crudService;

    private final RvaAppMapper appMapper;

    private final RvaObjectMapper objectMapper;

    @Override
    public List<RvaMap<String, Object>> selectNodes(String appId, RvaMap req) {
        RvaApp app = appMapper.selectRvaAppById(appId);
        List<RvaAppitem> navTreeCruds = app.getAppItemsByType(RvaAppitem.TYPE_NAV);
        if (navTreeCruds.size() == 0) {
            navTreeCruds = app.getAppItems();
        }
        String treeCrudId = req.getString(PARAM_TCRUD_ID);
        req.rvaPut("pageSize", 100L);
        List<RvaMap<String, Object>> results = new ArrayList<>();
        if (RvaUtils.isEmpty(treeCrudId)) {
            navTreeCruds.forEach(item -> {
                if (RvaUtils.isEmpty(item.getParentId())) {
                    addToResults(req, results, item);
                }
            });
        } else {
            Optional<RvaAppitem> first = navTreeCruds.stream().filter(item -> item.getRelatedAppId().equals(treeCrudId)).findFirst();
            if (first.isPresent()) {
                if (hasPropParent(first.get())) {
                    addToResults(req, results, first.get());
                }
                if (results.size() == 0) {
                    navTreeCruds.forEach(item -> {
                        if (first.get().getId().equals(item.getParentId())) {
                            addToResults(req, results, item);
                        }
                    });
                }
            }

        }
        return results;
    }

    private void addToResults(RvaMap req, List<RvaMap<String, Object>> results, RvaAppitem item) {
        if ("list".equals(item.getSubType())) {
            results.addAll(queryList(item, req));
        } else if (velocityUtils.validateWithLoginUser(item.getShowIf(), req)) {
            RvaMap<String, Object> row = new RvaMap<String, Object>("label", item.getName())
                    .rvaPut(RvaConstants.PROP_KEY_VALUE, item.getRelatedAppId());
            setNodeKey(row, item);
            results.add(row);
        }
    }

    private Boolean hasPropParent(RvaAppitem item) {
        if (!RvaApp.TYPE_CRUD.equals(item.getRelatedAppType())) {
            return false;
        }
        String appId = item.getRelatedAppId();
        RvaApp app = appMapper.selectRvaAppById(appId);
        RvaObject object = objectMapper.selectRvaObjectById(app.getObjId());
        return object.hasPropParent();
    }

    private List<RvaMap<String, Object>> queryList(RvaAppitem item, RvaMap req) {
        String appId = item.getRelatedAppId();
        RvaApp app = appMapper.selectRvaAppById(appId);
        RvaObject object = objectMapper.selectRvaObjectById(app.getObjId());
        String where = null;
        if (object.hasPropParent()) {
            if (req.isEmpty(PARAM_TCRUD_NODE_VALUE)) {
                where = String.format("%s.%s = '0' or %s.%s is null or %s.%s = ''", object.getNo(), object.getPropNameParent(), object.getNo(), object.getPropNameParent(), object.getNo(), object.getPropNameParent());
            } else {
                String parentVal = req.getString(PARAM_TCRUD_NODE_VALUE);
                where = String.format("%s.%s = '%s'", object.getNo(), object.getPropNameParent(), parentVal);
            }
        }
        List<RvaMap<String, Object>> list = crudService.selectList(appId, req, where);
        list.forEach(row -> {
            row.put("label", row.getString(RvaConstants.PROP_NAME_VALUE));
            setNodeKey(row, item);
        });
        return list;
    }

    private void setNodeKey(RvaMap<String, Object> row, RvaAppitem item) {
        row.put(PARAM_NODE_KEY, item.getId() + "-" + row.getString(RvaConstants.PROP_KEY_VALUE));
        row.put(PARAM_APP_ITEM, item.getId());
        row.put(PARAM_APP_ITEM_SUB_TYPE, item.getSubType());
        row.put(PARAM_TCRUD_ID, item.getRelatedAppId());
    }

    private final RvaVelocityUtils velocityUtils;

    @Override
    public List<RvaAppitem> selectContents(String appId, RvaMap req) {
        RvaApp app = appMapper.selectRvaAppById(appId);
        List<RvaAppitem> navs = app.getAppItemsByType(RvaAppitem.TYPE_NAV);
        List<RvaAppitem> contents = app.getAppItemsByType("content");
        if (navs.size() == 0) {
            if (req.isNotEmpty(PARAM_TCRUD_ID)) {
                contents = app.getAppItems().stream().filter(rvaAppitem -> rvaAppitem.getRelatedAppId().equals(req.getString(PARAM_TCRUD_ID))).collect(Collectors.toList());
            }
        }
        return (List<RvaAppitem>) contents.stream().filter(item -> velocityUtils.validateWithLoginUser(item.getShowIf(), req)).collect(Collectors.toList());
    }
}
