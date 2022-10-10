package com.ruoyi.rva.fe.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.rva.fe.domain.RvaUniappMsgUser;
import com.ruoyi.rva.fe.service.IRvaUniappMsgUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息用户关联Controller
 *
 * @author jiaodacailei
 * @date 2022-05-27
 */
@RestController
@RequestMapping("/fe/rvaUniappMsgUser")
public class RvaUniappMsgUserController extends BaseController
{
    @Autowired
    private IRvaUniappMsgUserService rvaUniappMsgUserService;

    /**
     * 查询消息用户关联列表
     */
    @GetMapping("/list")
    public TableDataInfo list(RvaUniappMsgUser rvaUniappMsgUser)
    {
        startPage();
        Long userId = SecurityUtils.getUserId();
        rvaUniappMsgUser.setUserId(userId);
        List<RvaUniappMsgUser> list = rvaUniappMsgUserService.selectRvaUniappMsgUserList(rvaUniappMsgUser);
        return getDataTable(list);
    }


    /**
     * 获取消息用户关联详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(rvaUniappMsgUserService.selectRvaUniappMsgUserById(id));
    }


    /**
     * 消息阅读
     */

    @Log(title = "消息阅读", businessType = BusinessType.UPDATE)
    @PostMapping("/read")
    public AjaxResult read(Long id) {
        rvaUniappMsgUserService.read(id);
        return AjaxResult.success();
    }


}
