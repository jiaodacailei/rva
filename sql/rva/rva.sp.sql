-- --------------------------------------------------------
-- FUNCTION definition of rvaSplitCount
-- 函数说明：将字段值val，转化为拼接insert语句时合法的格式
-- --------------------------------------------------------
DELIMITER $$

DROP function IF EXISTS `rvaSplitCount` $$

CREATE FUNCTION `rvaSplitCount`

(f_string varchar(1000),f_delimiter varchar(5)) RETURNS int(11)

BEGIN

    # 计算传入字符串的总length

    return 1+(length(f_string) - length(replace(f_string,f_delimiter,'')));

END $$

DELIMITER;

-- --------------------------------------------------------
-- FUNCTION definition of rvaSplit
-- 函数说明：
-- --------------------------------------------------------
DELIMITER $$

DROP function IF EXISTS `rvaSplit` $$

CREATE FUNCTION `rvaSplit`

(f_string varchar(1000), f_delimiter varchar(5), f_order int) RETURNS varchar(255) CHARSET utf8

BEGIN

    # 拆分传入的字符串，返回拆分后的新字符串

        declare result varchar(255) default '';

        set result = reverse(substring_index(reverse(substring_index(f_string,f_delimiter,f_order)),f_delimiter,1));

return result;

END $$

DELIMITER;

-- --------------------------------------------------------
-- FUNCTION definition of rvaSplitString
-- 函数说明：
-- --------------------------------------------------------
DELIMITER $$

DROP PROCEDURE IF EXISTS `rvaSplitString` $$

CREATE PROCEDURE `rvaSplitString`

(IN f_string varchar(1000),IN f_delimiter varchar(5))

BEGIN

# 拆分结果

declare cnt int default 0;

declare i int default 0;

set cnt = rvaSplitCount(f_string,f_delimiter);

while i < cnt

do

    set i = i + 1;

insert into tmp_split(`value`) values (rvaSplit(f_string,f_delimiter,i));

end while;

END $$

DELIMITER;

-- --------------------------------------------------------
-- FUNCTION definition of rvaSplitLike
-- 函数说明：
-- --------------------------------------------------------
DELIMITER $$
DROP function IF EXISTS `rvaSplitLike` $$
CREATE FUNCTION `rvaSplitLike`
(f_string varchar(1000), f_delimiter varchar(5), f_like VARCHAR(100)) RETURNS SMALLINT
BEGIN
		declare result SMALLINT DEFAULT 0;
call rvaSplitString(f_string, f_delimiter);
select count(value) into result from tmp_split where value like f_like;
return result;
END $$
DELIMITER;

-- --------------------------------------------------------
-- FUNCTION definition of rvaSqlValue
-- 函数说明：将字段值val，转化为拼接insert语句时合法的格式
-- --------------------------------------------------------
drop FUNCTION if EXISTS rvaSqlValue;
delimiter $$
create FUNCTION rvaSqlValue(val text)
    RETURNS text
BEGIN
    #Routine body goes here...
    DECLARE result text;

    set result = val;

if result is null THEN
    set result = 'null';
ELSE
    set result = replace(result, "'", "\\'");
    set result = replace(result, '"', '\\"');
    set result = CONCAT("'", result ,"'");
end IF;

RETURN result;
END $$
delimiter ;

-- --------------------------------------------------------
-- FUNCTION definition of rvaTableColumns
-- 函数说明：获取schemaName库中，tableName对应的列名，多个以逗号隔开
-- --------------------------------------------------------
drop FUNCTION if EXISTS rvaTableColumns;
delimiter $$
create FUNCTION rvaTableColumns(schemaName varchar(100), tableName varchar(100), pri int)
    RETURNS VARCHAR(10000)
BEGIN
    #Routine body goes here...
    DECLARE cols VARCHAR(10000);
if pri = 1 then
    select GROUP_CONCAT(COLUMN_NAME) into cols from information_schema.`COLUMNS` where TABLE_SCHEMA = schemaName and TABLE_NAME = tableName;
else
    select GROUP_CONCAT(COLUMN_NAME) into cols from information_schema.`COLUMNS` where TABLE_SCHEMA = schemaName and TABLE_NAME = tableName and COLUMN_KEY <> 'PRI';
end if;

RETURN cols;
END $$
delimiter ;

