package com.ruoyi.rva.be.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.rva.framework.domain.RvaAppitem;
import com.ruoyi.rva.framework.service.IRvaAppitemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 应用Controller
 * 
 * @author jiaodacailei
 * @date 2021-09-05
 */
@RestController
@RequestMapping("/rva/appitem")
public class RvaAppitemController extends BaseController
{
    @Autowired
    private IRvaAppitemService rvaAppitemService;

    /**
     * 查询应用列表
     */
    @PreAuthorize("@ss.hasPermi('rva:appitem:list')")
    @GetMapping("/list")
    public TableDataInfo list(RvaAppitem rvaAppitem)
    {
        startPage();
        List<RvaAppitem> list = rvaAppitemService.selectRvaAppitemList(rvaAppitem);
        return getDataTable(list);
    }

    /**
     * 导出应用列表
     */
    @PreAuthorize("@ss.hasPermi('rva:appitem:export')")
    @Log(title = "应用", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(RvaAppitem rvaAppitem)
    {
        List<RvaAppitem> list = rvaAppitemService.selectRvaAppitemList(rvaAppitem);
        ExcelUtil<RvaAppitem> util = new ExcelUtil<RvaAppitem>(RvaAppitem.class);
        return util.exportExcel(list, "应用数据");
    }

    /**
     * 获取应用详细信息
     */
    @PreAuthorize("@ss.hasPermi('rva:appitem:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(rvaAppitemService.selectRvaAppitemById(id));
    }

    /**
     * 新增应用
     */
    @PreAuthorize("@ss.hasPermi('rva:appitem:add')")
    @Log(title = "应用", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RvaAppitem rvaAppitem)
    {
        return toAjax(rvaAppitemService.insertRvaAppitem(rvaAppitem));
    }

    /**
     * 修改应用
     */
    @PreAuthorize("@ss.hasPermi('rva:appitem:edit')")
    @Log(title = "应用", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RvaAppitem rvaAppitem)
    {
        return toAjax(rvaAppitemService.updateRvaAppitem(rvaAppitem));
    }

    /**
     * 删除应用
     */
    @PreAuthorize("@ss.hasPermi('rva:appitem:remove')")
    @Log(title = "应用", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(rvaAppitemService.deleteRvaAppitemByIds(ids));
    }
}
