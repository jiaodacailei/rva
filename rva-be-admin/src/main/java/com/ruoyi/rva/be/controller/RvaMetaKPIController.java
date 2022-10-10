package com.ruoyi.rva.be.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.service.IRvaMetaKPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * KPI元数据管理Controller
 * 
 * @author jiaodacailei
 * @date 2022-03-24
 */
@RestController
@RequestMapping("/rva/meta/kpi")
public class RvaMetaKPIController extends BaseController
{
    @Autowired
    private IRvaMetaKPIService kpiService;

    @PostMapping("/quickCreate")
    public AjaxResult quickCreate(@RequestBody RvaMap<String, Object> req)
    {
        kpiService.quickCreate(req);
        return AjaxResult.success();
    }

}
