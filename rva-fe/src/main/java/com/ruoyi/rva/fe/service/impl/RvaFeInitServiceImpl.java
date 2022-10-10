package com.ruoyi.rva.fe.service.impl;

import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.rva.fe.domain.RvaFeList;
import com.ruoyi.rva.fe.service.IRvaFeInitService;
import com.ruoyi.rva.fe.service.IRvaFeListService;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaObject;
import com.ruoyi.rva.framework.service.IRvaObjectService;
import com.ruoyi.rva.framework.util.RvaUtils;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysDictDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaFeInitServiceImpl implements IRvaFeInitService {

    private final ISysDictDataService dictDataService;

    private final ISysConfigService configService;

    private final IRvaObjectService objectService;

    private final IRvaFeListService listService;

    @Override
    public RvaMap<String, RvaMap<String, Object>> getCache(String uniAppId) {
        List<SysDictData> dictDataList = dictDataService.selectDictDataList(new SysDictData());
        RvaMap<String, Object> dictMap = new RvaMap<>();
        dictDataList.forEach(dict -> {
            dictMap.getList(dict.getDictType()).add(dict);
        });
        List<RvaFeList> feLists = listService.selectRvaFeListList(new RvaFeList());
        List<SysConfig> configs = configService.selectConfigList(new SysConfig());
        List<RvaObject> objectList = objectService.selectRvaObjectList(new RvaObject());
        return new RvaMap<>("dict", dictMap).rvaPut("list", RvaUtils.list2Map(feLists))
                .rvaPut("object", RvaUtils.list2Map(objectList))
                .rvaPut("config", RvaUtils.list2Map(configs, "getConfigKey"));
    }
}
