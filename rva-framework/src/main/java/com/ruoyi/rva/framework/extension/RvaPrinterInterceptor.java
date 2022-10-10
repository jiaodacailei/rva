package com.ruoyi.rva.framework.extension;

import com.ruoyi.rva.framework.domain.RvaMap;

public interface RvaPrinterInterceptor {

    String BEAN_FREFIX = "rva.view.print.interceptor.";
    String getHtml (RvaMap<String, Object> req );
}
