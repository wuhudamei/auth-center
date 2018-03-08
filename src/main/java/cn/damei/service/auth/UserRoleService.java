package cn.damei.service.auth;

import cn.damei.common.service.CrudService;
import cn.damei.entity.auth.UserRole;
import cn.damei.repository.auth.UserRoleDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserRoleService extends CrudService<UserRoleDao, UserRole> {


    public void deleteByUserIdAndAppIds(Long userId, List<Long> appIds) {
        if (userId != null && CollectionUtils.isNotEmpty(appIds)) {
            this.entityDao.deleteByUserIdAndAppIds(userId, appIds);
        }
    }


    public void batchInsert(List<UserRole> userRoles) {
        if (CollectionUtils.isNotEmpty(userRoles)) {
            this.entityDao.batchInsert(userRoles);
        }
    }
}
