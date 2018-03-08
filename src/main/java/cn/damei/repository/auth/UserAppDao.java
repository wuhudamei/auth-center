package cn.damei.repository.auth;

import cn.damei.common.persistence.CrudDao;
import cn.damei.entity.auth.App;
import cn.damei.entity.auth.UserApp;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserAppDao extends CrudDao<UserApp> {


    void batchInsert(@Param("userApps") List<UserApp> userApps);


    List<App> findAppByUserId(@Param("userId") Long userId);


    void deleteByIds(@Param("userId") Long userId, @Param("appIds") List<Long> appIds);


    void deleteUserApp(UserApp userApp);
}
