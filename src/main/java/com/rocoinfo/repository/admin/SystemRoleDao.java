package com.rocoinfo.repository.admin;

import com.rocoinfo.common.persistence.CrudDao;
import com.rocoinfo.entity.admin.SystemRole;
import com.rocoinfo.entity.admin.SystemUserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <dl>
 * <dd>Description: 管理员角色Dao</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/31 上午10:58</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@Repository
public interface SystemRoleDao extends CrudDao<SystemRole> {
    /**
     * 根据名称查询角色信息，排除指定id
     *
     * @param id   id
     * @param name 名称
     * @return
     */
    SystemRole getByNameExceptId(@Param("id") Long id, @Param("name") String name);

    /**
     * 根据角色id删除角色用户关联关心
     *
     * @param id 角色id
     */
    void deleteUserRolesByRoleId(Long id);

    /**
     * 根据用户id查询用户角色信息
     *
     * @param userId 用户id
     * @return
     */
    List<SystemRole> getRolesByUserId(Long userId);

    /**
     * 根据用户id删除用户角色关联关系
     *
     * @param userId 用户id
     */
    void deleteUserRolesByUserId(@Param("userId") Long userId);

    /**
     * 批量插入用户角色关联关心
     *
     * @param userRoles 用户角色关联关系
     */
    void batchInsertUserRole(@Param("userRoles") List<SystemUserRole> userRoles);
}
