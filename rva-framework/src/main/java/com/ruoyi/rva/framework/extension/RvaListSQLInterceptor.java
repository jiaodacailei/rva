package com.ruoyi.rva.framework.extension;

import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaObject;
import com.ruoyi.rva.framework.domain.RvaSQL;
import com.ruoyi.rva.framework.domain.RvaView;

/**
 *
 */
public interface RvaListSQLInterceptor {

    String BEAN_FREFIX = "rva.list.sql.interceptor.";

    void preHandle(RvaSQL sql, RvaObject object, RvaView list, RvaView search, RvaMap req);

    void postHandle(RvaSQL sql, RvaObject object, RvaView list, RvaView search, RvaMap req);
}
