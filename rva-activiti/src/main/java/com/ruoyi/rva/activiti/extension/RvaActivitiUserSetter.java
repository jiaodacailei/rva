package com.ruoyi.rva.activiti.extension;

import com.ruoyi.rva.framework.domain.RvaMap;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

public interface RvaActivitiUserSetter {

    String BEAN_FREFIX = "rva.activiti.user.setter.";

    /**
     * 设置nextTask的candidate
     * @param processInstance
     * @param task
     * @param nextTask
     * @param req
     */
    void setNextTaskCandidates(ProcessInstance processInstance, Task task, Task nextTask, RvaMap<String, Object> req);

    default void toEnd(ProcessInstance processInstance, Task task, RvaMap<String, Object> req) {
    }
}
