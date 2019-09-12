package com.jimboss.deer.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jimboss.deer.common.service.CacheService;
import com.jimboss.deer.system.dao.UserConfigMapper;
import com.jimboss.deer.system.domain.UserConfig;
import com.jimboss.deer.system.service.UserConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName UserConfigServiceImpl
 * @Description TODO
 * @Author jinyong
 * @DATE 2019/7/23 17:11
 * @Version 1.0
 **/
@Service("userConfigService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserConfigServiceImpl extends ServiceImpl<UserConfigMapper, UserConfig> implements UserConfigService {

    @Autowired
    private CacheService cacheService;

    @Override
    public UserConfig findByUserId(String userId) {
        return baseMapper.selectById(userId);
    }

    @Override
    @Transactional
    public void update(UserConfig userConfig) throws Exception {
        baseMapper.updateById(userConfig);
        cacheService.saveUserConfigs(String.valueOf(userConfig.getUserId()));
    }
}
