package com.rocoinfo.service.auth;

import com.rocoinfo.common.service.CrudService;
import com.rocoinfo.entity.auth.UserRole;
import com.rocoinfo.repository.auth.UserRoleDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <dl>
 * <dd>Description: </dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/2 上午10:35</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@Service
public class UserRoleService extends CrudService<UserRoleDao, UserRole> {

    /**
     * 删除原有的用户角色关联关系(需要限制app的范围，不能全删)
     *
     * @param userId 用户id
     * @param appIds 应用id
     */
    public void deleteByUserIdAndAppIds(Long userId, List<Long> appIds) {
        if (userId != null && CollectionUtils.isNotEmpty(appIds)) {
            this.entityDao.deleteByUserIdAndAppIds(userId, appIds);
        }
    }

    /**
     * 批量插入
     *
     * @param userRoles 用户角色关联关系
     */
    public void batchInsert(List<UserRole> userRoles) {
        if (CollectionUtils.isNotEmpty(userRoles)) {
            this.entityDao.batchInsert(userRoles);
        }
    }
}