-- --------------------------------------------------------
-- PROCEDURE definition of rvaExecuteSQL
-- 函数说明：执行动态sql语句sqlStatement
-- --------------------------------------------------------
drop PROCEDURE if EXISTS rvaExecuteSQL;
delimiter $$
create PROCEDURE rvaExecuteSQL(in sqlStatement text)
BEGIN
    set @stmt = sqlStatement;
PREPARE stmt FROM @stmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END $$
delimiter ;

-- --------------------------------------------------------
-- PROCEDURE definition of rvaExportInsertSql
-- 函数说明：执行动态sql语句sqlStatement
-- --------------------------------------------------------
drop PROCEDURE if EXISTS rvaExportInsertSql;
delimiter $$
create PROCEDURE rvaExportInsertSql(in schemaName varchar(100), in tableName varchar(100),in whereClause VARCHAR(10000), in pri int)
BEGIN
    DECLARE end_flag int DEFAULT 0;
    DECLARE columnNames varchar(1000);
    DECLARE columnVaribles text;
    DECLARE vals text;
    #声明游标
    DECLARE records CURSOR FOR select v.vals from tmp_rva_view v;

#设置终止标志
DECLARE CONTINUE HANDLER FOR NOT FOUND SET end_flag = 1;

    -- 获取列表字段，逗号隔开
select rvaTableColumns(schemaName, tableName, pri) into columnNames;
-- 获取列表字段对应的变量，逗号隔开
set columnVaribles = CONCAT('rvaSqlValue(cast(`', replace(columnNames, ',', '` as char)), rvaSqlValue(cast(`'), '` as char))');

    -- select columnNames, columnVaribles;

drop view if EXISTS tmp_rva_view;

if whereClause is null or whereClause = '' THEN
        set whereClause = '';
ELSE
        set whereClause = CONCAT(' where ', whereClause);
end if;

call rvaExecuteSQL(CONCAT('create view tmp_rva_view as select concat_ws(", ", ', columnVaribles, ') as vals from ', tableName, whereClause));

-- if tableName = 'rva_view' THEN
--    select CONCAT('create view tmp_rva_view as select concat_ws(", ", ', columnVaribles, ') as vals from ', tableName, whereClause);
-- end if;

insert into rva_inserts (`inserts`) values ('-- --------------------------------------------------------');
insert into rva_inserts (`inserts`) values (CONCAT('-- Records of ', tableName));
insert into rva_inserts (`inserts`) values ('-- --------------------------------------------------------');

#打开游标
OPEN records;

    #遍历游标
REPEAT

        #获取当前游标指针记录，取出值赋给自定义的变量
        -- call rvaExecuteSQL(CONCAT('FETCH records INTO ' , columnVaribles));
        FETCH records INTO vals;

        #利用取到的值进行数据库的操作
        -- select vals;
if end_flag = 0 THEN
            insert into rva_inserts (`inserts`) values (CONCAT('insert into ', tableName, '(', '`', replace(columnNames, ',', '`, `') ,'`) values (', vals ,');'));
END IF;

        # 根据 end_flag 判断是否结束
    UNTIL end_flag END REPEAT;

    #关闭游标
close records;

    -- set sqlStatement = CONCAT('insert into ', tableName, '(', sqlStatement, ') values()');

END $$
delimiter ;

-- set @s = '';
-- call rvaExportInsertSql('ry-vue', 'rva_module', 'id=2', @s);
-- select @s;

-- --------------------------------------------------------
-- PROCEDURE definition of rvaExportSql
-- 函数说明：导出属于module模块的数据insert语句
-- --------------------------------------------------------
drop PROCEDURE if EXISTS rvaExportSql;
delimiter $$
create PROCEDURE rvaExportSql(in module varchar(100))
BEGIN
    declare schemaName varchar(100);

    set schemaName = database();

-- rva_module
call rvaExportInsertSql(schemaName, 'rva_module', CONCAT("name=", rvaSqlValue(module)), 1);

-- rva_object
insert into rva_inserts (`inserts`) values ('');
call rvaExportInsertSql(schemaName, 'rva_object', CONCAT("module=", rvaSqlValue(module)), 1);

-- rva_property
insert into rva_inserts (`inserts`) values ('');
call rvaExportInsertSql(schemaName, 'rva_property', CONCAT("obj_id in (select id from rva_object where module = ", rvaSqlValue(module), ")"), 1);

