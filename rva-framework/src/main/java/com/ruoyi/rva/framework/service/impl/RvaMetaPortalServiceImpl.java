package com.ruoyi.rva.framework.service.impl;

import com.ruoyi.rva.framework.domain.*;
import com.ruoyi.rva.framework.mapper.*;
import com.ruoyi.rva.framework.service.IRvaMetaPortalService;
import com.ruoyi.rva.framework.util.RvaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaMetaPortalServiceImpl implements IRvaMetaPortalService {

    private final RvaAppMapper appMapper;

    private final RvaViewMapper viewMapper;

    private final RvaChartMapper chartMapper;

    private final RvaKpiMapper kpiMapper;

    private final RvaAppitemMapper appitemMapper;

    @Override
    public void quickCreate(RvaMap<String, Object> req) {
        List<String> relatedApps = req.getList("c0_none_portal_guanlianxiang");
        if (relatedApps.size() == 0) {
            RvaUtils.throwRequiredException("关联项");
        }
        List<String> portalIds = req.getList("c0_none_portal_app");
        if (portalIds.size() > 0) {
            portalIds.forEach(appId -> {
                RvaApp app = appMapper.selectRvaAppById(appId);
                createItems(relatedApps, appId, app.getAppItems().get(app.getAppItems().size() - 1).getIdx() + 1);
            });
        } else {
            String appId = RvaApp.TYPE_PORTAL + "_" + relatedApps.get(0).split(":")[1] + "_" + RvaUtils.generateKey32(4);
            RvaApp app = createItems(relatedApps, appId, 0);
            app.setType(RvaApp.TYPE_PORTAL);
            app.setJsonProperty("columns", 1);
            app.setIdx(0);
            app.setStatus("1");
            appMapper.insertRvaApp(app);
        }

    }

    private RvaApp createItems (List<String> relatedApps, String appId, int itemIndexStart) {
        RvaApp a = new RvaApp();
        a.setId(appId);
        for (int i = 0; i < relatedApps.size(); i++) {
            String[] strings = relatedApps.get(i).split(":");
            RvaAppitem appitem = new RvaAppitem();
            appitem.setId(appId + "_" + (itemIndexStart + i));
            appitem.setIdx((itemIndexStart + i));
            String relatedAppId = strings[1];
            appitem.setRelatedAppId(relatedAppId);
            appitem.setAppId(appId);
            if (strings[0].endsWith("_rva_app")) {
                RvaApp app = appMapper.selectRvaAppById(relatedAppId);
                appitem.setRelatedAppType(app.getType());
                appitem.setType(RvaAppitem.TYPE_CONTENT);
                appitem.setName(app.getName());
                if (i == 0) {
                    a.setName(app.getName());
                    a.setObjId(app.getObjId());
                }
            } else if (strings[0].endsWith("_rva_view")) {
                RvaView view = viewMapper.selectRvaViewById(relatedAppId);
                appitem.setRelatedAppType(view.getType());
                appitem.setType(RvaAppitem.TYPE_CONTENT);
                appitem.setName(view.getName());
                if (view.getType().equals(RvaView.TYPE_SEARCH)) {
                    appitem.setType(RvaAppitem.TYPE_SEARCH);
                }
                if (i == 0) {
                    a.setName(view.getName());
                    a.setObjId(view.getObjId());
                }
            } else if (strings[0].endsWith("_rva_chart")) {
                RvaChart chart = chartMapper.selectRvaChartById(relatedAppId);
                appitem.setRelatedAppType(RvaChart.NAME);
                appitem.setType(RvaAppitem.TYPE_CONTENT);
                appitem.setName(chart.getName());
                if (i == 0) {
                    a.setName(chart.getName());
                    a.setObjId(chart.getObjId());
                }
            } else if (strings[0].endsWith("_rva_kpi")) {
                RvaKpi kpi = kpiMapper.selectRvaKpiById(relatedAppId);
                appitem.setRelatedAppType(RvaKpi.NAME);
                appitem.setType(RvaAppitem.TYPE_CONTENT);
                appitem.setName(kpi.getName());
                if (i == 0) {
                    a.setName(kpi.getName());
                    a.setObjId(kpi.getObjId());
                }
            }
            appitemMapper.insertRvaAppitem(appitem);
        }
        return a;
    }

}
