package lzlzgame.service;

import lzlzgame.entity.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class UserService {
    public User getSessionUser(HttpSession session){
        User user =(User)session.getAttribute("user");
        if (user == null) {
            user = new User();//测试时User的作用仅仅是作为map的key所以new一个即可
            session.setAttribute("user",user);
        }
        return user;
    }
}
