alter table sys_menu AUTO_INCREMENT= 5000;
alter table sys_role AUTO_INCREMENT= 100;
alter table sys_config AUTO_INCREMENT= 2000;
alter table sys_dict_type AUTO_INCREMENT= 2000;
alter table sys_dict_data AUTO_INCREMENT= 4000;

update rva_module set is_default = 'N';
