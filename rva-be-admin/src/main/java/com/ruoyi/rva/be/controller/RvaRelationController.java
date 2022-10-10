package com.ruoyi.rva.be.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaRelation;
import com.ruoyi.rva.framework.service.IRvaRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 关系Controller
 * 
 * @author jiaodacailei
 * @date 2021-07-27
 */
@RestController
@RequestMapping("/rva/relation")
public class RvaRelationController extends BaseController
{
    @Autowired
    private IRvaRelationService rvaRelationService;

    /**
     * 新增关系
     */
    @PostMapping("/quickCreate")
    public AjaxResult quickCreate(@RequestBody RvaMap<String, Object> req)
    {
        rvaRelationService.quickCreate(req);
        return AjaxResult.success();
    }

    @PostMapping("/reverse")
    public AjaxResult reverse(@RequestBody RvaMap<String, Object> req)
    {
        rvaRelationService.reverse(((Map)req.getList("selection").get(0)).get("keyPropValue").toString());
        return AjaxResult.success();
    }

    /**
     * 删除对象（包括关联对象）的字段、关系表、对象属性、对象关系、视图属性、视图触发器等
     * @param req
     * @return
     */
    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody RvaMap<String, Object> req)
    {
        rvaRelationService.delete(((Map)req.getList("selection").get(0)).get("keyPropValue").toString());
        return AjaxResult.success();
    }
}
