package com.ruoyi.rva.framework.util;

public interface RvaConstants {

    /**
     * 登录用户的key，用于Velocity表达式中
     */
    String LOGIN_USER = "rvaLoginUser";

    String DATE_UTILS = "rvaDateUtils";

    String UTILS = "rvaUtils";

    String PINYIN_UTILS = "rvaPinyinUtils";

    /**
     * 登录用户的Velocity表达式
     */
    String EXPRESSION_LOGIN_USER = String.format("${%s.userId}", LOGIN_USER);

    /**
     * 当前时间的Velocity表达式
     */
    String EXPRESSION_NOW = String.format("${%s.now}", DATE_UTILS);

    /**
     * 列表上根据用户id获取名称
     */
    String EXPRESSION_SQL_USER_NAME = "#if(${currentPropValue}) select user_name from sys_user where user_id = '${currentPropValue}' #end";

    String EXPRESSION_LOGIN_ADMIN = String.format("$%s.userId == 1", LOGIN_USER);

    String PARAM_ORDER_BY = "orderByList";

    String MSG_FORMAT = "‘%s’格式不正确，请参考：%s";

    String MSG_PARSE = "数据解析出错：%s";

    String MSG_REQUIRED = "‘%s’不允许为空！";

    String MSG_REQUIRED_ALL = "%s不允许都为空！";

    String MSG_EXISTS = "‘%s’已存在！";

    String MSG_NOT_SET = "‘%s’未设置！";

    String MSG_SET = "‘%s’设置不正确！";

    String MSG_CALL = "调用方法%s失败！";

    String MSG_QUERY = "未查询到‘%s’数据：%s！";

    String PROP_KEY_ID = "keyPropId";

    String PROP_KEY_VALUE = "keyPropValue";

    String PROP_NAME_ID = "namePropId";

    String PROP_NAME_VALUE = "namePropValue";
}
