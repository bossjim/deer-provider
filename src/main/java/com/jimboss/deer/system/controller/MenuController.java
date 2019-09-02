package com.jimboss.deer.system.controller;

import com.jimboss.deer.common.domain.DeerConstant;
import com.jimboss.deer.common.domain.router.VueRouter;
import com.jimboss.deer.common.service.RedisService;
import com.jimboss.deer.common.utils.DateUtil;
import com.jimboss.deer.system.domain.Menu;
import com.jimboss.deer.system.manager.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

/**
 * @ClassName MenuController
 * @Description TODO
 * @Author jinyong
 * @DATE 2019/7/18 12:08
 * @Version 1.0
 **/

@Slf4j
@Validated
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private UserManager userManager;

    @Autowired
    private RedisService redisService;

    @GetMapping("/{username}")
    public ArrayList<VueRouter<Menu>> getUserRouters(@NotBlank(message = "{required}") @PathVariable String username) {
        return this.userManager.getUserRouters(username);
    }

    @DeleteMapping("kickout/{id}")
    @RequiresPermissions("user:kickout")
    public void kickout(@NotBlank(message = "{required}") @PathVariable String id) throws Exception {
        String now = DateUtil.formatFullTime(LocalDateTime.now());
        Set<String> userOnlineStringSet = redisService.zrangeByScore(DeerConstant.ACTIVE_USERS_ZSET_PREFIX, now, "+inf");
    }
}
