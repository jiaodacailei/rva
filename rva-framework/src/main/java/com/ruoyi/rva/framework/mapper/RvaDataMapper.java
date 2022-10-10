package com.ruoyi.rva.framework.mapper;

import com.ruoyi.rva.framework.domain.RvaMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author cailei
 * @date 2017年6月8日
 */
public interface RvaDataMapper {

	int insert (@Param("tableName") String tableName, @Param("columnMap") Map<String, Object> fieldValues);

	int update (String sql);

	int updateWhereMap (@Param("tableName") String tableName, @Param("columnMap") Map<String, Object> fieldValues, @Param("whereMap") Map<String, Object> whereMap, @Param("updateNull") Boolean updateNull);

	int updateWhereSql (@Param("tableName") String tableName, @Param("columnMap") Map<String, Object> fieldValues, @Param("whereSql") String whereSql, @Param("updateNull") Boolean updateNull);

	int delete (String sql);

	int deleteWhereMap (@Param("tableName") String table, @Param("whereMap") Map<String, Object> whereMap);

	List<RvaMap<String, Object>> selectList(String sql);

	Long selectLong (String sql);

	String selectString (String sql);

	String selectDefaultModule ();

}