-- rva_relation
insert into rva_inserts (`inserts`) values ('');
call rvaExportInsertSql(schemaName, 'rva_relation', CONCAT("obj_id in (select id from rva_object where module = ", rvaSqlValue(module), ")"), 1);

-- rva_relationitem
insert into rva_inserts (`inserts`) values ('');
call rvaExportInsertSql(schemaName, 'rva_relationitem', CONCAT("relation_id in (select id from rva_relation where obj_id in (select id from rva_object where module = ", rvaSqlValue(module), "))"), 1);


-- rva_module_view
insert into rva_inserts (`inserts`) values ('');
-- select CONCAT("module_name=", rvaSqlValue(module));
call rvaExportInsertSql(schemaName, 'rva_module_view', CONCAT("module_name=", rvaSqlValue(module)), 1);


-- rva_view
insert into rva_inserts (`inserts`) values ('');
call rvaExportInsertSql(schemaName, 'rva_view', CONCAT("id in (select view_id from vw_rva_module_view where module_name = ", rvaSqlValue(module), ")"), 1);

-- rva_viewproperty
insert into rva_inserts (`inserts`) values ('');
call rvaExportInsertSql(schemaName, 'rva_viewproperty', CONCAT("view_id in (select view_id from vw_rva_module_view where module_name = ", rvaSqlValue(module), ")"), 1);

-- rva_viewbutton
insert into rva_inserts (`inserts`) values ('');
call rvaExportInsertSql(schemaName, 'rva_viewbutton', CONCAT("view_id in (select view_id from vw_rva_module_view where module_name = ", rvaSqlValue(module), ")"), 1);

-- rva_trigger
insert into rva_inserts (`inserts`) values ('');
call rvaExportInsertSql(schemaName, 'rva_trigger', CONCAT("view_id in (select view_id from vw_rva_module_view where module_name = ", rvaSqlValue(module), ")"), 1);

-- rva_triggeraction
insert into rva_inserts (`inserts`) values ('');
call rvaExportInsertSql(schemaName, 'rva_triggeraction', CONCAT("trigger_id in (select id from rva_trigger where view_id in (select view_id from vw_rva_module_view where module_name = ", rvaSqlValue(module), "))"), 1);

-- rva_app
insert into rva_inserts (`inserts`) values ('');
call rvaExportInsertSql(schemaName, 'rva_app', CONCAT("obj_id in (select id from rva_object where module = ", rvaSqlValue(module), ")"), 1);

-- rva_appitem
insert into rva_inserts (`inserts`) values ('');
call rvaExportInsertSql(schemaName, 'rva_appitem', CONCAT("app_id in (select id from rva_app where obj_id in (select id from rva_object where module = ", rvaSqlValue(module), "))"), 1);

-- rva_fe_list
insert into rva_inserts (`inserts`) values ('');
call rvaExportInsertSql(schemaName, 'rva_fe_list', CONCAT("obj_id in (select id from rva_object where module = ", rvaSqlValue(module), ")"), 1);

-- rva_fe_list_item
insert into rva_inserts (`inserts`) values ('');
call rvaExportInsertSql(schemaName, 'rva_fe_list_item', CONCAT("list_id in (select id from rva_fe_list where obj_id in (select id from rva_object where module = ", rvaSqlValue(module), "))"), 1);

-- rva_chart
insert into rva_inserts (`inserts`) values ('');
call rvaExportInsertSql(schemaName, 'rva_chart', CONCAT("obj_id in (select id from rva_object where module = ", rvaSqlValue(module), ") or JSON_EXTRACT(data,'$.module')=",rvaSqlValue(module)), 1);

-- rva_chart_axis
insert into rva_inserts (`inserts`) values ('');
call rvaExportInsertSql(schemaName, 'rva_chart_axis', CONCAT("chart_id in (select id from rva_chart where obj_id in (select id from rva_object where module = ", rvaSqlValue(module), ")","or JSON_EXTRACT(data,'$.module')=",rvaSqlValue(module) ,")"), 1);

-- rva_chart_series
insert into rva_inserts (`inserts`) values ('');
call rvaExportInsertSql(schemaName, 'rva_chart_series', CONCAT("chart_id in (select id from rva_chart where obj_id in (select id from rva_object where module = ", rvaSqlValue(module), ")","or JSON_EXTRACT(data,'$.module')=",rvaSqlValue(module),")"), 1);

