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
import com.ruoyi.rva.framework.domain.RvaTenant;
import com.ruoyi.rva.framework.service.IRvaTenantService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 租户Controller
 * 
 * @author jiaodacailei
 * @date 2022-03-05
 */
@RestController
@RequestMapping("/framework/tenant")
public class RvaTenantController extends BaseController
{
    @Autowired
    private IRvaTenantService rvaTenantService;

    /**
     * 查询租户列表
     */
    @PreAuthorize("@ss.hasPermi('framework:tenant:list')")
    @GetMapping("/list")
    public TableDataInfo list(RvaTenant rvaTenant)
    {
        startPage();
        List<RvaTenant> list = rvaTenantService.selectRvaTenantList(rvaTenant);
        return getDataTable(list);
    }

    /**
     * 导出租户列表
     */
    @PreAuthorize("@ss.hasPermi('framework:tenant:export')")
    @Log(title = "租户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RvaTenant rvaTenant)
    {
        List<RvaTenant> list = rvaTenantService.selectRvaTenantList(rvaTenant);
        ExcelUtil<RvaTenant> util = new ExcelUtil<RvaTenant>(RvaTenant.class);
        util.exportExcel(response, list, "租户数据");
    }

    /**
     * 获取租户详细信息
     */
    @PreAuthorize("@ss.hasPermi('framework:tenant:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(rvaTenantService.selectRvaTenantById(id));
    }

    /**
     * 新增租户
     */
    @PreAuthorize("@ss.hasPermi('framework:tenant:add')")
    @Log(title = "租户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RvaTenant rvaTenant)
    {
        return toAjax(rvaTenantService.insertRvaTenant(rvaTenant));
    }

    /**
     * 修改租户
     */
    @PreAuthorize("@ss.hasPermi('framework:tenant:edit')")
    @Log(title = "租户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RvaTenant rvaTenant)
    {
        return toAjax(rvaTenantService.updateRvaTenant(rvaTenant));
    }

    /**
     * 删除租户
     */
    @PreAuthorize("@ss.hasPermi('framework:tenant:remove')")
    @Log(title = "租户", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(rvaTenantService.deleteRvaTenantByIds(ids));
    }
}
