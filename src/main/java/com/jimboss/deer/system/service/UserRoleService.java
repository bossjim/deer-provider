package com.jimboss.deer.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jimboss.deer.system.domain.UserRole;

public interface UserRoleService extends IService<UserRole> {

    void deleteUserRolesByUserId(String[] userIds);
}
