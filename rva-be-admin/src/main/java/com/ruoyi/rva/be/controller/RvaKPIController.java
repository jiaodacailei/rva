package com.ruoyi.rva.be.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.service.IRvaChartService;
import com.ruoyi.rva.framework.service.IRvaKpiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * KPI的Controller
 * 
 * @author jiaodacailei
 * @date 2022-03-24
 */
@RestController
@RequestMapping("/rva/kpi")
public class RvaKPIController extends BaseController
{
    @Autowired
    private IRvaKpiService kpiService;

    /**
     * 根据图表元数据，获取其结果集
     * @param kpiId
     * @param req
     * @return
     */
    @RequestMapping("/{kpi}")
    public AjaxResult getData(@PathVariable("kpi") String kpiId, @RequestBody RvaMap<String, Object> req)
    {
        return AjaxResult.success(kpiService.getData(kpiId, req));
    }

}
