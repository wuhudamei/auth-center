package cn.damei.repository.auth;

import cn.damei.common.persistence.CrudDao;
import cn.damei.entity.auth.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository(value = "appRoleDao")
public interface RoleDao extends CrudDao<Role> {

    String APP_ID = "appId";



    Role selectRoleByNameAndAppId(@Param("roleName") String roleName, @Param("appId")Long appId);

}