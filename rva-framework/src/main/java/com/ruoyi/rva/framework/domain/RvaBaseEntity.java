package com.ruoyi.rva.framework.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.rva.framework.service.IRvaSystemService;
import com.ruoyi.rva.framework.util.RvaUtils;

import java.util.Date;
import java.util.List;

public class RvaBaseEntity extends BaseEntity {

    public void setData(String data) {}

    public String getData() {
        return null;
    }

    public void setJsonProperty (String property, Object value) {
        String data = RvaUtils.setJsonProperty(getData(), property, value);
        setData(data);
    }

    public String getJsonPropertyString (String key) {
        return getJsonPropertyString (key, null);
    }

    public String getJsonPropertyString (String key, String defaultVal) {
        String value = RvaUtils.parseMap(getData()).getString(key);
        if (RvaUtils.isEmpty(value)) {
            return defaultVal;
        }
        return value;
    }

    public int getJsonPropertyInt (String key, int defaultVal) {
        String value = getJsonPropertyString (key, null);
        return RvaUtils.getInt(value,defaultVal);
    }

    public List<String> getJsonPropertyStringList (String key) {
        return RvaUtils.parseMap(getData()).getStringList(key);
    }

    public RvaMap getJsonPropertyMap (String key) {
        return RvaUtils.parseMap(getData()).getMap(key);
    }

    public void setCreateUpdateInfo () {
        Long userId = SpringUtils.getBean(IRvaSystemService.class).getLoginUser().getUserId();
        setCreateBy(userId.toString());
        setUpdateBy(userId.toString());
        setCreateTime(new Date());
        setUpdateTime(getCreateTime());
    }

    public void setUpdateInfo () {
        Long userId = SpringUtils.getBean(IRvaSystemService.class).getLoginUser().getUserId();
        setUpdateBy(userId.toString());
        setUpdateTime(new Date());
    }
}
