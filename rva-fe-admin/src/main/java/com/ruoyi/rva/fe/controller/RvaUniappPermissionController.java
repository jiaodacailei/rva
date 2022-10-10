package com.ruoyi.rva.fe.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.utils.SecurityUtils;
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
import com.ruoyi.rva.fe.domain.RvaUniappPermission;
import com.ruoyi.rva.fe.service.IRvaUniappPermissionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 移动应用权限Controller
 *
 * @author jiaodacailei
 * @date 2022-05-12
 */
@RestController
@RequestMapping("/rva/fe/permission")
public class RvaUniappPermissionController extends BaseController {
    @Autowired
    private IRvaUniappPermissionService rvaUniappPermissionService;


    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters(String uniAppId) {
        Long userId = SecurityUtils.getUserId();
        List<RvaUniappPermission> menus = rvaUniappPermissionService.selectMenuTreeByUserId(userId, uniAppId);
        //rvaUniappPermissionService.buildMenus(menus)
        return AjaxResult.success(menus);
    }

}
