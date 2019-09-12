package com.jimboss.deer.system.controller;

import com.jimboss.deer.common.controller.BaseController;
import com.jimboss.deer.common.domain.QueryRequest;
import com.jimboss.deer.system.domain.Dept;
import com.jimboss.deer.system.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName DeptController
 * @Description TODO
 * @Author jinyong
 * @DATE 2019/9/9 10:24
 * @Version 1.0
 **/
@Slf4j
@Validated
@RestController
@RequestMapping("dept")
public class DeptController extends BaseController {

    @Autowired
    private DeptService deptService;

    @GetMapping
    public Map<String, Object> deptList(QueryRequest request, Dept dept) {
        return this.deptService.findDepts(request, dept);
    }
}
