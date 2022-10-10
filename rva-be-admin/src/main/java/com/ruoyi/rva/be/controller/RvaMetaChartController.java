package com.ruoyi.rva.be.controller;

import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.service.IRvaMetaChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * 图表元数据管理Controller
 * 
 * @author jiaodacailei
 * @date 2022-03-04
 */
@RestController
@RequestMapping("/rva/meta/chart")
public class RvaMetaChartController extends BaseController
{
    @Autowired
    private IRvaMetaChartService chartMetaService;

    @PostMapping("/quickCreate")
    public AjaxResult quickCreate(@RequestBody RvaMap<String, Object> req)
    {
        chartMetaService.quickCreate(req);
        return AjaxResult.success();
    }

    @PostMapping("/createBySql")
    public AjaxResult createBySql(@RequestBody RvaMap<String, Object> req)
    {
        chartMetaService.createBySql(req);
        return AjaxResult.success();
    }
}
