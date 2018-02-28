package com.rocoinfo.service.auth;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.rocoinfo.Constants;
import com.rocoinfo.common.service.CrudService;
import com.rocoinfo.dto.StatusDto;
import com.rocoinfo.entity.auth.*;
import com.rocoinfo.enumeration.Status;
import com.rocoinfo.repository.auth.AppDao;
import com.rocoinfo.repository.auth.RoleDao;
import com.rocoinfo.repository.auth.UserDao;
import com.rocoinfo.service.admin.SystemUserAppService;
import com.rocoinfo.utils.PasswordUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <dl>
 * <dd>Description: 用户管理Service</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/1 上午10:32</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@Service
public class UserService extends CrudService<UserDao, User> {

    @Autowired
    private UserAppService userAppService;
    @Autowired
    private SystemUserAppService systemUserAppService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private AppService appService;
    @Autowired
    private AppDao appDao;
    @Autowired
    private RoleDao roleDao;

    /**
     * 新建用户
     *
     * @param user 用户信息
     */
    @Transactional(rollbackFor = Exception.class)
    public StatusDto create(User user) {
        // 检查用户名是否重复
        if (this.getByUsername(user.getUsername()) != null) {
            return StatusDto.buildFailure("用户名已存在！");
        }

        // 设置密码
        String salt = PasswordUtil.generateSalt();
        user.setSalt(salt);
        user.setPassword(PasswordUtil.entryptUserPassword(Constants.DEFAULT_PASSWORD, salt));
        user.setStatus(Status.OPEN);
        user.setDeleted(Boolean.FALSE);

        // 插入用户
        int i = this.insert(user);
        if (i < 0) {
            return StatusDto.buildFailure();
        }

        // 建立用户应用关联关系
        this.buildUserAppRelation(user.getId(), user.getAppIds());
        return StatusDto.buildSuccess();
    }

    /**
     * 根据id查询用户信息
     *
     * @param id 用户id
     * @return
     */
    @Override
    public User getById(Long id) {
        User user = super.getById(id);
        if (user == null) {
            return null;
        }
        // 查询用户关联的应用信息
        List<App> apps = this.userAppService.findAppByUserId(id);
        user.setApps(apps);
        return user;
    }

    /**
     * 编辑用户
     *
     * @param user         用户信息
     * @param loggedUserId 当前登录用户id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public StatusDto edit(User user, Long loggedUserId) {
        User existUser = this.getByUsername(user.getUsername());
        if (existUser != null && !existUser.getId().equals(user.getId())) {
            return StatusDto.buildFailure("用户名已存在！");
        }

        // 更新用户信息
        int i = this.update(user);
        if (i < 0) {
            return StatusDto.buildFailure();
        }

        // 编辑用户应用关联关系
        this.editUserAppRelation(user.getId(), user.getAppIds(), loggedUserId);
        return StatusDto.buildSuccess();
    }

    /**
     * 重置用户密码
     *
     * @param id 用户id
     * @return
     */
    public boolean resetPwd(Long id) {
        if (id == null) {
            return false;
        }
        User user = new User();
        user.setId(id);
        String salt = PasswordUtil.generateSalt();
        user.setSalt(salt);
        user.setPassword(PasswordUtil.entryptUserPassword(Constants.DEFAULT_PASSWORD, salt));
        // 更新信息
        int i = this.update(user);
        return i > 0;
    }

    /**
     * 编辑用户应用关联关系
     *
     * @param userId       用户id
     * @param selectAppIds 用户选择appids
     * @param loggedUserId 当前登录管理员用户id
     */
    @Transactional(rollbackFor = Exception.class)
    public void editUserAppRelation(Long userId, List<Long> selectAppIds, Long loggedUserId) {
        // 获取当前登录用户的app权限
        List<App> apps = this.systemUserAppService.findAppsByUserId(loggedUserId);
        if (CollectionUtils.isNotEmpty(apps)) { // 如果用户app权限不为空，才执行下述操作
            // 删除旧的用户应用关联关系 （权限范围内）
            List<Long> appIds = apps.stream().map(App::getId).collect(Collectors.toList());
            this.userAppService.deleteByIds(userId, appIds);
            // 建立新的用户应用关联关系
            // 过滤掉越权数据
            if (CollectionUtils.isNotEmpty(selectAppIds)) {
                List<Long> filteredIds = selectAppIds.stream().filter((o) -> appIds.contains(o)).collect(Collectors.toList());
                // 插入数据
                this.buildUserAppRelation(userId, filteredIds);
            }
        }
    }

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return
     */
    public User getByUsername(String username) {
        if (StringUtils.isNotBlank(username)) {
            return this.entityDao.getByUsername(username);
        }
        return null;
    }

