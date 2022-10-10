package com.ruoyi.rva.be.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.service.IRvaViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 视图Controller
 *
 * @author jiaodacailei
 * @date 2021-09-06
 */
@RestController
@RequestMapping("/rva/view")
public class RvaViewController extends BaseController {
    @Autowired
    private IRvaViewService rvaViewService;

    /**
     * 获取视图元数据.
     *
     * @param id
     * @return
     */
    @PostMapping("/{view}")
    public AjaxResult getInfo(@PathVariable("view") String id, @RequestBody Map<String, Object> req) {
        return AjaxResult.success(rvaViewService.selectRvaViewById(id, req));
    }

    /**
     * 查询列表数据.url配置在【rva_view.load_url】中.
     *
     * @param listId
     * @param searchId
     * @param req
     * @return
     */
    @PostMapping("/{view}/load/list")
    public TableDataInfo list(@PathVariable("view") String listId, String searchId, @RequestBody Map<String, Object> req) {
        rvaViewService.selectList(listId, searchId, new RvaMap(req));
        return getDataTable(rvaViewService.selectList(listId, searchId, new RvaMap(req)),  rvaViewService.selectSummariesData(listId, searchId, new RvaMap(req)));
    }

    /**
     * 加载【新建】表单数据.url配置在【rva_view.load_url】中.
     *
     * @param viewId
     * @param req
     * @return
     */
    @PostMapping("/{view}/load/create")
    public AjaxResult loadCreate(@PathVariable("view") String viewId, @RequestBody Map<String, Object> req) {
        return AjaxResult.success(rvaViewService.selectCreateViewData(viewId, new RvaMap(req)));
    }

    /**
     * 加载克隆表单数据.url配置在【rva_view.load_url】中.
     *
     * @param viewId
     * @param req
     * @return
     */
    @PostMapping("/{view}/load/clone")
    public AjaxResult loadClone(@PathVariable("view") String viewId, @RequestBody Map<String, Object> req) {
        return AjaxResult.success(rvaViewService.selectCloneViewData(viewId, new RvaMap(req)));
    }

    /**
     * 提交【新建】表单数据.url配置在【rva_viewbutton.action_url】中.
     *
     * @param viewId
     * @param req
     * @return
     */
    @PostMapping("/{view}/submit/create")
    public AjaxResult submitCreate(@PathVariable("view") String viewId, @RequestBody Map<String, Object> req) {
        rvaViewService.submitCreateView(viewId, new RvaMap(req));
        return AjaxResult.success();
    }

    /**
     * 加载【修改】表单数据.url配置在【rva_view.load_url】中.
     *
     * @param viewId
     * @param req
     * @return
     */
    @PostMapping("/{view}/load/update")
    public AjaxResult loadUpdate(@PathVariable("view") String viewId, @RequestBody Map<String, Object> req) {
        return AjaxResult.success(rvaViewService.selectUpdateViewData(viewId, new RvaMap(req), true));
    }

    /**
     * 提交【修改】表单数据.url配置在【rva_viewbutton.action_url】中.
     *
     * @param viewId
     * @param req
     * @return
     */
    @PostMapping("/{view}/submit/update")
    public AjaxResult submitUpdate(@PathVariable("view") String viewId, @RequestBody Map<String, Object> req) {
        rvaViewService.submitUpdateView(viewId, new RvaMap(req));
        return AjaxResult.success();
    }

    /**
     * 删除一行或者多行数据.url配置在【rva_viewbutton.action_url】中.
     *
     * @param viewId
     * @param req    req.selection包括一到多行数据
     * @return
     */
    @PostMapping("/{view}/delete")
    public AjaxResult delete(@PathVariable("view") String viewId, @RequestBody Map<String, Object> req) {
        rvaViewService.delete(viewId, new RvaMap(req));
        return AjaxResult.success();
    }

    /**
     * 隐藏一行或者多行数据.url配置在【rva_viewbutton.action_url】中.
     *
     * @param viewId
     * @param req    req.selection包括一到多行数据
     * @return
     */
    @PostMapping("/{view}/hide")
    public AjaxResult hide(@PathVariable("view") String viewId, @RequestBody Map<String, Object> req) {
        rvaViewService.hide(viewId, new RvaMap(req));
        return AjaxResult.success();
    }

    /**
     * 加载多个表单视图（元）数据.
     *
     * @param createView 创建表单视图ID
     * @param updateView 修改表单视图ID
     * @param req
     * @return 包括创建表单（元）数据和修改表单（元）数据列表. 格式：{ create: {viewData, formData}, update: {viewData, formDataList:[]} }
     */
    @PostMapping("/{createView}/{updateView}/load/forms")
    public AjaxResult loadForms(@PathVariable("createView") String createView, @PathVariable("updateView") String updateView, @RequestBody Map<String, Object> req) {
        return AjaxResult.success(rvaViewService.selectFormsViewData(createView, updateView, new RvaMap(req)));
    }

    /**
     * 提交多个表单视图数据
     *
     * @param createView 创建表单视图ID
     * @param updateView 修改表单视图ID
     * @param req
     * @return
     */
    @PostMapping("/{createView}/{updateView}/submit/forms")
    public AjaxResult submitForms(@PathVariable("createView") String createView, @PathVariable("updateView") String updateView, @RequestBody Map<String, Object> req) {
        rvaViewService.submitFormViews(createView, updateView, new RvaMap(req));
        return AjaxResult.success();
    }

    /**
     * 上移行.url配置在【rva_viewbutton.action_url】中.
     *
     * @param viewId
     * @param req    req.selection包括两行数据，则移动索引较大的行到另一行之上；如果包含一行数据，则和上一行数据交换索引值
     * @return
     */
    @PostMapping("/{view}/move/up")
    public AjaxResult moveUp(@PathVariable("view") String viewId, @RequestBody Map<String, Object> req) {
        rvaViewService.moveUp(viewId, new RvaMap(req));
        return AjaxResult.success();
    }

    /**
     * 下移行.url配置在【rva_viewbutton.action_url】中.
     *
     * @param viewId
     * @param req    req.selection包括两行数据，则移动索引较小的行到另一行之下；如果仅包含一行数据，则和下一行数据交换索引值
     * @return
     */
    @PostMapping("/{view}/move/down")
    public AjaxResult moveDown(@PathVariable("view") String viewId, @RequestBody Map<String, Object> req) {
        rvaViewService.moveDown(viewId, new RvaMap(req));
        return AjaxResult.success();
    }

    @PostMapping("/import")
    public AjaxResult importExcel(@RequestBody RvaMap<String, Object> req) {
        String filePath = req.getString("c0_none_piliangdaoru_wenjian");
        String createViewId = req.getString("createViewId");
        rvaViewService.importExcel(filePath, createViewId, new RvaMap(req));
        return AjaxResult.success();
    }
}
