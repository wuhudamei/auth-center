package cn.damei.repository.auth;

import cn.damei.common.persistence.CrudDao;
import cn.damei.entity.auth.UserWechat;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserWechatDao extends CrudDao<UserWechat> {


    UserWechat findByOpenid(@Param(value = "openid") String openid);


    List<UserWechat> findByUserId(@Param(value = "userId") Long userId);


    List<UserWechat> findByJobNums(@Param(value = "jobNums") List<String> jobNums);



    List<UserWechat> findByOrgNums(@Param(value = "orgNums") List<String> orgNums);
}
