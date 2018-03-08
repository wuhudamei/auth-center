package cn.damei.repository.admin;

import cn.damei.entity.admin.SystemUserApp;
import cn.damei.common.persistence.CrudDao;
import cn.damei.entity.auth.App;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SystemUserAppDao extends CrudDao<SystemUserApp> {


    List<App> findAppsByUserId(@Param("userId") Long userId);


    List<App> findAppsByUserIdWithEcho(@Param("userId") Long userId);


    void deleteByUserId(@Param("userId") Long userId);


    void batchInsert(@Param("userApps") List<SystemUserApp> userApps);
}
