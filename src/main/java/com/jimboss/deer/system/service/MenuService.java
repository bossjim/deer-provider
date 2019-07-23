package com.jimboss.deer.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jimboss.deer.system.domain.Menu;

import java.util.List;

public interface MenuService extends IService<Menu> {

    List<Menu> findUserPermissions(String username);
}
