package com.ruoyi.rva.be.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.rva.framework.service.IRvaAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 应用Controller
 * 
 * @author jiaodacailei
 * @date 2021-07-27
 */
@RestController
@RequestMapping("/rva/app")
public class RvaAppController extends BaseController
{
    @Autowired
    private IRvaAppService rvaAppService;

    @RequestMapping("/{app}")
    public AjaxResult getInfo(@PathVariable("app") String ids)
    {
        return AjaxResult.success(rvaAppService.selectRvaAppById(ids));
    }

}
