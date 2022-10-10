package com.ruoyi.rva.fe.extension.impl;

import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.extension.RvaObjectDeleteExtension;
import com.ruoyi.rva.framework.mapper.RvaDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 对象删除时的扩展，用于删除关联的前端列表数据。
 * com.ruoyi.rva.framework.service.impl.RvaMetaServiceImpl#deleteObject(java.lang.String)
 */
@Component(RvaObjectDeleteExtension.BEAN_FREFIX + "1")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaFeListObjectDeleteExtension implements RvaObjectDeleteExtension {

    private final RvaDataMapper dataMapper;

    @Override
    public void preDelete(String objectId) {
        dataMapper.delete("delete from rva_fe_list_item where list_id in (select id from rva_fe_list where obj_id = '" + objectId + "')");
        dataMapper.deleteWhereMap("rva_fe_list", new RvaMap<>("obj_id", objectId));
    }
}
