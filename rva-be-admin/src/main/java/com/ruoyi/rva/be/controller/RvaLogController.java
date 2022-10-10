package com.ruoyi.rva.be.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.rva.framework.domain.RvaLog;
import com.ruoyi.rva.framework.service.IRvaLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 日志Controller
 * 
 * @author jiaodacailei
 * @date 2022-03-20
 */
@RestController
@RequestMapping("/framework/log")
public class RvaLogController extends BaseController
{
    @Autowired
    private IRvaLogService rvaLogService;

    /**
     * 查询日志列表
     */
    @PreAuthorize("@ss.hasPermi('framework:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(RvaLog rvaLog)
    {
        startPage();
        List<RvaLog> list = rvaLogService.selectRvaLogList(rvaLog);
        return getDataTable(list);
    }

    /**
     * 导出日志列表
     */
    @PreAuthorize("@ss.hasPermi('framework:log:export')")
    @Log(title = "日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RvaLog rvaLog)
    {
        List<RvaLog> list = rvaLogService.selectRvaLogList(rvaLog);
        ExcelUtil<RvaLog> util = new ExcelUtil<RvaLog>(RvaLog.class);
        util.exportExcel(response, list, "日志数据");
    }

    /**
     * 获取日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('framework:log:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(rvaLogService.selectRvaLogById(id));
    }

    /**
     * 新增日志
     */
    @PreAuthorize("@ss.hasPermi('framework:log:add')")
    @Log(title = "日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RvaLog rvaLog)
    {
        return toAjax(rvaLogService.insertRvaLog(rvaLog));
    }

    /**
     * 修改日志
     */
    @PreAuthorize("@ss.hasPermi('framework:log:edit')")
    @Log(title = "日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RvaLog rvaLog)
    {
        return toAjax(rvaLogService.updateRvaLog(rvaLog));
    }

    /**
     * 删除日志
     */
    @PreAuthorize("@ss.hasPermi('framework:log:remove')")
    @Log(title = "日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(rvaLogService.deleteRvaLogByIds(ids));
    }
}
