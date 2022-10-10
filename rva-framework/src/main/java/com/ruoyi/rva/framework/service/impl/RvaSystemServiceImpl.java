package com.ruoyi.rva.framework.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.rva.framework.mapper.RvaDataMapper;
import com.ruoyi.rva.framework.util.RvaJsonUtils;
import com.ruoyi.rva.framework.util.RvaUtils;
import com.ruoyi.rva.framework.service.IRvaSystemService;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Transactional
@Service
public class RvaSystemServiceImpl implements IRvaSystemService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private RvaDataMapper dataMapper;

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private ServerConfig serverConfig;

    @Override
    public SysUser getLoginUser() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        if (loginUser == null) {
            // throw new RuntimeException("用户未登录！");
            SysUser admin = userMapper.selectUserById(1L);
            admin.setRoleId(1L);
            return admin;
        }
        return loginUser.getUser();
    }

    @Override
    public void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            // String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize);
        }
    }

    private final static String ORDER_BY_LIST = "orderByList";

    @Override
    public List<Map> getOrderBys() {
        String parameter = ServletUtils.getParameter(ORDER_BY_LIST);
        if (RvaUtils.isEmpty(parameter)) {
            return new ArrayList<>();
        }
        return RvaJsonUtils.readAsList(parameter, Map.class);
    }

    @Override
    public List<String> getPropertyBlacklist() {
        SysUser loginUser = getLoginUser();
        Long[] roleIds = loginUser.getRoleIds();
        if(ObjectUtils.isEmpty(roleIds)){
            return null;
        }
        String str = dataMapper.selectString("SELECT property_blacklist FROM sys_role WHERE role_id=" + roleIds[0]);
        if (RvaUtils.isNotEmpty(str)) {
            List<String> blacklist = RvaUtils.asList(str.split(","));
            return blacklist;
        }
        return null;
    }

    @Override
    public String getFilePrefix() {
        String sysFilePrefix = sysConfigService.selectConfigByKey("sys.file.prefix");
        String url = null;
        if (ObjectUtils.isEmpty(sysFilePrefix) || sysFilePrefix.equals("/")) {//本地
            url = serverConfig.getUrl();
        } else { //阿里云oss
            url = sysFilePrefix;
        }
        return url;
    }

    @Override
    public boolean isOssUpload() {
        String sysFilePrefix = sysConfigService.selectConfigByKey("sys.file.prefix");
        if (ObjectUtils.isEmpty(sysFilePrefix) || sysFilePrefix.equals("/")) {//本地
            return false;
        } else { //阿里云oss
            return true;
        }
    }
}
