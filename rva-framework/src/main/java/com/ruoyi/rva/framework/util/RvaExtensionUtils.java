package com.ruoyi.rva.framework.util;

import java.util.ArrayList;
import java.util.List;

public class RvaExtensionUtils {

    public static final int MAX = 100;

    public static <T> List<T> getExtensions (Class<T> cls, String beanPrefix, String id) {
        List<T> results = new ArrayList<>();
        if (RvaUtils.isNotEmpty(id)) {
            RvaUtils.getBean(beanPrefix + id).ifPresent(e -> results.add((T)e));
        }
        for (int i = 0; i < MAX; i++) {
            String beanId = beanPrefix + i;
            if (RvaUtils.isNotEmpty(id)) {
                beanId += "." + id;
            }
            RvaUtils.getBean(beanId).ifPresent(e -> results.add((T)e));
        }
        return results;
    }

    public static <T> List<T> getExtensions (Class<T> cls, String beanPrefix) {
        return getExtensions (cls, beanPrefix, null);
    }
}
