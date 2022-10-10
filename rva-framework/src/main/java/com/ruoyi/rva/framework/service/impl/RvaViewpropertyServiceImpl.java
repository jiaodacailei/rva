package com.ruoyi.rva.framework.service.impl;

import java.util.Arrays;
import java.util.List;

import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaView;
import com.ruoyi.rva.framework.domain.RvaViewproperty;
import com.ruoyi.rva.framework.mapper.RvaDataMapper;
import com.ruoyi.rva.framework.mapper.RvaViewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.framework.mapper.RvaViewpropertyMapper;
import com.ruoyi.rva.framework.service.IRvaViewpropertyService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 视图属性Service业务层处理
 * 
 * @author jiaodacailei
 * @date 2021-07-27
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class RvaViewpropertyServiceImpl implements IRvaViewpropertyService 
{
    private final RvaDataMapper dataMapper;

    private final RvaViewpropertyMapper viewpropertyMapper;

    private final RvaViewMapper viewMapper;

    @Override
    public void deleteWithTriggersByPropId(String propId) {
        RvaViewproperty search = new RvaViewproperty();
        search.setPropId(propId);
        deleteWithTriggers(search);
    }

    @Override
    public void deleteWithTriggersByRelationId(String relationId) {
        RvaViewproperty search = new RvaViewproperty();
        search.setRelationId(relationId);
        deleteWithTriggers(search);
    }

    private void deleteWithTriggers(RvaViewproperty search) {
        List<RvaViewproperty> viewproperties = viewpropertyMapper.selectRvaViewpropertyList(search);
        viewproperties.forEach(viewproperty -> {
            RvaView view = viewMapper.selectRvaViewById(viewproperty.getViewId());
            // 删除触发器
            view.getTriggers().forEach(rvaTrigger -> {
                if (Arrays.asList(rvaTrigger.getParams().split(",")).contains(viewproperty.getId())) {
                    dataMapper.deleteWhereMap("rva_triggeraction", new RvaMap<>("trigger_id", rvaTrigger.getId()));
                    dataMapper.deleteWhereMap("rva_trigger", new RvaMap<>("id", rvaTrigger.getId()));
                }
            });
            viewpropertyMapper.deleteRvaViewpropertyById(viewproperty.getId());
        });
    }
}
