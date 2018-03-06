package lzlzgame.service;

import lzlzgame.entity.SecurityUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class LoginService {
    @Autowired
    SessionManager sessionManager;
    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 认证登录
     * 在redis中保存着hash值 key:LOGIN_USER  hashKey:userid value sessionId
     * 当相同用户登录时（即userid是同一个），判断是否为同一个session
     * 否则将原来的session中放入一个kickout参数，并且更新redis中的sessionId 为新的session
     */
    public void login(String username, String password){
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        SecurityUtils.getSubject().login(token);
        SecurityUser user =(SecurityUser)SecurityUtils.getSubject().getPrincipal();
        String id = String.valueOf(user.getId());
        Serializable sessionId = SecurityUtils.getSubject().getSession().getId();

        HashOperations<String,String,Serializable> redisHashOperations = redisTemplate.opsForHash();
        Serializable loginSessionId = redisHashOperations.get("LOGIN_USER",id);
        //如果存在已经登录的session并且不等于当前session
        if (loginSessionId != null&&!loginSessionId.equals(sessionId)) {
            //这里报错 SessionKey must be an HTTP compatible implementation. 不知道为什么
            SessionKey sessionKey = new DefaultSessionKey(loginSessionId);
            Session kickOutSession = sessionManager.getSession(sessionKey);
            if (kickOutSession != null) {
                kickOutSession.setAttribute("kickout",1);
            }
        }
        redisHashOperations.put("LOGIN_USER",id,sessionId);
    }
}
