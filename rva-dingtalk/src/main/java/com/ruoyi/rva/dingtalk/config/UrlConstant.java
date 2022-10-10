package com.ruoyi.rva.dingtalk.config;

/**
 * top地址相关配置
 *
 * @author openapi@dingtalk
 * @date 2020/2/4
 */
public class UrlConstant {
    private static final String HOST = "https://oapi.dingtalk.com";

    /**
     * 钉钉网关 gettoken 地址
     */
    public static final String URL_GET_TOKEN = HOST + "/gettoken";

    /**
     * 获取 jsapi_ticket 地址
     */
    public static final String URL_GET_JSTICKET = HOST + "/get_jsapi_ticket";

    /**
     * 获取用户在企业内 userId 的接口URL
     */
    public static final String URL_GET_USER_INFO = HOST + "/topapi/v2/user/getuserinfo";

    /**
     * 获取用户姓名的接口URL
     */
    public static final String URL_USER_GET = HOST + "/topapi/v2/user/get";

    /**
     * 发送消息
     */
    public static final String URL_SEND_MSG = HOST + "/topapi/message/corpconversation/asyncsend_v2";

    /**
     * 获取部门用户接口URL
     */
    public static final String URL_USER_SIMPLELIST = HOST + "/user/simplelist";

}
