package com.ruoyi.rva.framework.domain;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.ruoyi.rva.framework.util.RvaUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.List;

public class RvaSQL {

    protected SQL sql = new SQL();

    private List<String> leftOuterJoins = new ArrayList<>();

    private static interface AddToList {
        void add (String s);
    }

    private static void add (List<String> list, AddToList callback, String ... strings) {
        for (String s : strings) {
            if (list.contains(s)) {
                continue;
            }
            list.add(s);
            callback.add(s);
        }
    }

    public RvaSQL leftJoin(String table, String alias, String condition) {
        return leftOuterJoin(table + " " + alias + " ON (" + condition + ")");
    }

    public RvaSQL leftOuterJoin(String... joins) {
        add (leftOuterJoins, (s) -> {
            sql.LEFT_OUTER_JOIN(s);
        }, joins);
        return this;
    }

    private List<String> selectColumnAlias = new ArrayList<>();

    public RvaSQL selectEmpty(String columnAlias) {
        if (!selectColumnAlias.contains(columnAlias)) {
            select(String.format("'' as %s", columnAlias));
            selectColumnAlias.add(columnAlias);
        }
        return this;
    }

    public RvaSQL select(String tableAlias, String column) {
        return select(tableAlias, column, null, null);
    }

    public RvaSQL selectExpression(String expression, String columnAlias) {
        if (!expression.trim().startsWith("(")) {
            expression = "(" + expression + ")";
        }
        return select(null, expression, null, columnAlias);
    }

    /**
     *
     * @param tableAlias 表别名
     * @param column 列名
     * @param columnJsonProp 如果column为json，则columnJsonProp为其属性
     * @param columnAlias 列别名
     * @return
     */
    public RvaSQL select(String tableAlias, String column, String columnJsonProp, String columnAlias) {
        if (columnAlias == null) {
            columnAlias = column;
        }
        if (!selectColumnAlias.contains(columnAlias)) {
            String fieldExpression = createSelectFieldExpression(tableAlias, column, columnJsonProp, columnAlias);
            select(fieldExpression);
            selectColumnAlias.add(columnAlias);
        }
        return this;
    }

    public static String createSelectFieldExpression(String tableAlias, String column, String columnJsonProp) {
        if (RvaUtils.isEmpty(column)) {
            return "''";
        }
        if (RvaUtils.isNotEmpty(columnJsonProp)) {
            column = String.format("%s->>'$.%s'", column, columnJsonProp);
        }
        if (tableAlias == null) {
            return column;
        }
        return String.format("%s.%s", tableAlias, column);
    }

    public static String createSelectFieldExpression(String tableAlias, String column, String columnJsonProp, String columnAlias) {
        if (columnAlias == null) {
            columnAlias = column;
        }
        return String.format("%s as %s", createSelectFieldExpression(tableAlias, column, columnJsonProp), columnAlias);
    }

    private List<String> selects = new ArrayList<>();

    public RvaSQL select(String... columns) {
        add (selects, (s) -> {
            sql.SELECT(s);
        }, columns);
        return this;
    }

    private List<String> froms = new ArrayList<>();

    public RvaSQL from (String table, String alias) {
        return from(table + " `" + alias + "`");
    }

    public RvaSQL from(String... tables) {
        add (froms, (s) -> {
            sql.FROM(s);
        }, tables);
        return this;
    }

    private List<String> orderBys = new ArrayList<>();

    public RvaSQL orderBy(String... columns) {
        add (orderBys, (s) -> sql.ORDER_BY(s), columns);
        return this;
    }

    public RvaSQL orderByOnce(String tableAlias, String column, String value) {
        if ("ascending".equals(value)) {
            value = "asc";
        } else if ("descending".equals(value)) {
            value = "desc";
        }
        String orderby = "%s.%s %s";
        orderBy(String.format(orderby, tableAlias, column, value));
        return this;
    }

    private List<String> setColumns = new ArrayList<>();

    public RvaSQL set(String column, String value, Boolean quatation) {
        if (value != null && quatation) {
            value = "'" + value + "'";
        }
        final String finalValue = value;
        add (setColumns, (s) -> {
            sql.SET(s + "=" + finalValue);
        }, column);
        return this;
    }

    private List<String> wheres = new ArrayList<>();

    public RvaSQL where(String... conditions) {
        if (RvaUtils.isArrayOrListEmpty(conditions)) {
            return this;
        }
        add (wheres, (s) -> {
            String[] strings = s.split("[ ]+[oO][rR][dD][eE][rR][ ]+[bB][yY][ ]+");
            sql.WHERE("(" + strings[0] + ")");
            if (strings.length == 2) {
                this.orderBy(strings[1].trim().split("[ ]*,[ ]*"));
            }
            }, conditions);
        return this;
    }

    /**
     * 增加where：tableAlias.column = 'value'或者tableAlias.column like 'value'
     * @param tableAlias
     * @param column
     * @param value 如果包含%，则为like
     * @return
     */
    public RvaSQL whereEqOrLike(String tableAlias, String column, String value) {
        if (value != null) {
            String where = "%s.%s = '%s'";
            if (value.indexOf('%') >= 0) {
                where = "%s.%s like '%s'";
            }
            where(String.format(where, tableAlias, column, value));
        }
        return this;
    }

    private List<String> insertColumns = new ArrayList<>();

    public RvaSQL values(String column, String value, Boolean quatation) {
        if (value != null && quatation) {
            value = "'" + value + "'";
        }
        final String finalValue = value;
        add (insertColumns, (s) -> sql.VALUES(s, finalValue), column);
        return this;
    }

    public String getOrderBys () {
        return RvaUtils.join(orderBys, ",");
    }

    public String getWhere () {
        return RvaUtils.join(wheres, " and ");
    }

    @Override
    public String toString() {
        return sql.toString();
    }

    public SQL getSql () {
        return sql;
    }

    public void clearSelects() {
        this.selects.clear();
        this.selectColumnAlias.clear();
        sql = new SQL();
        this.leftOuterJoins.forEach(join -> {
            sql.LEFT_OUTER_JOIN(join);
        });
        this.froms.forEach(from -> {
            sql.FROM(from);
        });
        this.wheres.forEach(st -> {
            sql.WHERE(st);
        });
    }

    public void clearOrderBys() {
        this.orderBys.clear();
    }

    public static List<String> parseSelectColumns (String sql) {
        List<String> results = new ArrayList<>();
        if (RvaUtils.isEmpty(sql)) {
            return results;
        }
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, DbType.mysql);
        if (sqlStatements.size() == 0) {
            if (RvaUtils.isEmpty(sql)) {
                return results;
            }
        }
        SQLSelectStatement sqlStatement = (SQLSelectStatement)sqlStatements.get(sqlStatements.size() - 1);
        sqlStatement.getSelect().getQueryBlock().getSelectList().forEach(item -> {
            String name = item.getAlias();
            if (RvaUtils.isEmpty(name)) {
                name = item.getExpr().toString();
            }
            results.add(name);
        });
        return results;
    }

    public static void main(String[] args) {
        List<String> strings = parseSelectColumns("select ab a, cd, count(*) from abc d group by d.module, d.abc");
        System.out.println(strings);
    }
}
