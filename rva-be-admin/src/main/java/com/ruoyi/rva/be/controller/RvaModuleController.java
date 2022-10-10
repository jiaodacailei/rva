package com.ruoyi.rva.be.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.rva.framework.domain.RvaModule;
import com.ruoyi.rva.framework.service.IRvaModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 模块Controller
 * 
 * @author jiaodacailei
 * @date 2021-07-27
 */
@RestController
@RequestMapping("/rva/module")
public class RvaModuleController extends BaseController
{
    @Autowired
    private IRvaModuleService rvaModuleService;

    /**
     * 查询模块列表
     */
    @PreAuthorize("@ss.hasPermi('rva:module:list')")
    @GetMapping("/list")
    public TableDataInfo list(RvaModule rvaModule)
    {
        startPage();
        List<RvaModule> list = rvaModuleService.selectRvaModuleList(rvaModule);
        return getDataTable(list);
    }

    /**
     * 导出模块列表
     */
    @PreAuthorize("@ss.hasPermi('rva:module:export')")
    @Log(title = "模块", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(RvaModule rvaModule)
    {
        List<RvaModule> list = rvaModuleService.selectRvaModuleList(rvaModule);
        ExcelUtil<RvaModule> util = new ExcelUtil<RvaModule>(RvaModule.class);
        return util.exportExcel(list, "模块数据");
    }

    /**
     * 获取模块详细信息
     */
    @PreAuthorize("@ss.hasPermi('rva:module:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(rvaModuleService.selectRvaModuleById(id));
    }

    /**
     * 新增模块
     */
    @PreAuthorize("@ss.hasPermi('rva:module:add')")
    @Log(title = "模块", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RvaModule rvaModule)
    {
        return toAjax(rvaModuleService.insertRvaModule(rvaModule));
    }

    /**
     * 修改模块
     */
    @PreAuthorize("@ss.hasPermi('rva:module:edit')")
    @Log(title = "模块", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RvaModule rvaModule)
    {
        return toAjax(rvaModuleService.updateRvaModule(rvaModule));
    }

    /**
     * 删除模块
     */
    @PreAuthorize("@ss.hasPermi('rva:module:remove')")
    @Log(title = "模块", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(rvaModuleService.deleteRvaModuleByIds(ids));
    }
}
