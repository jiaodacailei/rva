package com.ruoyi.rva.activiti.extension;

import com.ruoyi.rva.framework.domain.RvaMap;
import org.activiti.engine.history.HistoricTaskInstance;

public interface RvaActivitiLogInfo {

    String BEAN_FREFIX = "rva.activiti.log.info.";

    RvaMap<String, Object> getLogInfo(String appId, HistoricTaskInstance taskInstance, RvaMap<String, Object> log);
}
