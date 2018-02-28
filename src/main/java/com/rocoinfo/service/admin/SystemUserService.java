package com.rocoinfo.service.admin;

import com.rocoinfo.common.service.CrudService;
import com.rocoinfo.dto.StatusDto;
import com.rocoinfo.entity.admin.SystemUser;
import com.rocoinfo.entity.admin.SystemUserApp;
import com.rocoinfo.entity.auth.App;
import com.rocoinfo.enumeration.Status;
import com.rocoinfo.repository.admin.SystemUserDao;
import com.rocoinfo.utils.PasswordUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <dl>
 * <dd>Description: 管理员用户Service</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/31 下午1:57</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@Service
public class SystemUserService extends CrudService<SystemUserDao, SystemUser> {

    @Autowired
    private SystemUserAppService userAppService;

    /**
     * 默认密码
     */
    private static String DEFAULT_PASSWORD = "123456";

    @Transactional(rollbackFor = Exception.class)
    public StatusDto create(SystemUser user) {
        if (this.getByUsername(user.getUsername()) != null) {
            return StatusDto.buildFailure("用户名已存在！");
        }
        String salt = PasswordUtil.generateSalt();
        user.setSalt(salt);
        user.setPassword(PasswordUtil.entryptUserPassword(DEFAULT_PASSWORD, salt));
        user.setStatus(Status.OPEN);
        user.setDeleted(false);
        super.insert(user);
        return StatusDto.buildSuccess();
    }

    /**
     * 根据用户名查询管理员用户
     *
     * @param username 用户名
     * @return
     */
    public SystemUser getByUsername(String username) {
        if (StringUtils.isNotBlank(username)) {
            return this.entityDao.getByUsername(username);
        }
        return null;
    }

    /**
     * 更新管理员用户
     *
     * @param user 用户信息
     * @return
     */
    public StatusDto edit(SystemUser user) {
        SystemUser existUser = this.getByUsername(user.getUsername());
        if (existUser != null && !existUser.getId().equals(user.getId())) {
            return StatusDto.buildFailure("用户名已存在！");
        }
        super.update(user);
        return StatusDto.buildSuccess();
    }

    /**
     * 重置用户密码
     *
     * @param id 用户id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public StatusDto resetPwd(Long id) {
        if (id == null || this.getById(id) == null) {
            return StatusDto.buildFailure("用户id为null或查询不到此用户信息");
        }
        SystemUser user = new SystemUser(id);
        String salt = PasswordUtil.generateSalt();
        user.setSalt(salt);
        user.setPassword(PasswordUtil.entryptUserPassword(DEFAULT_PASSWORD, salt));
        int i = this.update(user);
        if (i > 0) {
            return StatusDto.buildSuccess();
        }
        return StatusDto.buildFailure();
    }

    /**
     * 根据用户名查询用户的所有信息（角色、权限）
     *
     * @param username 用户名
     * @return
     */
    public SystemUser getAllInfoByUsername(String username) {
        if (StringUtils.isNotBlank(username)) {
            return this.entityDao.getAllInfoByUsername(username);
        }
        return null;
    }

    /**
     * 根据用户id查询用户的所有信息（角色、权限）
     *
     * @param id 用户id
     * @return
     */
    public SystemUser getAllInfoById(Long id) {
        if (id != null) {
            return this.entityDao.getAllInfoById(id);
        }
        return null;
    }

    /**
     * 根据用户id查询用户应用信息
     *
     * @param id 用户id
     * @return
     */
    public List<App> getAppsByUserId(Long id) {
        if (id != null) {
            return this.userAppService.findAppsByUserIdWithEcho(id);
        }
        return null;
    }

    /**
     * 设置用户应用权限信息
     *
     * @param id     用户id
     * @param appIds 应用ids
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public StatusDto setAppsByUserId(Long id, List<Long> appIds) {
        if (id == null) {
            return StatusDto.buildFailure("用户id不能为null");
        }
        // 删除旧的关联关系
        this.userAppService.deleteByUserId(id);
        // 建立新的关联关系
        if (CollectionUtils.isNotEmpty(appIds)) {
            List<SystemUserApp> userApps = appIds.stream().map((o) -> new SystemUserApp(id, o)).collect(Collectors.toList());
            this.userAppService.batchInsert(userApps);
        }
        return StatusDto.buildSuccess();
    }

    /**
     * 修改密码
     *
     * @param id     用户id
     * @param oldPwd 原密码
     * @param newPwd 新密码
     * @return
     */
    public StatusDto modifyPwd(Long id, String oldPwd, String newPwd) {
        SystemUser user = this.entityDao.getById(id);
        try {
            if (user.getPassword().equals(PasswordUtil.hashPassword(oldPwd, user.getSalt()))) {
                SystemUser u = new SystemUser(id);
                String salt = PasswordUtil.generateSalt();
                u.setSalt(salt);
                u.setPassword(PasswordUtil.entryptUserPassword(newPwd, salt));
                this.entityDao.update(u);
                return StatusDto.buildSuccess("修改密码成功！");
            } else {
                return StatusDto.buildFailure("原密码输入错误！");
            }
        } catch (Exception e) {
            return StatusDto.buildFailure("修改密码失败！");
        }
    }
}
