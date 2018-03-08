package cn.damei.service.admin;

import cn.damei.common.service.CrudService;
import cn.damei.dto.StatusDto;
import cn.damei.entity.admin.SystemUser;
import cn.damei.entity.admin.SystemUserApp;
import cn.damei.enumeration.Status;
import cn.damei.entity.auth.App;
import cn.damei.repository.admin.SystemUserDao;
import cn.damei.utils.PasswordUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SystemUserService extends CrudService<SystemUserDao, SystemUser> {

    @Autowired
    private SystemUserAppService userAppService;


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


    public SystemUser getByUsername(String username) {
        if (StringUtils.isNotBlank(username)) {
            return this.entityDao.getByUsername(username);
        }
        return null;
    }


    public StatusDto edit(SystemUser user) {
        SystemUser existUser = this.getByUsername(user.getUsername());
        if (existUser != null && !existUser.getId().equals(user.getId())) {
            return StatusDto.buildFailure("用户名已存在！");
        }
        super.update(user);
        return StatusDto.buildSuccess();
    }


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


    public SystemUser getAllInfoByUsername(String username) {
        if (StringUtils.isNotBlank(username)) {
            return this.entityDao.getAllInfoByUsername(username);
        }
        return null;
    }


    public SystemUser getAllInfoById(Long id) {
        if (id != null) {
            return this.entityDao.getAllInfoById(id);
        }
        return null;
    }


    public List<App> getAppsByUserId(Long id) {
        if (id != null) {
            return this.userAppService.findAppsByUserIdWithEcho(id);
        }
        return null;
    }


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
