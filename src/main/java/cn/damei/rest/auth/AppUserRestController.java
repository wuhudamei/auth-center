package cn.damei.rest.auth;

import cn.damei.dto.StatusDto;
import cn.damei.dto.page.Sort;
import cn.damei.service.auth.UserService;
import cn.damei.common.persistence.CrudDao;
import cn.damei.dto.page.PageTable;
import cn.damei.dto.page.Pagination;
import cn.damei.entity.auth.UserApp;
import cn.damei.service.auth.UserAppService;
import cn.damei.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/app/users")
public class AppUserRestController {

    @Autowired
    private UserAppService userAppService;

    @Autowired
    private UserService userService;


    @GetMapping()
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


    @GetMapping("/notBindUsers")
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


    @PostMapping(value = "/addUsers")
    public StatusDto addUsers(@RequestBody List<UserApp> userApps) {
        userAppService.batchInsert(userApps);
        return StatusDto.buildSuccess();
    }


    @DeleteMapping("/{userId}/del")
    public StatusDto unbound(@PathVariable(value = "userId") Long userId,
                             @RequestParam(value = "appId") Long appId) {

        userAppService.unbound(userId, appId);

        return StatusDto.buildSuccess();
    }

}
