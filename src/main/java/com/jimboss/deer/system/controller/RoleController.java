package com.jimboss.deer.system.controller;

import com.jimboss.deer.common.controller.BaseController;
import com.jimboss.deer.common.domain.QueryRequest;
import com.jimboss.deer.system.domain.Role;
import com.jimboss.deer.system.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName RoleController
 * @Description TODO
 * @Author jinyong
 * @DATE 2019/9/30 10:10
 * @Version 1.0
 **/

@Slf4j
@Validated
@RestController
@RequestMapping("role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    @RequiresPermissions("role:view")
    public Map<String, Object> roleList(QueryRequest queryRequest, Role role) {
        return getDataTable(roleService.findRoles(role, queryRequest));
    }
}
