package com.rocoinfo.repository.auth;

import com.rocoinfo.common.persistence.CrudDao;
import com.rocoinfo.entity.auth.Permission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <dl>
 * <dd>Description: 应用权限Dao</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/31 下午17:12</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
@Repository(value = "appPermission")
public interface PermissionDao extends CrudDao<Permission> {

    /**
     * 查询指定appId的权限
     *
     * @param appId 应用Id
     * @return 返回应用权限列表
     */
    List<Permission> findByAppId(@Param(value = "appId") Long appId);
}