package com.jimboss.deer.system.controller;

import com.jimboss.deer.common.domain.router.VueRouter;
import com.jimboss.deer.system.domain.Menu;
import com.jimboss.deer.system.manager.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;

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

    @GetMapping("/{username}")
    public ArrayList<VueRouter<Menu>> getUserRouters(@NotBlank(message = "{required}") @PathVariable String username) {
        return this.userManager.getUserRouters(username);
    }
}
