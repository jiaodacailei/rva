package com.ruoyi.rva.framework.extension.impl;

import com.ruoyi.rva.framework.domain.RvaObject;
import com.ruoyi.rva.framework.extension.RvaListSQLInterceptor;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaSQL;
import com.ruoyi.rva.framework.domain.RvaView;
import com.ruoyi.rva.framework.util.RvaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component(RvaListSQLInterceptor.BEAN_FREFIX + "l1_rva_view")
@Slf4j
public class RvaViewListSQLInterceptor1 implements RvaListSQLInterceptor {

    @Autowired
    private RvaViewListSQLInterceptor sqlInterceptor;

    @Override
    public void preHandle(RvaSQL sql, RvaObject object, RvaView list, RvaView search, RvaMap req) {

    }

    @Override
    public void postHandle(RvaSQL sql, RvaObject object, RvaView list, RvaView search, RvaMap req) {
        Map<String,Object> row = (Map<String,Object>)req.getMap("rvaAppParams").getList("selection").get(0);
        List<String> viewIds = new ArrayList<>();
        sqlInterceptor.getViewIds(row.get("l1_rva_app_id").toString(), viewIds);
        if (viewIds.size() > 0) {
            sql.where(String.format("%s.id in ('%s')", object.getNo(), RvaUtils.join(viewIds, "','")));
        } else {
            sql.where("1=2");
        }
    }

}
