package com.ruoyi.rva.be.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.rva.framework.domain.RvaApp;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.service.IRvaTreeCrudService;
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
@RequestMapping
public class RvaTreeCrudController extends BaseController
{
    @Autowired
    private IRvaTreeCrudService treeCrudService;

    /**
     * 加载树节点数据
     */
    @PostMapping(RvaApp.URL_TCRUD_LOAD_NODE)
    public TableDataInfo loadNode(@PathVariable("app") String appId, @RequestBody Map<String, Object> req)
    {
        return getDataTable(treeCrudService.selectNodes(appId, new RvaMap(req)));
    }

    /**
     * 加载内容数据
     */
    @PostMapping(RvaApp.URL_TCRUD_LOAD_CONTENT)
    public AjaxResult getContent(@PathVariable("app") String appId, @RequestBody Map<String, Object> req)
    {
        return AjaxResult.success(treeCrudService.selectContents(appId, new RvaMap(req)));
    }
}
