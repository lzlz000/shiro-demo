package lzlzgame.service.im;

import lzlzgame.entity.im.IMUser;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.UUID;

@Service
public class IMUserService {
    private final int expire = 600 * 1000;//user的过期时间 600s没有活动 频道中的user自动过期

    /**
     * 过期时间间隔
     * @return 过期时间间隔(ms)
     */
    public int getExpire(){
        return expire;
    }
    /**
     * 获取默认过期时间的IMUser
     * @param session httpSession
     */
    public IMUser getIMUser(HttpSession session){
        return getIMUser(session,this.expire);
    }

    /**
     * 获取指定过期时间的IMUser
     * @param session httpSession
     * @param expire 过期时间，<=0代表不会过期
     */
    private IMUser getIMUser(HttpSession session,int expire){
        IMUser user =(IMUser)session.getAttribute("imUser");
        if(user != null&&user.getSaveTime()<= new Date().getTime()){//如果过期 则从session中删除用户
            session.removeAttribute("imUser");
            user = null;
        }
        if (user == null) {
            user = new IMUser();//测试时User的作用仅仅是作为map的key所以new一个即可
            user.setId(UUID.randomUUID().toString());
            user.setSaveTime(new Date().getTime()+expire);//设置过期时间
            session.setAttribute("imUser",user);
        }
        user.setExpire(expire);
        return user;
    }

    public boolean isExpired(IMUser user) {
        return user.getExpire() > 0 && user.getSaveTime() <= new Date().getTime();
    }
}
