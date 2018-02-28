package com.rocoinfo.repository.auth;

import com.rocoinfo.common.persistence.CrudDao;
import com.rocoinfo.entity.auth.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <dl>
 * <dd>Description: 应用角色Dao</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/31 下午17:15</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
@Repository(value = "appRoleDao")
public interface RoleDao extends CrudDao<Role> {
    /**
     * appId
     */
    String APP_ID = "appId";


    /**
     * 根据角色名称和应用id查询 角色
     * @param roleName
     * @param appId
     * @return
     */
    Role selectRoleByNameAndAppId(@Param("roleName") String roleName,@Param("appId")Long appId);

}