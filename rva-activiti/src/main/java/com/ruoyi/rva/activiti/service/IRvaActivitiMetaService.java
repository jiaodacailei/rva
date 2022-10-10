package com.ruoyi.rva.activiti.service;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.rva.framework.domain.RvaMap;

import java.util.List;

public interface IRvaActivitiMetaService {

    List<RvaMap<String, Object>> getWorkflowDefinitions(String searchContent);

    @Deprecated
    void quickCreate(String processDefinitionId);

    String PROCESS_DEFINITION_KEY = "actProcessDefinitionKey";

    String TASK_DEFINITION_KEY = "actTaskDefinitionKey";

    String RELATED_UPDATE_FORM = "relatedUpdateForm";

    default String getTaskDefinitionPrefix(String taskDefinitionKey) {
        return StringUtils.toUnderScoreCase(taskDefinitionKey) + "_";
    }

    void create(String processId);

    void deploy(String selection);
}
