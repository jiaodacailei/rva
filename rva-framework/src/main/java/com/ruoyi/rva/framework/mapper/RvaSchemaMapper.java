package com.ruoyi.rva.framework.mapper;

import com.ruoyi.rva.framework.domain.MySqlColumn;

import java.util.List;
import java.util.Map;

/**
 * @author cailei
 * @date 2017年6月8日
 */
public interface RvaSchemaMapper {
	List<Map<String, Object>> queryAllTables();

	String queryTableComment(String table);

	/**
	 * 查询表数量，用于判断表是否存在
	 * @param table
	 * @return
	 */
	Integer queryTableCount (String table);

	/**
	 *
	 * @param table
	 * @return
	 */
	List<MySqlColumn> queryColumns(String table);

	void execute(String sql);
}
