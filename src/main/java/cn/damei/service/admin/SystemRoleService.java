package cn.damei.service.admin;

import cn.damei.dto.StatusDto;
import cn.damei.common.service.CrudService;
import cn.damei.entity.admin.SystemPermission;
import cn.damei.entity.admin.SystemRole;
import cn.damei.entity.admin.SystemRolePermission;
import cn.damei.entity.admin.SystemUserRole;
import cn.damei.repository.admin.SystemRoleDao;
import cn.damei.repository.admin.SystemRolePermissionDao;
import cn.damei.service.auth.AppService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SystemRoleService extends CrudService<SystemRoleDao, SystemRole> {

    @Autowired
    private SystemRolePermissionDao rolePermissionDao;
    @Autowired
    private AppService appService;


    @Transactional(rollbackFor = Exception.class)
    public StatusDto saveOrUpdate(SystemRole role) {
        SystemRole tempRole = entityDao.getByNameExceptId(role.getId(), role.getName());
        if (tempRole != null) {
            return StatusDto.buildFailure("角色名称已存在！");
        }
        int i = 0;
        if (role.getId() != null) {
            i = entityDao.update(role);
        } else {
            i = entityDao.insert(role);
        }
        if (i <= 0) {
            StatusDto.buildFailure("操作失败！");
        }
        return StatusDto.buildSuccess("操作成功！");
    }


    @Transactional(rollbackFor = Exception.class)
    public StatusDto deleteRole(Long id) {
        // 删除角色
        this.entityDao.deleteById(id);
        // 删除角色用户关联
        this.entityDao.deleteUserRolesByRoleId(id);
        // 删除角色权限关联
        this.rolePermissionDao.deleteByRoleId(id);
        return StatusDto.buildSuccess("删除角色成功！");
    }


    public List<SystemPermission> findRolePermission(Long id) {
        return this.rolePermissionDao.findRolePermission(id);
    }


    @Transactional(rollbackFor = Exception.class)
    public Object setRolePermissions(Long id, List<Long> permissionIds) {
        SystemRole role = entityDao.getById(id);
        if (role == null)
            return StatusDto.buildFailure("系统查询不到此角色信息！");
        // 删除旧的角色权限关联关系
        this.rolePermissionDao.deleteByRoleId(id);
        if (CollectionUtils.isNotEmpty(permissionIds)) {
            List<SystemRolePermission> rolePermissions = permissionIds.stream().map((o) -> new SystemRolePermission(id, o)).collect(Collectors.toList());
            // 批量插入新的角色关联关系
            this.rolePermissionDao.batchInsert(rolePermissions);
        }
        return StatusDto.buildSuccess("权限设置成功！");
    }


    public List<SystemRole> getRolesByUserId(Long userId) {
        return entityDao.getRolesByUserId(userId);
    }


    @Transactional(rollbackFor = Exception.class)
    public StatusDto setRolesByUserId(Long userId, List<Long> roleIds) {
        if (userId == null || CollectionUtils.isEmpty(roleIds)) {
            return StatusDto.buildFailure("id为null或没有选择角色");
        }
        // 删除原有的用户角色关联关系
        this.entityDao.deleteUserRolesByUserId(userId);
        // 建立新的用户角色关联关系
        List<SystemUserRole> userRoles = roleIds.stream().map((o) -> new SystemUserRole(userId, o)).collect(Collectors.toList());
        this.entityDao.batchInsertUserRole(userRoles);
        return StatusDto.buildSuccess();
    }
}
