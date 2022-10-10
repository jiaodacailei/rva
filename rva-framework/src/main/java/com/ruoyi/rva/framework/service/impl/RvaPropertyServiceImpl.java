package com.ruoyi.rva.framework.service.impl;

import com.ruoyi.rva.framework.domain.*;
import com.ruoyi.rva.framework.mapper.*;
import com.ruoyi.rva.framework.service.IRvaPropertyService;
import com.ruoyi.rva.framework.service.IRvaViewpropertyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 属性Service业务层处理
 * 
 * @author jiaodacailei
 * @date 2021-07-27
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class RvaPropertyServiceImpl implements IRvaPropertyService 
{
    private final RvaPropertyMapper propertyMapper;

    private final RvaObjectMapper objectMapper;

    private final RvaSchemaMapper schemaMapper;

    private final IRvaViewpropertyService viewpropertyService;

    @Override
    public void drop(String id) {
        RvaProperty property = propertyMapper.selectRvaPropertyById(id);
        RvaObject object = objectMapper.selectRvaObjectById(property.getObjId());
        // 删除表字段
        schemaMapper.queryColumns(property.getObjId()).forEach(mySqlColumn -> {
            if (mySqlColumn.getField().equalsIgnoreCase(property.getName())) {
                String dropSql = RvaProperty.getDropSql(property, object);
                schemaMapper.execute(dropSql);
            }
        });
        // 删除对象属性
        propertyMapper.deleteRvaPropertyById(property.getId());
        // 删除关联的视图属性
        viewpropertyService.deleteWithTriggersByPropId(property.getId());
    }
}