    /**
     * 建立用户与应用的关联关系
     *
     * @param userId 用户id
     * @param appIds 应用ids
     */
    private void buildUserAppRelation(Long userId, List<Long> appIds) {
        if (userId == null || CollectionUtils.isEmpty(appIds)) {
            return;
        }
        List<UserApp> userApps = appIds.stream().map((appId) -> new UserApp(userId, appId)).collect(Collectors.toList());
        this.userAppService.batchInsert(userApps);
    }

    /**
     * 查询用户app权限的所有角色
     *
     * @param userId       用户id
     * @param loggedUserId 当前登录管理员用户id
     * @return
     */
    public List<App> getUserRoles(Long userId, Long loggedUserId) {
        // 取当前登录的管理员app权限  和 用户app权限的交集
        List<App> apps = this.getIntersectionOfApps(userId, loggedUserId);
        if (CollectionUtils.isNotEmpty(apps)) {
            List<Long> appIds = apps.stream().map(App::getId).collect(Collectors.toList());
            // 根据userid和appids查询包含角色信息的apps
            return this.appService.findAppWithRolesByUserIdAndAppIds(userId, appIds);
        }
        return null;
    }

    /**
     * 取当前登录的管理员app权限  和 用户app权限的交集
     *
     * @param userId       用户id
     * @param loggedUserId 当前登录的管理员用户id
     * @return
     */
    private List<App> getIntersectionOfApps(Long userId, Long loggedUserId) {
        // 获取当前登录用户的app权限
        List<App> loginUserApps = this.systemUserAppService.findAppsByUserId(loggedUserId);
        if (CollectionUtils.isNotEmpty(loginUserApps)) {
            // 获取当前用户的app权限
            List<App> userApps = this.userAppService.findAppByUserId(userId);
            if (CollectionUtils.isNotEmpty(userApps)) {
                // 当前用户的app权限的ids
                List<Long> userAppIds = userApps.stream().map(App::getId).collect(Collectors.toList());
                // 取交集
                return loginUserApps.stream().filter((o) -> userAppIds.contains(o.getId())).collect(Collectors.toList());
            }

        }

        return null;
    }

    /**
     * 为用户设置角色
     *
     * @param userId       用户id
     * @param roleIds      角色ids
     * @param loggedUserId 当前登录用户id
     * @return
     */
    public StatusDto setUserRoles(Long userId, List<Long> roleIds, Long loggedUserId) {
        // 取当前登录的管理员app权限  和 用户app权限的交集
        List<App> apps = this.getIntersectionOfApps(userId, loggedUserId);
        if (CollectionUtils.isEmpty(apps)) {
            return StatusDto.buildFailure("管理员或用户没有指定应用的权限");
        }
        List<Long> appIds = apps.stream().map(App::getId).collect(Collectors.toList());
        // 删除原有的用户角色关联关系(需要限制app的范围，不能全删)
        this.userRoleService.deleteByUserIdAndAppIds(userId, appIds);
        // 建立新的用户角色关联关系
        List<UserRole> userRoles = roleIds.stream().map((roleId) -> new UserRole(userId, roleId)).collect(Collectors.toList());
        this.userRoleService.batchInsert(userRoles);
        return StatusDto.buildSuccess();
    }

    /**
     * 根据用户名查询用户的所有信息（包括权限、角色）
     *
     * @param username 用户名
     * @return
     */
    public User getAllInfoByUsername(String username) {
        if (StringUtils.isNotEmpty(username)) {
            return this.entityDao.findAllInfoByUsername(username);
        }
        return null;
    }

