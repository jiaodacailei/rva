package com.ruoyi.rva.be.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 获取系统启动参数
 */
@RestController
@RequestMapping("/init")
public class RvaInitController {

    @Autowired
    private ISysConfigService configService;

    @GetMapping
    public AjaxResult getData()
    {
        List<SysConfig> configs = configService.selectConfigList(new SysConfig() {{
            setConfigType("Y");
        }});
        return AjaxResult.success(new RvaMap().rvaPut("config", configs));
    }
}
