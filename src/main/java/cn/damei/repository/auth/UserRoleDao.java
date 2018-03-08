package cn.damei.repository.auth;

import cn.damei.common.persistence.CrudDao;
import cn.damei.entity.auth.UserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRoleDao extends CrudDao<UserRole> {


    void batchInsert(@Param("userRoles") List<UserRole> userRoles);


    void deleteByUserIdAndAppIds(@Param("userId") Long userId, @Param("appIds") List<Long> appIds);
}
