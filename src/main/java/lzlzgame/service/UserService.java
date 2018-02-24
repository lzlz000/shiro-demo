package lzlzgame.service;

import lzlzgame.entity.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Service
public class UserService {
    private final int expire = 300 * 1000;//user的过期时间 此处300秒

    /**
     * 过期时间间隔
     * @return 过期时间间隔(ms)
     */
    public int getExpire(){
        return expire;
    }
    public User getSessionUser(HttpSession session){
        User user =(User)session.getAttribute("user");
        if(user != null&&user.getSaveTime()<= new Date().getTime()){//如果过期 则从session中删除用户
            session.removeAttribute("user");
            user = null;
        }
        if (user == null) {
            user = new User();//测试时User的作用仅仅是作为map的key所以new一个即可
            user.setId(session.getId());
            user.setSaveTime(new Date().getTime()+expire);//设置过期时间
            session.setAttribute("user",user);
        }

        return user;
    }
}
