package com.rocoinfo.repository.auth;

import com.rocoinfo.common.persistence.CrudDao;
import com.rocoinfo.entity.auth.UserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <dl>
 * <dd>Description: 用户角色Dao</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/2 上午10:34</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@Repository
public interface UserRoleDao extends CrudDao<UserRole> {

    /**
     * 批量插入
     *
     * @param userRoles 用户角色关联关系
     */
    void batchInsert(@Param("userRoles") List<UserRole> userRoles);

    /**
     * 删除原有的用户角色关联关系(需要限制app的范围，不能全删)
     *
     * @param userId 用户id
     * @param appIds 应用id
     */
    void deleteByUserIdAndAppIds(@Param("userId") Long userId, @Param("appIds") List<Long> appIds);
}
