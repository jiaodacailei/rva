package com.ruoyi.rva.be.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.service.IRvaObjectFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 收藏对象Controller
 *
 * @author jiaodacailei
 * @date 2021-07-27
 */
@RestController
@RequestMapping("/rva/favorite")
public class RvaObjectFavoriteController extends BaseController
{
    @Autowired
    private IRvaObjectFavoriteService rvaObjectFavoriteService;


    @PostMapping("/toggle")
    public AjaxResult toggle(@RequestBody RvaMap<String, Object> req)
    {
        rvaObjectFavoriteService.toggle(req);
        return AjaxResult.success();
    }


}
