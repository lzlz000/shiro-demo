package lzlz000.shirodemo.service;

import lzlz000.shirodemo.entity.MySecurityUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginService {
    @Autowired
    StringRedisTemplate redisTemplate;

    private static final String REDIS_KEY_PREFIX ="LOGIN_USER_";
    private static final String KICKOUT_TAG ="kickout";
    private static final String LOGIN_TAG ="login";


    public void login(String username, String password){
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        SecurityUtils.getSubject().login(token);
        MySecurityUser user =(MySecurityUser)SecurityUtils.getSubject().getPrincipal();
        singleLogin(String.valueOf(user.getId()));
    }
    /**
     * 单用户登录
     * 在redis中保存着hash值 key:LOGIN_USER+userId  hashKey:sessionId value 是否kickout
     * 当相同用户登录时（即userid是同一个），判断是否为同一个session
     * 否则将原来的session中放入一个kickout参数，并且更新redis中的sessionId 为新的session
     */
    private void singleLogin(String userId){
        Serializable sessionId = SecurityUtils.getSubject().getSession().getId();
        //从redis中获取相同用户的所有sessionId
        HashOperations<String,Serializable,String> hashOps = redisTemplate.opsForHash();
        String key = REDIS_KEY_PREFIX+userId;
        Map<Serializable,String> loginSessionIdMap = hashOps.entries(key);
        loginSessionIdMap.forEach((loginSessionId,isKickOut)->{
            //如果sessionId不是当前的Session的，加入kickout标志
            if (!loginSessionId.equals(sessionId)) {
                hashOps.put(key,loginSessionId,KICKOUT_TAG);
            }
        });
        hashOps.put(key,sessionId,LOGIN_TAG);
        redisTemplate.expire(key,60, TimeUnit.MINUTES);//设置过期时间60分钟
    }

    /**
     * 踢出redis中被标记kickout的session
     * @return 踢出了用户 返回true 否则返回false
     */
    public boolean kickout(){
        Subject subject = SecurityUtils.getSubject();
        MySecurityUser user =(MySecurityUser)subject.getPrincipal();
        String userId = String.valueOf(user.getId());
        Serializable sessionId = subject.getSession().getId();

        //如果需要踢出用户
        HashOperations<String,Serializable,String> hashOps = redisTemplate.opsForHash();
        String kickOut = hashOps.get(LoginService.REDIS_KEY_PREFIX+userId,sessionId);
        if(LoginService.KICKOUT_TAG.equals(kickOut)){
            subject.logout();
            hashOps.delete(LoginService.REDIS_KEY_PREFIX+userId,sessionId);
            return true;
        }
        return false;
    }

}
