package cn.damei.repository.admin;

import cn.damei.common.persistence.CrudDao;
import cn.damei.entity.admin.SystemUser;
import org.springframework.stereotype.Repository;


@Repository
public interface SystemUserDao extends CrudDao<SystemUser> {

    SystemUser getAllInfoByUsername(String username);


    SystemUser getAllInfoById(Long id);


    SystemUser getByUsername(String username);
}
