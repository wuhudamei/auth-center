package com.rocoinfo.repository.auth;

import com.rocoinfo.common.persistence.CrudDao;
import com.rocoinfo.entity.auth.App;
import com.rocoinfo.entity.auth.UserApp;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <dl>
 * <dd>Description: </dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/1 下午2:03</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@Repository
public interface UserAppDao extends CrudDao<UserApp> {

    /**
     * 批量插入
     *
     * @param userApps 用户应用关联
     */
    void batchInsert(@Param("userApps") List<UserApp> userApps);

    /**
     * 根据用户id查询用户绑定的应用
     *
     * @param userId 用户id
     * @return
     */
    List<App> findAppByUserId(@Param("userId") Long userId);

    /**
     * 根据id集合删除用户应用关联数据
     *
     * @param userId 用户id
     * @param appIds 应用ids
     */
    void deleteByIds(@Param("userId") Long userId, @Param("appIds") List<Long> appIds);

    /**
     * 删除用户app绑定关系
     * @param userApp 用户app信息
     */
    void deleteUserApp(UserApp userApp);
}
