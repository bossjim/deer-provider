package com.jimboss.deer.common.service.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jimboss.deer.common.domain.DeerConstant;
import com.jimboss.deer.common.service.CacheService;
import com.jimboss.deer.common.service.RedisService;
import com.jimboss.deer.system.dao.UserMapper;
import com.jimboss.deer.system.domain.Menu;
import com.jimboss.deer.system.domain.Role;
import com.jimboss.deer.system.domain.User;
import com.jimboss.deer.system.domain.UserConfig;
import com.jimboss.deer.system.service.UserConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName CacheServiceImpl
 * @Description TODO
 * @Author jinyong
 * @DATE 2019/7/22 17:08
 * @Version 1.0
 **/
@Service("cacheService")
public class CacheServiceImpl implements CacheService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserConfigService userConfigService;

    @Override
    public User getUser(String username) throws Exception {
        String userString = this.redisService.get(DeerConstant.USER_CACHE_PREFIX + username);
        if (StringUtils.isBlank(userString))
            throw new Exception();
        else
            return this.mapper.readValue(userString, User.class);
    }

    @Override
    public void saveUser(String username) throws Exception {
        User user = userMapper.findDetail(username);
        this.deleteUser(username);
        redisService.set(DeerConstant.USER_CACHE_PREFIX + username, mapper.writeValueAsString(user));
    }

    @Override
    public void deleteUser(String username) throws Exception {
        username = username.toLowerCase();
        redisService.del(DeerConstant.USER_CACHE_PREFIX + username);
    }

    @Override
    public List<Role> getRoles(String username) throws Exception {
        String roleListString = this.redisService.get(DeerConstant.USER_ROLE_CACHE_PREFIX + username);
        if (StringUtils.isBlank(roleListString)) {
            throw new Exception();
        } else {
            JavaType type = mapper.getTypeFactory().constructParametricType(List.class, Role.class);
            return this.mapper.readValue(roleListString, type);
        }
    }

    @Override
    public List<Menu> getPermissions(String username) throws Exception {
        String permissionListString = this.redisService.get(DeerConstant.USER_PERMISSION_CACHE_PREFIX + username);
        if (StringUtils.isBlank(permissionListString)) {
            throw new Exception();
        } else {
            JavaType type = mapper.getTypeFactory().constructParametricType(List.class, Menu.class);
            return this.mapper.readValue(permissionListString, type);
        }
    }

    @Override
    public UserConfig getUserConfig(String userId) throws Exception {
        String userConfigString = this.redisService.get(DeerConstant.USER_CONFIG_CACHE_PREFIX + userId);
        if (StringUtils.isBlank(userConfigString))
            throw new Exception();
        else
            return this.mapper.readValue(userConfigString, UserConfig.class);
    }

    @Override
    public void saveUserConfigs(String userId) throws Exception {
        UserConfig userConfig = this.userConfigService.findByUserId(userId);
        if (userConfig != null) {
            this.deleteUserConfigs(userId);
            redisService.set(DeerConstant.USER_CONFIG_CACHE_PREFIX + userId, mapper.writeValueAsString(userConfig));
        }
    }

    @Override
    public void deleteUserConfigs(String userId) throws Exception {
        redisService.del(DeerConstant.USER_CONFIG_CACHE_PREFIX + userId);
    }
}
