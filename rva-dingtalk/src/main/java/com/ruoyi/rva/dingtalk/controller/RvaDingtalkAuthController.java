package com.ruoyi.rva.dingtalk.controller;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.rva.dingtalk.config.AppConfig;
import com.ruoyi.rva.dingtalk.domain.ConfigDTO;
import com.ruoyi.rva.dingtalk.service.RvaDingtalkAuthService;
import com.ruoyi.rva.dingtalk.service.RvaDingtalkTokenService;
import com.ruoyi.rva.dingtalk.util.JsApiSignature;
import com.ruoyi.rva.framework.domain.RvaMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 企业内部应用免登录：https://open.dingtalk.com/document/orgapp-server/enterprise-internal-application-logon-free，
 * 内网穿透：https://open.dingtalk.com/document/resourcedownload/http-intranet-penetration
 */
@RestController
@RequestMapping("/rva/dingtalk")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaDingtalkAuthController extends BaseController {

    private final RvaDingtalkAuthService authService;

    private final RvaDingtalkTokenService tokenService;

    private final AppConfig appConfig;

    /**
     * 钉钉用户登录，显示当前登录用户的token
     *
     * @param req req.authCode为临时免登码
     * @return token
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody RvaMap req) {
        AjaxResult ajax = AjaxResult.success();
        String token = authService.login(req.getString("authCode"));
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    @PostMapping("/config")
    public AjaxResult config(@RequestParam String url) {
        ConfigDTO config = new ConfigDTO();
        String jsTicket = tokenService.getJsTicket();
        config.setAgentId(appConfig.getAgentId());
        config.setCorpId(appConfig.getCorpId());
        config.setJsticket(jsTicket);
        config.setNonceStr(JsApiSignature.genNonce());
        config.setTimeStamp(System.currentTimeMillis() / 1000);
        String sign = JsApiSignature.sign(url, config.getNonceStr(), config.getTimeStamp(), config.getJsticket());
        config.setSignature(sign);
        return AjaxResult.success(config);
    }
}
