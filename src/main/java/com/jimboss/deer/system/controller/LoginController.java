package com.jimboss.deer.system.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jimboss.deer.common.authentication.JWTToken;
import com.jimboss.deer.common.domain.ActiveUser;
import com.jimboss.deer.common.domain.DeerConstant;
import com.jimboss.deer.common.domain.DeerResponse;
import com.jimboss.deer.common.exception.DeerException;
import com.jimboss.deer.common.properties.DeerProperties;
import com.jimboss.deer.common.service.RedisService;
import com.jimboss.deer.common.utils.*;
import com.jimboss.deer.system.dao.LoginLogMapper;
import com.jimboss.deer.system.domain.LoginLog;
import com.jimboss.deer.system.domain.User;
import com.jimboss.deer.system.domain.UserConfig;
import com.jimboss.deer.system.manager.UserManager;
import com.jimboss.deer.system.service.LoginLogService;
import com.jimboss.deer.system.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.lionsoul.ip2region.DbSearcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName LoginController
 * @Description TODO
 * @Author jinyong
 * @DATE 2019/7/22 16:42
 * @Version 1.0
 **/
@Validated
@RestController
public class LoginController {

    @Autowired
    private UserManager userManager;
    @Autowired
    private UserService userService;
    @Autowired
    private LoginLogService loginLogService;
    @Autowired
    private DeerProperties properties;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private LoginLogMapper loginLogMapper;

    @PostMapping("/login")
    public DeerResponse login(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password, HttpServletRequest request) throws Exception {
        username = StringUtils.lowerCase(username);
        password = MD5Util.encrypt(username, password);

        final String errorMessage = "用户名或密码错误";
        User user = this.userManager.getUser(username);

        if (user == null)
            throw new DeerException(errorMessage);
        if (!StringUtils.equals(user.getPassword(), password))
            throw new DeerException(errorMessage);
        if (User.STATUS_LOCK.equals(user.getStatus()))
            throw new DeerException("账号已被锁定,请联系管理员！");

        // 更新用户登录时间
        this.userService.updateLoginTime(username);
        // 保存登录记录
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        this.loginLogService.saveLoginLog(loginLog);

        String token = DeerUtil.encryptToken(JWTUtil.sign(username, password));
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(properties.getShiro().getJwtTimeOut());
        String expireTimeStr = DateUtil.formatFullTime(expireTime);
        JWTToken jwtToken = new JWTToken(token, expireTimeStr);

        String userId = this.saveTokenToRedis(user, jwtToken, request);
        user.setId(userId);

        Map<String, Object> userInfo = this.generateUserInfo(jwtToken, user);
        return new DeerResponse().message("认证成功").data(userInfo);
    }

    private String saveTokenToRedis(User user, JWTToken token, HttpServletRequest request) throws Exception {
        String ip = IPUtil.getIpAddr(request);

        // 构建在线用户
        ActiveUser activeUser = new ActiveUser();
        activeUser.setUsername(user.getUsername());
        activeUser.setIp(ip);
        activeUser.setToken(token.getToken());
        activeUser.setLoginAddress(AddressUtil.getCityInfo(DbSearcher.BTREE_ALGORITHM, ip));

        // zset 存储登录用户，score 为过期时间戳
        this.redisService.zadd(DeerConstant.ACTIVE_USERS_ZSET_PREFIX, Double.valueOf(token.getExipreAt()), mapper.writeValueAsString(activeUser));
        // redis 中存储这个加密 token，key = 前缀 + 加密 token + .ip
        this.redisService.set(DeerConstant.TOKEN_CACHE_PREFIX + token.getToken() + StringPool.DOT + ip, token.getToken(), properties.getShiro().getJwtTimeOut() * 1000);

        return activeUser.getId();
    }

    /**
     * 生成前端需要的用户信息，包括：
     * 1. token
     * 2. Vue Router
     * 3. 用户角色
     * 4. 用户权限
     * 5. 前端系统个性化配置信息
     *
     * @param token token
     * @param user  用户信息
     * @return UserInfo
     */
    private Map<String, Object> generateUserInfo(JWTToken token, User user) {
        String username = user.getUsername();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("token", token.getToken());
        userInfo.put("exipreTime", token.getExipreAt());

        Set<String> roles = this.userManager.getUserRoles(username);
        userInfo.put("roles", roles);

        Set<String> permissions = this.userManager.getUserPermissions(username);
        userInfo.put("permissions", permissions);

        UserConfig userConfig = this.userManager.getUserConfig(String.valueOf(user.getUserId()));
        userInfo.put("config", userConfig);

        user.setPassword("it's a secret");
        userInfo.put("user", user);
        return userInfo;
    }

    @GetMapping("index/{username}")
    public DeerResponse index(@NotBlank(message = "{required}") @PathVariable String username) {
        Map<String, Object> data = new HashMap<>();
        // 获取系统访问记录
        Long totalVisitCount = loginLogMapper.findTotalVisitCount();
        data.put("totalVisitCount", totalVisitCount);
        Long todayVisitCount = loginLogMapper.findTodayVisitCount();
        data.put("todayVisitCount", todayVisitCount);
        Long todayIp = loginLogMapper.findTodayIp();
        data.put("todayIp", todayIp);
        // 获取近期系统访问记录
        List<Map<String, Object>> lastSevenVisitCount = loginLogMapper.findLastSevenDaysVisitCount(null);
        data.put("lastSevenVisitCount", lastSevenVisitCount);
        User param = new User();
        param.setUsername(username);
        List<Map<String, Object>> lastSevenUserVisitCount = loginLogMapper.findLastSevenDaysVisitCount(param);
        data.put("lastSevenUserVisitCount", lastSevenUserVisitCount);
        return new DeerResponse().data(data);
    }

    @GetMapping("logout/{id}")
    public void logout(@NotBlank(message = "{required}") @PathVariable String id) throws Exception {
        this.kickout(id);
    }

    @DeleteMapping("kickout/{id}")
    @RequiresPermissions("user:kickout")
    public void kickout(@NotBlank(message = "{required}") @PathVariable String id) throws Exception {
        String now = DateUtil.formatFullTime(LocalDateTime.now());
        Set<String> userOnlineStringSet = redisService.zrangeByScore(DeerConstant.ACTIVE_USERS_ZSET_PREFIX, now, "+inf");
        ActiveUser kickoutUser = null;
        String kickoutUserString = "";
        for (String userOnlineString : userOnlineStringSet) {
            ActiveUser activeUser = mapper.readValue(userOnlineString, ActiveUser.class);
            if (StringUtils.equals(activeUser.getId(), id)) {
                kickoutUser = activeUser;
                kickoutUserString = userOnlineString;
            }
        }
        if (kickoutUser != null && StringUtils.isNotBlank(kickoutUserString)) {
            // 删除 zset中的记录
            redisService.zrem(DeerConstant.ACTIVE_USERS_ZSET_PREFIX, kickoutUserString);
            // 删除对应的 token缓存
            redisService.del(DeerConstant.TOKEN_CACHE_PREFIX + kickoutUser.getToken() + "." + kickoutUser.getIp());
        }
    }
}
