package com.ruoyi.rva.framework.util;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.rva.framework.domain.RvaMap;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
public class RvaUtils {

    /**
     * 检查value是否匹配regex
     *
     * @param regex 正则表达式
     * @param value 要检查的内容
     * @return
     */
    public static boolean isMatched(String regex, String value) {
        if (isEmpty(value)) {
            return false;
        }
        return Pattern.matches(regex, value);
    }

    public static boolean isNotMatched(String regex, String value) {
        return !isMatched(regex, value);
    }

    /**
     * 中文空格.
     */
    private final static String CNSPACE = "　";

    /**
     * @param len
     * @return
     */
    public static String getCnSpace(int len) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            sb.append(CNSPACE);
        }
        return sb.toString();
    }

    /**
     * if len is 4 and idx is 23 then re is 0023.
     *
     * @param len
     * @param idx
     * @return re
     */
    public static String generateFixedLenNo(int len, int idx) {
        String re = String.valueOf(idx);
        if (re.length() >= len) {
            return re.substring(0, len);
        }
        len = len - re.length();
        for (int i = 0; i < len; i++) {
            re = '0' + re;
        }
        return re;
    }

    /**
     * @param s
     * @return
     */
    public static Boolean isEmpty(Object s) {
        return (s == null || s.equals("") || s.toString().trim().equals(""));
    }

    /**
     * @param s
     * @return
     */
    public static Boolean isNotEmpty(Object s) {
        return !isEmpty(s);
    }

    /**
     * @param s
     * @return
     */
    public static Boolean isArrayOrListEmpty(Object s) {
        if (isEmpty(s)) {
            return true;
        }
        if (s instanceof Object[]) {
            Object[] objs = (Object[]) s;
            if (objs.length == 0) {
                return true;
            }
            boolean empty = true;
            for (Object obj : objs) {
                if (!isArrayOrListEmpty(obj)) {
                    empty = false;
                }
            }
            return empty;
        }
        if (s instanceof List) {
            List objs = (List) s;
            if (objs.size() == 0) {
                return true;
            }
            boolean empty = true;
            for (Object obj : objs) {
                if (!isArrayOrListEmpty(obj)) {
                    empty = false;
                }
            }
            return empty;
        }
        return false;
    }

    /**
     * @param s
     * @param defaultVal
     * @return
     */
    public static Integer getInt(Object s, Integer defaultVal) {
        if (isEmpty(s)) {
            return defaultVal;
        }
        try {
            return getDouble(s).intValue();
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    /**
     * @param s
     * @return
     */
    public static Integer getInt(Object s) {
        return getInt(s, null);
    }

    /**
     * @param s
     * @param defaultVal
     * @return
     */
    public static Boolean getBoolean(Object s, Boolean defaultVal) {
        if (isEmpty(s)) {
            return defaultVal;
        }
        try {
            return Boolean.valueOf(s.toString());
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    /**
     * @param s
     * @param defaultVal
     * @return
     */
    public static Long getLong(Object s, Long defaultVal) {
        if (isEmpty(s)) {
            return defaultVal;
        }
        try {
            return getDouble(s).longValue();
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    /**
     * @param s
     * @return
     */
    public static Long getLong(Object s) {
        return getLong(s, null);
    }

    /**
     * @param s
     * @param defaultVal
     * @return s == null ? defaultVal : s
     */
    public static Double getDoubleIfNull(Double s, Double defaultVal) {
        return s == null ? defaultVal : s;
    }

    /**
     * @param s
     * @param defaultVal
     * @return
     */
    public static Double getDouble(Object s, Double defaultVal) {
        if (isEmpty(s)) {
            return defaultVal;
        }
        try {
            if (s instanceof BigDecimal) {
                return ((BigDecimal) s).doubleValue();
            }
            String val = s.toString().trim();
            return Double.parseDouble(val);
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    /**
     * @param s
     * @return
     */
    public static Double getDouble(Object s) {
        return getDouble(s, null);
    }

    public static String getString(Object s) {
        return getString(s, null);
    }

    public static String getString(Object s, String defaultString) {
        if (isEmpty(s)) {
            return defaultString;
        }
        return s.toString();
    }

    /**
     * 返回一个double数值的长度，例如：-88.5长度为2
     *
     * @param d
     * @return
     */
    public static int getDoubleLength(Double d) {
        Double val = Double.valueOf(Math.log10(Math.abs(d)));
        return val.intValue() + 1;
    }

    /**
     * @return
     */
    public static String generateKey32() {
        UUID uuid = UUID.randomUUID();
        String key = uuid.toString().replaceAll("-", "");
        return key;
    }

    /**
     * 从32为key值中获取后len位
     *
     * @param len
     * @return
     */
    public static String generateKey32(int len) {
        return generateKey32().substring(32 - len, 32);
    }

    /**
     * @param objs
     * @return
     */
    public static <T> String toString(T[] objs) {
        if (isEmpty(objs)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < objs.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(objs[i].toString());
        }
        return sb.toString();
    }

    /**
     * @param rawString
     * @return
     */
    @SneakyThrows
    public static String getMd5String(String rawString) {
        // 生成一个MD5加密计算摘要
        MessageDigest md = MessageDigest.getInstance("MD5");// MD5 SHA-256...
        // 计算md5函数
        md.update((rawString).getBytes());
        // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
        // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
        return new BigInteger(1, md.digest()).toString(16);
    }

    public static String getMD5(Object salt, Object raw) {
        return getMd5String(salt.toString() + raw.toString());
    }

    private final static String NO = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * 生成下一个编号.
     *
     * @param no  编号
     * @param len 下一个编号可以变化的字符范围，在倒数len位之内
     * @return
     */
    public static String createNextNo(String no, int len) {
        String msg = "no=" + no + ", length=" + len;
        if (isEmpty(no) || no.length() < len) {
            throw new RuntimeException("Wrong format:" + msg);
        }
        char last = no.charAt(no.length() - 1);
        for (int i = 0; i < NO.length(); i++) {
            if (last == NO.charAt(i)) {
                if (i == (NO.length() - 1)) {
                    if (len > 1) {
                        return createNextNo(no.substring(0, no.length() - 1), len - 1) + NO.charAt(NO.length() - 1);
                    }
                    throw new RuntimeException("createNextNo error:" + msg);
                } else {
                    return no.substring(0, no.length() - 1) + NO.charAt(i + 1);
                }
            }
        }
        throw new RuntimeException("createNextNo error:" + msg);
    }

    /**
     * @param in
     * @param out
     */
    public static void copy(InputStream in, OutputStream out) {
        try {
            // 创建一个缓冲区
            byte buffer[] = new byte[1024];
            // 判断输入流中的数据是否已经读完的标识
            int len = 0;
            // 循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
            while ((len = in.read(buffer)) > 0) {
                // 使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\"
                // + filename)当中
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            log.warn("copy(in, out) error", e);
        } finally {
            // 关闭输入流
            try {
                in.close();
            } catch (Exception e) {
            }
            // 关闭输出流
            try {
                out.close();
            } catch (Exception e) {
            }
        }
    }

    @SneakyThrows
    public static <T> T cloneBySetter(Object src, Class<T> tClass) {
        Object target = tClass.newInstance();
        cloneBySetter(src, target);
        return (T) target;
    }

    /**
     * 调用target的setter，其值为src对应的getter
     *
     * @param src
     * @param target
     */
    public static void cloneBySetter(Object src, Object target) {
        if (src == null) {
            log.warn("src is null");
            return;
        }
        if (target == null) {
            log.warn("target is null");
            return;
        }
        Method[] methods = src.getClass().getMethods();
        for (Method srcSetter : methods) {
            if (!srcSetter.getName().startsWith("set") || srcSetter.getParameterTypes().length != 1) {
                continue;
            }
            // 获取getter
            String getter = "get" + srcSetter.getName().substring(3);
            Method srcGetter = null;
            try {
                srcGetter = src.getClass().getMethod(getter);
            } catch (NoSuchMethodException e) {
                log.warn(e.getMessage());
                continue;
            }
            // 获取目标setter
            Method targetSetter = null;
            try {
                targetSetter = target.getClass().getMethod(srcSetter.getName(), srcSetter.getParameterTypes()[0]);
            } catch (NoSuchMethodException e) {
                log.warn(e.getMessage());
                continue;
            }
            // 调用目标setter
            try {
                Object getterValue = srcGetter.invoke(src);
                if (getterValue instanceof Map) {
                    RvaMap hashMap = new RvaMap();
                    hashMap.putAll((Map) getterValue);
                    getterValue = hashMap;
                } else if (getterValue instanceof List) {
                    ArrayList objects = new ArrayList();
                    objects.addAll((List) getterValue);
                    getterValue = objects;
                }
                targetSetter.invoke(target, getterValue);
            } catch (IllegalAccessException e) {
                log.warn(e.getMessage());
                continue;
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    /**
     * @param cls
     * @param pos
     * @return
     */
    @SneakyThrows
    public static List getDtos(Class cls, List pos) {
        List list = new ArrayList();
        if (pos == null || pos.size() == 0) {
            return list;
        }
        for (Object po : pos) {
            // 找只有一个参数的构造函数，并且参数类型是po类型的父类
            for (Constructor c : cls.getConstructors()) {
                if (c.getParameterTypes().length == 1 && c.getParameterTypes()[0].isAssignableFrom(po.getClass())) {
                    Object o = c.newInstance(po);
                    list.add(o);
                }
            }
        }
        return list;
    }

    public static void main(String[] args) {
        System.out.println(createNextNo("0001", 4));
    }

    /**
     * 通过反射设置实体类的字段值
     *
     * @param o
     * @param field 实体类字段名
     * @param value 要设置的值
     * @throws Exception
     */
    @SneakyThrows
    public static void setField(Object o, String field, String value) {
        // 获取Class对象
        Class<?> aClass = o.getClass();

        // 获取字段值
        Field declaredField = aClass.getDeclaredField(field);
        declaredField.setAccessible(true);
        String type = declaredField.getGenericType().toString();
        if (declaredField.getType() == Integer.class) {
            // 将String转换为int类型
            Integer i = Integer.parseInt(value);
            declaredField.set(o, i);
        } else if (declaredField.getType() == String.class) {
            declaredField.set(o, value);
        }
    }

    /**
     * @param list
     * @return
     */
    public static String join(List list) {
        return join(list, ",");
    }

    /**
     * @param list
     * @param separator
     * @return
     */
    public static String join(List list, String separator) {
        StringBuffer sb = new StringBuffer();
        list.stream().forEach(e -> {
            if (sb.length() > 0) {
                sb.append(separator);
            }
            sb.append(e);
        });
        return sb.toString();
    }

    /**
     * 将name转化为驼峰规则字符串
     *
     * @param name
     * @param firstCharUpperCase 首字母大写
     * @return
     */
    public static String getCamelCase(String name, Boolean firstCharUpperCase) {
        String[] strings = name.split("_");
        StringBuffer stringBuffer = new StringBuffer();
        for (String string : strings) {
            if (stringBuffer.length() == 0 && !firstCharUpperCase) {
                stringBuffer.append(string.substring(0, 1));
            } else {
                stringBuffer.append(string.substring(0, 1).toUpperCase());
            }
            stringBuffer.append(string.substring(1));
        }
        return stringBuffer.toString();
    }

    /**
     * 创建path的所有目录
     *
     * @param path
     */
    public static File mkdirs(String path) {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }

    /**
     * 将数组转化为List，如果直接使用Arrays.asList，则新生成的List元素无法删除，所以需要专门提供本方法
     *
     * @param a
     * @param <T>
     * @return
     */
    public static <T> List<T> asList(T... a) {
        return new ArrayList<>(Arrays.asList(a));
    }

    public static List<String> readLines(String content) {
        List<String> list = new ArrayList<>();
        if (isEmpty(content)) {
            return list;
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            log.error("", e);
        } finally {
            try {
                br.close();
            } catch (Exception e) {

            }
        }
        return list;
    }


    /**
     * 获取对象
     *
     * @param name
     * @return Object 一个以所给名字注册的bean的实例
     * @throws BeansException
     */
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> getBean(String name) throws BeansException {
        if (RvaUtils.isNotEmpty(name) && SpringUtils.containsBean(name)) {
            return Optional.of(SpringUtils.getBean(name));
        }
        return Optional.ofNullable(null);
    }

    public static <T> Optional<T> getBean(Class<T> clz) {
        try {
            return Optional.of(SpringUtils.getBean(clz));
        } catch (BeansException be) {
            return Optional.ofNullable(null);
        }
    }

    public static Object getBeanByName(String name) throws BeansException {
        return SpringUtils.getBean(name);
    }

    public static <T> Optional<T> getBean(String name, Class<T> clz) {
        try {
            return Optional.of(SpringUtils.getBean(name, clz));
        } catch (BeansException be) {
            return Optional.ofNullable(null);
        }
    }

    public static Boolean isMap(Object map) {
        return isNotEmpty(map) && map instanceof Map;
    }

    public static RvaMap<String, Object> parseMap(String data) {
        if (RvaUtils.isEmpty(data)) {
            return new RvaMap<>();
        }
        Map map = RvaJsonUtils.readAsType(data, Map.class);
        return new RvaMap<>(map);
    }

    public static String setJsonProperty(String data, String property, Object val) {
        return RvaJsonUtils.writeAsString(parseMap(data).rvaPut(property, val));
    }

    public static void throwRuntimeException(String msg, Object... args) {
        throw new RuntimeException(String.format(msg, args));
    }

    public static void throwExistsException(String name) {
        throwRuntimeException(RvaConstants.MSG_EXISTS, name);
    }

    public static void throwNotSetException(String name) {
        throwRuntimeException(RvaConstants.MSG_NOT_SET, name);
    }

    public static void throwCallException(String name) {
        throwRuntimeException(RvaConstants.MSG_CALL, name);
    }

    public static void throwQueryException(String name, String data) {
        throwRuntimeException(RvaConstants.MSG_QUERY, name, data);
    }

    public static void throwRequiredException(String name) {
        throwRuntimeException(RvaConstants.MSG_REQUIRED, name);
    }

    public static void throwFormatException(String name, String format) {
        throwRuntimeException(RvaConstants.MSG_FORMAT, name, format);
    }

    public static void throwRequiredAllException(String... name) {
        throwRuntimeException(RvaConstants.MSG_REQUIRED_ALL, RvaUtils.join(Arrays.asList(name)));
    }

    public static void throwSetException(String name) {
        throwRuntimeException(RvaConstants.MSG_SET, name);
    }

    public static int getMaxNumber(String prefix, List list) {
        int num = -1;
        for (Object o : list) {
            try {
                String id = (String) o.getClass().getMethod("getId").invoke(o);
                String[] strs = id.split("_");
                if (strs[0].startsWith(prefix) && strs[0].length() > prefix.length()) {
                    int n = RvaUtils.getInt(strs[0].substring(prefix.length()), num);
                    if (n > num) {
                        num = n;
                    }
                }
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
        }
        num++;
        return num;
    }

    /**
     * 将list转成Map，key为list中对象的id属性值，value是list中的对象
     *
     * @param list
     * @return
     */
    public static RvaMap<String, Object> list2Map(List list) {
        return list2Map(list, null);
    }

    /**
     * 将list转成Map，key为list中对象的某个属性值，默认为id属性的值，value是list中的对象
     *
     * @param list
     * @param keyMethod 获取list中对象的属性值的方法
     * @return
     */
    public static RvaMap<String, Object> list2Map(List list, String keyMethod) {
        RvaMap<String, Object> map = new RvaMap<>();
        if (RvaUtils.isEmpty(keyMethod)) {
            keyMethod = "getId";
        }
        for (Object item : list) {
            try {
                Object key = item.getClass().getMethod(keyMethod).invoke(item);
                map.put(key.toString(), item);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throwCallException(keyMethod);
            }
        }
        return map;
    }

    public static List<List> listMap2List(List<RvaMap<String, Object>> list, List<String> keys) {
        List results = new ArrayList();
        list.forEach(e -> {
            List row = new ArrayList();
            results.add(row);
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                row.add(e.get(key));
            }
        });
        return results;
    }

    public static String getFileString(String path) {
        try {
            Resource resource = new DefaultResourceLoader().getResource("classpath:"+path);
            InputStream inputStream = resource.getInputStream();
            return IOUtils.toString(inputStream, "utf-8");
        } catch (Exception e) {
            throw new RuntimeException("获取文件出错:" + path, e);
        }
    }
    /**
     * 用户密码加密
     * @param pwd
     * @return
     */
    public static String encryptPassword(String pwd) {
        return SecurityUtils.encryptPassword(pwd);
    }
}
