package cn.damei.service.auth;

import cn.damei.common.service.CrudService;
import cn.damei.entity.auth.UserWechat;
import cn.damei.repository.auth.UserWechatDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;


@Service
public class UserWechatService extends CrudService<UserWechatDao, UserWechat> {


    public UserWechat findByOpenid(String openid) {
        if (StringUtils.isEmpty(openid)) {
            return null;
        }
        return this.entityDao.findByOpenid(openid);
    }



    public List<UserWechat> findByJobNum(String jobNum) {
        if (StringUtils.isNotEmpty(jobNum)) {
            List<String> jobNums = new ArrayList<>();
            jobNums.add(jobNum);
            return this.entityDao.findByJobNums(jobNums);
        }
        return Collections.EMPTY_LIST;
    }



    public List<UserWechat> findByOrgNum(String orgNum) {
        if (StringUtils.isNotEmpty(orgNum)) {
            List<String> orgNums = new ArrayList<>();
            orgNums.add(orgNum);
            return this.entityDao.findByOrgNums(orgNums);
        }
        return Collections.EMPTY_LIST;
    }



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
