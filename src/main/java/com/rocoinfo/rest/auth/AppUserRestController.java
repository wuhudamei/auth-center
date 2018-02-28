package com.rocoinfo.rest.auth;

import com.rocoinfo.aop.logger.Logger;
import com.rocoinfo.common.persistence.CrudDao;
import com.rocoinfo.config.log.Type;
import com.rocoinfo.dto.StatusDto;
import com.rocoinfo.dto.page.PageTable;
import com.rocoinfo.dto.page.Pagination;
import com.rocoinfo.dto.page.Sort;
import com.rocoinfo.entity.auth.UserApp;
import com.rocoinfo.service.auth.UserAppService;
import com.rocoinfo.service.auth.UserService;
import com.rocoinfo.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <dl>
 * <dd>Description: 应用用户管理Rest Controller</dd>
 * <dd>Company: 大诚若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/21 14:18</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
@RestController
@RequestMapping(value = "/api/app/users")
public class AppUserRestController {

    @Autowired
    private UserAppService userAppService;

    @Autowired
    private UserService userService;

    /**
     * 查询指定应用下的用户列表
     *
     * @return 返回用户列表
     */
    @GetMapping()
    @Logger(module = Type.APP_USER)
    public StatusDto search(@RequestParam(value = "appId") Long appId,
                            @RequestParam(value= "roleId",required = false)  Integer roleId,
                            @RequestParam(value = "keyword") String keyword,
                            @RequestParam(defaultValue = "0") int offset,
                            @RequestParam(defaultValue = "20") int limit,
                            @RequestParam(defaultValue = "id") String sortName,
                            @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) {

        Map<String, Object> params = new HashMap<>();
        params.put("appId", appId);
        if(roleId != null){
            params.put("roleId", roleId);
        }
        MapUtils.putNotNull(params, CrudDao.KEYWORD, keyword);

        PageTable apps = userService.searchScrollPage(params, new Pagination(offset, limit, new Sort(new Sort.Order(sortOrder, sortName))));

        return StatusDto.buildSuccess(apps);
    }

    /**
     * 查询指定app没有绑定绑定的用户列表
     *
     * @return 返回用户列表
     */
    @GetMapping("/notBindUsers")
    @Logger(module = Type.APP_USER)
    public StatusDto findNotBindUsers(@RequestParam(value = "appId") Long appId,
                                      @RequestParam(value = "keyword") String keyword,
                                      @RequestParam(defaultValue = "0") int offset,
                                      @RequestParam(defaultValue = "20") int limit,
                                      @RequestParam(defaultValue = "id") String sortName,
                                      @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) {
        Map<String, Object> params = new HashMap<>();
        params.put("exclusionAppUser", appId);
        MapUtils.putNotNull(params, CrudDao.KEYWORD, keyword);

        PageTable apps = userService.searchScrollPage(params, new Pagination(offset, limit, new Sort(new Sort.Order(sortOrder, sortName))));

        return StatusDto.buildSuccess(apps);
    }

    /**
     * 批量绑定app用户
     *
     * @param userApps app用户绑定信息
     * @return 返回绑定信息
     */
    @PostMapping(value = "/addUsers")
    @Logger(module = Type.APP_USER)
    public StatusDto addUsers(@RequestBody List<UserApp> userApps) {
        userAppService.batchInsert(userApps);
        return StatusDto.buildSuccess();
    }

    /**
     * 解除绑定关系
     *
     * @param userId 用户id
     * @param appId  应用id
     * @return 返回解绑结果
     */
    @DeleteMapping("/{userId}/del")
    @Logger(module = Type.APP_USER)
    public StatusDto unbound(@PathVariable(value = "userId") Long userId,
                             @RequestParam(value = "appId") Long appId) {

        userAppService.unbound(userId, appId);

        return StatusDto.buildSuccess();
    }

}
