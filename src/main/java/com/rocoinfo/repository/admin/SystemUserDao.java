package com.rocoinfo.repository.admin;

import com.rocoinfo.common.persistence.CrudDao;
import com.rocoinfo.entity.admin.SystemUser;
import org.springframework.stereotype.Repository;

/**
 * <dl>
 * <dd>Description: 管理员用户Dao</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/31 下午1:56</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@Repository
public interface SystemUserDao extends CrudDao<SystemUser> {
    /**
     * 根据用户名查询用户的所有信息（角色、权限）
     *
     * @param username 用户名
     * @return
     */
    SystemUser getAllInfoByUsername(String username);

    /**
     * 根据用户id查询用户的所有信息（角色、权限）
     *
     * @param id 用户id
     * @return
     */
    SystemUser getAllInfoById(Long id);

    /**
     * 根据用户名查询管理员用户
     *
     * @param username 用户名
     * @return
     */
    SystemUser getByUsername(String username);
}
