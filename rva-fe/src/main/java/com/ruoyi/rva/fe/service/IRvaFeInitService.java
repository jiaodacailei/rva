package com.ruoyi.rva.fe.service;

import com.ruoyi.rva.framework.domain.RvaMap;

public interface IRvaFeInitService {

    RvaMap<String, RvaMap<String, Object>> getCache(String uniAppId);
}
