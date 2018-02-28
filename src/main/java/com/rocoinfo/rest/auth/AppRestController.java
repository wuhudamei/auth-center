package com.rocoinfo.rest.auth;

import com.rocoinfo.aop.logger.Logger;
import com.rocoinfo.common.BaseController;
import com.rocoinfo.common.persistence.CrudDao;
import com.rocoinfo.config.log.Type;
import com.rocoinfo.config.shiro.ShiroUser;
import com.rocoinfo.dto.StatusDto;
import com.rocoinfo.dto.page.PageTable;
import com.rocoinfo.dto.page.Pagination;
import com.rocoinfo.dto.page.Sort;
import com.rocoinfo.entity.auth.App;
import com.rocoinfo.service.auth.AppService;
import com.rocoinfo.utils.MapUtils;
import com.rocoinfo.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <dl>
 * <dd>Description: 应用Controller</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/31 10:52</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
@RequestMapping("/api/apps")
@RestController
public class AppRestController extends BaseController {

    @Autowired
    private AppService appService;

    @GetMapping
    @Logger(module = Type.APP)
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

    /**
     * 根据id获取应用名称
     *
     * @param id 应用id
     * @return 返回应用信息
     */
    @GetMapping(value = "/{id}/get")
    @Logger(module = Type.APP)
    public StatusDto getById(@PathVariable(value = "id") Long id) {
        App app = appService.getById(id);
        if (app != null) {
            return StatusDto.buildSuccess("查询应用信息成功！", app);
        } else {
            return StatusDto.buildFailure("没有查询到此应用信息！");
        }

    }


    /**
     * 创建更新应用信息
     *
     * @param app 应用信息
     * @return 返回操作结果
     */
    @PostMapping
    @Logger(module = Type.APP)
    public StatusDto CreateOrUpdate(@RequestBody @Validated App app) {
        ShiroUser logged = WebUtils.getLoggedUser();
        if (logged == null) {
            return StatusDto.buildFailure("请登录！");
        }
        appService.createOrUpdate(app, logged);
        return StatusDto.buildSuccess("操作成功！");
    }

    /**
     * 删除应用
     *
     * @param id 应用id
     * @return 返回删除结果
     */
    @DeleteMapping(value = "/{id}/del")
    @Logger(module = Type.APP)
    public StatusDto delete(@PathVariable(value = "id") Long id) {
        appService.logicDelById(id);
        return StatusDto.buildSuccess("删除成功！");
    }

    /**
     * 修改状态
     *
     * @param id     应用id
     * @param status 应用状态
     * @return 返回结果
     */
    @PutMapping("/changeStatus")
    @Logger(module = Type.APP)
    public StatusDto changeStatus(@RequestParam(value = "id") Long id,
                                  @RequestParam(value = "status") App.Status status) {

        if (id != null && status != null) {
            appService.changeStatus(id, status);
            return StatusDto.buildSuccess("修改成功");
        }
        return StatusDto.buildFailure("没有查询到您要修改的应用信息！");
    }
}
