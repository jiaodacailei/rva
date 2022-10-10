/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.1.102(linux)
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : 192.168.1.102:3306
 Source Schema         : cigarette

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 18/05/2022 18:23:33
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for rva_app
-- ----------------------------
DROP TABLE IF EXISTS `rva_app`;
CREATE TABLE `rva_app`
(
    `id`            varchar(64) NOT NULL COMMENT 'ID',
    `name`          varchar(100) NULL DEFAULT NULL COMMENT '名称',
    `type`          varchar(50) NULL DEFAULT NULL COMMENT '类型',
    `idx`           int(6) NULL DEFAULT 0 COMMENT '索引',
    `obj_id`        varchar(64) NOT NULL COMMENT '对象ID',
    `template`      varchar(100) NULL DEFAULT NULL COMMENT '模板',
    `list_template` varchar(100) NULL DEFAULT NULL COMMENT '列表模板',
    `status`        varchar(5) NULL DEFAULT NULL COMMENT '状态',
    `views`         text NULL COMMENT '包含视图',
    `apps`          text NULL COMMENT '包含应用',
    `data`          text NULL COMMENT '其他数据',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '应用';

-- ----------------------------
-- Table structure for rva_appitem
-- ----------------------------
DROP TABLE IF EXISTS `rva_appitem`;
CREATE TABLE `rva_appitem`
(
    `id`               varchar(64) NOT NULL COMMENT 'ID',
    `name`             varchar(100) NULL DEFAULT NULL COMMENT '名称',
    `idx`              int(11) NULL DEFAULT 0 COMMENT '索引',
    `related_app_type` varchar(50) NULL DEFAULT NULL COMMENT '关联类型',
    `related_app_id`   varchar(64) NOT NULL COMMENT '关联ID',
    `show_if`          varchar(200) NULL DEFAULT NULL COMMENT '条件',
    `type`             varchar(50) NULL DEFAULT NULL COMMENT '类型',
    `sub_type`         varchar(50) NULL DEFAULT NULL COMMENT '子类型',
    `parent_id`        varchar(64) NULL DEFAULT NULL COMMENT '父ID',
    `app_id`           varchar(64) NOT NULL COMMENT '应用ID',
    `data`             text NULL COMMENT '其他数据',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '应用项';

-- ----------------------------
-- Table structure for rva_chart
-- ----------------------------
DROP TABLE IF EXISTS `rva_chart`;
CREATE TABLE `rva_chart`
(
    `id`          varchar(100) NOT NULL COMMENT 'ID',
    `name`        varchar(100) NOT NULL COMMENT '名称',
    `type`        varchar(30)  NOT NULL DEFAULT 'bar' COMMENT '类型',
    `crud_id`     varchar(100) NULL DEFAULT NULL COMMENT '关联CRUD',
    `search_id`   varchar(100) NULL DEFAULT NULL COMMENT '查询视图',
    `obj_id`      varchar(100) NULL DEFAULT NULL COMMENT '所属对象',
    `create_by`   bigint(20) NULL DEFAULT NULL COMMENT '创建者',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   bigint(20) NULL DEFAULT NULL COMMENT '更新者',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `data`        text NULL COMMENT '其他数据',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '图表';

-- ----------------------------
-- Table structure for rva_chart_axis
-- ----------------------------
DROP TABLE IF EXISTS `rva_chart_axis`;
CREATE TABLE `rva_chart_axis`
(
    `id`               varchar(100) NOT NULL COMMENT 'ID',
    `name`             varchar(100) NOT NULL COMMENT '名称',
    `type`             varchar(30) NULL DEFAULT NULL COMMENT '类型',
    `data_type`        varchar(100) NULL DEFAULT 'column' COMMENT '数据模式(column row)，仅type=category',
    `data_index`       smallint(6) NULL DEFAULT 0 COMMENT '数据索引，rva_chart_dataset返回结果集的行或者列索引，仅type=category',
    `chart_dataset_id` varchar(100) NULL DEFAULT NULL COMMENT '数据集，仅type=category',
    `chart_grid_id`    varchar(100) NULL DEFAULT NULL COMMENT '坐标系',
    `chart_id`         varchar(100) NOT NULL COMMENT '图表',
    `idx`              smallint(6) NULL DEFAULT 0 COMMENT '排序',
    `create_by`        bigint(20) NULL DEFAULT NULL COMMENT '创建者',
    `create_time`      datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`        bigint(20) NULL DEFAULT NULL COMMENT '更新者',
    `update_time`      datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `data`             text NULL COMMENT '其他数据',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '图表轴';

-- ----------------------------
-- Table structure for rva_chart_datacolumn
-- ----------------------------
DROP TABLE IF EXISTS `rva_chart_datacolumn`;
CREATE TABLE `rva_chart_datacolumn`
(
    `id`               varchar(100) NOT NULL COMMENT 'ID',
    `name`             varchar(100) NOT NULL COMMENT '名称',
    `type`             varchar(30) NULL DEFAULT NULL COMMENT '类型',
    `viewproperty_id`  varchar(100) NULL DEFAULT NULL COMMENT '列表属性，rva_chart_dataset.crud_id对应应用的列表视图属性ID',
    `formula`          varchar(300) NULL DEFAULT NULL COMMENT '公式',
    `option_dict`      varchar(100) NULL DEFAULT NULL COMMENT '选项字典',
    `option_sql`       varchar(1000) NULL DEFAULT NULL COMMENT '选项sql',
    `idx`              smallint(6) NULL DEFAULT 0 COMMENT '排序',
    `chart_dataset_id` varchar(100) NOT NULL COMMENT '数据集',
    `create_by`        bigint(20) NULL DEFAULT NULL COMMENT '创建者',
    `create_time`      datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`        bigint(20) NULL DEFAULT NULL COMMENT '更新者',
    `update_time`      datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `data`             text NULL COMMENT '其他数据',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '图表数据列';

-- ----------------------------
-- Table structure for rva_chart_dataset
-- ----------------------------
DROP TABLE IF EXISTS `rva_chart_dataset`;
CREATE TABLE `rva_chart_dataset`
(
    `id`          varchar(100) NOT NULL COMMENT 'ID',
    `name`        varchar(100) NOT NULL COMMENT '名称',
    `crud_id`     varchar(100) NULL DEFAULT NULL COMMENT '关联CRUD',
    `data_sql`    text NULL COMMENT 'SQL',
    `chart_id`    varchar(100) NOT NULL COMMENT '图表',
    `create_by`   bigint(20) NULL DEFAULT NULL COMMENT '创建者',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   bigint(20) NULL DEFAULT NULL COMMENT '更新者',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `data`        text NULL COMMENT '其他数据',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '图表数据集';

-- ----------------------------
-- Table structure for rva_chart_formula
-- ----------------------------
DROP TABLE IF EXISTS `rva_chart_formula`;
CREATE TABLE `rva_chart_formula`
(
    `id`            int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`          varchar(100) NOT NULL COMMENT '名称',
    `type`          varchar(100) NULL DEFAULT NULL COMMENT '类型',
    `applicable_to` varchar(150) NULL DEFAULT NULL COMMENT '适用类型',
    `formula`       varchar(100) NOT NULL COMMENT '公式',
    `description`   varchar(500) NULL DEFAULT NULL COMMENT '说明',
    `create_by`     bigint(20) NULL DEFAULT NULL COMMENT '创建者',
    `create_time`   datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`     bigint(20) NULL DEFAULT NULL COMMENT '更新者',
    `update_time`   datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `data`          text NULL COMMENT '其他数据',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '图表公式';

-- ----------------------------
-- Table structure for rva_chart_grid
-- ----------------------------
DROP TABLE IF EXISTS `rva_chart_grid`;
CREATE TABLE `rva_chart_grid`
(
    `id`          varchar(100) NOT NULL COMMENT 'ID',
    `name`        varchar(100) NOT NULL COMMENT '名称',
    `grid_top`    varchar(30) NULL DEFAULT NULL COMMENT 'top',
    `grid_bottom` varchar(30) NULL DEFAULT NULL COMMENT 'bottom',
    `grid_left`   varchar(30) NULL DEFAULT NULL COMMENT 'left',
    `grid_right`  varchar(30) NULL DEFAULT NULL COMMENT 'right',
    `grid_width`  varchar(30) NULL DEFAULT NULL COMMENT 'width',
    `grid_height` varchar(30) NULL DEFAULT NULL COMMENT 'height',
    `axis_x0`     varchar(100) NULL DEFAULT NULL COMMENT 'x0轴',
    `axis_x1`     varchar(100) NULL DEFAULT NULL COMMENT 'x1轴',
    `axis_y0`     varchar(100) NULL DEFAULT NULL COMMENT 'y0轴',
    `axis_y1`     varchar(100) NULL DEFAULT NULL COMMENT 'y1轴',
    `chart_id`    varchar(100) NOT NULL COMMENT '图表',
    `idx`         smallint(6) NULL DEFAULT 0 COMMENT '排序',
    `create_by`   bigint(20) NULL DEFAULT NULL COMMENT '创建者',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   bigint(20) NULL DEFAULT NULL COMMENT '更新者',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `data`        text NULL COMMENT '其他数据',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '图表坐标系';

-- ----------------------------
-- Table structure for rva_chart_series
-- ----------------------------
DROP TABLE IF EXISTS `rva_chart_series`;
CREATE TABLE `rva_chart_series`
(
    `id`               varchar(100) NOT NULL COMMENT 'ID',
    `name`             varchar(100) NOT NULL COMMENT '名称',
    `type`             varchar(30)  NOT NULL DEFAULT 'inherit' COMMENT '类型',
    `axis_x_id`        varchar(100) NULL DEFAULT NULL COMMENT 'X轴',
    `axis_y_id`        varchar(100) NULL DEFAULT NULL COMMENT 'Y轴',
    `data_type`        varchar(100) NULL DEFAULT 'column' COMMENT '数据模式(column row)',
    `data_index`       smallint(6) NULL DEFAULT 0 COMMENT '数据索引，rva_chart_dataset返回结果集的行或者列索引',
    `chart_dataset_id` varchar(100) NULL DEFAULT NULL COMMENT '数据集',
    `chart_id`         varchar(100) NULL DEFAULT NULL COMMENT '图表',
    `idx`              smallint(6) NULL DEFAULT 0 COMMENT '排序',
    `create_by`        bigint(20) NULL DEFAULT NULL COMMENT '创建者',
    `create_time`      datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`        bigint(20) NULL DEFAULT NULL COMMENT '更新者',
    `update_time`      datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `data`             text NULL COMMENT '其他数据',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '图表系列';

-- ----------------------------
-- Table structure for rva_fe_list
-- ----------------------------
DROP TABLE IF EXISTS `rva_fe_list`;
CREATE TABLE `rva_fe_list`
(
    `id`                 varchar(64) NOT NULL COMMENT 'ID',
    `name`               varchar(100) NULL DEFAULT NULL COMMENT '名称',
    `mode`               smallint(6) NULL DEFAULT 1 COMMENT '排版模式（1-大图 2-左图 3-右图 4-无图 5-聊天群 6-聊天）',
    `force_mode`         varchar(1) NULL DEFAULT 'Y' COMMENT '强制模式，为Y时，忽略列表项的mode，采用列表的mode',
    `obj_id`             varchar(64) NOT NULL COMMENT '主对象ID，此处为表名',
    `obj_favorites_ids`  text NULL COMMENT '收藏等对象ID，JSON对象格式，key为rva_objectfavorite.type的值，value为表名，用于存放收藏、点赞等数据',
    `obj_comments_id`    varchar(64) NULL DEFAULT NULL COMMENT '评论对象ID，用于存放评论数据，此处为表名',
    `load_where`         varchar(500) NULL DEFAULT NULL COMMENT '查询条件，velocity模板，解析后为sql脚本where语句，作为列表查询条件的一部分',
    `rows_init`          smallint(6) NULL DEFAULT 10 COMMENT '初始行数，列表初始显示行数，0表示无限制，不分页',
    `rows_update`        smallint(6) NULL DEFAULT 10 COMMENT '更新行数，每次上划下拉更新的行数',
    `show_title`         varchar(1) NULL DEFAULT 'Y' COMMENT '显示标题，针对列表项',
    `show_excerpt`       varchar(1) NULL DEFAULT 'Y' COMMENT '显示摘要，针对列表项',
    `show_avatar_count`  smallint(6) NULL DEFAULT 1 COMMENT '显示图片数，不大于0表示不显示',
    `show_extras_count`  smallint(6) NULL DEFAULT 2 COMMENT '显示额外数量，针对列表项，0表示不显示',
    `show_button_count`  smallint(6) NULL DEFAULT 3 COMMENT '显示按钮数量，针对列表项，0表示不显示',
    `prop_name_mode`     varchar(64) NULL DEFAULT NULL COMMENT '排版模式属性名，指定obj_id对应表的某个字段，用于存储排版模式',
    `prop_name_disabled` varchar(64) NULL DEFAULT NULL COMMENT '禁用属性名，指定obj_id对应表的某个字段，用于禁用信息',
    `prop_name_category` varchar(64) NULL DEFAULT NULL COMMENT '分类属性名，指定obj_id对应表的某个字段，关联obj_categories_id主键，用于存储分类数据',
    `prop_name_group`    varchar(64) NULL DEFAULT NULL COMMENT '分组属性名，指定obj_id对应表的某个字段，用于存储分组信息，实现动态分组',
    `dict_category`      varchar(64) NULL DEFAULT NULL COMMENT '分类字典，prop_name_category的值对应的数据字典',
    `dict_group`         varchar(64) NULL DEFAULT NULL COMMENT '分组字典，prop_name_group的值或者rva_fe_list_item.group_value对应的数据字典',
    `data`               text NULL COMMENT '其他数据',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '列表';

-- ----------------------------
-- Table structure for rva_fe_list_item
-- ----------------------------
DROP TABLE IF EXISTS `rva_fe_list_item`;
CREATE TABLE `rva_fe_list_item`
(
    `id`            varchar(64) NOT NULL COMMENT 'ID',
    `name`          varchar(100) NULL DEFAULT NULL COMMENT '名称',
    `position`      varchar(30) NULL DEFAULT NULL COMMENT '位置（title-标题 excerpt-摘要 extras-额外字段 avatar-图片 buttons-操作按钮 time-时间文本 badge-badge文本 right-右侧文本）',
    `list_id`       varchar(64) NOT NULL COMMENT '列表ID，rva_fe_list.id',
    `prop_id`       varchar(64) NULL DEFAULT NULL COMMENT '属性ID，rva_property.id',
    `idx`           smallint(6) NULL DEFAULT 0 COMMENT '排序',
    `icon`          varchar(500) NULL DEFAULT NULL COMMENT '图标',
    `text`          varchar(500) NULL DEFAULT NULL COMMENT '文本内容，为velocity模板，解析后的内容用于显示，如果为空，则显示name的值',
    `order_index`   smallint(6) NULL DEFAULT -1 COMMENT '排序索引',
    `order_type`    varchar(10) NULL DEFAULT NULL COMMENT '排序类型，DESC/ASC',
    `group_value`   varchar(100) NULL DEFAULT NULL COMMENT '分组值，用于对列表项进行静态分组，关联rva_fe_list.group_dict',
    `action_type`   varchar(30) NULL DEFAULT NULL COMMENT '位置（dialog ajax navigateTo switchTab redirectTo reLaunch toogleFavorite delete share）',
    `action_params` text NULL COMMENT '动作参数，JSON格式，action_type=dialog时，参数中应该包含viewId，指明关联的表单视图；action_type=ajax/navigateTo/switchTab/redirectTo/reLaunch时，参数中应该包含url',
    `dict`          varchar(64) NULL DEFAULT NULL COMMENT '数据字典',
    `number_scale`  smallint(6) NULL DEFAULT 0 COMMENT '小数位',
    `show_arrow`    varchar(1) NULL DEFAULT 'Y' COMMENT '显示箭头',
    `switch_url`    varchar(200) NULL DEFAULT NULL COMMENT 'Switch地址，有值时显示Switch控件，点击ajax请求',
    `data`          text NULL COMMENT '其他数据',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '列表项';

-- ----------------------------
-- Table structure for rva_inserts
-- ----------------------------
DROP TABLE IF EXISTS `rva_inserts`;
CREATE TABLE `rva_inserts`
(
    `inserts` text NULL,
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳'
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for rva_kpi
-- ----------------------------
DROP TABLE IF EXISTS `rva_kpi`;
CREATE TABLE `rva_kpi`
(
    `id`          varchar(100) NOT NULL COMMENT 'ID',
    `name`        varchar(100) NULL DEFAULT NULL COMMENT '名称',
    `search_id`   varchar(100) NULL DEFAULT NULL COMMENT '查询视图',
    `obj_id`      varchar(100) NULL DEFAULT NULL COMMENT '所属对象',
    `data_sql`    varchar(10000) NULL DEFAULT NULL COMMENT 'SQL',
    `template`    varchar(100) NULL DEFAULT NULL COMMENT '模板',
    `idx`         smallint(6) NULL DEFAULT 0 COMMENT '排序',
    `create_by`   bigint(20) NULL DEFAULT NULL COMMENT '创建者',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   bigint(20) NULL DEFAULT NULL COMMENT '更新者',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `data`        text NULL COMMENT '其他数据',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = 'KPI';

-- ----------------------------
-- Table structure for rva_kpi_item
-- ----------------------------
DROP TABLE IF EXISTS `rva_kpi_item`;
CREATE TABLE `rva_kpi_item`
(
    `id`               varchar(100) NOT NULL COMMENT 'ID',
    `name`             varchar(100) NULL DEFAULT NULL COMMENT '名称',
    `icon`             varchar(200) NULL DEFAULT NULL COMMENT '图标',
    `value_expression` varchar(1000) NULL DEFAULT NULL COMMENT '值表达式',
    `kpi_id`           varchar(100) NULL DEFAULT NULL COMMENT 'KPI，rva_kpi.id',
    `idx`              int(11) NULL DEFAULT 0 COMMENT '排序',
    `create_by`        bigint(20) NULL DEFAULT NULL COMMENT '创建者',
    `create_time`      datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`        bigint(20) NULL DEFAULT NULL COMMENT '更新者',
    `update_time`      datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `data`             text NULL COMMENT '其他数据',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = 'KPI项';

-- ----------------------------
-- Table structure for rva_log
-- ----------------------------
DROP TABLE IF EXISTS `rva_log`;
CREATE TABLE `rva_log`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`        varchar(100) NULL DEFAULT NULL COMMENT '名称',
    `view_id`     varchar(100) NOT NULL COMMENT '视图',
    `obj_id`      varchar(100) NULL DEFAULT NULL COMMENT '对象',
    `key_value`   varchar(100) NULL DEFAULT NULL COMMENT '主键值，obj_id对应表的主键值',
    `fk_value`    varchar(100) NULL DEFAULT NULL COMMENT '外键值，obj_id对应表的外键值',
    `form_data`   text NULL COMMENT '表单数据',
    `view_data`   text NULL COMMENT '视图数据',
    `create_by`   bigint(20) NULL DEFAULT NULL COMMENT '创建者',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `data`        text NULL COMMENT '其他数据',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '日志';

-- ----------------------------
-- Table structure for rva_module
-- ----------------------------
DROP TABLE IF EXISTS `rva_module`;
CREATE TABLE `rva_module`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`        varchar(100) NULL DEFAULT NULL COMMENT '名称',
    `idx`         int(11) NULL DEFAULT 0 COMMENT '索引',
    `is_default`  varchar(1) NULL DEFAULT 'N' COMMENT '是否默认',
    `description` varchar(500) NULL DEFAULT NULL COMMENT '描述',
    `data`        text NULL COMMENT '其他数据',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '模块';

-- ----------------------------
-- Table structure for rva_module_view
-- ----------------------------
DROP TABLE IF EXISTS `rva_module_view`;
CREATE TABLE `rva_module_view`
(
    `view_id`     varchar(255) NULL DEFAULT NULL,
    `module_name` varchar(255) NULL DEFAULT NULL,
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳'
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for rva_object
-- ----------------------------
DROP TABLE IF EXISTS `rva_object`;
CREATE TABLE `rva_object`
(
    `id`                    varchar(64) NOT NULL COMMENT 'ID',
    `no`                    varchar(64) NULL DEFAULT NULL COMMENT '编号',
    `name`                  varchar(100) NULL DEFAULT NULL COMMENT '名称',
    `idx`                   int(11) NULL DEFAULT NULL COMMENT '索引',
    `module`                varchar(64) NULL DEFAULT NULL COMMENT '模块前缀',
    `status`                varchar(1) NULL DEFAULT '1' COMMENT '状态（0-关系表 1-普通表）',
    `description`           varchar(500) NULL DEFAULT NULL COMMENT '说明',
    `prop_name_name`        varchar(64) NULL DEFAULT NULL COMMENT '名称属性名',
    `prop_name_del`         varchar(64) NULL DEFAULT NULL COMMENT '逻辑删除属性名',
    `prop_name_no`          varchar(64) NULL DEFAULT NULL COMMENT '层级编号属性名',
    `prop_name_key`         varchar(300) NULL DEFAULT NULL COMMENT '主键属性名',
    `prop_name_create_by`   varchar(64) NULL DEFAULT NULL COMMENT '创建人属性名',
    `prop_name_create_time` varchar(64) NULL DEFAULT NULL COMMENT '创建时间属性名',
    `prop_name_update_by`   varchar(64) NULL DEFAULT NULL COMMENT '修改人属性名',
    `prop_name_update_time` varchar(64) NULL DEFAULT NULL COMMENT '修改时间属性名',
    `uniques`               varchar(300) NULL DEFAULT NULL COMMENT '唯一键',
    `prop_index_max`        int(3) NULL DEFAULT 0 COMMENT '属性索引最大值',
    `relation_index_max`    int(3) NULL DEFAULT 0 COMMENT '关系索引最大值',
    `data`                  text NULL COMMENT '其他数据',
    `obj_favorite_id`       varchar(64) NULL DEFAULT NULL COMMENT '收藏表',
    `obj_comment_id`        varchar(64) NULL DEFAULT NULL COMMENT '评论表',
    `obj_comment_like_id`   varchar(64) NULL DEFAULT NULL COMMENT '评论点赞表',
    `prop_name_favorites`   varchar(64) NULL DEFAULT NULL COMMENT '收藏数属性名',
    `prop_name_likes`       varchar(64) NULL DEFAULT NULL COMMENT '点赞数属性名',
    `prop_name_subscribes`  varchar(64) NULL DEFAULT NULL COMMENT '预约数属性名',
    `prop_name_follows`     varchar(64) NULL DEFAULT NULL COMMENT '关注数属性名',
    `prop_name_reads`       varchar(64) NULL DEFAULT NULL COMMENT '已读数属性名',
    `prop_name_readers`     varchar(64) NULL DEFAULT NULL COMMENT '已读人数属性名',
    `prop_name_sticky`      varchar(64) NULL DEFAULT NULL COMMENT '置顶属性名',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '对象';

-- ----------------------------
-- Table structure for rva_object_comment
-- ----------------------------
DROP TABLE IF EXISTS `rva_object_comment`;
CREATE TABLE `rva_object_comment`
(
    `id`               bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `obj_id`           varchar(64) NULL DEFAULT NULL COMMENT '对象ID，rva_object.id',
    `obj_id_value`     varchar(64) NULL DEFAULT NULL COMMENT '对象ID值，对象ID对应表中的ID值，comment_type为0时有效',
    `user_id`          bigint(20) NULL DEFAULT NULL COMMENT '用户ID，sys_user.id',
    `comment_type`     smallint(6) NULL DEFAULT NULL COMMENT '评论类型（0-新评论 1-回复评论）',
    `comment_content`  varchar(500) NULL DEFAULT NULL COMMENT '评论内容',
    `comment_image`    varchar(500) NULL DEFAULT NULL COMMENT '评论图片地址',
    `reply_user_id`    bigint(20) NULL DEFAULT NULL COMMENT '回复用户ID，sys_user.id，comment_type为1时有效',
    `reply_comment_id` bigint(20) NULL DEFAULT NULL COMMENT '回复评论ID，rva_object_comment.id，comment_type为1时有效',
    `comment_ip`       varchar(200) NULL DEFAULT NULL COMMENT '评论者IP',
    `like_count`       int(11) NULL DEFAULT NULL COMMENT '点赞数',
    `create_time`      timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`      timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '更新时间',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '评论';

-- ----------------------------
-- Table structure for rva_object_comment_like
-- ----------------------------
DROP TABLE IF EXISTS `rva_object_comment_like`;
CREATE TABLE `rva_object_comment_like`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `obj_id`       varchar(64) NULL DEFAULT NULL COMMENT '对象ID，rva_object.id',
    `obj_id_value` varchar(64) NULL DEFAULT NULL COMMENT '评论ID，关联rva_object_comment.id',
    `user_id`      bigint(20) NULL DEFAULT NULL COMMENT '用户ID，sys_user.id',
    `create_time`  timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`  timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '更新时间',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '评论点赞';

-- ----------------------------
-- Table structure for rva_object_favorite
-- ----------------------------
DROP TABLE IF EXISTS `rva_object_favorite`;
CREATE TABLE `rva_object_favorite`
(
    `id`           bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `obj_id`       varchar(64) NULL DEFAULT NULL COMMENT '对象ID，rva_object.id',
    `obj_id_value` varchar(64) NULL DEFAULT NULL COMMENT 'ID值，对象ID对应表中的ID值',
    `user_id`      bigint(20) NULL DEFAULT NULL COMMENT '用户ID，sys_user.id',
    `type`         varchar(20) NULL DEFAULT NULL COMMENT '类型（favorite-收藏 like-点赞 subscribe-预约 follow-关注 read-已读）',
    `create_time`  timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`  timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '更新时间',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '收藏';

-- ----------------------------
-- Table structure for rva_process
-- ----------------------------
DROP TABLE IF EXISTS `rva_process`;
CREATE TABLE `rva_process`
(
    `id`             varchar(100) NOT NULL COMMENT 'ID',
    `name`           varchar(100) NULL DEFAULT NULL COMMENT '名称',
    `obj_id`         varchar(100) NULL DEFAULT NULL COMMENT '所属对象，rva_object.id',
    `related_obj_id` varchar(100) NULL DEFAULT NULL COMMENT '关联对象，rva_object.id',
    `module`         varchar(100) NULL DEFAULT NULL COMMENT '模块，rva_module.name',
    `create_by`      bigint(20) NULL DEFAULT NULL COMMENT '创建者',
    `create_time`    datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`      bigint(20) NULL DEFAULT NULL COMMENT '更新者',
    `update_time`    datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `data`           text NULL COMMENT '其他数据',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '流程定义';

-- ----------------------------
-- Table structure for rva_process_task
-- ----------------------------
DROP TABLE IF EXISTS `rva_process_task`;
CREATE TABLE `rva_process_task`
(
    `id`          varchar(100) NOT NULL COMMENT 'ID',
    `name`        varchar(100) NULL DEFAULT NULL COMMENT '名称',
    `key`         varchar(100) NULL DEFAULT NULL COMMENT '标识',
    `decision`    varchar(1) NULL DEFAULT 'N' COMMENT '是否决策',
    `refuse_end`  varchar(1) NULL DEFAULT 'Y' COMMENT '打回结束',
    `process_id`  varchar(100) NULL DEFAULT NULL COMMENT '流程定义，rva_process.id',
    `idx`         smallint(6) NULL DEFAULT 0 COMMENT '排序',
    `create_by`   bigint(20) NULL DEFAULT NULL COMMENT '创建者',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   bigint(20) NULL DEFAULT NULL COMMENT '更新者',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `data`        text NULL COMMENT '其他数据',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '流程任务定义';

-- ----------------------------
-- Table structure for rva_property
-- ----------------------------
DROP TABLE IF EXISTS `rva_property`;
CREATE TABLE `rva_property`
(
    `id`                 varchar(64) NOT NULL COMMENT 'ID',
    `name`               varchar(100) NULL DEFAULT NULL COMMENT '名称',
    `type`               varchar(20) NULL DEFAULT NULL COMMENT '数据类型',
    `type_detail`        varchar(100) NULL DEFAULT NULL COMMENT '数据类型补充',
    `id_gen_type`        varchar(50) NULL DEFAULT NULL COMMENT '主键生成方式',
    `required`           varchar(1) NULL DEFAULT 'N' COMMENT '必需',
    `dict_type`          varchar(100) NULL DEFAULT NULL COMMENT '字典类型',
    `dict_select_single` varchar(1) NULL DEFAULT 'Y' COMMENT '字典单选',
    `value_max`          varchar(200) NULL DEFAULT NULL COMMENT '最大值',
    `value_min`          varchar(200) NULL DEFAULT NULL COMMENT '最小值',
    `default_value`      varchar(200) NULL DEFAULT NULL COMMENT '默认值',
    `number_scale`       int(3) NULL DEFAULT 0 COMMENT '小数位数',
    `idx`                int(3) NULL DEFAULT 0 COMMENT '索引',
    `description`        varchar(500) NULL DEFAULT NULL COMMENT '描述',
    `data`               text NULL COMMENT '其他数据',
    `obj_id`             varchar(64) NOT NULL COMMENT '对象ID',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '属性';

-- ----------------------------
-- Table structure for rva_qingjia
-- ----------------------------
DROP TABLE IF EXISTS `rva_qingjia`;
CREATE TABLE `rva_qingjia`
(
    `id`                       int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `task_apply_tjr`           int(11) NULL DEFAULT NULL COMMENT '申请提交人',
    `task_apply_tjsj`          datetime(0) NULL DEFAULT NULL COMMENT '申请提交时间',
    `task_apply_tjsm`          varchar(200) NULL DEFAULT NULL COMMENT '申请提交说明',
    `task_dept_review_jg`      varchar(1) NULL DEFAULT NULL COMMENT '部门审批结果',
    `task_dept_review_tjr`     int(11) NULL DEFAULT NULL COMMENT '部门审批提交人',
    `task_dept_review_tjsj`    datetime(0) NULL DEFAULT NULL COMMENT '部门审批提交时间',
    `task_dept_review_tjsm`    varchar(200) NULL DEFAULT NULL COMMENT '部门审批提交说明',
    `task_company_review_jg`   varchar(1) NULL DEFAULT NULL COMMENT '公司审批结果',
    `task_company_review_tjr`  int(11) NULL DEFAULT NULL COMMENT '公司审批提交人',
    `task_company_review_tjsj` datetime(0) NULL DEFAULT NULL COMMENT '公司审批提交时间',
    `task_company_review_tjsm` varchar(200) NULL DEFAULT NULL COMMENT '公司审批提交说明',
    `task_report_back_tjr`     int(11) NULL DEFAULT NULL COMMENT '销假提交人',
    `task_report_back_tjsj`    datetime(0) NULL DEFAULT NULL COMMENT '销假提交时间',
    `task_report_back_tjsm`    varchar(200) NULL DEFAULT NULL COMMENT '销假提交说明',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '请假';

-- ----------------------------
-- Table structure for rva_relation
-- ----------------------------
DROP TABLE IF EXISTS `rva_relation`;
CREATE TABLE `rva_relation`
(
    `id`                   varchar(100) NOT NULL COMMENT 'ID',
    `name`                 varchar(100) NULL DEFAULT NULL COMMENT '关系名称',
    `type`                 varchar(20) NULL DEFAULT NULL COMMENT '类型',
    `obj_id`               varchar(64)  NOT NULL COMMENT '对象ID',
    `prop_obj_id`          varchar(64) NULL DEFAULT NULL COMMENT '关联对象属性ID',
    `related_name`         varchar(100) NULL DEFAULT NULL COMMENT '反向关系名称',
    `relation_obj_id`      varchar(64) NULL DEFAULT NULL COMMENT '关系对象ID',
    `relation_obj_prop_id` varchar(64) NULL DEFAULT NULL COMMENT '关系对象关联对象属性ID',
    `idx`                  int(3) NULL DEFAULT 0 COMMENT '索引',
    `data`                 text NULL COMMENT '其他数据',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '关系';

-- ----------------------------
-- Table structure for rva_relationitem
-- ----------------------------
DROP TABLE IF EXISTS `rva_relationitem`;
CREATE TABLE `rva_relationitem`
(
    `id`                       varchar(100) NOT NULL COMMENT 'ID',
    `name`                     varchar(100) NULL DEFAULT NULL COMMENT '关系项名称',
    `relation_id`              varchar(100) NULL DEFAULT NULL COMMENT '关系ID',
    `prop_id`                  varchar(64) NULL DEFAULT NULL COMMENT '本方关联属性',
    `related_name`             varchar(100) NULL DEFAULT NULL COMMENT '反向关系项名称',
    `related_obj_id`           varchar(64) NULL DEFAULT NULL COMMENT '关联对象ID',
    `related_prop_id`          varchar(64) NULL DEFAULT NULL COMMENT '关联对象属性ID',
    `relation_prop_id`         varchar(64) NULL DEFAULT NULL COMMENT '关系对象本方关联属性ID',
    `relation_inverse_prop_id` varchar(64) NULL DEFAULT NULL COMMENT '关系对象他方关联属性ID',
    `idx`                      int(3) NULL DEFAULT 0 COMMENT '索引',
    `data`                     text NULL COMMENT '其他数据',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '关系项';

-- ----------------------------
-- Table structure for rva_tenant
-- ----------------------------
DROP TABLE IF EXISTS `rva_tenant`;
CREATE TABLE `rva_tenant`
(
    `id`          varchar(100) NOT NULL COMMENT 'ID',
    `name`        varchar(100) NOT NULL COMMENT '名称',
    `dept_id`     bigint(20) NULL DEFAULT NULL COMMENT '部门',
    `create_by`   bigint(20) NULL DEFAULT NULL COMMENT '创建者',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   bigint(20) NULL DEFAULT NULL COMMENT '更新者',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `data`        text NULL COMMENT '其他数据',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '租户';

-- ----------------------------
-- Table structure for rva_trigger
-- ----------------------------
DROP TABLE IF EXISTS `rva_trigger`;
CREATE TABLE `rva_trigger`
(
    `id`         varchar(64)  NOT NULL COMMENT 'ID',
    `params`     varchar(200) NOT NULL COMMENT '参数',
    `trigger_if` varchar(500) NOT NULL COMMENT '条件',
    `view_id`    varchar(64)  NOT NULL COMMENT '视图ID',
    `idx`        smallint(6) NULL DEFAULT -1 COMMENT '索引',
    `data`       text NULL COMMENT '其他数据',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '触发器';

-- ----------------------------
-- Table structure for rva_triggeraction
-- ----------------------------
DROP TABLE IF EXISTS `rva_triggeraction`;
CREATE TABLE `rva_triggeraction`
(
    `id`             varchar(100) NOT NULL COMMENT 'ID',
    `action_subject` varchar(64)  NOT NULL COMMENT '动作对象',
    `action`         varchar(50)  NOT NULL COMMENT '动作',
    `action_params`  varchar(10000) NULL DEFAULT NULL COMMENT '动作参数',
    `data`           text NULL COMMENT '其他数据',
    `idx`            smallint(6) NULL DEFAULT -1 COMMENT '索引',
    `trigger_id`     varchar(64)  NOT NULL COMMENT '触发器ID',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '触发动作';

-- ----------------------------
-- Table structure for rva_uniapp
-- ----------------------------
DROP TABLE IF EXISTS `rva_uniapp`;
CREATE TABLE `rva_uniapp`
(
    `id`          varchar(255) NOT NULL COMMENT 'ID',
    `name`        varchar(100) NOT NULL COMMENT '名称',
    `tenant_id`   varchar(100) NULL DEFAULT NULL COMMENT '租户ID',
    `dept_id`     bigint(20) NULL DEFAULT NULL COMMENT '部门',
    `create_by`   bigint(20) NULL DEFAULT NULL COMMENT '创建者',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   bigint(20) NULL DEFAULT NULL COMMENT '更新者',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `data`        text NULL COMMENT '其他数据',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '移动应用';

-- ----------------------------
-- Table structure for rva_uniapp_msg
-- ----------------------------
DROP TABLE IF EXISTS `rva_uniapp_msg`;
CREATE TABLE `rva_uniapp_msg`
(
    `id`           int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `title`        varchar(100) NOT NULL COMMENT '标题',
    `content`      text         NOT NULL COMMENT '内容',
    `msg_url`      varchar(500) NULL DEFAULT NULL COMMENT '消息链接，ruoyi-ui页面地址',
    `msg_app_url`  varchar(500) NULL DEFAULT NULL COMMENT '消息链接，rpp页面地址',
    `send_by`      bigint(20) NULL DEFAULT NULL COMMENT '发送者',
    `received_by`  text NULL COMMENT '接收者',
    `msg_group`    varchar(255) NULL DEFAULT NULL COMMENT '分组',
    `remark`       varchar(255) NULL DEFAULT NULL COMMENT '备注',
    `create_by`    bigint(20) NULL DEFAULT NULL COMMENT '创建者',
    `create_time`  datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`    bigint(20) NULL DEFAULT NULL COMMENT '更新者',
    `update_time`  datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `tenant_id`    varchar(100) NULL DEFAULT NULL COMMENT '租户ID',
    `data`         text NULL COMMENT '其他数据',
    `push_channel` varchar(1000) NULL DEFAULT NULL COMMENT '推送渠道',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '应用消息表';

-- ----------------------------
-- Table structure for rva_uniapp_push
-- ----------------------------
DROP TABLE IF EXISTS `rva_uniapp_push`;
CREATE TABLE `rva_uniapp_push`
(
    `id`              int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `push_channel`    varchar(100) NULL DEFAULT NULL COMMENT '推送渠道，uniPush-uniapp推送，dingPush-钉钉推送，wechatPush-微信推送',
    `cid`             varchar(255) NOT NULL COMMENT 'cid,uniPush-cid,dingPush-dingTalkUserId,wechatPush-微信cid',
    `uniapp_id`       varchar(100) NULL DEFAULT NULL COMMENT '应用ID',
    `user_id`         bigint(20) NOT NULL COMMENT '用户ID',
    `device_platform` varchar(100) NULL DEFAULT NULL COMMENT '设备平台类型',
    `device_brand`    varchar(100) NULL DEFAULT NULL COMMENT '设备品牌',
    `device_system`   varchar(100) NULL DEFAULT NULL COMMENT '设备系统',
    `tenant_id`       varchar(100) NULL DEFAULT NULL COMMENT '租户ID',
    `token`           varchar(1000) NULL DEFAULT NULL COMMENT '用户登录凭证',
    `update_time`     datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '用户消息推送设备';

-- ----------------------------
-- Table structure for rva_uniapp_push_msg
-- ----------------------------
DROP TABLE IF EXISTS `rva_uniapp_push_msg`;
CREATE TABLE `rva_uniapp_push_msg`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `msg_id`       bigint(20) NULL DEFAULT NULL COMMENT 'ID值，rva_uniapp_msg.id',
    `user_id`      bigint(20) NULL DEFAULT NULL COMMENT '接收者，sys_user.id，rva_uniapp_msg.received_by中的一个',
    `title`        varchar(100) NOT NULL COMMENT '标题，rva_uniapp_msg.title',
    `content`      text         NOT NULL COMMENT '内容，rva_uniapp_msg.content',
    `msg_url`      varchar(500) NULL DEFAULT NULL COMMENT '消息链接，ruoyi-ui页面地址，rva_uniapp_msg.msg_url',
    `msg_app_url`  varchar(500) NULL DEFAULT NULL COMMENT '消息链接，rpp页面地址，rva_uniapp_msg.msg_app_url',
    `msg_group`    varchar(255) NULL DEFAULT NULL COMMENT '分组，rva_uniapp_msg.msg_group',
    `send_by`      bigint(20) NULL DEFAULT NULL COMMENT '发送者，sys_user.id，rva_uniapp_msg.send_by',
    `cid`          varchar(255) NOT NULL COMMENT '推送ID，rva_uniapp_push.cid',
    `create_time`  datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`  datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `push_status`  varchar(10) NULL DEFAULT '01' COMMENT '推送状态，01-未推送，02-已推送',
    `push_channel` varchar(10) NULL COMMENT '推送渠道',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '推送消息明细，每个用户关联的渠道设备一个';

-- ----------------------------
-- Table structure rva_uniapp_msg_user
-- ----------------------------
DROP TABLE IF EXISTS `rva_uniapp_msg_user`;
CREATE TABLE `rva_uniapp_msg_user`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `msg_id`      bigint(20) DEFAULT NULL COMMENT 'ID值，rva_uniapp_msg.id',
    `user_id`     bigint(20) DEFAULT NULL COMMENT '接收者，sys_user.id，rva_uniapp_msg.received_by中的一个',
    `read_status` varchar(20)  DEFAULT '01' COMMENT '阅读状态,01-未读，02-已读',
    `title`       varchar(100) NOT NULL COMMENT '标题，rva_uniapp_msg.title',
    `content`     text         NOT NULL COMMENT '内容，rva_uniapp_msg.content',
    `msg_url`     varchar(500) DEFAULT NULL COMMENT '消息链接，ruoyi-ui页面地址，rva_uniapp_msg.msg_url',
    `msg_app_url` varchar(500) DEFAULT NULL COMMENT '消息链接，rpp页面地址，rva_uniapp_msg.msg_app_url',
    `msg_group`   varchar(255) DEFAULT NULL COMMENT '分组，rva_uniapp_msg.msg_group',
    `send_by`     bigint(20) DEFAULT NULL COMMENT '发送者，sys_user.id，rva_uniapp_msg.send_by',
    `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime     DEFAULT NULL COMMENT '更新时间',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='消息用户关联表';

-- ----------------------------
-- Table structure for rva_uniapp_permission
-- ----------------------------
DROP TABLE IF EXISTS `rva_uniapp_permission`;
CREATE TABLE `rva_uniapp_permission`
(
    `id`          varchar(100) NOT NULL COMMENT '权限ID',
    `uniapp_id`   varchar(100) NULL DEFAULT NULL COMMENT '应用ID，不设则为公共权限',
    `name`        varchar(50)  NOT NULL COMMENT '菜单名称',
    `parent_id`   varchar(255) NULL DEFAULT '0' COMMENT '父菜单ID',
    `order_num`   int(11) NULL DEFAULT 0 COMMENT '显示顺序',
    `path`        varchar(200) NULL DEFAULT NULL COMMENT '路由地址',
    `query`       varchar(255) NULL DEFAULT NULL COMMENT '路由参数',
    `is_frame`    int(11) NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
    `is_cache`    int(11) NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
    `menu_type`   varchar(1) NULL DEFAULT NULL COMMENT '菜单类型',
    `visible`     varchar(1) NULL DEFAULT '0' COMMENT '显示状态',
    `status`      varchar(1) NULL DEFAULT '0' COMMENT '菜单状态',
    `icon`        varchar(500) NULL DEFAULT NULL COMMENT '菜单图标',
    `color`       varchar(50) NULL DEFAULT NULL COMMENT '图标颜色',
    `remark`      varchar(500) NULL DEFAULT NULL COMMENT '备注',
    `tenant_id`   varchar(100) NULL DEFAULT NULL COMMENT '租户ID',
    `create_by`   bigint(20) NULL DEFAULT NULL COMMENT '创建者',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   bigint(20) NULL DEFAULT NULL COMMENT '更新者',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `data`        text NULL COMMENT '其他数据',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '移动应用权限';



-- ----------------------------
-- Table structure for rva_uniapp_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `rva_uniapp_role_permission`;
CREATE TABLE `rva_uniapp_role_permission`
(
    `permission_id` varchar(100) NOT NULL COMMENT '权限ID',
    `uniapp_id`     varchar(100) NOT NULL COMMENT '应用ID',
    `role_id`       bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳'
) ENGINE = InnoDB COMMENT = '应用权限角色关联表';

DROP TABLE IF EXISTS `rva_uniapp_category`;
CREATE TABLE `rva_uniapp_category`
(
    `category_id`      bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类id',
    `parent_id`        bigint(20) DEFAULT '0' COMMENT '父分类id',
    `ancestors`        varchar(50)        DEFAULT NULL COMMENT '祖级列表',
    `category_name`    varchar(30)        DEFAULT NULL COMMENT '分类名称',
    `order_num`        int(11) DEFAULT '0' COMMENT '显示顺序',
    `status`           varchar(1)         DEFAULT '0' COMMENT '分类状态（0正常 1停用）',
    `del_flag`         varchar(1)         DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`        varchar(64)        DEFAULT NULL COMMENT '创建者',
    `create_time`      datetime           DEFAULT NULL COMMENT '创建时间',
    `update_by`        varchar(64)        DEFAULT NULL COMMENT '更新者',
    `update_time`      datetime           DEFAULT NULL COMMENT '更新时间',
    `type`             int(11) DEFAULT NULL COMMENT '分类(1-知识库分类)',
    `rva_update_time_` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
    PRIMARY KEY (`category_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=125 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='分类表';

DROP TABLE IF EXISTS `rva_uniapp_tablist`;
CREATE TABLE `rva_uniapp_tablist`
(
    `id`               int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `title`            varchar(100) NOT NULL COMMENT '标题',
    `excerpt`          varchar(200)          DEFAULT NULL COMMENT '摘要',
    `content`          text COMMENT '内容',
    `image`            varchar(1000)         DEFAULT NULL COMMENT '图片',
    `video`            varchar(1000)         DEFAULT NULL COMMENT '视频',
    `mode`             varchar(20)           DEFAULT NULL COMMENT '排版模式',
    `disabled`         varchar(1)            DEFAULT 'N' COMMENT '禁用',
    `category`         varchar(300)          DEFAULT NULL COMMENT '分类',
    `group`            varchar(300)          DEFAULT NULL COMMENT '分组',
    `uni_app_id`       varchar(100)          DEFAULT NULL COMMENT '移动应用, rva_fe_app.id',
    `platform`         varchar(100)          DEFAULT NULL COMMENT '平台，pc/ios/android等',
    `device`           varchar(100)          DEFAULT NULL COMMENT '设备',
    `dept_id`          bigint(20) DEFAULT NULL COMMENT '部门',
    `tenant_id`        varchar(100)          DEFAULT NULL COMMENT '租户',
    `create_by`        bigint(20) DEFAULT NULL COMMENT '创建者',
    `create_time`      datetime              DEFAULT NULL COMMENT '创建时间',
    `update_by`        bigint(20) DEFAULT NULL COMMENT '更新者',
    `update_time`      datetime              DEFAULT NULL COMMENT '更新时间',
    `data`             text COMMENT '其他数据',
    `type`             int(11) DEFAULT NULL COMMENT '类型',
    `public`           varchar(10)           DEFAULT 'Y' COMMENT '公开',
    `rva_update_time_` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='分类列表';

-- ----------------------------
-- Table structure for rva_view
-- ----------------------------
DROP TABLE IF EXISTS `rva_view`;
CREATE TABLE `rva_view`
(
    `id`                     varchar(64) NOT NULL COMMENT 'ID',
    `name`                   varchar(100) NULL DEFAULT NULL COMMENT '名称',
    `type`                   varchar(50) NULL DEFAULT NULL COMMENT '类型',
    `idx`                    int(6) NULL DEFAULT 0 COMMENT '索引',
    `width`                  int(6) NULL DEFAULT 0 COMMENT '宽度',
    `height`                 int(6) NULL DEFAULT 0 COMMENT '高度',
    `obj_id`                 varchar(64) NOT NULL COMMENT '对象ID',
    `load_before_sql`        varchar(2000) NULL DEFAULT NULL COMMENT '加载前sql',
    `load_after_sql`         varchar(2000) NULL DEFAULT NULL COMMENT '加载后sql',
    `load_sql`               varchar(500) NULL DEFAULT NULL COMMENT '加载数据sql',
    `load_where`             varchar(10000) NULL DEFAULT NULL COMMENT '数据加载条件',
    `form_columns`           int(3) NULL DEFAULT 1 COMMENT '列数',
    `form_readonly`          varchar(1) NULL DEFAULT 'N' COMMENT '只读',
    `load_url`               varchar(500) NULL DEFAULT NULL COMMENT '加载数据url',
    `form_submit_url`        varchar(500) NULL DEFAULT NULL COMMENT '表单提交url',
    `form_submit_before_sql` varchar(2000) NULL DEFAULT NULL COMMENT '提交前sql',
    `form_submit_after_sql`  varchar(2000) NULL DEFAULT NULL COMMENT '提交后sql',
    `list_rows`              int(3) NULL DEFAULT 10 COMMENT '列表最大行数',
    `list_paging`            varchar(1) NULL DEFAULT 'Y' COMMENT '是否分页',
    `data`                   text NULL COMMENT '其他数据',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '视图';

-- ----------------------------
-- Table structure for rva_viewbutton
-- ----------------------------
DROP TABLE IF EXISTS `rva_viewbutton`;
CREATE TABLE `rva_viewbutton`
(
    `id`                    varchar(64) NOT NULL COMMENT 'ID',
    `name`                  varchar(100) NULL DEFAULT NULL COMMENT '名称',
    `type`                  varchar(200) NULL DEFAULT NULL COMMENT '类型',
    `idx`                   int(11) NULL DEFAULT 0 COMMENT '索引',
    `icon`                  varchar(50) NULL DEFAULT NULL COMMENT '图标',
    `cls`                   varchar(50) NULL DEFAULT NULL COMMENT '样式',
    `position`              varchar(50) NULL DEFAULT NULL COMMENT '位置',
    `readonly_show`         varchar(1) NULL DEFAULT 'N' COMMENT '只读显示',
    `action`                varchar(500) NULL DEFAULT NULL COMMENT '动作',
    `action_url`            varchar(4000) NULL DEFAULT NULL COMMENT '动作Url',
    `action_dialog_view_id` varchar(1000) NULL DEFAULT NULL COMMENT '弹框视图',
    `action_dialog_app_id`  varchar(1000) NULL DEFAULT NULL COMMENT '弹框应用',
    `action_success`        varchar(30) NULL DEFAULT NULL COMMENT '动作成功',
    `action_failure`        varchar(30) NULL DEFAULT NULL COMMENT '动作失败',
    `select_type`           varchar(30) NULL DEFAULT NULL COMMENT 'selectType',
    `show_if`               varchar(200) NULL DEFAULT NULL COMMENT '显示条件',
    `view_id`               varchar(64) NOT NULL COMMENT '视图ID',
    `data`                  text NULL COMMENT '其他数据',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '视图属性';

-- ----------------------------
-- Table structure for rva_viewproperty
-- ----------------------------
DROP TABLE IF EXISTS `rva_viewproperty`;
CREATE TABLE `rva_viewproperty`
(
    `id`                        varchar(64) NOT NULL COMMENT 'ID',
    `name`                      varchar(100) NULL DEFAULT NULL COMMENT '名称',
    `type`                      varchar(50) NULL DEFAULT NULL COMMENT '类型',
    `idx`                       int(11) NULL DEFAULT 0 COMMENT '索引',
    `prop_id`                   varchar(64) NULL DEFAULT NULL COMMENT '属性ID',
    `prop_sub_id`               varchar(64) NULL DEFAULT NULL COMMENT '属性子ID',
    `relation_id`               varchar(100) NULL DEFAULT NULL COMMENT '关系ID',
    `width`                     int(11) NULL DEFAULT 0 COMMENT '宽度',
    `height`                    int(11) NULL DEFAULT 0 COMMENT '高度',
    `form_init_value`           varchar(1000) NULL DEFAULT NULL COMMENT '加载值',
    `form_init_replace_empty`   varchar(1) NULL DEFAULT 'Y' COMMENT '空时替换加载值',
    `form_init_replace`         varchar(1) NULL DEFAULT 'N' COMMENT '非空替换加载值',
    `form_submit_value`         varchar(1000) NULL DEFAULT NULL COMMENT '提交值',
    `form_submit_replace_empty` varchar(1) NULL DEFAULT 'N' COMMENT '空时替换提交值',
    `form_submit_replace`       varchar(1) NULL DEFAULT 'N' COMMENT '非空替换提交值',
    `form_submit`               varchar(1) NULL DEFAULT 'Y' COMMENT '是否提交',
    `form_row_span`             int(11) NULL DEFAULT 1 COMMENT '跨行',
    `form_col_span`             int(11) NULL DEFAULT 1 COMMENT '跨列',
    `form_required`             varchar(1) NULL DEFAULT 'N' COMMENT '必需',
    `form_value_max`            varchar(200) NULL DEFAULT NULL COMMENT '最大值',
    `form_value_min`            varchar(200) NULL DEFAULT NULL COMMENT '最小值',
    `form_readonly`             varchar(1) NULL DEFAULT 'N' COMMENT '只读',
    `form_related_crud`         varchar(200) NULL DEFAULT NULL COMMENT '关联CRUD',
    `form_selector_single`      varchar(1) NULL DEFAULT 'Y' COMMENT 'selector单选',
    `form_inputor_search`       varchar(200) NULL DEFAULT NULL COMMENT 'inputor查询url',
    `form_inputor_data`         varchar(200) NULL DEFAULT NULL COMMENT 'inputor数据url',
    `form_filter_prop`          varchar(64) NULL DEFAULT NULL COMMENT '过滤属性ID',
    `form_filter_value`         varchar(64) NULL DEFAULT NULL COMMENT '过滤属性值',
    `list_order_type`           varchar(5) NULL DEFAULT 'ASC' COMMENT '排序方式',
    `list_order_idx`            int(11) NULL DEFAULT -1 COMMENT '排序索引',
    `list_sql`                  varchar(500) NULL DEFAULT NULL COMMENT 'sql获取值',
    `list_expression`           varchar(500) NULL DEFAULT NULL COMMENT '表达式获取值',
    `list_buttons`              varchar(500) NULL DEFAULT NULL COMMENT '按钮',
    `search_filter`             varchar(1) NULL DEFAULT 'N' COMMENT '查询过滤器',
    `search_type`               varchar(50) NULL DEFAULT NULL COMMENT '查询类型',
    `search_expression`         varchar(200) NULL DEFAULT NULL COMMENT '查询表达式',
    `number_scale`              int(11) NULL DEFAULT 0 COMMENT '小数位数',
    `dict_type`                 varchar(100) NULL DEFAULT NULL COMMENT '字典类型',
    `view_id`                   varchar(64) NOT NULL COMMENT '视图ID',
    `data`                      text NULL COMMENT '其他数据',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '视图属性';

-- ----------------------------
-- Table structure for rva_material
-- ----------------------------
DROP TABLE IF EXISTS `rva_material`;
CREATE TABLE `rva_material`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`        varchar(100) NOT NULL COMMENT '名称',
    `dept_id`     bigint(20) DEFAULT NULL COMMENT '部门',
    `idx`         smallint(6) DEFAULT '0' COMMENT '排序',
    `create_by`   bigint(20) DEFAULT NULL COMMENT '创建者',
    `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`   bigint(20) DEFAULT NULL COMMENT '更新者',
    `update_time` datetime     DEFAULT NULL COMMENT '更新时间',
    `data`        text COMMENT '其他数据',
    `type`        varchar(100) DEFAULT NULL COMMENT '类型',
    `category`    varchar(100) DEFAULT NULL COMMENT '分类',
    `image`       varchar(300) DEFAULT NULL COMMENT '图片',
    `video`       varchar(300) DEFAULT NULL COMMENT '视频',
    `content`     varchar(100) DEFAULT NULL COMMENT '文案',
    `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='素材库';

-- ----------------------------
-- 所有表上增加 rva_update_time_，脚本生成：
-- select concat('alter table ', table_name, " add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';") from information_schema.TABLES where TABLE_SCHEMA = database() and TABLE_TYPE = 'BASE TABLE' ORDER BY TABLE_NAME
-- ----------------------------
-- alter table rva_app
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_appitem
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_chart
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_chart_axis
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_chart_datacolumn
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_chart_dataset
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_chart_formula
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_chart_grid
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_chart_series
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_fe_list
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_fe_list_item
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_inserts
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_kpi
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_kpi_item
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_log
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_module
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_module_view
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_object
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_object_comment
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_object_comment_like
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_object_favorite
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_process
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_process_task
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_property
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_qingjia
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_relation
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_relationitem
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_tenant
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_trigger
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_triggeraction
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_uniapp
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_uniapp_msg
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_uniapp_msg_user
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_uniapp_permission
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_uniapp_push
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_uniapp_push_msg
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_uniapp_role_permission
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_uniapp_tablist
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_view
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_viewbutton
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';
-- alter table rva_viewproperty
--     add `rva_update_time_` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '时间戳';

-- ----------------------------
-- max_allowed_packet调大一点
-- ----------------------------
set global max_allowed_packet = 41943040;

SET
FOREIGN_KEY_CHECKS = 1;
