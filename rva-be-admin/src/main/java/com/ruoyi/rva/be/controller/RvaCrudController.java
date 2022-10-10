package com.ruoyi.rva.be.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.service.IRvaCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 应用Controller
 * 
 * @author jiaodacailei
 * @date 2021-07-27
 */
@RestController
@RequestMapping("/rva/crud")
public class RvaCrudController extends BaseController
{
    @Autowired
    private IRvaCrudService rvaCrudService;

    @PostMapping("/{app}/search")
    public TableDataInfo search(@PathVariable("app") String appId, @RequestBody Map<String, Object> req)
    {
        RvaMap rvaMap = new RvaMap(req);
        return getDataTable(rvaCrudService.search(appId, rvaMap.getString("searchContent"),rvaMap));
    }

    @PostMapping("/{app}/getByIds")
    public TableDataInfo getByIds(@PathVariable("app") String appId, @RequestBody Map<String, Object> req, String keyColumn)
    {
        RvaMap req1 = new RvaMap(req);
        return getDataTable(rvaCrudService.getByIds(appId, req1.isEmpty("ids") ? new String[0] : req1.getString("ids").split(","), req1, keyColumn));
    }
}
