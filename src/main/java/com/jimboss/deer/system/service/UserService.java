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

    /**
     * 更新个人信息
     *
     * @param user 个人信息
     */
    void updateProfile(User user) throws Exception;

    /**
     * 更新用户密码
     *
     * @param username 用户名
     * @param password 新密码
     */
    void updatePassword(String username, String password) throws Exception;

    /**
     * 更新用户头像
     *
     * @param username 用户名
     * @param avatar   用户头像
     */
    void updateAvatar(String username, String avatar) throws Exception;
}
