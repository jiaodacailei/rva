package com.ruoyi.rva.be.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.rva.framework.domain.RvaTriggeraction;
import com.ruoyi.rva.framework.service.IRvaTriggeractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 触发动作Controller
 * 
 * @author jiaodacailei
 * @date 2021-09-16
 */
@RestController
@RequestMapping("/rva/triggeraction")
public class RvaTriggeractionController extends BaseController
{
    @Autowired
    private IRvaTriggeractionService rvaTriggeractionService;

    /**
     * 查询触发动作列表
     */
    @PreAuthorize("@ss.hasPermi('rva:triggeraction:list')")
    @GetMapping("/list")
    public TableDataInfo list(RvaTriggeraction rvaTriggeraction)
    {
        startPage();
        List<RvaTriggeraction> list = rvaTriggeractionService.selectRvaTriggeractionList(rvaTriggeraction);
        return getDataTable(list);
    }

    /**
     * 导出触发动作列表
     */
    @PreAuthorize("@ss.hasPermi('rva:triggeraction:export')")
    @Log(title = "触发动作", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(RvaTriggeraction rvaTriggeraction)
    {
        List<RvaTriggeraction> list = rvaTriggeractionService.selectRvaTriggeractionList(rvaTriggeraction);
        ExcelUtil<RvaTriggeraction> util = new ExcelUtil<RvaTriggeraction>(RvaTriggeraction.class);
        return util.exportExcel(list, "触发动作数据");
    }

    /**
     * 获取触发动作详细信息
     */
    @PreAuthorize("@ss.hasPermi('rva:triggeraction:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(rvaTriggeractionService.selectRvaTriggeractionById(id));
    }

    /**
     * 新增触发动作
     */
    @PreAuthorize("@ss.hasPermi('rva:triggeraction:add')")
    @Log(title = "触发动作", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RvaTriggeraction rvaTriggeraction)
    {
        return toAjax(rvaTriggeractionService.insertRvaTriggeraction(rvaTriggeraction));
    }

    /**
     * 修改触发动作
     */
    @PreAuthorize("@ss.hasPermi('rva:triggeraction:edit')")
    @Log(title = "触发动作", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RvaTriggeraction rvaTriggeraction)
    {
        return toAjax(rvaTriggeractionService.updateRvaTriggeraction(rvaTriggeraction));
    }

    /**
     * 删除触发动作
     */
    @PreAuthorize("@ss.hasPermi('rva:triggeraction:remove')")
    @Log(title = "触发动作", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(rvaTriggeractionService.deleteRvaTriggeractionByIds(ids));
    }
}
