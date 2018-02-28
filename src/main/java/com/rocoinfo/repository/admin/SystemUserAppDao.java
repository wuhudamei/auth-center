package com.rocoinfo.repository.admin;

import com.rocoinfo.common.persistence.CrudDao;
import com.rocoinfo.entity.admin.SystemUserApp;
import com.rocoinfo.entity.auth.App;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <dl>
 * <dd>Description: </dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/31 下午7:36</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@Repository
public interface SystemUserAppDao extends CrudDao<SystemUserApp> {

    /**
     * 根据用户id查询用户关联的应用信息
     *
     * @param userId 用户id
     * @return
     */
    List<App> findAppsByUserId(@Param("userId") Long userId);

    /**
     * 根据用户id查询用户关联的应用信息(带回显信息)
     *
     * @param userId 用户id
     * @return
     */
    List<App> findAppsByUserIdWithEcho(@Param("userId") Long userId);

    /**
     * 根据用户id删除用户应用关联关系
     *
     * @param userId 用户id
     */
    void deleteByUserId(@Param("userId") Long userId);

    /**
     * 批量插入用户应用关联关系
     *
     * @param userApps 用户应用关联关系
     */
    void batchInsert(@Param("userApps") List<SystemUserApp> userApps);
}
