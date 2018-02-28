package com.rocoinfo.service.admin;

import com.rocoinfo.common.service.CrudService;
import com.rocoinfo.entity.admin.SystemUserApp;
import com.rocoinfo.entity.auth.App;
import com.rocoinfo.repository.admin.SystemUserAppDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <dl>
 * <dd>Description: </dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/31 下午7:39</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@Service
public class SystemUserAppService extends CrudService<SystemUserAppDao, SystemUserApp> {

    /**
     * 根据用户id查询用户关联的app
     *
     * @param userId 用户id
     * @return
     */
    public List<App> findAppsByUserId(Long userId) {
        if (userId != null) {
            return this.entityDao.findAppsByUserId(userId);
        }
        return null;
    }

    /**
     * 根据用户id查询用户关联的应用信息(带回显信息)
     *
     * @param userId 用户id
     * @return
     */
    public List<App> findAppsByUserIdWithEcho(Long userId) {
        if (userId != null) {
            return this.entityDao.findAppsByUserIdWithEcho(userId);
        }
        return null;
    }

    /**
     * 根据用户id删除用户应用关联关系
     *
     * @param userId 用户id
     */
    public void deleteByUserId(Long userId) {
        if (userId != null) {
            this.entityDao.deleteByUserId(userId);
        }
    }

    /**
     * 批量插入用户应用关联关系
     *
     * @param userApps 用户应用关联关系
     */
    public void batchInsert(List<SystemUserApp> userApps) {
        if (CollectionUtils.isNotEmpty(userApps)) {
            this.entityDao.batchInsert(userApps);
        }
    }
}
