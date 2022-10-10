package com.ruoyi.rva.framework.service.impl;

import com.ruoyi.rva.framework.domain.RvaApp;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaObject;
import com.ruoyi.rva.framework.domain.RvaView;
import com.ruoyi.rva.framework.mapper.RvaObjectMapper;
import com.ruoyi.rva.framework.util.RvaConstants;
import com.ruoyi.rva.framework.util.RvaUtils;
import com.ruoyi.rva.framework.mapper.RvaAppMapper;
import com.ruoyi.rva.framework.mapper.RvaViewMapper;
import com.ruoyi.rva.framework.service.IRvaCrudService;
import com.ruoyi.rva.framework.service.IRvaViewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaCrudServiceImpl implements IRvaCrudService {

    private final RvaViewMapper rvaViewMapper;

    private final RvaObjectMapper rvaObjectMapper;

    private final RvaAppMapper rvaAppMapper;

    private final IRvaViewService viewService;

    @Override
    public RvaMap selectListViewData(String appId, RvaMap req) {
        RvaApp app = rvaAppMapper.selectRvaAppById(appId);
        return viewService.selectListViewData(app.getListId(), app.getSearchId(), req);
    }

    @Override
    public List<RvaMap<String, Object>> selectList(String appId, RvaMap req, String... wheres) {
        RvaApp app = rvaAppMapper.selectRvaAppById(appId);
        return viewService.selectList(app.getListId(), app.getSearchId(), req, wheres);
    }

    @Override
    public List<RvaMap<String, Object>> search(String appId, String searchContent, RvaMap req) {
        List<String> appIds = req.getList("appIds");
        if (appIds.size() > 1) {
            List<RvaMap<String, Object>> results = new ArrayList<>();
            for (String id : appIds) {
                results.addAll(search(id, searchContent, new RvaMap(req), true));
            }
            return results;
        }
        if (appIds.size() == 1) {
            appId = appIds.get(0);
        }
        return search(appId, searchContent, req, false);
    }

    private List<RvaMap<String, Object>> search(String appId, String searchContent, RvaMap req, Boolean multiple) {
        RvaApp app = rvaAppMapper.selectRvaAppById(appId);
        RvaView search = rvaViewMapper.selectRvaViewById(app.getSearchId());
        RvaMap velocity = new RvaMap(req).rvaPut(search.getProperties().get(0).getId(), searchContent);
        List<RvaMap<String, Object>> list = viewService.selectList(app.getListId(), app.getSearchId(), velocity);
        addSelectorData(appId, multiple, list);
        return list;
    }

    private void addSelectorData(String appId, Boolean multiple, List<RvaMap<String, Object>> list) {
        for (RvaMap<String, Object> map : list) {
            String keyValue = map.getString(RvaConstants.PROP_KEY_VALUE);
            map.put("selectorValue", multiple ? String.format("%s:%s", appId, keyValue) : keyValue);
            map.put("selectorLabel", map.getString(RvaConstants.PROP_NAME_VALUE));
        }
    }

    @Override
    public List<RvaMap<String, Object>> getByIds(String appId, String[] ids, RvaMap req, String keyColumn) {
        if (RvaUtils.isArrayOrListEmpty(ids)) {
            return new ArrayList<>();
        }
        List<String> appIds = req.getList("appIds");
        if (appIds.size() > 1) {
            return getByIds(appId, ids, req, true, keyColumn);
        }
        if (appIds.size() == 1) {
            appId = appIds.get(0);
        }
        return getByIds(appId, ids, req, false, keyColumn);
    }

    private List<RvaMap<String, Object>> getByIds(String appId, String[] ids, RvaMap req, Boolean multiple, String keyColumn) {
        List<RvaMap<String, Object>> results = new ArrayList<>();
        for (String valueId : ids) {
            String[] strings = valueId.split(":");
            String aId = strings[0];
            if (!multiple) {
                aId = appId;
            }
            RvaApp app = rvaAppMapper.selectRvaAppById(aId);
            RvaObject object = rvaObjectMapper.selectRvaObjectById(app.getObjId());
            String idValue = strings[strings.length - 1];
            String propNameKey = object.getPropNameKey();
            if (RvaUtils.isNotEmpty(keyColumn)) {
                propNameKey = keyColumn;
            }
            List<RvaMap<String, Object>> list = viewService.selectList(app.getListId(), app.getSearchId(), new RvaMap("loadWhere", object.getNo() + "." + propNameKey + " = '" + idValue + "'"));
            addSelectorData(aId, multiple, list);
            results.addAll(list);
        }
        return results;
    }
}
