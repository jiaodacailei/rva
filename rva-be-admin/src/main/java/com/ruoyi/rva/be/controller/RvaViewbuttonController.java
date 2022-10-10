package com.ruoyi.rva.be.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.service.IRvaViewbuttonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 视图属性Controller
 * 
 * @author jiaodacailei
 * @date 2021-07-27
 */
@RestController
@RequestMapping("/rva/viewbutton")
public class RvaViewbuttonController extends BaseController
{
    @Autowired
    private IRvaViewbuttonService rvaViewbuttonService;

    /**
     * 删除视图属性
     */
    @PreAuthorize("@ss.hasPermi('rva:viewbutton:remove')")
    @Log(title = "视图属性", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(rvaViewbuttonService.deleteRvaViewbuttonByIds(ids));
    }

    @PostMapping("/{id}/submit/sql")
    public AjaxResult submitSql(@PathVariable String id, @RequestBody Map<String, Object> req)
    {
        return AjaxResult.success(rvaViewbuttonService.submitSql(id, new RvaMap(req)));
    }
}
