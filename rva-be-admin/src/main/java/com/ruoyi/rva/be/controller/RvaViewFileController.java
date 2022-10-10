package com.ruoyi.rva.be.controller;

import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaView;
import com.ruoyi.rva.framework.service.IRvaViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/rva/view")
public class RvaViewFileController {

    @Autowired
    private View rvaListExportExcelView;

    @Autowired
    private IRvaViewService rvaViewService;

    @PostMapping("/export")
    public ModelAndView exportExcel(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();

        RvaMap<String, Object> rvaMap = new RvaMap<>();
        for (String key : parameterMap.keySet()) {
            rvaMap.put(key, parameterMap.get(key)[0]);
        }
        String viewId = request.getParameter("button[viewId]");
        RvaMap listViewData = rvaViewService.selectListViewData(viewId, null, rvaMap.rvaPut("pageSize", 100000L));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(rvaListExportExcelView);
        modelAndView.addAllObjects(listViewData);
        return modelAndView;
    }

    @RequestMapping("/template/download")
    public ModelAndView download(String createViewId) {
        RvaView rvaView = rvaViewService.selectRvaViewById(createViewId, new RvaMap<>());
        RvaMap rvaMap = new RvaMap<>();
        rvaMap.put("viewData", rvaView);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(rvaListExportExcelView);
        modelAndView.addAllObjects(rvaMap);
        return modelAndView;
    }
}
