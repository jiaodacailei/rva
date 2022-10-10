package com.ruoyi.rva.be.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.service.IRvaMetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 元数据管理
 * 
 * @author jiaodacailei
 * @date 2021-07-27
 */
@RestController
@RequestMapping
public class RvaMetaController extends BaseController
{
    @Autowired
    private IRvaMetaService metaService;

    @PostMapping("/rva/meta/object/sql")
    public AjaxResult getCreateSQL(@RequestBody RvaMap<String, Object> req)
    {

        List<Map> selection = new RvaMap(req).getList("selection");
        return AjaxResult.success(metaService.getCreateSQL(selection.get(0).get("keyPropValue").toString()));
    }


    /**
     * 在对象1列表上点击创建CRUD
     * @param req
     * @return
     */
    @PostMapping("/rva/meta/object/refresh")
    public AjaxResult refreshObject(@RequestBody Map<String, Object> req)
    {
        List<Map> selection = new RvaMap(req).getList("selection");
        metaService.refreshObject(selection.get(0).get("l1_rva_object_id").toString());
        return AjaxResult.success();
    }

    /**
     * 在对象1列表上点击"快捷创建"
     * @param req
     * @return
     */
    @PostMapping("/rva/meta/object/quickCreate")
    public AjaxResult quickCreateObject(@RequestBody RvaMap<String, Object> req)
    {
        metaService.quickCreateObject(req.getString("c0_none_kuaijie_duixiangmingcheng"), req.getString("c0_none_kuaijie_duixiangbianhao"), req.getString("c0_none_kuaijie_mokuai"), "Y".equals(req.getString("c0_none_kuaijie_yidongyingyong")), "Y".equals(req.getString("c0_none_kuaijie_saas")));
        return AjaxResult.success();
    }

    /**
     * 显示应用元数据sql脚本
     * @param req
     * @return
     */
    @PostMapping("/rva/meta/app/sql")
    public AjaxResult getAppMetaSQL(@RequestBody RvaMap<String, Object> req)
    {
        List<Map> selection = new RvaMap(req).getList("selection");
        return AjaxResult.success(metaService.getAppMetaSQL(selection.get(0).get("keyPropValue").toString()));
    }

    /**
     * 显示元数据文档
     * @param req
     * @return
     */
    @PostMapping("/rva/meta/app/doc")
    public AjaxResult getAppDocument(@RequestBody RvaMap<String, Object> req)
    {
        List<Map> selection = new RvaMap(req).getList("selection");
        return AjaxResult.success(metaService.getAppDocument(selection.get(0).get("keyPropValue").toString()));
    }

    /**
     * 在对象1列表上点击创建CRUD
     * @param req
     * @return
     */
    @PostMapping("/rva/meta/crud/create")
    public AjaxResult createCrud(@RequestBody Map<String, Object> req)
    {
        List<Map> selection = new RvaMap(req).getList("selection");
        metaService.createCrud(selection.get(0).get("l1_rva_object_id").toString());
        return AjaxResult.success();
    }

    private List<String> getCrudId (Map<String, Object> req) {
        RvaMap<String, Object> rvaMap = new RvaMap<>(req);
        List<String> ids = new ArrayList<>();
        rvaMap.getList("selection").forEach(row -> {
            ids.add(((Map<String, Object>)row).get("l1_rva_app_id").toString());
        });
        return ids;
    }

    @PostMapping("/rva/meta/crud/clone")
    public AjaxResult cloneCrud(@RequestBody Map<String, Object> req)
    {
        metaService.cloneApp(getCrudId(req).get(0), false, true);
        return AjaxResult.success();
    }

    @PostMapping("/rva/meta/crud/delete")
    public AjaxResult deleteCrud(@RequestBody Map<String, Object> req)
    {
        metaService.deleteApp(getCrudId(req));
        return AjaxResult.success();
    }

    @PostMapping("/rva/meta/app/createMenu")
    public AjaxResult createMenuAndButtonsByApp(@RequestBody RvaMap<String, Object> req)
    {
        metaService.createMenuAndButtonsByApp(req);
        return AjaxResult.success();
    }

    @PostMapping("/rva/meta/app/usage")
    public AjaxResult getAppUsage(@RequestBody RvaMap<String, Object> req)
    {
        List<Map> selection = new RvaMap(req).getList("selection");
        RvaMap formViewData = metaService.getAppUsage(selection.get(0).get("keyPropValue").toString());
        return AjaxResult.success(formViewData);
    }

    @PostMapping("/rva/meta/view/createMenu")
    public AjaxResult createMenuAndButtonsByView(@RequestBody RvaMap<String, Object> req)
    {
        metaService.createMenuAndButtonsByView(req);
        return AjaxResult.success();
    }

    @PostMapping("/rva/meta/view/sql")
    public AjaxResult getViewMetaSQL(@RequestBody RvaMap<String, Object> req)
    {
        List<Map> selection = new RvaMap(req).getList("selection");
        return AjaxResult.success(metaService.getViewMetaSQL(selection.get(0).get("keyPropValue").toString()));
    }
}
