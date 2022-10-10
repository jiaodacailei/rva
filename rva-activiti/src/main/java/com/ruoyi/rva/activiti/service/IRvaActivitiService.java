package com.ruoyi.rva.activiti.service;

import com.ruoyi.rva.framework.domain.RvaMap;

import java.util.List;

public interface IRvaActivitiService {

    RvaMap<String, Object> loadCreate(String appId, String viewId, RvaMap rvaMap);

    void submitCreate(String appId, String viewId, RvaMap<String, Object> req);

    RvaMap<String, Object> loadUpdate(String appId, String viewId, RvaMap rvaMap);

    void submitUpdate(String appId, String viewId, RvaMap<String, Object> req);

    /**
     * 查询流程当前任务对应的名称、表单视图loadUrl等信息
     * @param appId
     * @param bizKey
     * @return
     */
    RvaMap<String, Object> loadTaskView(String appId, String bizKey);

    /**
     * 查询流程日志
     * @param appId
     * @param bizKey
     * @return
     */
    List<RvaMap<String, Object>> loadLogs(String appId, String bizKey);

    RvaMap<String, Object> loadTaskLogView(String appId, String taskId);

    byte[] loadImage(String appId, String bizKey);
}

