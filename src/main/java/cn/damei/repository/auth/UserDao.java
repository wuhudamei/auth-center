package cn.damei.repository.auth;

import cn.damei.common.persistence.CrudDao;
import cn.damei.entity.auth.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface UserDao extends CrudDao<User> {


    User getByUsername(@Param("username") String username);


    User findAllInfoByUsername(@Param(value = "username") String username);


    User getAllInfoByUsernameAndAppId(@Param("username") String username, @Param("appId") Long appId);




    List<User> findUsersFromAppid(Map<String,Object> params);



    List<Map<String,Object>> selectUserByAppAndRole(Map<String,Object> params);



    void insertUserInfoWithGroup(@Param("userInfos") List<Map<String,Object>> userInfoList);



    void deleteAllUserInfoWithGroup();



    List<Map<String,Object>> selectUserAllOrganization(int userId);
}