-- rva_chart_dataset
insert into rva_inserts (`inserts`) values ('');
call rvaExportInsertSql(schemaName, 'rva_chart_dataset', CONCAT("chart_id in (select id from rva_chart where obj_id in (select id from rva_object where module = ", rvaSqlValue(module), ")","or JSON_EXTRACT(data,'$.module')=",rvaSqlValue(module),")"), 1);

-- rva_chart_grid
insert into rva_inserts (`inserts`) values ('');
call rvaExportInsertSql(schemaName, 'rva_chart_grid', CONCAT("chart_id in (select id from rva_chart where obj_id in (select id from rva_object where module = ", rvaSqlValue(module), ")","or JSON_EXTRACT(data,'$.module')=",rvaSqlValue(module),")"), 1);

-- rva_chart_datacolumn
insert into rva_inserts (`inserts`) values ('');
call rvaExportInsertSql(schemaName, 'rva_chart_datacolumn', CONCAT("chart_dataset_id in( SELECT id FROM rva_chart_dataset where chart_id in (select id from rva_chart where obj_id in (select id from rva_object where module = ", rvaSqlValue(module), ") ","or JSON_EXTRACT(data,'$.module')=",rvaSqlValue(module),"))"), 1);

-- rva_kpi
insert into rva_inserts (`inserts`) values ('');
call rvaExportInsertSql(schemaName, 'rva_kpi', CONCAT("obj_id in (select id from rva_object where module = ", rvaSqlValue(module), ") or JSON_EXTRACT(data,'$.module')=",rvaSqlValue(module)), 1);

-- rva_kpi_item
insert into rva_inserts (`inserts`) values ('');
call rvaExportInsertSql(schemaName, 'rva_kpi_item', CONCAT("kpi_id in (select id from rva_kpi where obj_id in (select id from rva_object where module = ", rvaSqlValue(module), ")","or JSON_EXTRACT(data,'$.module')=",rvaSqlValue(module) ,")"), 1);

-- rva_process
insert into rva_inserts (`inserts`) values ('');
call rvaExportInsertSql(schemaName, 'rva_process', CONCAT("module = ", rvaSqlValue(module)), 1);

-- rva_process_task
insert into rva_inserts (`inserts`) values ('');
call rvaExportInsertSql(schemaName, 'rva_process_task', CONCAT("process_id in (select id from rva_process where module = ", rvaSqlValue(module), ")"), 1);

if module <> 'sys' THEN

    -- sys_config
    insert into rva_inserts (`inserts`) values ('');
    call rvaExportInsertSql(schemaName, 'sys_config', CONCAT("config_key like '", module, "%'"), 0);

    -- sys_dict_type
    insert into rva_inserts (`inserts`) values ('');
    call rvaExportInsertSql(schemaName, 'sys_dict_type', CONCAT("dict_type like '", module, "%'"), 0);

    -- sys_dict_data
    insert into rva_inserts (`inserts`) values ('');
    call rvaExportInsertSql(schemaName, 'sys_dict_data', CONCAT("dict_type like '", module, "%'"), 0);

    drop TABLE if EXISTS tmp_sys_menu;
    CREATE TEMPORARY TABLE tmp_sys_menu
    select menu_id,
           @path := CONCAT(parent_id, ',' , menu_id) as p1,
           @paths := CONCAT(@paths, ',|' ,parent_id, ',' , menu_id, '|') as p2,
           @path := if(parent_id = 0, @path, CONCAT(SUBSTRING_INDEX(SUBSTRING_INDEX(@paths, CONCAT(',' ,parent_id, '|'), 1),',|', -1), ',' , parent_id, ',', menu_id)) as full_path,
           @paths := replace(@paths, CONCAT('|', parent_id, ',' , menu_id, '|'), concat('|', @path ,'|')) as p4
    from sys_menu, (select @paths := '', @path := '0') vv ORDER BY parent_id, menu_id;

    select group_concat(menu_id) into @menuIds from tmp_sys_menu where full_path REGEXP (select CONCAT('.*,', GROUP_CONCAT(menu_id SEPARATOR '[,]{0,1}.*|.*,'), '[,]{0,1}.*') from sys_menu where menu_type = 'M' and (path = module || LOCATE(CONCAT(module, '_'), path) = 1));

    if @menuIds is null then
        select @menuIds := 'null';
    end if;

    -- sys_menu
    insert into rva_inserts (`inserts`) values ('');
    call rvaExportInsertSql(schemaName, 'sys_menu', CONCAT("menu_id in (", @menuIds, ")"), 1);

    -- rva
    if module = 'rva' THEN

        -- sys_role_menu
        insert into rva_inserts (`inserts`) values ('');
        insert into rva_inserts (`inserts`) values ('truncate table sys_role_menu;');
        call rvaExportInsertSql(schemaName, 'sys_role_menu', "role_id <= 2", 1);

        -- sys_role
        insert into rva_inserts (`inserts`) values ('');
        insert into rva_inserts (`inserts`) values ('truncate table sys_role;');
        call rvaExportInsertSql(schemaName, 'sys_role', "role_id <= 2", 1);

        -- sys_role_dept
