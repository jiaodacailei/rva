package com.ruoyi.rva.activiti.extension.impl;

import com.ruoyi.rva.activiti.service.IRvaActivitiMetaService;
import com.ruoyi.rva.framework.domain.*;
import com.ruoyi.rva.framework.extension.RvaListSQLInterceptor;
import com.ruoyi.rva.framework.mapper.RvaAppMapper;
import com.ruoyi.rva.framework.util.RvaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 用给定taskDefinitionKey过滤流程列表，用于查询视图的“当前任务”视图属性
 */
@Component(RvaListSQLInterceptor.BEAN_FREFIX + "rvaActTask")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaActivitiTaskSearchSQLInterceptor  implements RvaListSQLInterceptor {

    private final TaskService taskService;

    private final RuntimeService runtimeService;

    private final RvaAppMapper appMapper;

    @Override
    public void preHandle(RvaSQL sql, RvaObject object, RvaView list, RvaView search, RvaMap req) {
        String taskDefKey = req.getString("currentPropValue");
        if (taskDefKey == null || taskDefKey.equals("0")) {// 0-所有
            return;
        }
        String treeCrudId = req.getString("appId");
        RvaApp app = appMapper.selectRvaAppById(treeCrudId);
        String procDef = app.getJsonPropertyString(IRvaActivitiMetaService.PROCESS_DEFINITION_KEY);
        List<String> keyValues = new ArrayList<>();
        if (taskDefKey.equals("-1")) {// 已结束流程
            runtimeService.createProcessInstanceQuery().processDefinitionKey(procDef).list().forEach(e -> keyValues.add(e.getBusinessKey()));
            if (keyValues.size() > 0) {
                sql.where(object.getNo() + "." + object.getKeyProperty().getName() + " not in ('" + RvaUtils.join(keyValues, "','") + "')");
            }
            return;
        }
        taskService.createTaskQuery().processDefinitionKey(procDef).taskDefinitionKey(taskDefKey).list().forEach(e -> keyValues.add(e.getBusinessKey()));
        if (keyValues.size() > 0) {
            sql.where(object.getNo() + "." + object.getKeyProperty().getName() + " in ('" + RvaUtils.join(keyValues, "','") + "')");
        } else {
            sql.where("1=2");
        }
    }

    @Override
    public void postHandle(RvaSQL sql, RvaObject object, RvaView list, RvaView search, RvaMap req) {

    }
}
