package com.ruoyi.rva.fe.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.rva.fe.service.IRvaFeListRuntimeService;
import com.ruoyi.rva.fe.service.IRvaFeListService;
import com.ruoyi.rva.framework.domain.RvaMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 前端列表运行时接口
 *
 * @author jiaodacailei
 * @date 2021-07-27
 */
@RestController
@RequestMapping("/rva/fe/list")
public class RvaFeListRuntimeController extends BaseController {

    @Autowired
    private IRvaFeListRuntimeService listRuntimeService;

    /**
     * 获取RvaFeList元数据，包含RvaFeListItem数据
     * @param id
     * @return
     */
    @GetMapping("/{listId}")
    public AjaxResult getInfo(@PathVariable("listId") String id) {
        return AjaxResult.success(listRuntimeService.selectMeta(id));
    }

    /**
     *
     * @param listId
     * @param req
     * @return
     */
    @PostMapping("/{listId}/load/list")
    public TableDataInfo list(@PathVariable("listId") String listId, @RequestBody Map<String, Object> req) {
        return getDataTable(listRuntimeService.selectList(listId, new RvaMap(req)));
    }
}
