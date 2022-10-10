package com.ruoyi.rva.fe.service;

import com.ruoyi.rva.fe.domain.RvaFeList;
import com.ruoyi.rva.framework.domain.RvaMap;

import java.util.List;

public interface IRvaFeListRuntimeService {

    List<RvaMap<String, Object>> selectList(String listId, RvaMap rvaMap, String... wheres);

    RvaFeList selectMeta(String listId);
}
