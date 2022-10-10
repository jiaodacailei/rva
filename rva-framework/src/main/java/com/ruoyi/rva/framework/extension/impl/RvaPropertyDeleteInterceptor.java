package com.ruoyi.rva.framework.extension.impl;

import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaObject;
import com.ruoyi.rva.framework.domain.RvaProperty;
import com.ruoyi.rva.framework.domain.RvaView;
import com.ruoyi.rva.framework.extension.RvaDeleteInterceptor;
import com.ruoyi.rva.framework.mapper.RvaObjectMapper;
import com.ruoyi.rva.framework.mapper.RvaSchemaMapper;
import com.ruoyi.rva.framework.service.IRvaViewpropertyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component(RvaDeleteInterceptor.BEAN_FREFIX + "u0_rva_property")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaPropertyDeleteInterceptor implements RvaDeleteInterceptor {

    private final RvaSchemaMapper schemaMapper;

    private final RvaObjectMapper objectMapper;

    private final IRvaViewpropertyService rvaViewpropertyService;

    @Override
    public Boolean preHandle(String keyValue, Map selection, RvaObject object, RvaView view, RvaMap req) {
        RvaObject o = objectMapper.selectRvaObjectById(req.getStringBySuffix("rva_property_obj_id"));
        String columnSql = RvaProperty.getDropSql(o.getProperty(keyValue), o);
        schemaMapper.execute(columnSql);
        rvaViewpropertyService.deleteWithTriggersByPropId(keyValue);
        return true;
    }

    @Override
    public void postHandle(String keyValue, Map selection, RvaObject object, RvaView view, RvaMap req) {

    }
}
