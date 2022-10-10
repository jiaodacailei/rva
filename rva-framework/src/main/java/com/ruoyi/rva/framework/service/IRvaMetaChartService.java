package com.ruoyi.rva.framework.service;

import com.ruoyi.rva.framework.domain.RvaMap;

public interface IRvaMetaChartService {
    void quickCreate(RvaMap<String, Object> req);

    void createBySql(RvaMap<String, Object> req);
}

