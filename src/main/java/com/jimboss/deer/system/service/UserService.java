package com.jimboss.deer.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jimboss.deer.system.domain.User;

public interface UserService extends IService<User> {

    /**
     * 通过用户名查找用户
     *
     * @param username username
     * @return user
     */
    User findByName(String username);

    /**
     * 更新用户登录时间
     *
     * @param username username
     */
    void updateLoginTime(String username) throws Exception;
}
