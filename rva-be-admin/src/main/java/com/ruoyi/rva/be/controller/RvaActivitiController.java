package com.ruoyi.rva.be.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.rva.activiti.service.IRvaActivitiService;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.util.RvaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Map;

@RestController
@RequestMapping("/rva/activiti")
@Slf4j
public class RvaActivitiController extends BaseController {

    @Autowired
    private IRvaActivitiService rvaActivitiService;

    @PostMapping("/{app}/{view}/load/create")
    public AjaxResult loadCreate(@PathVariable("app") String appId, @PathVariable("view") String viewId, @RequestBody Map<String, Object> req) {
        return AjaxResult.success(rvaActivitiService.loadCreate(appId, viewId, new RvaMap(req)));
    }

    @PostMapping("/{app}/{view}/submit/create")
    public AjaxResult submitCreate(@PathVariable("app") String appId, @PathVariable("view") String viewId, @RequestBody RvaMap<String, Object> req) {
        rvaActivitiService.submitCreate(appId, viewId, req);
        return AjaxResult.success();
    }

    /**
     * 查询流程当前任务对应的名称、表单视图loadUrl等信息
     *
     * @param appId  应用ID
     * @param bizKey 工作流横表ID
     * @return {data: {name: '任务名称', url: '表单视图loadUrl'}}
     */
    @PostMapping("/{app}/{bizKey}/load/view")
    public AjaxResult loadView(@PathVariable("app") String appId, @PathVariable("bizKey") String bizKey) {
        return AjaxResult.success(rvaActivitiService.loadTaskView(appId, bizKey));
    }

    /**
     * 查询流程日志
     *
     * @param appId 应用ID
     * @param bizKey    工作流横表ID
     * @return {data: [{name: '任务名称', url: '表单视图loadUrl', time: '2001-09-08 11:12:14'}]}
     */
    @PostMapping("/{app}/{bizKey}/load/logs")
    public AjaxResult loadLogs(@PathVariable("app") String appId, @PathVariable("bizKey") String bizKey) {
        return AjaxResult.success(rvaActivitiService.loadLogs(appId, bizKey));
    }

    /**
     * 查询taskId对应的修改视图数据
     *
     * @param appId  应用ID
     * @param taskId 工作流任务ID
     * @return {data: {formData: {}, viewData: {}}}
     */
    @PostMapping("/{app}/{taskId}/load/log")
    public AjaxResult loadLogView(@PathVariable("app") String appId, @PathVariable("taskId") String taskId) {
        return AjaxResult.success(rvaActivitiService.loadTaskLogView(appId, taskId));
    }

    @PostMapping("/{app}/{view}/load/update")
    public AjaxResult loadUpdate(@PathVariable("app") String appId, @PathVariable("view") String viewId, @RequestBody Map<String, Object> req) {
        return AjaxResult.success(rvaActivitiService.loadUpdate(appId, viewId, new RvaMap(req)));
    }

    @PostMapping("/{app}/{view}/submit/update")
    public AjaxResult submitUpdate(@PathVariable("app") String appId, @PathVariable("view") String viewId, @RequestBody RvaMap<String, Object> req) {
        rvaActivitiService.submitUpdate(appId, viewId, req);
        return AjaxResult.success();
    }

    @GetMapping("/{app}/load/image")
    public void loadImage(@PathVariable("app") String appId, String bizKey, HttpServletResponse response) {
        // 设置页面不缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/svg+xml");
        byte[] imageData = rvaActivitiService.loadImage(appId, bizKey);
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            FileCopyUtils.copy(imageData, outputStream);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            RvaUtils.throwRuntimeException(e.getMessage());
        } finally {
            try {
                // outputStream.close();
            } catch (Exception e) {
            }
        }
    }
}
