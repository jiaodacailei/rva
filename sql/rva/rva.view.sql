-- --------------------------------------------------------
-- View definition of vw_rva_module_view
-- 说明：查询视图和模块的关系数据
-- --------------------------------------------------------
drop view if EXISTS vw_rva_module_view;
create view vw_rva_module_view as
select btn.action_dialog_view_id as view_id, obj.module as module_name
from rva_viewbutton btn LEFT JOIN rva_view vw on (btn.view_id = vw.id)
                        LEFT JOIN rva_view vw2 on (btn.action_dialog_view_id = vw2.id)
                        LEFT JOIN rva_object obj on (vw.obj_id = obj.id)
where btn.action_dialog_view_id is not null and vw2.obj_id = 'none' and btn.action_dialog_view_id not in (select view_id from rva_module_view where module_name <> obj.module)
union (select view_id, module_name from rva_module_view)
union (select vw3.id as view_id, obj.module as module_name from rva_view vw3 inner JOIN rva_object obj on (vw3.obj_id = obj.id) );

-- --------------------------------------------------------
-- View definition of vw_rva_module_perms
-- 说明：查询模块的权限数据，权限字符串包括：应用、视图、按钮ID
-- --------------------------------------------------------
drop view if EXISTS vw_rva_module_perms;
create view vw_rva_module_perms as
select app.id, obj.module as module_name from rva_app app LEFT JOIN rva_object obj on (app.obj_id = obj.id)

union

select VIEW_id as id, module_name from vw_rva_module_view

UNION

select btn.id, vw.module_name from rva_viewbutton btn INNER JOIN vw_rva_module_view vw on (btn.view_id = vw.view_id);

-- --------------------------------------------------------
-- View definition of vw_rva_module_menu
-- 说明：查询模块的菜单
-- --------------------------------------------------------
drop view if EXISTS vw_rva_module_menu;
create view vw_rva_module_menu as
select
    ifnull (
            perms.module_name,
            if (
                            menu_type = 'M' and menu_id >= 2477,
                            if (
                                        LOCATE('_', path) > 0,
                                left(path, LOCATE('_', path) - 1),
                                        path
                                ),
                            null
                )
        ) as module_name, m.*
from sys_menu m LEFT JOIN vw_rva_module_perms perms on (m.perms = perms.id);

-- --------------------------------------------------------
-- View definition of vw_sys_menu
-- 说明：查询菜单的full_path
-- --------------------------------------------------------
-- select menu_id, menu_name, parent_id,
-- @path := CONCAT(parent_id, ',' , menu_id),
-- @paths := CONCAT(@paths, ',|' ,@path, '|'),
-- @path := if(parent_id = 0, @path, CONCAT(SUBSTRING_INDEX(SUBSTRING_INDEX(@paths, CONCAT(',' ,parent_id, '|'), 1),',|', -1), ',' , parent_id, ',', menu_id)) as full_path,
-- @paths := replace(@paths, CONCAT('|', parent_id, ',' , menu_id, '|'), concat('|', @path ,'|'))
-- from sys_menu, (select @paths := '', @path := '0') vv ORDER BY parent_id,menu_id;
