package com.ruoyi.rva.framework.extension.impl;

import com.ruoyi.rva.framework.domain.RvaObject;
import com.ruoyi.rva.framework.extension.RvaFormSubmitInterceptor;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaProperty;
import com.ruoyi.rva.framework.domain.RvaView;
import com.ruoyi.rva.framework.mapper.RvaObjectMapper;
import com.ruoyi.rva.framework.util.RvaPinyinUtils;
import com.ruoyi.rva.framework.util.RvaUtils;
import com.ruoyi.rva.framework.mapper.RvaPropertyMapper;
import com.ruoyi.rva.framework.mapper.RvaSchemaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(RvaFormSubmitInterceptor.BEAN_FREFIX + "u0_rva_property")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaPropertySubmitUpdateInterceptor extends RvaFormSubmitBaseInterceptor {

    private final RvaSchemaMapper schemaMapper;

    private final RvaObjectMapper objectMapper;

    private final RvaPropertyMapper propertyMapper;

    @Override
    public void preHandle(RvaMap<String, Object> fieldValues, RvaObject obj, RvaView view, RvaMap req) {
        String name = getRequestParameter("name", view, req);
        String description = getRequestParameter("description", view, req);
        if (RvaUtils.isEmpty(name) && RvaUtils.isEmpty(description)) {
            throw new RuntimeException("名称和中文名称不能同时为空！");
        }
        if (RvaUtils.isEmpty(name)) {
            name = RvaPinyinUtils.getPinyinLower(description);
        }
        if (RvaUtils.isEmpty(description)) {
            description = name;
        }
        // 设置id/name/description
        setRequestParameter("name", name, view, req);
        setRequestParameter("description", description, view, req);
        // 设置value_min/value_max/number_scale
        String type = req.getStringBySuffix("rva_property_type");
        String detail = req.getStringBySuffix("rva_property_type_detail");
        setRequestParameterIfEmpty("value_min", 0, view, req);
        switch (type) {
            case RvaProperty.TYPE_VARCHAR:
                if (RvaUtils.isEmpty(detail)) {
                    detail = "100";
                    setRequestParameter("type_detail", detail, view, req);
                }
                if (RvaUtils.isNotMatched("[0-9]*", detail)) {
                    throw new RuntimeException("数据补充的格式不正确，应该为正整数！");
                }
                setRequestParameterIfEmpty("value_max", detail, view, req);
                break;
            case RvaProperty.TYPE_INTEGER:
                setRequestParameterIfEmpty("value_max", 11, view, req);
                break;
            case RvaProperty.TYPE_SMALLINT:
                setRequestParameterIfEmpty("value_max", 6, view, req);
                break;
            case RvaProperty.TYPE_NUMERIC:
                if (RvaUtils.isEmpty(detail)) {
                    detail = "10,2";
                    setRequestParameter("type_detail", detail, view, req);
                }
                if (RvaUtils.isNotMatched("[0-9]*([,][0-9]*)+", detail)) {
                    throw new RuntimeException("数据补充的格式不正确，正确的格式为：‘10,2’或者正整数！");
                }
                String[] details = detail.split(",");
                int numberScale = 0;
                if (details.length == 2) {
                    numberScale = Integer.parseInt(details[1]);
                }
                setRequestParameter("number_scale", numberScale, view, req);
                setRequestParameterIfEmpty("value_max", RvaUtils.getInt(details[0]) - numberScale, view, req);
                break;
        }
        // 设置列名
        String id = getRequestParameter("id", view, req);
        RvaProperty rvaProperty = propertyMapper.selectRvaPropertyById(id);
        req.put("oldName", rvaProperty.getName());
    }

    @Override
    public void postHandle(RvaMap<String, Object> fieldValues, RvaObject obj, RvaView view, RvaMap req) {
        String table = getRequestParameter("obj_id", view, req);
        if (schemaMapper.queryTableCount(table) == 0) {
            return;
        }
        RvaObject object = objectMapper.selectRvaObjectById(table);
        String id = getRequestParameter("id", view, req);
        String columnSql = RvaProperty.getModifySql(object.getProperty(id), req.getString("oldName"), object);
        schemaMapper.execute(columnSql);
    }
}
