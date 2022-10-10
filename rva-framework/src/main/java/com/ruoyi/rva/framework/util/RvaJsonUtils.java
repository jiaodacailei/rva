package com.ruoyi.rva.framework.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Jackson工具箱，用于处理Json数据。
 * </p>
 * Created by Eugene on 2016/10/18.
 */
@Slf4j
public class RvaJsonUtils {

	/**
	 * Json与Object转换的操作类。
	 */
	public final static ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	/**
	 * 将对象转换为Json字符串。
	 * 
	 * @param obj
	 *            待转换的对象。
	 * @return Json字符串。
	 */
	@SneakyThrows
	public static String writeAsString(Object obj) {
		return mapper.writeValueAsString(obj);
	}

	/**
	 * 将Json字符串转换为指定Class类型的对象。
	 * 
	 * @param value
	 *            Json字符串。
	 * @param clazz
	 *            最终的转换类型，可以是任意Java类型。
	 * @param <T>
	 *            自定义泛型类型。
	 * @return 指定类型的对象。
	 */
	@SneakyThrows
	public static <T> T readAsType(String value, Class<T> clazz) {
		return mapper.readValue(formatJson(value), clazz);
	}

	private static String formatJson (String json) {
		return json.replace("\t", " ");
	}

	/**
	 * 将Json字符串转换为指定Class类型的对象。
	 *
	 * @param data Map对象
	 * @param clazz 最终的转换类型，可以是任意Java类型。
	 * @param <T>
	 *            自定义泛型类型。
	 * @return 指定类型的对象。
	 */
	@SneakyThrows
	public static <T> T readAsTypeByData(Object data, Class<T> clazz) {
		if (RvaUtils.isEmpty(data)) {
			return null;
		}
		return readAsType(writeAsString(data), clazz);
	}

	/**
	 * @param json
	 * @param cls
	 * @return
	 */
	@SneakyThrows
	public static <T> List<T> readAsList (String json, Class<T> cls) {
		if (RvaUtils.isEmpty(json)) {
			return new ArrayList<T>();
		}
		JavaType javaType = getCollectionType(ArrayList.class, cls);
		return (List<T>) mapper.readValue(formatJson(json), javaType);
	}

	/**
	 * 获取泛型的Collection Type
	 *
	 * @param collectionClass
	 *            泛型的Collection
	 * @param elementClasses
	 *            元素类
	 * @return JavaType Java类型
	 * @since 1.0
	 */
	public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

}
