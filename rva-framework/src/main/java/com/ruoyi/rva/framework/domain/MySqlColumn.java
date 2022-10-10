package com.ruoyi.rva.framework.domain;

import com.ruoyi.rva.framework.util.RvaSpringUtils;
import com.ruoyi.rva.framework.util.RvaUtils;

import java.util.ArrayList;
import java.util.List;

public class MySqlColumn {
	private String field = null;
	private String type = null;
	private String comment = null;
	private String allowsNull = null;
	private String key = null;
	private String defaultValue = null;
	private String extra = null;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAllowsNull() {
		return allowsNull;
	}

	public void setAllowsNull(String allowsNull) {
		this.allowsNull = allowsNull;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * mysql类型、MProperty.type以及Java类型的映射数据
	 */
	private final static List<String[]> typesData = new ArrayList<>();

	private static List<String[]> getTypesData () {
		if (typesData.size() == 0) {
			List<String> list = RvaSpringUtils.getEnvPropertyList("rva.data.types");
			list.forEach(types -> {
				typesData.add(types.split(" "));
			});
		}
		return typesData;
	}

	/**
	 * 根据type值，查找类型数据
	 * @param index getTypesData ()中的列索引，0-数据库类型，1-逻辑类型，2-Java类型
	 * @param type
	 * @return
	 */
	public static String[] getTypes (int index, String type) {
		for (String[] m : getTypesData ()) {
			if (m[index].equals(type)) {
				return m;
			}
		}
		return getTypesData ().get(0);
	}
	
	private String[] getTypes() {
		return getTypes(0, getColumnType());
	}

	public String getColumnType() {
		String type = this.type.toLowerCase();
		int idx = type.indexOf('(');
		if (idx > 0) {
			type = type.substring(0, idx);
		}
		return type;
	}

	public String getLogicType() {
		return getTypes()[1];
	}

	public String getJavaType() {
		return getTypes()[2];
	}

	public String getTypeDetail() {
		int idx = this.type.indexOf('(');
		if (idx < 1) {
			return null;
		}
		return this.type.substring(idx + 1, type.length() - 1);
	}

	public boolean isPrimaryKey() {
		return key != null && key.equals("PRI");
	}

	public boolean isNull() {
		return allowsNull != null && allowsNull.equals("YES");
	}

	public static String getDefaultJavaValue (String javaType, Object defaultValue) {
		if (defaultValue == null) {
			return "null";
		}
		if (javaType.equals("Double") || javaType.equals("Integer")) {
			if (defaultValue.equals("")) {
				return "null";
			}
			return defaultValue.toString();
		}
		return "\"" + defaultValue + "\"";
	}
	
	public String getDefaultJavaValue() {
		return getDefaultJavaValue (this.getJavaType(), this.getDefaultValue());
	}

	public String getDescription() {
		if (RvaUtils.isEmpty(comment)) {
			return this.field;
		}
		return this.comment;
	}
}
