package com.ruoyi.rva.activiti.service.impl;

import com.ruoyi.rva.activiti.extension.RvaActivitiLogInfo;
import com.ruoyi.rva.activiti.extension.RvaActivitiUserSetter;
import com.ruoyi.rva.activiti.service.IRvaActivitiMetaService;
import com.ruoyi.rva.activiti.service.IRvaActivitiService;
import com.ruoyi.rva.framework.domain.*;
import com.ruoyi.rva.framework.mapper.RvaAppMapper;
import com.ruoyi.rva.framework.mapper.RvaObjectMapper;
import com.ruoyi.rva.framework.mapper.RvaViewMapper;
import com.ruoyi.rva.framework.service.IRvaSystemService;
import com.ruoyi.rva.framework.service.IRvaViewService;
import com.ruoyi.rva.framework.util.RvaConstants;
import com.ruoyi.rva.framework.util.RvaDateUtils;
import com.ruoyi.rva.framework.util.RvaJsonUtils;
import com.ruoyi.rva.framework.util.RvaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.HistoricDetailVariableInstanceUpdateEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.InputStream;
import java.util.*;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaActivitiServiceImpl implements IRvaActivitiService {

    private final IRvaViewService viewService;

    private final IRvaActivitiMetaService activitiMetaService;

    private final IRvaSystemService systemService;

    private final RuntimeService runtimeService;

    private final TaskService taskService;

    private final RepositoryService repositoryService;

    private final RvaAppMapper appMapper;

    private final RvaViewMapper viewMapper;

    @Override
    public RvaMap<String, Object> loadCreate(String appId, String viewId, RvaMap rvaMap) {
        // 当流程有并行分支时，需要重新设计
        return viewService.selectCreateViewData(viewId, rvaMap);
    }

    /**
     * 该参数表示流程创建时的提交
     */
    private final static String CREATE = "rvaActivitiCreate";


    @Override
    public void submitCreate(String appId, String viewId, RvaMap<String, Object> req) {
        String keyValue = viewService.submitCreateView(viewId, req);
        RvaApp app = appMapper.selectRvaAppById(appId);
        String actProcessDefinition = getProcessDefinitionId(app);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(actProcessDefinition, keyValue);
        Task task = taskService.createTaskQuery().processDefinitionKey(actProcessDefinition).processInstanceId(processInstance.getId()).singleResult();
        // 设置参数，所有参数都会绑定到下一个task上，而不是现在这个task
        taskService.setVariables(task.getId(), filterReq(req, viewId, keyValue));
        taskService.setVariable(task.getId(), CREATE, "Y");
        String userId = getCurrentUserName();
        taskService.setVariable(task.getId(), activitiMetaService.getTaskDefinitionPrefix(task.getTaskDefinitionKey()) + "user", userId);
        taskService.setAssignee(task.getId(), userId);
        // taskService.addCandidateUser(task.getId(), userId);
        taskService.complete(task.getId());
        // 下面也可以
        // taskService.complete(task.getId(), req);
        setCandidates(app, processInstance, task, req);
    }

    private RvaMap<String, Object> filterReq(RvaMap<String, Object> req, String viewId, String keyValue) {
        RvaView rvaView = viewService.selectRvaViewById(viewId, req);
        RvaObject object = objectMapper.selectRvaObjectById(rvaView.getObjId());
        String key = object.getKeyProperty().getId();
        List<RvaViewproperty> properties = rvaView.getProperties();
        RvaMap<String, Object> rvaMap = new RvaMap<>();
        for (RvaViewproperty p : properties) {
            String id = p.getId();
            if (key.equals(p.getPropId())) {
                rvaMap.put(id, keyValue);
            } else {
                rvaMap.put(id, req.get(id));
            }
        }
        return rvaMap;
    }

    private String getCurrentUserName() {
        return systemService.getLoginUser().getUserName();
    }

    private void setCandidates(RvaApp app, ProcessInstance processInstance, Task task, RvaMap<String, Object> req) {
        Task nextTask = taskService.createTaskQuery().processDefinitionId(processInstance.getProcessDefinitionId()).processInstanceId(processInstance.getId()).singleResult();
//        if (nextTask == null) {
//            return;
//        }
        Optional<RvaActivitiUserSetter> candidateSetter = RvaUtils.getBean(RvaActivitiUserSetter.BEAN_FREFIX + app.getObjId());
        if (candidateSetter.isPresent()) {
            if (nextTask == null) {
                candidateSetter.get().toEnd(processInstance, task, req);
            } else {
                candidateSetter.get().setNextTaskCandidates(processInstance, task, nextTask, req);
            }
        } else if (nextTask != null) {
            taskService.addCandidateUser(nextTask.getId(), "admin");
        }
    }

    private String getProcessDefinitionId(RvaApp app) {
        return app.getJsonPropertyString(IRvaActivitiMetaService.PROCESS_DEFINITION_KEY);
    }

    @Override
    public RvaMap<String, Object> loadTaskView(String appId, String bizKey) {
        RvaApp app = appMapper.selectRvaAppById(appId);
        String actProcessDefinition = getProcessDefinitionId(app);
        // 当流程有并行分支时，下面应该返回多个任务
        Task task = taskService.createTaskQuery().processDefinitionKey(actProcessDefinition).processInstanceBusinessKey(bizKey).taskCandidateUser(getCurrentUserName()).singleResult();
        RvaMap<String, Object> result = new RvaMap<>();
        if (task != null) {
            RvaView view = getView(app, task.getTaskDefinitionKey());
            result.put("url", view.getLoadUrl());
            result.put("name", task.getName());
        }
        result.put("app", app);
        return result;
    }

    private RvaView getView(RvaApp app, String taskDefinitionKey2) {
        for (int i = 0; i < app.getAppItems().size(); i++) {
            RvaAppitem rvaAppitem = app.getAppItems().get(i);
            if (RvaView.FORM_UPDATE.equals(rvaAppitem.getType())) {
                RvaView view = viewMapper.selectRvaViewById(rvaAppitem.getRelatedAppId());
                String taskDefinitionKey = getTaskDefinitionKey(view);
                if (taskDefinitionKey.equals(taskDefinitionKey2)) {
                    return view;
                }
            }
        }
        RvaUtils.throwQueryException("表单视图", "任务定义=" + taskDefinitionKey2);
        return null;
    }

    private final HistoryService historyService;

    @Override
    public List<RvaMap<String, Object>> loadLogs(String appId, String bizKey) {
        RvaApp app = appMapper.selectRvaAppById(appId);
        String actProcessDefinition = getProcessDefinitionId(app);
        List<HistoricTaskInstance> taskInstances = historyService.createHistoricTaskInstanceQuery().processDefinitionKey(actProcessDefinition).processInstanceBusinessKey(bizKey).finished().orderByTaskCreateTime().asc().list();
        List<RvaMap<String, Object>> results = new ArrayList<>();
        taskInstances.forEach(historicTaskInstance -> {
            RvaMap<String, Object> log = new RvaMap<>();
            log.put("url", String.format("/rva/activiti/%s/%s/load/log", appId, historicTaskInstance.getId()));
            log.put("name", historicTaskInstance.getName());
            log.put("time", RvaDateUtils.getDatetime(historicTaskInstance.getEndTime()));
            Optional<RvaActivitiLogInfo> bean = RvaUtils.getBean(RvaActivitiLogInfo.BEAN_FREFIX + appId);
            bean.ifPresent(e -> e.getLogInfo(appId, historicTaskInstance, log));
            results.add(log);
        });
        return results;
    }

    private final RvaObjectMapper objectMapper;

    @Override
    public RvaMap<String, Object> loadTaskLogView(String appId, String taskId) {
        RvaApp app = appMapper.selectRvaAppById(appId);
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        List<HistoricTaskInstance> taskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(historicTaskInstance.getProcessInstanceId()).orderByTaskCreateTime().asc().list();
        HistoricTaskInstance next = null;
        for (int i = 0; i < taskInstances.size(); i++) {
            HistoricTaskInstance ti = taskInstances.get(i);
            if (ti.getId().equals(taskId) && i < taskInstances.size() - 1) {
                next = taskInstances.get(i + 1);
                break;
            }
        }
        RvaMap<String, Object> formData = new RvaMap<>();
        if (next == null) {
            historyService.createHistoricVariableInstanceQuery().processInstanceId(historicTaskInstance.getProcessInstanceId()).list().forEach(historicVariableInstance -> {
                formData.put(historicVariableInstance.getVariableName(), historicVariableInstance.getValue());
            });
        } else {
            historyService.createHistoricDetailQuery().taskId(next.getId()).orderByTime().desc().list().forEach(detail -> {
                HistoricDetailVariableInstanceUpdateEntity variableInstanceUpdateEntity = (HistoricDetailVariableInstanceUpdateEntity) detail;
                formData.put(variableInstanceUpdateEntity.getName(), variableInstanceUpdateEntity.getTextValue());
            });
        }
        for (String key : formData.keySet()) {
            Object val = formData.get(key);
            if (RvaUtils.isEmpty(val)) {
                continue;
            }
            if (val instanceof String) {
                String s = val.toString().trim();
                if (s.startsWith("{")) {
                    try {
                        Map map = RvaJsonUtils.readAsType(s, Map.class);
                        formData.put(key, map);
                    } catch (Exception e) {

                    }
                } else if (s.startsWith("[")) {
                    try {
                        List list = RvaJsonUtils.readAsList(s, Object.class);
                        formData.put(key, list);
                    } catch (Exception e) {

                    }
                }
            }
        }
        RvaView view = getView(app, historicTaskInstance.getTaskDefinitionKey());
        if (formData.equals(CREATE, "Y")) {
            view = viewMapper.selectRvaViewById(app.getCreateId());
        } else {
            RvaObject object = objectMapper.selectRvaObjectById(view.getObjId());
            RvaViewproperty keyProperty = view.getKeyProperty(object);
            List<RvaMap> selection = new ArrayList<>();
            selection.add(new RvaMap(RvaConstants.PROP_KEY_VALUE, formData.get(keyProperty.getId())));
            RvaMap rvaMap = viewService.selectUpdateViewData(view.getId(), new RvaMap("rvaAppParams", new RvaMap("selection", selection)), true);
            view = (RvaView) rvaMap.get("viewData");
        }
        return new RvaMap<String, Object>("formData", formData).rvaPut("viewData", view);
    }

    private String getTaskDefinitionKey(RvaView view) {
        return view.getJsonPropertyString(IRvaActivitiMetaService.TASK_DEFINITION_KEY);
    }

    @Override
    public void submitUpdate(String appId, String viewId, RvaMap<String, Object> req) {
        String keyValue = viewService.submitUpdateView(viewId, req);
        RvaApp app = appMapper.selectRvaAppById(appId);
        String actProcessDefinition = getProcessDefinitionId(app);
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey(actProcessDefinition).processInstanceBusinessKey(keyValue).singleResult();
        if (processInstance == null) {
            RvaUtils.throwQueryException("流程实例", "bizKey=" + keyValue);
        }
        Task task = taskService.createTaskQuery().processDefinitionKey(actProcessDefinition).processInstanceId(processInstance.getId()).singleResult();
        // 设置参数，所有参数都会绑定到下一个task上，而不是现在这个task
        taskService.setVariables(task.getId(), filterReq(req, viewId, keyValue));
        taskService.setVariable(task.getId(), CREATE, "N");
        String userId = getCurrentUserName();
        taskService.setVariable(task.getId(), activitiMetaService.getTaskDefinitionPrefix(task.getTaskDefinitionKey()) + "user", userId);
        taskService.setAssignee(task.getId(), userId);
        taskService.complete(task.getId());
        // 下面也可以
        // taskService.complete(task.getId(), req);

        setCandidates(app, processInstance, task, req);
    }

    @Override
    public RvaMap<String, Object> loadUpdate(String appId, String viewId, RvaMap rvaMap) {
        return viewService.selectUpdateViewData(viewId, rvaMap, true);
    }

    private final ProcessEngine processEngine;

    @Override
    public byte[] loadImage(String appId, String bizKey) {
        RvaApp app = appMapper.selectRvaAppById(appId);
        String actProcessDefinition = getProcessDefinitionId(app);
        if (RvaUtils.isNotEmpty(bizKey)) {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processDefinitionKey(actProcessDefinition).processInstanceBusinessKey(bizKey).singleResult();
            return getActivitiProccessImage(historicProcessInstance.getId());
        }
        String id = repositoryService.createProcessDefinitionQuery().processDefinitionKey(actProcessDefinition).latestVersion().singleResult().getId();
        return generateImageData(id, Collections.emptyList());
    }


    /**
     * 获取流程图像，已执行节点和流程线高亮显示
     */
    private byte[] getActivitiProccessImage(String pProcessInstanceId) {
        //  获取历史流程实例
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(pProcessInstanceId).singleResult();
        if (historicProcessInstance == null) {
            return new byte[0];
        }
        // 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(pProcessInstanceId).orderByHistoricActivityInstanceStartTime().desc().list();

        // 已执行的节点ID集合
        List<String> executedActivityIdList = new ArrayList<String>();
        executedActivityIdList.add(historicActivityInstanceList.get(0).getActivityId());
        //获取已经执行的节点ID
//        for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
//            executedActivityIdList.add(activityInstance.getActivityId());
//        }
        return generateImageData(historicProcessInstance.getProcessDefinitionId(), executedActivityIdList);
    }

    private byte[] generateImageData(String processDefinitionId, List<String> executedActivityIdList) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        // 获取流程图图像字符流
        ProcessDiagramGenerator pec = new DefaultProcessDiagramGenerator();
        InputStream imageStream = null;
        try {
            //配置字体
            imageStream = pec.generateDiagram(bpmnModel, executedActivityIdList, Collections.emptyList(), "宋体", "宋体", "宋体", true);
            // FileCopyUtils.copy(imageStream, new FileOutputStream(new File("d:/test.svg")));
            // return new byte[0];
            return FileCopyUtils.copyToByteArray(imageStream);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            RvaUtils.throwRuntimeException(e.getMessage());
            return new byte[0];
        } finally {
            try {
                imageStream.close();
            } catch (Exception e) {
            }
        }
    }
}
