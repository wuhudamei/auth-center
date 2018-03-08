package cn.damei.repository.admin;

import cn.damei.common.persistence.CrudDao;
import cn.damei.entity.admin.SystemRole;
import cn.damei.entity.admin.SystemUserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SystemRoleDao extends CrudDao<SystemRole> {

    SystemRole getByNameExceptId(@Param("id") Long id, @Param("name") String name);


    void deleteUserRolesByRoleId(Long id);


    List<SystemRole> getRolesByUserId(Long userId);


    void deleteUserRolesByUserId(@Param("userId") Long userId);


    void batchInsertUserRole(@Param("userRoles") List<SystemUserRole> userRoles);
}