    /**
     * * 根据用户名和应用id查询用户的所有信息（包括权限、角色）;
     * sql中有逻辑,请注意:
     * 并查询oa_employee_org表,将对应的公司且是门店标记的组织机构编码返回 store_code
     *  如果其直属和兼职都是门店,那么就将其,号拼接起来;并且按照type排序
     *  ( DIRECTLY直属靠前,兼职PART_TIME靠后)
     *
     * @param username 用户名
     * @param appId    应用主键
     * @return
     */
    public User getAllInfoByUsernameAndAppId(String username, Long appId) {
        if (StringUtils.isNotEmpty(username) && appId != null) {
            User user = this.entityDao.getAllInfoByUsernameAndAppId(username, appId);

            //主职机构是否是mr能否是
            Integer ifStore = 0;

            //确定机构相关的信息
            List<Map<String,Object>> allOrgList = entityDao.selectUserAllOrganization(user.getId().intValue());
            if(allOrgList != null && allOrgList.size() > 0){
                //指定所有门店门店编号(主职和兼职,storeCode)
                String storeCodeStr = "";
                List<String> storeCodeList = allOrgList.stream().filter(org -> "true".equals(org.get("ifStore").toString()) &&
                        "DIRECTLY".equals(org.get("type").toString())).map(org->org.get("storeCode").toString())
                        .collect(Collectors.toList());
                //——之所以单独处理主职门店是为了保证主职门店一定在最前
                if(storeCodeList != null && storeCodeList.size()>0){
                    storeCodeStr = storeCodeList.get(0);
                    ifStore = 1;
                }
                user.setIfStoreFlag(ifStore);

                //——查询多个兼职门店的编号
                //因为对接 单点拿门店code的很多系统取值方法不对，为了适应他们，不返回空字符串的门店
                String parttimeStoreCodes = StringUtils.join(allOrgList.stream().filter(org -> "true".equals(org.get("ifStore").toString()) &&
                        "PART_TIME".equals(org.get("type").toString()) && StringUtils.isNotBlank(org.get("storeCode").toString()))
                        .map(org->org.get("storeCode").toString()).collect(Collectors.toList()),",");
                if(StringUtils.isNotBlank(parttimeStoreCodes)){
                    if(StringUtils.isBlank(storeCodeStr)){
                        storeCodeStr = parttimeStoreCodes;
                    }
                    else {
                        storeCodeStr = storeCodeStr + "," + parttimeStoreCodes;
                    }
                }
                user.setStoreCode(storeCodeStr);

                //指定主部门编号（depCode）
                String depCode = "";
                List<String> depCodeList = allOrgList.stream().filter(
                        org -> "DIRECTLY".equals(org.get("type").toString())).map(org->org.get("depCode").toString()
                ).collect(Collectors.toList());
                if(depCodeList != null && depCodeList.size()>0){
                    depCode = depCodeList.get(0);
                }
                user.setDepCode(depCode);
            }
            return user;

        }
        return null;
    }

    /**
     * 用户更改密码
     *
     * @param jobNum    员工号
     * @param originPwd 旧密码
     * @param newPwd    新密码
     * @return
     */
    public StatusDto changePassword(String jobNum, String originPwd, String newPwd) {
        User user = this.getByUsername(jobNum);
        if (user == null) {
            return StatusDto.buildFailure("查询不到此用户");
        }

        String password = PasswordUtil.entryptUserPassword(originPwd, user.getSalt());
        if (!password.equals(user.getPassword())) {
            return StatusDto.buildFailure("原密码输入错误");
        }

        User newUser = new User(user.getId());
        String salt = PasswordUtil.generateSalt();
        newUser.setSalt(salt);
        newUser.setPassword(PasswordUtil.hashPassword(newPwd, salt));

        this.update(newUser);
        return StatusDto.buildSuccess();
    }

    /**
     * 根据应用的id查询该应用下的所有用户
     *
     * @param params 其中包含appId和roleId
     * @return
     */
    public List<User> findUsersFromAppid(Map<String,Object> params) {
        Long appId = Long.parseLong(params.get("appId").toString());
        if (appId == null) {
            return Lists.newArrayListWithCapacity(0);
        }
        return this.entityDao.findUsersFromAppid(params);
    }


    /**
     * 根据应用和角色将员工组，并持久化员工信息和分组信息
     * @return
     */
    public int groupUserByAppAndRole(){
        List<Map<String,Object>> allUserInfoList = new ArrayList<>();

        //查询所有没删除的应用
        List<App> allAppList = appDao.search(null);

        for(App app : allAppList){
            //查询某个应用下所有启用的角色
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("appId",app.getId());
            List<Role> roleListInApp = roleDao.search(paramMap);

            //查询某个应用用户分组的用户信息List
            for (Role role : roleListInApp){
                Map<String,Object> params = new HashMap<String,Object>();
                params.put("appId",app.getId());
                params.put("roleId",role.getId());
                params.put("appName",app.getName());
                params.put("roleName",role.getName());
                List<Map<String,Object>> partUserList = entityDao.selectUserByAppAndRole(params);
                allUserInfoList.addAll(partUserList);
            }
        }

        if(allUserInfoList.size()>0){
            //先清楚以前的数据
            entityDao.deleteAllUserInfoWithGroup();

            //持久化带有分组信息的用户List
            entityDao.insertUserInfoWithGroup(allUserInfoList);
        }



        return allUserInfoList.size();
    }
}