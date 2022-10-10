package com.ruoyi.rva.framework.extension.impl;

import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaObject;
import com.ruoyi.rva.framework.domain.RvaView;
import com.ruoyi.rva.framework.extension.RvaFormSubmitInterceptor;
import com.ruoyi.rva.framework.util.RvaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public abstract class RvaFormSubmitBaseInterceptor implements RvaFormSubmitInterceptor {

    protected void setRequestParameter(String column, Object val, RvaView view, RvaMap req) {
        req.put(view.getId() + "_" + column, val);
    }

    protected void setRequestParameterIfEmpty(String column, Object val, RvaView view, RvaMap req) {
        if (isRequestParameterEmpty(column, view, req)) {
            setRequestParameter(column, val, view, req);
        }
    }

    protected Boolean isRequestParameterEmpty(String column, RvaView view, RvaMap req) {
        String value = getRequestParameter(column, view, req);
        return RvaUtils.isEmpty(value);
    }

    protected String getRequestParameter(String column, RvaView view, RvaMap req) {
        return getRequestParameter(column, view.getId(), req);
    }

    protected Long getRequestParameterLong(String column, RvaView view, RvaMap req) {
        return getRequestParameterLong(column, view.getId(), req);
    }

    protected String getRequestParameter(String column, String viewId, RvaMap req) {
        return req.getString(String.format("%s_%s", viewId, column));
    }

    protected Long getRequestParameterLong(String column, String viewId, RvaMap req) {
        return req.getLong(String.format("%s_%s", viewId, column));
    }

    protected List getRequestParameterList(String column, RvaView view, RvaMap req) {
        return req.getList(String.format("%s_%s", view.getId(), column));
    }

    protected String getKeyValue(RvaObject object, RvaMap<String, Object> fieldValues) {
        if (object == null) {
            return null;
        }
        return object.getKeyValue(fieldValues);
    }
}
