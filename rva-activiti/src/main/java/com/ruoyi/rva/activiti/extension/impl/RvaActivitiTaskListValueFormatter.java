package com.ruoyi.rva.activiti.extension.impl;

import com.ruoyi.rva.activiti.service.IRvaActivitiMetaService;
import com.ruoyi.rva.framework.domain.RvaApp;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaView;
import com.ruoyi.rva.framework.domain.RvaViewproperty;
import com.ruoyi.rva.framework.extension.RvaListValueFormatter;
import com.ruoyi.rva.framework.mapper.RvaAppMapper;
import com.ruoyi.rva.framework.util.RvaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 工作流的列表属性显示任务名称
 */
@Component("rvaActTaskName")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaActivitiTaskListValueFormatter implements RvaListValueFormatter {

    private final RvaAppMapper appMapper;

    private final TaskService taskService;

    @Override
    public String formatValue(RvaView list, RvaMap<String, Object> row, RvaMap req, RvaViewproperty prop, String value) {
        RvaApp app = appMapper.selectRvaAppById(req.getString("appId"));
        String procDef = app.getJsonPropertyString(IRvaActivitiMetaService.PROCESS_DEFINITION_KEY);
        String keyPropValue = row.getString("keyPropValue");
        List<Task> tasks = taskService.createTaskQuery().processDefinitionKey(procDef).processInstanceBusinessKey(keyPropValue).list();
        if (tasks.size() == 0) {
            return "结束";
        }
        List<String> taskNames = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            taskNames.add(task.getName());
        }
        return RvaUtils.join(taskNames);
    }
}
