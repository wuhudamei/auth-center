package com.rocoinfo.repository.auth;

import com.rocoinfo.common.persistence.CrudDao;
import com.rocoinfo.entity.auth.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <dl>
 * <dd>Description: 用户管理Dao</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/1 上午10:32</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@Repository
public interface UserDao extends CrudDao<User> {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return
     */
    User getByUsername(@Param("username") String username);

    /**
     * 通过用户名获取用户信息，角色信息，权限信息等
     *
     * @param username 用户名
     * @return 返回用户详情信息
     */
    User findAllInfoByUsername(@Param(value = "username") String username);

    /**
     * 根据用户名和应用id查询用户的所有信息（包括权限、角色）;
     * sql中有逻辑,请注意:
     * 并查询oa_employee_org表,将对应的公司且是门店标记的组织机构编码返回 store_code
     *  如果其直属和兼职都是门店,那么就将其,号拼接起来;并且按照type排序
     *  ( DIRECTLY直属靠前,兼职PART_TIME靠后)
     * @param username 用户名
     * @param appId    应用主键
     * @return
     */
    User getAllInfoByUsernameAndAppId(@Param("username") String username, @Param("appId") Long appId);



    /**
     * 根据应用的id查询该应用下的所有用户
     *
     * @param params 参数集合，有appId和roleId
     * @return
     */
    List<User> findUsersFromAppid(Map<String,Object> params);


    /**
     * 查询某个应用的某个角色下的员工信息
     * @return
     */
    List<Map<String,Object>> selectUserByAppAndRole(Map<String,Object> params);


    /**
     * 持久化带有分组信息的用户List
     * @param userInfoList
     */
    void insertUserInfoWithGroup(@Param("userInfos") List<Map<String,Object>> userInfoList);


    /**
     * 删除原来的老数据
     */
    void deleteAllUserInfoWithGroup();


    /**
     * 查询某个用户的所有关联的机构List
     * @param userId 用户id
     * @return
     */
    List<Map<String,Object>> selectUserAllOrganization(int userId);
}