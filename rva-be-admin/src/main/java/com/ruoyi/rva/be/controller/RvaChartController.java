package com.ruoyi.rva.be.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.service.IRvaChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 图表Controller
 * 
 * @author jiaodacailei
 * @date 2022-03-07
 */
@RestController
@RequestMapping("/rva/chart")
public class RvaChartController extends BaseController
{
    @Autowired
    private IRvaChartService chartService;

    /**
    * 获取图表元数据.
    * @param id
    * @return
     */
    @RequestMapping("/{chart}")
    public AjaxResult getInfo(@PathVariable("chart") String id)
    {
        return AjaxResult.success(chartService.selectRvaChartById(id));
    }

    /**
     * 根据图表元数据，获取其结果集
     * @param datasetId
     * @param req
     * @return
     */
    @PostMapping("/dataset/{dataset}")
    public AjaxResult getDataset(@PathVariable("dataset") String datasetId, @RequestBody RvaMap<String, Object> req)
    {
        return AjaxResult.success(chartService.getDataset(datasetId, req));
    }

}
