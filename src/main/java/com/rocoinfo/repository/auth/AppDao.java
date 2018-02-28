package com.rocoinfo.repository.auth;

import com.rocoinfo.common.persistence.CrudDao;
import com.rocoinfo.entity.auth.App;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <dl>
 * <dd>Description: 应用Dao</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/31 上午10:50</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
@Repository
public interface AppDao extends CrudDao<App> {

    /**
     * 根据userid和appids查询包含角色信息的apps
     *
     * @param userId 用户id
     * @param appIds 应用ids，用来确定应用的范围
     * @return
     */
    List<App> findAppWithRolesByUserIdAndAppIds(@Param("userId") Long userId, @Param("appIds") List<Long> appIds);

    /**
     * 根据appid查询
     *
     * @param appid appid
     * @return
     */
    App getByAppid(@Param("appid") String appid);

    /**
     * 根据appid和secret查询
     *
     * @param appid  appid
     * @param secret 秘钥
     * @return
     */
    App getByAppidAndSecret(@Param("appid") String appid, @Param("secret") String secret);
}