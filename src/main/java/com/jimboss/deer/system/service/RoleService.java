package com.jimboss.deer.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jimboss.deer.common.domain.QueryRequest;
import com.jimboss.deer.system.domain.Role;

import java.util.List;

public interface RoleService extends IService<Role> {

    List<Role> findUserRole(String userName);

    IPage<Role> findRoles(Role role, QueryRequest request);
}
