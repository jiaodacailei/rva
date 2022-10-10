package com.ruoyi.rva.framework.extension.impl;

import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaObject;
import com.ruoyi.rva.framework.domain.RvaView;
import com.ruoyi.rva.framework.extension.RvaDeleteInterceptor;
import com.ruoyi.rva.framework.mapper.RvaDataMapper;
import com.ruoyi.rva.framework.mapper.RvaObjectMapper;
import com.ruoyi.rva.framework.mapper.RvaSchemaMapper;
import com.ruoyi.rva.framework.service.IRvaMetaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component(RvaDeleteInterceptor.BEAN_FREFIX + "l1_rva_object")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaObjectDeleteInterceptor implements RvaDeleteInterceptor {

    private final IRvaMetaService metaService;

    private final RvaSchemaMapper schemaMapper;

    private final RvaObjectMapper objectMapper;
    private final RvaDataMapper dataMapper;


    @Override
    public Boolean preHandle(String keyValue, Map selection, RvaObject object, RvaView list, RvaMap req) {
        RvaObject o = objectMapper.selectRvaObjectById(keyValue);

        String sql = String.format("SELECT id FROM rva_app WHERE obj_id ='%s'", keyValue);
        ArrayList<String> strings = new ArrayList<>();
        List<RvaMap<String, Object>> rvaMaps = dataMapper.selectList(sql);
        //删除所有关联菜单
        if (!ObjectUtils.isEmpty(rvaMaps) &&rvaMaps.size() > 0) {
            rvaMaps.forEach(m -> {
                strings.add(m.getString("id"));
            });
            String[] appIds = new String[strings.size()];
            strings.toArray(appIds);
            metaService.deleteMenuAndButtons(appIds);
        }

        schemaMapper.execute(RvaObject.getDropTableSql(o.getId()));
        metaService.deleteAllObjectMeta(keyValue);

        return false;
    }

    @Override
    public void postHandle(String keyValue, Map selection, RvaObject object, RvaView list, RvaMap req) {


    }
}
