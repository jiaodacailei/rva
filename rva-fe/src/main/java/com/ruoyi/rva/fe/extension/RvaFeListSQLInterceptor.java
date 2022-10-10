package com.ruoyi.rva.fe.extension;

import com.ruoyi.rva.fe.domain.RvaFeList;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaObject;
import com.ruoyi.rva.framework.domain.RvaSQL;

public interface RvaFeListSQLInterceptor {

    String BEAN_FREFIX = "rva.fe.list.sql.interceptor.";

    void preHandle(RvaSQL sql, RvaObject object, RvaFeList list, RvaMap req);

    void postHandle(RvaSQL sql, RvaObject object, RvaFeList list, RvaMap req);
}
