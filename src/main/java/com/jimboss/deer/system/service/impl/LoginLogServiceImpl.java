package com.jimboss.deer.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jimboss.deer.common.utils.AddressUtil;
import com.jimboss.deer.common.utils.HttpContextUtil;
import com.jimboss.deer.common.utils.IPUtil;
import com.jimboss.deer.system.dao.LoginLogMapper;
import com.jimboss.deer.system.domain.LoginLog;
import com.jimboss.deer.system.service.LoginLogService;
import org.lionsoul.ip2region.DbSearcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @ClassName LoginLogServiceImpl
 * @Description TODO
 * @Author jinyong
 * @DATE 2019/7/23 9:53
 * @Version 1.0
 **/
@Service("loginLogService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

    @Override
    @Transactional
    public void saveLoginLog(LoginLog loginLog) {
        loginLog.setLoginTime(new Date());
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String ip = IPUtil.getIpAddr(request);
        loginLog.setIp(ip);
        loginLog.setLocation(AddressUtil.getCityInfo(DbSearcher.BTREE_ALGORITHM, ip));
        this.save(loginLog);
    }
}
