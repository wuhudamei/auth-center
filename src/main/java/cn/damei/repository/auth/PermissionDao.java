package cn.damei.repository.auth;

import cn.damei.common.persistence.CrudDao;
import cn.damei.entity.auth.Permission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository(value = "appPermission")
public interface PermissionDao extends CrudDao<Permission> {


    List<Permission> findByAppId(@Param(value = "appId") Long appId);
}