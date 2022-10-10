package com.ruoyi.rva.dingtalk.service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.request.OapiV2UserGetuserinfoRequest;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.api.response.OapiV2UserGetuserinfoResponse;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.ISysUserService;
import com.taobao.api.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.ruoyi.rva.dingtalk.config.UrlConstant.URL_GET_USER_INFO;
import static com.ruoyi.rva.dingtalk.config.UrlConstant.URL_USER_GET;

/**
 * 钉钉认证服务
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaDingtalkAuthService {

    private final RvaDingtalkTokenService tokenService;

    private final SysUserMapper userMapper;

    private final SysLoginService loginService;

    private final ISysUserService userService;

    /**
     * https://open.dingtalk.com/document/orgapp-server/enterprise-internal-application-logon-free
     * @param authCode
     * @return
     */
    public String login(String authCode) {
        String accessToken = tokenService.getAccessToken();
        // 获取用户userId
        String userId = getUserInfo(accessToken, authCode);
        SysUser sysUser = userMapper.selectUserByDing(userId);
        if (sysUser == null) {
            sysUser = getUser(accessToken, userId);
            userService.insertUser(sysUser);
        }
        return loginService.loginByNpr(sysUser.getUserName());
    }

    /**
     * 访问/user/getuserinfo接口获取用户userId
     *
     * @param accessToken access_token
     * @param authCode    临时授权码
     * @return 用户userId或错误信息
     */
    private String getUserInfo(String accessToken, String authCode) {
        DingTalkClient client = new DefaultDingTalkClient(URL_GET_USER_INFO);
        OapiV2UserGetuserinfoRequest request = new OapiV2UserGetuserinfoRequest();
        request.setCode(authCode);

        OapiV2UserGetuserinfoResponse response;
        try {
            response = client.execute(request, accessToken);
        } catch (ApiException e) {
            throw new RuntimeException(e.getErrMsg() + " - " + URL_GET_USER_INFO + ":" + e.getErrCode(), e);
        }
        if (!response.isSuccess()) {
            throw new RuntimeException(response.getErrmsg() + ":" + response.getErrorCode());
        }
        return response.getResult().getUserid();
    }

    /**
     * 访问/user/get 获取用户名称
     *
     * @param accessToken access_token
     * @param userId      用户userId
     * @return 用户名称或错误信息
     */
    private SysUser getUser(String accessToken, String userId) {
        DingTalkClient client = new DefaultDingTalkClient(URL_USER_GET);
        OapiV2UserGetRequest request = new OapiV2UserGetRequest();
        request.setUserid(userId);
        OapiV2UserGetResponse response;
        try {
            response = client.execute(request, accessToken);
        } catch (ApiException e) {
            throw new RuntimeException(e.getErrMsg() + " - " + URL_USER_GET + ":" + e.getErrCode(), e);
        }
        if (!response.isSuccess()) {
            throw new RuntimeException(response.getErrmsg() + ":" + response.getErrorCode());
        }
        OapiV2UserGetResponse.UserGetResponse result = response.getResult();
        SysUser user = new SysUser();
        user.setDingId(result.getUserid());
        user.setDingUnionId(result.getUnionid());
        user.setUserName(result.getName());
        user.setAvatar(result.getAvatar());
        user.setPhonenumber(result.getMobile());
        user.setEmail(result.getEmail());
        user.setNickName(result.getName());
        user.setDeptId(result.getDeptIdList().get(0));
        user.setRoleIds(new Long[] {3L});
        return user;
    }
}
