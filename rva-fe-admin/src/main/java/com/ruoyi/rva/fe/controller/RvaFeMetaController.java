package com.ruoyi.rva.fe.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.rva.fe.service.IRvaFeMetaService;
import com.ruoyi.rva.framework.domain.RvaMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 前端元数据管理
 * 
 * @author jiaodacailei
 * @date 2021-07-27
 */
@RestController
@RequestMapping("/rva/femeta")
public class RvaFeMetaController extends BaseController
{
    @Autowired
    private IRvaFeMetaService metaService;

    /**
     * 在对象1菜单的列表上，点击'+ 前端列表'，快速创建前端列表的对象及相关元数据
     * @param req
     * @return
     */
    @PostMapping("/list/quickCreate")
    public AjaxResult quickCreate(@RequestBody RvaMap<String, Object> req)
    {
        metaService.quickCreate(req.getString("c0_none_qianduanliebiao_duixiangmingcheng"), req.getString("c0_none_qianduanliebiao_no"), req.getString("c0_none_qianduanliebiao_mokuaimingcheng"));
        return AjaxResult.success();
    }

    /**
     * 在对象1菜单的列表上，点击'前端列表'，刷新默认list元数据
     * @param req
     * @return
     */
    @PostMapping("/list/refresh")
    public AjaxResult refreshList(@RequestBody Map<String, Object> req)
    {
        List<Map> selection = new RvaMap(req).getList("selection");
        metaService.refreshList(selection.get(0).get("l1_rva_object_id").toString());
        return AjaxResult.success();
    }

    /**
     * 在前端列表菜单的列表上，点击'克隆'，克隆一个新的list
     * @param req
     * @return
     */
    @PostMapping("/list/clone")
    public AjaxResult cloneList(@RequestBody Map<String, Object> req)
    {
        List<Map> selection = new RvaMap(req).getList("selection");
        metaService.cloneList(selection.get(0).get("l0_rva_fe_list_id").toString());
        return AjaxResult.success();
    }
}
