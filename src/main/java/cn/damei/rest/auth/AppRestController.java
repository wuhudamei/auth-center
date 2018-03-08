package cn.damei.rest.auth;

import cn.damei.common.BaseController;
import cn.damei.common.persistence.CrudDao;
import cn.damei.config.shiro.ShiroUser;
import cn.damei.dto.StatusDto;
import cn.damei.dto.page.PageTable;
import cn.damei.dto.page.Pagination;
import cn.damei.dto.page.Sort;
import cn.damei.entity.auth.App;
import cn.damei.service.auth.AppService;
import cn.damei.utils.MapUtils;
import cn.damei.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RequestMapping("/api/apps")
@RestController
public class AppRestController extends BaseController {

    @Autowired
    private AppService appService;

    @GetMapping
    public StatusDto search(@RequestParam(name = "keyword", required = false) String keyword,
                            @RequestParam(name = "offset", defaultValue = "0") int offset,
                            @RequestParam(name = "limit", defaultValue = "20") int limit,
                            @RequestParam(name = "sortName", defaultValue = "id") String sortName,
                            @RequestParam(name = "sortOrder", defaultValue = "DESC") Sort.Direction sortOrder) {
        Map<String, Object> params = new HashMap<>();
        MapUtils.putNotNull(params, CrudDao.KEYWORD, keyword);
        PageTable apps = appService.searchScrollPage(params, new Pagination(offset, limit, new Sort(new Sort.Order(sortOrder, sortName))));
        return StatusDto.buildSuccess(apps);
    }


    @GetMapping(value = "/{id}/get")
    public StatusDto getById(@PathVariable(value = "id") Long id) {
        App app = appService.getById(id);
        if (app != null) {
            return StatusDto.buildSuccess("查询应用信息成功！", app);
        } else {
            return StatusDto.buildFailure("没有查询到此应用信息！");
        }

    }



    @PostMapping
    public StatusDto CreateOrUpdate(@RequestBody @Validated App app) {
        ShiroUser logged = WebUtils.getLoggedUser();
        if (logged == null) {
            return StatusDto.buildFailure("请登录！");
        }
        appService.createOrUpdate(app, logged);
        return StatusDto.buildSuccess("操作成功！");
    }


    @DeleteMapping(value = "/{id}/del")
    public StatusDto delete(@PathVariable(value = "id") Long id) {
        appService.logicDelById(id);
        return StatusDto.buildSuccess("删除成功！");
    }


    @PutMapping("/changeStatus")
    public StatusDto changeStatus(@RequestParam(value = "id") Long id,
                                  @RequestParam(value = "status") App.Status status) {

        if (id != null && status != null) {
            appService.changeStatus(id, status);
            return StatusDto.buildSuccess("修改成功");
        }
        return StatusDto.buildFailure("没有查询到您要修改的应用信息！");
    }
}
