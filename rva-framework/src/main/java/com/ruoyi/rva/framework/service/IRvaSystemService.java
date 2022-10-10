package com.ruoyi.rva.framework.service;

import com.ruoyi.common.core.domain.entity.SysUser;

import java.util.List;
import java.util.Map;

public interface IRvaSystemService {

    SysUser getLoginUser ();

    @Deprecated
    void startPage ();

    List<Map> getOrderBys ();

    /**
     * 黑名单属性
     * @return
     */
    List<String> getPropertyBlacklist();

    String getFilePrefix();

    boolean isOssUpload();
}
