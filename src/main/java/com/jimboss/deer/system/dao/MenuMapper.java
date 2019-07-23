package com.jimboss.deer.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jimboss.deer.system.domain.Menu;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> findUserPermissions(String userName);

}
