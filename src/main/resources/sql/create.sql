DROP TABLE IF EXISTS `auth_app`;

CREATE TABLE `auth_app` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '应用名称',
  `appid` varchar(50) NOT NULL COMMENT '应用id 系统生成',
  `secret` varchar(50) NOT NULL COMMENT '应用秘钥 系统生成',
  `token` varchar(50) NOT NULL COMMENT '应用token 用户输入',
  `url` varchar(50) NOT NULL COMMENT '认证中心向应用推消息的url',
  `wx_appid` varchar(50) NOT NULL COMMENT '微信的appid  用于微信授权获取用户openid',
  `wx_secret` varchar(50) NOT NULL COMMENT '微信公众号的秘钥',
  `push_flag` tinyint(1) NOT NULL COMMENT '是否开启消息推送，如果开启则表示允许认证中心向应用推送相应的消息',
  `status` varchar(255) NOT NULL COMMENT '应用状态，启用、禁用',
  `deleted` tinyint(1) NOT NULL COMMENT '逻辑删除标志 0：未删除 1：已删除',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='应用信息表';

/*Table structure for table `auth_permission` */

DROP TABLE IF EXISTS `auth_permission`;

CREATE TABLE `auth_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL COMMENT '权限名称',
  `description` varchar(255) DEFAULT NULL COMMENT '权限描述',
  `permission` varchar(255) NOT NULL COMMENT '权限的值',
  `seq` int(11) NOT NULL DEFAULT '0' COMMENT '权限排序值 按从大到小的倒叙排列',
  `pid` int(11) NOT NULL COMMENT '父权限的id',
  `app_id` int(11) NOT NULL COMMENT '应用的id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='权限信息表';

/*Table structure for table `auth_role` */

DROP TABLE IF EXISTS `auth_role`;

CREATE TABLE `auth_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL COMMENT '角色姓名',
  `description` varchar(255) NOT NULL COMMENT '角色描述',
  `app_id` int(11) NOT NULL COMMENT '应用id',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标志位',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='角色信息表';

/*Table structure for table `auth_role_permission` */

DROP TABLE IF EXISTS `auth_role_permission`;

CREATE TABLE `auth_role_permission` (
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `permission_id` int(11) NOT NULL COMMENT '权限id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色权限关联关系表';


/*Table structure for table `auth_user_app` */

DROP TABLE IF EXISTS `auth_user_app`;

CREATE TABLE `auth_user_app` (
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `app_id` int(11) NOT NULL COMMENT '应用id 系统生成的自增主键'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户应用关联关系表';

/*Table structure for table `auth_user_role` */

DROP TABLE IF EXISTS `auth_user_role`;

CREATE TABLE `auth_user_role` (
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `role_id` int(11) NOT NULL COMMENT '角色id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关联关系表';

/*Table structure for table `auth_system_permission` */

DROP TABLE IF EXISTS `auth_system_permission`;

CREATE TABLE `auth_system_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) DEFAULT NULL COMMENT '权限名称',
  `module` varchar(255) DEFAULT NULL COMMENT '模块名称',
  `seq` int(11) NOT NULL COMMENT '排序字段',
  `permission` varchar(255) DEFAULT NULL COMMENT '权限名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='权限表';

/*Table structure for table `auth_system_role` */

DROP TABLE IF EXISTS `auth_system_role`;

CREATE TABLE `auth_system_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(80) NOT NULL COMMENT '角色名称',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='角色信息表';

/*Table structure for table `auth_system_role_permission` */

DROP TABLE IF EXISTS `auth_system_role_permission`;

CREATE TABLE `auth_system_role_permission` (
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `permission_id` int(11) NOT NULL COMMENT '权限id',
  PRIMARY KEY (`role_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色权限关联表';

/*Table structure for table `auth_system_user` */

DROP TABLE IF EXISTS `auth_system_user`;

CREATE TABLE `auth_system_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `password` varchar(50) NOT NULL,
  `salt` varchar(20) NOT NULL COMMENT '盐值',
  `name` varchar(20) NOT NULL COMMENT '真实姓名',
  `mobile` varchar(20) NOT NULL,
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `department` varchar(20) DEFAULT NULL COMMENT '部门',
  `company` varchar(50) DEFAULT NULL COMMENT '公司',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标志位',
  `status` varchar(10) NOT NULL COMMENT '状态\r\n            1:正常',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` int(11) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `auth_system_user_app` */

DROP TABLE IF EXISTS `auth_system_user_app`;

CREATE TABLE `auth_system_user_app` (
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `app_id` int(11) NOT NULL COMMENT '应用id',
  PRIMARY KEY (`user_id`,`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员应用关联表';

/*Table structure for table `auth_system_user_role` */

DROP TABLE IF EXISTS `auth_system_user_role`;

CREATE TABLE `auth_system_user_role` (
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员角色关联表';