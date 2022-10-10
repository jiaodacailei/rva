package com.ruoyi.rva.be.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.rva.framework.domain.RvaRelationitem;
import com.ruoyi.rva.framework.service.IRvaRelationitemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 关系项Controller
 * 
 * @author jiaodacailei
 * @date 2021-07-27
 */
@RestController
@RequestMapping("/rva/relationitem")
public class RvaRelationitemController extends BaseController
{
    @Autowired
    private IRvaRelationitemService rvaRelationitemService;

    /**
     * 查询关系项列表
     */
    @PreAuthorize("@ss.hasPermi('rva:relationitem:list')")
    @GetMapping("/list")
    public TableDataInfo list(RvaRelationitem rvaRelationitem)
    {
        startPage();
        List<RvaRelationitem> list = rvaRelationitemService.selectRvaRelationitemList(rvaRelationitem);
        return getDataTable(list);
    }

    /**
     * 导出关系项列表
     */
    @PreAuthorize("@ss.hasPermi('rva:relationitem:export')")
    @Log(title = "关系项", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(RvaRelationitem rvaRelationitem)
    {
        List<RvaRelationitem> list = rvaRelationitemService.selectRvaRelationitemList(rvaRelationitem);
        ExcelUtil<RvaRelationitem> util = new ExcelUtil<RvaRelationitem>(RvaRelationitem.class);
        return util.exportExcel(list, "关系项数据");
    }

    /**
     * 获取关系项详细信息
     */
    @PreAuthorize("@ss.hasPermi('rva:relationitem:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(rvaRelationitemService.selectRvaRelationitemById(id));
    }

    /**
     * 新增关系项
     */
    @PreAuthorize("@ss.hasPermi('rva:relationitem:add')")
    @Log(title = "关系项", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RvaRelationitem rvaRelationitem)
    {
        return toAjax(rvaRelationitemService.insertRvaRelationitem(rvaRelationitem));
    }

    /**
     * 修改关系项
     */
    @PreAuthorize("@ss.hasPermi('rva:relationitem:edit')")
    @Log(title = "关系项", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RvaRelationitem rvaRelationitem)
    {
        return toAjax(rvaRelationitemService.updateRvaRelationitem(rvaRelationitem));
    }

    /**
     * 删除关系项
     */
    @PreAuthorize("@ss.hasPermi('rva:relationitem:remove')")
    @Log(title = "关系项", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(rvaRelationitemService.deleteRvaRelationitemByIds(ids));
    }
}
