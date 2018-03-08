package cn.damei.service.auth;

import cn.damei.common.service.CrudService;
import cn.damei.entity.auth.UserApp;
import com.google.common.collect.Lists;
import cn.damei.entity.auth.App;
import cn.damei.repository.auth.UserAppDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserAppService extends CrudService<UserAppDao, UserApp> {


    public void batchInsert(List<UserApp> userApps) {
        if (CollectionUtils.isNotEmpty(userApps)) {
            this.entityDao.batchInsert(userApps);
        }
    }


    public List<App> findAppByUserId(Long userId) {
        if (userId != null) {
            return this.entityDao.findAppByUserId(userId);
        }
        return Lists.newArrayListWithCapacity(0);
    }


    public void deleteByIds(Long userId, List<Long> appIds) {
        if (userId != null && CollectionUtils.isNotEmpty(appIds)) {
            this.entityDao.deleteByIds(userId, appIds);
        }

    }


    public void unbound(Long userId, Long appId) {
        if(userId != null && appId != null){
            this.entityDao.deleteUserApp(new UserApp(userId,appId));
        }
    }
}
