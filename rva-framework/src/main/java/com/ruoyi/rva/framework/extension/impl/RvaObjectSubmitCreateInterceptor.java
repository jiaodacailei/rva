package com.ruoyi.rva.framework.extension.impl;

import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaObject;
import com.ruoyi.rva.framework.domain.RvaView;
import com.ruoyi.rva.framework.extension.RvaFormSubmitInterceptor;
import com.ruoyi.rva.framework.mapper.RvaObjectMapper;
import com.ruoyi.rva.framework.util.RvaJsonUtils;
import com.ruoyi.rva.framework.util.RvaPinyinUtils;
import com.ruoyi.rva.framework.util.RvaUtils;
import com.ruoyi.rva.framework.mapper.RvaDataMapper;
import com.ruoyi.rva.framework.mapper.RvaSchemaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import oshi.hardware.platform.mac.MacPowerSource;

import java.util.List;
import java.util.Map;

@Component(RvaFormSubmitInterceptor.BEAN_FREFIX + "c1_rva_object")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaObjectSubmitCreateInterceptor extends RvaFormSubmitBaseInterceptor {

    @Override
    public void preHandle(RvaMap<String, Object> fieldValues, RvaObject object, RvaView view, RvaMap req) {
        String no = getRequestParameter("no", view, req);
        String name = getRequestParameter("name", view, req);
        if (RvaUtils.isEmpty(name) && RvaUtils.isEmpty(no)) {
            throw new RuntimeException("名称和编号不能同时为空！");
        }
        if (RvaUtils.isEmpty(no)) {
            no = RvaPinyinUtils.getPinyinLower(name);
        }
        if (RvaUtils.isEmpty(name)) {
            name = no;
        }
        String module = req.getStringBySuffix("rva_object_module");
        // 生成ID，即：表名
        setRequestParameter("id", module + "_" + no, view, req);
        setRequestParameter("name", name, view, req);
        setRequestParameter("no", no, view, req);
        // 设置索引
        Long idx = dataMapper.selectLong("select if(max(idx) is null, 0, max(idx) + 1) from rva_object");
        setRequestParameter("idx", idx, view, req);
        // 如果
        List properties = getRequestParameterList("properties", view, req);
        if (properties.size() == 0) {
            List<Map> maps = RvaJsonUtils.readAsList("[{\"create\":true,\"c0_rva_property_required\":\"N\",\"c0_rva_property_type\":\"INTEGER\",\"c0_rva_property_dict_select_single\":\"Y\",\"c0_rva_property_number_scale\":\"0\",\"c0_rva_property_idx\":\"0\",\"c0_rva_property_description\":\"ID\",\"c0_rva_property_id_gen_type\":\"AUTO_INCREMENT\"},{\"create\":true,\"c0_rva_property_required\":\"N\",\"c0_rva_property_type\":\"VARCHAR\",\"c0_rva_property_dict_select_single\":\"Y\",\"c0_rva_property_number_scale\":\"0\",\"c0_rva_property_idx\":\"0\",\"c0_rva_property_description\":\"名称\"}]", Map.class);
            setRequestParameter("properties", maps, view, req);
        }
    }

    private final RvaObjectMapper objectMapper;

    private final RvaSchemaMapper schemaMapper;

    private final RvaDataMapper dataMapper;

    @Override
    public void postHandle(RvaMap<String, Object> fieldValues, RvaObject obj, RvaView view, RvaMap req) {
        String id = getRequestParameter("id", view, req);
        RvaObject object = objectMapper.selectRvaObjectById(id);
        object.initPropNames();
        objectMapper.updateRvaObject(object);
        String createTableSql = RvaObject.getCreateTableSql(object);
        // 创建表
        schemaMapper.execute(createTableSql);
    }
}
