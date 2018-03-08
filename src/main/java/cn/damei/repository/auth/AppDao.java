package cn.damei.repository.auth;

import cn.damei.common.persistence.CrudDao;
import cn.damei.entity.auth.App;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AppDao extends CrudDao<App> {


    List<App> findAppWithRolesByUserIdAndAppIds(@Param("userId") Long userId, @Param("appIds") List<Long> appIds);


    App getByAppid(@Param("appid") String appid);


    App getByAppidAndSecret(@Param("appid") String appid, @Param("secret") String secret);
}