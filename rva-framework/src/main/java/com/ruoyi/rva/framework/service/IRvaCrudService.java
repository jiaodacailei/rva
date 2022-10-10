package com.ruoyi.rva.framework.service;

import com.ruoyi.rva.framework.domain.RvaMap;

import java.util.List;

public interface IRvaCrudService {

    RvaMap selectListViewData (String appId, RvaMap req);

    List<RvaMap<String, Object>> selectList (String appId, RvaMap req, String... wheres);

    List<RvaMap<String, Object>> search(String appId, String searchContent, RvaMap req);

    List<RvaMap<String, Object>> getByIds(String appId, String[] ids, RvaMap req, String keyColumn);
}
