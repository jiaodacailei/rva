package com.ruoyi.rva.dingtalk.extension;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.rva.dingtalk.domain.UserDTO;

public interface RvaDingtalkUserCreator {

    String BEAN_FREFIX = "rva.dingtalk.user.creator.";

    SysUser create(UserDTO user);
}
