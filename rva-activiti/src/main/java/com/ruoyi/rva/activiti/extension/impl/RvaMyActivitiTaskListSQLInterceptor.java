package com.ruoyi.rva.activiti.extension.impl;

import com.ruoyi.rva.activiti.service.IRvaActivitiMetaService;
import com.ruoyi.rva.framework.domain.*;
import com.ruoyi.rva.framework.extension.RvaListSQLInterceptor;
import com.ruoyi.rva.framework.mapper.RvaAppMapper;
import com.ruoyi.rva.framework.service.IRvaSystemService;
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
 * activiti流程【待我处理】
 */
@Component(RvaListSQLInterceptor.BEAN_FREFIX + "myActivitiTask")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaMyActivitiTaskListSQLInterceptor implements RvaListSQLInterceptor {

    private final TaskService taskService;

    private final RvaAppMapper appMapper;

    private final IRvaSystemService systemService;

    @Override
    public void preHandle(RvaSQL sql, RvaObject object, RvaView list, RvaView search, RvaMap req) {
        String treeCrudId = req.getString("treeCrudId");
        RvaApp app = appMapper.selectRvaAppById(treeCrudId);
        String procDef = app.getJsonPropertyString(IRvaActivitiMetaService.PROCESS_DEFINITION_KEY);
        List<String> keyValues = new ArrayList<>();
        taskService.createTaskQuery().processDefinitionKey(procDef).taskCandidateUser(systemService.getLoginUser().getUserName()).list().forEach(task -> keyValues.add(task.getBusinessKey()));
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
