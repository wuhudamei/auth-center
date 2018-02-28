package com.rocoinfo.service.auth;

import com.rocoinfo.common.service.CrudService;
import com.rocoinfo.entity.auth.UserWechat;
import com.rocoinfo.repository.auth.UserWechatDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * <dl>
 * <dd>Description: 功能描述</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/5 17:27</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
@Service
public class UserWechatService extends CrudService<UserWechatDao, UserWechat> {

    /**
     * 根据openid查询openid和用户的绑定关系
     *
     * @param openid openid
     * @return
     */
    public UserWechat findByOpenid(String openid) {
        if (StringUtils.isEmpty(openid)) {
            return null;
        }
        return this.entityDao.findByOpenid(openid);
    }


    /**
     * 根据jibNum查找用户绑定列表
     *
     * @param jobNum 用户jobNum
     * @return 返回绑定列表
     */
    public List<UserWechat> findByJobNum(String jobNum) {
        if (StringUtils.isNotEmpty(jobNum)) {
            List<String> jobNums = new ArrayList<>();
            jobNums.add(jobNum);
            return this.entityDao.findByJobNums(jobNums);
        }
        return Collections.EMPTY_LIST;
    }


    /**
     * 根据新定的员工号(org_code)查询微信绑定关系
     * @param orgNum 新版的工号
     * @return
     */
    public List<UserWechat> findByOrgNum(String orgNum) {
        if (StringUtils.isNotEmpty(orgNum)) {
            List<String> orgNums = new ArrayList<>();
            orgNums.add(orgNum);
            return this.entityDao.findByOrgNums(orgNums);
        }
        return Collections.EMPTY_LIST;
    }


    /**
     * 根据jobNums查询用户绑定的openId，并根据jobNum进行分组
     *
     * @param jobNums 用户工号列表
     * @return 返回用户微信绑定信息
     */
    public Map<String, Set<String>> findOpenidsByJobNums(List<String> jobNums) {

        if (CollectionUtils.isNotEmpty(jobNums)) {
            List<UserWechat> userWechats = this.entityDao.findByJobNums(jobNums);
            //过滤掉没有绑定的用户
            userWechats = userWechats.stream().filter(userWechat -> userWechat.getOpenid() != null).collect(toList());
            if (CollectionUtils.isEmpty(userWechats)) {
                return Collections.EMPTY_MAP;
            }

            //对绑定关系以用户jobNum进行分组
            Map<String, Set<String>> bindInfo = userWechats.stream()
                    .collect(groupingBy(UserWechat::getJobNum,
                            Collectors.mapping(UserWechat::getOpenid, toSet())));

            return bindInfo;
        }

        return Collections.EMPTY_MAP;
    }
}
