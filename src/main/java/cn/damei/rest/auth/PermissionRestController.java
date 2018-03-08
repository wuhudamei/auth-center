package cn.damei.rest.auth;

import cn.damei.dto.StatusDto;
import cn.damei.entity.auth.Permission;
import cn.damei.dto.app.PermissionTreeDto;
import cn.damei.service.auth.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController(value = "appPermissionRestController")
@RequestMapping("/api/app/permissions")
public class PermissionRestController {

    @Autowired
    private PermissionService permissionService;


    @GetMapping
    public StatusDto search(@RequestParam(value = "appId") Long appId) {
        PermissionTreeDto permissionTreeDto = permissionService.findPermissionTree(appId);
        if (permissionTreeDto != null) {
            return StatusDto.buildSuccess(permissionTreeDto);
        }
        return StatusDto.buildFailure("没有查询该应用的权限信息,请联系管理员添加该应用权限根节点!");

    }



    @GetMapping("/{id}/get")
    public StatusDto getById(@PathVariable(value = "id") Long id) {
        return StatusDto.buildSuccess(permissionService.getById(id));
    }


    @DeleteMapping("/{id}/del")
    public StatusDto delete(@PathVariable(value = "id") Long id) {
        permissionService.deleteById(id);
        return StatusDto.buildSuccess("删除成功！");
    }


    @PostMapping
    public StatusDto createOrUpdate(@RequestBody @Validated Permission permission) {
        PermissionTreeDto treeDto = permissionService.createOrUpdate(permission);
        return StatusDto.buildSuccess("操作成功",treeDto);
    }
}
