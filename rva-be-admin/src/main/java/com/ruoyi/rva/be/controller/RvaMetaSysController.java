package com.ruoyi.rva.be.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.service.IRvaMetaChartService;
import com.ruoyi.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统管理Controller
 *
 * @author jiaodacailei
 * @date 2022-03-04
 */
@RestController
@RequestMapping("/rva/meta/sys")
public class RvaMetaSysController extends BaseController {
    @Autowired
    private ISysConfigService configService;

    /**
     * 刷新参数缓存
     *
     * @return
     */
    @PostMapping("/config/refreshCache")
    public AjaxResult refreshCache() {
        configService.resetConfigCache();
        return AjaxResult.success();
    }
}
