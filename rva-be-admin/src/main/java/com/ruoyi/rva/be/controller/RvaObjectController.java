package com.ruoyi.rva.be.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.rva.framework.domain.RvaObject;
import com.ruoyi.rva.framework.service.IRvaObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 对象Controller
 * 
 * @author jiaodacailei
 * @date 2021-11-30
 */
@RestController
@RequestMapping("/rva/object")
public class RvaObjectController extends BaseController
{
    @Autowired
    private IRvaObjectService rvaObjectService;

    /**
     * 查询对象列表
     */
    @PreAuthorize("@ss.hasPermi('rva:object:list')")
    @GetMapping("/list")
    public TableDataInfo list(RvaObject rvaObject)
    {
        startPage();
        List<RvaObject> list = rvaObjectService.selectRvaObjectList(rvaObject);
        return getDataTable(list);
    }

    /**
     * 导出对象列表
     */
    @PreAuthorize("@ss.hasPermi('rva:object:export')")
    @Log(title = "对象", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RvaObject rvaObject)
    {
        List<RvaObject> list = rvaObjectService.selectRvaObjectList(rvaObject);
        ExcelUtil<RvaObject> util = new ExcelUtil<RvaObject>(RvaObject.class);
        util.exportExcel(response, list, "对象数据");
    }

    /**
     * 获取对象详细信息
     */
    @PreAuthorize("@ss.hasPermi('rva:object:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(rvaObjectService.selectRvaObjectById(id));
    }

    /**
     * 新增对象
     */
    @PreAuthorize("@ss.hasPermi('rva:object:add')")
    @Log(title = "对象", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RvaObject rvaObject)
    {
        return toAjax(rvaObjectService.insertRvaObject(rvaObject));
    }

    /**
     * 修改对象
     */
    @PreAuthorize("@ss.hasPermi('rva:object:edit')")
    @Log(title = "对象", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RvaObject rvaObject)
    {
        return toAjax(rvaObjectService.updateRvaObject(rvaObject));
    }

    /**
     * 删除对象
     */
    @PreAuthorize("@ss.hasPermi('rva:object:remove')")
    @Log(title = "对象", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(rvaObjectService.deleteRvaObjectByIds(ids));
    }
}
