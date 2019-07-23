package com.jimboss.deer.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jimboss.deer.system.domain.LoginLog;

public interface LoginLogService extends IService<LoginLog> {

    void saveLoginLog (LoginLog loginLog);
}
