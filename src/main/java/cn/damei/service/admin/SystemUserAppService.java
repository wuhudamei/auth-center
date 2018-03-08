package cn.damei.service.admin;

import cn.damei.common.service.CrudService;
import cn.damei.entity.admin.SystemUserApp;
import cn.damei.entity.auth.App;
import cn.damei.repository.admin.SystemUserAppDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SystemUserAppService extends CrudService<SystemUserAppDao, SystemUserApp> {


    public List<App> findAppsByUserId(Long userId) {
        if (userId != null) {
            return this.entityDao.findAppsByUserId(userId);
        }
        return null;
    }


    public List<App> findAppsByUserIdWithEcho(Long userId) {
        if (userId != null) {
            return this.entityDao.findAppsByUserIdWithEcho(userId);
        }
        return null;
    }


    public void deleteByUserId(Long userId) {
        if (userId != null) {
            this.entityDao.deleteByUserId(userId);
        }
    }


    public void batchInsert(List<SystemUserApp> userApps) {
        if (CollectionUtils.isNotEmpty(userApps)) {
            this.entityDao.batchInsert(userApps);
        }
    }
}
