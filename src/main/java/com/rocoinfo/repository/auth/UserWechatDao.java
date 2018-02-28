package com.rocoinfo.repository.auth;

import com.rocoinfo.common.persistence.CrudDao;
import com.rocoinfo.entity.auth.UserWechat;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <dl>
 * <dd>Description: 用户绑定关系DAO</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/5 17:22</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
@Repository
public interface UserWechatDao extends CrudDao<UserWechat> {

    /**
     * 根据openid查询openid和用户的绑定关系
     *
     * @param openid openid
     * @return 返回绑定关系
     */
    UserWechat findByOpenid(@Param(value = "openid") String openid);

    /**
     * 根据userId查询绑定列表
     *
     * @param userId 用户id
     */
    List<UserWechat> findByUserId(@Param(value = "userId") Long userId);

    /**
     * 根据用户工号查询绑定关系
     *
     * @param jobNums 用户工号列表
     * @return 返回绑定关系列表
     */
    List<UserWechat> findByJobNums(@Param(value = "jobNums") List<String> jobNums);


    /**
     * 根据新定的员工号(org_code)查询微信绑定关系
     *
     * @param orgNums
     * @return
     */
    List<UserWechat> findByOrgNums(@Param(value = "orgNums") List<String> orgNums);
}
