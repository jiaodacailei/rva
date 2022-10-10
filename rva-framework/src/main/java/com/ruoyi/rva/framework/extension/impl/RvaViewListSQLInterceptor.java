package com.ruoyi.rva.framework.extension.impl;

import com.ruoyi.rva.framework.extension.RvaListSQLInterceptor;
import com.ruoyi.rva.framework.domain.*;
import com.ruoyi.rva.framework.util.RvaUtils;
import com.ruoyi.rva.framework.mapper.RvaAppMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component(RvaListSQLInterceptor.BEAN_FREFIX + "l0_rva_view")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaViewListSQLInterceptor implements RvaListSQLInterceptor {

    @Override
    public void preHandle(RvaSQL sql, RvaObject object, RvaView list, RvaView search, RvaMap req) {

    }

    private final RvaAppMapper appMapper;

    @Override
    public void postHandle(RvaSQL sql, RvaObject object, RvaView list, RvaView search, RvaMap req) {
        String treeCrudId = req.getString("treeCrudId");
        String treeCrudNodeValue = req.getString("treeCrudNodeValue");
        if ("crud0_rva_app".equals(treeCrudId)) {
            List<String> viewIds = new ArrayList<>();
            getViewIds(treeCrudNodeValue, viewIds);
            sql.where(String.format("%s.id in ('%s')", object.getNo(), RvaUtils.join(viewIds, "','")));
        } else {
            sql.where("1=2");
        }
    }

    protected void getViewIds(String treeCrudNodeValue, List<String> viewIds) {
        RvaApp rvaApp = appMapper.selectRvaAppById(treeCrudNodeValue);
        for (int i = 0; i < rvaApp.getAppItems().size(); i++) {
            RvaAppitem appitem = rvaApp.getAppItems().get(i);
            if (appitem.isView()) {
                viewIds.add(appitem.getRelatedAppId());
            }
            if (appitem.isApp()) {
                getViewIds (appitem.getRelatedAppId(), viewIds);
            }
        }
    }
}
