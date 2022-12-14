package com.ruoyi.rva.activiti.service.impl;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.rva.activiti.domain.RvaProcess;
import com.ruoyi.rva.activiti.domain.RvaProcessTask;
import com.ruoyi.rva.activiti.mapper.RvaProcessMapper;
import com.ruoyi.rva.activiti.mapper.RvaProcessTaskMapper;
import com.ruoyi.rva.activiti.service.IRvaActivitiMetaService;
import com.ruoyi.rva.framework.domain.*;
import com.ruoyi.rva.framework.extension.RvaListSQLInterceptor;
import com.ruoyi.rva.framework.mapper.*;
import com.ruoyi.rva.framework.service.IRvaMetaService;
import com.ruoyi.rva.framework.service.IRvaRelationService;
import com.ruoyi.rva.framework.service.IRvaTreeCrudService;
import com.ruoyi.rva.framework.util.RvaConstants;
import com.ruoyi.rva.framework.util.RvaPinyinUtils;
import com.ruoyi.rva.framework.util.RvaUtils;
import com.ruoyi.rva.framework.util.RvaVelocityUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaActivitiMetaServiceImpl implements IRvaActivitiMetaService {

    private final RepositoryService repositoryService;

    private final RvaDataMapper dataMapper;

    private final RvaSchemaMapper schemaMapper;

    private final RvaObjectMapper objectMapper;

    private final RvaPropertyMapper propertyMapper;

    private final RvaAppMapper appMapper;

    private final RvaAppitemMapper appitemMapper;

    private final RvaViewMapper viewMapper;

    private final RvaViewpropertyMapper viewpropertyMapper;

    private final RvaViewbuttonMapper viewbuttonMapper;

    private final IRvaMetaService metaService;

    private final RvaProcessMapper processMapper;

    private final RvaProcessTaskMapper taskMapper;

    private final RvaVelocityUtils velocityUtils;

    @Override
    public List<RvaMap<String, Object>> getWorkflowDefinitions(String searchContent) {
        if (RvaUtils.isEmpty(searchContent)) {
            searchContent = "%";
        } else {
            searchContent = "%" + searchContent + "%";
        }
        List<RvaMap<String, Object>> results = new ArrayList<>();
        Set<String> keys = new HashSet<>();
        repositoryService.createProcessDefinitionQuery().processDefinitionNameLike(searchContent)
                .orderByProcessDefinitionName().orderByProcessDefinitionVersion().desc().listPage(0, 50)
                .forEach(processDefinition -> {
                    String key = processDefinition.getKey();
                    if (!keys.contains(key)) {
                        results.add(new RvaMap<String, Object>("selectorValue", key).rvaPut("selectorLabel", processDefinition.getName()));
                        keys.add(key);
                    }
                });
        return results;
    }

    @Override
    public void quickCreate(String processDefinitionKey) {
        // ?????????????????????????????????UserTask??????
        String processDefinitionId = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).latestVersion().singleResult().getId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        Process process = bpmnModel.getProcesses().get(0);
        List<String[]> tasks = new ArrayList<>();
        process.getFlowElements().forEach(flowElement -> {
            if (flowElement instanceof UserTask) {
                tasks.add(new String[] {flowElement.getId(), flowElement.getName()});
            }
        });
        // ????????????????????????????????????
        createMeta (processDefinitionKey, process.getName(), tasks);
    }

    @Deprecated
    private void createMeta(String processDefinitionKey, String name, List<String[]> tasks) {
        // 1.????????????
        RvaObject object = new RvaObject();
        String module = dataMapper.selectDefaultModule();
        object.setModule(module);
        object.setNo(RvaPinyinUtils.getPinyinLower(name));
        object.setName(name);
        object.setId(object.getModule() + "_" + object.getNo());
        Long aLong = dataMapper.selectLong("select max(idx) from rva_object");
        object.setIdx(aLong == null ? 0L : aLong + 1);
        object.setPropNameKey("id");
        object.setStatus("0");
        // 2.????????????
        List<RvaProperty> properties = new ArrayList<>();
        // 2.1.????????????
        RvaProperty property = new RvaProperty();
        property.setId(object.getId() + "_id");
        property.setDescription("ID");
        property.setObjId(object.getId());
        property.setType(RvaProperty.TYPE_INTEGER);
        property.setIdGenType(RvaProperty.AUTO_INCREMENT);
        property.setName("id");
        property.setIdx(0);
        properties.add(property);
        // 2.2.????????????????????????
        List<String> taskDictData = new ArrayList<>();
        taskDictData.add("0");
        taskDictData.add("??????");
        for (int i = 0; i < tasks.size(); i++) {
            String[] task = tasks.get(i);
            String taskId = task[0];
            String taskName = task[1];
            taskDictData.add(taskId);
            taskDictData.add(taskName);
            if (taskName.indexOf("??????") >= 0 || taskName.indexOf("??????") >= 0) {
                // ????????????
                RvaProperty result = createProperty("??????", RvaProperty.TYPE_VARCHAR, i * 100 + 1, taskName, taskId, object.getId(), properties);
                result.setTypeDetailAndNumberScale("1");
            }
            // ?????????
            createProperty("?????????", RvaProperty.TYPE_INTEGER, i * 100 + 2, taskName, taskId, object.getId(), properties).setTagSubmitter();
            // ????????????
            createProperty("????????????", RvaProperty.TYPE_DATETIME, i * 100 + 3, taskName, taskId, object.getId(), properties).setTagSubmitTime();
            // ????????????
            RvaProperty remark = createProperty("????????????", RvaProperty.TYPE_VARCHAR, i * 100 + 4, taskName, taskId, object.getId(), properties);
            if (object.getPropNameName() == null) {
                object.setPropNameName(remark.getName());
            }
        }
        taskDictData.add("-1");
        taskDictData.add("??????");
        object.setProperties(properties);
        // 3.?????????
        String createTableSql = RvaObject.getCreateTableSql(object);
        log.info("createTableSql:\n{}", createTableSql);
        schemaMapper.execute(createTableSql);
        // 4.??????RvaObject/RvaProperty
        objectMapper.insertRvaObject(object);
        properties.forEach(p -> {
            propertyMapper.insertRvaProperty(p);
        });
        // 5.????????????????????????
        metaService.createDictData(object.getId(), "??????", null, taskDictData.toArray(new String[0]), true);
        // 6.????????????crud
        RvaApp crud = metaService.createCrud(object.getId());
        List<RvaAppitem> appItems = crud.getAppItems();
        // 7.????????????act
        crud.setId(RvaApp.TYPE_ACT + "0_" + object.getId());
        crud.setIdx(0);
        crud.setStatus("1");
        // crud.setTemplate("rva/app/" + RvaApp.TYPE_ACT);
        // crud.setType(RvaApp.TYPE_ACT);
        crud.setCascadeDelete(true);
        crud.setJsonProperty(PROCESS_DEFINITION_KEY, processDefinitionKey);
        appMapper.insertRvaApp(crud);
        // 8.???????????????
        RvaAppitem updateItem = null;
        RvaAppitem createItem = null;
        for (int i = 0; i < appItems.size(); i++) {
            RvaAppitem rvaAppitem = appItems.get(i);
            rvaAppitem.setId(RvaApp.TYPE_ACT + "0_" + object.getId() + "_" + rvaAppitem.getType());
            rvaAppitem.setAppId(crud.getId());
            if (RvaView.FORM_UPDATE.equals(rvaAppitem.getType())) {
                updateItem = rvaAppitem;
                rvaAppitem.setId(rvaAppitem.getId() + "_0");
            } else if (RvaView.FORM_CREATE.equals(rvaAppitem.getType())) {
                createItem = rvaAppitem;
            } else if (RvaView.TYPE_LIST.equals(rvaAppitem.getType())) {
                handleListView(crud, rvaAppitem);
            } else if (RvaView.TYPE_SEARCH.equals(rvaAppitem.getType())) {
                handleSearchView(crud, rvaAppitem);
            }
            appitemMapper.insertRvaAppitem(rvaAppitem);
        }
        // 9.????????????????????????
        String createId = crud.getCreateId();
        String updateId = crud.getUpdateId();
        for (int i = 0; i < tasks.size(); i++) {
            String[] task = tasks.get(i);
            String taskId = task[0];
            String taskName = task[1];
            // ????????????????????????
            RvaView view = cloneFormView(crud.getId(), object, updateId, taskId, taskName, false);
            if (i == 0) {
                // ???????????????????????????updateItem
                updateItem.setRelatedAppId(view.getId());
                updateItem.setIdx(0);
                appitemMapper.updateRvaAppitem(updateItem);
                // ???????????????????????????????????????createItem
                view = cloneFormView(crud.getId(), object, createId, taskId, taskName, true);
                createItem.setRelatedAppId(view.getId());
                appitemMapper.updateRvaAppitem(createItem);
            } else {
                RvaAppitem appitem = RvaUtils.cloneBySetter(updateItem, RvaAppitem.class);
                appitem.setId(appitem.getId().substring(0, appitem.getId().length() - 2) + "_" + i);
                appitem.setRelatedAppId(view.getId());
                appitem.setIdx(i);
                appitemMapper.insertRvaAppitem(appitem);
            }
        }
        // 10.??????tcrud
        RvaApp tcrud = new RvaApp();
        tcrud.setId(RvaApp.TYPE_TCRUD + "0_" + object.getId());
        tcrud.setName(object.getName());
        tcrud.setType(RvaApp.TYPE_TCRUD);
        tcrud.setTemplate("rva/app/" + RvaApp.TYPE_TCRUD);
        tcrud.setIdx(0);
        tcrud.setStatus("1");
        tcrud.setObjId(object.getId());
        appMapper.insertRvaApp(tcrud);
        // 10.1 ??????nav???(????????????)
        RvaAppitem appitem = new RvaAppitem();
        appitem.setId(tcrud.getId() + "_0");
        appitem.setIdx(0);
        appitem.setName("????????????");
        appitem.setType(RvaAppitem.TYPE_NAV);
        appitem.setRelatedAppType(RvaAppitem.APP_CRUD);
        appitem.setRelatedAppId(crud.getId());
        appitem.setAppId(tcrud.getId());
        appitem.setSubType(RvaListSQLInterceptor.BEAN_FREFIX + "myActivitiTask");
        appitem.setPermission(false);
        appitemMapper.insertRvaAppitem(appitem);
        // 10.2 ??????nav???(????????????)
        appitem.setId(tcrud.getId() + "_1");
        appitem.setIdx(1);
        appitem.setName("????????????");
        appitem.setSubType(RvaListSQLInterceptor.BEAN_FREFIX + "myActiviti");
        appitem.setPermission(false);
        appitemMapper.insertRvaAppitem(appitem);
        // 10.3 ??????nav???(??????)
        appitem.setId(tcrud.getId() + "_2");
        appitem.setIdx(2);
        appitem.setName("??????");
        appitem.setSubType(null);
        appitem.setShowIf(RvaConstants.EXPRESSION_LOGIN_ADMIN);
        appitem.setPermission(false);
        appitemMapper.insertRvaAppitem(appitem);
        // 10.3 ??????content???
        appitem.setId(tcrud.getId() + "_3");
        appitem.setIdx(3);
        appitem.setName(object.getName());
        appitem.setType(RvaAppitem.TYPE_CONTENT);
        appitem.setShowIf(null);
        appitem.setPermission(true);
        appitemMapper.insertRvaAppitem(appitem);
    }

    private void handleListView(RvaApp crud, RvaAppitem rvaAppitem) {
        // ???????????????????????????????????????loadWhere
        RvaView view = metaService.cloneView(rvaAppitem.getRelatedAppId(), "l", true);
        view.setLoadWhere("#if($" + IRvaTreeCrudService.PARAM_APP_ITEM_SUB_TYPE + ") ${" + IRvaTreeCrudService.PARAM_APP_ITEM_SUB_TYPE + "} #end");
        viewMapper.updateRvaView(view);
        rvaAppitem.setRelatedAppId(view.getId());
        // ????????????????????????????????????
        RvaViewproperty task = new RvaViewproperty();
        task.setId(view.getId() + "_task");
        task.setParamFormatter("rvaActTaskName");
        task.setName("????????????");
        task.setType(RvaViewproperty.TYPE_TEXT);
        task.setIdx(-1);
        task.setViewId(view.getId());
        viewpropertyMapper.insertRvaViewproperty(task);
        // ????????????
        for (int i1 = 0; i1 < view.getButtons().size(); i1++) {
            RvaViewbutton viewbutton = view.getButtons().get(i1);
            if (viewbutton.getId().endsWith("_create")) {
            } else if (viewbutton.getId().endsWith("_update")) {
                viewbutton.setName("??????");
                viewbutton.setAction(RvaViewbutton.ACTION_TAB);
                viewbutton.setActionUrl(String.format("/rva/activiti/process/%s", crud.getId()));
            } else if (viewbutton.getId().endsWith("_delete_top") || viewbutton.getId().endsWith("_delete_inner")) {
                viewbutton.setActionUrl(String.format("/rva/%s/%s/%s/delete", RvaApp.TYPE_ACT, crud.getId(), view.getId()));
            }
            viewbuttonMapper.updateRvaViewbutton(viewbutton);
        }
    }

    private void handleSearchView(RvaApp crud, RvaAppitem rvaAppitem) {
        // ????????????
        RvaView view = metaService.cloneView(rvaAppitem.getRelatedAppId(), "s", true);
        // ????????????????????????????????????
        RvaViewproperty task = new RvaViewproperty();
        task.setId(view.getId() + "_task");
        task.setName("????????????");
        task.setType(RvaViewproperty.TYPE_SELECT);
        task.setDictType(crud.getObjId() + "_renwu");
        task.setIdx(-1);
        task.setViewId(view.getId());
        task.setSearchType(RvaViewproperty.SEARCH_EXPRESSION);
        task.setSearchExpression(RvaListSQLInterceptor.BEAN_FREFIX + "rvaActTask");
        viewpropertyMapper.insertRvaViewproperty(task);
        rvaAppitem.setRelatedAppId(view.getId());
    }

    private RvaView cloneFormView(String appId, RvaObject object, String updateId, String taskDefinitionKey, String taskName, Boolean create) {
        String type = "update";
        if (create) {
            type = "create";
        }
        RvaView view = metaService.cloneView(updateId, type.substring(0, 1), true);
        view.setName(taskName);
        view.setJsonProperty(TASK_DEFINITION_KEY, taskDefinitionKey);
        view.setLoadUrl(String.format("/rva/%s/%s/%s/load/%s", RvaApp.TYPE_ACT, appId, view.getId(), type));
        if (!create) {
            view.setLoadWhere(String.format("%s.%s = '${rvaAppParams.selection.get(0)['keyPropValue']}'", object.getNo(), object.getKeyProperty().getName()));
        }
        viewMapper.updateRvaView(view);
        for (int i = 0; i < view.getProperties().size(); i++) {
            if (i == 0) {
                continue;
            }
            RvaViewproperty vp = view.getProperties().get(i);
            if (vp.getPropId().startsWith(object.getId() + "_" + getTaskDefinitionPrefix(taskDefinitionKey))) {
                int idx = vp.getIdx() % 100;
                switch (idx) {
                    // ????????????
                    case 1:
                        vp.setType(RvaViewproperty.TYPE_RADIO);
                        vp.setDictType("sys_yes_no");
                        vp.setName("????????????");
                        vp.setFormRequired("Y");
                        break;
                    // ?????????
                    case 2:
                        vp.setName("?????????");
                        break;
                    // ????????????
                    case 3:
                        vp.setName("????????????");
                        break;
                    // ????????????
                    case 4:
                        vp.setType(RvaViewproperty.TYPE_TEXTAREA);
                        vp.setName("????????????");
                        break;
                }
                viewpropertyMapper.updateRvaViewproperty(vp);
                continue;
            }
            viewpropertyMapper.deleteRvaViewpropertyById(vp.getId());
        }
        RvaViewbutton btnSubmit = view.getButtons().get(0);
        btnSubmit.setActionUrl(String.format("/rva/%s/%s/%s/submit/%s", RvaApp.TYPE_ACT, appId, view.getId(), type));
        if (!create) {
            btnSubmit.setActionSuccess("goBack");
        }
        viewbuttonMapper.updateRvaViewbutton(btnSubmit);
        return view;
    }

    private RvaProperty createProperty (String name, String type, Integer index, String taskName, String taskId, String objectId, List<RvaProperty> properties) {
        RvaProperty property = new RvaProperty();
        property.setDescription(taskName + name);
        property.setObjId(objectId);
        property.setType(type);
        String columnPrefix = StringUtils.toUnderScoreCase(taskId) + "_";
        property.setName(columnPrefix + RvaPinyinUtils.getPinyinFirstCharLower(name));
        property.setId(property.getObjId() + "_" + property.getName());
        property.setIdx(index);
        if (type.equals(RvaProperty.TYPE_VARCHAR)) {
            property.setTypeDetailAndNumberScale("200");
        }
        properties.add(property);
        return property;
    }

    @Value("${ruoyi.profile}")
    private String uploadDir;

    @SneakyThrows
    @Override
    public void create(String processId) {
        RvaProcess process = processMapper.selectRvaProcessById(processId);
        createMeta(process);
        String xml = velocityUtils.mergeToString("/vm/process.bpmn.vm", new RvaMap("process", process));
        repositoryService.createDeployment().addString(process.getModule() + "_" + processId.toLowerCase() + ".bpmn", xml).deploy();
        log.info(xml);
    }

    private void createMeta(RvaProcess process) {
        // 0.?????????????????????????????????
        if (RvaUtils.isNotEmpty(process.getObjId())) {
            schemaMapper.execute(RvaObject.getDropTableSql(process.getObjId()));
            metaService.deleteAllObjectMeta(process.getObjId());
        }
        // 1.????????????
        RvaObject object = new RvaObject();
        object.setModule(process.getModule());
        object.setNo(process.getId().toLowerCase());
        object.setName(process.getName());
        object.setId(object.getModule() + "_" + object.getNo());
        Long aLong = dataMapper.selectLong("select max(idx) from rva_object");
        object.setIdx(aLong == null ? 0L : aLong + 1);
        object.setPropNameKey("id");
        object.setStatus("0");
        // 2.????????????
        List<RvaProperty> properties = new ArrayList<>();
        // 2.1.????????????
        RvaProperty property = new RvaProperty();
        property.setId(object.getId() + "_id");
        property.setDescription("ID");
        property.setObjId(object.getId());
        property.setType(RvaProperty.TYPE_INTEGER);
        property.setIdGenType(RvaProperty.AUTO_INCREMENT);
        property.setName("id");
        property.setIdx(0);
        properties.add(property);
        // 2.2.????????????????????????
        List<String> taskDictData = new ArrayList<>();
        taskDictData.add("0");
        taskDictData.add("??????");
        for (int i = 0; i < process.getTasks().size(); i++) {
            RvaProcessTask task = process.getTasks().get(i);
            String taskId = task.getKey();
            String taskName = task.getName();
            taskDictData.add(taskId);
            taskDictData.add(taskName);
            if ("Y".equals(task.getDecision())) {
                // ????????????
                RvaProperty result = createProperty("??????", RvaProperty.TYPE_VARCHAR, i * 100 + 1, taskName, taskId, object.getId(), properties);
                result.setTypeDetailAndNumberScale("1");
            }
            // ?????????
            createProperty("?????????", RvaProperty.TYPE_INTEGER, i * 100 + 2, taskName, taskId, object.getId(), properties).setTagSubmitter();
            // ????????????
            createProperty("????????????", RvaProperty.TYPE_DATETIME, i * 100 + 3, taskName, taskId, object.getId(), properties).setTagSubmitTime();
            // ????????????
            RvaProperty remark = createProperty("????????????", RvaProperty.TYPE_VARCHAR, i * 100 + 4, taskName, taskId, object.getId(), properties);
            if (object.getPropNameName() == null) {
                object.setPropNameName(remark.getName());
            }
        }
        taskDictData.add("-1");
        taskDictData.add("??????");
        object.setProperties(properties);
        // 3.?????????
        String dropTableSql = RvaObject.getDropTableSql(object.getId());
        schemaMapper.execute(dropTableSql);
        String createTableSql = RvaObject.getCreateTableSql(object);
        log.info("createTableSql:\n{}", createTableSql);
        schemaMapper.execute(createTableSql);
        // 4.??????RvaObject/RvaProperty
        objectMapper.insertRvaObject(object);
        properties.forEach(p -> {
            propertyMapper.insertRvaProperty(p);
        });
        // 5.????????????????????????
        metaService.createDictData(object.getId(), "??????", null, taskDictData.toArray(new String[0]), true);
        // 6.????????????crud
        RvaApp crud = metaService.createCrud(object.getId());
        List<RvaAppitem> appItems = crud.getAppItems();
        // 7.????????????act
        crud.setId(RvaApp.TYPE_ACT + "0_" + object.getId());
        crud.setIdx(0);
        crud.setStatus("1");
        // crud.setTemplate("rva/app/" + RvaApp.TYPE_ACT);
        // crud.setType(RvaApp.TYPE_ACT);
        crud.setCascadeDelete(true);
        crud.setJsonProperty(PROCESS_DEFINITION_KEY, process.getId());
        appMapper.insertRvaApp(crud);
        // 8.???????????????
        RvaAppitem updateItem = null;
        RvaAppitem createItem = null;
        for (int i = 0; i < appItems.size(); i++) {
            RvaAppitem rvaAppitem = appItems.get(i);
            rvaAppitem.setId(RvaApp.TYPE_ACT + "0_" + object.getId() + "_" + rvaAppitem.getType());
            rvaAppitem.setAppId(crud.getId());
            if (RvaView.FORM_UPDATE.equals(rvaAppitem.getType())) {
                updateItem = rvaAppitem;
                rvaAppitem.setId(rvaAppitem.getId() + "_0");
            } else if (RvaView.FORM_CREATE.equals(rvaAppitem.getType())) {
                createItem = rvaAppitem;
            } else if (RvaView.TYPE_LIST.equals(rvaAppitem.getType())) {
                handleListView(crud, rvaAppitem);
            } else if (RvaView.TYPE_SEARCH.equals(rvaAppitem.getType())) {
                handleSearchView(crud, rvaAppitem);
            }
            appitemMapper.insertRvaAppitem(rvaAppitem);
        }
        // 9.????????????????????????
        String createId = crud.getCreateId();
        String updateId = crud.getUpdateId();
        for (int i = 0; i < process.getTasks().size(); i++) {
            RvaProcessTask task = process.getTasks().get(i);
            String taskId = task.getKey();
            String taskName = task.getName();
            // ????????????????????????
            RvaView view = cloneFormView(crud.getId(), object, updateId, taskId, taskName, false);
            task.setViewId(view.getId());
            taskMapper.updateRvaProcessTask(task);
            if (i == 0) {
                // ???????????????????????????updateItem
                updateItem.setRelatedAppId(view.getId());
                updateItem.setIdx(0);
                appitemMapper.updateRvaAppitem(updateItem);
                // ???????????????????????????????????????createItem
                view = cloneFormView(crud.getId(), object, createId, taskId, taskName, true);
                task.setViewId(view.getId());
                taskMapper.updateRvaProcessTask(task);
                createItem.setRelatedAppId(view.getId());
                appitemMapper.updateRvaAppitem(createItem);
            } else {
                RvaAppitem appitem = RvaUtils.cloneBySetter(updateItem, RvaAppitem.class);
                appitem.setId(appitem.getId().substring(0, appitem.getId().length() - 2) + "_" + i);
                appitem.setRelatedAppId(view.getId());
                appitem.setIdx(i);
                appitemMapper.insertRvaAppitem(appitem);
            }
        }
        // 10.??????tcrud
        RvaApp tcrud = new RvaApp();
        tcrud.setId(RvaApp.TYPE_TCRUD + "0_" + object.getId());
        tcrud.setName(object.getName());
        tcrud.setType(RvaApp.TYPE_TCRUD);
        tcrud.setTemplate("rva/app/" + RvaApp.TYPE_TCRUD);
        tcrud.setIdx(0);
        tcrud.setStatus("1");
        tcrud.setObjId(object.getId());
        appMapper.insertRvaApp(tcrud);
        // 10.1 ??????nav???(????????????)
        RvaAppitem appitem = new RvaAppitem();
        appitem.setId(tcrud.getId() + "_0");
        appitem.setIdx(0);
        appitem.setName("????????????");
        appitem.setType(RvaAppitem.TYPE_NAV);
        appitem.setRelatedAppType(RvaAppitem.APP_CRUD);
        appitem.setRelatedAppId(crud.getId());
        appitem.setAppId(tcrud.getId());
        appitem.setSubType(RvaListSQLInterceptor.BEAN_FREFIX + "myActivitiTask");
        appitem.setPermission(false);
        appitemMapper.insertRvaAppitem(appitem);
        // 10.2 ??????nav???(????????????)
        appitem.setId(tcrud.getId() + "_1");
        appitem.setIdx(1);
        appitem.setName("????????????");
        appitem.setSubType(RvaListSQLInterceptor.BEAN_FREFIX + "myActiviti");
        appitem.setPermission(false);
        appitemMapper.insertRvaAppitem(appitem);
        // 10.3 ??????nav???(??????)
        appitem.setId(tcrud.getId() + "_2");
        appitem.setIdx(2);
        appitem.setName("??????");
        appitem.setSubType(null);
        appitem.setShowIf(RvaConstants.EXPRESSION_LOGIN_ADMIN);
        appitem.setPermission(false);
        appitemMapper.insertRvaAppitem(appitem);
        // 10.3 ??????content???
        appitem.setId(tcrud.getId() + "_3");
        appitem.setIdx(3);
        appitem.setName(object.getName());
        appitem.setType(RvaAppitem.TYPE_CONTENT);
        appitem.setShowIf(null);
        appitem.setPermission(true);
        appitemMapper.insertRvaAppitem(appitem);
        // 11.??????rva_process.obj_id
        process.setObjId(object.getId());
        processMapper.updateRvaProcess(process);
        // 12.?????????????????????????????????
        String relatedView = createRelatedView(process);
        crud.setJsonProperty(RELATED_UPDATE_FORM, relatedView);
        appMapper.updateRvaApp(crud);
    }

    private final IRvaRelationService relationService;

    private String createRelatedView (RvaProcess process) {
        if (RvaUtils.isEmpty(process.getRelatedObjId())) {
            return null;
        }
        RvaApp crud = metaService.createCrud(process.getRelatedObjId());
        RvaView view = metaService.cloneView(crud.getUpdateId(), RvaView.FORM_UPDATE.substring(0, 1), true);
        RvaRelation m21 = relationService.createM21(process.getObjId(), process.getRelatedObjId());
        RvaObject obj = objectMapper.selectRvaObjectById(process.getObjId());
        RvaObject relObj = objectMapper.selectRvaObjectById(process.getRelatedObjId());
        view.setLoadWhere(String.format("%s.%s = (select %s from %s where %s = '${bizKey}')", relObj.getNo(), m21.getRelatedProp().getName(), m21.getProp().getName(), process.getObjId(), obj.getKeyProperty().getName()));
        viewMapper.updateRvaView(view);
        return view.getId();
    }

    @Override
    public void deploy(String processId) {
        RvaProcess process = processMapper.selectRvaProcessById(processId);
        String xml = velocityUtils.mergeToString("/vm/process.bpmn.vm", new RvaMap("process", process));
        repositoryService.createDeployment().addString(process.getModule() + "_" + processId.toLowerCase() + ".bpmn", xml).deploy();
        log.info(xml);
    }
}
