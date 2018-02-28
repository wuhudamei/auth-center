package com.rocoinfo.service.auth;

import com.google.common.collect.Lists;
import com.rocoinfo.common.service.CrudService;
import com.rocoinfo.entity.auth.App;
import com.rocoinfo.entity.auth.UserApp;
import com.rocoinfo.repository.auth.UserAppDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <dl>
 * <dd>Description: </dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/1 下午2:03</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@Service
public class UserAppService extends CrudService<UserAppDao, UserApp> {

    /**
     * 批量插入
     *
     * @param userApps 用户应用关联
     */
    public void batchInsert(List<UserApp> userApps) {
        if (CollectionUtils.isNotEmpty(userApps)) {
            this.entityDao.batchInsert(userApps);
        }
    }

    /**
     * 根据用户id查询用户绑定的应用
     *
     * @param userId 用户id
     * @return
     */
    public List<App> findAppByUserId(Long userId) {
        if (userId != null) {
            return this.entityDao.findAppByUserId(userId);
        }
        return Lists.newArrayListWithCapacity(0);
    }

    /**
     * 根据id集合删除用户应用关联数据
     *
     * @param userId 用户id
     * @param appIds 应用ids
     */
    public void deleteByIds(Long userId, List<Long> appIds) {
        if (userId != null && CollectionUtils.isNotEmpty(appIds)) {
            this.entityDao.deleteByIds(userId, appIds);
        }

    }

    /**
     * 解除用户与app的绑定关系
     * @param userId 用户id
     * @param appId 应用id
     */
    public void unbound(Long userId, Long appId) {
        if(userId != null && appId != null){
            this.entityDao.deleteUserApp(new UserApp(userId,appId));
        }
    }
}
