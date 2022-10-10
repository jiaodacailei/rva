package com.ruoyi.rva.framework.util;

import com.ruoyi.common.utils.spring.SpringUtils;
import org.springframework.core.env.Environment;

import java.util.List;


public class RvaSpringUtils {

    private static Environment getEnv() {
        return SpringUtils.getBean(Environment.class);
    }

    /**
     * @param key
     * @return
     */
    public static String getEnvProperty(String key) {
        return getEnv().getProperty(key);
    }

    public static Integer getEnvPropertyInt(String key) {
        return getEnv().getProperty(key, Integer.class);
    }

    public static Long getEnvPropertyLong(String key) {
        return getEnv().getProperty(key, Long.class);
    }

    public static List<String> getEnvPropertyList(String key) {
        return getEnv().getProperty(key, List.class);
    }
}
