package com.ruoyi.rva.framework.util;

import com.ruoyi.common.constant.Constants;
import java.lang.RuntimeException;

import com.ruoyi.rva.framework.service.IRvaSystemService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.anakia.Escape;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * The summery of the class.
 *
 * @author cailei
 * @version 1.0
 */
@Slf4j
@Component
public class RvaVelocityUtils {

    private final static String ENCODING_UTF8 = "UTF-8";

    private VelocityEngine velocity = new VelocityEngine();

    // @Value("${big.platform.velocity}")
    private String dir = "/";

    /**
     *
     */
    @PostConstruct
    private void init() {
        Properties p = new Properties();
        // 加载classpath目录下的vm文件
        p.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        // 定义字符集
        p.setProperty(Velocity.INPUT_ENCODING, Constants.UTF8);
        p.setProperty(Velocity.OUTPUT_ENCODING, Constants.UTF8);
        // 初始化Velocity引擎，指定配置Properties
        p.setProperty("runtime.references.strict", "true");
        velocity.init(p);
    }

    private Template getTemplate(String tmplFileName) {
        Template template = null;
        try {
            template = velocity.getTemplate(tmplFileName, ENCODING_UTF8);
        } catch (Exception e1) {
            throw new RuntimeException("获取模板文件失败:" + tmplFileName, e1);
        }
        return template;
    }

    private VelocityContext getContext(Map<String, Object> map) {
        VelocityContext context = new VelocityContext();
        context.put(RvaConstants.DATE_UTILS, new RvaDateUtils());
        context.put(RvaConstants.UTILS, new RvaUtils());
        context.put(RvaConstants.PINYIN_UTILS, new RvaPinyinUtils());
        if (map == null || map.size() == 0) {
            return context;
        }
        for (String key : map.keySet()) {
            context.put(key, map.get(key));
        }

        return context;
    }

    /**
     * @param tmplFileName
     * @param map
     * @return
     */
    public String mergeToString(String tmplFileName, Map<String, Object> map) {
        VelocityContext context = getContext(map);
        Template template = getTemplate(tmplFileName);
        StringWriter writer = null;
        try {
            writer = new StringWriter();
            template.merge(context, writer);
            return writer.getBuffer().toString();
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
            }
        }
    }

    /**
     * @param tmplFilePath
     * @param srcOutputStream
     * @param map
     */
    @SneakyThrows
    private void mergeToOutputStream(String tmplFilePath, OutputStream srcOutputStream, Map<String, Object> map) {
        VelocityContext context = getContext(map);
        Template template = getTemplate(tmplFilePath);
        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(srcOutputStream, ENCODING_UTF8);
            template.merge(context, writer);
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
            }
        }
    }

    /**
     * @param tmplFilePath
     * @param srcFilePath
     * @param map
     */
    @SneakyThrows
    public void mergeToFile(String tmplFilePath, String srcFilePath, Map<String, Object> map) {
        log.info("srcFilePath:" + srcFilePath);
        File srcFile = new File(srcFilePath);
        srcFile.getParentFile().mkdirs();
        if (!srcFile.exists()) {
            srcFile.createNewFile();
        }
        mergeToOutputStream(tmplFilePath, new FileOutputStream(srcFile), map);
    }

    @Autowired
    private IRvaSystemService systemService;


    /**
     * map中设置当前登录用户
     * @param tmplString
     * @param map
     * @return
     */
    public String parseWithLoginUser(String tmplString, Map<String, Object> map) {
        map.put(RvaConstants.LOGIN_USER, systemService.getLoginUser());
        return parse(tmplString, map);
    }

    /**
     * @param tmplString velocity逻辑表达式
     * @param map
     * @return
     */
    public boolean validateWithLoginUser (String tmplString, Map<String, Object> map) {
        if (RvaUtils.isEmpty(tmplString)) {
            return true;
        }
        String result = parseWithLoginUser(String.format("#if(%s) true #else false #end", tmplString), map);
        return "true".equals(result);
    }

    /**
     * @param tmplString
     * @param map
     * @return
     */
    public String parse(String tmplString, Map<String, Object> map) {
        if (RvaUtils.isEmpty(tmplString)) {
            return null;
        }
        Map<String, Object> map2 = new HashMap<>(map);
        for (String key : map.keySet()) {
            int index = key.indexOf('_');
            if (index > 0 && Pattern.matches("[uc][c]?[x0-9][0-9]*_", key.substring(0, index + 1))) {
                String newKey = key.substring(index + 1);
                if (map.containsKey(newKey)) {
                    continue;
                }
                map2.put(newKey, map.get(key));
            }
        }
        // log.info("map:\n" + map2);
        VelocityContext context = getContext(map2);
        StringWriter sw = null;
        try {
            sw = new StringWriter();
            velocity.evaluate(context, sw, tmplString, tmplString);
            log.info("parseResult:" + sw.toString());
            return sw.toString().trim();
        } catch (Exception e) {
            log.warn("parse error:" + tmplString, e);
            return tmplString;
        } finally {
            try {
                sw.close();
            } catch (Exception ex) {
            }
        }
    }

    /**
     * @param s
     * @return
     */
    public String getText(String s) {
        if (s == null) {
            return "";
        }
        return Escape.getText(s);
    }

    public static void main(String[] args) {
        Boolean l0_rva_view = Pattern.matches("[uc][x0-9][0-9]*_", "u0_");
        log.info(l0_rva_view.toString());
    }
}