--         insert into rva_inserts (`inserts`) values ('');
--         insert into rva_inserts (`inserts`) values ('truncate table sys_role_dept;');
--         call rvaExportInsertSql(schemaName, 'sys_role_dept',  "role_id <= 2", 1);

        -- sys_role_app_c4c1
        insert into rva_inserts (`inserts`) values ('');
        call rvaExportInsertSql(schemaName, 'sys_role_app_c4c1',  "jiaosexinxibiao_9a37 <= 2", 1);

        -- sys_role_appitem_a4e7
        insert into rva_inserts (`inserts`) values ('');
        call rvaExportInsertSql(schemaName, 'sys_role_appitem_a4e7',  "jiaosexinxibiao_5877 <= 2", 1);

        -- sys_role_kpi_item_68ff
        insert into rva_inserts (`inserts`) values ('');
        call rvaExportInsertSql(schemaName, 'sys_role_kpi_item_68ff',  "jiaosexinxibiao_167a <= 2", 1);

        -- rva_chart_formula
        insert into rva_inserts (`inserts`) values ('');
        call rvaExportInsertSql(schemaName, 'rva_chart_formula',null, 1);
    else
        -- sys_role_menu
        insert into rva_inserts (`inserts`) values ('');
        call rvaExportInsertSql(schemaName, 'sys_role_menu', "role_id > 2", 1);

        -- sys_role
        insert into rva_inserts (`inserts`) values ('');
        call rvaExportInsertSql(schemaName, 'sys_role', "role_id > 2", 1);

        -- sys_role_app_c4c1
        insert into rva_inserts (`inserts`) values ('');
        call rvaExportInsertSql(schemaName, 'sys_role_app_c4c1',  "jiaosexinxibiao_9a37 > 2", 1);

        -- sys_role_appitem_a4e7
        insert into rva_inserts (`inserts`) values ('');
        call rvaExportInsertSql(schemaName, 'sys_role_appitem_a4e7',  "jiaosexinxibiao_5877 > 2", 1);

        -- sys_role_dept
        insert into rva_inserts (`inserts`) values ('');
        call rvaExportInsertSql(schemaName, 'sys_role_dept',  "role_id > 2", 1);

        -- sys_role_kpi_item_68ff
        insert into rva_inserts (`inserts`) values ('');
        call rvaExportInsertSql(schemaName, 'sys_role_kpi_item_68ff',  "jiaosexinxibiao_167a > 2", 1);

    END IF;
else
    -- sys_config
    insert into rva_inserts (`inserts`) values ('');
    call rvaExportInsertSql(schemaName, 'sys_config', CONCAT("config_id >= 100 and config_key like '", module, "%'"), 0);

    -- sys_dict_type
    insert into rva_inserts (`inserts`) values ('');
    call rvaExportInsertSql(schemaName, 'sys_dict_type', CONCAT("dict_id > 140 and dict_type like '", module, "%'"), 0);

    -- sys_dict_data
    insert into rva_inserts (`inserts`) values ('');
    call rvaExportInsertSql(schemaName, 'sys_dict_data', CONCAT("dict_code > 218 and dict_type like '", module, "%'"), 0);
END IF;

END $$
delimiter ;

-- TRUNCATE TABLE rva_inserts;
-- call rvaExportSql('sys');
-- call rvaExportSql('rva');
-- SELECT inserts FROM rva_inserts;
