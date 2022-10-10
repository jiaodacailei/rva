alter table sys_user add `tenant_id` varchar(100) default 'default' COMMENT '租户';

-- ----------------------------
-- Records of rva_tenant
-- ----------------------------
INSERT INTO `rva_tenant` VALUES ('default', '默认租户', null, '1', '2022-06-16 19:50:15', '1', '2022-06-16 19:50:18', null, '2022-06-16 19:50:31');
