package com.ruoyi.rva.be.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.rva.framework.domain.RvaTrigger;
import com.ruoyi.rva.framework.service.IRvaTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 触发器Controller
 * 
 * @author jiaodacailei
 * @date 2021-09-16
 */
@RestController
@RequestMapping("/rva/trigger")
public class RvaTriggerController extends BaseController
{
    @Autowired
    private IRvaTriggerService rvaTriggerService;

    /**
     * 查询触发器列表
     */
    @PreAuthorize("@ss.hasPermi('rva:trigger:list')")
    @GetMapping("/list")
    public TableDataInfo list(RvaTrigger rvaTrigger)
    {
        startPage();
        List<RvaTrigger> list = rvaTriggerService.selectRvaTriggerList(rvaTrigger);
        return getDataTable(list);
    }

    /**
     * 导出触发器列表
     */
    @PreAuthorize("@ss.hasPermi('rva:trigger:export')")
    @Log(title = "触发器", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(RvaTrigger rvaTrigger)
    {
        List<RvaTrigger> list = rvaTriggerService.selectRvaTriggerList(rvaTrigger);
        ExcelUtil<RvaTrigger> util = new ExcelUtil<RvaTrigger>(RvaTrigger.class);
        return util.exportExcel(list, "触发器数据");
    }

    /**
     * 获取触发器详细信息
     */
    @PreAuthorize("@ss.hasPermi('rva:trigger:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(rvaTriggerService.selectRvaTriggerById(id));
    }

    /**
     * 新增触发器
     */
    @PreAuthorize("@ss.hasPermi('rva:trigger:add')")
    @Log(title = "触发器", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RvaTrigger rvaTrigger)
    {
        return toAjax(rvaTriggerService.insertRvaTrigger(rvaTrigger));
    }

    /**
     * 修改触发器
     */
    @PreAuthorize("@ss.hasPermi('rva:trigger:edit')")
    @Log(title = "触发器", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RvaTrigger rvaTrigger)
    {
        return toAjax(rvaTriggerService.updateRvaTrigger(rvaTrigger));
    }

    /**
     * 删除触发器
     */
    @PreAuthorize("@ss.hasPermi('rva:trigger:remove')")
    @Log(title = "触发器", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(rvaTriggerService.deleteRvaTriggerByIds(ids));
    }
}
