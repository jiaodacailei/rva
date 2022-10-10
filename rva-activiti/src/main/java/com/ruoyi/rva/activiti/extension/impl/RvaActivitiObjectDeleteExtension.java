package com.ruoyi.rva.activiti.extension.impl;

import com.ruoyi.rva.activiti.service.IRvaActivitiMetaService;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.extension.RvaObjectDeleteExtension;
import com.ruoyi.rva.framework.mapper.RvaDataMapper;
import com.ruoyi.rva.framework.service.IRvaMetaService;
import com.ruoyi.rva.framework.util.RvaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 对象删除时的扩展，用于删除关联的activiti数据。
 * com.ruoyi.rva.framework.service.impl.RvaMetaServiceImpl#deleteObject(java.lang.String)
 */
@Component(RvaObjectDeleteExtension.BEAN_FREFIX + "0")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaActivitiObjectDeleteExtension implements RvaObjectDeleteExtension {

    private final RvaDataMapper dataMapper;

    private final IRvaMetaService metaService;

    private String concatValues (String column) {
       return String.format("CONCAT(\"'\",GROUP_CONCAT(%s SEPARATOR \"','\"),\"'\")", column);
    }

    @Override
    public void preDelete(String objectId) {
        // 查询关联流程定义key
        String definitionKey = dataMapper.selectString(String.format("select id from rva_process where obj_id = '%s'", objectId));
        if (RvaUtils.isEmpty(definitionKey)) {
            return;
        }
        // 删除流程关联对象视图
        String relatedForms = String.format("select distinct data->>'$.relatedUpdateForm' as viewId from rva_app where data->>'$.actProcessDefinitionKey' = '%s'", definitionKey);
        List<RvaMap<String, Object>> maps = dataMapper.selectList(relatedForms);
        maps.forEach(row -> {
            if (row == null) {
                return;
            }
            metaService.deleteView(row.getString("viewId"));
        });
        String procDefIdSql = String.format("select %s from act_re_procdef where KEY_ = '%s'", concatValues("ID_"), definitionKey);
        String procDefIds = dataMapper.selectString(procDefIdSql);
        String procDefIdWhere = String.format("PROC_DEF_ID_ in (%s)", procDefIds);
        String deleteByProcDefId = "delete from %s where " + procDefIdWhere;
        dataMapper.delete(String.format(deleteByProcDefId, "act_evt_log"));
        String deployIdSql = String.format("select %s from act_re_procdef where KEY_ = '%s'", concatValues("DEPLOYMENT_ID_"), definitionKey);
        String deployIds = dataMapper.selectString(deployIdSql);
        String deployIdWhere = String.format("DEPLOYMENT_ID_ in (%s)", deployIds);
        String deleteByDeployId = "delete from %s where " + deployIdWhere;
        // HI
        String procInstIdSql = String.format("select %s from act_hi_procinst where %s", concatValues("PROC_INST_ID_"), procDefIdWhere);
        String procInstIds = dataMapper.selectString(procInstIdSql);
        if (procInstIds != null) {
            String deleteByProcInstId = "delete from %s where " + String.format("PROC_INST_ID_ in (%s)", procInstIds);
            dataMapper.delete(String.format(deleteByProcInstId, "act_hi_attachment"));
            dataMapper.delete(String.format(deleteByProcInstId, "act_hi_comment"));
            dataMapper.delete(String.format(deleteByProcInstId, "act_hi_detail"));
            dataMapper.delete(String.format(deleteByProcInstId, "act_hi_identitylink"));
            dataMapper.delete(String.format(deleteByProcInstId, "act_hi_varinst"));
            // 这是RU中的例外，需要deleteByProcInstId
            dataMapper.delete(String.format(deleteByProcInstId, "act_ru_variable"));
        }
        String taskInstIdSql = String.format("select %s from act_hi_taskinst where %s", concatValues("ID_"), procDefIdWhere);
        String taskInstIds = dataMapper.selectString(taskInstIdSql);
        if (taskInstIds != null) {
            String deleteByTaskInstId = "delete from %s where " + String.format("TASK_ID_ in (%s)", taskInstIds);
            dataMapper.delete(String.format(deleteByTaskInstId, "act_hi_comment"));
            dataMapper.delete(String.format(deleteByTaskInstId, "act_hi_identitylink"));
        }
        dataMapper.delete(String.format(deleteByProcDefId, "act_hi_actinst"));
        dataMapper.delete(String.format(deleteByProcDefId, "act_hi_taskinst"));
        dataMapper.delete(String.format(deleteByProcDefId, "act_hi_procinst"));
        dataMapper.delete(String.format(deleteByProcDefId, "act_procdef_info"));
        // RU
        dataMapper.delete(String.format(deleteByProcDefId, "act_ru_deadletter_job"));
        dataMapper.delete(String.format(deleteByProcDefId, "act_ru_event_subscr"));
        procInstIdSql = String.format("select %s from act_ru_execution where %s", concatValues("distinct PROC_INST_ID_"), procDefIdWhere);
        procInstIds = dataMapper.selectString(procInstIdSql);
        if (procInstIds != null) {
            String deleteByProcInstId = "delete from %s where " + String.format("PROC_INST_ID_ in (%s)", procInstIds);
            dataMapper.delete(String.format(deleteByProcInstId, "act_ru_identitylink"));
        }
        taskInstIdSql = String.format("select %s from act_ru_task where %s", concatValues("ID_"), procDefIdWhere);
        taskInstIds = dataMapper.selectString(taskInstIdSql);
        if (taskInstIds != null) {
            String deleteByTaskInstId = "delete from %s where " + String.format("TASK_ID_ in (%s)", taskInstIds);
            dataMapper.delete(String.format(deleteByTaskInstId, "act_ru_identitylink"));
        }
        dataMapper.delete(String.format(deleteByProcDefId, "act_ru_integration"));
        dataMapper.delete(String.format(deleteByProcDefId, "act_ru_job"));
        dataMapper.delete(String.format(deleteByProcDefId, "act_ru_suspended_job"));
        dataMapper.delete(String.format(deleteByProcDefId, "act_ru_timer_job"));
        dataMapper.delete(String.format(deleteByProcDefId, "act_ru_task"));
        dataMapper.delete(String.format(deleteByProcDefId, "act_ru_execution"));
        // GE
        dataMapper.delete(String.format(deleteByDeployId, "act_ge_bytearray"));
        // RE
        dataMapper.delete(String.format(deleteByDeployId, "act_re_model"));
        dataMapper.delete(String.format("delete from act_re_procdef where KEY_ = '%s'", definitionKey));
        dataMapper.delete(String.format("delete from act_re_deployment where ID_ in (%s)", deployIds));
    }
}
