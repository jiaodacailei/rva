package com.ruoyi.rva.be.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.service.IRvaViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 视图Controller
 *
 * @author jiaodacailei
 * @date 2021-09-06
 */
@Controller
@RequestMapping("/rva/print")
public class RvaPrintController extends BaseController {
    @Autowired
    private IRvaViewService rvaViewService;


    @RequestMapping("/{beanName}")
    public void print(@PathVariable("beanName") String beanName, @RequestParam Map<String, Object> req, HttpServletResponse response, HttpServletRequest request) {
        rvaViewService.print(response,beanName, new RvaMap<String,Object>(req) );
    }
}
