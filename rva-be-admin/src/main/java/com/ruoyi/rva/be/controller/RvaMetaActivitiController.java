package com.ruoyi.rva.be.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.rva.activiti.service.IRvaActivitiMetaService;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.util.RvaConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rva/meta/activiti")
@Slf4j
public class RvaMetaActivitiController extends BaseController {

    @Autowired
    private IRvaActivitiMetaService rvaActivitiMetaService;

    /**
     * 获取工作流定义数据，用于【对象-工作流-流程定义selector】获取查询数据
     *
     * @param req
     * @return
     */
    @PostMapping("/getWorkflowDefinitions")
    public TableDataInfo getWorkflowDefinitions(@RequestBody RvaMap<String, Object> req) {
        return getDataTable(rvaActivitiMetaService.getWorkflowDefinitions(req.getString("searchContent")));
    }

    @PostMapping("/quickCreate")
    @Deprecated
    public AjaxResult quickCreate(@RequestBody RvaMap<String, Object> req) {
        rvaActivitiMetaService.quickCreate(req.getString("c0_none_gongzuoliu_liuchengdingyi"));
        return AjaxResult.success();
    }

    @PostMapping("/create")
    public AjaxResult create(@RequestBody RvaMap<String, Object> req) {
        rvaActivitiMetaService.create(req.getMapList("selection").get(0).get(RvaConstants.PROP_KEY_VALUE).toString());
        return AjaxResult.success();
    }

    @PostMapping("/deploy")
    public AjaxResult deploy(@RequestBody RvaMap<String, Object> req) {
        rvaActivitiMetaService.deploy(req.getMapList("selection").get(0).get(RvaConstants.PROP_KEY_VALUE).toString());
        return AjaxResult.success();
    }

}
