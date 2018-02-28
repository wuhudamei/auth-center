insert into auth_system_user ( username, password, salt, name, mobile, email, department, company, deleted, status, create_time, create_user)
values ( 'admin', '83ce7b1bb2111d4de5ee36f7ab4f6dc0b0714378', 'c31c0317b5931ee8', '超级管理员', '', null, null, null, '0', 'OPEN', '2017-05-31 19:33:52', '1');

insert into auth_system_role ( name, description) values ( '超级管理员', '超级管理员');

insert into auth_system_user_role ( user_id, role_id) values ( '1', '1');

Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values(1,'所有权限','embed',0,'*');

Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values(21,'系统管理-菜单','系统管理',2,'system:manager');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (22,'管理员管理-菜单','系统管理',2,'admin:menu');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (23,'管理员管理-列表','系统管理',2,'admin:list');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (24,'管理员管理-新增','系统管理',2,'admin:add');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (25,'管理员管理-编辑','系统管理',2,'admin:edit');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (26,'管理员管理-删除','系统管理',2,'admin:delete');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (27,'管理员管理-重置密码','系统管理',2,'admin:pwd');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (28,'管理员管理-设置角色','系统管理',2,'admin:role');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (29,'管理员管理-设置应用','系统管理',2,'admin:app');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (211,'管理员管理-锁定启用','系统管理',2,'admin:switch');

insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (30,'角色管理-菜单','系统管理',2,'role:menu');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (31,'角色管理-列表','系统管理',2,'role:list');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (32,'角色管理-添加','系统管理',2,'role:add');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (33,'角色管理-编辑','系统管理',2,'role:edit');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (34,'角色管理-授权','系统管理',2,'role:auth');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (35,'角色管理-删除','系统管理',2,'role:delete');

Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (41,'应用管理-菜单','应用管理',3,'app:menu');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (42,'应用管理-列表','应用管理',3,'app:list');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (43,'应用管理-新增','应用管理',3,'app:add');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (44,'应用管理-编辑','应用管理',3,'app:edit');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (45,'应用管理-删除','应用管理',3,'app:delete');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (46,'应用管理-角色设置','应用管理',3,'app:role');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (47,'应用管理-权限设置','应用管理',3,'app:permission');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (48,'应用管理-开启锁定','应用管理',3,'app:switch');

Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (51,'用户管理-菜单','用户管理',4,'user:menu');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (52,'用户管理-列表','用户管理',4,'user:list');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (53,'用户管理-新增','用户管理',4,'user:add');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (54,'用户管理-编辑','用户管理',4,'user:edit');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (55,'用户管理-删除','用户管理',4,'user:delete');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (56,'用户管理-设置角色','用户管理',4,'user:role');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (57,'用户管理-设置应用','用户管理',4,'user:app');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (58,'用户管理-锁定开启','用户管理',4,'user:switch');
Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (59,'用户管理-重置密码','用户管理',4,'user:pwd');

Insert into auth_system_permission(ID,NAME,MODULE,SEQ,PERMISSION) values (61,'修改密码','修改密码',5,'pwd:edit');

insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '21');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '22');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '23');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '24');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '25');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '26');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '27');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '28');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '29');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '30');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '31');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '32');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '33');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '34');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '35');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '41');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '42');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '43');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '44');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '45');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '46');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '47');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '48');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '51');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '52');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '53');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '54');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '55');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '56');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '57');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '58');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '59');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '61');
insert into auth_system_role_permission ( role_id, permission_id) values ( '1', '211');