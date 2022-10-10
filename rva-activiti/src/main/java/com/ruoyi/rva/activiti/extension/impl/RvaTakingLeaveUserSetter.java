package com.ruoyi.rva.activiti.extension.impl;

import com.ruoyi.rva.activiti.extension.RvaActivitiUserSetter;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.service.IRvaSystemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(RvaActivitiUserSetter.BEAN_FREFIX + "rva_qingjia")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaTakingLeaveUserSetter implements RvaActivitiUserSetter {

    private final TaskService taskService;

    private final IRvaSystemService systemService;

    @Override
    public void setNextTaskCandidates(ProcessInstance processInstance, Task task, Task nextTask, RvaMap<String, Object> req) {
        taskService.addCandidateUser(nextTask.getId(), systemService.getLoginUser().getUserName());
    }
}
